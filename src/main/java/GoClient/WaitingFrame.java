package GoClient;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

/** Class show window for player currently waiting in queue. */
public class WaitingFrame extends JFrame{

	private final static int windowLength = 400;
	private final static int windowHeight = 200;
	private JLabel infoLabel;
/*-------------------------------------------------------------------------------------------------------------------*/	
	
	WaitingFrame(){
		super();
		init();
		setSizes();
		setLabelInfo();
		this.add(infoLabel);		
		this.repaint();
	}
	
	private void init(){
		setTitle("Waiting for opponent.");
		setSize(windowLength, windowHeight);
		this.setLocation(400, 300);
		infoLabel = new JLabel("");
	}
	
	private void setSizes(){
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		Dimension labelDim = new Dimension(3*windowLength/4, 2*windowHeight/3);
		infoLabel.setPreferredSize(labelDim);
		layout.putConstraint(SpringLayout.WEST,	 infoLabel,	0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, infoLabel,	0,	SpringLayout.NORTH, this);
	}
	
	protected void setLabelInfo(){
		infoLabel.setText("<html>You have been added to queue in matchmaking.<br>"
				+ "Status: Waiting for opponent.<br>"
				+ "Please wait.</html>");
	}
		

}
