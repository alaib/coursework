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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import misc.Constants;
import oeHelper.Circle;
import oeHelper.VectorStringHistory;
import relayServer.RelayServerInterface;
import sessionServer.SessionServerInterface;
import tracer.MVCTracerInfo;
import tracer.TraceableListenerMod;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.AListenableString;
import util.models.AListenableVector;
import util.models.PropertyListenerRegisterer;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
import util.trace.TraceableBus;
import util.trace.Tracer;
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
	public AListenableString topic;
	public VectorStringHistory historyBuffer = new VectorStringHistory();
	public AListenableVector<String> userList = new AListenableVector<String>();
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	PropertyChangeListener pListener;
	Circle c;
	TemporaryUI tempUI;
	RelayServerInterface rServerInt;
	SessionServerInterface sServerInt;
	ClientCallbackImpl callback;
	public boolean relayMode = true;
	
	Map <String, ClientCallbackInterface> clientMap;
    Map <String, String> clientStatusMap;
    
	String data[] = new String[2];
	String result[] = new String[2];
	String mode = "";
	String connUserList[];
	int delayed = 0;
	
	public enum uStatus {
		Available, Busy, Invisible, Idle
	};

	public String clientName;
	public uStatus clientStatus = uStatus.Available;
	protected CustomChatUI cui; 
	ChatClient self;
	
	int otherUpdate = 0;
	VectorListener vListener;
	
	int tracerActive = 0;
	ArrayList<String> tracerMsgs = new ArrayList<String>();
	
	ChatClient(String cName, String m) {
		Tracer.setKeywordDisplayStatus(this, true);
		mode = m;
		if(mode.equals("relay")){
			this.relayMode = true;
		}else{
			this.relayMode = false;
		}
		self = this;
		addListeners(self);
		addPropertyChangeListener(pListener);
		
		this.clientName = cName;
		this.clientMap = new HashMap<String, ClientCallbackInterface>();    		
		this.clientStatusMap = new HashMap<String, String>();    		
		
		try {
			rServerInt = (RelayServerInterface) Naming.lookup ("rmi://localhost/RelayServer");
			sServerInt = (SessionServerInterface) Naming.lookup ("rmi://localhost/SessionServer");
			MVCTracerInfo.newInfo("Connected to session server", this);
			callback = new ClientCallbackImpl(this);
			rServerInt.registerCallback(this.clientName, this.clientStatus.toString(), callback);
			sServerInt.registerCallback(this.clientName, this.clientStatus.toString(), callback);
			
			this.cui = new CustomChatUI(this); 
			addToUserList(cName, clientStatus);
			String currTopic = "";
			if(mode.equals("relay")){
				currTopic = this.rServerInt.getCurrentTopic();
			}else{
				currTopic = this.sServerInt.getCurrentTopic();
			}
			topic = new AListenableString(currTopic);
			this.cui.topicTextUI.setText(currTopic);
			topic.addVectorListener(vListener);
			
			data[0] = this.clientStatus.toString();
			tempUI = new TemporaryUI();
			
			//Client Join and exit should be noted to both rServer and sServer
			//rServer
			rServerInt.handleChatEvent(clientName, data, Constants.CLIENT_JOIN);
			//sServer
			String uList = sServerInt.getUserList();
			String[] connUserList = uList.split("\n");
			if(!uList.equals("")){
				result[0] = uList;
				this.handleChatEventNotify(result, Constants.CLIENT_JOIN);
			}
			if(connUserList.length <= 1){
				//this.tempUI.showMsgBox("You are the only user connected, please wait until others join");
			}else{
				String connUser = "";
				for(int i = 0; i < connUserList.length; i++){
					String[] splitArr = connUserList[i].split("-");
					if(splitArr.length != 2){
						System.out.println("Error! splitArr length < 2, String = "+connUserList[i]);
						continue;
					}
					connUser = splitArr[0].trim();
					if(!connUser.equals(this.clientName)){
						ClientCallbackInterface cb = sServerInt.getCallback(connUser);
						if(!clientMap.containsKey(connUser)){
							clientMap.put(connUser, cb);
							this.issueConnEstablishMsg(connUser);
							sServerInt.sendMyCallbackToUser(this.clientName, connUser);
						}
					}
				}	
				//Update the topic from user
				tracerMsgs.add("Fetched callbacks of all other clients via Session Server");
				this.data[0] = uList;
				this.sendChatEventUpdateToAllClients(this.clientName, this.data, Constants.CLIENT_JOIN);
				tracerMsgs.add("Sent an update saying "+this.clientName+" joined the session");
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

	public void activateTracer(){
		this.tracerActive = 1;
		//Empty the queue and add to tracer
		for(int i = 0; i < this.tracerMsgs.size(); i++){
			MVCTracerInfo.newInfo(tracerMsgs.get(i), this);
		}
	}
	
	public int retrieveDelayFlag(){
		return this.delayed;
	}
	
	public void setRelayMode(boolean value){
		this.relayMode = value;
		if(value == true){
			this.mode = "relay";
			MVCTracerInfo.newInfo("Changed mode to relay", this);
		}else{
			this.mode = "p2p";
			MVCTracerInfo.newInfo("Changed mode to p2p", this);
		}
	}
	
	public boolean getRelayMode(){
		return this.relayMode;
	}
	
	public void addCircle(Circle circle){
		c = circle;
		this.cui.addCircle(c);
		try {
			Point p = new Point(20, 50);
			if(mode.equals("relay")){
				p = this.rServerInt.getCurrPoint();
			}else{
				p = this.sServerInt.getCurrentPoint();
			}
			this.c.setX(p.x);
			this.c.setY(p.y);
			this.cui.myGlassPane.setPoint(p);
			this.cui.myGlassPane.repaint();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Point retrieveCurrPoint(){
		Point p = new Point(20, 50);
		if(mode.equals("relay")){
			if(this.rServerInt != null){
				try {
					p = this.rServerInt.getCurrPoint();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			if(this.sServerInt != null){
				try {
					p = this.sServerInt.getCurrentPoint();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return p;
	}
	
	public void issueConnEstablishMsg(String connUser){
		this.setMessage(connUser+" joined the chat session, Connection Established");
	}
	
	public void sendChatEvtToServer(String[] newData, int STATUS_CODE){
		try {
			if(mode.equals("relay")){
				this.rServerInt.handleChatEvent(this.clientName, newData, STATUS_CODE);
			}else{
				this.sendChatEventUpdateToAllClients(this.clientName, newData, STATUS_CODE);
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
			if(mode.equals("relay")){
				this.rServerInt.handleTelePointerEvent(this.clientName, p, Constants.MOVE_POINTER);
			}else{
				this.sServerInt.setCurrentPoint(p);
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
	
	public void addListeners(ChatClient c) {
		pListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
			}
		};
		vListener = new VectorListener(){
			@Override
			public void updateVector(VectorChangeEvent evt) {
				int evtType = evt.getEventType();
				Character newVal = (Character)evt.getNewValue();
				Character oldVal = (Character)evt.getOldValue();
				int pos = evt.getPosition();
				//Object, EventType, oldSize, null, element, newSize
				if(evtType == VectorChangeEvent.AddComponentEvent){
					//System.out.println("Event = AddComponent, pos = "+pos+", oldVal = "+oldVal+", newVal = "+newVal);
				}else if(evtType == VectorChangeEvent.DeleteComponentEvent){
					if(self.cui.updateIssued == 0){
						String[] data = new String[3];
						data[0] = Integer.toString(pos);
						data[1] = Character.toString(oldVal);
						data[2] = "";
						self.cui.updateTopic(data, Constants.CLIENT_TOPIC_CHANGE_DELETE, self.otherUpdate);
						if(self.otherUpdate == 1){
							self.otherUpdate = 0;
						}
					}
				}else if(evtType == VectorChangeEvent.InsertComponentEvent){
					if(self.cui.updateIssued == 0){
						String[] data = new String[3];
						data[0] = Integer.toString(pos);
						data[1] = Character.toString(newVal);
						data[2] = "";
						self.cui.updateTopic(data, Constants.CLIENT_TOPIC_CHANGE_INSERT, self.otherUpdate);
						if(self.otherUpdate == 1){
							self.otherUpdate = 0;
						}
					}
				}else if(evtType == VectorChangeEvent.ReplaceComponentEvent){
					//System.out.println("Event = ReplaceComponent, pos = "+pos+", oldVal = "+oldVal+", newVal = "+newVal);
				}else if(evtType == VectorChangeEvent.ClearEvent){
					//System.out.println("Event = ClearComponent, pos = "+pos+", oldVal = "+oldVal+", newVal = "+newVal);
				}
			}
		};
	}
	
	public String retrieveMode(){
		return this.mode;
	}

	public void addClientCallback(String cName, ClientCallbackInterface cb){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, cb);
			MVCTracerInfo.newInfo("Received a callback from "+cName+", appended it to the list", this);
			if(cName != this.clientName){
				this.setMessage(cName+ " joined chat session, Connection Established");
			}
		}
	}
	
	public void removeClientCallback(String cName){
		if(clientMap.containsKey(cName)){
			clientMap.remove(cName);
			MVCTracerInfo.newInfo("Removed callback of '"+cName+"' from the list", this);
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
				System.out.println("calleeMethod = "+calleeMethod);
				MVCTracerInfo.newInfo("New Message = "+elem, this);
				if(mode.equals("relay")){
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
		if(!calleeMethod.equals("handleChatEventNotify") && !calleeMethod.equals("setClientStatusFromObj")){
			data[0] = this.clientStatus.toString();
			try {
				MVCTracerInfo.newInfo("My New Status = "+data[0], this);
				if(mode.equals("relay")){
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
				MVCTracerInfo.newInfo("Update received - Move Telepointer to = ("+p.x+","+p.y+")", this);
			}
		}
	}
	
	public void handleChatEventNotify(String[] result, int STATUS_CODE){
		String uClient = "";
		if(result.length > 2){
			uClient = result[2];
		}
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
			if(STATUS_CODE == Constants.CLIENT_STATUS_CHANGE){
				MVCTracerInfo.newInfo(uClient+" changed his status", this);
			}else if(STATUS_CODE == Constants.CLIENT_EXIT){
				MVCTracerInfo.newInfo(uClient+" left the session", this);
			}
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE){
		}else if(STATUS_CODE == Constants.CLIENT_NEW_MSG){
			String newMsg = result[1];
			this.setMessage(newMsg);
			MVCTracerInfo.newInfo("New message received = "+newMsg, this);
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_DELETE){
			int pos = Integer.parseInt(result[0], 10);
			this.otherUpdate = 1;
			topic.removeElementAt(pos);
			MVCTracerInfo.newInfo("Topic - character deleted at pos = "+pos, this);
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_INSERT){
			int pos = Integer.parseInt(result[0], 10);
			Character c = result[1].charAt(0);
			this.otherUpdate = 1;
			topic.insert(pos, c);
			MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
		}
	}
	
	public void updateTopic(String s){
		try {
			this.sServerInt.setCurrentTopic(s);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TemporaryUI ul = new TemporaryUI();
		String cName = ul.getUserName();

		Color col = Color.red;
		boolean filled = true;
		int x = 10, y = 100, width = 20, height = 20;
		Circle c = new Circle(x, y, width, height, col, filled);

		//String mode ="relay";
		String mode ="relay";
		final ChatClient ch = new ChatClient(cName, mode);
		c.addCH(ch);
		ch.addCircle(c);
		OEFrame oeFrame = ObjectEditor.edit(ch);
		oeFrame.setTelePointerModel(c);
		ObjectEditor.edit(TraceableDisplayAndWaitManagerFactory.getTraceableDisplayAndPrintManager());
		
		MVCTracerInfo.newInfo("Connected to Relay Server", ch);
		MVCTracerInfo.newInfo("Connected to Session Server", ch);
		MVCTracerInfo.newInfo("Mode = "+mode, ch);
		
		ch.activateTracer();
		
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
