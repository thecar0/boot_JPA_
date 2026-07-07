package com.example.boot.service;

import com.example.boot.dto.CommentDTO;
import com.example.boot.entity.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {
    // convert
    // DTO => Entity
    default Comment convertDtoToEntity(CommentDTO commentDTO) {
        return Comment.builder()
                .cno(commentDTO.getCno())
                .bno(commentDTO.getBno())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
    }

    // Entity => DTO
    default CommentDTO convertEntityToDto(Comment comment) {
        return CommentDTO.builder()
                .cno(comment.getCno())
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .regDate(comment.getRegDate())
                .modDate(comment.getModDate())
                .build();
    }

    long post(CommentDTO commentDTO);

    List<CommentDTO> getList(long bno);

    void remove(long cno);

    Page<CommentDTO> getList(long bno, int page);

    long update(CommentDTO commentDTO);
}
