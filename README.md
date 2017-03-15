# [MVP High Scores](https://mvp-highscores.herokuapp.com/)

## Features  
* Register player  
* Submit high scores  
* Delete high scores  
* List high scores by game or by player  
* Search for player names/ game titles  

## Requirements:  
* Java JDK 1.8  
* Gradle Wrapper 3.4  
* Spring Boot 1.5  

## Config file:  
Spring application properties. E.g.  
`
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect  
spring.datasource.driverClassName=org.postgresql.Driver  
spring.datasource.url=jdbc:postgresql://ec2-54-247-81-76.eu-west-1.compute.amazonaws.com:5432/d991k3ri1rrh6i?sslmode=require  
spring.datasource.username=xympdmnprmrivl  
spring.datasource.password=8b92b53df530eb56ee1f89dea8d8b3d9c0ab2f82587a32ef330087705287ddd5  
`

## API Design  
### Register player  
Register player with the system by providing player name and return UUID for player.   
* **URL**  
	/register  
* **Method**  
	`POST`  
* **URL Parameters**  
	None  
* **Body parameters**  
	`{"playerName": "Matti"}`  
* **Success response**  
	* **Code:** 201 CREATED  
	**Content:** `{"playerId": "f9919de2-3039-4e71-9257-2c9cab27f933"}`  
* **Error response**  
	* **Code:** 400 BAD REQUEST  
	**Content:** `{"error": "Invalid request parameters"}`  
OR  
	* **Code:** 409 CONFLICT  
	**Content:** `{"error": "Player name has been registered."}`  
	
### Submit a high score  
Submit a high score by providing a valid player id, game title and score.   
* **URL**  
	/score  
* **Method**  
	`POST`  
* **URL Parameters**  
	None  
* **Body parameters**  
	`{"playerId": "Matti", "gameTitle": "Mario", "score": 268}`  
* **Success response**  
	* **Code:** 201 CREATED  
	**Content:** `{"playerName": "Matti", "gameTitle": "Mario", "score": 268}`  
OR  
	* **Code:** 200 OK  
	**Content:** `{"playerName": "Matti", "gameTitle": "Mario", "score": 268}`  
* **Error response**  
	* **Code:** 400 BAD REQUEST  
	**Content:** `{"error": "Invalid request parameters"}`  
OR  
	* **Code:** 404 NOT FOUND  
	**Content:** `{"error": "Player not found with the given Id."}`  
	
### Delete a high score  
Delete a high score by providing a valid player id and game title.  
* **URL**  
	/score  
* **Method**  
	`DELETE`  
* **URL Parameters**  
	None  
* **Body parameters**  
	`{"playerId": "Matti", "gameTitle": "Mario"}`  
* **Success response**  
	* **Code:** 200 OK  
	**Content:** None  
* **Error response**  
	* **Code:** 400 BAD REQUEST  
	**Content:** `{"error": "Invalid request parameters"}`  
OR  
	* **Code:** 404 NOT FOUND  
	**Content:** `{"error": "Player not found with the given Id."}`  
OR  
	* **Code:** 404 NOT FOUND  
	**Content:** `{"error": "Game not found with the given title."}`  
	
### Get player high scores  
Get a list of high scores by providing a valid player id.  
* **URL**  
	/score/player/:id  
* **Method**  
	`GET`  
* **URL Parameters**  
	**Required:**  
	id=\[UUID\]  
* **Body parameters**  
	None  
* **Success response**  
	* **Code:** 200 OK  
	**Content:** `[{"gameTitle": "Mario", "score": 268}, {"gameTitle": "Township", "score": 688}]`  
* **Error response**  
	* **Code:** 400 BAD REQUEST  
	**Content:** `{"error": "Invalid request parameters"}`  
OR  
	* **Code:** 404 NOT FOUND  
	**Content:** `{"error": "Player not found with the given Id."}`  
	
### Get game high scores  
Get a list of high scores by providing a game title.  
* **URL**  
	/score/game/:title  
* **Method**  
	`GET`  
* **URL Parameters**  
	**Required:**  
	title=\[String\]  
* **Body parameters**  
	None  
* **Success response**  
	* **Code:** 200 OK  
	**Content:** `[{"playerName": "Matti", "score": 268}, {"playerName": "Johnny", "score": 688}]`  
* **Error response**  
	* **Code:** 404 NOT FOUND  
	**Content:** `{"error": "Game not found with the given title."}`  

### Search player names  
Get a list of player names match the given pattern.  
* **URL**  
	/player?search=pattern  
* **Method**  
	`GET`  
* **Query Parameters**  
	**Required:**  
	search=\[String\]  
* **Body parameters**  
	None  
* **Success response**  
	* **Code:** 200 OK  
	**Content:** `["Matti", "Aava"]`  
* **Error response**  
	None  
	
### Search game title  
Get a list of game titles match the given pattern.  
* **URL**  
	/game?search=pattern  
* **Method**  
	`GET`  
* **Query Parameters**  
	**Required:**  
	search=\[String\]  
* **Body parameters**  
	None  
* **Success response**  
	* **Code:** 200 OK  
	**Content:** `["Mario", "Township"]`  
* **Error response**  
	None  
	
## Known issues  
* At the moment, the players have to securely store player id by their own as there is no way to get it again after registration.  
* Game title and player name are not spacing trimmed, so player can register with the same name and space to duplicate one.  
 
## Development notes:  
* Data validation:  
	* `playerName`, `gameTitle` should not be empty.  
	* `score` should be more than 0.  
	* Controllers tests are included and placed at `api/src/test/java/com/kuuasema`.  

	