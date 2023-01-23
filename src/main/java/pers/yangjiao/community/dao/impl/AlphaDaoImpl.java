package pers.yangjiao.community.dao.impl;

import org.springframework.stereotype.Repository;
import pers.yangjiao.community.dao.AlphaDao;

@Repository
public class AlphaDaoImpl implements AlphaDao {
    @Override
    public String select() {
        return "AlphaDaoImpl.select()";
    }
}
