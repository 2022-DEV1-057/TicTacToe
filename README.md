# Application : TicTacToe (Tic Tac Toe Game)
#Rules to play the game:

- 2 players can play the game at a time.
- Players should use the numbers between 1-9 numbers.
- Players one after another should take a move.
- The game is played on a grid that's 3 squares by 3 squares.
- You are X, your friend is O. Players take turns putting their marks in empty squares.
- The first player to get 3 of her marks in a row (up, down, across, or diagonally) is the winner.
- When all 9 squares are full, the game is over. If no player has 3 marks in a row, the game ends in a tie.

## Requirements

For building and running the application you need:

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.tictactoe.app.TicTacToeApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

Commmandline : First , execute mvn clean compile  then execute following command in respective directory.

```shell
>java -jar target/TicTacToe-0.0.1-SNAPSHOT.jar
```

## Getting Start 
### Testing through Postman
This RESTful service is available to play tictactoe game with below endpoints.


 Start new game: Its a POST request with no input body, below request creates the new game simply.

    HTTP POST Request URL: http://localhost:9090/tictactoe/startNewGame
    
    Response example Json: 
    {
	    "message": "Hello Mr.X and Mr.O your game started!,All the best and enjoy playing",
	    "gameboard": {
	        "1": null,
	        "2": null,
	        "3": null,
	        "4": null,
	        "5": null,
	        "6": null,
	        "7": null,
	        "8": null,
	        "9": null
	    }
    }
   	
 To see players information: Its a GET request with no input body, below is the example.

    HTTP GET Request URL: http://localhost:9090/tictactoe-players/info
    
    Response example Json: 
    [
	    {
	        "id": "X",
	        "description": "PLAYER_X"
	    },
	    {
	        "id": "O",
	        "description": "PLAYER_O"
	    }
    ]
   	