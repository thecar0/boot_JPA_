package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.dto.BoardFileDTO;
import com.example.boot.dto.FileDTO;
import com.example.boot.entity.Board;
import com.example.boot.entity.File;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BoardService {
    // interface 추상메서드만 가능한 객체
    // default method : 인터페이스에서 규칙을 잡거나, 로직을 잡거나 할 때 사용
    // 호환성 유지

    // DTO (화면용)  Entity (DB저장용)
    // convert DTO -> Entity   / Entity -> DTO
    // BoardDTO => board(Entity)  변환
    // BoardDTO : bno, title, writer, content, readCount, cmtQty, fileQty, regDate, modDate
    // Board(entity) : bno, title, writer, content, readCount, cmtQty, fileQty
    default Board convertDtoToEntity(BoardDTO boardDTO){
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .readCount(boardDTO.getReadCount())
                .cmtQty(boardDTO.getCmtQty())
                .fileQty(boardDTO.getFileQty())
                .build();
    }

    /* 반대 케이스 DB => 화면 */
    default BoardDTO convertEntityToDto(Board board){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .readCount(board.getReadCount())
                .cmtQty(board.getCmtQty())
                .fileQty(board.getFileQty())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .build();
    }

    Long insert(BoardDTO boardDTO);

//    List<BoardDTO> getList();

    BoardFileDTO getDetail(Long bno);

    void update(BoardDTO boardDTO);

    void remove(Long bno);

    Page<BoardDTO> getList(int pageNO);

    // file convert
    // FileDTO => FileEntity (date X)
    default File convertDTOToEntity(FileDTO fileDTO){
        return File.builder()
                .uuid(fileDTO.getUuid())
                .saveDir(fileDTO.getSaveDir())
                .fileName(fileDTO.getFileName())
                .fileType(fileDTO.getFileType())
                .bno(fileDTO.getBno())
                .fileSize(fileDTO.getFileSize())
                .build();
    }

    // FileEntity => FileDTO (date O)
    default FileDTO convertEntityToDto(File file) {
        return FileDTO.builder()
                .uuid(file.getUuid())
                .saveDir(file.getSaveDir())
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .fileSize(file.getFileSize())
                .regDate(file.getRegDate())
                .modDate(file.getModDate())
                .build();
    }

    Long insert(BoardFileDTO boardFileDTO);

    FileDTO getFile(String uuid);

    long fileRemove(String uuid);

    void modify(BoardFileDTO boardFileDTO);

    List<FileDTO> gettodayFileList(String today);

    Page<BoardDTO> getList(int pageNo, String type, String keyword);

    List<BoardDTO> getTop5();
}