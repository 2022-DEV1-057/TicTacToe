package com.tictactoe.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tictactoe.app.openapi.model.Player;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerServiceTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void checkGetPlayerEndPointServiceAvailable() throws Exception {
		this.mockMvc.perform(get("/tictactoe-players/info")).andExpect(status().is(200));
	}

	@Test
	void checkPlayersCount() throws Exception {
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/tictactoe-players/info");
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		String responseBody = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Player> playerList = objectMapper.readValue(responseBody, List.class);
		assertEquals(2, playerList.size());
	}
}
