package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Product;
import com.lh.hgmall.service.CategoryService;
import com.lh.hgmall.service.ProductService;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    @GetMapping(value = "admin/categories/{cid}/products")
    public PageUtil<Product> list(@PathVariable("cid")int cid, @RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "9")int size) throws Exception
    {
        start = start<0?0:start;
        PageUtil<Product> page =productService.listByCidAndStatus(start, size, 5,cid,0);
        List<Product> products = page.getContent();
        productService.fillFirst(products);
        page.setContent(products);
        return page;
    }

    @PostMapping(value = "admin/categories/{cid}/products")
    public void add(@RequestBody Product product)
    {
        product.setCreateDate(new Date());
        productService.add(product);
    }

    @DeleteMapping(value = "admin/categories/{cid}/products/{id}")
    public String delete(@PathVariable("id")int id)
    {
        Product product = productService.get(id);
        product.setStatus(1);
        productService.update(product);
        return null;
    }

    @GetMapping(value = "admin/categories/{cid}/products/{id}")
    public Product get(@PathVariable("id")int id)
    {
        return productService.get(id);
    }

    @PutMapping(value = "admin/categories/{cid}/products/{id}")
    public void update(@RequestBody Product product)
    {
        productService.update(product);
    }

    @PostMapping(value = "admin/categories/{cid}/products/search")
    public List<Product> search(@RequestParam("key")String key,@PathVariable("cid")int cid)
    {
        List<Product> products =  productService.listByCidAndKeyAndStatus(cid,0,key);
        productService.fillFirst(products);
        return products;
    }


    @PostMapping(value = "admin/categories/products/tree")
    public Object tree(HttpSession session) throws Exception{
        Manager manager = (Manager) session.getAttribute("manager");
        List<Map<String, Object>> products = productService.listForTree(manager.getSid());
        List<Map<String, Object>> resultList = TreeUtil.treeViewDataTransform(products);
        return resultList;
    }


}
