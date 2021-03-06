package tictactoe;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class TicTacToeSeries {
	public String currentPlayer;
	public int totalMoves;
	public int [][]moves = {{0, 0, 0}, {0,0,0}, {0,0,0}};
	public JButton[] b;
	JFrame f;
	JPanel lp;
	JPanel rp;
	JTextField p1, p2, draws;
	int p1Wins, p2Wins, drawCount;
	int first;
	JLabel pStatus;

	public TicTacToeSeries(){
		currentPlayer = "P1"; //P1 - O , P2 - X
		totalMoves = 0;
		p1Wins = p2Wins = drawCount = 0;
		first = 1;
	}
	public static void main(String[] args) {
		TicTacToeSeries t = new TicTacToeSeries();
		t.initUI();
	}

	public void initUI() {
		//Instantiate a frame
		f = new JFrame();
		
		//Set frame layout
		f.setLayout(new FlowLayout());
		
		//Set frame size
		f.setSize(650, 590);
		
		//Instantiate GridBagLayout
		GridBagLayout bgl = new GridBagLayout();
		GridLayout gl = new GridLayout(3, 2, 5, 5);
		
		lp = new JPanel();
		lp.setPreferredSize(new Dimension(460, 560));
		rp = new JPanel();
		rp.setPreferredSize(new Dimension(120, 120));
		
		
		//Add panel to JFrame
		f.add(lp);
		f.add(rp);
		
		//Set layout for the panel
		lp.setLayout(bgl);
		rp.setLayout(gl);
		
		//We want 9 buttons as part of 3x3 grid
		b = new JButton[9];
		int k = 0;
		//GridBagLayout constraints
		GridBagConstraints c = new GridBagConstraints();
		ImageIcon defaultIcon = new ImageIcon(getClass().getResource("default.png"));
		
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				//Used when the component's display area is larger
				//than the component's requested size to determine
				//whether and how to resize the component. 
				c.fill = GridBagConstraints.BOTH;
				
				//Internal X and Y padding
				c.ipadx = 10;
				c.ipady = 10;
				
				//Grid position (i, j)
				c.gridx = i;
				c.gridy = j;
				
				//Weights (0.0 to 1.0) are used to determine how to distribute 
				//space among columns (weightx) and among rows (weighty);
				//this is important for specifying resizing behavior.
				c.weightx = 0.1;
				c.weighty = 0.1;
				
				//Set button properties
				b[k] = new JButton();
				//Set default icon
				b[k].setIcon(defaultIcon);
				//Add action listener (click handler)
				b[k].addActionListener(new ButtonHandler(this));
					
				//Add button to panel
				lp.add(b[k], c);
				
				//Increment k
				k++;
			}
		}
		
		//Add status
		pStatus = new JLabel("Player 1 (O) turn");
		pStatus.setHorizontalAlignment(JLabel.CENTER);
		pStatus.setFont(new Font("SansSerif", Font.BOLD, 20));
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.ipady = 10;
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 0.5;
		c.weighty = 0.5;
		lp.add(pStatus, c);
		
		//Add score tracking
		p1 = new JTextField("0");
		p1.setEditable(false);
		p2 = new JTextField("0");
		p2.setEditable(false);
		draws = new JTextField("0");
		draws.setEditable(false);
		rp.add(new JLabel("Player 1"));
		rp.add(p1);
		rp.add(new JLabel("Player 2"));
		rp.add(p2);
		rp.add(new JLabel("Draw"));
		rp.add(draws);
		
		//Set frame title
		f.setTitle("Tic Tac Toe");
		//Make the frame visible (important)
		f.setVisible(true);
		//exit on closing frame
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	class ButtonHandler implements ActionListener{
		private TicTacToeSeries t;
		private ImageIcon p1Icon, p2Icon;

		public ButtonHandler(TicTacToeSeries t1){
			t = t1;
			p1Icon = new ImageIcon(getClass().getResource("O.png"));
			p2Icon = new ImageIcon(getClass().getResource("X.png"));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b;
			if(t.currentPlayer.equals("P1")){
				b = (JButton)e.getSource();
				b.setIcon(p1Icon);
			}else{
				b = (JButton)e.getSource();
				b.setIcon(p2Icon);
			}
			//find which button was click and update data
			int[] state = t.updateState(b);
			//Disable button and setDisabledButtonIcon
			b.setEnabled(false);
			b.setDisabledIcon(b.getIcon());
			//Increment moves
			t.totalMoves++;
			//Check if there is a winner
			t.checkWinner(state);
			if(t.totalMoves != 0){
				//Toggle current player
				t.togglePlayer();
			}
		}
	}
	
	public int[] updateState(JButton b) {
		int width = 3;
		int player;
		int []state = {-1, -1, -1}; //x, y, player
		for(int i = 0; i < width; i++){
			for(int j = 0; j < width; j++){
				if(b == this.b[i+width*j]){
					if(this.currentPlayer.equals("P1")){
						player = 1;
					}else{
						player = 2;
					}
					moves[i][j] = player;
					state[0] = i;
					state[1] = j;
					state[2] = player;
					return state;
				}
			}
		}
		return state;
	}
	
	public void checkWinner(int[] state) {
		int n = 3;
		int x = state[0], y = state[1], player = state[2];
		int winner = -1;
		//Check col
		for(int i = 0; i < n; i++){
			if(moves[x][i] != player){
				break;
			}
			if(i == n-1){
				reportWinner(player);
				return;
			}
		}
		
		//check row
    	for(int i = 0; i < n; i++){
    		if(moves[i][y] != player)
    			break;
    		if(i == n-1){
    			reportWinner(player);
    			return;
    		}
    	}

    	//check diag
    	if(x == y){
    		//we're on a diagonal
    		for(int i = 0; i < n; i++){
    			if(moves[i][i] != player)
    				break;
    			if(i == n-1){
    				reportWinner(player);
    				return;
    			}
    		}
    	}

            //check anti diag (thanks rampion)
    	for(int i = 0;i<n;i++){
    		if(moves[i][(n-1)-i] != player)
    			break;
    		if(i == n-1){
				reportWinner(player);
				return;
    		}
    	}

    	//check draw
    	if(this.totalMoves == 9){
    		reportDraw();
    	}
		
	}
	
	public void reportWinner(int player){
		String winner = "";
		if(player == 1){
			p1Wins++;
			winner = "Player 1 (O)";
		}else{
			p2Wins++;
			winner = "Player 2 (X)";
		}
		String msg = winner + " won the game";
		JOptionPane.showMessageDialog(f, msg);
		resetGame();
		updateSeriesStats();
	}
	
	public void reportDraw(){
		drawCount++;
		String msg = "The game is a draw";
		JOptionPane.showMessageDialog(f, msg);
		resetGame();
		updateSeriesStats();
	}
	
	public void updateSeriesStats(){
		p1.setText(Integer.toString(p1Wins));
		p2.setText(Integer.toString(p2Wins));
		draws.setText(Integer.toString(drawCount));
	}
	
	public void resetGame(){
		int width = 3;
		ImageIcon defaultIcon = new ImageIcon(getClass().getResource("default.png"));
		//Reset all buttons and state
		for(int i = 0; i < width; i++){
			for(int j = 0; j < width; j++){
				b[i+width*j].setEnabled(true);
				b[i+width*j].setIcon(defaultIcon);
				moves[i][j] = 0;
			}
		}
		totalMoves = 0;
		//Switch first moves on alternate games
		if(first == 1){
			currentPlayer = "P2";
			pStatus.setText("Player 2 (X) turn");
			first = 2;
		}else{
			currentPlayer = "P1";
			pStatus.setText("Player 1 (O) turn");
			first = 1;
		}
	}
	
	public void togglePlayer(){
		if(this.currentPlayer.equals("P1")){
			this.currentPlayer = "P2";
			pStatus.setText("Player 2 (X) turn");
		}else{
			this.currentPlayer = "P1";
			pStatus.setText("Player 1 (O) turn");
		}
	}
}