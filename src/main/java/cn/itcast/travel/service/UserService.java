package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

/**
 * Servlet调用service
 * service调用dao
 */
public interface UserService {
    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    boolean regist(User user);

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    boolean active(String code);
}
