package com.lh.hgmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.lh.hgmall.bean.Power;
import com.lh.hgmall.service.PowerService;
import com.lh.hgmall.util.ImageUtil;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PowerController {
    @Autowired
    PowerService powerService;

    @GetMapping(value = "/admin/scores/{mid}/powers")
    public PageUtil<Power> listPower(@PathVariable("mid")int mid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception{
        start = start<0?0:start;
        PageUtil<Power> page =powerService.listByMember(start, size, 5, mid);
        return page;
    }

    @PostMapping(value = "/admin/scores/{mid}/powers")
    public void addPower(MultipartFile image, Power power, HttpServletRequest request)throws Exception
    {
        powerService.add(power);
        if(image==null)
            return;
        int id = power.getId();
        File imageFolder= new File(request.getServletContext().getRealPath("image/power"));
        File file = new File(imageFolder,id+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }
    @PostMapping(value = "/admin/scores/{mid}/powers/search")
    public List<Power> search(@RequestParam("key")String key,@PathVariable("mid")int mid)
    {
        List<Power> articles = powerService.listByMemberAndKey(mid, key);
        return articles;
    }

    @DeleteMapping(value = "/admin/scores/{mid}/powers/{id}")
    public String deletePower(@PathVariable("id")int id,HttpServletRequest request)throws Exception
    {
        powerService.delete(id);
        File imageFolder= new File(request.getServletContext().getRealPath("image/power"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }

    @GetMapping(value = "/admin/scores/{mid}/powers/{id}")
    public Power getPower(@PathVariable("id")int id)
    {
        Power power =  powerService.get(id);
        return power;
    }
    @PutMapping(value = "/admin/scores/{mid}/powers/{id}")
    public void updatePower(MultipartFile image, Power power, HttpServletRequest request)throws Exception
    {
        powerService.update(power);
        if(image==null)
            return;
        int id = power.getId();
        File imageFolder= new File(request.getServletContext().getRealPath("image/power"));
        File file = new File(imageFolder,id+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @PostMapping(value = "/admin/scores/{mid}/powers/image")
    public String upload(MultipartFile image, HttpServletRequest request)throws Exception
    {
        Calendar calCurrent = Calendar.getInstance();
        int intDay = calCurrent.get(Calendar.DATE);
        int intMonth = calCurrent.get(Calendar.MONTH) + 1;
        int intYear = calCurrent.get(Calendar.YEAR);
        String now = String.valueOf(intYear) + "-" + String.valueOf(intMonth) + "-" +
                String.valueOf(intDay) + "_";
        now +=System.currentTimeMillis();
        File imageFolder= new File(request.getServletContext().getRealPath("image/power_article"));
        File file = new File(imageFolder,now+".jpg");
        String newFileName = request.getContextPath()+"/image/power_article/"+file.getName();
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        JSONObject obj = new JSONObject();
        obj.put("error", 0);
        obj.put("url", newFileName);
        return obj.toString();
    }
}
