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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.tictactoe.app.openapi.model.NewGameInfo;
import com.tictactoe.app.openapi.model.Player;
import com.tictactoe.app.openapi.model.TurnRequest;
import com.tictactoe.app.openapi.model.TurnResponse;

@SpringBootTest
@AutoConfigureMockMvc
class GameStateServiceTest {
	private static final String START_NEW_GAME_ENDPOINT = "/tictactoe/startNewGame";
	private static final String PLAYER_TURN_ENDPOINT = "/tictactoe/playerTurn";
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private GameStateService gameStateService;

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

	@Test
	void updateFirstPlayerFirstMove() throws Exception {
		Map<String, String> expectedGameBoard = getDefaultGameBoard();
		gameStateService.setGameBoard(expectedGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_X, 1));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setState(expectedGameBoard);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());
	}

	@Test
	void ShouldNotUpdateSamePosition() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_X);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_O, 1));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	void SamePlayerShouldNotTakeContinuousTurnsOnStartUp() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_O);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_O, 1));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	void SamePlayerShouldNotTakeContinuousTurnsOnAnyStateOfGmeByPlayerX() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_TWO_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_THREE_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_SIX_ON_GAME_BOARD, PLAYER_X);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_X, 5));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	void SamePlayerShouldNotTakeContinuousTurnsOnAnyStateOfGmeByPlayerO() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_TWO_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_THREE_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_SIX_ON_GAME_BOARD, PLAYER_O);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_O, 5));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(requestBuilder).andExpect(status().is(400));
	}

	@Test
	void checkHorizontalWinningToPlayerX() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_TWO_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_FIVE_ON_GAME_BOARD, PLAYER_O);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_X, 3));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_X);
		expectedwinner.setDescription(PLAYER_DESCRIPTION_X);
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setState(existingGameBoard);
		expectedResponse.setWinner(expectedwinner);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());
	}

	@Test
	void checkHorizontalWinningToPlayerO() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_TWO_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_FIVE_ON_GAME_BOARD, PLAYER_X);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_O, 3));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_O);
		expectedwinner.setDescription(PLAYER_DESCRIPTION_O);
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setState(existingGameBoard);
		expectedResponse.setWinner(expectedwinner);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());
	}

	@Test
	void checkVerticalWinningToPlayer() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_TWO_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_FIVE_ON_GAME_BOARD, PLAYER_O);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_X, 7));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_X);
		expectedwinner.setDescription(PLAYER_DESCRIPTION_X);
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setState(existingGameBoard);
		expectedResponse.setWinner(expectedwinner);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());
	}

	@Test
	void checkDiagonalWinningToPlayer() throws Exception {
		Map<String, String> existingGameBoard = getDefaultGameBoard();
		existingGameBoard.put(POSITION_ONE_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_THREE_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_TWO_ON_GAME_BOARD, PLAYER_X);
		existingGameBoard.put(POSITION_FIVE_ON_GAME_BOARD, PLAYER_O);
		existingGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, PLAYER_X);
		gameStateService.setGameBoard(existingGameBoard);
		ObjectWriter ow = new ObjectMapper().writer();
		String inputRequestJson = ow.writeValueAsString(prepareInputTurnRequest(PLAYER_O, 7));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(PLAYER_TURN_ENDPOINT)
				.accept(MediaType.APPLICATION_JSON).content(inputRequestJson).contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		MockHttpServletResponse responseActual = result.getResponse();
		Player expectedwinner = new Player();
		expectedwinner.setId(PLAYER_O);
		expectedwinner.setDescription(PLAYER_DESCRIPTION_O);
		TurnResponse expectedResponse = new TurnResponse();
		expectedResponse.setState(existingGameBoard);
		expectedResponse.setWinner(expectedwinner);
		assertEquals(ow.writeValueAsString(expectedResponse), responseActual.getContentAsString());
	}

	public Map<String, String> getDefaultGameBoard() {
		Map<String, String> defaultGameBoard = new HashMap<>();
		defaultGameBoard.put(POSITION_ONE_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_TWO_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_THREE_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_FOUR_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_FIVE_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_SIX_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_SEVEN_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_EIGHT_ON_GAME_BOARD, null);
		defaultGameBoard.put(POSITION_NINE_ON_GAME_BOARD, null);
		return defaultGameBoard;
	}

	public NewGameInfo convertResponseJsonToNewGameInfoObject(MvcResult result)
			throws UnsupportedEncodingException, JsonMappingException, JsonProcessingException {
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		NewGameInfo gameBoardInfo = objectMapper.readValue(responseBody, NewGameInfo.class);
		return gameBoardInfo;

	}

	public TurnRequest prepareInputTurnRequest(String playerId, int position) {
		TurnRequest turnRequest = new TurnRequest();
		turnRequest.setPlayerId(playerId);
		turnRequest.setPosition(position);
		return turnRequest;
	}
}
