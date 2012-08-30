import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

public class chatImpl extends UnicastRemoteObject implements chatInterface {    
	private static final long serialVersionUID = -6313448627522080760L;
	String message;
	JFrame frame;
	JTextArea enteredText;
	JTextArea userList;
	JTextField statusText;
	JTextField topicText;
	Container container;
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
    
    //Timer
    Timer clearStatusTimer;
    Timer typedTimer;
    
    //Buffer
    CircularFifoBuffer buffer;

  
    public chatImpl (String msg) throws RemoteException {  
    	initModel(msg);
    	initView();
    	initController();
    }
    
    public void initModel(String msg){
    	clearStatusTimer = new Timer();
    	typedTimer = new Timer();
    	message = msg;
    	dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	buffer = new CircularFifoBuffer(10);    
    }
    
    public void initView(){
    	frame = new JFrame();
    	frame.setSize(400, 200);    	
    	
    	container = frame.getContentPane();
    	container.setLayout(new BorderLayout());
    	
        enteredText = new JTextArea(30, 50);  
        enteredText.setEditable(false);
        enteredText.setBackground(Color.LIGHT_GRAY);
                        
        userList = new JTextArea(30, 20);
        userList.setEditable(false);
        userList.setBackground(Color.LIGHT_GRAY);
        
        statusText = new JTextField(32);
        statusText.setEditable(false);
        
        topicText = new JTextField(32);
        topicText.setEditable(false);
        topicText.setText("Spanish La Liga");
        topicText.setBackground(Color.LIGHT_GRAY);
        
        topicLabel = new JLabel("Topic:");        
        topicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        uPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		uPanel.add(topicLabel);
        uPanel.add(topicText);
        
        lPanel = new JPanel(new BorderLayout());
        lPanel.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        lPanel.add(new JScrollPane(userList), BorderLayout.EAST);
        lPanel.add(statusText, BorderLayout.SOUTH);        
        
        container.add(uPanel, BorderLayout.NORTH);
        container.add(lPanel, BorderLayout.SOUTH);
        
        frame.setTitle("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();      
        frame.setVisible(true);
        
        //Init Msg
        String s = "["+dateFormat.format(new Date())+"] Chat Server is online\n";
        enteredText.append(s);                
        buffer.add((Object)s);
    }
    
    public void initController(){
        
    }
    public String[] handleEvent(String clientName, String clientStatus, String newMsg, int EVENT_CODE) throws RemoteException {		 
    	if(!newMsg.isEmpty()){
    		message = newMsg;
    	}
    	String msg = "";
    	String history = "";
    	if(EVENT_CODE == EVENT_NEW_MSG){
	        msg = "["+dateFormat.format(new Date())+"] "+ clientName + " : " + message +"\n";
	        enteredText.append(msg);
	        buffer.add((Object)msg);
	        clearStatus();
    	}else if(EVENT_CODE == EVENT_KEY_PRESS){    		
    		msg = "["+dateFormat.format(new Date())+"] "+ clientName + " is typing ...";
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
    		}, 1500);    		
    	}else if(EVENT_CODE == EVENT_CLIENT_JOIN){
    		history = clientJoin(clientName, clientStatus);
    		msg = topicText.getText();
    		buffer.add((Object)msg);
    	}else if(EVENT_CODE == EVENT_CLIENT_STATUS_CHANGE){
    		changeClientStatus(clientName, clientStatus);
    	}else if(EVENT_CODE == EVENT_CLIENT_EXIT){
    		clientExit(clientName, clientStatus);
    	}else if(EVENT_CODE == EVENT_CHANGE_TOPIC){
    		topicText.setText(message);
    		msg = "["+dateFormat.format(new Date())+"] "+ clientName + " changed topic to '" + message +"'\n";
    		enteredText.append(msg);
    		buffer.add((Object)msg);
    	}
    	String[] data = new String[2];
    	data[0] = msg;
    	data[1] = history;
        return data;
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
		}, 1500); 
    }
    
    public String clientJoin(String cName, String cStatus){
    	String msg = " " + cName + " - " + cStatus + "\n";
    	userList.append(msg);
    	msg = "["+dateFormat.format(new Date())+"] "+ cName + " joined the chat session\n";
    	enteredText.append(msg);
    	String history = "";    	
    	Iterator it = buffer.iterator();
    	while(it.hasNext()){
    		Object elem = it.next();
    		history = history + (String)elem;
    	}
    	buffer.add((Object)msg);
    	return history;
    }
    
    public void clientExit(String cName, String cStatus){
    	String msg = "";
    	userList.setText(msg);
    	msg = "["+dateFormat.format(new Date())+"] "+ cName + " left the chat session\n";
    	enteredText.append(msg);
    	buffer.add((Object)msg);
    }
    
    public void changeClientStatus(String cName, String cStatus){
    	String msg = " " + cName + " - " + cStatus + "\n";
    	userList.setText(msg);
    }
}