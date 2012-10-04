import java.awt.Color;

import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class chatWindow {
	public String topicText = "";
	public VectorStringHistory historyBuffer = new VectorStringHistory();
	public VectorStringHistory userList = new VectorStringHistory();
	public String typedText = "";
	
	public String getTypedText(){
		return typedText;
	}
	
	public String getTopicText(){
		return topicText;
	}
	
	public void setTopicText(String s){
		topicText = s;
	}
	
	public void setTypedText(String s){
		typedText = s;
		historyBuffer.addElement(s);
	}

	public static void main(String[] args){
		chatWindow chatWin = new chatWindow();
		int x = 10, y = 10, width = 20, height = 20;
		Color col = Color.red;
		boolean filled = true;
		Circle c = new Circle(x, y, width, height, col, filled);
		
		OEFrame oeFrame = ObjectEditor.edit(chatWin);
		oeFrame.setTelePointerModel(c);
		
	}
}
