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
	public static final String PLAYER_X = "X";
	public static final String PLAYER_O = "O";
	public static final String PLAYER_ONE = "Player X";
	public static final String PLAYER_TWO = "Player O";

	@Override
	public ResponseEntity<List<Player>> getPlayersInfo() {
		List<Player> playerList = new ArrayList<>();
		Player playerOne = new Player();
		playerOne.setId(PLAYER_X);
		playerOne.setDescription(PLAYER_ONE);
		Player playerTwo = new Player();
		playerTwo.setId(PLAYER_O);
		playerTwo.setDescription(PLAYER_TWO);
		playerList.add(playerOne);
		playerList.add(playerTwo);
		return new ResponseEntity<>(playerList, HttpStatus.OK);
	}
}
