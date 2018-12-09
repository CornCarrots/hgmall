package com.lh.hgmall.controller;

import com.lh.hgmall.bean.Manager;
import com.lh.hgmall.bean.Message;
import com.lh.hgmall.bean.Store;
import com.lh.hgmall.bean.User;
import com.lh.hgmall.dao.MessageDAO;
import com.lh.hgmall.service.MailService;
import com.lh.hgmall.service.MessageService;
import com.lh.hgmall.service.StoreService;
import com.lh.hgmall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    MailService mailService;
    @Autowired
    StoreService storeService;

    @GetMapping(value = "/admin/messages")
    public PageUtil<Message> listMessage(HttpSession session,@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception{
        start = start<0?0:start;
        Manager manager = (Manager) session.getAttribute("manager");
        PageUtil<Message> page =messageService.listMessage(start, size, 5, MessageDAO.type_message,manager.getSid());
        List<Message> messages = page.getContent();
        messageService.fillUser(messages);
        page.setContent(messages);
        return page;
    }

    @GetMapping(value = "/admin/views")
    public PageUtil<Message> listView(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "7") int size) throws Exception{
        start = start<0?0:start;
        PageUtil<Message> page =messageService.listView(start, size, 5, MessageDAO.type_message);
        List<Message> messages = page.getContent();
        messageService.fillUser(messages);
        page.setContent(messages);
        return page;
    }

    @PutMapping(value = "/admin/messages/check/{id}")
    public void check(@PathVariable("id")int id)throws Exception
    {
        Message message = messageService.get(id);
        message.setStatus(0);
        messageService.update(message);
    }

    @PutMapping(value = "/admin/messages/unCheck/{id}")
    public void unCheck(@PathVariable("id")int id)throws Exception
    {
        Message message = messageService.get(id);
        message.setStatus(1);
        messageService.update(message);
    }

    @DeleteMapping(value = "/admin/messages/{id}")
    public String delete(@PathVariable("id")int id)throws Exception
    {
        Message message = messageService.get(id);
        message.setStatus(2);
        messageService.update(message);
        return null;
    }

    @PutMapping(value = "/admin/messages/{id}")
    public void update(@RequestBody Message message,HttpSession session)
    {
        Manager manager = (Manager) session.getAttribute("manager");
        String title = "零号商城";
        if(manager.getSid()!=0)
        {
            Store store = storeService.get(manager.getSid());
            title = store.getName();
        }
        message.setReplyDate(new Date());
        messageService.update(message);
        messageService.fillUser(message);
        String from = "953625619@qq.com";
        String to = message.getUser().getEmail();
        String subject = title+"：您好，我们已收到您的留言！";
        String content = message.getReply();
        mailService.sendHtmlMail(from,to,subject,content);

    }

    @PostMapping(value = "/admin/messages/search")
    public List<Message> searchMessage(@RequestParam("key")String key,HttpSession session)
    {
        Manager manager = (Manager) session.getAttribute("manager");
        List<Message> messages = messageService.listMessageByKey(MessageDAO.type_message,key,manager.getSid());
        messageService.fillUser(messages);
        return messages;
    }

    @PostMapping(value = "/admin/views/search")
    public List<Message> searchView(@RequestParam("key")String key)
    {
        List<Message> messages = messageService.listViewByKey(MessageDAO.type_message,key);
        messageService.fillUser(messages);
        return messages;
    }

    @GetMapping(value = "/admin/messages/{id}")
    public Message get(@PathVariable("id")int id)
    {
        Message message = messageService.get(id);
        messageService.fillUser(message);
        return message;
    }



}
