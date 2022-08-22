package com.tictactoe.app.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tictactoe.app.dao.PlayerData;
import com.tictactoe.app.openapi.api.TictactoePlayersApiDelegate;
import com.tictactoe.app.openapi.model.Player;

@Service
public class PlayerService implements TictactoePlayersApiDelegate {

	@Override
	public ResponseEntity<List<Player>> getPlayersInfo() {
		return new ResponseEntity<>(PlayerData.getPlayingTeamInformationList(), HttpStatus.OK);
	}
}
