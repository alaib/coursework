import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

public class OEServerImpl extends UnicastRemoteObject implements OEServerInterface {    	
	//ClientListMap
    Map <String, OEClientCallbackInterface> clientMap;
    Map <String, String> clientStatusMap;
    OEServerView view;
    OEServerModel model;
    DateFormat dateFormat;
    Point currPoint;
    Dimension currDim;
    
    //Constants
  	public int MOVE_POINTER = 1;
  	public int CLIENT_JOIN = 2;
	public int CLIENT_EXIT = 3;
	public int CLIENT_STATUS_CHANGE = 4;
	public int CLIENT_TOPIC_CHANGE = 5;
	public int CLIENT_NEW_MSG = 6;
  	
    public OEServerImpl (String msg) throws RemoteException {  
    	model = new OEServerModel(this);
    	view = new OEServerView();
    	currPoint = new Point(10, 100);    	
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
		if(STATUS_CODE == this.CLIENT_JOIN){
			String clientStatus = data[0];
			clientStatusMap.put(cName, clientStatus);
		}else if(STATUS_CODE == this.CLIENT_STATUS_CHANGE){
			if(this.clientStatusMap.containsKey(cName)){
				String clientStatus = data[0];
				clientStatusMap.put(cName, clientStatus);
				result[0] = this.computeUserList();
			}
		}else if(STATUS_CODE == this.CLIENT_TOPIC_CHANGE){
			String newTopic = data[1];
			result[1] = newTopic;
		}else if(STATUS_CODE == this.CLIENT_NEW_MSG){
			String newMsg = data[1];
			result[1] = newMsg;
		}else if(STATUS_CODE == this.CLIENT_EXIT){
			this.unRegisterCallback(cName);
			result[0] = this.computeUserList();
		}
		sendChatEventUpdateToAllClients(cName, result, STATUS_CODE);
	}
	
	public Point getCurrPoint() throws RemoteException{
		return currPoint;
	}
	
	public void sendTelePointerUpdateToAllClients(String cName, Point p, int STATUS_CODE){	    	
    	for(Map.Entry<String, OEClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		OEClientCallbackInterface tcCallback = item.getValue();
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
    	for(Map.Entry<String, OEClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		OEClientCallbackInterface tcCallback = item.getValue();
    		if(!clientName.equals(cName) || STATUS_CODE == this.CLIENT_JOIN){
    			try {
					tcCallback.handleChatEventNotify(data, STATUS_CODE);
					if(STATUS_CODE == this.CLIENT_JOIN){
						tcCallback.handleTelePointerNotify(this.currPoint, this.MOVE_POINTER);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	    
	}
	
	public void registerCallback(String cName, String cStatus, OEClientCallbackInterface tCallback){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, tCallback);
			clientStatusMap.put(cName, cStatus);
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has joined the telepointer session </br>";			
			view.appendArchive(msg);
		}		
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
			}		
		}					
		if(clientStatusMap.containsKey(cName)){
			clientStatusMap.remove(cName);
		}
	}
	
    public class OEServerModel{
    	public OEServerModel(OEServerImpl ts){
    		ts.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		ts.clientMap = new HashMap<String, OEClientCallbackInterface>();    		
    		ts.clientStatusMap = new HashMap<String, String>();    		
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
	        
	        frame.setTitle("Telepointer Server");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();      
	        frame.setVisible(true);
	        
	        //Init Msg
	        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        String s = "["+dateFormat.format(new Date())+"] Object Editor Server is online</br>";
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