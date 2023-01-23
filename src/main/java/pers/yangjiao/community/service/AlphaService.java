package pers.yangjiao.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pers.yangjiao.community.dao.AlphaDao;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AlphaService {
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("construct AlphaService");
    }

    @PostConstruct
    public void init(){
        System.out.println("init AlphaService");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy AlphaService");
    }

    public String find(){
        return alphaDao.select();
    }
}
