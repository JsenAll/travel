package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    /**
     * 处理分发请求
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1: 获取请求路径
        String uri = req.getRequestURI();///travel/user/add
        System.out.println("请求路径->" + uri);
        //2:获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);//add
        System.out.println("请求方法名=>" + methodName);
        try {
            //3:获取方法对象 Method
            // getDeclaredMethod() 可以获取 所有的方法  私有的 受保护的 公有的都可以 忽略访问权限修饰符
            // getMethod() 只可以获取公有的
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            //4:执行方法
            //暴力反射  不去检查
            //  method.setAccessible(true);
            method.invoke(this, req, resp);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     *
     * @param obj
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), obj);
    }

    /**
     * 将传入的对象序列化为json，返回
     *
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}
