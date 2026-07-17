package com.example.boot.root;

import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RootController {

    private final BoardService boardService;

    @GetMapping("/")
    public String index(Model model){

        model.addAttribute("topList", boardService.getTop5());

        return "index";
    }
}