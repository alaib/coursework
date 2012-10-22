package customUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import misc.Constants;

/**
 * We have to provide our own glass pane so that it can paint.
 */
public class MyGlassPane extends JComponent implements ItemListener {
	public Point point;

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
			this.cui.ch.sendTelePointerEvtToServer(glassPanePoint);
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