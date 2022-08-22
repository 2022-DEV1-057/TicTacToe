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
	@Override
	public ResponseEntity<List<Player>> getPlayersInfo() {
		List<Player> playerList = new ArrayList<>();
		Player playerOne = new Player();
		playerOne.setId("X");
		playerOne.setDescription("Player X");
		Player playerTwo = new Player();
		playerTwo.setId("O");
		playerTwo.setDescription("Player O");
		playerList.add(playerOne);
		playerList.add(playerTwo);
		return new ResponseEntity<>(playerList, HttpStatus.OK);
	}
}
