package com.lh.hgmall.controller;

import com.lh.hgmall.bean.ProductImage;
import com.lh.hgmall.dao.ProductImageDAO;
import com.lh.hgmall.service.ProductImageService;
import com.lh.hgmall.service.ProductService;
import com.lh.hgmall.util.ImageUtil;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductImageController {
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ProductService productService;

    @GetMapping(value = "admin/categories/{cid}/products/{pid}/productImages")
    public Map<String, List<ProductImage>> list(@PathVariable("pid") int pid) throws Exception {
        List<ProductImage> singles = productImageService.listByPidAndType(pid, ProductImageDAO.type_single);
        List<ProductImage> details = productImageService.listByPidAndType(pid, ProductImageDAO.type_detail);
        productImageService.fill(singles);
        productImageService.fill(details);
        Map<String, List<ProductImage>> map = new HashMap<>();
        map.put("singles", singles);
        map.put("details", details);
        return map;
    }

    @PostMapping(value = "admin/categories/{cid}/products/{pid}/productImages")
    public void add(ProductImage productImage, MultipartFile image, HttpServletRequest request) throws Exception {
        System.out.println(productImage);
        productImageService.add(productImage);
        File imageFolder = null;
        File imageFolder_middle = null;
        File imageFolder_small = null;
        String fileName = productImage.getId() + ".jpg";
        if (productImage.getType().equals(ProductImageDAO.type_single)) {
            imageFolder = new File(request.getServletContext().getRealPath("image/productSingle"));
            imageFolder_middle = new File(request.getServletContext().getRealPath("image/productSingle_middle"));
            imageFolder_small = new File(request.getServletContext().getRealPath("image/productSingle_small"));
        }
        else
            imageFolder = new File(request.getServletContext().getRealPath("image/productDetail"));
        File file = new File(imageFolder, fileName);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        image.transferTo(file);
        BufferedImage img = ImageUtil.change2jpg(file);
        ImageIO.write(img, "jpg", file);
        if(productImage.getType().equals(ProductImageDAO.type_single))
        {
            File desc_small = new File(imageFolder_small,fileName);
            File desc_middle = new File(imageFolder_middle,fileName);
            ImageUtil.resizeImage(file, 56, 56, desc_small);
            ImageUtil.resizeImage(file, 217, 190, desc_middle);
        }
    }

    @DeleteMapping(value = "admin/categories/{cid}/products/{pid}/productImages/{id}")
    public String delete(@PathVariable("id")int id,HttpServletRequest request) {
        ProductImage image = productImageService.get(id);
        productImageService.delete(id);
        File imageFolder = null;
        File imageFolder_middle = null;
        File imageFolder_small = null;
        String fileName = id + ".jpg";
        if (image.getType().equals(ProductImageDAO.type_single)) {
            imageFolder = new File(request.getServletContext().getRealPath("image/productSingle"));

        }
        else
            imageFolder = new File(request.getServletContext().getRealPath("image/productDetail"));
        File file = new File(imageFolder, fileName);
        file.delete();
        if (image.getType().equals(ProductImageDAO.type_single)){
            imageFolder_middle = new File(request.getServletContext().getRealPath("image/productSingle_middle"));
            imageFolder_small = new File(request.getServletContext().getRealPath("image/productSingle_small"));
            File file1 = new File(imageFolder_middle, fileName);
            File file2 = new File(imageFolder_small, fileName);
            file1.delete();
            file2.delete();
        }
            return null;
    }


}
