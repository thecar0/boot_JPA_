package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.entity.Board;
import com.example.boot.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // CRUD에 해당하는 메서드 제공
        // save() : 저장
        // 저장하는 객체는 Entity (Board)
        Board board = convertDtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();  // 저장 후 bno 가져가기
        return bno;
    }

//    @Override
//    public List<BoardDTO> getList() {
//        // findAll() => 전체 값 리턴
//        // select * from board order by bno desc
//        // 정렬 : Sort.by(Sort.direction.DESC, "정렬기준 칼럼명")
//        // DB에서 가져오는 return List<Board>  => List<BoardDTO> 변환
//        List<Board> boardList = boardRepository.findAll(
//                Sort.by(Sort.Direction.DESC, "bno"));
//
//        List<BoardDTO> boardDTOList = boardList
//                .stream()
//                .map(board -> convertEntityToDto(board))
////                .map(this :: convertEntityToDto)
//                .toList();
//        return boardDTOList;
//    }

    @Override
    public BoardDTO getDetail(Long bno) {
        // findOne => 기본키를 이용하여 원하는 객체 검색 where bno
        // findBy칼럼명 => 원하는 칼럼명을 이용하여 검색
        // findById = findOne
        // Optional<T> : nullPointException이 발생하지 않도록 도와줌
        // Optional.isEmpty() : null 이면 true / false
        // Optional.isPresent() : 값이 있는지 없는지 확인 true / false
        // Optional.get() : 객체 가져오기
        Optional<Board> optional =  boardRepository.findById(bno);
        if(optional.isPresent()){
            Board board = optional.get();
            BoardDTO boardDTO = convertEntityToDto(board);
            // 조회수 올리기 readCount + 1
            // update board set read_count = read_count+1 where bno = bno
            // save()
            // 변경된 객체로 객체를 수정 => 저장
            board.setReadCount(board.getReadCount()+1);
            boardRepository.save(board);

            return boardDTO;
        }
        return null;
    }

    @Override
    public void update(BoardDTO boardDTO) {
        // update 기능은 없음.
        // update 할 Board 객체를 잘 만든다음 save()
        // findById 객체를 가져와서 => 내 객체의 변경값만 수정 => save()
        Optional<Board> optional = boardRepository.findById(boardDTO.getBno());
        if(optional.isPresent()){
            Board board = optional.get();  // 내 객체
            board.setTitle(boardDTO.getTitle());
            board.setContent(boardDTO.getContent());
            board.setReadCount(board.getReadCount()-1);
            boardRepository.save(board);
        }
    }

    @Override
    public void remove(Long bno) {
        boardRepository.deleteById(bno);
    }

    @Override
    public Page<BoardDTO> getList(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1, 10,
                Sort.by("bno").descending());
        Page<Board> pageList = boardRepository.findAll(pageable);

        Page<BoardDTO> boardDTOPage = pageList.map(this :: convertEntityToDto);
        return boardDTOPage;
    }
}