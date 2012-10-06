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

public class CustomChatUI {
	public ChatClientModel ch;
	JTextField typedTextUI, topicTextUI;
	JTextArea archivePaneUI, userListPaneUI;
	JLabel topicLabelUI;
	JComboBox statusListUI;
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

	CustomChatUI(ChatClientModel cWindow) {
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
					System.out.println("OldKey = " + oldKey + ", NewKey = "
							+ newKey + ", Index = " + index);
					if (index >= 0) {
						ch.userList.set(index, newKey);
					}
					ch.setClientStatusFromObj(cb.getSelectedItem());
					modUserList(ch.clientName, cb.getSelectedItem().toString());
					// data = chWindow.handleEvent(clientName, clientStatus,
					// statusMsg, EVENT_CLIENT_STATUS_CHANGE);
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
			newList += s;
		}
		userListPaneUI.setText(newList);
	}
}

/**
 * We have to provide our own glass pane so that it can paint.
 */
class MyGlassPane extends JComponent implements ItemListener {
	Point point;

	// React to change button clicks.
	public void itemStateChanged(ItemEvent e) {
		setVisible(e.getStateChange() == ItemEvent.SELECTED);
	}

	protected void paintComponent(Graphics g) {
		// System.out.println("paint called");
		if (point != null) {
			g.setColor(Color.red);
			g.fillOval(point.x - 10, point.y - 10, 20, 20);
		}
	}

	public void setPoint(Point p) {
		point = p;
	}

	public MyGlassPane(AbstractButton aButton, CustomChatUI c,
			Container contentPane) {
		CBListener listener = new CBListener(aButton, c, this, contentPane);
		addMouseListener(listener);
		addMouseMotionListener(listener);
	}
}

/**
 * Listen for all events that our check box is likely to be interested in.
 * Redispatch them to the check box.
 */
class CBListener extends MouseInputAdapter {
	Toolkit toolkit;
	Component liveButton;
	JMenuBar menuBar;
	MyGlassPane glassPane;
	Container contentPane;
	CustomChatUI cui;

	public CBListener(Component liveButton, CustomChatUI c,
			MyGlassPane glassPane, Container contentPane) {
		toolkit = Toolkit.getDefaultToolkit();
		this.liveButton = liveButton;
		this.cui = c;
		this.glassPane = glassPane;
		this.contentPane = contentPane;
	}

	public void mouseMoved(MouseEvent e) {
		redispatchMouseEvent(e, false);
	}

	public void mouseDragged(MouseEvent e) {
		redispatchMouseEvent(e, true);
	}

	public void mouseClicked(MouseEvent e) {
		redispatchMouseEvent(e, false);
	}

	public void mouseEntered(MouseEvent e) {
		redispatchMouseEvent(e, false);
	}

	public void mouseExited(MouseEvent e) {
		redispatchMouseEvent(e, false);
	}

	public void mousePressed(MouseEvent e) {
		redispatchMouseEvent(e, false);
	}

	public void mouseReleased(MouseEvent e) {
		redispatchMouseEvent(e, false);
	}

	// A basic implementation of redispatching events.
	private void redispatchMouseEvent(MouseEvent e, boolean repaint) {
		Point glassPanePoint = e.getPoint();
		Container container = contentPane;
		Point containerPoint = SwingUtilities.convertPoint(glassPane,
				glassPanePoint, contentPane);
		if (containerPoint.y < 0) { // we're not in the content pane
			repaint = false;
		} else {
			// The mouse event is probably over the content pane.
			// Find out exactly which component it's over.
			Component component = SwingUtilities.getDeepestComponentAt(
					container, containerPoint.x, containerPoint.y);

			if ((component != null)
					&& (component.equals(this.cui.topicTextUI)
							|| component.equals(this.cui.statusListUI) || component
								.equals(this.cui.typedTextUI))) {
				// Forward events over the check box.
				Point componentPoint = SwingUtilities.convertPoint(glassPane,
						glassPanePoint, component);
				component
						.dispatchEvent(new MouseEvent(component, e.getID(), e
								.getWhen(), e.getModifiers(), componentPoint.x,
								componentPoint.y, e.getClickCount(), e
										.isPopupTrigger()));
				repaint = false;
			}
		}

		// Update the glass pane if requested.
		if (repaint) {
			glassPane.setPoint(glassPanePoint);
			glassPane.repaint();
			this.cui.c.setX(glassPanePoint.x);
			this.cui.c.setY(glassPanePoint.y);
			try {
				this.cui.ch.serverInt.handleTelePointerEvent(this.cui.ch.clientName, glassPanePoint, this.cui.ch.MOVE_POINTER);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public Component findComponentUnderGlassPaneAt(Point p, Component top) {
		Component c = null;
		if (top.isShowing()) {
			if (top instanceof RootPaneContainer) {
				c = ((RootPaneContainer) top).getLayeredPane().findComponentAt(
						SwingUtilities.convertPoint(top, p,
								((RootPaneContainer) top).getLayeredPane()));
			} else {
				c = ((Container) top).findComponentAt(p);
			}
		}
		return c;
	}
}