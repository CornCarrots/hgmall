package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Carousel;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.dao.CarouselDAO;
import com.lh.hgmall.service.CarouselService;
import com.lh.hgmall.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@RestController
public class CarouselController {
    @Autowired
    CarouselService carouselService;

    @GetMapping(value = "/admin/images/carousels")
    public List<Carousel> list(@RequestParam("type")String type, HttpSession session)throws Exception
    {
        List<Carousel> carousels = null;
        if(type.equals(CarouselDAO.type_store))
            {
                Manager manager = (Manager) session.getAttribute("manager");
                carousels=carouselService.listBySid(manager.getSid());
            }
         else
            carousels= carouselService.listByType(type);
        return carousels;
    }

    @GetMapping(value = "/admin/images/carousels/{id}")
    public Carousel get(@PathVariable("id")int id)throws Exception
    {
        Carousel carousel = carouselService.get(id);
        return carousel;
    }

    @DeleteMapping(value = "/admin/images/carousels/{id}")
    public String delete(@PathVariable("id")int id)throws Exception
    {
        carouselService.delete(id);
        return null;
    }

    @PutMapping(value = "/admin/images/carousels/{id}")
    public void update(MultipartFile image,Carousel carousel,HttpServletRequest request)throws Exception
    {
        carouselService.update(carousel);
        if(image==null)
            return;
        int id = carousel.getId();
        File imageFolder= new File(request.getServletContext().getRealPath("image/carousel_home"));
        File file = new File(imageFolder,id+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @PostMapping(value = "/admin/images/carousels")
    public void add(MultipartFile image, Carousel carousel, HttpServletRequest request)throws Exception{
        carouselService.add(carousel);
        int id = carousel.getId();
        File imageFolder= new File(request.getServletContext().getRealPath("image/carousel_home"));
        File file = new File(imageFolder,id+".jpg");
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }



}
