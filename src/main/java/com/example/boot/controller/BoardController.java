package com.example.boot.controller;

import com.example.boot.dto.BoardDTO;
import com.example.boot.handler.PagingHandler;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;

/*    @GetMapping("/register")
    public String register(){
        *//* /board/register  => (board 컨트롤러에서 register라는 getmapping으로 연결) *//*
        // 들어오는 경로와 나가는 경로가 같을 경우 생략가능
        return "/board/register";
    }*/

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO){
        log.info(">> boardDTO >> {}", boardDTO);
        Long bno = boardService.insert(boardDTO);

        return "redirect:/board/list";
    }

//    @GetMapping("/list")
//    public void list(Model model){
//        List<BoardDTO> list = boardService.getList();
//        model.addAttribute("list", list);
//    }

    @GetMapping("/list")
    public void list(Model model,
                     @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo){
        Page<BoardDTO> list = boardService.getList(pageNo);
        model.addAttribute("list",list);
//        log.info("getTotalElements >> {}", list.getTotalElements()); // 전체 게시글 수
//        log.info("getTotalPages >> {}", list.getTotalPages()); // realEndPage
//        log.info("hasPrevious >> {}", list.hasPrevious()); // 이전 버튼의 필요 여부
//        log.info("hasNext >> {}", list.hasNext());; // 다음버튼의 필요 여부

        PagingHandler ph = new PagingHandler(list, pageNo);
        model.addAttribute("ph", ph);
    }

    @GetMapping("/detail")
    public void detail(@RequestParam("bno") Long bno, Model model){
        BoardDTO boardDTO = boardService.getDetail(bno);
        model.addAttribute("board", boardDTO);
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO,
                         RedirectAttributes redirectAttributes){
        boardService.update(boardDTO);

        // redirect시 해당 위치로 객체를 보내주는 역할
        redirectAttributes.addAttribute("bno",boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("bno")Long bno){
        boardService.remove(bno);
        return "redirect:/board/list";
    }

}