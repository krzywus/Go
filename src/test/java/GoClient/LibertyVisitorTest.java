package GoClient;

import junit.framework.TestCase;

/** LibertyVisitor class public methods: deleteDeadStones, isSuicidal
 * 	are tested in GameBoardTest - GameBoard class is the only method using them
 *  and is easier to test them in working environment.
 *  In this class tested will only be tested main purpose of LibertyVisitor class: liberty counting.*/

public class LibertyVisitorTest extends TestCase {

	
	private Stone stoneBoard[][];
	private int gameSize;
	private LibertyVisitor visitor;
	
	public void setUp(){
		gameSize = 9;
		stoneBoard = new Stone[gameSize][gameSize];
		for(int i = 0; i <  gameSize; i++){
			for(int j = 0; j <  gameSize; j++){
				stoneBoard[i][j] = new Stone('N', i, j);
			}
		}
	}
	
	public void testLibertyCountOnEdge(){
		/* Shape (x - stones, o - empty fieds):
		   xoooo
		   ooooo
		   ooooo
		*/
		int stonePositionX = 0;
		int stonePositionY = 0;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[stonePositionX][stonePositionY].accept(visitor);
		assertEquals(2, stoneBoard[stonePositionX][stonePositionY].liberty);
	}
	
	public void testLibertyCountInTheMiddler(){		
		/* Shape (x - stones, o - empty fieds):
		   ooooo
		   oxooo
		   ooooo
		 */
		int stonePositionX = 1;
		int stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[stonePositionX][stonePositionY].accept(visitor);
		assertEquals(4, stoneBoard[stonePositionX][stonePositionY].liberty);
	}
	
	public void testLibertyCountOfStoneChainOnEdge(){
		// stones at 0,0 ; 0,1		
		/* Shape (x - stones, o - empty fieds):
		   xxoooo
		   oooooo
		   oooooo
		   oooooo 
		 */
		int stonePositionX = 0;
		int stonePositionY = 0;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		stonePositionX = 0;
		stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[stonePositionX][stonePositionY].accept(visitor);
		assertEquals(3, stoneBoard[stonePositionX][stonePositionY].liberty);
	}
	
	public void testLibertyCountOfStoneChainInTheMiddle(){
		// stones at 1,1 ; 1,2 ; 1,3 ; 2,2
		/* Shape (x - stones, o - empty fieds):
		   oooooo
		   oxxxoo
		   ooxooo
		   oooooo 
		 */
		int stonePositionX = 1;
		int stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		stonePositionX = 1;
		stonePositionY = 2;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		stonePositionX = 1;
		stonePositionY = 3;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		stonePositionX = 2;
		stonePositionY = 2;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[stonePositionX][stonePositionY].accept(visitor);
		assertEquals(8, stoneBoard[stonePositionX][stonePositionY].liberty);
	}
	
	public void testIfEnemyStoneTakesLiberties(){	// test both white and black
		/* Shape (w - white stones, x- black stones, o - empty fieds):
		   oooooo
		   owoooo 
		   owxooo
		   oxoooo
		 */
		int stonePositionX = 1;
		int stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		stonePositionX = 2;
		stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		stonePositionX = 2;
		stonePositionY = 2;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('B', stonePositionX, stonePositionY);
		stonePositionX = 3;
		stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('B', stonePositionX, stonePositionY);
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[stonePositionX][stonePositionY].accept(visitor);
		assertEquals(3, stoneBoard[stonePositionX][stonePositionY].liberty); // black stone at 3,1
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[2][2].accept(visitor);
		assertEquals(3, stoneBoard[2][2].liberty); 							 // black stone at 2,2
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[1][1].accept(visitor);
		assertEquals(4, stoneBoard[1][1].liberty); // white stoneChain at 1,1
		
	}
	
	public void testIfEnemyStoneChainTakesLiberties(){
		/* Shape (w - white stones, x- black stones, o - empty fieds):
		   oooooo
		   oxwxoo
		   oxxooo
		   oooooo 
		 */
		int stonePositionX = 1;
		int stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('B', stonePositionX, stonePositionY);
		stonePositionX = 2;
		stonePositionY = 1;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('B', stonePositionX, stonePositionY);
		stonePositionX = 2;
		stonePositionY = 2;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('B', stonePositionX, stonePositionY);
		stonePositionX = 1;
		stonePositionY = 3;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('B', stonePositionX, stonePositionY);
		stonePositionX = 1;
		stonePositionY = 2;
		stoneBoard[stonePositionX][stonePositionY] = new Stone('W', stonePositionX, stonePositionY);
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[2][1].accept(visitor);
		assertEquals(6, stoneBoard[2][1].liberty); // black stoneChain at 2,1
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[1][3].accept(visitor);
		assertEquals(3, stoneBoard[1][3].liberty); 							 // black stone at 1,3
		visitor = new LibertyVisitor(stoneBoard, gameSize);
		stoneBoard[1][2].accept(visitor);
		assertEquals(1, stoneBoard[1][2].liberty); 							 // white stone at 1,1
	}
}
