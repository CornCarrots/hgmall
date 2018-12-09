package com.lh.hgmall.dao;

import com.lh.hgmall.bean.Module;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleDAO extends JpaRepository<Module,Integer> {
    public List<Module> findAllByNameContaining(String name, Sort sort);
    public List<Module> findAllByPid(int pid, Sort sort);
    public List<Module> findAllByUrl(String url);
}
