//Layout
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
//AWT
//SWING



public class ChatClient{	  
    // GUI stuff    
    private JEditorPane archivePane;
    private JScrollPane archiveScrollPane;
    private JEditorPane userListPane;
    private JScrollPane userListScrollPane;
    private JTextField typedText;
    private JTextPane statusText;
    private JTextField topicText;
    private JFrame frame;
    private Container container;
    private ChatServerInterface chWindow;
    public JComboBox statusList;
    private JPanel uPanel;
    private JPanel lPanel;
    private JPanel lPanel2;
    private JLabel topicLabel;
    private JTextField typingStatus;
    
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
    
    //Strings
    String prevTopic;
    
    //Client
    String clientName;
    String clientStatus;
    
    //Data
    String [] data;
    
    //KeyCount
    int keyCount = 0;
    
    //CallbackClient
    CallbackClientInterface cbClient;
    
    //Boolean
    boolean setFocusFirst;
    
    //Timers
    DateFormat dateFormat;
    Timer clearStatusTimer;
    Timer typedTimer;
    
    public ChatClient() {
    	preInit();    	          
    }
    
    public void preInit(){
    	final JFrame loginFrame = new JFrame();
        Container loginContainer = loginFrame.getContentPane();
        
        //Upper Panel
        JLabel loginPrompt = new JLabel("Enter your UserName: ");
        final JTextField userLogin = new JTextField(16);
        JButton connectBtn = new JButton("Connect");
        
        connectBtn.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		try{
        			clientName = userLogin.getText();
        			loginFrame.dispose();
        			init();
        		}catch(Exception e1){
        			System.out.println(e1);
        		}
        	}
        });
        
        userLogin.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		try{
        			clientName = userLogin.getText();
        			loginFrame.dispose();
        			init();
        		}catch(Exception e1){
        			System.out.println(e1);
        		}
        	}        	
        });
        
        JPanel loginPanel= new JPanel(new FlowLayout());
        loginPanel.add(loginPrompt);
        loginPanel.add(userLogin);
        loginPanel.add(connectBtn);
        
        //Add both panels to container
        loginContainer.add(loginPanel, BorderLayout.CENTER);                          

        // display the window, with focus on typing box
        loginFrame.setTitle("Login to Chat");
        loginFrame.setPreferredSize(new Dimension(250, 120));
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.pack();
        userLogin.requestFocusInWindow();
              
        /*
        //Get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         
        // Determine the new location of the window
        int w = screenSize.width;
        int h = screenSize.height;
        loginFrame.setLocation(w/2-100, h/2);
        loginFrame.setLocationRelativeTo(null);
        */
         
        loginFrame.setVisible(true);
        
    }
    
    public void init(){
    	initModel();
    	initView();
    	initController();
    }
    
    public void initModel(){
    	//Create client    	
    	clientStatus = "Available";    	
                
        //Strings
        prevTopic = "";    
        
        try {
			cbClient = new CallbackClientImpl(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
        setFocusFirst = false;
        clearStatusTimer = new Timer();
    	typedTimer = new Timer();    	
    	dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
    
    public void initView(){
    	// create GUI stuff   	     	
    	typedText = new JTextField(32);   
    	
    	typingStatus = new JTextField(32);
    	typingStatus.setBackground(Color.LIGHT_GRAY);
    	typingStatus.setEditable(false);
    	
    	statusText = new JTextPane();
    	statusText.setContentType("text/html");
    	String statusHTML = "<div style='color:gray'>Set your status...</div>";
    	statusText.setText(statusHTML);
    	statusText.setPreferredSize(new Dimension(250, 20));
    	statusText.setEditable(true);
    	
    	topicText = new JTextField(20);
    	
    	archivePane = new JEditorPane();        
        archivePane.setEditable(false);
        archivePane.setContentType("text/html");
        archivePane.setPreferredSize(new Dimension(300, 300));          
        
        userListPane = new JEditorPane();        
        userListPane.setEditable(false);
        userListPane.setContentType("text/html");
        userListPane.setPreferredSize(new Dimension(150, 300));
            	        
        topicLabel = new JLabel("Topic:");
                
        Vector model = new Vector();
        model.addElement(new Item(new ImageIcon("images/available.png"), "Available"));
        model.addElement(new Item(new ImageIcon("images/busy.png"), "Busy"));
        model.addElement(new Item(new ImageIcon("images/invisible.png"), "Invisible"));
        model.addElement(new Item(new ImageIcon("images/idle.png"), "Idle"));
        
        statusList = new JComboBox(model);
        statusList.setRenderer(new ItemRenderer());               
        statusList.setSelectedIndex(0);
        statusList.setEditable(false);
                
        frame = new JFrame();
        container = frame.getContentPane();
        
        //Upper Panel                
        uPanel = new JPanel();
        uPanel.add(statusList);        
        uPanel.add(statusText);
        uPanel.add(topicLabel);
        uPanel.add(topicText);
                
        //Lower Panel
        lPanel = new JPanel(new BorderLayout());
        archiveScrollPane = new JScrollPane(archivePane);
        userListScrollPane = new JScrollPane(userListPane);        
        lPanel.add(archiveScrollPane, BorderLayout.CENTER);
        lPanel.add(userListScrollPane, BorderLayout.EAST);
        lPanel.add(typedText, BorderLayout.SOUTH);
        
        lPanel2 = new JPanel(new BorderLayout());
        lPanel2.add(typingStatus, BorderLayout.CENTER);
        
        //Add both panels to container
        container.add(uPanel, BorderLayout.NORTH);
        container.add(lPanel, BorderLayout.CENTER);
        container.add(lPanel2, BorderLayout.SOUTH);

        // display the window, with focus on typing box
        frame.setTitle("Chat Client - "+clientName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        typedText.requestFocusInWindow();
        frame.setVisible(true);
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
    
    public void initController(){
    	//Event listeners
        setEventListeners();
        
      //Connect to Server        
        try {
	        chWindow =(ChatServerInterface) Naming.lookup ("rmi://localhost/Chat");
	        chWindow.registerCallback(clientName, cbClient);	        
	        data = chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_CLIENT_JOIN);	        
	        String msg = data[0];
	        String history = data[1];
	        String userList = data[2];
	        prevTopic = msg;
	        topicText.setText(msg);
	        archivePane.setText("==== Begin Chat Session History ===");	        
	        String[] histBuffer = history.split("</br>");
	        for(int i = 0; i < histBuffer.length; i++){
	        	appendArchive(histBuffer[i]);
	        }	 
	        appendArchive("=== End Chat Session History ===</br></br>");
	        userListPane.setText(userList);
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
            			if(keyCount != 1 && keyCount%3 != 0){
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
    	    	if(typedText.equals("")){
    	    		return;
    	    	}
    	    	try {      	    		
    		        data = chWindow.handleEvent(clientName, clientStatus, typedText.getText(), EVENT_NEW_MSG);
    		        String msg = data[0];    		        
    		        msg = msg.replaceFirst(clientName+"</strong>", "me</strong>");     		            		        
    		        appendArchive(msg);
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
	   	    		Item item = (Item)cb.getSelectedItem();
	   	    		clientStatus = item.getText();
	   	    		String statusMsg = "";
	   	    		String text = statusText.getDocument().getText(0, statusText.getDocument().getLength());
	   	    		if(!text.contains("Set your status...")){
	   	    			statusMsg = text;
	   	    		}	   	    		
	   	    		data = chWindow.handleEvent(clientName, clientStatus, text, EVENT_CLIENT_STATUS_CHANGE);
	   	    		modUserList(clientName, clientStatus, "modify");
	   		    }catch (Exception e1){
	   		        System.out.println("chatClient exception: " + e1);
	   		    }    	            	       
   	        }
        });   
        
        topicText.addActionListener(new ActionListener(){
    	    public void actionPerformed(ActionEvent e){
    	    	try {      	
    	    		if(!topicText.getText().equals(prevTopic) || !topicText.getText().equals("")){
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
                      
        FocusListener fListen = new FocusListener(){			
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				String text = "";				
				try {
					text = statusText.getDocument().getText(0, statusText.getDocument().getLength());
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				if(text.contains("Set your status...")){
					statusText.setText("<div style='color:black;'></div>");
				}				
			}
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub			
			}        	
        };        	 
        statusText.addFocusListener(fListen);
        
        statusText.addKeyListener(new KeyAdapter(){ 
            public void keyPressed(KeyEvent e) {
            	if(e.getKeyCode() == KeyEvent.VK_ENTER){
            		typedText.requestFocusInWindow();
            		String text = "";
					try {
						text = statusText.getDocument().getText(0, statusText.getDocument().getLength());
					} catch (BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            		try {
						data = chWindow.handleEvent(clientName, clientStatus, text, EVENT_CHANGE_STATUS_MSG);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
             	}         	            	            	
             }
         });
    }       
    
    public void handleCallback(Map <String, String> data){    	
    	int EVENT_CODE = Integer.parseInt(data.get("event_code"), 10);    	
    	
    	if(EVENT_CODE == EVENT_NEW_MSG){
    		appendArchive(data.get("msg"));	        
    	}else if(EVENT_CODE == EVENT_KEY_PRESS){    		    		  
    		//Nothing to do
    		String cName = data.get("clientName");    		    		
    		typingStatus.setText(data.get("msg"));
    		//Remove existing timers
    		typedTimer.purge();    		
    		clearStatusTimer.purge();
    		
    		//Set to typed after 1 second
    		final String backupClientName = cName;
    		typedTimer.schedule(new TimerTask() {
    			public void run() {
    				setTypedStatus(backupClientName);
    			}
    		}, 1000);    		
    	}else if(EVENT_CODE == EVENT_CLIENT_JOIN){
    		appendArchive(data.get("msg"));
    		String cName = data.get("clientName");
    		String cStatus = data.get("clientStatus");
    		modUserList(cName, cStatus, "add");
    	}else if(EVENT_CODE == EVENT_CLIENT_STATUS_CHANGE){
    		//Change client availability status    		
    		String cName = data.get("clientName");
    		String cStatus = data.get("clientStatus");
    		modUserList(cName, cStatus, "modify");    		
    	}else if(EVENT_CODE == EVENT_CLIENT_EXIT){
    		appendArchive(data.get("msg"));
    		String cName = data.get("clientName");
    		modUserList(cName, "", "exit");
    	}else if(EVENT_CODE == EVENT_CHANGE_TOPIC){
    		appendArchive(data.get("msg"));  
    		String topic = data.get("newTopic");
    		topicText.setText(topic);
    		typedText.requestFocusInWindow();
    	}    	
    }
    
    public void setTypedStatus(String cName){    					
		String msg = "["+dateFormat.format(new Date())+"] "+ cName + " has typed";
		typingStatus.setText(msg);
		clearStatusTimer.schedule(new TimerTask() {
			public void run() {
				clearStatus();
			}
		}, 1000); 
    }
    
    public void clearStatus(){
    	typingStatus.setText("");
    }
    
    public void modUserList(String cName, String cStatus, String action){
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
	    	String html = "<div id='" + cName + "' <img width=14 height=14 src='"+imgsrc+"' />&nbsp;"+cName+"</div>";    		    		
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
	    	String html = "<img width=14 height=14 src='"+imgsrc+"' />&nbsp;"+cName;   
	    	
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
    
    class ItemRenderer extends BasicComboBoxRenderer{    
    public Component getListCellRendererComponent(
        JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus){    
        	super.getListCellRendererComponent(list, value, index,isSelected, cellHasFocus);

        	Item item = (Item)value;

        	if (index == -1){	        
	            setText( item.getText() );
	            setIcon( null );
	        }else{	        
	            setText( item.getText() );
	            setIcon( item.getIcon() );
	        }	
	        return this;
	    }
    }

    class Item{    
	    private Icon icon;
	    private String text;
	
	    public Item(Icon icon, String text){	    
	        this.icon = icon;
	        this.text = text;
	    }
	
	    public Icon getIcon(){	    
	        return icon;
	    }
	
	    public String getText(){	    
	        return text;
	    }
    }
    
    public static void main (String[] argv){
    	final ChatClient client = new ChatClient();
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
