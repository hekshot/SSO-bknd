package com.hekshot.skillrebe.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        this.setDefaultTargetUrl(frontendUrl+"/");  // Set the default target URL after successful logout.
        super.onLogoutSuccess(request, response, authentication);
    }
}
