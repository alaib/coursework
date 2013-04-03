package tictactoe;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class TicTacToe {
	public String currentPlayer;
	public int totalMoves;
	public int [][]moves = {{0, 0, 0}, {0,0,0}, {0,0,0}};
	public JButton[] b;
	JFrame f;
	JPanel p;
	int first;

	public TicTacToe(){
		currentPlayer = "P1"; //P1 - O , P2 - X
		totalMoves = 0;
		first = 1;
	}
	public static void main(String[] args) {
		TicTacToe t = new TicTacToe();
		t.initUI();
	}

	public void initUI() {
		//Instantiate a frame
		f = new JFrame();
		
		//Set frame height and width (pixels)
		f.setSize(460, 460);
		
		//Instantiate GridBagLayout
		GridBagLayout bgl = new GridBagLayout();
		
		p = new JPanel();
		
		//Add panel to JFrame
		f.add(p);
		
		//Set layout for the panel
		p.setLayout(bgl);
		
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
				c.weightx = 0.5;
				c.weighty = 0.5;
				
				//Set button properties
				b[k] = new JButton();
				//Set background color
				b[k].setBackground(Color.WHITE);
				//Set default icon
				b[k].setIcon(defaultIcon);
				//Add action listener (click handler)
				b[k].addActionListener(new ButtonHandler(this));
					
				//Add button to panel
				p.add(b[k], c);
				
				//Increment k
				k++;
			}
		}
		
		//Set frame title
		f.setTitle("Tic Tac Toe [ Player 1 (O) turn ]");
		//Make the frame visible (important)
		f.setVisible(true);
		//exit on closing frame
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	class ButtonHandler implements ActionListener{
		private TicTacToe t;
		private ImageIcon p1Icon, p2Icon;

		public ButtonHandler(TicTacToe t1){
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
			winner = "Player 1 (O)";
		}else{
			winner = "Player 2 (X)";
		}
		String msg = winner + " won the game";
		JOptionPane.showMessageDialog(f, msg);
		resetGame();
	}
	
	public void reportDraw(){
		String msg = "The game is a draw";
		JOptionPane.showMessageDialog(f, msg);
		resetGame();
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
			f.setTitle("Tic Tac Toe [ Player 2 (X) turn ]");
			first = 2;
		}else{
			currentPlayer = "P1";
			f.setTitle("Tic Tac Toe [ Player 1 (O) turn ]");
			first = 1;
		}
	}
		
	public void togglePlayer(){
		if(this.currentPlayer.equals("P1")){
			this.currentPlayer = "P2";
			f.setTitle("Tic Tac Toe [ Player 2 (X) turn ]");
		}else{
			this.currentPlayer = "P1";
			f.setTitle("Tic Tac Toe [ Player 1 (O) turn ]");
		}
	}
}
