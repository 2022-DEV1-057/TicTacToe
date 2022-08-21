package com.tictactoe.app.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class GameStateServiceTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void checkStartNewGameServiceAvailable() throws Exception {
		this.mockMvc.perform(post("/tictactoe/startNewGame")).andExpect(status().is(201));
	}
}
