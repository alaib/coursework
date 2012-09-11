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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

public class TeleServerImpl extends UnicastRemoteObject implements TeleServerInterface {    	
	//ClientListMap
    Map <String, TeleClientCallbackInterface> clientMap;
    TeleServerView view;
    TeleServerModel model;
    DateFormat dateFormat;
    Point currPoint;
    Dimension currDim;
    
    //Constants
  	public int MOVE_POINTER = 1;
  	
    public TeleServerImpl (String msg) throws RemoteException {  
    	model = new TeleServerModel(this);
    	view = new TeleServerView();
    	currPoint = new Point(25, 90);    	
    	currDim = new Dimension(450, 470);    	
    }
	
	public void handleEvent(String cName, Point p, Dimension d, int STATUS_CODE) throws RemoteException {
		currPoint = p;		
		currDim = d;		
		sendUpdateToAllClients(cName, p, d, STATUS_CODE);		
	}
	
	public Point getCurrPoint() throws RemoteException{
		return currPoint;
	}
	
	public Dimension getCurrDim() throws RemoteException{
		return currDim;
	}
	
	public void sendUpdateToAllClients(String cName, Point p, Dimension d, int STATUS_CODE){	    	
    	for(Map.Entry<String, TeleClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		TeleClientCallbackInterface tcCallback = item.getValue();
    		if(!clientName.equals(cName)){
    			try {
					tcCallback.handleNotify(p, d, STATUS_CODE);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	    
	}
	
	public void registerCallback(String cName, TeleClientCallbackInterface tCallback){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, tCallback);
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has joined the telepointer session </br>";			
			view.appendArchive(msg);
		}
	}
	
	public void unRegisterCallback(String cName){		
		if(clientMap.containsKey(cName)){
			clientMap.remove(cName);			
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has left the telepointer session </br>";
			view.appendArchive(msg);
			if(clientMap.size() == 0){
				currPoint = new Point(25, 90);    	
		    	currDim = new Dimension(450, 470);
			}
		}			
	}
	
    public class TeleServerModel{
    	public TeleServerModel(TeleServerImpl ts){
    		ts.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		ts.clientMap = new HashMap<String, TeleClientCallbackInterface>();
    	}
    }
    
	public class TeleServerView{
		public JFrame frame;
		public JEditorPane archivePane;
		public JScrollPane archiveScrollPane;
		public Container container;
		public JPanel lPanel;
		public DateFormat dateFormat;
		public Date date;	
		
		public TeleServerView(){
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
	        lPanel.add(archiveScrollPane, BorderLayout.CENTER);
	        	                	        	        
	        container.add(lPanel, BorderLayout.CENTER);
	        
	        frame.setTitle("Telepointer Server");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();      
	        frame.setVisible(true);
	        
	        //Init Msg
	        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	        String s = "["+dateFormat.format(new Date())+"] Telepointer Server is online</br>";
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