package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Servlet调用service
 * service调用dao
 */

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {


    UserService service = new UserServiceImpl();

    /**
     * 注册
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //验证校验
        String check = request.getParameter("check");
        //从sesion中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;
        }


        //Todo 1:获取数据
        Map<String, String[]> parameterMap = request.getParameterMap();

        //Todo 2:封装对象
        User user = new User();
        try {
            //将parameterMap的值对应的封装到user的属性里面去
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //Todo 3:调用service完成注册
//        UserService service = new UserServiceImpl();
        Boolean flag = service.regist(user);
        System.out.println("注册:" + (flag ? "成功" : "失败"));

        //Todo 4:响应结果
        ResultInfo resultInfo = new ResultInfo();
        if (flag) {
            resultInfo.setFlag(flag);
        } else {
            resultInfo.setFlag(flag);
            resultInfo.setErrorMsg("注册失败");
        }
        //4.2:将resultInfo序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(resultInfo);
        //4.3:将json数据写回客户端,设置 content - type
        response.setContentType("application/json;chatset=utf-8");
        response.getWriter().write(json);
    }

    /**
     * 登陆
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用Service查询
//        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();

        //4.判断用户对象是否为null
        if (u == null) {
            //用户名密码或错误
            info.setFlag(false);
            info.setErrorMsg("用户名密码或错误");
        }
        //5.判断用户是否激活
        if (u != null && !"N".equals(u.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活");
        }
        //6.判断登录成功
        if (u != null && "Y".equals(u.getStatus())) {
            request.getSession().setAttribute("user", u);//登录成功标记

            //登录成功
            info.setFlag(true);
        }
        //响应数据
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), info);

    }


    /**
     * 将登陆后的用户  对应的用户名输出到页面中
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端

        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), user);

    }

    /**
     * 用户退出  清除Session中user 属性 返回到登陆页面
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session  invalidate() 自杀
        request.getSession().invalidate();

        //2.跳转登录页面 重定向
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 激活的servlet
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        // http://localhost/travel/activeUserServlet?code=56b063bf06914aefa0c659ba012f9ff9
        //通过这个链接  获取code的值

        String code = request.getParameter("code");
        System.out.println("--------------------------");
        if (code != null) {
//            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);
            //3.判断标记
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功，请<a href='http://localhost/travel/login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败，请联系管理员!";
            }
            response.setContentType("text/html;charset=utf-8");//防止乱码
            response.getWriter().write(msg);//写回客户端
        }
    }


}
