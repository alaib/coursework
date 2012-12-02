package relayServer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

import misc.Constants;
import otHelper.EditWithOTTimeStampInterface;
import otHelper.OTTimeStamp;
import tracer.MVCTracerInfo;
import client.ClientCallbackInterface;

/**
 * RelayServerImpl implements the interface RelayServerInterface and provides relayer functionality
 * @author ravikirn
 *
 */
public class RelayServerImpl extends UnicastRemoteObject implements RelayServerInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//ClientListMap
    Map <String, ClientCallbackInterface> clientMap;
    Map <String, String> clientStatusMap;
    Map <String, OTTimeStamp> myClientOTTimeStamp;
    Map <String, List<EditWithOTTimeStampInterface>> myClientOTBuffer;
    OEServerView view;
    OEServerModel model;
    DateFormat dateFormat;
    Point currPoint;
    Dimension currDim;
    String currTopic = "";
    int priority = 0;
    
    public RelayServerImpl () throws RemoteException {  
    	model = new OEServerModel(this);
    	view = new OEServerView();
    	currPoint = new Point(20, 50);    	
    }
	
	public void handleTelePointerEvent(String cName, Point p, int STATUS_CODE) throws RemoteException {
		//System.out.println("TelePointer request received from client = "+cName+", status_code = "+STATUS_CODE);
		currPoint = p;
		sendTelePointerUpdateToAllClients(cName, p, STATUS_CODE);		
	}
	
	public void handleChatEvent(String cName, String[] data, int STATUS_CODE) throws RemoteException {
		//System.out.println("ChatEvent request received from client = "+cName+", status_code = "+STATUS_CODE);
		String result[] = new String[3];
		result[0] = this.computeUserList();
		if(STATUS_CODE == Constants.CLIENT_JOIN){
			String clientStatus = data[0];
			clientStatusMap.put(cName, clientStatus);
		}else if(STATUS_CODE == Constants.CLIENT_STATUS_CHANGE){
			if(this.clientStatusMap.containsKey(cName)){
				String clientStatus = data[0];
				clientStatusMap.put(cName, clientStatus);
				result[0] = this.computeUserList();
			}
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE){
			String newTopic = data[1];
			result[1] = newTopic;
		}else if(STATUS_CODE == Constants.CLIENT_NEW_MSG){
			String newMsg = data[1];
			result[1] = newMsg;
		}else if(STATUS_CODE == Constants.CLIENT_EXIT){
			this.unRegisterCallback(cName);
			result[0] = this.computeUserList();
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_DELETE){
			result[0] = data[0];
			result[1] = data[1];
			String newTopic = data[2];
			this.currTopic = newTopic;
		}else if(STATUS_CODE == Constants.CLIENT_TOPIC_CHANGE_INSERT){
			result[0] = data[0];
			result[1] = data[1];
			String newTopic = data[2];
			this.currTopic = newTopic;
		}
		result[2] = cName;
		sendChatEventUpdateToAllClients(cName, result, STATUS_CODE);
	}
	
	public void handleOTEvent(String cName, EditWithOTTimeStampInterface ed, String newTopic, int STATUS_CODE) throws RemoteException {
		System.out.println("Received OT Event from client = "+cName);
		this.currTopic = newTopic;
		
		//Transform ed at the server
		//Set the server flag
		ed.setServer(1);
		EditWithOTTimeStampInterface edRemote = transformEditAtServer(cName, ed);
		//Disable server flag as it will be turned into local for others
		edRemote.setServer(0);
		//Extract and increment the client's remote counter value at server and put it back
		OTTimeStamp cliOTTimeStamp = myClientOTTimeStamp.get(cName);
		cliOTTimeStamp.incrementRemoteCount();
		myClientOTTimeStamp.put(cName, cliOTTimeStamp);
		
		//Convert transformed remote edit into local for everyone else and increment the timestamp for them
		EditWithOTTimeStampInterface localEdit = edRemote.copy();
		
    	for(Map.Entry<String, ClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		ClientCallbackInterface tcCallback = item.getValue();
    		if(!clientName.equals(cName)){
    			try {
					//Insert localEditInstance as a local operation for all other clients and put it back
					EditWithOTTimeStampInterface localEditInstance = localEdit.copy();
					List <EditWithOTTimeStampInterface> lBufferInstance = myClientOTBuffer.get(clientName);
					lBufferInstance.add(localEditInstance);
					myClientOTBuffer.put(clientName, lBufferInstance);
					
    				//Increment respective client local timestamp
					OTTimeStamp cOTTimeStamp = myClientOTTimeStamp.get(clientName);
					cOTTimeStamp.incrementLocalCount();
					myClientOTTimeStamp.put(clientName, cOTTimeStamp);
    				
					//Send local operation to client
    				tcCallback.transformInsertAndExecute(cName, localEditInstance);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	    
	}
	
	public EditWithOTTimeStampInterface transformEditAtServer(String cName, EditWithOTTimeStampInterface remoteEdit) throws RemoteException{
		int i;
		System.out.println("At Server -> Received OTEvent from "+cName);
		System.out.println("At Server -> Remote OTEvent -> "+remoteEdit.printStr());
		EditWithOTTimeStampInterface rEdit = remoteEdit.copy();
		EditWithOTTimeStampInterface prevEdit = remoteEdit.copy();
		
		//Extract lBuffer, make changes and put it back
		List <EditWithOTTimeStampInterface> lBuffer = myClientOTBuffer.get(cName);
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
		
			//See slide 168 in Lecture Notes
			for(i = 0; i < lBuffer.size();i++){
				EditWithOTTimeStampInterface localEdit = lBuffer.get(i);
				//Apply(Transform (Transform (Transform (R, L1), L2) … LN))
				EditWithOTTimeStampInterface RT = transformSingle(rEdit, localEdit);
				//Apply original remote transform to local
				System.out.println("Before lEdit = "+localEdit.printStr());
				System.out.println("rEdit = "+rEdit.printStr());
				//Temporarily set it to server
				localEdit.setServer(1);
				localEdit = transformSingle(localEdit, rEdit);
				localEdit.setServer(0);
				//Reset serverFlag
				//Transform
				System.out.println("After lEdit = "+localEdit.printStr());
				//Increment remote counter for the edit
				localEdit.incrementRemote();
				//Replace the element in the local buffer with new local edit
				lBuffer.set(i, localEdit);
				rEdit = RT.copy();
			}
		}
		//Put it back after removals and transform changes
		myClientOTBuffer.put(cName,  lBuffer);
		
		printLBuffer(lBuffer, "LBuffer After");
		//Execute rEdit
		System.out.println("At Server -> Dummy Executing remote Edit");
		String msg1 = "At Server -> " + rEdit.printStr();
		System.out.println(msg1);
		return rEdit;
	}
	
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
				System.out.println(lEdit.printStr());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	

	public Point getCurrPoint() throws RemoteException{
		return currPoint;
	}
	
	public void sendTelePointerUpdateToAllClients(String cName, Point p, int STATUS_CODE){	    	
    	for(Map.Entry<String, ClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		ClientCallbackInterface tcCallback = item.getValue();
    		if(!clientName.equals(cName)){
    			try {
					tcCallback.handleTelePointerNotify(p, STATUS_CODE);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	    
	}
	
	public void sendChatEventUpdateToAllClients(String cName, String[] data, int STATUS_CODE){	    	
    	for(Map.Entry<String, ClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		ClientCallbackInterface tcCallback = item.getValue();
    		if(!clientName.equals(cName) || STATUS_CODE == Constants.CLIENT_JOIN){
    			try {
					tcCallback.handleChatEventNotify(data, STATUS_CODE);
					if(STATUS_CODE == Constants.CLIENT_JOIN){
						tcCallback.handleTelePointerNotify(this.currPoint, Constants.MOVE_POINTER);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	    
	}
	
	public String getCurrentTopic(){
		return this.currTopic;
	}
	
	public int registerCallback(String cName, String cStatus, ClientCallbackInterface tCallback){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, tCallback);
			clientStatusMap.put(cName, cStatus);
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has joined the telepointer session </br>";			
			view.appendArchive(msg);
			
			//Add an entry in myClientOTTimeStamp and myClientOTBuffer
			List <EditWithOTTimeStampInterface> lBuffer = new ArrayList<EditWithOTTimeStampInterface>();
			OTTimeStamp myOTTimeStamp = new OTTimeStamp();
			myClientOTTimeStamp.put(cName, myOTTimeStamp);
			myClientOTBuffer.put(cName,  lBuffer);
			
			priority += 1;
			return priority;
		}		
		return -1;
	}
	
	public String computeUserList(){
		String s = "";
		for(Map.Entry<String, String> item : clientStatusMap.entrySet()){
    		String clientName = item.getKey();
    		String clientStatus = item.getValue();
    		s = s + clientName + " - " + clientStatus + "\n";
    	}	    
		return s;
	}
	
	public void unRegisterCallback(String cName){		
		if(clientMap.containsKey(cName)){
			clientMap.remove(cName);			
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has left the telepointer session </br>";
			view.appendArchive(msg);
			if(clientMap.size() == 0){
				currPoint = new Point(25, 90);    	
				currTopic = "";
				priority = 0;
			}		
		}					
		if(clientStatusMap.containsKey(cName)){
			clientStatusMap.remove(cName);
		}
		if(myClientOTTimeStamp.containsKey(cName)){
			myClientOTTimeStamp.remove(cName);
		}
		if(myClientOTBuffer.containsKey(cName)){
			myClientOTBuffer.remove(cName);
		}
	}
	
    public class OEServerModel{
    	public OEServerModel(RelayServerImpl ts){
    		ts.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		ts.clientMap = new HashMap<String, ClientCallbackInterface>();    		
    		ts.clientStatusMap = new HashMap<String, String>();    		
    		ts.myClientOTTimeStamp = new HashMap<String, OTTimeStamp>();
    		ts.myClientOTBuffer = new HashMap<String, List<EditWithOTTimeStampInterface>>();
    	}
    }
    
	public class OEServerView{
		public JFrame frame;
		public JEditorPane archivePane;
		public JScrollPane archiveScrollPane;
		public Container container;
		public JPanel lPanel;
		public DateFormat dateFormat;
		public Date date;	
		
		public OEServerView(){
			frame = new JFrame();
	    	frame.setSize(400, 200);    	
	    	
	    	container = frame.getContentPane();
	    	container.setLayout(new BorderLayout());
	    	                
	        archivePane = new JEditorPane();        
	        archivePane.setEditable(false);
	        archivePane.setContentType("text/html");
	        archivePane.setPreferredSize(new Dimension(500, 400));	        	        	                              	       
	        	        	        
	        lPanel = new JPanel(new BorderLayout());        
	        archiveScrollPane = new JScrollPane(archivePane);
	        archiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        lPanel.add(archiveScrollPane, BorderLayout.CENTER);
	        	                	        	        
	        container.add(lPanel, BorderLayout.CENTER);
	        
	        frame.setTitle("Relay Server");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();      
	        frame.setVisible(true);
	        
	        //Init Msg
	        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        String s = "["+dateFormat.format(new Date())+"] Relay Server is online</br>";
	        appendArchive(s);	          
		}
		
		public void appendArchive(String s){    	
	    	HTMLEditorKit editor = (HTMLEditorKit)archivePane.getEditorKit();    	    	
	    	StringReader reader = new StringReader(s);

	    	try {
	    	  editor.read(reader, archivePane.getDocument(), archivePane.getDocument().getLength());
	    	}
	    	catch(BadLocationException ex) {
	    	   //This happens if your offset is out of bounds.
	    		System.out.println(ex);
	    	}
	    	catch (IOException ex) {
	    	  // I/O error
	    		System.out.println(ex);
	    	}
	    }
	}
}