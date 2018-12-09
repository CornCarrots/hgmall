package com.lh.hgmall.service;

import com.lh.hgmall.bean.*;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.util.Log4jUtil;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import com.lh.hgmall.util.SpringContextUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;
    @Autowired
    StoreService storeService;
    @Autowired
    ProductService productService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    ArticleService articleService;

    Logger logger =  Logger.getLogger(CategoryService.class);


    @Cacheable(keyGenerator = "wiselyKeyGenerator")
    public List<Category> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAll(sort);
    }

    @Cacheable(keyGenerator = "wiselyKeyGenerator")
    public List<Category> listByType(String type) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAllByType(type, sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Category> listByTypeAndSid(String type, int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAllByTypeAndSid(type, sid, sort);
    }

        @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Category> listByTypeAndSidNot(String type, int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAllByTypeAndSid(type, sid, sort);
    }

    @Cacheable(keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Category> listByTypeAndSid(int start, int size, int number, String type, int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page page = categoryDAO.findAllByTypeAndSid(type, sid, pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());
        return new PageUtil<Category>(page, number);
    }

    public List<Category> listByTypeAndKeyAndSid(String type, String key, int sid) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return categoryDAO.findAllByTypeAndNameContainingAndSid(type, key, sid, sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Category category) {
        categoryDAO.save(category);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id) {
        categoryDAO.delete(id);
        propertyService.deleteAllByCategory(id);
        productService.deleteAllByCategory(id);
    }

    @CacheEvict(allEntries = true)
    public void update(Category category) {
        categoryDAO.save(category);
    }

    @Cacheable(keyGenerator = "wiselyKeyGenerator")
    public Category get(int id) {
        return categoryDAO.findOne(id);
    }

    public void fillStore(Category category) {
        int sid = category.getSid();
        if (sid == 0) {
            List<Store> stores = storeService.listByStatusAndCategory(3, category.getId());
            category.setStores(stores);
            storeService.fill(stores);
            for (Store store :
                    stores) {
                List<Category> categories = store.getCategories();
                for (Category category1 :
                        categories) {
                    category1.setStore(store);
                }
            }
        } else {
            Store store = storeService.get(sid);
            category.setStore(store);
        }

    }

    public void fillStore(List<Category> categories) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);

        for (Category category :
                categories) {
            categoryService.fillStore(category);
        }
    }

    public void fillProduct(Category category) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);
        int cid = category.getId();
        List<Product> products = new ArrayList<>();
        if (category.getSid() == 0) {
            categoryService.fillStore(category);
            for (Store store :
                    category.getStores()) {
                for (Category category1 :
                        store.getCategories()) {
                    List<Product> products1 = productService.listByCidAndStatus(category1.getId(), 0);
                    if (products1.size() != 0) {
                        productService.fillFirst(products1);
                        productService.fillCategoryAndStore(products1);
                        products.addAll(products1);
                    }

                }
            }
            category.setProducts(products);
        } else {
            products = productService.listByCidAndStatus(category.getId(), 0);
            if (products.size() != 0) {
                int sid = category.getSid();
                for (Product product :
                        products) {
                    product.setStore(storeService.get(sid));
                }
                category.setProducts(products);
                List<Product> show = new ArrayList<>();
                int target = 6;
                List<Product> products1 = products.subList(0, products.size() > target ? target : products.size());
                productService.fillFirst(products1);
                show.addAll(products1);
                category.setProductShow(show);
            }

        }
    }

    public void fillProduct(List<Category> categories) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);

        for (Category category :
                categories) {
            categoryService.fillProduct(category);
        }
    }

    public void fillRow(Category category) {
        List<List<Product>> rows = new ArrayList<>();
        List<Product> show = new ArrayList<>();
        int target = 6;
        List<Store> stores = storeService.listByStatusAndCategory(3, category.getId());
        storeService.fill(stores);
        for (Store store :
                stores) {
            for (Category category1 :
                    store.getCategories()) {
                List<Product> row = new ArrayList<>();
                List<Product> products = productService.listByCidAndStatus(category1.getId(), 0);
                for (int i = 0; i < products.size(); i = i + target) {
                    row.addAll(products.subList(i, target < products.size() - i ? i + target : products.size()));
                }
                rows.add(row);
                int sum = show.size();
                if (sum < target) {
                    List<Product> products1 = products.subList(0, products.size() + sum > target ? target - sum : products.size());
                    productService.fillFirst(products1);
                    show.addAll(products1);

                }
            }
        }
        category.setProductRows(rows);
        category.setProductShow(show);
    }

    public void fillRow(List<Category> categories) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);

        for (Category category :
                categories) {
            categoryService.fillRow(category);
        }
    }

    public void fillImg(Category category) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);
        if (category.getSid() != 0)
            category.setImgID(category.getId());
        else {
            if (category.getStores() == null)
                categoryService.fillStore(category);
            int x = (int) (Math.random() * category.getStores().size());
            Store store = category.getStores().get(x);
            storeService.fill(store);
            category.setImgID(store.getCategories().get(0).getId());
        }
    }

    public void fillArticle(Category category) {
        List<Article> articles = articleService.listBySuccessAndCategory(0, category.getId());
        if (articles.size() != 0)
            category.setArticles(articles);
    }


    public void fillArticle(List<Category> categories) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);

        for (Category category :
                categories) {
            categoryService.fillArticle(category);
        }
    }

    public List<Map<String, Object>> listForTree(int sid) {
        CategoryService categoryService = SpringContextUtils.getBean(CategoryService.class);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        if (sid == 0)
            categories = categoryService.listByType(CategoryDAO.type_product);
        else {
            categories = categoryService.listByTypeAndSid(CategoryDAO.type_product, sid);
            categories.add(get(sid));
        }
//        System.out.println(categories);
        int i = 0;
        int pid = -1;
        boolean found = false;
        for (Category category :
                categories) {
            categoryService.fillStore(category);
            Map<String, Object> map = new HashMap<>();
            if (category.getSid() != 0)
                map.put("parent_id", category.getStore().getCid());
            else
                map.put("parent_id", 0);
            map.put("layer_name", category.getName());
            map.put("EquIndex", i);
            map.put("id", category.getId());
            Map<String, Object> state = new HashMap<>();
            if (category.getSid() == 0)
                map.put("selectable", false);
            else
                map.put("selectable", true);
            if (!found) {
                if (category.getSid() != 0) {
                    pid = category.getStore().getCid();
                    state.put("selected", true);
                    state.put("expanded", true);
                    found = true;

                }
            } else {
                if (category.getId() == pid) {
                    pid = category.getSid();
                    state.put("expanded", true);
                } else {
                    state.put("selected", false);
                    state.put("expanded", false);
                }
            }

            map.put("state", state);
            list.add(map);
            i++;
        }
        return list;
    }


}