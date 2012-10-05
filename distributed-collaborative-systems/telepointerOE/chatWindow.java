import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)

public class chatWindow {
	public enum uStatus {Available, Busy, Invisible, Idle};
	
	public String topicText = "";
	public String typedText = "";
	public String clientName;
	public uStatus clientStatus = uStatus.Available;
	CustomChatUI cui;
	
	public VectorStringHistory historyBuffer = new VectorStringHistory();
	public VectorStringHistory userList = new VectorStringHistory();
	
	public chatWindow(String cName){
		this.clientName = cName;
		this.cui = new CustomChatUI(this);
		addToUserList(cName, clientStatus);
	}

	public void addToUserList(String cName, uStatus cStatus){
		userList.addElement(cName+"-"+cStatus.toString());
	}
	public String getTypedText() {
		return typedText;
	}

	public String getTopicText() {
		return topicText;
	}

	public void setTopicText(String s) {
		topicText = s;
	}

	public void setTypedText(String s) {
		String elem = this.clientName+" : "+s;
		historyBuffer.addElement(elem);
		typedText = "";;
	}
	

	public static void main(String[] args) {
		UserLogin ul = new UserLogin();
		String cName = ul.getUserName();
		
		chatWindow chatWin = new chatWindow(cName);
		
		int x = 10, y = 10, width = 20, height = 20;
		Color col = Color.red;
		boolean filled = true;
		Circle c = new Circle(x, y, width, height, col, filled);

		OEFrame oeFrame = ObjectEditor.edit(chatWin);
		oeFrame.setTelePointerModel(c);
	}
}