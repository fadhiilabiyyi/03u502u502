package com.fadhiilabiyyi.license3j.Interceptor;

import com.fadhiilabiyyi.license3j.LicenseValidator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import static com.fadhiilabiyyi.license3j.License3jApplication.om;

@AllArgsConstructor
@Component
public class LicenseInterceptor extends GenericFilterBean {
    private final LicenseValidator licenseService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (licenseService.licenseExpired()) {
            ObjectNode objectNode = om.createObjectNode();
            objectNode.set("message", TextNode.valueOf("License expired, please renew your license."));

            String resp = om.writeValueAsString(objectNode);

            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write(resp);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}