package com.lh.hgmall.interceptor;

import com.lh.hgmall.bean.*;
import com.lh.hgmall.dao.AdvertisementDAO;
import com.lh.hgmall.dao.CategoryDAO;
import com.lh.hgmall.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ForeInterceptor implements HandlerInterceptor {
    @Autowired
    AdvertisementService advertisementService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    OptionService optionService;
    @Autowired
    UserService userService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    MemberService memberService;

    Logger logger = Logger.getLogger(ForeInterceptor.class);
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        logger.info(httpServletRequest.getRequestURL());
        ServletContext context = httpServletRequest.getServletContext();
        List<Advertisement> advertisements = advertisementService.listByType(AdvertisementDAO.type_home);
        List<Category> categories = categoryService.listByTypeAndSid(CategoryDAO.type_product, 0);
        List<Category> subCategories = categoryService.listByTypeAndSidNot(CategoryDAO.type_product,0);
        Option option = optionService.get(1);
        context.setAttribute("advertisements", advertisements);
        context.setAttribute("categories", categories);
        context.setAttribute("categories_below", subCategories.subList(0, 4));
        context.setAttribute("right", option.getRight());
        HttpSession session = httpServletRequest.getSession();
        int shoppingCart = 0;
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Member> members = memberService.list();
            for (Member member:
                 members) {
                if(member.getMin()<=user.getScore()&&member.getMax()>user.getScore())
                {
                    user.setMid(member.getId());
                    userService.update(user);
                    break;
                }
            }
            List<OrderItem> items = orderItemService.listByUserAndOrder(user.getId(),0);
            shoppingCart = items.size();
            if (user.getMember() == null) {
                userService.fillMember(user);
                session.setAttribute("user", user);
            }
        }
        session.setAttribute("shoppingCart", shoppingCart);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
