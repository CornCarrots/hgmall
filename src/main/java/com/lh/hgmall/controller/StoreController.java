package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Store;
import com.lh.hgmall.service.MailService;
import com.lh.hgmall.service.ManagerRoleService;
import com.lh.hgmall.service.ManagerService;
import com.lh.hgmall.service.StoreService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
public class StoreController {
    @Autowired
    StoreService storeService;
    @Autowired
    ManagerService managerService;
    @Autowired
    MailService mailService;
    @Autowired
    ManagerRoleService managerRoleService;

    @GetMapping(value = "/admin/stores")
    public PageUtil<Store> list(@RequestParam(value = "start",defaultValue = "0")int start, @RequestParam(value = "size",defaultValue = "7")int size)throws Exception
    {
        start = start<0?0:start;
        PageUtil<Store> list = storeService.listByStatus(start,size,5,3);
        List<Store> stores = list.getContent();
        storeService.fill(stores);
        list.setContent(stores);
        return list;
    }

    @GetMapping(value = "/admin/stores/{id}")
    public Store get(@PathVariable("id")int id)throws Exception
    {
        Store store = storeService.get(id);
        storeService.fill(store);
        return store;
    }

    @PutMapping(value = "/admin/stores/{id}")
    public String update(@PathVariable("id")int id, @RequestBody Store store, HttpServletRequest request) throws Exception {
        int status = store.getStatus();
        Store store1 = storeService.get(id);
        store1.setStatus(status);
        store1.setAddDate(new Date());
        storeService.update(store1);
        if(status==3)
        {
            for (Manager manager:
            managerService.listByStore(id)) {
                managerRoleService.deleteByManager(manager.getId());
                managerService.delete(manager.getId());
            }
        }
        if(status==0)
        {
            if(managerService.listByStore(id).size()==0)
            {
                int mid = managerService.init(store1);
                String oldFolder = "image/profile_manager/default.jpg";
                String newFolder = "image/profile_manager/";
                File image1 = new File(request.getServletContext().getRealPath(oldFolder));
                File image2 = new File(request.getServletContext().getRealPath(newFolder), id + ".jpg");
                FileCopyUtils.copy(image1, image2);
                String from = "953625619@qq.com";
                String to = store1.getEmail();
                String subject = "零号商城：恭喜您申请店铺成功！";
                String content = "<html>\n" +
                        "<body>\n" +
                        "<BR>\n" +
                        "<div align='center'>\n" +
                        " <h3>恭喜您，申请成功！</h3>\n" +
                        "    <h3>您的管理员账号为：<b>\"您申请的用户名\"</b></h3>" +
                        "<h3>您的管理员密码为：<b>身份证号码后六位</b></h3>\n"+
                        "<BR>\n" +
                        "<h4>\n" +
                        "<a href='http://www.lhaoo.top/hgmall'>点此前往查看</a>\n" +
                        "</h4>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>";
                mailService.sendHtmlMail(from,to,subject,content);
            }
        }
        if(status==1)
        {
            String from = "953625619@qq.com";
            String to = store1.getEmail();
            String subject = "零号商城：抱歉，申请店铺失败！";
            String content = "<html>\n" +
                    "<body>\n" +
                    "<BR>\n" +
                    "<div align='center'>\n" +
                    " <h3>抱歉，您在零号商城申请店铺失败了！</h3>\n" +
                    "    <h3>请你核对信息，重新申请吧！</h3>\n"+
                    "<BR>\n" +
                    "<h4>\n" +
                    "<a href='http://www.lhaoo.top/hgmall'>点此进入</a>\n" +
                    "</h4>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>";
            mailService.sendHtmlMail(from,to,subject,content);
        }
        return "ok";
    }

    @PostMapping(value = "/admin/stores/search")
    public List<Store> search(@RequestParam(value = "key") String key) throws Exception
    {
        List<Store> stores =  storeService.listByStatusAndKey(3,key);
        storeService.fill(stores);
        return stores;
    }

    @GetMapping(value = "/admin/stores/info")
    public Store info(HttpSession session) throws Exception
    {
        Manager manager = (Manager) session.getAttribute("manager");
        Store store = storeService.get(manager.getSid());
        storeService.fill(store);

        return store;
    }
    @PutMapping(value = "/admin/stores/info")
    public String updateStore(@RequestBody Store store) throws Exception
    {
        storeService.update(store);
        return "ok";
    }

}
