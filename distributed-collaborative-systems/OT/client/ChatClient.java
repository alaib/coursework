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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import misc.Constants;
import oeHelper.Circle;
import otHelper.EditWithOTTimeStamp;
import otHelper.EditWithOTTimeStampInterface;
import otHelper.MsgWithEpoch;
import otHelper.OTTimeStamp;
import relayServer.RelayServerInterface;
import tracer.MVCTracerInfo;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.AListenableString;
import util.models.AListenableVector;
import util.models.PropertyListenerRegisterer;
import util.models.VectorChangeEvent;
import util.models.VectorListener;
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
	String message = "";
	AListenableString topic;
	
	AListenableVector<String> historyBuffer = new AListenableVector<String>();
	AListenableVector<String> userList = new AListenableVector<String>();
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	PropertyChangeListener pListener;
	Circle c;
	RelayServerInterface rServerInt;
	ClientCallbackImpl callback;
	
	String data[] = new String[3];
	String result[] = new String[4];
	String connUserList[];
	
	boolean delayed = true;
	int minDelay = 6000;
	int maxDelay = 6000;
	Random randomGenerator = new Random();
	Timer timer;
    DateFormat dateFormat;
	
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
	
	// OT
	List <EditWithOTTimeStampInterface> lBuffer = new ArrayList<EditWithOTTimeStampInterface>();
	OTTimeStamp myOTTimeStamp = new OTTimeStamp();
    Map <String, String> editLog = new HashMap<String, String>();
	int id = -1;
	
	//OT for Messages
	List <MsgWithEpoch> msgList = new ArrayList<MsgWithEpoch>();
	long remoteEpoch = -1;
	int epochSet = 0;
	
	ChatClient(String cName) {
		Tracer.setKeywordDisplayStatus(this, true);
		self = this;
		addListeners(self);
		addPropertyChangeListener(pListener);
		
		// Set Random Number Seed
        Date date = new Date();
        long time = date.getTime();
		randomGenerator.setSeed(time);
		
		//Timer
		timer = new Timer();
		dateFormat = new SimpleDateFormat("HH:mm:ss:S");
		
		this.clientName = cName;
		
		try {
			rServerInt = (RelayServerInterface) Naming.lookup ("rmi://localhost/RelayServer");
			callback = new ClientCallbackImpl(this);
			this.id = rServerInt.registerCallback(this.clientName, this.clientStatus.toString(), callback);
			
			this.cui = new CustomChatUI(this); 
			addToUserList(cName, clientStatus);
			String currTopic = "";
			currTopic = this.rServerInt.getCurrentTopic();
			
			topic = new AListenableString(currTopic);
			this.cui.topicTextUI.setText(currTopic);
			topic.addVectorListener(vListener);
			
			//Get latecomer msgs, add it to msgList and update UI
			String []lMsgs = rServerInt.getLatecomerMsgs();
			for(int i = 0; i < lMsgs.length; i++){
				String msg = lMsgs[i];
				int index = msg.indexOf("-");
				String epStr = msg.substring(0, index-1);
				long epoch = Long.parseLong(epStr);
				String m = msg.substring(index+1, msg.length());
				MsgWithEpoch me = new MsgWithEpoch(epoch, m);
				msgList.add(me);
				int msgPos = i;
				this.addElemToHistBuffer(msgPos, m);
				cui.appendTextToArchivePane(msgPos, m+"\n");
			}
			
			data[0] = this.clientStatus.toString();
			
			//Client Join and exit should be noted to both rServer and sServer
			//rServer
			rServerInt.handleChatEvent(clientName, data, Constants.CLIENT_JOIN);
		} catch (RemoteException | MalformedURLException | NotBoundException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	  			
	}

	public boolean getDelayed(){
		return this.delayed;
	}
	
	public String convertToKey(EditWithOTTimeStampInterface ed) throws RemoteException{
		String key = ed.getPos()+"-"+ed.getChar()+"-"+ed.getLocalCount()+"-"+ed.getRemoteCount();
		return key;
	}
	
	public boolean retrieveDelayFlag(){
		return this.delayed;
	}
	
	public int retrieveMinDelay(){
		return this.minDelay;
	}
	
	public int retrieveMaxDelay(){
		return this.maxDelay;
	}
	
	public void setHistoryBuffer(AListenableVector<String> buf){
		this.historyBuffer = buf;
	}
	public AListenableVector<String> getHistoryBuffer(){
		return this.historyBuffer;
	}
	
	public void setTopic(AListenableString t){
		this.topic = t;
	}
	public AListenableString getTopic(){
		return this.topic;
	}
	
	public void setUserList(AListenableVector<String> l){
		this.userList = l;
	}
	
	public AListenableVector<String> getUserList(){
		return this.userList;
	}
	
	public void addCircle(Circle circle){
		c = circle;
		this.cui.addCircle(c);
		try {
			Point p = this.rServerInt.getCurrPoint();
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
		try {
			p = this.rServerInt.getCurrPoint();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;
	}
	
	public void issueConnEstablishMsg(String connUser){
		this.setMessage(connUser+" joined the chat session, Connection Established");
	}
	
	public void sendChatEvtToServer(String[] newData, int STATUS_CODE){
		try {
			this.rServerInt.handleChatEvent(this.clientName, newData, STATUS_CODE);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendOTEvtToServer(EditWithOTTimeStampInterface edit, String newTopic, int STATUS_CODE){
		try {
			System.out.println(edit.printStr());
			System.out.println(this.clientName+ " sent OT Event to Server");
			MVCTracerInfo.newInfo(this.clientName+ " sent OT Event to Server, event desc = "+edit.printStr(), this);
			this.rServerInt.handleOTEvent(this.clientName, edit, newTopic, STATUS_CODE);
			editLog.put(self.convertToKey(edit), "1");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void transformInsertAndExecute(String cName, EditWithOTTimeStampInterface remoteEdit)throws RemoteException{
		int i;
		System.out.println("Received OTEvent from "+cName+" through Server at client = "+this.clientName);
		System.out.println("Remote OTEvent -> Pos = "+remoteEdit.getPos()+", Char = "+remoteEdit.getChar()+
						   ", lCount = "+remoteEdit.getLocalCount()+", rCount = "+remoteEdit.getRemoteCount());
		EditWithOTTimeStampInterface rEdit = remoteEdit.copy();
		
		printLBuffer(lBuffer, "LBuffer Before");
		
		synchronized(lBuffer){
			//Clean up the local buffer before processing (Remove all lEdit, where lEdit_TS < rEdit_TS)
			ListIterator<EditWithOTTimeStampInterface> it = lBuffer.listIterator(); 
			while(it.hasNext()){
				EditWithOTTimeStampInterface lEdit = (EditWithOTTimeStampInterface) it.next();
				if(remoteEdit.isGreaterThanOrEqualTo(lEdit) == 1){
					it.remove();
				}
			}
			/*
			//See slide 168 in Lecture Notes
			for(i = 0; i < lBuffer.size();i++){
				EditWithOTTimeStampInterface localEdit = lBuffer.get(i);
				//Apply(Transform (Transform (Transform (R, L1), L2) … LN))
				EditWithOTTimeStampInterface RT = transformSingle(rEdit, localEdit);
				//Apply original remote transform to local
				localEdit = transformSingle(localEdit, rEdit);
				//Increment remote counter for the edit
				localEdit.incrementRemote();
				//Replace the element in the local buffer with new local edit
				lBuffer.set(i, localEdit);
				rEdit = RT.copy();
			}
			*/
		}
		printLBuffer(lBuffer, "LBuffer After");
		//Execute rEdit
		String preMsg = "Executing remote Edit at client = "+this.clientName;
		String msg = preMsg + " - " + rEdit.printStr();
		MVCTracerInfo.newInfo(msg, this);
		System.out.println(msg);
		
		Character c = rEdit.getChar();
		int pos = rEdit.getPos();
		
		//Very important to set otherUpdate else will send duplicate events
		this.otherUpdate = 1;
		System.out.println("At "+this.clientName+", before insert, char = "+c+", pos = "+pos);
		this.topic.insertElementAt(c, pos);
		System.out.println("At "+this.clientName+", after insert, char = "+c+", pos = "+pos);
		//Increment current site remote operation count
		this.myOTTimeStamp.incrementRemoteCount();
		MVCTracerInfo.newInfo(this.clientName+" OT = "+myOTTimeStamp.printData(), this);
	}
	
			//If there is a local event in buffer which is not sent to server, transform locally
	public EditWithOTTimeStampInterface transformSingle(EditWithOTTimeStampInterface R, EditWithOTTimeStampInterface L) throws RemoteException{
		EditWithOTTimeStampInterface rEdit = R.copy();
		int rPos = R.getPos();
		int lPos = L.getPos();
		int rId = R.getId();
		int lId = L.getId();
		//Lower Id = Greater Priority, if position is same and remoteId priority is less local priority, increment local index
		if((rPos > lPos) || (rPos == lPos && R.isServer() == 1 && lId < rId)){
			rEdit.setPos(rPos+1);
		}
		return rEdit;
	}
	
	void printLBuffer(List<EditWithOTTimeStampInterface> lBuffer, String ... param){
		if(String.class.isInstance(param[0])){
			System.out.println(param[0]);
			System.out.println("===============");
		}
		System.out.println("Size = "+lBuffer.size());
		for(int i = 0; i < lBuffer.size(); i++){
			EditWithOTTimeStampInterface lEdit = lBuffer.get(i);
			try {
				System.out.println("Pos = "+lEdit.getPos()+", Char = "+lEdit.getChar()+
								   ", lCount = "+lEdit.getLocalCount()+", rCount = "+lEdit.getRemoteCount());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void sendTelePointerEvtToServer(Point p){
		try {
			this.rServerInt.handleTelePointerEvent(this.clientName, p, Constants.MOVE_POINTER);
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
						int updateCUITopic = 1;
						try {
							//Dummy Edit (pos, char, operation, isServer, OTTimeStamp)
							EditWithOTTimeStampInterface edit = (EditWithOTTimeStampInterface)
																new EditWithOTTimeStamp(-1, 'x', "D", 0, self.id, new OTTimeStamp());
							//Send Chat Event to Server
							self.cui.updateTopic(data, edit, Constants.CLIENT_TOPIC_CHANGE_DELETE, self.otherUpdate, updateCUITopic);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
						int updateCUITopic = 1;
						try {
							EditWithOTTimeStampInterface edit;
							//Send OT Event to Server (Insert's ONLY)
							if(self.otherUpdate != 1){
								myOTTimeStamp.incrementLocalCount();
								System.out.println("Adding to local buffer, clientName = "+self.clientName+", id = "+self.id);
								edit = (EditWithOTTimeStampInterface)
										new EditWithOTTimeStamp(pos, newVal, "I", 0, self.id, myOTTimeStamp.deepCopy());
								
								editLog.put(self.convertToKey(edit), "0");
								lBuffer.add(edit);
							}else{
								//Dummy Edit
								edit = (EditWithOTTimeStampInterface)
										new EditWithOTTimeStamp(-1, 'x', "I", 0, self.id, new OTTimeStamp());
							}
							self.cui.updateTopic(data, edit, Constants.CLIENT_OT_TOPIC_CHANGE_INSERT, self.otherUpdate, updateCUITopic);
							
							//Send Chat Event to Server
							//self.cui.updateTopic(data, edit, Constants.CLIENT_TOPIC_CHANGE_INSERT, self.otherUpdate);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	
	public void addToLocalBuffer(EditWithOTTimeStampInterface ed){
		lBuffer.add(ed);
	}
	
	public void incrementOTCounter(String counterType){
		if(counterType == "L"){
			this.myOTTimeStamp.incrementLocalCount();
		}else if(counterType == "R"){
			this.myOTTimeStamp.incrementRemoteCount();
		}
	}
	
	public void addToEditLog(String key, String value){
		this.editLog.put(key, value);
	}
	
	public int retrieveId(){
		return this.id;
	}
	
	public OTTimeStamp retrieveMyOTTimeStamp(){
		return this.myOTTimeStamp.deepCopy();
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
		boolean userMsg = false;
		if(!calleeMethod.equals("handleChatEventNotify") && !calleeMethod.equals("removeClientCallback") && 
				!calleeMethod.equals("addClientCallback") && !calleeMethod.equals("issueConnEstablishMsg") &&
				!calleeMethod.equals("appendLateComerMsgs")){
			
			elem = "["+dateFormat.format(new Date())+"] "+ clientName + " : " + s;
			userMsg = true;
		}else{
			elem = s;
		}
		//propertyChangeSupport.firePropertyChange("message", null, message);
		
		if(calleeMethod.equals("handleChatEventNotify")){
			System.out.println("Client = "+this.clientName+", Handle chat event notify in setMessage");
			System.out.println("handle Chat NOtify ->RemoteEpoch = "+remoteEpoch+", epochSet = "+epochSet);
		}else{
			System.out.println("No handle Chat NOtify -> RemoteEpoch = "+remoteEpoch+", epochSet = "+epochSet);
		}
		long epochTime;
		if(this.epochSet == 1 && this.remoteEpoch != -1){
			epochTime = this.remoteEpoch;
			this.remoteEpoch = -1;
			this.epochSet = 0;
		}else{
			epochTime = System.currentTimeMillis();
		}
		
		final long epoch = epochTime;
		int msgPos = insertToMsgList(epoch, elem);
		System.out.println("Add to history buffer and cui, pos =  "+msgPos+", elem = "+elem);
		this.addElemToHistBuffer(msgPos, elem);
		//historyBuffer.addElement(elem);
		// Send update to CUI
		cui.appendTextToArchivePane(msgPos, elem+"\n");
		//cui.archivePaneUI.append(elem + "\n");
		cui.typedTextUI.setText("");
		
		//Send update to everyone
		if(!calleeMethod.equals("handleChatEventNotify") && !calleeMethod.equals("issueConnEstablishMsg")
				&& !calleeMethod.equals("appendLateComerMsgs")){ 
			System.out.println("delay = "+this.delayed+" userMsg = "+userMsg);
            if(this.delayed == true && userMsg == true){
                int delay = getRandomNumber(this.minDelay, this.maxDelay);
                final String dElem = elem;
                final String dCalleeMethod = calleeMethod;
                MVCTracerInfo.newInfo("Delay = "+delay+", Msg = "+dElem, this);
                timer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data[0] = self.clientStatus.toString();
                        data[1] = dElem;
                        data[2] = Long.toString(epoch);
                        try {
                            System.out.println("calleeMethod = "+dCalleeMethod);
                            MVCTracerInfo.newInfo("New Message = "+dElem, self);
                            self.rServerInt.handleChatEvent(self.clientName, data, Constants.CLIENT_NEW_MSG);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }, delay);
            }else{
                data[0] = this.clientStatus.toString();
                data[1] = elem;
                data[2] = Long.toString(epoch);
                try {
                    System.out.println("calleeMethod = "+calleeMethod);
                    MVCTracerInfo.newInfo("New Message = "+elem, this);
                    this.rServerInt.handleChatEvent(this.clientName, data, Constants.CLIENT_NEW_MSG);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
		}
	}
	
	public int insertToMsgList(Long epoch, String elem){
		int pos = -1;
		MsgWithEpoch newMsg = new MsgWithEpoch(epoch, elem);
		//Empty Map
		if(msgList.size() == 0){ 
			pos = 0;
			System.out.println("InsertToMsgList  -> "+", client = "+this.clientName+
							   ", size = "+msgList.size()+", pos = "+pos+", epoch = "+epoch+", elem = "+elem);
			msgList.add(newMsg);
			return pos;
		}
		
		//Find correct position
		int i = 0;
		while(i < msgList.size() && msgList.get(i).epoch < epoch) {
			i += 1;
		}
		pos = i;
		
		System.out.println("InsertToMsgList -> size = "+msgList.size()+", pos = "+pos);
		if(pos == msgList.size()){
			msgList.add(newMsg);
		}else{
			for(i = msgList.size(); i > pos; i--){
				if(i == msgList.size()){
					msgList.add(msgList.get(i-1));
				}else{
					msgList.set(i, msgList.get(i-1));
				}
			}
			msgList.set(pos, newMsg);
		}
		return pos;
	}

	public String getMessage() {
		return message;
	}
	
	public void setDelayed(boolean value){
		this.delayed = value;
		if(value == true){
			MVCTracerInfo.newInfo("Delay set to true", this);
		}else{
			MVCTracerInfo.newInfo("Delay set to false", this);
			
		}
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
				this.rServerInt.handleChatEvent(this.clientName, data, Constants.CLIENT_STATUS_CHANGE);
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
			System.out.println("At client = "+this.clientName+", result = "+result);
			System.out.println(result);
			String newMsg = result[1];
			long epoch = Long.parseLong(result[3]);
			this.remoteEpoch = epoch;
			this.epochSet = 1;
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
			topic.insertElementAt(c, pos);
			MVCTracerInfo.newInfo("Topic - character '"+c+"' inserted at pos = "+pos, this);
		}
	}
	
	public int getRandomNumber(int START, int END){
		long range = END - START + 1;
		long frac = (long)(randomGenerator.nextDouble()*range);
		int r = (int)(frac + START);
		return r;
	}
	
	//Fix public variable functions
	public void addElemToHistBuffer(int pos, String msg){
		this.historyBuffer.clear();
		for(int i = 0; i < this.msgList.size(); i++){
			MsgWithEpoch m = this.msgList.get(i);
			System.out.println("i = "+i+", epoch = "+m.epoch+", msg = "+m.msg);
			this.historyBuffer.add(m.msg);
		}
	}
	
	public void insertElemAtPosForTopic(Character c, int pos){
		this.topic.insertElementAt(c, pos);
	}
	
	public void removeElemAtPosForTopic(int pos){
		this.topic.removeElementAt(pos);
	}
	
	public int getUserListIndexOfKey(String key){
		return this.userList.indexOf(key);
	}
	
	public void setUserListAtIndex(int index, String key){
		this.userList.set(index, key);
	}

	public static void main(String[] args) {
		TemporaryUI ul = new TemporaryUI();
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
		ObjectEditor.edit(TraceableDisplayAndWaitManagerFactory.getTraceableDisplayAndPrintManager());
		
		MVCTracerInfo.newInfo("Connected to Relay Server", ch);
		
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		ch.data[0] = ch.clientStatus.toString();
            		Constants c = new Constants();
            		ch.rServerInt.handleChatEvent(ch.clientName, ch.data, Constants.CLIENT_EXIT);
            	}catch(Exception e){
            		System.out.println("chatClientModel exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
    }
}