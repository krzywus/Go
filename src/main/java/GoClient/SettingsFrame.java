package GoClient;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;


public class SettingsFrame extends JFrame{
	
	/** Wymiary okna. */
	public final static int windowLength = 400; 
	public final static int windowHeight = 400;

	/** Elementy okna. */
	private JLabel mainTitle;
	private JLabel sizeLabel, smallSizeLabel, mediumSizeLabel, bigSizeLabel;
	private JLabel opponentLabel, OtherOpponentLabel, AIOpponentLabel;
	private JCheckBox smallSizeBox, mediumSizeBox, bigSizeBox;
	private JCheckBox AIBox, otherClientBox;
	private JButton save, exit;

	/** Klient. */
	private GoClient client;
/*-------------------------------------------------------------------------------------------------------------------*/

	
	/** Konstruktor. Tworzy okno. */
	SettingsFrame(GoClient client){
		super();
		this.client = client;
		addWindowListener(new MyWindowAdapter(client));
		init();
		setSizes();
		addElements();
		repaint();
	} // end Board constructor
	
	/** Metoda do inicjalizacji elementow okna. */
	private void init(){
		mainTitle = new JLabel("Settings");
		opponentLabel = new JLabel("Opponent");
		AIOpponentLabel = new JLabel("AI");	OtherOpponentLabel = new JLabel("Player");
		sizeLabel = new JLabel("Board Size");
		smallSizeLabel = new JLabel("9x9");	mediumSizeLabel = new JLabel("13x13"); bigSizeLabel = new JLabel("19x19");
		smallSizeBox = new JCheckBox();  mediumSizeBox = new JCheckBox();  bigSizeBox = new JCheckBox(); 
		AIBox = new JCheckBox();  otherClientBox = new JCheckBox(); 
		save = new JButton("Save Settings"); exit = new JButton("Exit"); 
	}
	
	/** Ustawienie rozmiarow okna, planszy do gry oraz statystyk danej rozgrywki. */
	private void setSizes(){
		setSize(windowLength,windowHeight);
		setLocation(400,125);
		SpringLayout layout= new SpringLayout();
		setLayout(layout);
		/* Ustawienie zaleznosci w obrebie okna. */
		Dimension mainTitleDim= new Dimension(100, 50);	mainTitle.setPreferredSize(mainTitleDim);
		Dimension sizeLabelDim= new Dimension(80, 35);	sizeLabel.setPreferredSize(sizeLabelDim);
		Dimension smallSizeLabelDim= new Dimension(30, 30);	smallSizeLabel.setPreferredSize(smallSizeLabelDim);
		Dimension mediumSizeLabelDim= new Dimension(50, 30);	mediumSizeLabel.setPreferredSize(mediumSizeLabelDim);
		Dimension bigSizeLabelDim= new Dimension(50, 30);	bigSizeLabel.setPreferredSize(bigSizeLabelDim);
		Dimension opponentLabelDim= new Dimension(80, 35);	opponentLabel.setPreferredSize(opponentLabelDim);
		Dimension AIOpponentLabelDim= new Dimension(50, 30);	AIOpponentLabel.setPreferredSize(AIOpponentLabelDim);
		Dimension OtherOpponentLabelDim= new Dimension(50, 30);	OtherOpponentLabel.setPreferredSize(OtherOpponentLabelDim);
		Dimension saveDim= new Dimension(150, 50);	save.setPreferredSize(saveDim);
		Dimension exitDim= new Dimension(100, 50);	exit.setPreferredSize(exitDim);
		
		layout.putConstraint(SpringLayout.WEST,	mainTitle,	windowLength/2-40/*-mainTitle.getWidth()*/,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mainTitle,	windowHeight/40,	SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	sizeLabel,	windowLength/2-60,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, sizeLabel,	2*windowHeight/10,	SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	smallSizeLabel,	windowLength/3-80,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, smallSizeLabel,	3*windowHeight/10,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	mediumSizeLabel,	2*windowLength/3-100,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mediumSizeLabel,	3*windowHeight/10,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	bigSizeLabel,	3*windowLength/3-100,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bigSizeLabel,	3*windowHeight/10,	SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST,	smallSizeBox,	windowLength/3-80,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, smallSizeBox,	3*windowHeight/10+30,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	mediumSizeBox,	2*windowLength/3-90,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, mediumSizeBox,	3*windowHeight/10+30,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	bigSizeBox,	3*windowLength/3-90,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, bigSizeBox,	3*windowHeight/10+30,	SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST,	opponentLabel,	windowLength/2-60,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, opponentLabel,	5*windowHeight/10,	SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	AIOpponentLabel,	windowLength/3-80,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, AIOpponentLabel,	6*windowHeight/10,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	OtherOpponentLabel,	2*windowLength/3-100,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, OtherOpponentLabel,	6*windowHeight/10,	SpringLayout.NORTH, this);

		layout.putConstraint(SpringLayout.WEST,	AIBox,	windowLength/3-80,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, AIBox, 6*windowHeight/10+30,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	otherClientBox,	2*windowLength/3-90,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, otherClientBox,	6*windowHeight/10+30,	SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST,	save,	windowLength/3-100,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, save, 8*windowHeight/10,	SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST,	exit,	2*windowLength/3,	SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, exit,	8*windowHeight/10,	SpringLayout.NORTH, this);
	} // end setSizes
	
	/** Metoda dodajaca elementy do okna. */
	private void addElements(){
		add(mainTitle);
		add(sizeLabel); 
		add(smallSizeLabel);	add(mediumSizeLabel); 	add(bigSizeLabel); 
		add(smallSizeBox); 		add(mediumSizeBox); 	add(bigSizeBox); 
		add(opponentLabel); 
		add(AIOpponentLabel); 	add(OtherOpponentLabel); 
		add(AIBox); 			add(otherClientBox);  
		add(save);
		add(exit);
	} // end addElements
}
