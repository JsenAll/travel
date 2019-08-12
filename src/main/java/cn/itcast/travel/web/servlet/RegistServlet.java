package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet调用service
 * service调用dao
 */


@WebServlet(name = "registServlet")
public class RegistServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1:获取数据
        Map<String, String[]> parameterMap = request.getParameterMap();
        //2:封装对象
        User user = new User();
        //3:调用service完成注册
        //4:响应结果
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
