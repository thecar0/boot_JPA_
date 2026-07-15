package com.example.boot.controller;

import com.example.boot.dto.BoardDTO;
import com.example.boot.dto.BoardFileDTO;
import com.example.boot.dto.FileDTO;
import com.example.boot.handler.FileHandler;
import com.example.boot.handler.FileRemoveHandler;
import com.example.boot.handler.PagingHandler;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;

/*    @GetMapping("/register")
    public String register(){
        *//* /board/register  => (board 컨트롤러에서 register라는 getmapping으로 연결) *//*
        // 들어오는 경로와 나가는 경로가 같을 경우 생략가능
        return "/board/register";
    }*/

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO,
                           @RequestParam(name = "files", required = false)MultipartFile[] files){
        // 추가된 파일 처리
        // DB 로 갈 fileDto 객체 만들기
        // 실제 저장
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 파일이 존재 한다면... 핸들러를 호출
            fileList = fileHandler.uploadFile(files);
        }
        log.info(">> boardDTO >> {}", boardDTO);
        log.info(">> fileList >> {}", fileList);
        Long bno = boardService.insert(new BoardFileDTO(boardDTO, fileList));
//        Long bno = boardService.insert(boardDTO);

        return "redirect:/board/list";
    }

//    @GetMapping("/list")
//    public void list(Model model){
//        List<BoardDTO> list = boardService.getList();
//        model.addAttribute("list", list);
//    }

    @GetMapping("/list")
    public void list(Model model,
                     @RequestParam(name = "pageNo", required = false, defaultValue = "1") int pageNo,
                     @RequestParam(name = "type", required = false)String type,
                     @RequestParam(name = "keyword", required = false)String keyword){
        log.info("type >> {}", type);
        log.info("keyword >> {}", keyword);

        Page<BoardDTO> list = boardService.getList(pageNo, type, keyword);
        model.addAttribute("list",list);
//        log.info("getTotalElements >> {}", list.getTotalElements()); // 전체 게시글 수
//        log.info("getTotalPages >> {}", list.getTotalPages()); // realEndPage
//        log.info("hasPrevious >> {}", list.hasPrevious()); // 이전 버튼의 필요 여부
//        log.info("hasNext >> {}", list.hasNext());; // 다음버튼의 필요 여부

        PagingHandler ph = new PagingHandler(list, pageNo, type, keyword);
        log.info("ph >> {}",ph);
        model.addAttribute("ph", ph);
    }

    @GetMapping("/detail")
    public void detail(@RequestParam("bno") Long bno, Model model){
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFile", boardFileDTO);
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO,
                         RedirectAttributes redirectAttributes,
                         @RequestParam(name="files", required = false)MultipartFile[] files){
        List<FileDTO> fileDTOList = null;
        if(files != null && files[0].getSize() > 0){
            fileDTOList = fileHandler.uploadFile(files);
        }
        boardService.modify(new BoardFileDTO(boardDTO, fileDTOList));
//        boardService.update(boardDTO);

        log.info(">>> fileDTOList >> {}", fileDTOList);

        // redirect시 해당 위치로 객체를 보내주는 역할
        redirectAttributes.addAttribute("bno",boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("bno")Long bno){
        boardService.remove(bno);
        return "redirect:/board/list";
    }

    @ResponseBody
    @DeleteMapping("/file/{uuid}")
    public String fileRemove(@PathVariable("uuid") String uuid){
        long bno = boardService.fileRemove(uuid);
        return bno > 0 ? "1" : "0" ;
    }

//    @DeleteMapping("/file/{uuid}")
//    public ResponseEntity<String> fileRemove(@PathVariable("uuid") String uuid){
//        // 비동기
//        log.info(">> uuid >> {}", uuid);
//        // 폴더에 있는 파일을 먼저 삭제 후 DB 의 데이터를 삭제
//        FileDTO removeFile = boardService.getFile(uuid);
//        FileRemoveHandler fileRemoveHandler = new FileRemoveHandler();
//        boolean isDel = fileRemoveHandler.removeFile(removeFile);
//        long bno = 0;
//        if(isDel){
//            // DB 삭제 요청
//            bno = boardService.fileRemove(uuid);
//        }
//        return bno > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) :
//                new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}