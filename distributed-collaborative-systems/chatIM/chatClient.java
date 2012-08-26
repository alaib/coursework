//Layout
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.Naming;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//AWT
//SWING



public class chatClient{	  
    // GUI stuff
    private JTextArea  enteredText;
    private JTextField typedText;
    private JFrame frame;
    private Container container;
    private chatInterface chWindow;
    private JComboBox statusList;
    
    //Constants    
    private int EVENT_NEW_MSG = 1;
    private int EVENT_NEW_STATUS = 2;
    private int EVENT_KEY_PRESS = 3;
    private int EVENT_KEY_TYPED = 4;
    private int EVENT_CLIENT_JOIN = 5;
    private int EVENT_CLIENT_STATUS_CHANGE = 6;
    private int EVENT_CLIENT_EXIT = 7;
    
    //Client
    String clientName;
    String clientStatus;

    public chatClient() {
    	//Create client
    	clientName = "John";
    	clientStatus = "Available";
    	
        // create GUI stuff   	 
    	enteredText = new JTextArea(10, 32);
    	typedText = new JTextField(32);
        enteredText.setEditable(true);
        enteredText.setBackground(Color.LIGHT_GRAY);
        
        String[] statusStrings = {"Available", "Busy", "Invisible", "Idle"};
        statusList = new JComboBox(statusStrings);
        statusList.setSelectedIndex(0);
        statusList.setEditable(false);
        
        
        frame = new JFrame();
        container = frame.getContentPane();
        container.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        container.add(typedText, BorderLayout.SOUTH);
        container.add(statusList, BorderLayout.NORTH);

        // display the window, with focus on typing box
        frame.setTitle("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        typedText.requestFocusInWindow();
        frame.setVisible(true);
        
        setEventListeners();
        
        try {
	        chWindow =(chatInterface) Naming.lookup ("rmi://localhost/Chat");
	        chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_CLIENT_JOIN);	        
	    }catch (Exception e){
	        System.out.println ("chatClient exception: " + e);
	    }
    }
    
    public void setEventListeners(){       	   	
    	typedText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	//Do something
            }
 
            public void keyPressed(KeyEvent e) {            	
            	if(e.getKeyCode() != KeyEvent.VK_ENTER){
            		try {	        
        		        chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_KEY_PRESS);        		        
        		    }catch (Exception e1){
        		        System.out.println("chatClient exception: " + e1);
        		    }            		
            	}            	
            }
        });
    	
       typedText.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	    	try {      	    		
    		        chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_NEW_MSG);
    		    }catch (Exception e1){
    		        System.out.println("chatClient exception: " + e1);
    		    }
    	        typedText.setText("");
    	        typedText.requestFocusInWindow();    	        
    	    }
    	});
       
        statusList.addActionListener(new ActionListener(){
   	        public void actionPerformed(ActionEvent e){   	        	
   	    	    try {  
	   	    		JComboBox cb = (JComboBox)e.getSource();	   	    		
	   	    		Object item = cb.getSelectedItem();
	   	    		clientStatus = (String) item;
	   	    		chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_CLIENT_STATUS_CHANGE);
	   		    }catch (Exception e1){
	   		        System.out.println("chatClient exception: " + e1);
	   		    }    	            	       
   	        }
        });       
    }
    
    public void actionPerformed(ActionEvent e) {
          
    }    	   
       
    
    public static void main (String[] argv){
    	final chatClient client = new chatClient();
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		client.chWindow.handleEvent(client.clientName, client.clientStatus, "", client.EVENT_CLIENT_EXIT);
            	}catch(Exception e){
            		System.out.println("chatClient exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
    }
	    
}
