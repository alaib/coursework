import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class chatImpl extends UnicastRemoteObject implements chatInterface {    
	private static final long serialVersionUID = -6313448627522080760L;
	String message;
	JFrame frame;
	JTextArea enteredText;
	JTextArea userList;
	JTextField statusText;
	JTextField topicText;
	Container container;
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
    
    //Timer
    Timer timer;

  
    public chatImpl (String msg) throws RemoteException {  
		frame = new JFrame("This is a test");
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
        topicText.setEditable(true);
        topicText.setText("Topic: Talk about La Liga!!!");
        
		timer = new Timer();
                
        container.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        container.add(new JScrollPane(userList), BorderLayout.EAST);
        container.add(statusText, BorderLayout.SOUTH);
        container.add(topicText, BorderLayout.NORTH);
        frame.setTitle("Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();      
        frame.setVisible(true);             
        
    	message = msg;
    	dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();
        
        String s = "["+dateFormat.format(date)+"] Chat Server is online\n";
        enteredText.append(s);
    }
    
    public String handleEvent(String clientName, String clientStatus, String newMsg, int EVENT_CODE) throws RemoteException {		 
    	if(!newMsg.isEmpty()){
    		message = newMsg;
    	}
    	String msg = "";
    	if(EVENT_CODE == EVENT_NEW_MSG){
	        msg = "["+dateFormat.format(date)+"] "+ clientName + " : " + message +"\n";
	        enteredText.append(msg);
	        clearStatus();
    	}else if(EVENT_CODE == EVENT_KEY_PRESS){    		
    		msg = "["+dateFormat.format(date)+"] "+ clientName + " is typing ...";
    		statusText.setText(msg);
    		//Clear status automatically after 1 second
    		timer.schedule(new TimerTask() {
    			public void run() {
    				clearStatus();
    			}
    		}, 1500);
    	}else if(EVENT_CODE == EVENT_CLIENT_JOIN){
    		clientJoin(clientName, clientStatus);
    	}else if(EVENT_CODE == EVENT_CLIENT_STATUS_CHANGE){
    		changeClientStatus(clientName, clientStatus);
    	}else if(EVENT_CODE == EVENT_CLIENT_EXIT){
    		clientExit(clientName, clientStatus);
    	}
        return msg;
    }
    
    public void clearStatus(){
    	statusText.setText("");
    }   
    
    public void clientJoin(String cName, String cStatus){
    	String msg = " " + cName + " - " + cStatus + "\n";
    	userList.append(msg);
    	msg = "["+dateFormat.format(date)+"] "+ cName + " joined the chat session\n";
    	enteredText.append(msg);
    }
    
    public void clientExit(String cName, String cStatus){
    	String msg = "";
    	userList.setText(msg);
    	msg = "["+dateFormat.format(date)+"] "+ cName + " left the chat session\n";
    	enteredText.append(msg);
    }
    
    public void changeClientStatus(String cName, String cStatus){
    	String msg = " " + cName + " - " + cStatus + "\n";
    	userList.setText(msg);
    }
}