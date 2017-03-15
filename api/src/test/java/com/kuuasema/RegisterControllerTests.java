package com.kuuasema;

import com.google.gson.Gson;
import com.kuuasema.model.Player;
import com.kuuasema.repository.GameRepository;
import com.kuuasema.repository.PlayerRepository;
import com.kuuasema.repository.ScoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisterControllerTests {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private String registerEndpoint = "/register";

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.scoreRepository.deleteAll();
        this.playerRepository.deleteAll();
        this.gameRepository.deleteAll();

        this.playerRepository.save(new Player("Matti"));
    }

    Gson gson = new Gson();

    private class RegisterParams {
        private String playerName;

        public RegisterParams(String playerName) {
            this.playerName = playerName;
        }

        public String getPlayerName() {
            return playerName;
        }
    }

    @Test
    public void registerSuccess() throws Exception {
        mockMvc.perform(post(this.registerEndpoint)
                .content(gson.toJson(new RegisterParams("hihi")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerFailConflict() throws Exception {
        mockMvc.perform(post(this.registerEndpoint)
                .content(gson.toJson(new RegisterParams("Matti")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void registerFailInvalidParams() throws Exception {
        mockMvc.perform(post(this.registerEndpoint)
                .content(gson.toJson(new RegisterParams("")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
