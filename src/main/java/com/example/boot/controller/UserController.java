package com.example.boot.controller;

import com.example.boot.dto.UserDTO;
import com.example.boot.entity.User;
import com.example.boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/*")
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public String join(UserDTO userDTO){
        String email = userService.insert(userDTO);
        log.info("회원가입 완료");
        return "index";
    }

    @GetMapping("/login")
    public void login(HttpServletRequest request, Model model){
        String errMsg = (String)request.getSession().getAttribute("errMsg");
        if(errMsg != null){
            log.info(">> errMsg >>{}", errMsg);
            model.addAttribute("errMsg", errMsg);
        }
        // 전송후 지우기
        request.getSession().removeAttribute("errMsg");
    }

    @GetMapping("/admin")
    public String admin(@RequestParam("email") String adminEmail){
//        String adminEmail = "admin123@test.com";
        userService.grantAdminRole(adminEmail);
        log.info(">>> grantADminRole success!!");
        return "redirect:/user/userList";
    }

    @GetMapping("/modify")
    public void modify(Model model, Principal principal){
        // principal : 로그인한 유저 이름(username)을 저장하는 객체
        UserDTO userDTO = userService.getDetail(principal.getName());
        model.addAttribute("userDTO", userDTO);
    }

    @PostMapping("/modify")
    public String modify(UserDTO userDTO, RedirectAttributes redirectAttributes,
                         HttpServletRequest request, HttpServletResponse response){
        // 회원정보를 수정한 후 로그아웃
        // "/" msg 보내서 재 로그인 시도
        String email = userService.modify(userDTO);

        if(email != null){
            redirectAttributes.addFlashAttribute("modMsg","ok");
            logout(request, response);
            return "redirect:/";
        }else {
            redirectAttributes.addFlashAttribute("modMsg","fail");
            return "redirect:/user/modify";
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response){
        // request, response, authentication
        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if(auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @GetMapping("/userList")
    public void list(Model model){

        List<UserDTO> list = userService.getList();
        log.info(">> list >> {}", list);
        log.info(">> list >> {}", list.get(0).getAuthList());

        model.addAttribute("list", list);

    }

    @GetMapping("/remove")
    public String remove(Principal principal,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request,
                         HttpServletResponse response){
        String email = userService.remove(principal.getName());

        if(email != null){
            redirectAttributes.addFlashAttribute("delMsg","ok");
            logout(request, response);
            return "redirect:/";
        }else{
            redirectAttributes.addFlashAttribute("delMsg","fail");
            return "redirect:/user/modify";
        }
    }

    @GetMapping("/check")
    @ResponseBody
    public String checkEmail(@RequestParam String email){

        if(userService.checkEmail(email)){
            return "NO";
        }

        return "OK";
    }
}
