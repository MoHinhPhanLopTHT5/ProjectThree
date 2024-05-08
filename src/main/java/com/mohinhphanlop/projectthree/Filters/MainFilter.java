package com.mohinhphanlop.projectthree.Filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.mohinhphanlop.projectthree.Services.ThanhVienService;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
@Order(1)
public class MainFilter implements Filter {

    @Autowired
    private ThanhVienService tvService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String requestedUri = req.getRequestURI();
        if (requestedUri.matches(".*(css|jpg|png|gif|js)")) {
            filterChain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession();

        String username = session.getAttribute("username") == null ? "" : session.getAttribute("username").toString();
        String pw = session.getAttribute("pw") == null ? "" : session.getAttribute("pw").toString();

        boolean onSpecialPages = requestedUri.contains("/dangnhap")
                || requestedUri.contains("/dangky") || requestedUri.contains("/quenmatkhau")
                || requestedUri.contains("/quantri");

        if ((username.isEmpty()
                || pw.isEmpty()) && !onSpecialPages) {
            // Chưa đăng nhập và đang ở trên trang nào đấy
            res.sendRedirect("/dangnhap");
        } else if (!tvService.CheckLogin(username, pw) && !onSpecialPages) {
            // Đăng nhập thất bại
            res.sendRedirect("/dangnhap");
        } else if (onSpecialPages && tvService.CheckLogin(username, pw)) {
            // Đã đăng nhập và đang ở trên trang đăng nhập/đăng ký
            res.sendRedirect("/");
        } else
            filterChain.doFilter(req, res);
    }
}