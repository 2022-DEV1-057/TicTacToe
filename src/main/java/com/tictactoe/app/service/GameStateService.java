package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantUtility.MESSAGE;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_EIGHT_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_FIVE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_FOUR_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_NINE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_ONE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_SEVEN_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_SIX_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_THREE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_TWO_ON_GAME_BOARD;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;

@Service
public class GameStateService implements TictactoeApiDelegate {

	@Override
	public ResponseEntity<NewGameInfo> startNewGame() {
		NewGameInfo newGameInfo = new NewGameInfo();
		newGameInfo.setGameboard(getInitialGameBoardOnNewGameStartUp());
		newGameInfo.message(MESSAGE);
		return new ResponseEntity<>(newGameInfo, HttpStatus.CREATED);
	}

	private static Map<String, String> getInitialGameBoardOnNewGameStartUp() {
		Map<String, String> gameBoard = new HashMap<>();
		gameBoard.put(POSITION_ONE_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_TWO_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_THREE_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_FOUR_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_FIVE_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_SIX_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_SEVEN_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_EIGHT_ON_GAME_BOARD, null);
		gameBoard.put(POSITION_NINE_ON_GAME_BOARD, null);
		return gameBoard;
	}
}
