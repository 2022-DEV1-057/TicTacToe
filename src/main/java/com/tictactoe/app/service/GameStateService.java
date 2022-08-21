package com.tictactoe.app.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.openapi.api.TictactoeApiDelegate;
import com.tictactoe.app.openapi.model.NewGameInfo;

@Service
public class GameStateService implements TictactoeApiDelegate {

	@Override
	public ResponseEntity<NewGameInfo> startNewGame() {
		NewGameInfo newGameInfo= new NewGameInfo();
		return new ResponseEntity<>(newGameInfo, HttpStatus.CREATED);
	}
}
