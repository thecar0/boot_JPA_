package com.example.boot.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {
    // /error 맵핑을 받는 컨트롤러

    @RequestMapping("/error")
    public String handlerError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        log.info(">> status >> {}", status);

        if (status != null){
            // request 에서 받은 에러 코드를 숫자로 변환
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()){
                // 404 int
                return "/error/404";
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                // 500 int
                return "/error/500";
            }
        }
        return "/";
    }
}
