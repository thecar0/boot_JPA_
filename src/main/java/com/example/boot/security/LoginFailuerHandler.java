package com.example.boot.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class LoginFailuerHandler implements AuthenticationFailureHandler {

    // redirect 데이터 정보를 가지고 있는 객체 (redirect 로 경로 저장(이동) 역할)
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패시
        // error 메세지 띄우기
        String errorMessage; // error message 저장용

        // BadCredentialsException (패스워트 틀렸을 때)
        // InternalAuthenticationServiceException (내부 시스템 문제)
        // UsernameNotFoundException (id 오류)
        // AuthenticationCredentialsNotFoundException (인증요청 거부)
        if(exception instanceof BadCredentialsException){
            errorMessage = "email / password fail";
        }else {
            errorMessage = "관리자에게 문의해주세요.";
        }

        log.info(">> error message >>{}", errorMessage);

        // 로그인 페이지로 해당 에러 메시지를 리다이렉트
        request.getSession().setAttribute("errMsg", errorMessage);

        redirectStrategy.sendRedirect(request, response, "/user/login");
    }
}