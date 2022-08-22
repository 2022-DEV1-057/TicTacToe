package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantUtility.MESSAGE;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;
import com.tictactoe.app.openapi.model.Player;
import com.tictactoe.app.openapi.model.TurnRequest;
import com.tictactoe.app.openapi.model.TurnResponse;

@Service
public class GameStateService implements TictactoeApiDelegate {

	private static Logger log = LoggerFactory.getLogger(GameStateService.class);
	private Map<String, String> gameBoard = new HashMap<>();
	private boolean isGameEnd = Boolean.FALSE;
	@Autowired
	private GameBoardChecker gameBoardChecker;

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
		if (Boolean.TRUE.equals(checkingGameOverAlready())) {
			log.info("--:Hi Player-{}, Game over already:--", turnRequest.getPlayerId());
			return new ResponseEntity<>(preparePlayerTurnResponse(), HttpStatus.OK);
		}
		if (!gameBoardChecker.stoppingSamePlayerTakingContinuousTurns(turnRequest.getPlayerId(), gameBoard)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (getCurrentlyPlayingGameBoard().get(String.valueOf(turnRequest.getPosition())) != null) {
			log.error("--:Player-{} trying wrong move, occupied position/slot not allowed:--",
					turnRequest.getPlayerId());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		getCurrentlyPlayingGameBoard().put(String.valueOf(turnRequest.getPosition()), turnRequest.getPlayerId());
		return new ResponseEntity<>(preparePlayerTurnResponse(), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Map<String, String>> getStateOfGameBoard() {
		return new ResponseEntity<>(getCurrentlyPlayingGameBoard(), HttpStatus.OK);
	}
	
	private Map<String, String> getInitialGameBoardOnNewGameStartUp() {
		return this.gameBoard = gameBoardChecker.getInitialGameBoardOnNewGameStartUp();
	}

	public Map<String, String> getCurrentlyPlayingGameBoard() {
		return this.gameBoard;
	}

	public void setGameBoard(Map<String, String> gameBoard) {
		this.gameBoard = gameBoard;
	}

	public boolean checkingGameOverAlready() {
		return this.isGameEnd;
	}

	public void endTheGame(Boolean isGameOVer) {
		this.isGameEnd = isGameOVer;
	}
	public TurnResponse preparePlayerTurnResponse() {
		TurnResponse turnResponse = new TurnResponse();
		Player winner = gameBoardChecker.findWinner(getCurrentlyPlayingGameBoard());
		if (winner != null) {
			endTheGame(Boolean.TRUE);
		}
		turnResponse.setGameOver(checkingGameOverAlready());
		turnResponse.setState(getCurrentlyPlayingGameBoard());
		turnResponse.setWinner(winner);
		return turnResponse;
	}
}