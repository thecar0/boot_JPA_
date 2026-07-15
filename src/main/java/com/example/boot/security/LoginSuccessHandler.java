package com.example.boot.security;

import com.example.boot.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    // redirect 데이터 정보를 가지고 있는 객체 (redirect 로 경로 저장(이동) 역할)
    RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // 세션정보, 직전 url 정보를 가지고 있는 객체
    RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공시

        // lastlogin 시간 업데이트
        log.info(">> authentication >>{}", authentication);
        userService.lastloginUpdate(authentication.getName());

        // 직전 로그인 실패기록 삭제
        // WebAttributes.AUTHENTICATION_EXCEPTION : 로그인 실패시 세션에 기록
        HttpSession ses = request.getSession();
        if(ses == null){
            return;
        }else {
            ses.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        // 직전 url 의 정보로 리다이렉트 해줘야 함.
        // 이전 mapping 경로 가져오기 => 없으면 /board/list 로 보내기
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        redirectStrategy.sendRedirect(request, response,
                savedRequest != null ? savedRequest.getRedirectUrl() : "/board/list");
    }
}