package GoClient;

import junit.framework.TestCase;
import static org.mockito.Mockito.*;

import java.util.Random;

public class GameBoardTest extends TestCase {

	private BoardFrame frame;
	private GameBoard board;
	private final int smallBoardSize = 9;
	@SuppressWarnings("unused")
	private final int mediumBoardSize = 13;
	@SuppressWarnings("unused")
	private final int bigBoardSize = 19;
	private int selectedGameSize;
	
	public void setUp(){
		frame = mock(BoardFrame.class);
		frame.playerColor = 'B';
		//TODO: change below to medium/big to test other board sizes in all tests
		frame.windowHeight = BoardFrameBuilder.smallBoardWindowHeight;	// here
		frame.windowLength = BoardFrameBuilder.smallBoardWindowLength;	// here
		selectedGameSize = smallBoardSize;								// here
		board = new GameBoard(frame, selectedGameSize);					
	}
	
	public void testInit(){
		assertNotNull(board.alphaBStoneBI);
		assertNotNull(board.alphaWStoneBI);
		assertNotNull(board.BStoneBI);
		assertNotNull(board.WStoneBI);
		assertNotNull(board.boardBI);
		assertNotNull(board.g2d);
		assertNotNull(board.gameSize);
		assertNotNull(board.newStone);
		assertNotNull(board.stoneSize);
	}
	
	public void testCornerMoveValidation(){		
		/* Shape (x - stones, o - empty fieds):
		   xooox
		   ooooo
		   xooox
		*/
		int stonePositionX = 0;
		int stonePositionY = 0;
		int x = stonePositionX * board.stoneSize - (board.stoneSize/2) + 40;
		int y = stonePositionY * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue( board.checkMoveValidity(x, y) );	// board[0][0]
		stonePositionX = 0;
		stonePositionY = board.gameSize - 1;
		x = stonePositionX * board.stoneSize - (board.stoneSize/2) + 40;
		y = stonePositionY * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue( board.checkMoveValidity(x, y) );	// board[0][gameSize-1]
		stonePositionX = board.gameSize - 1;
		stonePositionY = 0;
		x = stonePositionX * board.stoneSize - (board.stoneSize/2) + 40;
		y = stonePositionY * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue( board.checkMoveValidity(x, y) );	// board[gameSize-1][0]
		stonePositionX = board.gameSize - 1;
		stonePositionY = board.gameSize - 1;
		x = stonePositionX * board.stoneSize - (board.stoneSize/2) + 40;
		y = stonePositionY * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue( board.checkMoveValidity(x, y) );	// board[gameSize-1][gameSize-1]
	}
	
	public void testPutOpponentStone(){
		Random rand = new Random();
		int stonePositionX = rand.nextInt(selectedGameSize);
		int stonePositionY = rand.nextInt(selectedGameSize);	
		Stone stoneBoard[][] = board.getBoard();
		assertTrue( stoneBoard[stonePositionX][stonePositionY].color == 'N');
		board.putOpponentStone(stonePositionX, stonePositionY);
		if(frame.playerColor == 'W')
			assertEquals('B', stoneBoard[stonePositionX][stonePositionY].color);
		else
			assertEquals('W', stoneBoard[stonePositionX][stonePositionY].color);
	}
	
	public void testSuicide(){
		/* Shape (w - white stones, ! - black stone placement, o - empty fieds):
		   !woooo    	owoooo
		   wooooo  ->	wooooo 
		   oooooo 		oooooo
		   oooooo 		oooooo
		 */
		int stonePositionX = 0;
		int stonePositionY = 1;
		board.putOpponentStone(stonePositionX, stonePositionY);
		stonePositionX = 1;
		stonePositionY = 0;
		board.putOpponentStone(stonePositionX, stonePositionY);
		stonePositionX = 0;
		stonePositionY = 0;
		assertFalse(board.checkMoveValidity(stonePositionX, stonePositionY));	// check if move is valid for system
		Stone stoneBoard[][] = board.getBoard();
		assertTrue( stoneBoard[stonePositionX][stonePositionY].color == 'N');	// check if there is still no stone
	}
	
	public void testKillingMove(){
		/* Shape (w - white stones, x- black stones, o - empty fieds):
		   xwoooo    	owoooo
		   oooooo  ->	wooooo 
		   oooooo 		oooooo
		   oooooo 		oooooo
		 */
		int stonePositionX = 0;
		int stonePositionY = 0;	
		board.putOpponentStone(stonePositionX, stonePositionY);					// opponent stone at 0,0
		int x = 0;
		int y = 1;
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue( board.checkMoveValidity(stonePositionX, stonePositionY));	// player stone at 0,1
		x = 1;
		y = 0;
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue(board.checkMoveValidity(stonePositionX, stonePositionY));	// player stone at 1,0 - should kill
		x = 0;
		y = 0;
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		assertTrue(board.getStoneFromBoard(x, y).color == 'N');					// field should be empty
		assertTrue(board.checkMoveValidity(stonePositionX, stonePositionY));	// nothing blocks player from playing his stone there
	}
	
	public void testKillingStoneChainMove(){
		/* Shape (w - white stones, x- black stones, o - empty fieds):
		   owwooo    	owwooo
		   wxxooo  ->	wooWoo 
		   owwooo 		owwooo
		   oooooo 		oooooo
		 */
		int stonePositionX = 1;
		int stonePositionY = 1;	
		board.putOpponentStone(stonePositionX, stonePositionY);					
		stonePositionX = 1; stonePositionY = 2;	
		board.putOpponentStone(stonePositionX, stonePositionY);
		// opponent stones at 1,1 ; 1,2
		int x = 0; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		int y = 1; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 0,1
		x = 0; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 2; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 0,2
		x = 1; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 0; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 1,0
		x = 2; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 1; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 2,1
		x = 2; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 2; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 2,2
		// killing stone:
		x = 1; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 3; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 1,3
		board.deleteDeadStones(x, y);
		assertEquals('N', board.getStoneFromBoard(1, 1).color); // check if stone at 1,1 was killed
		assertEquals('N', board.getStoneFromBoard(1, 2).color); // check if stone at 1,2 was killed
	}
	
	public void testKillingMultipleMove(){
		/* Shape (w - white stones, x- black stones, o - empty fieds):
		   oxwooo    	Wowooo
		   xwoooo  ->	owoooo 
		   xwoooo 		owoooo
		   wooooo 		wooooo
		 */
		int stonePositionX = 0;
		int stonePositionY = 1;	
		board.putOpponentStone(stonePositionX, stonePositionY);					
		stonePositionX = 1; stonePositionY = 0;	
		board.putOpponentStone(stonePositionX, stonePositionY);			
		stonePositionX = 2; stonePositionY = 0;	
		board.putOpponentStone(stonePositionX, stonePositionY);
		// opponent stones at 0,1 ; 1,0 ; 2,0
		int x = 3; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		int y = 0; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 3,0
		x = 2; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 1; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 2,1
		x = 1; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 1; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 1,1
		x = 0; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 2; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 0,2
		// killing stone:
		x = 0; stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		y = 0; stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY); // player stone at 0,0 - killing
		board.deleteDeadStones(x, y);
		assertEquals('N', board.getStoneFromBoard(0, 1).color); // check if stone at 0,1 was killed
		assertEquals('N', board.getStoneFromBoard(1, 0).color); // check if stone at 1,0 was killed
		assertEquals('N', board.getStoneFromBoard(2, 0).color); // check if stone at 2,0 was killed
	}
	
	public void testKo(){		
		/* Shape (w - white stones, x- black stones, !- illegal white move o - empty fieds):
		   oxoooo		oxoooo   	oxoooo
		   xwxooo  ->	xoxooo	 ->	x!xooo
		   wowooo 		wxwooo 		wxwooo
		   owoooo 		owoooo		owoooo
		 */
		int stonePositionX = 0, stonePositionY = 1;	
		board.putOpponentStone(stonePositionX, stonePositionY);		
		stonePositionX = 1;	stonePositionY = 0;	
		board.putOpponentStone(stonePositionX, stonePositionY);	
		stonePositionX = 1; stonePositionY = 2;	
		board.putOpponentStone(stonePositionX, stonePositionY);				
		// opponent stones at 0,1; 1,0 ; 1,2
		int x = 2;
		int y = 0;
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY);
		x = 2;
		y = 2;
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY);
		x = 3;
		y = 1;
		stonePositionX = y * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = x * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY);
		x = 1;
		y = 1;
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		board.checkMoveValidity(stonePositionX, stonePositionY);
		// player stones at 2,0 ; 2,2 ; 3,1	; 1,1
		stonePositionX = 2; stonePositionY = 1;	
		board.putOpponentStone(stonePositionX, stonePositionY);		
		board.deleteDeadStones(stonePositionX, stonePositionY);	
		// opponent stone at 2,1; - killing stone at 1,1
		assertTrue(board.getStoneFromBoard(stonePositionX, stonePositionY).color == 'W');
		assertEquals('N', board.getStoneFromBoard(1,1).color);
		x = 1; // only for clarity
		y = 1; // only for clarity
		stonePositionX = x * board.stoneSize - (board.stoneSize/2) + 40;
		stonePositionY = y * board.stoneSize - (board.stoneSize/2) + 40;
		assertFalse( board.checkMoveValidity(stonePositionX, stonePositionY) );
		// player stone at 1,1 - illegal because of ko rule	
	}
	
}
