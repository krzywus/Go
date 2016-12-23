# Go

####Student project implementing board game Go in Java.

Game uses client-server architecture. Clients connect with server and communicate with it by ClientHandler class.<br />
Players can choose board size in settings menu. Playing against AI is not yet implemented.<br />
After joining matchmaking player wait for second player to join with the same board configuration.<br />
Black player is chosen randomly and begins game. Move validation happens on the client side, not the server.<br />
After both players pass in a row, game goes to territory bargain state in which black player selects dead stones on the
board and sends his proposition to white. White then can either decline the offer or accept and end the game. If he 
declines, its his turn to make proposition to black. If any player accepts other side offer, game ends.<br />
Counting territory is not yet implemented, so its up to players to conclude who won.<br />

#####To try game: <br />
1. Run GoServer<br />
2. Run GoClient and start game with selected settings.<br />
3. Run another GoClient.

####Used design patterns: <br />
In class GameSession - state.<br />
In class Matchmaker - singleton.<br />
In class LibertyVisitor - visitor.<br />
In AI player implementation - strategy ( not yet implemented into system )<br />


####AI<br />
Bot implements strategy design pattern in which he chooses strategy to evaluate every field on the map.<br />
Then he chooses move from one of the highest evalueted fields. <br />
Evaluating fields is almost completely implemented as well as 2 sample strategies. <br />
However bot is not yet connected to system in any way. Possible is only testing strategies (how they evaluate field
in bots Main class by edditing board on which bots computes ( you have to edit it by hand ).

#####Documentation<br />
In docs folder you can find few UML diagrams ilustrating the sytem.<br />


#####Testing<br />
Project contains few JUnit test, few of which use Mockito.
