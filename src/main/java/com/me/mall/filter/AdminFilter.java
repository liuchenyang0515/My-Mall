package com.me.mall.filter;

import com.me.mall.common.Constant;
import com.me.mall.model.pojo.User;
import com.me.mall.service.UserService;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 描述：管理员校验过滤器
 */
public class AdminFilter implements Filter {
    @Resource
    private UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 利用request获取session
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpSession session = request1.getSession();

        User currentUser = (User) session.getAttribute(Constant.MY_MALL_USER);
        if (currentUser == null) {
            // return ApiRestResponse.error(MyMallExceptionEnum.NEED_LOGIN);
            // 因为doFilter方法返回值是void，上面返回通用对象的方法就不行了。此时如何传给前端json数据，利用下面这种方法
            // 这种方法是还没进入controller层之前使用拦截方法
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
        // 校验是否是管理员
        if (userService.checkAdminRole(currentUser)) {
            // 是管理员就放行
            chain.doFilter(request, response);
        } else {
            // return ApiRestResponse.error(MyMallExceptionEnum.NEED_LOGIN);
            // 因为doFilter方法返回值是void，上面返回通用对象的方法就不行了。此时如何传给前端json数据，利用下面这种方法
            // 这种方法是还没进入controller层之前使用拦截方法
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
