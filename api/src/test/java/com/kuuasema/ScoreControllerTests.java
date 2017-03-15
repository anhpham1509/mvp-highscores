package com.kuuasema;

import com.google.gson.Gson;
import com.kuuasema.model.Game;
import com.kuuasema.model.Player;
import com.kuuasema.model.Score;
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

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScoreControllerTests {

	@Autowired
	private GameRepository gameRepository;

	@Autowired
	private PlayerRepository playerRepository;

	@Autowired
	private ScoreRepository scoreRepository;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	private String scoreEndpoint = "/score";

	private Game game;
	private Player player;
	private Score score;

	Random r = new Random();
	Gson gson = new Gson();

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		this.scoreRepository.deleteAll();
		this.playerRepository.deleteAll();
		this.gameRepository.deleteAll();

		gameRepository.save(new Game("Township"));
		this.game = gameRepository.save(new Game("Mario"));
		this.player = playerRepository.save(new Player("Princess"));
		this.score = scoreRepository.save(new Score(player, game, (int)(666 * r.nextDouble() * 100) / 100.0));
	}

	private class PostScoreParams {
		private String playerId;
		private String gameTitle;
		private Double score;

		public PostScoreParams(String playerId, String gameTitle, Double score) {
			this.playerId = playerId;
			this.gameTitle = gameTitle;
			this.score = score;
		}

		public String getPlayerId() {
			return playerId;
		}

		public String getGameTitle() {
			return gameTitle;
		}

		public Double getScore() {
			return score;
		}
	}

	private class DeleteScoreParams {
		private String playerId;
		private String gameTitle;

		public DeleteScoreParams(String playerId, String gameTitle) {
			this.playerId = playerId;
			this.gameTitle = gameTitle;
		}

		public String getPlayerId() {
			return playerId;
		}

		public String getGameTitle() {
			return gameTitle;
		}
	}

	@Test
	public void submitScoreSuccessCreated() throws Exception{
		mockMvc.perform(post(this.scoreEndpoint)
				.content(gson.toJson(new PostScoreParams(player.getId(), "Township", (int)(666 * r.nextDouble() * 100) / 100.0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	@Test
	public void submitScoreSuccessOk() throws Exception{
		mockMvc.perform(post(this.scoreEndpoint)
				.content(gson.toJson(new PostScoreParams(player.getId(), game.getTitle(), (int) (666 * r.nextDouble() * 100) / 100.0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void submitScorePlayerNotFound() throws Exception{
		Player newPlayer = new Player("Hello");
		mockMvc.perform(post(this.scoreEndpoint)
				.content(gson.toJson(new PostScoreParams(newPlayer.getId(), game.getTitle(), 2.0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void submitScoreInvalidRequestPlayerId() throws Exception{
		mockMvc.perform(post(this.scoreEndpoint)
				.content(gson.toJson(new PostScoreParams("sample_player_id", game.getTitle(), 2.0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void submitScoreInvalidRequestGameTitle() throws Exception{
		mockMvc.perform(post(this.scoreEndpoint)
				.content(gson.toJson(new PostScoreParams(player.getId(), "", 2.0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void submitScoreInvalidRequestScore() throws Exception{
		mockMvc.perform(post(this.scoreEndpoint)
				.content(gson.toJson(new PostScoreParams(player.getId(), game.getTitle(), -2.0)))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void getGameScoreSuccess() throws Exception{
		mockMvc.perform(get(this.scoreEndpoint + "/game/Mario")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	public void getGameScoreNotFound() throws Exception{
		mockMvc.perform(get(this.scoreEndpoint + "/game/Sample Game")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getPlayerScoreSuccess() throws Exception{
		mockMvc.perform(get(this.scoreEndpoint + "/player/" + player.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
	}

	@Test
	public void getPlayerScorePlayerNotFound() throws Exception{
		Player newPlayer = new Player("Hello");
		mockMvc.perform(get(this.scoreEndpoint + "/player/" + newPlayer.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getPlayerScoreInvalidRequest() throws Exception{
		mockMvc.perform(get(this.scoreEndpoint + "/player/samplePlayerId")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deletePlayerScoreSuccess() throws Exception{
		mockMvc.perform(delete(this.scoreEndpoint)
				.content(gson.toJson(new DeleteScoreParams(player.getId(), game.getTitle())))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void deletePlayerScorePlayerNotFound() throws Exception{
		Player newPlayer = new Player("Hello");
		mockMvc.perform(delete(this.scoreEndpoint)
				.content(gson.toJson(new DeleteScoreParams(newPlayer.getId(), game.getTitle())))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deletePlayerScoreGameNotFound() throws Exception{
		Game newGame = new Game("New Game");
		mockMvc.perform(delete(this.scoreEndpoint)
				.content(gson.toJson(new DeleteScoreParams(player.getId(), newGame.getTitle())))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void deletePlayerScoreNotFound() throws Exception{
		this.scoreRepository.delete(score);

		mockMvc.perform(delete(this.scoreEndpoint)
				.content(gson.toJson(new DeleteScoreParams(player.getId(), game.getTitle())))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	 public void deletePlayerScoreInvalidRequestPlayerId() throws Exception{
		mockMvc.perform(delete(this.scoreEndpoint)
				.content(gson.toJson(new DeleteScoreParams("SamplePlayerId", game.getTitle())))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void deletePlayerScoreInvalidRequestGameTitle() throws Exception{
		mockMvc.perform(delete(this.scoreEndpoint)
				.content(gson.toJson(new DeleteScoreParams(player.getId(), "")))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}
