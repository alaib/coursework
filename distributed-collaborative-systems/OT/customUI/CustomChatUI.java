package customUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
import otHelper.EditWithOTTimeStamp;
import otHelper.EditWithOTTimeStampInterface;
import otHelper.OTTimeStamp;
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
	Random randomGenerator = new Random();
	Timer timer;
    DateFormat dateFormat;

	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	String prevTopic = "";
	public MyGlassPane myGlassPane;
	Circle c;
	CustomChatUI self;

	public CustomChatUI(ChatClient cWindow) {
		ch = cWindow;
		self = this;
		genCustomUI();
		addListeners();
		
		// Set Random Number Seed
        Date date = new Date();
        long time = date.getTime();
		randomGenerator.setSeed(time);
		
		//Timer
		timer = new Timer();
		dateFormat = new SimpleDateFormat("HH:mm:ss:S");
	}

	public void addCircle(Circle circle) {
		c = circle;
	}

	@SuppressWarnings("unchecked")
	public void genCustomUI() {
		typedTextUI = new JTextField(32);
		topicTextUI = new JTextField(20);

		archivePaneUI = new JTextArea(10, 16);
		archivePaneUI.setEditable(false);

		userListPaneUI = new JTextArea(10, 12);
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
		archiveScrollPaneUI.setPreferredSize(new Dimension(300, 200));
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
					String msg = "["+dateFormat.format(new Date())+"] "+ ch.clientName + " : " + typedTextUI.getText();
					final long epoch = System.currentTimeMillis();
					int msgPos = self.ch.insertToMsgList(epoch, msg);
					self.ch.addElemToHistBuffer(msgPos, msg);
					// Send update to CUI
					self.appendTextToArchivePane(msgPos, msg+"\n");
					//cui.archivePaneUI.append(elem + "\n");
					self.typedTextUI.setText("");
					
					final String newData[] = new String[3];
					newData[0] = ch.clientStatus.toString();
					newData[1] = msg;
                    newData[2] = Long.toString(epoch);
                    
					if(ch.retrieveDelayFlag() == true){
						int delay = getRandomNumber(ch.retrieveMinDelay(), ch.retrieveMaxDelay());
						MVCTracerInfo.newInfo("Delay = "+delay+", Message = "+msg, this);
						timer.schedule(new TimerTask(){
							@Override
							public void run() {
								ch.modifyData(newData);
								ch.sendChatEvtToServer(newData, Constants.CLIENT_NEW_MSG);
							}
							
						}, delay);
					}else{
						ch.modifyData(newData);
						ch.sendChatEvtToServer(newData, Constants.CLIENT_NEW_MSG);
						MVCTracerInfo.newInfo("New Message = "+msg, this);
					}
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
					int index = ch.getUserListIndexOfKey(oldKey);
					if (index >= 0) {
						ch.setUserListAtIndex(index, newKey);
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
				final String currStr = topicTextUI.getText();
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
					final String[] data = new String[3];
					
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
							ch.removeElemAtPosForTopic(pos);
							updateIssued = 0;
							data[0] = Integer.toString(pos);
							data[1] = String.valueOf(c);
							data[2] = "";
							
							try {
								//Dummy Edit (pos, char, operation, isServer, OTTimeStamp)
								EditWithOTTimeStampInterface edit = (EditWithOTTimeStampInterface)
															new EditWithOTTimeStamp(-1, 'x', "D", 0, 0, new OTTimeStamp());
								//Send Chat Event to Server
								int otherUpdate = 0;
								int updateCUITopic = 0;
								self.updateTopic(data, edit, Constants.CLIENT_TOPIC_CHANGE_DELETE, otherUpdate, updateCUITopic);
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
							ch.insertElemAtPosForTopic(c, pos);
							updateIssued = 0;
							data[0] = Integer.toString(pos);
							data[1] = String.valueOf(c);
							data[2] = currStr;
							
							try {
								int otherUpdate = 0;
								int updateCUITopic = 0;
								EditWithOTTimeStampInterface edit;
								//Send OT Event to Server (Insert's ONLY)
								if(otherUpdate != 1){
									self.ch.incrementOTCounter("L");
									System.out.println("Adding to local buffer, clientName = "+self.ch.getClientName());
									edit = (EditWithOTTimeStampInterface)
									new EditWithOTTimeStamp(pos, c, "I", 0, self.ch.retrieveId(), self.ch.retrieveMyOTTimeStamp());
									
									self.ch.addToEditLog(self.ch.convertToKey(edit), "0");
									self.ch.addToLocalBuffer(edit);
								}else{
									//Dummy Edit
									edit = (EditWithOTTimeStampInterface)
											new EditWithOTTimeStamp(-1, 'x', "I", 0, self.ch.retrieveId(), new OTTimeStamp());
								}
								self.updateTopic(data, edit, Constants.CLIENT_OT_TOPIC_CHANGE_INSERT, otherUpdate, updateCUITopic);
								//Send Chat Event to Server
								//self.cui.updateTopic(data, edit, Constants.CLIENT_TOPIC_CHANGE_INSERT, self.otherUpdate);
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					prevTopic = currStr;
				}
			}
		});
	}
	
	public int getRandomNumber(int START, int END){
		long range = END - START + 1;
		long frac = (long)(randomGenerator.nextDouble()*range);
		int r = (int)(frac + START);
		return r;
	}

	public void updateTopic(final String[] data, final EditWithOTTimeStampInterface ed, int STATUS_CODE, int otherUpdate, int updateCUI){
		String currStr = topicTextUI.getText();
		final int pos = Integer.parseInt(data[0], 10);
		final Character c = data[1].charAt(0);
		
		if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_DELETE){
			String tempStr = topicTextUI.getText();
			if(updateCUI == 1){
				tempStr = new StringBuffer(currStr).deleteCharAt(pos).toString();
				topicTextUI.setText(tempStr);
			}
			final String newStr = tempStr;
			if(otherUpdate == 0){
				data[2] = newStr;
				if(ch.retrieveDelayFlag() == true){
					int delay = getRandomNumber(ch.retrieveMinDelay(), ch.retrieveMaxDelay());
					MVCTracerInfo.newInfo("Delay = "+delay+", (not updated) Topic - character deleted at pos = "+pos, this);
					timer.schedule(new TimerTask(){
						@Override
						public void run() {
							ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_DELETE);
							MVCTracerInfo.newInfo("Topic - character deleted at pos = "+pos, this);
						}
						
					}, delay);
				}else{
					ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_DELETE);
					MVCTracerInfo.newInfo("Topic - character deleted at pos = "+pos, this);
				}
			}
			prevTopic = newStr;
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_INSERT){
			String tempStr = topicTextUI.getText();
			if(updateCUI == 1){
				tempStr = new StringBuffer(currStr).insert(pos, c).toString();
				topicTextUI.setText(tempStr);
			}
			final String newStr = tempStr;
			if(otherUpdate == 0){
				data[2] = newStr;
				if(ch.retrieveDelayFlag() == true){
					int delay = getRandomNumber(ch.retrieveMinDelay(), ch.retrieveMaxDelay());
					timer.schedule(new TimerTask(){
						@Override
						public void run() {
							ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_INSERT);
							MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
						}
						
					}, delay);
				}else{
					ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_INSERT);
					MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
				}
			}
			prevTopic = newStr;
		}else if(STATUS_CODE == Constants.CLIENT_OT_TOPIC_CHANGE_INSERT){
			String tempStr = topicTextUI.getText();
			if(updateCUI == 1){
				tempStr = new StringBuffer(currStr).insert(pos, c).toString();
				topicTextUI.setText(tempStr);
			}
			final String newStr = tempStr;
			if(otherUpdate == 0){
				data[2] = newStr;
				if(ch.retrieveDelayFlag() == true){
					int delay = getRandomNumber(ch.retrieveMinDelay(), ch.retrieveMaxDelay());
					timer.schedule(new TimerTask(){
						@Override
						public void run() {
							ch.sendOTEvtToServer(ed, newStr, Constants.CLIENT_OT_TOPIC_CHANGE_INSERT);
							//ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_INSERT);
							MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
						}
						
					}, delay);
				}else{
					ch.sendOTEvtToServer(ed, newStr, Constants.CLIENT_OT_TOPIC_CHANGE_INSERT);
					//ch.sendChatEvtToServer(data, Constants.CLIENT_TOPIC_CHANGE_INSERT);
					MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
				}
			}
			prevTopic = newStr;
		}
	}
	
	public void appendTextToArchivePane(int pos, String elem){
		String currText = this.archivePaneUI.getText();
		if(currText.length() == 0){
			this.archivePaneUI.append(elem);
			return;
		}
		String []currTextArr = currText.split("\n");
		String newText = "";
		if(currTextArr.length == pos){
			newText = currText + elem;
		}else{
			for(int i = 0; i < currTextArr.length; i++){
				if(i == pos){
					newText += elem;
				}
				newText += currTextArr[i] + "\n";
			}
		}
		this.archivePaneUI.setText(newText);
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