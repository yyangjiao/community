package pers.yangjiao.community.util;

import org.springframework.stereotype.Component;
import pers.yangjiao.community.entity.User;


/**
 * 持有用户信息，代替session对象
 */
@Component
public class HostHolder {
    ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
