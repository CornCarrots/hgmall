package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Category;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.service.CategoryService;
import com.lh.hgmall.util.ImageUtil;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "/admin/categories")
    public PageUtil<Category> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size, HttpSession session) throws Exception{
        Manager manager = (Manager) session.getAttribute("manager");
        start = start<0?0:start;
        PageUtil<Category> page =categoryService.listByTypeAndSid(start, size, 5,CategoryDAO.type_product,manager.getSid());
        List<Category> categories = page.getContent();
        categoryService.fillStore(categories);
        page.setContent(categories);
        return page;
    }

    @GetMapping(value = "/admin/categories/{id}")
    public Category get(@PathVariable("id")int id) throws Exception
    {
        Category category =  categoryService.get(id);
        categoryService.fillStore(category);
        return category;
    }

    @PostMapping(value = "/admin/categories")
    public String add(MultipartFile image,Category category, HttpServletRequest request,HttpSession session) throws Exception
    {
        Manager manager = (Manager) session.getAttribute("manager");
        category.setSid(manager.getSid());
        categoryService.add(category);
        if(image!=null)
        {
            int id = category.getId();
            File imageFolder= new File(request.getServletContext().getRealPath("image/store"));
            File file = new File(imageFolder,id+".jpg");
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }

        return "ok";
    }

    @DeleteMapping(value = "/admin/categories/{id}")
    public String delete(@PathVariable("id") int id,HttpServletRequest request) throws Exception
    {
        categoryService.delete(id);
        File imageFolder= new File(request.getServletContext().getRealPath("image/store"));
        File file = new File(imageFolder,id+".jpg");
        file.delete();
        return null;
    }

    @PutMapping(value = "/admin/categories/{id}")
    public void update(MultipartFile image, Category category,HttpServletRequest request) throws Exception
    {
        categoryService.update(category);
        if(image!=null)
        {
            int id = category.getId();
            File imageFolder= new File(request.getServletContext().getRealPath("image/store"));
            File file = new File(imageFolder,id+".jpg");
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            image.transferTo(file);
            BufferedImage img = ImageUtil.change2jpg(file);
            ImageIO.write(img, "jpg", file);
        }

    }

    @PostMapping(value = "/admin/categories/search")
    public List<Category> search(HttpSession session,@RequestParam(value = "key") String key) throws Exception
    {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Category> categories =  categoryService.listByTypeAndKeyAndSid(CategoryDAO.type_product,key,manager.getSid());
        categoryService.fillStore(categories);
        return categories;
    }


    @GetMapping(value = "/admin/categories/parent")
    public List<Category> listParent()
    {
        return categoryService.listByTypeAndSid(CategoryDAO.type_product,0);
    }

    @PostMapping(value = "admin/categories/tree")
    public Object tree(HttpSession session) throws Exception
    {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Map<String, Object>> categories = categoryService.listForTree(manager.getSid());
        List<Map<String, Object>> resultList = TreeUtil.treeViewDataTransform(categories);
        return resultList;
    }
}
