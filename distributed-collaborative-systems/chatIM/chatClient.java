//Layout
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.Naming;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//AWT
//SWING



public class chatClient{	  
    // GUI stuff
    private JTextArea  enteredText;
    private JTextField typedText;
    private JTextField topicText;
    private JFrame frame;
    private Container container;
    private chatInterface chWindow;
    private JComboBox statusList;
    private JPanel uPanel;
    private JPanel lPanel;
    private JLabel topicLabel;
    
    //Constants    
    private int EVENT_NEW_MSG = 1;
    private int EVENT_NEW_STATUS = 2;
    private int EVENT_KEY_PRESS = 3;
    private int EVENT_KEY_TYPED = 4;
    private int EVENT_CLIENT_JOIN = 5;
    private int EVENT_CLIENT_STATUS_CHANGE = 6;
    private int EVENT_CLIENT_EXIT = 7;
    private int EVENT_CHANGE_TOPIC = 8;
    
    //Strings
    String prevTopic;
    
    //Client
    String clientName;
    String clientStatus;
    
    //Data
    String [] data;
    
    //KeyCount
    int keyCount = 0;
    

    public chatClient() {
    	initModel();
    	initView();
    	initController();          
    }
    
    public void initModel(){
    	//Create client
    	clientName = "John";
    	clientStatus = "Available";    	
                
        //Strings
        prevTopic = "";    	
    }
    
    public void initView(){
    	// create GUI stuff   	 
    	enteredText = new JTextArea(10, 32);
    	typedText = new JTextField(32);
    	topicText = new JTextField(32);
    	
        enteredText.setEditable(true);
        enteredText.setBackground(Color.LIGHT_GRAY);
        
        topicLabel = new JLabel("Topic:");  
        topicLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        String[] statusStrings = {"Available", "Busy", "Invisible", "Idle"};
        statusList = new JComboBox(statusStrings);
        statusList.setSelectedIndex(0);
        statusList.setEditable(false);
                
        frame = new JFrame();
        container = frame.getContentPane();
        
        //Upper Panel
        uPanel = new JPanel(new FlowLayout());
        uPanel.add(statusList);
        uPanel.add(topicLabel);
        uPanel.add(topicText);
                
        //Lower Panel
        lPanel = new JPanel(new BorderLayout());
        lPanel.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        lPanel.add(typedText, BorderLayout.SOUTH);
        
        //Add both panels to container
        container.add(uPanel, BorderLayout.NORTH);
        container.add(lPanel, BorderLayout.SOUTH);                      

        // display the window, with focus on typing box
        frame.setTitle("Chat Client - "+clientName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        typedText.requestFocusInWindow();
        frame.setVisible(true);
    }
    
    public void initController(){
    	//Event listeners
        setEventListeners();
        
      //Connect to Server        
        try {
	        chWindow =(chatInterface) Naming.lookup ("rmi://localhost/Chat");
	        data = chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_CLIENT_JOIN);	        
	        String msg = data[0];
	        String history = data[1];
	        prevTopic = msg;
	        topicText.setText(msg);
	        enteredText.append("====Chat Session History===\n"+history);
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
            	keyCount++;
            	if(e.getKeyCode() != KeyEvent.VK_ENTER){
            		try {	
            			if(keyCount != 1 && keyCount%5 != 0){
            				return;
            			}
        		        data = chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_KEY_PRESS);        		        
        		    }catch (Exception e1){
        		        System.out.println("chatClient exception: " + e1);
        		    }            		
            	}            	
            }
        });
    	
       typedText.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	    	try {      	    		
    		        data = chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_NEW_MSG);
    		        String msg = data[0];
    		        msg.replace(clientName+" : ", "me : ");
    		        enteredText.append(msg);
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
	   	    		data = chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_CLIENT_STATUS_CHANGE);
	   		    }catch (Exception e1){
	   		        System.out.println("chatClient exception: " + e1);
	   		    }    	            	       
   	        }
        });   
        
        topicText.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	    	try {      	
    	    		if(!topicText.getText().equals(prevTopic)){
    	    			chWindow.handleEvent(clientName, clientStatus, topicText.getText(), EVENT_CHANGE_TOPIC);
    	    			String msg = data[0];
    	    			prevTopic = topicText.getText();
    	    		}    		        
    		    }catch (Exception e1){
    		        System.out.println("chatClient exception: " + e1);
    		    }    	        
    	        typedText.requestFocusInWindow();    	        
    	    }
    	});
    }       
    
    public static void main (String[] argv){
    	final chatClient client = new chatClient();
    	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		client.data = client.chWindow.handleEvent(client.clientName, client.clientStatus, "", client.EVENT_CLIENT_EXIT);
            	}catch(Exception e){
            		System.out.println("chatClient exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
    }
	    
}
