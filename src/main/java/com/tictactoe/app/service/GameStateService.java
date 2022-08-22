package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantUtility.MESSAGE;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_O;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_X;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_EIGHT_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_FIVE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_FOUR_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_NINE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_ONE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_SEVEN_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_SIX_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_THREE_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.POSITION_TWO_ON_GAME_BOARD;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_DESCRIPTION_X;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
		if (!stoppingSamePlayerTakingContinuousTurns(turnRequest.getPlayerId(), gameBoard)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (getCurrentlyPlayingGameBoard().get(String.valueOf(turnRequest.getPosition())) != null) {
			log.error("--:Player-{} trying wrong move, occupied position/slot not allowed:--",
					turnRequest.getPlayerId());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		getCurrentlyPlayingGameBoard().put(String.valueOf(turnRequest.getPosition()), turnRequest.getPlayerId());
		TurnResponse turnResponse = new TurnResponse();
		turnResponse.setState(getCurrentlyPlayingGameBoard());
		turnResponse.setWinner(findWinner(getCurrentlyPlayingGameBoard()));
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

	public boolean stoppingSamePlayerTakingContinuousTurns(String playerName, Map<String, String> gameBoard) {
		log.info("--:Validating any player taking continuous turns:--");
		Map<String, Long> playersCountWithMovesMap = gameBoard.values().stream().filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		if (playersCountWithMovesMap.size() == 2) {
			long firstPlayerOccupencyCount = playersCountWithMovesMap.get(PLAYER_X);
			long secondPlayerOccupencyCount = playersCountWithMovesMap.get(PLAYER_O);
			long difference = firstPlayerOccupencyCount > secondPlayerOccupencyCount
					? firstPlayerOccupencyCount - secondPlayerOccupencyCount
					: secondPlayerOccupencyCount - firstPlayerOccupencyCount;
			if (difference > 0 && ((firstPlayerOccupencyCount > secondPlayerOccupencyCount
					&& PLAYER_X.equals(playerName))
					|| (secondPlayerOccupencyCount > firstPlayerOccupencyCount && PLAYER_O.equals(playerName)))) {
				log.info("--:Player-{} has taken wrong turn:--", playerName);
				return Boolean.FALSE;
			}
		} else if (playersCountWithMovesMap.size() == 1 && playersCountWithMovesMap.containsKey(playerName)) {
			log.error("--:Player-{} taking continuous turn wrongly:--", playerName);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}
	
	private Player findWinner(Map<String, String> currentStateOfGameBoard) {
		String winner = checkWinningPossibility(currentStateOfGameBoard);
		Player playerWinner;
		if (winner != null) {
			playerWinner = new Player();
			playerWinner.setId(winner);
			playerWinner.setDescription(PLAYER_DESCRIPTION_X);
			return playerWinner;
		}
		return null;
	}

	public String checkWinningPossibility(Map<String, String> gameBoard) {
		int possibleWinLineCount= 8;
		for (int line = 0; line < possibleWinLineCount; line++) {
			String winLine = null;
			switch (line) {
			case 0:
				winLine = gameBoard.get("1") + gameBoard.get("2") + gameBoard.get("3");
				break;
			case 1:
				winLine = gameBoard.get("4") + gameBoard.get("5") + gameBoard.get("6");
				break;
			case 2:
				winLine = gameBoard.get("7") + gameBoard.get("8") + gameBoard.get("9");
				break;
			default:
				break;
			}
			if ("XXX".equals(winLine)) {
				return PLAYER_X;
			}
		}
		return null;
	}

}