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
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import misc.Constants;
import oeHelper.Circle;
import oeHelper.VectorStringHistory;
import otHelper.EditWithOTTimeStamp;
import otHelper.EditWithOTTimeStampInterface;
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
	
	VectorStringHistory historyBuffer = new VectorStringHistory();
	AListenableVector<String> userList = new AListenableVector<String>();
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	PropertyChangeListener pListener;
	Circle c;
	RelayServerInterface rServerInt;
	ClientCallbackImpl callback;
	
	String data[] = new String[2];
	String result[] = new String[2];
	String connUserList[];
	
	boolean delayed = false;
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
	int id = -1;
	
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
	
	public boolean retrieveDelayFlag(){
		return this.delayed;
	}
	
	public void setHistoryBuffer(VectorStringHistory buf){
		this.historyBuffer = buf;
	}
	public VectorStringHistory getHistoryBuffer(){
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
	
	public void sendOTEvtToServer(EditWithOTTimeStamp edit, String newTopic, int STATUS_CODE){
		try {
			System.out.println(this.clientName+ " sent OT Event to Server");
			MVCTracerInfo.newInfo(this.clientName+ " sent OT Event to Server", this);
			this.rServerInt.handleOTEvent(this.clientName, (EditWithOTTimeStampInterface)edit, newTopic, STATUS_CODE);
			System.out.println(this.clientName+ " send OT to Server Finished");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void transformInsertAndExecute(String cName, EditWithOTTimeStampInterface remoteEdit, String newTopic)throws RemoteException{
		System.out.println("Received OTEvent from "+cName+" through Server at client = "+this.clientName);
		EditWithOTTimeStampInterface rEdit = remoteEdit.copy();
		
		//See slide 168 in Lecture Notes
		synchronized(lBuffer){
			for(int i = 0; i < lBuffer.size();i++){
				EditWithOTTimeStampInterface localEdit = lBuffer.get(i);
				//Apply(Transform (Transform (Transform (R, L1), L2) … LN))
				EditWithOTTimeStampInterface RT = transformSingle(remoteEdit, localEdit);
				//Apply original remote transform to local
				localEdit = transformSingle(localEdit, remoteEdit);
				//Increment remote counter for the edit
				localEdit.incrementRemote();
				//Replace the element in the local buffer with new local edit
				lBuffer.set(i, localEdit);
				rEdit = RT.copy();
			}
		}
		//Execute rEdit
		System.out.println("Executing remote Edit");
		//MVCTracerInfo.newInfo("Executing remoteEdit = "+rEdit.printStr(), this);
		//topic.insert(rEdit.getPos(), rEdit.getChar());
		Character c = rEdit.getChar();
		int pos = rEdit.getPos();
		this.otherUpdate = 1;
		this.topic.insertElementAt(c, pos);
		MVCTracerInfo.newInfo("Remote Insertion: Topic - character '"+rEdit.getChar()+"' inserted at pos = "+rEdit.getPos(), this);
	}
	
	public EditWithOTTimeStampInterface transformSingle(EditWithOTTimeStampInterface R, EditWithOTTimeStampInterface L) throws RemoteException{
		EditWithOTTimeStampInterface rEdit = R.copy();
		int rPos = R.getPos();
		int lPos = L.getPos();
		int remoteId = R.getId();
		int localId = L.getId();
		//Lower Id = Greater Priority
		if((rPos > lPos) || (rPos == lPos && remoteId < localId)){
			rEdit.setPos(rPos+1);
		}
		return rEdit;
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
						try {
							//Dummy Edit
							EditWithOTTimeStamp edit = new EditWithOTTimeStamp(-1, 'x', "D", -1, new OTTimeStamp());
							//Send Chat Event to Server
							self.cui.updateTopic(data, edit, Constants.CLIENT_TOPIC_CHANGE_DELETE, self.otherUpdate);
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
						//Send OT Event to Server (Insert's ONLY)
						if(self.otherUpdate != 1){
							myOTTimeStamp.incrementLocalCount();
						}
						try {
							EditWithOTTimeStamp edit = new EditWithOTTimeStamp(pos, newVal, "I", self.id, myOTTimeStamp);
							//Send Chat Event to Server
							//self.cui.updateTopic(data, edit, Constants.CLIENT_TOPIC_CHANGE_INSERT, self.otherUpdate);
							self.cui.updateTopic(data, edit, Constants.CLIENT_OT_TOPIC_CHANGE_INSERT, self.otherUpdate);
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
				!calleeMethod.equals("addClientCallback") && !calleeMethod.equals("issueConnEstablishMsg") ){
			
			elem = "["+dateFormat.format(new Date())+"] "+ clientName + " : " + s;
			userMsg = true;
		}else{
			elem = s;
		}
		propertyChangeSupport.firePropertyChange("message", null, message);
		
		historyBuffer.addElement(elem);
		// Send update to CUI
		cui.archivePaneUI.append(elem + "\n");
		cui.typedTextUI.setText("");
		
		//Send update to everyone
		if(!calleeMethod.equals("handleChatEventNotify") && !calleeMethod.equals("issueConnEstablishMsg")){ 
			System.out.println("delay = "+this.delayed+" userMsg = "+userMsg);
            if(this.delayed == true && userMsg == true){
                int delay = getRandomNumber(1000, 5000);
                final String dElem = elem;
                final String dCalleeMethod = calleeMethod;
                MVCTracerInfo.newInfo("Delay = "+delay+", Msg = "+dElem, this);
                timer.schedule(new TimerTask(){
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        data[0] = self.clientStatus.toString();
                        data[1] = dElem;
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
	public void addElemToHistBuffer(String msg){
		this.historyBuffer.addElement(msg);
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