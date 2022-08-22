package com.tictactoe.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoePlayersApiDelegate;
import com.tictactoe.app.openapi.model.Player;

@Service
public class PlayerService implements TictactoePlayersApiDelegate {
	private static final String PLAYER_X = "X";
	private static final String PLAYER_O = "O";
	private static final String PLAYER_ONE = "Player X";
	private static final String PLAYER_TWO = "Player O";

	@Override
	public ResponseEntity<List<Player>> getPlayersInfo() {

		return new ResponseEntity<>(getPlayingTeamInformationList(), HttpStatus.OK);
	}

	private List<Player> getPlayingTeamInformationList() {
		List<Player> playerList = new ArrayList<>();
		Player playerX = new Player();
		playerX.setId(PLAYER_X);
		playerX.setDescription(PLAYER_TWO);
		Player playerO = new Player();
		playerO.setId(PLAYER_O);
		playerO.setDescription(PLAYER_TWO);
		playerList.add(playerX);
		playerList.add(playerO);
		return playerList;

	}
}
