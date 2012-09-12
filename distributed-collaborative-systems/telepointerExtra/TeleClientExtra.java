import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class TeleClientExtra{
	TeleClientModel cModel;
	TeleClientView cView;
	TeleClientController cController;
	TeleServerInterface teleInt;
	TeleClientCallbackImpl teleClientCallback;
	String clientName;	
	Map <String, Color> colorMap;
    Map <String, Point> pointMap;
    Color[] colorList = new Color[]{Color.RED, Color.CYAN, Color.YELLOW, Color.BLUE};
    Color clientColor;
	
	//Constants
	public int MOVE_POINTER = 1;
	public int INIT_COLOR = 2;
	
	public TeleClientExtra(){				
		cModel = new TeleClientModel(this);
		cView = new TeleClientView(this);
		cController = new TeleClientController();
	}
	
	public void handleCallback(Point p, Dimension d, int STATUS_CODE){
		if(STATUS_CODE == MOVE_POINTER){			
			this.cView.gPane.setPoint(this.cView.gPane.listener.prevPoint);	            
			this.cView.gPane.repaint();								
		}		
	}
	
	public class TeleClientModel{
		TeleClientExtra tc;
		public TeleClientModel(TeleClientExtra tClient){
			tc = tClient;
			try {
				tc.teleInt = (TeleServerInterface) Naming.lookup ("rmi://localhost/Telepointer");
				tc.teleClientCallback = new TeleClientCallbackImpl(tc);						    			    			    	
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	  			
		}
		
		public void setClientColor(){
			try{
				tc.colorMap = tc.teleInt.getColorList();
			}catch(Exception e){
				e.printStackTrace();				
			}
			for(int i = 0; i < tc.colorList.length; i++){
				Color c = tc.colorList[i];				
				if(!tc.colorMap.containsValue(c)){
					tc.clientColor = c;
					break;
				}
			}			
		}		
	}
	
	public class TeleClientView{
		public MyGlassPane gPane;
		public JFrame frame;
		public JMenuBar menuBar;
		public Container contentPane;
		public JCheckBox toggleBtn;
		public JEditorPane editorPane;		
		public JFrame loginFrame;
		public TeleClientExtra tc;
		
		public TeleClientView(TeleClientExtra tClient){
			tc = tClient;
			initLoginScreen();
		}								
	
		public void initLoginScreen(){
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
	        			setupGlassPane();
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
	        			setupGlassPane();
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
	        loginFrame.setTitle("Login to Telepointer");
	        loginFrame.setPreferredSize(new Dimension(250, 120));
	        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        loginFrame.pack();
	        userLogin.requestFocusInWindow();
	              	       
	        loginFrame.pack();
	        loginFrame.setVisible(true);
		}
		
		public void setupGlassPane(){
			//Setup JFrame
			frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			
			//Setup Container
			contentPane = frame.getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.setPreferredSize(new Dimension(450, 470));
			
			//Add toggle widget
	        toggleBtn = new JCheckBox("Activate Telepointer");
	        toggleBtn.setSelected(false);
	        toggleBtn.setPreferredSize(new Dimension(20, 50));
	        
	        //Add button
	        editorPane = new JEditorPane();
	        try{
	        	editorPane.setContentType("text/html");
	        	String imgPath = "images/linux-architecture.jpg";
	        	String imgsrc = new File(imgPath).toURL().toExternalForm();
	        	
	        	String html = "<html><body>" +
	        				  "<div style='margin: 10px 0px 0px 35px;'><img  width=337 height=382 src='"+imgsrc+"'/></div>"+
	        				  "</body></html>";	        		        
	        	editorPane.setText(html);
	        	editorPane.setEditable(false);
	        }catch(Exception e){
	        	System.out.println(e);
	        }
	        //editorPane.setPreferredSize(new Dimension(400, 300));
			
			//Add to Container
	        contentPane.add(toggleBtn, BorderLayout.NORTH);
	        contentPane.add(editorPane, BorderLayout.CENTER);
	        												
			//Setup GlassPane which appears over the menu bar and content pane
			gPane = new MyGlassPane(tc, toggleBtn, editorPane, contentPane);
			toggleBtn.addItemListener(gPane);
			frame.setGlassPane(gPane);
			frame.setTitle(clientName);
			
			//Show the window
			frame.pack();
			frame.setVisible(true);
		}			
	}	
		
	/* Provide the GlassPane */
	public class MyGlassPane extends JComponent implements ItemListener{
		Point point;
		TeleClientExtra tc;
		public PaneListener listener;
		
		public MyGlassPane(TeleClientExtra tClient, AbstractButton toggleBtn, JEditorPane editorPane, Container contentPane){
			tc = tClient;
			listener = new PaneListener(tc, toggleBtn, editorPane, this, contentPane);
			addMouseListener(listener);
			addMouseMotionListener(listener);
			//setVisible(true);
		}
		
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED){				
				setVisible(true);
				try {															
					tc.cModel.setClientColor();
					tc.teleInt.registerCallback(tc.clientName, tc.clientColor, tc.teleClientCallback);					
					tc.cView.gPane.setPoint(new Point(25, 90));
					tc.cView.gPane.repaint();					
				} catch (RemoteException e1) { 
					e1.printStackTrace();
				}
			}else{				
				setVisible(false);
				try {
					tc.teleInt.unRegisterCallback(tc.clientName);
					tc.cView.gPane.listener.prevPoint = new Point(25, 90);
				} catch (RemoteException e1) { 
					e1.printStackTrace();
				}
			}	        	    
	    }	 
		
		public void paintComponent(Graphics g){
			drawAllOtherPointers(g);
			if(point != null){
				//System.out.println("Called draw same");
				g.setColor(tc.clientColor);
				g.fillOval(point.x - 10, point.y - 10, 20, 20);
				g.setColor(Color.BLACK);
				g.drawString(tc.clientName, point.x-10, point.y-10+35);
			}
			
		}
		
		public void drawAllOtherPointers(Graphics g){
			//System.out.println("Called draw other");
			try{
				tc.colorMap = tc.teleInt.getColorList();
				tc.pointMap = tc.teleInt.getPointList();
				//System.out.println(tc.pointMap);
				for(Map.Entry<String, Point> item : tc.pointMap.entrySet()){
		    		String cName = item.getKey();
		    		Point p = item.getValue();
		    		Color c = tc.colorMap.get(cName);
		    		//System.out.println("color = "+c+", point = "+p+",cName = "+cName);
		    		if(!cName.equals(tc.clientName)){
		    			g.setColor(c);
		    			g.fillOval(p.x - 10, p.y - 10, 20, 20);
		    			g.setColor(Color.BLACK);
		    			g.drawString(cName, p.x-10, p.y-10+35);
		    		}
		    	}	    
			}catch(Exception e){
				e.printStackTrace();				
			}
		}
		
		public void setPoint(Point p){
			point = p;
		}
	}
	
	/* Mouse Event Handling */
	public class PaneListener extends MouseInputAdapter{
		Toolkit toolkit;
		MyGlassPane glassPane;
		Container contentPane;		
		Component toggleBtn;
		JEditorPane editorPane;
		Point prevPoint;
		TeleClientExtra tc;
		
		public PaneListener(TeleClientExtra tClient, Component tBtn, JEditorPane ePane, MyGlassPane gPane, Container cPane){
			toolkit = Toolkit.getDefaultToolkit();
			this.tc = tClient;
			this.prevPoint = new Point(25, 90);
			this.toggleBtn = tBtn;
			this.glassPane = gPane;
			this.contentPane = cPane;	
			this.editorPane = ePane;
		}
		
		public void mouseMoved(MouseEvent e) {
	        redispatchMouseEvent(e, false);
	    }
	 
	    public void mouseDragged(MouseEvent e) {
	        redispatchMouseEvent(e, true);
	    }
	 
	    public void mouseClicked(MouseEvent e) {	    	
	        redispatchMouseEvent(e, true);
	    }
	 
	    public void mouseEntered(MouseEvent e) {
	        redispatchMouseEvent(e, false);
	    }
	 
	    public void mouseExited(MouseEvent e) {
	        redispatchMouseEvent(e, false);
	    }
	 
	    public void mousePressed(MouseEvent e) {
	        redispatchMouseEvent(e, false);
	    }
	 
	    public void mouseReleased(MouseEvent e) {
	        redispatchMouseEvent(e, false);
	    }
	 
	    //Redispatch Event
	    private void redispatchMouseEvent(MouseEvent e, boolean repaint) {	    	
	        Point glassPanePoint = e.getPoint();	        
	        Container container = contentPane;
	        Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
	        if (containerPoint.y < 0) { 	        	
            	if(repaint == true){
            		repaint = false;
            	}	            		            
	        } else {
	            //The mouse event is over the content pane.
	            Component component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
	            //If it is over the check box, redispatch the event instead of handling it via repaint
	            if ((component != null) && (component.equals(toggleBtn))) {
	                //Forward events over the check box.
	                Point componentPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
	                component.dispatchEvent(new MouseEvent(	component,
	                										e.getID(),
	                										e.getWhen(),
	                										e.getModifiers(),
	                										componentPoint.x,
	                										componentPoint.y,
	                										e.getClickCount(),
	                										e.isPopupTrigger()
	                									   ));	                
	            }	            
	        }
	         
	        //Update the glass pane if requested.
	        if (repaint) {
	        	//Check if the dragged area is inside our circle
	        	Point center = new Point(prevPoint.x - 10, prevPoint.y - 10);
	        	int radiusSquare = (int)Math.pow(30, 2);
	        	int distance = (int)Math.pow((center.x - glassPanePoint.x), 2) + (int)Math.pow((center.y - glassPanePoint.y), 2);
	        	//String msg = "center = "+center+", prevPoint = "+prevPoint+",glassPanePoint = "+glassPanePoint + ",radius^2 = "+radiusSquare+", distance = "+distance;
	        	//System.out.println(msg);
	        	if(distance <= radiusSquare){
		            glassPane.setPoint(glassPanePoint);
		            glassPane.repaint();
		            Dimension d = new Dimension(tc.cView.frame.getSize());
		            try {
						tc.teleInt.handleEvent(tc.clientName, glassPanePoint, d, tc.clientColor, tc.MOVE_POINTER);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		            prevPoint = glassPanePoint;
	        	}	        	
	        }	        
	    }		
	}
	
	public class TeleClientController{
		public TeleClientController(){
		}
		
	}
	
	public static void main(String[] args){
		final TeleClientExtra tc = new TeleClientExtra();
		
		// Do something on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		tc.teleInt.unRegisterCallback(tc.clientName);
            	}catch(Exception e){
            		System.out.println("chatClient exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
	}
}
