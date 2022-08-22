package com.tictactoe.app.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;

@Service
public class GameStateService implements TictactoeApiDelegate {
	private static final String MESSAGE = "Hello Mr.X and Mr.O your game started!,All the best and enjoy playing";

	@Override
	public ResponseEntity<NewGameInfo> startNewGame() {
		NewGameInfo newGameInfo = new NewGameInfo();
		newGameInfo.setGameboard(getInitialGameBoardOnNewGameStartUp());
		newGameInfo.message(MESSAGE);
		return new ResponseEntity<>(newGameInfo, HttpStatus.CREATED);
	}

	private static Map<String, String> getInitialGameBoardOnNewGameStartUp() {
		Map<String, String> gameBoard = new HashMap<>();
		gameBoard.put("1", null);
		gameBoard.put("2", null);
		gameBoard.put("3", null);
		gameBoard.put("4", null);
		gameBoard.put("5", null);
		gameBoard.put("6", null);
		gameBoard.put("7", null);
		gameBoard.put("8", null);
		gameBoard.put("9", null);
		return gameBoard;
	}
}
