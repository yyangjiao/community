package pers.yangjiao.community.dao;

import org.apache.ibatis.annotations.Mapper;
import pers.yangjiao.community.entity.LoginTicket;

@Mapper
public interface LoginTicketMapper {
    int insertLoginTicket(LoginTicket longinTicket);

    LoginTicket selectByTicket(String ticket);

    int updateStatus(String ticket, int status);
}
