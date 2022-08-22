package com.tictactoe.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.app.openapi.model.NewGameInfo;

@SpringBootTest
@AutoConfigureMockMvc
class GameStateServiceTest {
	private static final String START_NEW_GAME_ENDPOINT = "/tictactoe/startNewGame";
	@Autowired
	private MockMvc mockMvc;

	@Test
	void checkStartNewGameServiceAvailable() throws Exception {
		this.mockMvc.perform(post(START_NEW_GAME_ENDPOINT)).andExpect(status().is(201));
	}

	@Test
	void checkNewGameBoardReady() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(START_NEW_GAME_ENDPOINT);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals(getDefaultGameBoard(), convertResponseJsonToNewGameInfoObject(result).getGameboard());
	}

	@Test
	void displayNewGameBoardStartMessage() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(START_NEW_GAME_ENDPOINT);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		assertEquals("Hello Mr.X and Mr.O your game started!,All the best and enjoy playing",
				convertResponseJsonToNewGameInfoObject(result).getMessage());
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

	public NewGameInfo convertResponseJsonToNewGameInfoObject(MvcResult result)
			throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);
		return gameBoardInfo;

	}
}
