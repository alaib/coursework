package customUI;

import java.awt.BorderLayout;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * User Login UI Provider
 * @author ravikirn
 *
 */

public class UserLogin{
	public UserLogin(){
		
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
	public static void main(String[] args){

	}
}