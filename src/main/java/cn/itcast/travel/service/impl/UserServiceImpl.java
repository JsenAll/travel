package cn.itcast.travel.service.impl;


import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        //Todo  1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为null
        if (u != null) {
            //用户名存在，注册失败
            return false;
        }
        //Todo  2.保存用户信息
        //2.1设置激活码，唯一字符串

        user.setCode(UuidUtil.getUuid());

        //2.2设置激活状态

        user.setStatus("N");
        userDao.save(user);

        //Todo  3.激活邮件发送，邮件正文？

        String content = "<a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
        System.out.println(content);

        boolean sendMail = MailUtils.sendMail(user.getEmail(), content, "激活邮件");
        System.out.println("邮箱发送:" + (sendMail ? "成功" : "失败"));
        return true;
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */

    @Override
    public boolean active(String code) {
        User user = userDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改激活状态的方法
            userDao.updateStatus(user);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 登陆
     *
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        User u = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (user != null) {
            return u;
        } else {
            return null;
        }

    }
}
