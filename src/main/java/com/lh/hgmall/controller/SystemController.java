package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Log;
import com.lh.hgmall.bean.Option;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.service.LogService;
import com.lh.hgmall.service.OptionService;
import com.lh.hgmall.service.UserService;
import com.lh.hgmall.util.ImageUtil;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
public class SystemController {
    @Autowired
    OptionService optionService;
    @Autowired
    LogService logService;
    @Autowired
    UserService userService;

    @GetMapping(value = "/admin/system/logs")
    public PageUtil<Log> listLog(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception{
        PageUtil<Log> page =  logService.list(start,size,5);
        List<Log> logs = page.getContent();
        if(logs.size()!=0)
            logService.fillLog(logs);
        page.setContent(logs);
        return page;
    }

    @GetMapping(value = "/admin/system/options")
    public Option getOption()throws Exception
    {
        return optionService.get(1);
    }

    @PutMapping(value = "/admin/system/options/{id}")
    public void updateOption(MultipartFile image, Option option, HttpServletRequest request)throws Exception
    {
        String name = System.currentTimeMillis()+".jpg";
        option.setIcon(name);
        System.out.println(option);
        optionService.update(option);
        if(image!=null)
        {
            File imageFolder= new File(request.getServletContext().getRealPath("image/option"));
            File file = new File(imageFolder,name);
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }

    }
    @GetMapping(value = "/admin/system/logs/search")
    public List<Log> search(@RequestParam("key")String key)
    {
        User user = userService.get(key);
        if(user==null)
            return null;
        else
            return logService.listByUser(user.getId());
    }



}
