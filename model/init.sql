CREATE TABLE game
(
  id character varying(36) NOT NULL,
  title character varying(512) NOT NULL,
  CONSTRAINT game_pkey PRIMARY KEY (id),
  CONSTRAINT unique_title UNIQUE (title)
);

CREATE TABLE player
(
  id character varying(36) NOT NULL,
  name character varying(512) NOT NULL,
  CONSTRAINT player_pkey PRIMARY KEY (id),
  CONSTRAINT unique_name UNIQUE (name)
);

CREATE TABLE score
(
  id character varying(36) NOT NULL,
  game_id character varying(36) NOT NULL,
  player_id character varying(36) NOT NULL,
  score numeric NOT NULL,
  CONSTRAINT score_pkey PRIMARY KEY (id),
  CONSTRAINT score_game_id_game_id_fkey FOREIGN KEY (game_id) REFERENCES game (id),
  CONSTRAINT score_player_id_player_id_fkey FOREIGN KEY (player_id) REFERENCES player (id)
);