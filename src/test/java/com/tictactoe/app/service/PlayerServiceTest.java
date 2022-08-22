package com.tictactoe.app.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerServiceTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void checkGetPlayerEndPointServiceAvailable() throws Exception {
		this.mockMvc.perform(get("/tictactoe-players/info")).andExpect(status().is(200));
	}
}
