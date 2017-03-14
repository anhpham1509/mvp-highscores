package com.kuuasema;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;

@SpringBootApplication
public class Application {
    public final static ControllerError PlayerNameConflic = new ControllerError(
            HttpStatus.CONFLICT, "Player name has been registered.");
    public final static ControllerError PlayerNotFound = new ControllerError(
            HttpStatus.NOT_FOUND, "Player not found with the given Id."
    );
    public final static ControllerError GameNotFound = new ControllerError(
            HttpStatus.NOT_FOUND, "Game not found with the given title."
    );
    public final static ControllerError ScoreNotFound = new ControllerError(
            HttpStatus.NOT_FOUND, "Score not found with the given gameTitle."
    );
    public final static ControllerError InvalidRequest = new ControllerError(
            HttpStatus.BAD_REQUEST, "Invalid request parameters"
    );

    public final static String baseUrl = "https://mvp-highscores.herokuapp.com";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
