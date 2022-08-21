package com.tictactoe.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.app.openapi.model.NewGameInfo;

@SpringBootTest
@AutoConfigureMockMvc
public class GameStateServiceTest {
	private static final String START_NEW_GAME_ENDPOINT="/tictactoe/startNewGame";
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void checkStartNewGameServiceAvailable() throws Exception {
		this.mockMvc.perform(post(START_NEW_GAME_ENDPOINT)).andExpect(status().is(201));
	}

	@Test
	public void checkNewGameBoardReady() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(START_NEW_GAME_ENDPOINT);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);
		assertEquals(getDefaultGameBoard(), gameBoardInfo.getGameboard());
	}

	@Test
	public void displayNewGameBoardStartMessage() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(START_NEW_GAME_ENDPOINT);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);
		assertEquals("Hello Mr.X and Mr.O your game started!,All the best and enjoy playing",
				gameBoardInfo.getMessage());
	}
	public Map<String, String> getDefaultGameBoard() {
		Map<String, String> defaultGameBoard = new HashMap<>();
		defaultGameBoard.put("1", null);
		defaultGameBoard.put("2", null);
		defaultGameBoard.put("3", null);
		defaultGameBoard.put("4", null);
		defaultGameBoard.put("5", null);
		defaultGameBoard.put("6", null);
		defaultGameBoard.put("7", null);
		defaultGameBoard.put("8", null);
		defaultGameBoard.put("9", null);
		return defaultGameBoard;
	}
}
