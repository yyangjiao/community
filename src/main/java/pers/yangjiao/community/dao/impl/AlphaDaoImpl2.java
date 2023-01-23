package pers.yangjiao.community.dao.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pers.yangjiao.community.dao.AlphaDao;

@Repository
@Primary
public class AlphaDaoImpl2 implements AlphaDao {
    @Override
    public String select() {
        return "AlphaDaoImpl2.select()";
    }
}
