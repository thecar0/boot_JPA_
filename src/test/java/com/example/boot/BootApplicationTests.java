package com.example.boot;

import com.example.boot.dto.BoardDTO;
import com.example.boot.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BootApplicationTests {

	@Autowired
	private BoardService boardService;

	@Test
	void contextLoads() {
		for(int i=0; i<500; i++){
			BoardDTO boardDTO = BoardDTO.builder()
					.title("test title "+(int)(Math.random()*100))
					.writer("tester"+(int)(Math.random()*50)+"@test.com")
					.content("test content"+i)
					.build();
			boardService.insert(boardDTO);
		}
	}
}