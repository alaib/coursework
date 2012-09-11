import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.collections.buffer.CircularFifoBuffer;


public class ChatServerImpl extends UnicastRemoteObject implements ChatServerInterface {    
	private static final long serialVersionUID = -6313448627522080760L;
	String message;
	JFrame frame;		
	JTextField statusText;
	JTextField topicText;
	Container container;
	JEditorPane archivePane;
	JEditorPane userListPane;
	JScrollPane archiveScrollPane;
	JScrollPane userListScrollPane;
	JPanel lPanel;
	JPanel uPanel;
	JLabel topicLabel;
	
	DateFormat dateFormat;
    Date date;	   
    
    //Constants
    private int EVENT_NEW_MSG = 1;
    private int EVENT_NEW_STATUS = 2;
    private int EVENT_KEY_PRESS = 3;
    private int EVENT_KEY_TYPED = 4;
    private int EVENT_CLIENT_JOIN = 5;
    private int EVENT_CLIENT_STATUS_CHANGE = 6;
    private int EVENT_CLIENT_EXIT = 7;
    private int EVENT_CHANGE_TOPIC = 8;
    private int EVENT_CHANGE_STATUS_MSG = 9;
    
    //Timer
    Timer clearStatusTimer;
    Timer typedTimer;
    
    //Buffer
    CircularFifoBuffer buffer;
    
    //ClientListMap
    Map <String, CallbackClientInterface> clientMap;
    //StringMap
    Map <String, String> dataLog;

  
    public ChatServerImpl (String msg) throws RemoteException {  
    	initModel(msg);
    	initView();
    	initController();
    }
    
    public void initModel(String msg){
    	clearStatusTimer = new Timer();
    	typedTimer = new Timer();
    	message = msg;
    	dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	buffer = new CircularFifoBuffer(5);   
    	clientMap = new HashMap<String, CallbackClientInterface>();
    	dataLog = new HashMap<String, String>();
    }
    
    public void initView(){
    	frame = new JFrame();
    	frame.setSize(400, 200);    	
    	
    	container = frame.getContentPane();
    	container.setLayout(new BorderLayout());
    	                
        archivePane = new JEditorPane();        
        archivePane.setEditable(false);
        archivePane.setContentType("text/html");
        archivePane.setPreferredSize(new Dimension(500, 400));
        
        userListPane = new JEditorPane();        
        userListPane.setEditable(false);
        userListPane.setContentType("text/html");
        userListPane.setPreferredSize(new Dimension(250, 400));
        userListPane.setText("<div id='endList'></div>");
                               
        statusText = new JTextField(32);
        statusText.setEditable(false);
        
        topicText = new JTextField(32);
        topicText.setEditable(false);
        topicText.setText("Spanish La Liga");        
        
        topicLabel = new JLabel("Topic:");        
        //topicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        uPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
		uPanel.add(topicLabel);
        uPanel.add(topicText);
        
        lPanel = new JPanel(new BorderLayout());        
        archiveScrollPane = new JScrollPane(archivePane);
        userListScrollPane = new JScrollPane(userListPane);
        lPanel.add(archiveScrollPane, BorderLayout.CENTER);
        lPanel.add(userListScrollPane, BorderLayout.EAST);
        lPanel.add(statusText, BorderLayout.SOUTH);        
        
        container.add(uPanel, BorderLayout.NORTH);
        container.add(lPanel, BorderLayout.SOUTH);
        
        frame.setTitle("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();      
        frame.setVisible(true);
        
        //Init Msg
        String s = "["+dateFormat.format(new Date())+"] Chat Server is online</br>";
        appendArchive(s);
        buffer.add((Object)s);        
    }
    
    public void initController(){    
    	//setEventListeners();
    }
    
    public void setEventListeners(){    
    	HyperlinkListener hListen = new HyperlinkListener(){
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				// TODO Auto-generated method stub
				if(e.getEventType() == HyperlinkEvent.EventType.ENTERED){					
					String type = (String) e.getSourceElement().getName();
					Element elem = null;
					if(type.equals("img")){
						elem = e.getSourceElement().getParentElement().getParentElement();						
					}else if(type.equals("content") || type.equals("a")){
						elem = e.getSourceElement().getParentElement().getParentElement();
					}
					System.out.println(elem);
					System.out.println(userListPane.getText());
					HTMLDocument d = (HTMLDocument) userListPane.getDocument();    		
		    		Element statImg = d.getElement(elem, HTML.Attribute.CLASS, "statImg");
		    		Element statLink = d.getElement(elem, HTML.Attribute.CLASS, "statLink");
		    		String imgSrc = (String) statImg.getAttributes().getAttribute(HTML.Attribute.SRC);
		    		String newStatus = (String) statLink.getAttributes().getAttribute(HTML.Attribute.TITLE);
		    		String clientName = (String) elem.getAttributes().getAttribute(HTML.Attribute.ID);																						
					System.out.println(clientName);
					System.out.println(imgSrc);
					System.out.println(newStatus);															
				}else if(e.getEventType() == HyperlinkEvent.EventType.EXITED){										
				}
			}    		
    	};
    	userListPane.addHyperlinkListener(hListen);
    }      
                
	public void registerCallback(String cName, CallbackClientInterface cbClient) throws RemoteException {
    	if(!clientMap.containsKey(cName)){
    		clientMap.put(cName, cbClient);    		
    	} 		
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
    
    public void modUserList(String cName, String cStatus, String newStatus, String action){
    	//Current hack
    	if(action.equals("exit")){
    		HTMLDocument d = (HTMLDocument) userListPane.getDocument();    		
    		Element e = d.getElement(d.getDefaultRootElement(), HTML.Attribute.ID, cName);  
    		d.removeElement(e);    		
    	}else if(action.equals("add")){
	    	String imgPath = "images/"+cStatus.toLowerCase()+".png";
	    	String imgsrc = "";
			try {
				imgsrc = new File(imgPath).toURL().toExternalForm();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	    	String html = "<div id='" + cName + "'><img class='statImg' border=0 width=14 height=14 src='"+imgsrc+"' />&nbsp;"+cName+"</div>";    		    		
    		HTMLDocument d = (HTMLDocument) userListPane.getDocument();    		
    		Element e = d.getElement(d.getDefaultRootElement(), HTML.Attribute.ID, "endList");
    		try {
				d.insertBeforeEnd(e, html);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}    			    
    	}else if(action.equals("modify")){
    		String imgPath = "images/"+cStatus.toLowerCase()+".png";
	    	String imgsrc = "";
			try {
				imgsrc = new File(imgPath).toURL().toExternalForm();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String html = "";			
			if(newStatus.equals("")){
				html = "<img style='border: none';  width=14 height=14 src='"+imgsrc+"' />&nbsp;"+cName;
			}else{	    	   
				html = "<img style='border: none';  width=14 height=14 src='"+imgsrc+"' />&nbsp;"+cName+"<span style='color:#778899'>-"+newStatus+"</span>";
			}
	    	
    		HTMLDocument d = (HTMLDocument) userListPane.getDocument();        		    		    		    		    
    		Element e = d.getElement(d.getDefaultRootElement(), HTML.Attribute.ID, cName);
    		try {
				d.setInnerHTML(e, html);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}	
    }    
    
    public String[] handleEvent(String clientName, String clientStatus, String newMsg, int EVENT_CODE) throws RemoteException {		 
    	if(!newMsg.isEmpty()){
    		message = newMsg;
    	}
    	String msg = "";
    	String history = "";
    	String userList = "";
    	if(EVENT_CODE == EVENT_NEW_MSG){
    		msg = clientNewMessage(clientName, newMsg, EVENT_CODE);	        
    	}else if(EVENT_CODE == EVENT_KEY_PRESS){    		    		  
    		msg = clientKeyPress(clientName, EVENT_CODE);
    	}else if(EVENT_CODE == EVENT_CLIENT_JOIN){
    		history = clientJoin(clientName, clientStatus, EVENT_CODE);
    		msg = topicText.getText();
    		userList = userListPane.getText();
    		System.out.println(userList);
    	}else if(EVENT_CODE == EVENT_CLIENT_STATUS_CHANGE){
    		changeClientStatus(clientName, clientStatus, newMsg, EVENT_CODE);    		    		
    	}else if(EVENT_CODE == EVENT_CLIENT_EXIT){
    		clientExit(clientName, clientStatus, EVENT_CODE);
    	}else if(EVENT_CODE == EVENT_CHANGE_TOPIC){
    		msg = clientChangeTopic(clientName, message, EVENT_CODE);    		
    	}else if(EVENT_CODE == EVENT_CHANGE_STATUS_MSG){
    		changeClientStatusMsg(clientName, clientStatus, newMsg, EVENT_CODE);
    	}
    	String[] data = new String[3];
    	data[0] = msg;
    	data[1] = history;
    	data[2] = userList;
        return data;
    }
    
    public String clientNewMessage(String clientName, String newMsg, int EVENT_CODE){
    	String msg = "["+dateFormat.format(new Date())+"] <strong>"+ clientName + "</strong> : " + newMsg +"</br>";
        appendArchive(msg);	        
        buffer.add((Object)msg);
        
        Map <String, String> data = new HashMap<String, String>();        
        data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("clientName", clientName);
        sendUpdateToAllClients(data);
        clearStatus();
        return msg;
    }
    
    public void clearStatus(){
    	statusText.setText("");
    }
    
    public void setTypedStatus(String cName){    	
		String msg = "["+dateFormat.format(new Date())+"] "+ cName + " has typed";
		statusText.setText(msg);
		clearStatusTimer.schedule(new TimerTask() {
			public void run() {
				clearStatus();
			}
		}, 1000); 
    }
    
    public String clientJoin(String cName, String cStatus, int EVENT_CODE){
    	String msg = " " + cName + " - " + cStatus + "</br>";    	
    	modUserList(cName, cStatus, "", "add");
    	msg = "["+dateFormat.format(new Date())+"] <strong>"+ cName + "</strong> joined the chat session</br>";
    	appendArchive(msg);    	
    	String history = "";    	
    	Iterator it = buffer.iterator();
    	while(it.hasNext()){
    		Object elem = it.next();
    		history = history + (String)elem;
    	}
    	buffer.add((Object)msg);
    	
    	Map <String, String> data = new HashMap<String, String>();
    	data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("clientName", cName);
        data.put("clientStatus", cStatus);
        sendUpdateToAllClients(data);            	
    	return history;
    }
    
    public String clientKeyPress(String clientName, int EVENT_CODE){
    	String msg = "["+dateFormat.format(new Date())+"] "+ clientName + " is typing ...";
		statusText.setText(msg);
		//Remove existing timers
		typedTimer.purge();    		
		clearStatusTimer.purge();
		
		//Set to typed after 1 second
		final String backupClientName = clientName;
		typedTimer.schedule(new TimerTask() {
			public void run() {
				setTypedStatus(backupClientName);
			}
		}, 1000);
		Map <String, String> data = new HashMap<String, String>();
    	data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("clientName", clientName);        
        sendUpdateToAllClients(data); 
		return msg;
    }
    
    public String clientChangeTopic(String clientName, String newTopic, int EVENT_CODE){
    	topicText.setText(message);
		String msg = "["+dateFormat.format(new Date())+"] <strong>"+ clientName + "</strong> changed the topic to <em>" + newTopic +"</em></br>";    		
		appendArchive(msg);
		buffer.add((Object)msg);
		
		Map <String, String> data = new HashMap<String, String>();
    	data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("clientName", clientName);
        data.put("newTopic", newTopic);
        
        sendUpdateToAllClients(data);		
		return msg;
    }
    
    public void changeClientStatusMsg(String clientName, String clientStatus, String newStatus, int EVENT_CODE){
    	modUserList(clientName, clientStatus, newStatus, "modify");
    	String msg = "["+dateFormat.format(new Date())+"] <strong>" + clientName + "</strong> changed his status message to <em>" + newStatus + "</em></br>";
		Map <String, String> data = new HashMap<String, String>();
    	data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("clientName", clientName);
        data.put("clientStatus", clientStatus);
        data.put("newStatus", newStatus);
        appendArchive(msg);
        sendUpdateToAllClients(data);            	
    }
    
    public void sendUpdateToAllClients(Map <String, String> data){
    	String cName = data.get("clientName");
    	for(Map.Entry<String, CallbackClientInterface> item : clientMap.entrySet()){
    		String clientName = item.getKey();
    		CallbackClientInterface cb = item.getValue();
    		if(!clientName.equals(cName)){
    			try {
					cb.handleNotify(data);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
    
    public void clientExit(String cName, String cStatus, int EVENT_CODE){
    	String msg = "";    	
    	msg = "["+dateFormat.format(new Date())+"] <strong>"+ cName + "</strong> left the chat session</br>";    	
    	appendArchive(msg);
    	modUserList(cName, "", "", "exit");
    	buffer.add((Object)msg);
    	//Unregister the client
    	clientMap.remove(cName);
    	
    	Map <String, String> data = new HashMap<String, String>();
    	data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("clientName", cName);
        data.put("clientStatus", cStatus);        
        sendUpdateToAllClients(data);		    	    	
    }
    
    public void changeClientStatus(String cName, String cStatus, String newMsg, int EVENT_CODE){
    	modUserList(cName, cStatus, newMsg, "modify");
    	String msg = "";
    	if(!cStatus.equalsIgnoreCase("invisible")){
    		msg = "["+dateFormat.format(new Date())+"] <strong>" + cName + "</strong> is " + cStatus + "</br>";
    		appendArchive(msg);
    	}    	
		Map <String, String> data = new HashMap<String, String>();
    	data.put("event_code", Integer.toString(EVENT_CODE));
        data.put("msg", msg);
        data.put("newStatus", newMsg);
        data.put("clientName", cName);
        data.put("clientStatus", cStatus);        
        sendUpdateToAllClients(data);            	
    }	
}