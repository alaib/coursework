import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;


public class teleClient {
	teleClientModel cModel;
	teleClientView cView;
	teleClientController cController;
	
	public teleClient(){
		cModel = new teleClientModel();
		cView = new teleClientView();
		cController = new teleClientController();
	}
	
	public class teleClientModel{
		public teleClientModel(){
			
		}
		
	}
	
	public class teleClientView{
		public MyGlassPane gPane;
		public JFrame frame;
		public JMenuBar menuBar;
		public Container contentPane;
		public JCheckBox toggleBtn;
		public JButton btn1;
		
		public teleClientView(){
			//Setup JFrame
			frame = new JFrame("GlassPane");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//Setup Container
			contentPane = frame.getContentPane();
			contentPane.setLayout(new FlowLayout());
			contentPane.setPreferredSize(new Dimension(200, 100));
			
			//Add toggle widget
	        toggleBtn = new JCheckBox("Activate Telepointer");
	        toggleBtn.setSelected(false);
	        
	        //Add button
	        btn1 = new JButton("test1");
			
			//Add to Container
	        contentPane.add(toggleBtn);
	        contentPane.add(btn1);
	        											
			//Setup Menubar
			menuBar = new JMenuBar();
			JMenu menu = new JMenu("Menu");
			menu.add(new JMenuItem("Help"));
			menuBar.add(menu);
			frame.setJMenuBar(menuBar);
			
			//Setup GlassPane which appears over the menu bar and content pane
			gPane = new MyGlassPane(toggleBtn, menuBar, contentPane);
			toggleBtn.addItemListener(gPane);
			frame.setGlassPane(gPane);
			
			//Show the window
			frame.pack();
			frame.setVisible(true);
		}					
	}
	
	/* Provide the GlassPane */
	public class MyGlassPane extends JComponent implements ItemListener{
		Point point;
		
		public MyGlassPane(AbstractButton toggleBtn, JMenuBar menuBar, Container contentPane){
			PaneListener listener = new PaneListener(toggleBtn, menuBar, this, contentPane);
			addMouseListener(listener);
			addMouseMotionListener(listener);
			//setVisible(true);
		}
		
		public void itemStateChanged(ItemEvent e) {	    	    
	        setVisible(e.getStateChange() == ItemEvent.SELECTED);	    	
	    }	 
		
		protected void paintComponent(Graphics g){
			if(point != null){
				g.setColor(Color.RED);
				g.fillOval(point.x - 10, point.y - 10, 20, 20);
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
		JMenuBar menuBar;
		Component toggleBtn;
		Point prevPoint;
		
		public PaneListener(Component tBtn, JMenuBar mBar, MyGlassPane gPane, Container cPane){
			toolkit = Toolkit.getDefaultToolkit();
			this.toggleBtn = tBtn;
			this.glassPane = gPane;
			this.contentPane = cPane;	
			this.menuBar = mBar;
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
	        	//we're not in the content pane
	            if (containerPoint.y + menuBar.getHeight() >= 0) { 
	                //The mouse event is over the menu bar.	                
	            } else { 
	                //The mouse event is over non-system window decorations such as the ones provided by the Java look and feel.	                
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
	            glassPane.setPoint(glassPanePoint);
	            glassPane.repaint();
	        }
	        prevPoint = glassPanePoint;
	    }		
	}
	
	public class teleClientController{
		public teleClientController(){
			
		}
		
	}
	
	public static void main(String[] args){
		final teleClient tc = new teleClient();
		
		// Do something on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
            	try{
            		
            	}catch(Exception e){
            		System.out.println("chatClient exception: "+ e);
            	}
            }
        }, "Shutdown-thread"));
	}
}
