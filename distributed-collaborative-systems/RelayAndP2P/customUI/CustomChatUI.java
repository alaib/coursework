package customUI;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import client.*;
import oeHelper.*;
import misc.*;

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

	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	String prevTopic = "";
	public static MyGlassPane myGlassPane;
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
		myGlassPane.setPoint(new Point(10, 100));
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
					ch.sendChatEvtToServer();
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

		topicTextUI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (!topicTextUI.getText().equals(prevTopic)
							|| !topicTextUI.getText().equals("")) {
						// chWindow.handleEvent(clientName, clientStatus,
						// topicTextUI.getText(), EVENT_CHANGE_TOPIC);
						prevTopic = topicTextUI.getText();
						ch.setTopic(prevTopic);

					}
				} catch (Exception e1) {
					System.out.println("chatClient exception: " + e1);
				}
				typedTextUI.requestFocusInWindow();
			}
		});
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