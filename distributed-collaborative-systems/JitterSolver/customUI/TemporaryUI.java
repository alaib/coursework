package customUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * Temporary UI which can be used and disposed (E.g:- User Login)
 * @author ravikirn
 *
 */

public class TemporaryUI{
	public TemporaryUI(){
		
	}
	
	public String getUserName(){
		String cName;
		final JFrame loginFrame = new JFrame();
	    final List<String> holder = new LinkedList<String>();
		Container loginContainer = loginFrame.getContentPane();

		// Upper Panel
		JLabel loginPrompt = new JLabel("Enter your UserName: ");
		final JTextField userLogin = new JTextField(16);
		JButton connectBtn = new JButton("Connect");

		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	           synchronized (holder) {
	                holder.add(userLogin.getText());
	                holder.notify();
	                loginFrame.dispose();
	           }
			}
		});

		userLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	           synchronized (holder) {
	                holder.add(userLogin.getText());
	                holder.notify();
	                loginFrame.dispose();
	           }
			}
		});

		JPanel loginPanel = new JPanel(new FlowLayout());
		loginPanel.add(loginPrompt);
		loginPanel.add(userLogin);
		loginPanel.add(connectBtn);

		// Add both panels to container
		loginContainer.add(loginPanel, BorderLayout.CENTER);

		// display the window, with focus on typing box
		loginFrame.setTitle("Login");
		loginFrame.setPreferredSize(new Dimension(250, 120));
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.pack();
		userLogin.requestFocusInWindow();

		loginFrame.pack();
		loginFrame.setVisible(true);
		
	    // "logic" thread 
	    synchronized (holder) {
	
	        // wait for input from field
	        while (holder.isEmpty())
				try {
					holder.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        cName = holder.remove(0);
		    return cName;
	    }
	}
	
	public void showMsgBox(String msg){
		final JFrame msgFrame = new JFrame();
		JOptionPane.showMessageDialog(msgFrame, msg);
		
		msgFrame.pack();
		msgFrame.setVisible(true);
		msgFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public String showConnectToUser(String uList, String cName){
		String[] connUserList = uList.split("\n");
		String userName = "";
	    final List<String> holder = new LinkedList<String>();
		
		final JFrame connFrame = new JFrame();
		Container c = connFrame.getContentPane();
		JPanel panel = new JPanel(new BorderLayout());
		c.add(panel);
		DefaultListModel listModel = new DefaultListModel();
		for(int i = 0; i < connUserList.length; i++){
			if(!connUserList[i].equals(cName)){
				listModel.addElement(connUserList[i]);
			}
		}

        //Create the list and put it in a scroll pane.
        final JList list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        
        JButton cButton = new JButton("Connect");
		cButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
	            synchronized (holder) {
	                holder.add((String)list.getSelectedValue());
	                holder.notify();
	                connFrame.dispose();
	            }
			}
			
		});
		panel.add(listScrollPane, BorderLayout.CENTER);
		panel.add(cButton, BorderLayout.SOUTH);
		connFrame.setPreferredSize(new Dimension(200, 200));
		connFrame.setTitle("Connect To User - P2P");
		connFrame.pack();
		connFrame.setVisible(true);
		connFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	    // "logic" thread 
	    synchronized (holder) {
	        // wait for input from field
	        while (holder.isEmpty())
				try {
					holder.wait();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        cName = holder.remove(0);
	        String[] cArr = cName.split("-");
	        return cArr[0].trim();
	    }
	}
	
	public static void main(String[] args){

	}
}