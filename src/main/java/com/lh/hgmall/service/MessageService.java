package com.lh.hgmall.service;

import com.lh.hgmall.bean.Message;
import com.lh.hgmall.dao.MessageDAO;
import com.lh.hgmall.util.PageUtil;
import com.lh.hgmall.util.RestPageImpl;
import com.lh.hgmall.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "message")
public class MessageService {
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    UserService userService;

    Sort sort = new Sort(Sort.Direction.DESC,"id");

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public List<Message> listMessage(String type,int sid)
    {
        return messageDAO.findAllByTypeIsAndStatusNotAndSid(type,2,sid,sort);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Message> listMessage(int start,int size,int number,String type,int sid)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Message> page = messageDAO.findAllByTypeIsAndStatusNotAndSid(type,2,sid,pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public PageUtil<Message> listView(int start,int size,int number,String type)
    {
        Pageable pageable = new PageRequest(start,size,sort);
        Page<Message> page = messageDAO.findAllByTypeNotAndStatusNot(type,2,pageable);
        page = new RestPageImpl(page.getContent(),pageable,page.getTotalElements());

        return new PageUtil<>(page,number);
    }

    public List<Message> listMessageByKey(String type,String key,int sid)
    {
        return messageDAO.findAllByTypeIsAndTextContainingAndSid(type,key,sid,sort);
    }

    public List<Message> listViewByKey(String type,String key)
    {
        return messageDAO.findAllByTypeNotAndTextContaining(type,key,sort);
    }

    @CacheEvict(allEntries = true)
    public void add(Message message){
        messageDAO.save(message);
    }

    @CacheEvict(allEntries = true)
    public void update(Message message)
    {
        messageDAO.save(message);
    }

    @CacheEvict(allEntries = true)
    public void delete(int id)
    {
        messageDAO.delete(id);
    }

    @Cacheable( keyGenerator = "wiselyKeyGenerator")
    public Message get(int id)
    {
        return messageDAO.findOne(id);
    }

    public void fillUser(Message message){
        int uid = message.getUid();
        message.setUser(userService.get(uid));
    }

    public void fillUser(List<Message> messages){
        MessageService messageService = SpringContextUtils.getBean(MessageService.class);
        for (Message message:
             messages) {
            messageService.fillUser(message);
        }
    }
}
