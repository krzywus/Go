package GoClient;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class GameCommander {

	/** Klient. */
	private GoClient client;
	/** Okno gry. */
	private BoardFrame boardFrame;
	
/*-------------------------------------------------------------------------------------------------------------------*/

	/** Konstruktor klasy. */
	GameCommander(GoClient client, BoardFrame boardFrame){
		this.client = client;
		this.boardFrame = boardFrame;
	}// end GameCommander constr
	
	
	/** Metoda do obslugi zdarzen z gry - przesyla komende do odpowiedniej metody. */
	protected void executeGameCommand(String command){
		if(command.contains("ENABLE")){
			executeEnabledCommand(command);
		}else if(command.contains("ABORTED")){
			executeAbortCommand(command);
		}else if(command.contains("PASS")){
			executePassCommand(command);
		}else if(command.contains("BARGAIN")){ 
			executeBargainCommand(command);
		}else if(command.contains("FINISH")){
			executeFinishCommand(command);
		}
	}// end executeGameCommand
	
	
	/** Metoda wykonuje akcje zwiazana z ruchem gracza. */
	private void executeEnabledCommand(String command){ 
		boardFrame.addMouseListener();
		boardFrame.enableButtons();
		boardFrame.inGameInfo.setText("Your move.");
		if(command.contains("NEWSTONE")){
			int posXIndex = command.indexOf("POSX:"); String posX = command.substring(posXIndex+5, posXIndex+5+2) ;
			int posYIndex = command.indexOf("POSY:"); String posY = command.substring(posYIndex+5, posYIndex+5+2) ;
			boardFrame.putOpponentStone(posX, posY);
			if(command.contains("KILL")){
				boardFrame.deleteDeadStones(posX, posY);
			}
		}	
	}// end executeEnabledCommand
	
	
	/** Metoda wykonuje akcje zwiazana z  */
	private void executeAbortCommand(String command){ 
		boardFrame.removeMouseListener();
		boardFrame.disableButtons();
		boardFrame.inGameInfo.setText("Enemy resigned.");
		String information = "Opponent has left the game. You win.\nDo you wish to leave?";
		int confirm = JOptionPane.showConfirmDialog(null, information, "", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			client.actionPerformed(new ActionEvent(this, 0, "EXIT GAME"));
        }
	}// end executeAbortCommand
	
	
	/** Metoda wykonuje akcje zwiazana z targiem o terytorium. */
	private void executeBargainCommand(String command){
		if(command.contains("START")){
			boardFrame.territoryBargain= true;
			boardFrame.startBargain();				
			boardFrame.boardBuilder.bargainAccept.setEnabled(false);
			boardFrame.boardBuilder.bargainDecline.setEnabled(false);	
			boardFrame.boardBuilder.bargainSend.setEnabled(false);
		}
		if(command.contains("MARK")){ 	// jezeli teraz tura gracza na zaznaczanie
			boardFrame.inGameInfo.setText("<html>Game finished.<br>Mark dead stones.</html>");
			boardFrame.addBargainMouseListener();
			boardFrame.boardBuilder.bargainAccept.setEnabled(false);
			boardFrame.boardBuilder.bargainDecline.setEnabled(false);
			boardFrame.boardBuilder.bargainSend.setEnabled(true);
		}else{
			if(command.contains("WAIT")){ // czekanie na zaznaczenie przez przeciwnka
				if(command.contains("DECLINE")) 
					boardFrame.resetAlphaBoard(); 
				boardFrame.inGameInfo.setText("<html>Game finished.<br>Wait for opponent <br>to mark his stones.</html>");	
				boardFrame.removeBargainMouseListener();				
				boardFrame.boardBuilder.bargainAccept.setEnabled(false);
				boardFrame.boardBuilder.bargainDecline.setEnabled(false);	
				boardFrame.boardBuilder.bargainSend.setEnabled(false);
			}else{						// decyzja o akceptacji zaznaczonych przez przeciwnika kamieni
				boardFrame.resetAlphaBoard(); 
				boardFrame.inGameInfo.setText("<html>Game finished.<br>Accept or decline <br>stones marked by opponent.</html>");
				ArrayList <int[]> selectedStones = getAlphaBoard(command.substring(20)); //"GAME BARGAIN CHOOSE"+Array
				for(int[] s: selectedStones){
					boardFrame.markOpponentProposition(s[0], s[1]);
				}
				boardFrame.boardBuilder.bargainAccept.setEnabled(true);
				boardFrame.boardBuilder.bargainDecline.setEnabled(true);				
			}
		}
	}// end executeBargainCommand
	
	
	/** Metoda wykonuje akcje zwiazana z pasowaniem gracza */
	private void executePassCommand(String command){ 
		boardFrame.addMouseListener();
		boardFrame.enableButtons();
		boardFrame.inGameInfo.setText("Enemy passed.");
	}// end executePassCommand
	
	
	/** Metoda wykonuje akcje zwiazana z  */
	private void executeFinishCommand(String command){ 
		boardFrame.disableButtons();
		String text;
		if(command.contains("WIN")) text = "<html>Game finished.<br>You WIN.</html>";
		else text = "<html>Game finished.<br>You LOSE.</html>";
		boardFrame.inGameInfo.setText(text);
	}// end executeFinishCommand
	
	/** Metoda tworzy tablice zaznaczonych kamieni przez przeciwnika. */
	private ArrayList<int[]> getAlphaBoard(String command){
		System.out.println(command);
		String temp;
		int tempInteger;
		boolean currentPosIsX = true;
		ArrayList<int[]> selectedStones = new ArrayList<int[]>();
		int tempPositions[] = new int[2];
		while(!command.isEmpty() && command.length() > 1 ){
			temp = command.substring(0,2);
			if(temp.charAt(1) == ' '){
				temp = temp.substring(0, 1);
				command = command.substring(2);
			}else 
				command = command.substring(3);
			tempInteger = Integer.parseInt(temp);
			if(currentPosIsX){ 	tempPositions[0] = tempInteger; currentPosIsX = false; }
			else{			 	tempPositions[1] = tempInteger; currentPosIsX = true;
								selectedStones.add(tempPositions); 
								tempPositions = new int[2];}
		}				

		return selectedStones;
	}// end getAlphaBoard
}
