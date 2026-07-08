package com.example.boot.handler;

import com.example.boot.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 내가 만든 객체를 스프링의 프레임워크에 빈으로 설정하는 어노테이션
@Slf4j
@Component
public class FileHandler {
    // 파일 저장 + DB 에 들어갈 List 생성
    private final String UP_DIR = "C:\\CHA_\\짜몽\\_myProject\\_java\\_fileUpload";

    public List<FileDTO> uploadFile(MultipartFile[] files){
        List<FileDTO> fileDTOList = new ArrayList<>();

        // 날짜 형태의 폴더 구성 2026/07/07
        LocalDate date = LocalDate.now(); // 2026-07-07 => 폴더의 경로로 변경
        String today = date.toString().replace("-", File.separator);

        // 경로 + 파일 이름
        File folders = new File(UP_DIR, today);

        // 해당폴더가 없으면 생성하고 / 있으면 생성 X
        // mkdir(폴더 1개만 생성) / mkdirs(하위 폴더까지 동시생성)
        if(!folders.exists()){
            folders.mkdirs();
        }

        // file 정보 생성 => fileDTO
        for(MultipartFile file : files){
            FileDTO fileDTO = new FileDTO();
            fileDTO.setFileName(file.getOriginalFilename());
            fileDTO.setFileSize(file.getSize());
            fileDTO.setFileType(file.getContentType().startsWith("image") ? 1 : 0);
            fileDTO.setSaveDir(today);

            // uuid
            UUID uuid = UUID.randomUUID();
            String uuidString = uuid.toString();
            fileDTO.setUuid(uuidString);

            fileDTOList.add(fileDTO);

            // 저장
            String fileName = uuidString+"_"+file.getOriginalFilename();
            String fileThName = uuidString+"_th"+file.getOriginalFilename();

            // 실 저장 파일 객체
            File storeFile = new File(folders, fileName);

            try{
                file.transferTo(storeFile);
                // 썸네일은 그림파일만...
//                if(fileDTO.getFileType() == 1){
//                    File thumbnail = new File(folders, fileThName);
//                    Thumbnails.of(storeFile).size(100, 100).toFile(thumbnail);
//                }
            }catch(Exception e){
                log.info(">> file save error >>", e);
            }
        }

        return fileDTOList;
    }
}
