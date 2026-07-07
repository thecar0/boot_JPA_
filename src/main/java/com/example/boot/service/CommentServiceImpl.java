package com.example.boot.service;

import com.example.boot.dto.CommentDTO;
import com.example.boot.entity.Board;
import com.example.boot.entity.Comment;
import com.example.boot.repository.BoardRepository;
import com.example.boot.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 두 개 이상의 명령어가 실행 될 때 (두 명령중 하나라도 잘못되면 error)
    @Transactional
    @Override
    public long post(CommentDTO commentDTO) {
        // 저장 대상 => entity (commentDTO comment 로 변환)
        // save()
        // 댓글이 등록되면 해당 board 의 cmt_qty update +1
//        Optional<Board> optional = boardRepository.findById(commentDTO.getBno());
//        if(optional.isPresent()){
//            Board board = optional.get();
//            board.setCmtQty(board.getCmtQty()+1);
//        }
        // 내가 굳이 save()를 안해도 update 가 일어남.

        Board board = boardRepository.findById(commentDTO.getBno())
                .orElseThrow(()-> new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()+1);

        long cno = commentRepository.save(convertDtoToEntity(commentDTO)).getCno();

        return cno;
    }

    @Transactional
    @Override
    public List<CommentDTO> getList(long bno) {
        // select * from comment where bno = #{bno}
        List<Comment> list = commentRepository.findByBno(bno);
        log.info("commentList >> {}", list);

        List<CommentDTO> commentDTOList = list.stream()
                .map(comment -> convertEntityToDto(comment))
                .toList();
        log.info("commentDTOList >> {}", commentDTOList);

        return commentDTOList;
    }

    @Override
    public Page<CommentDTO> getList(long bno, int page) {
        // select * from comment where bno = #{bno}
        // order by cno desc limit page, qty;
        // Page 된 값을 리턴받으려면 pageable 값을 파라미터로 전송
        Pageable pageable = PageRequest.of(page-1, 5,
                Sort.by("cno").descending());
        Page<Comment> list = commentRepository.findByBno(bno, pageable);

        return list.map(this::convertEntityToDto);
    }

    @Transactional
    @Override
    public void remove(long cno) {
        Comment comment = commentRepository.findById(cno)
                .orElseThrow(()-> new EntityNotFoundException());

        Board board = boardRepository.findById(comment.getBno())
                .orElseThrow(()-> new EntityNotFoundException());

        board.setCmtQty(board.getCmtQty()-1);

        commentRepository.deleteById(cno);
    }

    @Transactional
    @Override
    public long update(CommentDTO commentDTO) {

        Comment comment = commentRepository.findById(commentDTO.getCno())
                .orElseThrow(() -> new EntityNotFoundException());

        comment.setContent(commentDTO.getContent()); 
        return comment.getCno();
    }
}
