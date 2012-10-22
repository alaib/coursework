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
import java.util.HashMap;
import java.util.Map;

import misc.Constants;
import oeHelper.Circle;
import oeHelper.VectorStringHistory;
import relayServer.RelayServerInterface;
import sessionServer.SessionServerInterface;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.AListenableVector;
import util.models.PropertyListenerRegisterer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;
import bus.uigen.trace.TraceableDisplayAndWaitManagerFactory;
import customUI.CustomChatUI;
import customUI.TemporaryUI;

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
	TemporaryUI tempUI;
	RelayServerInterface rServerInt;
	SessionServerInterface sServerInt;
	ClientCallbackImpl callback;
	
	Map <String, ClientCallbackInterface> clientMap;
    Map <String, String> clientStatusMap;
    
	String data[] = new String[2];
	String result[] = new String[2];
	String mode = "";
	String connUserList[];
	
	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	public String clientName;
	public uStatus clientStatus = uStatus.Available;
	protected CustomChatUI cui; 
	
	ChatClient(String cName, String m) {
		mode = m;
		addListeners();
		addPropertyChangeListener(pListener);
		this.clientName = cName;
		this.clientMap = new HashMap<String, ClientCallbackInterface>();    		
		this.clientStatusMap = new HashMap<String, String>();    		
		this.cui = new CustomChatUI(this); addToUserList(cName, clientStatus);
		try {
			rServerInt = (RelayServerInterface) Naming.lookup ("rmi://localhost/RelayServer");
			sServerInt = (SessionServerInterface) Naming.lookup ("rmi://localhost/SessionServer");
			callback = new ClientCallbackImpl(this);
			rServerInt.registerCallback(this.clientName, this.clientStatus.toString(), callback);
			sServerInt.registerCallback(this.clientName, this.clientStatus.toString(), callback);
			data[0] = this.clientStatus.toString();
			tempUI = new TemporaryUI();
			if(mode.equals("relayer")){
				rServerInt.handleChatEvent(clientName, data, Constants.CLIENT_JOIN);
			}else{
				String uList = sServerInt.getUserList();
				String[] connUserList = uList.split("\n");
				if(!uList.equals("")){
					result[0] = uList;
					this.handleChatEventNotify(result, Constants.CLIENT_JOIN);
				}
				if(connUserList.length <= 1){
					//this.tempUI.showMsgBox("You are the only user connected, please wait until others join");
				}else{
					for(int i = 0; i < connUserList.length; i++){
						String[] splitArr = connUserList[i].split("-");
						if(splitArr.length != 2){
							System.out.println("Error! splitArr length < 2, String = "+connUserList[i]);
							continue;
						}
						String connUser = splitArr[0].trim();
						if(!connUser.equals(this.clientName)){
							System.out.println("User = "+connUser);
							ClientCallbackInterface cb = sServerInt.getCallback(connUser);
							if(!clientMap.containsKey(connUser)){
								clientMap.put(connUser, cb);
								System.out.println(connUser+" callback put on top on list");
								this.issueConnEstablishMsg(connUser);
								sServerInt.sendMyCallbackToUser(this.clientName, connUser);
								System.out.println("Sent my call back to user = "+connUser);
							}
							this.data[0] = uList;
							System.out.println("Sent updates to all clients that "+this.clientName+" joined");
							this.sendChatEventUpdateToAllClients(this.clientName, this.data, Constants.CLIENT_JOIN);
						}
					}	
				}
			}
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
			Point p = new Point(20, 50);
			if(mode.equals("relayer")){
				p = this.rServerInt.getCurrPoint();
			}
			this.c.setX(p.x);
			this.c.setY(p.y);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void issueConnEstablishMsg(String connUser){
		this.setMessage(connUser+" joined the chat session, Connection Established");
	}
	
	public void sendChatEvtToServer(){
		try {
			if(mode.equals("relayer")){
				this.rServerInt.handleChatEvent(this.clientName, this.data, Constants.CLIENT_NEW_MSG);
			}else{
				this.sendChatEventUpdateToAllClients(this.clientName, this.data, Constants.CLIENT_NEW_MSG);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendTelePointerUpdateToAllClients(String cName, Point p, int STATUS_CODE){	    	
    	for(Map.Entry<String, ClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		ClientCallbackInterface tcCallback = item.getValue();
			try {
				tcCallback.handleTelePointerNotify(p, STATUS_CODE);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	    
	}
	
	public void sendChatEventUpdateToAllClients(String cName, String[] data, int STATUS_CODE){	    	
    	for(Map.Entry<String, ClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		ClientCallbackInterface tcCallback = item.getValue();
			try {
				tcCallback.handleChatEventNotify(data, STATUS_CODE);
				if(STATUS_CODE == Constants.CLIENT_JOIN){
					tcCallback.handleTelePointerNotify(this.cui.myGlassPane.point, Constants.MOVE_POINTER);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	    
	}
	
	public void sendTelePointerEvtToServer(Point p){
		try {
			if(mode.equals("relayer")){
				this.rServerInt.handleTelePointerEvent(this.clientName, p, Constants.MOVE_POINTER);
			}else{
				this.sendTelePointerUpdateToAllClients(this.clientName, p, Constants.MOVE_POINTER);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void modifyData(String[] newData){
		this.data = newData;
	}
	
	public String[] fetchCurrentData(){
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

	public void addClientCallback(String cName, ClientCallbackInterface cb){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, cb);
			if(cName != this.clientName){
				this.setMessage(cName+ " joined chat session, Connection Established");
			}
		}
	}
	
	public void removeClientCallback(String cName){
		if(clientMap.containsKey(cName)){
			clientMap.remove(cName);
			this.setMessage(cName+ " left the chat session, Connection Ended - removeClientCallback");
		}
	}
	
	public void sendRemovalOfCallbackEvt(){
    	for(Map.Entry<String, ClientCallbackInterface> item : clientMap.entrySet()){
			String clientName = item.getKey();
			ClientCallbackInterface tcCallback = item.getValue();
			try {
				tcCallback.removeClientCallback(this.clientName);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
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
				if(mode.equals("relayer")){
					this.rServerInt.handleChatEvent(this.clientName, data, Constants.CLIENT_TOPIC_CHANGE);
				}else{
					this.sendChatEventUpdateToAllClients(this.clientName, data, Constants.CLIENT_TOPIC_CHANGE);
				}
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
		if(!calleeMethod.equals("handleChatEventNotify") && !calleeMethod.equals("removeClientCallback") && 
				!calleeMethod.equals("addClientCallback") && !calleeMethod.equals("issueConnEstablishMsg") ){
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
		if(!calleeMethod.equals("handleChatEventNotify") && !calleeMethod.equals("issueConnEstablishMsg") && 
		    !calleeMethod.equals("addClientCallback") && !calleeMethod.equals("removeClientCallback")){
			data[0] = this.clientStatus.toString();
			data[1] = elem;
			try {
				if(mode.equals("relayer")){
					this.rServerInt.handleChatEvent(this.clientName, data, Constants.CLIENT_NEW_MSG);
				}else{
					this.sendChatEventUpdateToAllClients(this.clientName, data, Constants.CLIENT_NEW_MSG);
				}
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
				if(mode.equals("relayer")){
					this.rServerInt.handleChatEvent(this.clientName, data, Constants.CLIENT_STATUS_CHANGE);
				}else{
					this.data = this.sServerInt.updateClientStatus(this.clientName, this.clientStatus.toString());
					this.sendChatEventUpdateToAllClients(this.clientName, this.data, Constants.CLIENT_STATUS_CHANGE);
				}
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
				if(splitData.length == 2){
					this.addToUserList(splitData[0].trim(), this.convertToUserStatus(splitData[1].trim()));
				}
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
		TemporaryUI ul = new TemporaryUI();
		String cName = ul.getUserName();

		Color col = Color.red;
		boolean filled = true;
		int x = 10, y = 100, width = 20, height = 20;
		Circle c = new Circle(x, y, width, height, col, filled);

		//String mode ="relayer";
		String mode ="p2p";
		final ChatClient ch = new ChatClient(cName, mode);
		c.addCH(ch);
		ch.addCircle(c);
		OEFrame oeFrame = ObjectEditor.edit(ch);
		oeFrame.setTelePointerModel(c);
//		ObjectEditor.edit(TraceableDisplayAndWaitManagerFactory.getTraceableDisplayAndPrintManager());
		
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		ch.data[0] = ch.clientStatus.toString();
            		Constants c = new Constants();
        			ch.sServerInt.unRegisterCallback(ch.clientName);
        			if(ch.mode.equals("p2p")){
        				ch.sendRemovalOfCallbackEvt();
        			}
            		ch.rServerInt.handleChatEvent(ch.clientName, ch.data, c.CLIENT_EXIT);
            	}catch(Exception e){
            		System.out.println("chatClientModel exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
    }
}
