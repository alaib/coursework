package client;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.AListenableVector;
import util.models.PropertyListenerRegisterer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import oeHelper.*;
import customUI.*;
import relayServer.*;
import misc.*;

/**
 * 
 * Model for the Chat Client (has data structures for both telepointer and IM tool) 
 * @author ravikirn
 *
 */

@StructurePattern(StructurePatternNames.BEAN_PATTERN)
public class ChatClient implements PropertyListenerRegisterer {
	public String message = "";
	public String topic = "";
	public VectorStringHistory historyBuffer = new VectorStringHistory();
	public AListenableVector<String> userList = new AListenableVector<String>();
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	PropertyChangeListener pListener;
	Circle c;
	OERelayServerInterface serverInt;
	OEClientCallbackImpl callback;
	
	String data[] = new String[2];
	
	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	public String clientName;
	public uStatus clientStatus = uStatus.Available;
	protected CustomChatUI cui; 
	
	ChatClient(String cName) {
		addListeners();
		addPropertyChangeListener(pListener);
		this.clientName = cName;
		this.cui = new CustomChatUI(this); addToUserList(cName, clientStatus);
		try {
			serverInt = (OERelayServerInterface) Naming.lookup ("rmi://localhost/Telepointer");
			callback = new OEClientCallbackImpl(this);
			serverInt.registerCallback(this.clientName, this.clientStatus.toString(), callback);
			data[0] = this.clientStatus.toString();
			serverInt.handleChatEvent(clientName, data, Constants.CLIENT_JOIN);
		} catch (MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	  			
	}

	public void addCircle(Circle circle){
		c = circle;
		this.cui.addCircle(c);
		try {
			Point p = this.serverInt.getCurrPoint();
			this.c.setX(p.x);
			this.c.setY(p.y);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendChatEvtToServer(){
		try {
			this.serverInt.handleChatEvent(this.clientName, this.data, Constants.CLIENT_NEW_MSG);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendTelePointerEvtToServer(Point p){
		try {
			this.serverInt.handleTelePointerEvent(this.clientName, p, Constants.MOVE_POINTER);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modifyData(String[] newData){
		this.data = newData;
	}
	
	public String[] getCurrentData(){
		return data;
	}
	
	public void drawPoint(Point p){
		this.cui.myGlassPane.setPoint(p);
		this.cui.myGlassPane.repaint();
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
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 
		String calleeMethod = elements[1].getMethodName(); 
		if(calleeMethod.equals("ChatClientModel")){
			cui.userListPaneUI.append(elem);
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		propertyChangeSupport.firePropertyChange("topic", null, topic);
		cui.topicTextUI.setText(topic);
		
		//Send Update to Everyone
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 
		String calleeMethod = elements[1].getMethodName(); 
		if(!calleeMethod.equals("handleChatEventNotify")){
			data[0] = this.clientStatus.toString();
			data[1] = this.topic;
			try {
				this.serverInt.handleChatEvent(this.clientName, data, Constants.CLIENT_TOPIC_CHANGE);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void setMessage(String s) {
		message = "";
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 
		String calleeMethod = elements[1].getMethodName(); 
		String elem = "";
		if(!calleeMethod.equals("handleChatEventNotify")){
			elem = clientName + " : " + s;
		}else{
			elem = s;
		}
		propertyChangeSupport.firePropertyChange("message", null, message);
		
		historyBuffer.addElement(elem);
		// Send update to CUI
		cui.archivePaneUI.append(elem + "\n");
		cui.typedTextUI.setText("");
		
		//Send update to everyone
		if(!calleeMethod.equals("handleChatEventNotify")){
			data[0] = this.clientStatus.toString();
			data[1] = elem;
			try {
				this.serverInt.handleChatEvent(this.clientName, data, Constants.CLIENT_NEW_MSG);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
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
	
	public uStatus convertToUserStatus(Object s){
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
		return status;
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
		if(index >= 0){
			userList.set(index, newKey);
		}
		
	    // Send update to CUI
		String value = this.clientStatus.toString();
		cui.statusListUI.setSelectedItem(value);
		
		// Send to everyone
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 
		String calleeMethod = elements[1].getMethodName(); 
		if(!calleeMethod.equals("handleChatEventNotify")){
			data[0] = this.clientStatus.toString();
			try {
				this.serverInt.handleChatEvent(this.clientName, data, Constants.CLIENT_STATUS_CHANGE);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
	
	//Don't change method name, if yes change setX and setY method in Circle
	public void handleTelePointerNotify(Point p, int STATUS_CODE){
		if(STATUS_CODE == Constants.MOVE_POINTER){
			if(this.c != null){
				this.c.setX(p.x);
				this.c.setY(p.y);
			}
		}
	}
	
	public void handleChatEventNotify(String[] result, int STATUS_CODE){
		if(STATUS_CODE == Constants.CLIENT_JOIN || STATUS_CODE == Constants.CLIENT_STATUS_CHANGE || STATUS_CODE == Constants.CLIENT_EXIT){
			this.userList.clear();
			String users = result[0];
			String []userList = users.split("\\n");
			for(String s : userList){
				String []splitData = s.split("-");
				this.addToUserList(splitData[0].trim(), this.convertToUserStatus(splitData[1].trim()));
			}
			this.cui.userListPaneUI.setText(users);
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE){
			String newTopic = result[1];
			this.setTopic(newTopic);
		}else if(STATUS_CODE == Constants.CLIENT_NEW_MSG){
			String newMsg = result[1];
			this.setMessage(newMsg);
		}
	}

	public static void main(String[] args) {
		UserLogin ul = new UserLogin();
		String cName = ul.getUserName();

		Color col = Color.red;
		boolean filled = true;
		int x = 10, y = 100, width = 20, height = 20;
		Circle c = new Circle(x, y, width, height, col, filled);

		final ChatClient ch = new ChatClient(cName);
		c.addCH(ch);
		ch.addCircle(c);
		OEFrame oeFrame = ObjectEditor.edit(ch);
		oeFrame.setTelePointerModel(c);
		
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		ch.data[0] = ch.clientStatus.toString();
            		Constants c = new Constants();
            		ch.serverInt.handleChatEvent(ch.clientName, ch.data, c.CLIENT_EXIT);
            	}catch(Exception e){
            		System.out.println("chatClientModel exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
    }
}
