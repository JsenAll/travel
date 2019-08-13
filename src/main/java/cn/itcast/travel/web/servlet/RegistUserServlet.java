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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Servlet调用service
 * service调用dao
 */

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("阿斯顿发射点发射点发射点");
        //1:获取数据
        Map<String, String[]> parameterMap = request.getParameterMap();

        //2:封装对象
        User user = new User();
        try {
            //将parameterMap的值对应的封装到user的属性里面去
            BeanUtils.populate(user, parameterMap);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3:调用service完成注册
        UserService userService = new UserServiceImpl();
        Boolean flag = userService.regist(user);

        //4:响应结果
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
