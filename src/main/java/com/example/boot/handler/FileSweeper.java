package com.example.boot.handler;

import com.example.boot.dto.FileDTO;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Slf4j
@Component
@RequiredArgsConstructor
public class FileSweeper {
    // 매일 정해진 시간에 스케줄러 실행
    // 매일 해당 날짜의 경로에 DB의 데이터와 폴더 안의 파일이 일치하는지 비교
    // DB == file 일치하면 남기고, DB != 폴더삭제
    private final BoardService boardService;
    private final String BASE_PATH = "C:\\CHA_\\짜몽\\_myProject\\_java\\_fileUpload\\";

    // cron 방식
    // 스프링(Spring) 기반 환경: 초(0~59) 분(0~59)
    @Scheduled(cron = "00 14 23 * * *")
    public void fileSweeper(){
        log.info(">> fileSweeper Start >>{}", LocalDateTime.now());
        // DB 에 등록된 파일 리스트 가져오기 (오늘 날짜 폴더만)
        // 오늘날짜의 경로가 필요
        LocalDate now = LocalDate.now();
        String today = now.toString().replace("-", File.separator);

        // DB 에서 today == saveDir 일치하는 값만 가져오기
        // select * from file where save_dir = todqy
        List<FileDTO> dbFileList = boardService.gettodayFileList(today);
        log.info(">> dbFileList >>{}", dbFileList);

        List<String> currFile = new ArrayList<>();
        for (FileDTO fileDTO : dbFileList){
            String fileName =BASE_PATH + today + File.separator + fileDTO.getUuid()+"_"+fileDTO.getFileName();
            currFile.add(fileName);
            if(fileDTO.getFileType() == 1){
                String fileThName =BASE_PATH + today + File.separator + fileDTO.getUuid()+"_th"+fileDTO.getFileName();
                currFile.add(fileThName);
            }
        }
        log.info(">> currFile >>{}", currFile);

        // today 경로 기반 저장된 파일 검색
        File dir = Paths.get(BASE_PATH+today).toFile();

        // 해당 경로 안에 있는 파일을 가져오기 (배열로 리턴)
        File[] allFileObject = dir.listFiles();

        // allFileObject , currFile 비교하여 DB 에 존재하지 않는 파일은 삭제
        // DB 기준
        for (File file : allFileObject){
            String storedFileName = file.toPath().toString();
            if(!currFile.contains(storedFileName)){
                // currFile 안에 storedFileName 이 존재하지 않는다면
                file.delete(); // 삭제
                log.info(">> 삭제되는 파일 이름 >> {}", storedFileName);
            }
        }

        log.info(">> fileSweeper End >>{}", LocalDateTime.now());
    }
}
