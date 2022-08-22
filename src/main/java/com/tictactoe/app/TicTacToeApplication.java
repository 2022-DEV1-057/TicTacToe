package com.tictactoe.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicTacToeApplication {
	private static Logger logger = LoggerFactory.getLogger(TicTacToeApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TicTacToeApplication.class, args);
		logger.info("--: TicTacToe game rest api service started! :-");
	}

}
