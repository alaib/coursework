import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.AListenableVector;
import util.models.PropertyListenerRegisterer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ChatWindow implements PropertyListenerRegisterer {
	public String message = "";
	public String topic = "";
	public VectorStringHistory historyBuffer = new VectorStringHistory();
	public AListenableVector userList = new AListenableVector();
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);
	PropertyChangeListener pListener;

	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	public String clientName;
	public uStatus clientStatus = uStatus.Available;
	CustomChatUI cui;

	ChatWindow(String cName) {
		addListeners();
		addPropertyChangeListener(pListener);
		this.clientName = cName;
		this.cui = new CustomChatUI(this);
		addToUserList(cName, clientStatus);
	}

	public void addListeners() {
		pListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
				System.out.println(property + " changed to " + e.getNewValue());
			}
		};
	}

	public void addToUserList(String cName, uStatus cStatus) {
		String elem = cName + " - " + cStatus.toString();
		// Send update to CUI
		userList.add(elem);
		cui.userListPaneUI.append(elem);
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		propertyChangeSupport.firePropertyChange("topic", null, topic);
		cui.topicTextUI.setText(topic);
	}

	public void setMessage(String s) {
		message = "";
		String elem = clientName + " : " + s;
		propertyChangeSupport.firePropertyChange("message", null, message);
		
		historyBuffer.addElement(elem);
		// Send update to CUI
		cui.archivePaneUI.append(elem + "\n");
		cui.typedTextUI.setText("");
	}

	public String getMessage() {
		return message;
	}

	public void setClientStatusFromObj(Object s) {
		String val = s.toString();
		uStatus status = uStatus.Available;
		if (val.equals("Available")) {
			status = uStatus.Available;
		} else if (val.equals("Busy")) {
			status = uStatus.Busy;
		} else if (val.equals("Invisible")) {
			status = uStatus.Invisible;
		} else if (val.equals("Idle")) {
			status = uStatus.Idle;
		}
		this.setClientStatus(status);
	}

	public uStatus getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(uStatus clientStatus) {
		String oldKey = clientName + " - " + this.clientStatus.toString();
		this.clientStatus = clientStatus;
		propertyChangeSupport.firePropertyChange("clientStatus", null, clientStatus);
		
		String newKey = clientName + " - " + this.clientStatus.toString();
		int index = userList.indexOf(oldKey);
		System.out.println("OldKey = "+oldKey+", NewKey = "+newKey+", Index = "+index);
		if(index >= 0){
			userList.set(index, newKey);
		}
		
	    // Send update to CUI
		String value = this.clientStatus.toString();
		cui.statusListUI.setSelectedItem(value);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener arg0) {
		propertyChangeSupport.addPropertyChangeListener(arg0);
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public static void main(String[] args) {
		UserLogin ul = new UserLogin();
		String cName = ul.getUserName();

		int x = 10, y = 100, width = 20, height = 20;
		Color col = Color.red;
		boolean filled = true;
		Circle c = new Circle(x, y, width, height, col, filled);

		ChatWindow ch = new ChatWindow(cName);
		OEFrame oeFrame = ObjectEditor.edit(ch);
		oeFrame.setTelePointerModel(c);
	}
}
