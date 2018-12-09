package com.lh.hgmall.filter;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.lh.hgmall.bean.Log;
import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.service.LogService;
import com.lh.hgmall.service.PermissionService;
import com.lh.hgmall.util.SpringContextUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.filter.authz.HttpMethodPermissionFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
public class URLPathMatchingFilter extends HttpMethodPermissionFilter {

    @Autowired
    PermissionService permissionService;

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        if(null==permissionService)
            permissionService = SpringContextUtils.getBean(PermissionService.class);

        String requestURI = getPathWithinApplication(request);
        String requestMethod = getHttpMethodAction(request);

        Subject subject = SecurityUtils.getSubject();
        // 如果没有登录，就跳转到登录页面
        if (!subject.isAuthenticated()) {
            WebUtils.issueRedirect(request, response, "/store_login");
            return false;
        }
        System.out.println("登录成功");

        // 看看这个路径权限里有没有维护，如果没有维护，一律放行(也可以改为一律不放行)
        boolean needInterceptor = permissionService.needInterceptor(requestURI);
        if (!needInterceptor) {
            System.out.println("没有维护");
            return true;
        } else {
            Manager manager = (Manager) subject.getSession().getAttribute("manager");
            boolean hasPermission = permissionService.hasPermission(subject.getPrincipal().toString(), requestURI, requestMethod,manager);

            if (hasPermission)
            {
                System.out.println(requestURI+"-"+requestMethod);
                return true;
            }
            else {
                UnauthorizedException ex = new UnauthorizedException("当前用户没有访问路径 " + requestURI + " 的权限");

                subject.getSession().setAttribute("ex", ex);

                WebUtils.issueRedirect(request, response, "/unauthorized");
                return false;
            }

        }

    }
}
