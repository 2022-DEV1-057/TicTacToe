package com.tictactoe.app.dao;

import static com.tictactoe.app.utility.ConstantUtility.PLAYER_DESCRIPTION_O;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_DESCRIPTION_X;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_O;
import static com.tictactoe.app.utility.ConstantUtility.PLAYER_X;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.tictactoe.app.openapi.model.Player;

@Component
public class PlayerData {
	
	private PlayerData() {
		
	}
	public static List<Player>  getPlayingTeamInformationList() {
		List<Player> playerList = new ArrayList<>();
		Player playerX = new Player();
		playerX.setId(PLAYER_X);
		playerX.setDescription(PLAYER_DESCRIPTION_X);
		Player playerO = new Player();
		playerO.setId(PLAYER_O);
		playerO.setDescription(PLAYER_DESCRIPTION_O);
		playerList.add(playerX);
		playerList.add(playerO);
		return playerList;

	}
}
