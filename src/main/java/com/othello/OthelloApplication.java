package com.othello;

import com.othello.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OthelloApplication implements CommandLineRunner {
	@Autowired
	private BoardService boardService;

	public static void main(String[] args) {
		SpringApplication.run(OthelloApplication.class, args);
	}

	@Override
	public void run(String... args) {
		boardService.play();
	}

}
