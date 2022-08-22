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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;
import com.tictactoe.app.openapi.model.TurnRequest;
import com.tictactoe.app.openapi.model.TurnResponse;

@Service
public class GameStateService implements TictactoeApiDelegate {

	private static Logger log = LoggerFactory.getLogger(GameStateService.class);

	private Map<String, String> gameBoard = new HashMap<>();

	@Override
	public ResponseEntity<NewGameInfo> startNewGame() {
		NewGameInfo newGameInfo = new NewGameInfo();
		newGameInfo.setGameboard(getInitialGameBoardOnNewGameStartUp());
		newGameInfo.message(MESSAGE);
		return new ResponseEntity<>(newGameInfo, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<TurnResponse> playerTurn(TurnRequest turnRequest) {
		log.info("--: Executing player move :--");
		getCurrentlyPlayingGameBoard().put(String.valueOf(turnRequest.getPosition()), turnRequest.getPlayerId());
		TurnResponse turnResponse = new TurnResponse();
		turnResponse.setState(getCurrentlyPlayingGameBoard());
		return new ResponseEntity<>(turnResponse, HttpStatus.OK);
	}

	private Map<String, String> getInitialGameBoardOnNewGameStartUp() {
		gameBoard = new HashMap<>();
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

	public Map<String, String> getCurrentlyPlayingGameBoard() {
		return this.gameBoard;
	}

	public void setGameBoard(Map<String, String> gameBoard) {
		this.gameBoard = gameBoard;
	}

}
