package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageDAO extends JpaRepository<Message,Integer> {
    public static String type_complaint = "type_complaint";
    public static String type_suggestion = "type_suggestion";
    public static String type_message = "type_message";

    public List<Message> findAllByTypeIsAndTextContainingAndSid(String type,String key,int sid, Sort sort);
    public List<Message> findAllByTypeNotAndTextContaining(String type,String key, Sort sort);
    public Page<Message> findAllByTypeIsAndStatusNotAndSid(String type,int status,int sid,Pageable pageable);
    public List<Message> findAllByTypeIsAndStatusNotAndSid(String type ,int status,int sid, Sort sort);
    public Page<Message> findAllByTypeNotAndStatusNot(String type,int status, Pageable pageable);
}
