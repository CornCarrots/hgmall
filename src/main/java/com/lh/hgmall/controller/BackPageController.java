package com.lh.hgmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class BackPageController {
    @GetMapping(value="/admin")
    public String admin(){
        return "back/home";
    }



    @GetMapping(value="/admin/logout")
    public String logout(HttpSession session){
        session.removeAttribute("manager");
        return "redirect:/";
    }

    @GetMapping(value = "/admin/store/storeList")
    public String store(){return "back/store/listStore";}
    @GetMapping(value = "/admin/store/search")
    public String searchStore(){ return "back/store/searchStore";}
    @GetMapping(value = "/admin/store/storeInfo")
    public String storeInfo(){return "back/store/storeInfo";}

    @GetMapping(value = "/admin/category/categoryList")
    public String category(){return "back/category/listCategory";}
    @GetMapping(value = "/admin/category/search")
    public String searchCategory(){ return "back/category/searchCategory";}
    @GetMapping(value = "/admin/category/property")
    public String property(){return "back/category/listProperty";}

    @GetMapping(value = "/admin/product/productList")
    public String product(){return "back/product/listProduct";}
    @GetMapping(value = "/admin/product/productImage")
    public String productImage(){return "back/product/listProductImage";}
    @GetMapping(value = "/admin/product/propertyValue")
    public String propertyValue(){return "back/product/listPropertyValue";}

    @GetMapping(value = "/admin/image/carousel")
    public String carousel(){return "back/image/listCarousel";}
    @GetMapping(value = "/admin/image/smallCarousel")
    public String smallCarousel(){return "back/image/listSmallCarousel";}
    @GetMapping(value = "/admin/image/advertisement")
    public String advertisement(){return "back/image/listAdvertisement";}

    @GetMapping(value = "/admin/sale/saleList")
    public String saleList(){return "back/sale/saleList";}
    @GetMapping(value = "/admin/sale/map")
    public String saleMap(){return "back/sale/saleMap";}
    @GetMapping(value = "/admin/sale/order")
    public String order(){return "back/sale/listOrder";}
    @GetMapping(value = "/admin/sale/orderItem")
    public String orderItem(){return "back/sale/listOrderItem";}
    @GetMapping(value = "/admin/sale/orderItem_exchange")
    public String orderItem_exchange(){return "back/sale/listOrderItem_Exchange";}
    @GetMapping(value = "/admin/sale/exchange")
    public String exchange(){return "back/sale/listExchange";}

    @GetMapping(value = "/admin/user/userList")
    public String user(){return "back/user/listUser";}
    @GetMapping(value = "/admin/user/member")
    public String member(){return "back/user/listMember";}
    @GetMapping(value = "/admin/user/score")
    public String score(){return "back/user/listScore";}
    @GetMapping(value = "/admin/user/power")
    public String power(){return "back/user/listPower";}

    @GetMapping(value = "/admin/message/messageList")
    public String message(){return "back/message/listMessage";}
    @GetMapping(value = "/admin/message/view")
    public String view(){return "back/message/listView";}


    @GetMapping(value = "/admin/article/articleList")
    public String article(){return "back/article/listArticle";}
    @GetMapping(value = "/admin/article/category")
    public String article_category(){return "back/article/listCategory";}

    @GetMapping(value = "/admin/system/systemList")
    public String option(){return  "back/system/listOption";}
    @GetMapping(value = "/admin/system/module")
    public String module(){return  "back/system/listModule";}
    @GetMapping(value = "/admin/system/operation")
    public String operation(){return  "back/system/listOperation";}

    @GetMapping(value = "/admin/manager/managerList")
    public String manager(){return  "back/manager/listManager";}
    @GetMapping(value = "/admin/manager/role")
    public String role(){return  "back/manager/listRole";}
    @GetMapping(value = "/admin/manager/permission")
    public String permission(){return  "back/manager/listPermission";}
    @GetMapping(value = "/admin/manager/person")
    public String person(){return  "back/manager/listPerson";}


}
