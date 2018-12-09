package com.lh.hgmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ForePageController {

    @GetMapping(value="/unauthorized")
    public String unauthorized(){
        return "exception/unauthorized";
    }

    @GetMapping(value="/error")
    public String error(){
        return "exception/404";
    }

    @GetMapping(value="/")
    public String index(){
        return "fore/home";
    }

    @GetMapping(value="/home")
    public String home(){
        return "fore/home";
    }


    @GetMapping(value="/search")
    public String search(){
        return "fore/search";
    }

    @GetMapping(value="/category")
    public String category(){
        return "fore/category";
    }

    @GetMapping(value="/product")
    public String product(){
        return "fore/product";
    }

    @GetMapping(value="/store")
    public String store(){
        return "fore/store";
    }

    @GetMapping(value="/login")
    public String login(){
        return "fore/login";
    }

    @GetMapping(value="/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping(value="/register")
    public String register(){
        return "fore/register";
    }

    @GetMapping(value="/registerSuccess")
    public String registerSuccess(){ return "fore/registerSuccess"; }

    @GetMapping(value="/user")
    public String user(){
        return "fore/user";
    }

    @GetMapping(value="/account")
    public String account(){ return "fore/account"; }

    @GetMapping(value="/orderList")
    public String orderList(){ return "fore/orderList"; }

    @GetMapping(value="/shoppingcart")
    public String shoppingCart(){
        return "fore/shoppingCart";
    }

    @GetMapping(value="/help")
    public String help(){
        return "fore/help";
    }

    @GetMapping(value="/message")
    public String message(){
        return "fore/message";
    }

    @GetMapping(value="/view")
    public String view(){
        return "fore/view";
    }

    @GetMapping(value="/member")
    public String member(){
        return "fore/member";
    }

    @GetMapping(value="/power")
    public String power(){
        return "fore/power";
    }

    @GetMapping(value="/manager")
    public String manager(){ return "fore/manager"; }

    @GetMapping(value="/store_login")
    public String store_login(){ return "fore/store_login"; }

    @GetMapping(value="/store_apply")
    public String store_apply(){ return "fore/store_apply"; }

    @GetMapping(value="/store_applySuccess")
    public String store_applySuccess(){ return "fore/store_applySuccess"; }

    @GetMapping(value="/orderItem")
    public String orderItem(){ return "fore/orderItem"; }

    @GetMapping(value="/order_pay")
    public String order_pay(){ return "fore/order_pay"; }

    @GetMapping(value="/order_confirm")
    public String order_confirm(){ return "fore/order_confirm"; }

    @GetMapping(value="/order_review")
    public String order_review(){ return "fore/review"; }

    @GetMapping(value="/waitSuccess")
    public String waitSuccess(){ return "fore/waitSuccess"; }

    @GetMapping(value="/paySuccess")
    public String paySuccess(){ return "fore/paySuccess"; }

    @GetMapping(value="/confirmSuccess")
    public String confirmSuccess(){ return "fore/confirmSuccess"; }

    @GetMapping(value="/reviewSuccess")
    public String reviewSuccess(){ return "fore/reviewSuccess"; }

}
