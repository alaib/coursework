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

public class TeleServerImpl extends UnicastRemoteObject implements TeleServerInterface {    	
	//ClientListMap
    Map <String, TeleClientCallbackInterface> clientMap;
    Map <String, Color> colorMap;
    Map <String, Point> pointMap;
    TeleServerView view;
    TeleServerModel model;
    DateFormat dateFormat;
    Point currPoint;
    Dimension currDim;
    
    //Constants
  	public int MOVE_POINTER = 1;
  	public int INIT_COLOR = 2;
  	
    public TeleServerImpl (String msg) throws RemoteException {  
    	model = new TeleServerModel(this);
    	view = new TeleServerView();
    	currPoint = new Point(25, 90);    	
    	currDim = new Dimension(450, 470);    	
    }
	
	public void handleEvent(String cName, Point p, Dimension d, Color c, int STATUS_CODE) throws RemoteException {
		currPoint = p;
		currDim = d;
		sendUpdateToAllClients(cName, p, d, c, STATUS_CODE);		
	}
	
	public Point getCurrPoint() throws RemoteException{
		return currPoint;
	}
	
	public Dimension getCurrDim() throws RemoteException{
		return currDim;
	}
	
	public Map <String, Color> getColorList() throws RemoteException{
		return colorMap;
	}
	
	public Map <String, Point> getPointList() throws RemoteException{
		return pointMap;
	}
	
	public void sendUpdateToAllClients(String cName, Point p, Dimension d, Color c, int STATUS_CODE){	    	
    	for(Map.Entry<String, TeleClientCallbackInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		TeleClientCallbackInterface tcCallback = item.getValue();
    		if(!clientName.equals(cName)){
    			try {
					tcCallback.handleNotify(p, d, c, STATUS_CODE);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}	    
	}
	
	public void registerCallback(String cName, Color c, TeleClientCallbackInterface tCallback){
		if(!clientMap.containsKey(cName)){
			clientMap.put(cName, tCallback);
			String msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> has joined the telepointer session </br>";			
			view.appendArchive(msg);
		}
		if(!colorMap.containsKey(cName)){
			colorMap.put(cName, c);
		}
		if(!pointMap.containsKey(cName)){
			pointMap.put(cName, new Point(25, 90));
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
		if(colorMap.containsKey(cName)){
			colorMap.remove(cName);
		}
		if(pointMap.containsKey(cName)){
			pointMap.remove(cName);
		}
	}
	
    public class TeleServerModel{
    	public TeleServerModel(TeleServerImpl ts){
    		ts.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    		ts.clientMap = new HashMap<String, TeleClientCallbackInterface>();
    		ts.colorMap = new HashMap<String, Color>();
    		ts.pointMap = new HashMap<String, Point>();
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
	        archiveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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