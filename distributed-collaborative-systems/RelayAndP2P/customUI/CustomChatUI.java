package customUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import misc.Constants;
import oeHelper.Circle;
import tracer.MVCTracerInfo;
import client.ChatClient;

/**
 * Custom (Manual) UI Generator 
 * @author ravikirn
 *
 */
public class CustomChatUI {
	public ChatClient ch;
	public JTextField typedTextUI;
	public JTextField topicTextUI;
	public JTextArea archivePaneUI;
	public JTextArea userListPaneUI;
	JLabel topicLabelUI;
	public JComboBox statusListUI;
	JFrame frameUI;
	Container containerUI;
	JPanel uPanelUI, lPanelUI;
	JScrollPane userListScrollPaneUI, archiveScrollPaneUI;
	public int updateIssued = 0;

	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	String prevTopic = "";
	public MyGlassPane myGlassPane;
	Circle c;

	public CustomChatUI(ChatClient cWindow) {
		ch = cWindow;
		genCustomUI();
		addListeners();
	}

	public void addCircle(Circle circle) {
		c = circle;
	}

	public void genCustomUI() {
		typedTextUI = new JTextField(32);
		topicTextUI = new JTextField(20);

		archivePaneUI = new JTextArea(10, 20);
		archivePaneUI.setEditable(false);

		userListPaneUI = new JTextArea(10, 20);
		userListPaneUI.setEditable(false);

		topicLabelUI = new JLabel("Topic:");
		String[] statusStrings = { "Available", "Busy", "Invisible", "Idle" };
		statusListUI = new JComboBox(statusStrings);
		statusListUI.setSelectedIndex(0);

		frameUI = new JFrame();
		containerUI = frameUI.getContentPane();

		// Upper Panel
		uPanelUI = new JPanel();
		uPanelUI.add(statusListUI);
		uPanelUI.add(topicLabelUI);
		uPanelUI.add(topicTextUI);

		// Start creating and adding components.
		JCheckBox changeButton = new JCheckBox("Glass pane \"visible\"");
		changeButton.setSelected(false);

		// Lower Panel
		lPanelUI = new JPanel(new BorderLayout());
		archiveScrollPaneUI = new JScrollPane(archivePaneUI);
		archiveScrollPaneUI.setPreferredSize(new Dimension(400, 200));
		userListScrollPaneUI = new JScrollPane(userListPaneUI);
		lPanelUI.add(archiveScrollPaneUI, BorderLayout.CENTER);
		lPanelUI.add(userListScrollPaneUI, BorderLayout.EAST);
		lPanelUI.add(typedTextUI, BorderLayout.SOUTH);
		lPanelUI.add(changeButton, BorderLayout.NORTH);

		// Add both panels to container
		containerUI.add(uPanelUI, BorderLayout.NORTH);
		containerUI.add(lPanelUI, BorderLayout.CENTER);

		// GlassPane

		// Set up the menu bar, which appears above the content pane.
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		menuBar.add(menu);

		// Set up the glass pane, which appears over both menu bar
		// and content pane and is an item listener on the change
		// button.
		myGlassPane = new MyGlassPane(changeButton, this,
				frameUI.getContentPane());
		changeButton.addItemListener(myGlassPane);
		frameUI.setGlassPane(myGlassPane);

		// display the window, with focus on typing box
		frameUI.setTitle("Chat Client - " + ch.clientName);
		frameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameUI.pack();
		typedTextUI.requestFocusInWindow();
		frameUI.setVisible(true);
		changeButton.setSelected(true);
		Point currP = this.ch.retrieveCurrPoint();
		myGlassPane.setPoint(currP);
		myGlassPane.repaint();
		lPanelUI.remove(changeButton);
	}

	public void addListeners() {
		typedTextUI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (typedTextUI.equals("")) {
					return;
				}
				try {
					// data = chWindow.handleEvent(clientName, clientStatus,
					// typedTextUI.getText(), EVENT_NEW_MSG);
					String msg = ch.clientName + " : " + typedTextUI.getText();
					archivePaneUI.append(msg + "\n");
					ch.historyBuffer.addElement(msg);
					String newData[] = new String[2];
					newData[0] = ch.clientStatus.toString();
					newData[1] = msg;
					ch.modifyData(newData);
					ch.sendChatEvtToServer(newData, Constants.CLIENT_NEW_MSG);
				} catch (Exception e1) {
					System.out.println("chatClient exception: " + e1);
				}
				typedTextUI.setText("");
				typedTextUI.requestFocusInWindow();
			}
		});

		statusListUI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JComboBox cb = (JComboBox) e.getSource();
					String oldKey = ch.clientName + " - "
							+ ch.clientStatus.toString();
					String newKey = ch.clientName + " - "
							+ cb.getSelectedItem().toString();
					int index = ch.userList.indexOf(oldKey);
					if (index >= 0) {
						ch.userList.set(index, newKey);
					}
					ch.setClientStatusFromObj(cb.getSelectedItem());
					modUserList(ch.clientName, cb.getSelectedItem().toString());
				} catch (Exception e1) {
					System.out.println("chatClient exception: " + e1);
				}
			}
		});

		topicTextUI.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				String currStr = topicTextUI.getText();
				Character k = e.getKeyChar();
				/*
				if(Character.isAlphabetic(k) || Character.isLetterOrDigit(k) || Character.isJavaLetterOrDigit(k)){
					currStr = topicTextUI.getText()+String.valueOf(k);
				}else{
					currStr = topicTextUI.getText();
				}
				*/
				
				if(currStr.equals(prevTopic)){
					//Do nothing
				}else{
					//Else figure out what has changed
					int prevLength = prevTopic.length();
					int currLength = currStr.length();
					String[] data = new String[3];
					
					if(prevLength > currLength){
						//Deletion
						int pos = -1;
						for(int i = 0; i < currLength; i++){
							if(prevTopic.charAt(i) != currStr.charAt(i)){
								pos = i;
								break;
							}
						}
						if(pos == -1){
							pos = prevLength-1;
						}
						if(pos > -1 && pos < prevLength){
							Character c = prevTopic.charAt(pos);
							updateIssued = 1;
							ch.topic.removeElementAt(pos);
							updateIssued = 0;
							data[0] = Integer.toString(pos);
							data[1] = String.valueOf(c);
							ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_DELETE);
							MVCTracerInfo.newInfo("Topic - character deleted at pos = "+pos, this);
							ch.updateTopic(currStr);
						}
					}else if(prevLength < currLength){
						//Insertion
						int pos = -1;
						for(int i = 0; i < prevLength; i++){
							if(prevTopic.charAt(i) != currStr.charAt(i)){
								pos = i;
								break;
							}
						}
						if(pos == -1){
							pos = currLength - 1;
						}
						if(pos > -1 && pos < currLength){
							Character c = currStr.charAt(pos);
							updateIssued = 1;
							ch.topic.insertElementAt(c, pos);
							updateIssued = 0;
							data[0] = Integer.toString(pos);
							data[1] = String.valueOf(c);
							data[2] = currStr;
							ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_INSERT);
							MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
							if(ch.retrieveMode().equals("p2p")){
								ch.updateTopic(currStr);
							}
						}
					}
					prevTopic = currStr;
				}
			}
			
		});
	}

	public void updateTopic(String[] data, int STATUS_CODE, int otherUpdate){
		String currStr = topicTextUI.getText();
		int pos = Integer.parseInt(data[0], 10);
		Character c = data[1].charAt(0);
		
		if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_DELETE){
			String newStr = new StringBuffer(currStr).deleteCharAt(pos).toString();
			topicTextUI.setText(newStr);
			if(otherUpdate == 0){
				data[2] = newStr;
				ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_DELETE);
				MVCTracerInfo.newInfo("Topic - character deleted at pos = "+pos, this);
				ch.updateTopic(newStr);
			}
			prevTopic = newStr;
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_INSERT){
			String newStr = new StringBuffer(currStr).insert(pos, c).toString();
			topicTextUI.setText(newStr);
			if(otherUpdate == 0){
				data[2] = newStr;
				ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_INSERT);
				MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
				ch.updateTopic(newStr);
			}
			prevTopic = newStr;
		}
	}
	
	public void modUserList(String cName, String status) {
		String list = userListPaneUI.getText();
		String[] lines = list.split(System.getProperty("line.separator"));
		String newList = "";
		for (String s : lines) {
			if (s.contains(cName + " - ")) {
				s = cName + " - " + status;
			}
			newList += s + "\n";
		}
		userListPaneUI.setText(newList);
	}
}