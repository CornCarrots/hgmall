package com.lh.hgmall.controller;

import com.alibaba.fastjson.JSONObject;
import com.lh.hgmall.bean.Article;
import com.lh.hgmall.bean.Category;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.service.ArticleService;
import com.lh.hgmall.service.CategoryService;
import com.lh.hgmall.util.CalendarRandomUtil;
import com.lh.hgmall.util.ImageUtil;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;


    @GetMapping(value = "/admin/articles")
    public Map<String,Object> listArticle(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception{
        start = start<0?0:start;
        PageUtil<Article> page =articleService.listByStatus(start, size, 5, 2);
        List<Article> articles = page.getContent();
        articleService.fillCategory(articles);
        page.setContent(articles);
        List<Category> categories =categoryService.listByTypeAndSid(CategoryDAO.type_article,0);
        Map<String,Object> map = new HashMap<>();
        map.put("page",page);
        map.put("categories",categories);
        return map;
    }

    @PostMapping(value = "/admin/articles")
    public void addArticle(@RequestBody Article article)throws Exception
    {
        article.setCreateDate(new Date());
        articleService.add(article);
    }

    @PostMapping(value = "/admin/articles/image")
    public String upload(MultipartFile image, HttpServletRequest request)throws Exception
    {
        String now = CalendarRandomUtil.getRandom();
        File imageFolder= new File(request.getServletContext().getRealPath("image/article"));
        File file = new File(imageFolder,now+".jpg");
        String newFileName = request.getContextPath()+"/image/article/"+file.getName();
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


    @GetMapping(value = "/admin/articles/{id}")
    public Article getArticle(@PathVariable("id")int id)
    {
        Article article =  articleService.get(id);
        articleService.fillCategory(article);
        return article;
    }

    @PutMapping(value = "/admin/articles/{id}")
    public void updateArticle(@PathVariable("id")int id,@RequestBody Article article)
    {
        articleService.update(article);
    }

    @DeleteMapping(value = "/admin/articles/{id}")
    public String deleteArticle(@PathVariable("id")int id)
    {
        articleService.delete(id);
        return null;
    }

    @PostMapping(value = "/admin/articles/search")
    public List<Article> search(@RequestParam("key")String key)
    {
        List<Article> articles = articleService.listByStatusAndKey(2, key);
        articleService.fillCategory(articles);
        return articles;
    }

    @GetMapping(value = "/admin/articles/categories")
    public PageUtil<Category> listArticleCategory(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception
    {
        start = start < 0 ? 0 : start;
        PageUtil<Category> page =categoryService.listByTypeAndSid(start, size, 5,CategoryDAO.type_article,0);

        return page;
    }
    @GetMapping(value = "/admin/articles/categories/{id}")
    public Category get(@PathVariable("id")int id) throws Exception
    {
        Category category =  categoryService.get(id);
        return category;
    }

    @PostMapping(value = "/admin/articles/categories")
    public void add(@RequestBody Category category) throws Exception
    {
        categoryService.add(category);
    }

    @DeleteMapping(value = "/admin/articles/categories/{id}")
    public String delete(@PathVariable("id") int id) throws Exception
    {
        categoryService.delete(id);
        return null;
    }

    @PutMapping(value = "/admin/articles/categories/{id}")
    public void update(@RequestBody Category category) throws Exception
    {
        categoryService.update(category);
    }

    @PostMapping(value = "/admin/articles/categories/search")
    public List<Category> searchCategory(@RequestParam(value = "key") String key, HttpSession session) throws Exception
    {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Category> categories =  categoryService.listByTypeAndKeyAndSid(CategoryDAO.type_article,key,manager.getSid());
        return categories;
    }



    }
