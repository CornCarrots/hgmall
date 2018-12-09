package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Advertisement;
import com.lh.hgmall.dao.AdvertisementDAO;
import com.lh.hgmall.service.AdvertisementService;
import com.lh.hgmall.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@RestController
public class AdvertisementController {
    @Autowired
    AdvertisementService advertisementService;

    @GetMapping(value = "/admin/images/advertisements")
    public List<Advertisement> list() {
        return advertisementService.list();
    }

    @PostMapping(value = "/admin/images/advertisements")
    public void add(MultipartFile image, Advertisement advertisement, HttpServletRequest request) throws Exception {
        advertisementService.add(advertisement);
        int id = advertisement.getId();
        String type = advertisement.getType();
        File imageFolder = null;
        if (type.equals(AdvertisementDAO.type_home))
            imageFolder = new File(request.getServletContext().getRealPath("image/advertisement_home"));
        if (type.equals(AdvertisementDAO.type_category))
            imageFolder = new File(request.getServletContext().getRealPath("image/advertisement_category"));
        if (type.equals(AdvertisementDAO.type_login))
            imageFolder = new File(request.getServletContext().getRealPath("image/advertisement_login"));
        if (type.equals(AdvertisementDAO.type_loginStore))
            imageFolder = new File(request.getServletContext().getRealPath("image/advertisement_loginStore"));
        File file = new File(imageFolder, id + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
    }

    @PutMapping(value = "/admin/images/advertisements/{id}")
    public void update(MultipartFile image, Advertisement advertisement, HttpServletRequest request) throws Exception {
        String old = advertisementService.get(advertisement.getId()).getType();
        int id = advertisement.getId();
        String type = advertisement.getType();
        advertisementService.update(advertisement);
        System.out.println(old+" "+type);
        String newFolder = "image/advertisement_" + type.substring(type.lastIndexOf("_")+1);;
            if (!old.equals(type))
            {
                String oldFolder = "image/advertisement_" + old.substring(old.lastIndexOf("_")+1);
                File image1 = new File(request.getServletContext().getRealPath(oldFolder), id + ".jpg");
                File image2 = new File(request.getServletContext().getRealPath(newFolder), id + ".jpg");
//                System.out.println(image1.getPath());
//                System.out.println(image2.getPath());
                FileCopyUtils.copy(image1, image2);
                image1.delete();
            }

//                type1 = type1.substring(type1.indexOf("_")+1);
//                String type2 = "category";
//                String type3 = "login";
//                String type4 = "loginStore";
//                if (type1.equals(type2))
//                {
//                    temp = type1;type1 = type2;type2 = temp;
//                }
//                else if(type1.equals(type3))
//                {
//                    temp = type1;type1 = type3;type3 = temp;
//                }
//                else if(type1.equals(type4))
//                {
//                    temp = type1;type1 = type4;type4 = temp;
//                }
//                System.out.println(type1+" "+type2);

        if (image == null) {
            return;
        }
        File file = new File(newFolder, id + ".jpg");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);

    }

    @GetMapping(value = "/admin/images/advertisements/{id}")
    public Advertisement get(@PathVariable("id") int id) {
        return advertisementService.get(id);
    }

    @DeleteMapping(value = "/admin/images/advertisements/{id}")
    public String delete(@PathVariable("id") int id, HttpServletRequest request) {
        Advertisement advertisement = advertisementService.get(id);
        String type = advertisement.getType();
        String newFolder = "image/advertisement_" + type.substring(type.lastIndexOf("_")+1);
        File file = new File(request.getServletContext().getRealPath(newFolder) , id + ".jpg");
        file.delete();
        advertisementService.delete(id);
        return null;
    }


}
