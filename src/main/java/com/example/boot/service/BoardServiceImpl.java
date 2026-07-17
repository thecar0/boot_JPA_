package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.dto.BoardFileDTO;
import com.example.boot.dto.FileDTO;
import com.example.boot.entity.Board;
import com.example.boot.entity.File;
import com.example.boot.repository.BoardRepository;
import com.example.boot.repository.FileRepository;
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
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Override
    public Long insert(BoardFileDTO boardFileDTO) {
        Board board = convertDtoToEntity(boardFileDTO.getBoardDTO());
        List<FileDTO> fileDTOList = boardFileDTO.getFileList();
         // fileQty 값만 넣어서 저장
         if(fileDTOList != null){
            board.setFileQty(fileDTOList.size());
        }
         long bno = boardRepository.save(board).getBno();
        // bno 얻어서 fileDTO bno setting
        if(bno > 0 && fileDTOList != null){
            for (FileDTO fileDTO : fileDTOList){
                fileDTO.setBno(bno);
                fileRepository.save(convertDTOToEntity(fileDTO));
            }
        }
        return bno;
    }

    @Override
    public FileDTO getFile(String uuid) {
        File file = fileRepository.findById(uuid)
                .orElseThrow(()-> new EntityNotFoundException("해당 파일이 없습니다."));
        FileDTO fileDTO = convertEntityToDto(file);
        return fileDTO;
    }

    @Transactional
    @Override
    public long fileRemove(String uuid) {
        File file = fileRepository.findById(uuid)
                .orElseThrow(()-> new EntityNotFoundException("해당 파일이 없습니다."));
        // 파일 갯수 차감 -1
        Board board = boardRepository.findById(file.getBno())
                .orElseThrow(()-> new EntityNotFoundException("해당 파일이 없습니다."));
        board.setFileQty(board.getFileQty()-1);
        fileRepository.deleteById(uuid);
        return board.getBno();
    }

    @Transactional
    @Override
    public void modify(BoardFileDTO boardFileDTO) {
        Board board = boardRepository.findById(boardFileDTO.getBoardDTO().getBno())
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시글 입니다."));
        board.setTitle(boardFileDTO.getBoardDTO().getTitle());
        board.setContent(boardFileDTO.getBoardDTO().getContent());
        board.setReadCount(board.getReadCount()-1);

        if(boardFileDTO.getFileList() != null){
            board.setFileQty(board.getFileQty()+boardFileDTO.getFileList().size());
            for (FileDTO fileDTO : boardFileDTO.getFileList()){
                // bno 가 없음
                fileDTO.setBno(board.getBno());
                fileRepository.save(convertDTOToEntity(fileDTO));
            }
        }
    }

    @Override
    public List<FileDTO> gettodayFileList(String today) {
        // select * from file where save_dir = todqy
        // findBy** -> fileRepository 등록
        List<File> fileList = fileRepository.findBySaveDir(today);
        if(fileList != null || fileList.isEmpty()){
            return fileList.stream().map(this::convertEntityToDto).toList();
        }
        return null;
    }

    @Override
    public Page<BoardDTO> getList(int pageNo, String type, String keyword) {
        // page + search
        // pageable, type, keyword => Page<Board>
        Pageable pageable = PageRequest.of(pageNo-1, 10,
                Sort.by("bno").descending());
        log.info("1 >>");
        Page<Board> pageList = boardRepository.searchBoard(type, keyword, pageable);
        return pageList.map(this :: convertEntityToDto);
    }

    @Override
    public List<BoardDTO> getTop5() {
        return boardRepository.findTop5ByOrderByReadCountDesc()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

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
    public BoardFileDTO getDetail(Long bno) {
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

            // fileList 가져오기 (==bno)
            List<File> fileList = fileRepository.findByBno(bno);
            List<FileDTO> fileDTOList = fileList.stream()
                    .map(this::convertEntityToDto)
                    .toList();

            BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileDTOList);

            return boardFileDTO;
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