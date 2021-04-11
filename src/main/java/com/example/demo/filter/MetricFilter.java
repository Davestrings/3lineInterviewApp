package com.example.demo.filter;

import com.example.demo.service.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MetricFilter implements Filter {

    private MetricService metricService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // access bean of metricService
        metricService = (MetricService) WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext())
                .getBean("metricService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();
        int status = ((HttpServletResponse) servletResponse).getStatus();
        metricService.increaseCount(uri, status);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
