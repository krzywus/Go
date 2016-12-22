package GoClient;

import java.awt.event.ActionEvent;

import junit.framework.TestCase;


public class SettingsFrameTest extends TestCase {

	private SettingsFrame settings = null;
	
	public void setUp(){
		settings = new SettingsFrame(null);
	}
	
	
	public void testDefaultSettings(){
		assertFalse(settings.AIBox.isSelected());
		assertTrue(settings.AIBox.isEnabled());
		assertTrue(settings.otherClientBox.isSelected());
		assertFalse(settings.otherClientBox.isEnabled());
		assertTrue(settings.smallSizeBox.isSelected());
		assertFalse(settings.smallSizeBox.isEnabled());
		assertFalse(settings.mediumSizeBox.isSelected());
		assertTrue(settings.mediumSizeBox.isEnabled());
		assertFalse(settings.bigSizeBox.isSelected());
		assertTrue(settings.bigSizeBox.isEnabled());
	}
	
	public void testActionOnPlayersBoxes(){
		settings.actionPerformed(new ActionEvent(settings.AIBox, 0, ""));	// AIbox clicked
		assertTrue(settings.AIBox.isSelected());
		assertFalse(settings.AIBox.isEnabled());
		assertFalse(settings.otherClientBox.isSelected());
		assertTrue(settings.otherClientBox.isEnabled());
	}
	
	public void testActionOnBoardSizeBoxes(){
		settings.actionPerformed(new ActionEvent(settings.mediumSizeBox, 0, "")); //medium size chosen
		assertFalse(settings.smallSizeBox.isSelected());
		assertTrue(settings.smallSizeBox.isEnabled());
		assertTrue(settings.mediumSizeBox.isSelected());
		assertFalse(settings.mediumSizeBox.isEnabled());
		assertFalse(settings.bigSizeBox.isSelected());
		assertTrue(settings.bigSizeBox.isEnabled());
		settings.actionPerformed(new ActionEvent(settings.smallSizeBox, 0, "")); //small size chosen
		assertTrue(settings.smallSizeBox.isSelected());
		assertFalse(settings.smallSizeBox.isEnabled());
		assertFalse(settings.mediumSizeBox.isSelected());
		assertTrue(settings.mediumSizeBox.isEnabled());
		assertFalse(settings.bigSizeBox.isSelected());
		assertTrue(settings.bigSizeBox.isEnabled());	
		settings.actionPerformed(new ActionEvent(settings.bigSizeBox, 0, "")); //big size chosen
		assertFalse(settings.smallSizeBox.isSelected());
		assertTrue(settings.smallSizeBox.isEnabled());
		assertFalse(settings.mediumSizeBox.isSelected());
		assertTrue(settings.mediumSizeBox.isEnabled());
		assertTrue(settings.bigSizeBox.isSelected());
		assertFalse(settings.bigSizeBox.isEnabled());
		
	}
}

