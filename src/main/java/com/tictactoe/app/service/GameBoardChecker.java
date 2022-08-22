package com.tictactoe.app.service;

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

	public Player findWinner(Map<String, String> currentStateOfGameBoard) {
		String winner = checkWinningPossibility(currentStateOfGameBoard);
		Player playerWinner;
		if (winner != null) {
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
			default:
				playerWinner = null;
			}
			return playerWinner;
		}
		return null;
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
			if ("XXX".equals(winLine)) {
				return PLAYER_X;
			} else if ("OOO".equals(winLine)) {
				return PLAYER_O;
			}
		}
		return null;
	}

}
