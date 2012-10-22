package sessionServer;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
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

import relayServer.RelayServerInterface;
import client.ClientCallbackInterface;

public class SessionServerImpl extends UnicastRemoteObject implements SessionServerInterface {    	
	//ClientListMap
    Map <String, ClientCallbackInterface> clientMap;
    Map <String, String> clientStatusMap;
    OEServerView view;
    OEServerModel model;
    DateFormat dateFormat;
    
    public SessionServerImpl () throws RemoteException {  
    	model = new OEServerModel(this);
    	view = new OEServerView();
    }
	
	public void registerCallback(String cName, String cStatus, ClientCallbackInterface tCallback){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, tCallback);
			clientStatusMap.put(cName, cStatus);
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has joined the telepointer session </br>";			
			view.appendArchive(msg);
		}		
	}
	
	public ClientCallbackInterface getCallback(String cName){
		if(clientMap.containsKey(cName)){
			return clientMap.get(cName);
		}
		return null;
	}
	
	public String getUserList(){
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
		}					
		if(clientStatusMap.containsKey(cName)){
			clientStatusMap.remove(cName);
		}
	}
	
    public class OEServerModel{
    	public OEServerModel(SessionServerImpl ts){
    		ts.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		ts.clientMap = new HashMap<String, ClientCallbackInterface>();    		
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
	        
	        frame.setTitle("Session Server");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();      
	        frame.setVisible(true);
	        
	        //Init Msg
	        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        String s = "["+dateFormat.format(new Date())+"] Session Server is online</br>";
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

	public String[] updateClientStatus(String cName, String clientStatus){
		String result[] = new String[2];
		result[0] = this.getUserList();
		if(this.clientStatusMap.containsKey(cName)){
			clientStatusMap.put(cName, clientStatus);
			result[0] = this.getUserList();
		}
		return result;
	}
	
	@Override
	public void sendMyCallbackToUser(String source, String dest){
		if(clientMap.containsKey(source) && clientMap.containsKey(dest)){
			ClientCallbackInterface sourceCB = clientMap.get(source);
			ClientCallbackInterface destCB = clientMap.get(dest);
			try {
				destCB.addClientCallback(source, sourceCB);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}