package com.tictactoe.app.service;

import static com.tictactoe.app.utility.ConstantUtility.GAME_DRAW;
import static com.tictactoe.app.utility.ConstantUtility.GAME_DRAW_DESCRIPTION;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_DESCRIPTION_O;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_DESCRIPTION_X;
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
import static com.tictactoe.app.utility.ConstantUtility.WIN_LINE_PLAYER_O;
import static com.tictactoe.app.utility.ConstantUtility.WIN_LINE_PLAYER_X;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tictactoe.app.openapi.model.Player;

@Component
public class GameBoardChecker {
	private static Logger log = LoggerFactory.getLogger(GameBoardChecker.class);

	public boolean stoppingSamePlayerTakingContinuousTurns(String playerName, Map<String, String> gameBoard) {
		log.info("--:Validating any player taking continuous turns:--");
		Map<String, Long> playersCountWithMovesMap = gameBoard.values().stream().filter(Objects::nonNull)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		if (playersCountWithMovesMap.size() == 2) {
			return playersGivingWrongMovesMiddleOfGame(playersCountWithMovesMap, playerName);
		} else if (playersCountWithMovesMap.size() == 1 && playersCountWithMovesMap.containsKey(playerName)) {
			log.error("--:Player-{} taking continuous turn wrongly:--", playerName);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;

	}

	public Player findWinner(Map<String, String> currentStateOfGameBoard) {
		String winner = checkWinningPossibility(currentStateOfGameBoard);
		Player playerWinner;
		switch (winner) {
		case PLAYER_X:
			playerWinner = new Player();
			playerWinner.setId(winner);
			playerWinner.setDescription(PLAYER_DESCRIPTION_X);
			break;
		case PLAYER_O:
			playerWinner = new Player();
			playerWinner.setId(winner);
			playerWinner.setDescription(PLAYER_DESCRIPTION_O);
			break;
		case GAME_DRAW:
			playerWinner = new Player();
			playerWinner.setId(GAME_DRAW);
			playerWinner.setDescription(GAME_DRAW_DESCRIPTION);
			break;
		default:
			playerWinner = null;
		}
		return playerWinner;
	}

	public String checkWinningPossibility(Map<String, String> gameBoard) {
		int possibleWinLineCount = 8;
		for (int line = 0; line < possibleWinLineCount; line++) {
			String winLine = null;
			switch (line) {
			case 0:
				winLine = gameBoard.get(POSITION_ONE_ON_GAME_BOARD) + gameBoard.get(POSITION_TWO_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_THREE_ON_GAME_BOARD);
				break;
			case 1:
				winLine = gameBoard.get(POSITION_FOUR_ON_GAME_BOARD) + gameBoard.get(POSITION_FIVE_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_SIX_ON_GAME_BOARD);
				break;
			case 2:
				winLine = gameBoard.get(POSITION_SEVEN_ON_GAME_BOARD) + gameBoard.get(POSITION_EIGHT_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_NINE_ON_GAME_BOARD);
				break;
			case 3:
				winLine = gameBoard.get(POSITION_ONE_ON_GAME_BOARD) + gameBoard.get(POSITION_FOUR_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_SEVEN_ON_GAME_BOARD);
				break;
			case 4:
				winLine = gameBoard.get(POSITION_TWO_ON_GAME_BOARD) + gameBoard.get(POSITION_FIVE_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_EIGHT_ON_GAME_BOARD);
				break;
			case 5:
				winLine = gameBoard.get(POSITION_THREE_ON_GAME_BOARD) + gameBoard.get(POSITION_SIX_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_NINE_ON_GAME_BOARD);
				break;
			case 6:
				winLine = gameBoard.get(POSITION_ONE_ON_GAME_BOARD) + gameBoard.get(POSITION_FIVE_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_NINE_ON_GAME_BOARD);
				break;
			case 7:
				winLine = gameBoard.get(POSITION_THREE_ON_GAME_BOARD) + gameBoard.get(POSITION_FIVE_ON_GAME_BOARD)
						+ gameBoard.get(POSITION_SEVEN_ON_GAME_BOARD);
				break;
			default:
				break;
			}
			if (WIN_LINE_PLAYER_X.equals(winLine)) {
				return PLAYER_X;
			} else if (WIN_LINE_PLAYER_O.equals(winLine)) {
				return PLAYER_O;
			}
		}
		if (isGameDraw(gameBoard)) {
			return GAME_DRAW;
		}

		return "";
	}

	public boolean isGameDraw(Map<String, String> gameBoard) {
		int movesCountWithNoWinng = gameBoard.values().stream().filter(Objects::nonNull).collect(Collectors.toList())
				.size();
		return movesCountWithNoWinng == 9;
	}

	public Map<String, String> getInitialGameBoardOnNewGameStartUp() {
		Map<String, String> defaultNewGameStartBoard = new HashMap<>();
		defaultNewGameStartBoard.put(POSITION_ONE_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_TWO_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_THREE_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_FOUR_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_FIVE_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_SIX_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_SEVEN_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_EIGHT_ON_GAME_BOARD, null);
		defaultNewGameStartBoard.put(POSITION_NINE_ON_GAME_BOARD, null);
		return defaultNewGameStartBoard;
	}

	public boolean playersGivingWrongMovesMiddleOfGame(Map<String, Long> playersCountWithMovesMap, String playerName) {
		long firstPlayerOccupencyCount = playersCountWithMovesMap.get(PLAYER_X);
		long secondPlayerOccupencyCount = playersCountWithMovesMap.get(PLAYER_O);
		long difference = firstPlayerOccupencyCount > secondPlayerOccupencyCount
				? firstPlayerOccupencyCount - secondPlayerOccupencyCount
				: secondPlayerOccupencyCount - firstPlayerOccupencyCount;
		if (difference > 0 && ((firstPlayerOccupencyCount > secondPlayerOccupencyCount && PLAYER_X.equals(playerName))
				|| (secondPlayerOccupencyCount > firstPlayerOccupencyCount && PLAYER_O.equals(playerName)))) {
			log.info("--:Player-{} has taken wrong turn:--", playerName);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
