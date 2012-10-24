package tracer;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import util.trace.TraceableListener;

public class TraceableListenerMod implements TraceableListener{
	static JFrame frameUI;
	static Container containerUI;
	static JTextArea archivePaneUI;
	static JScrollPane archiveScrollPaneUI;
	
	public TraceableListenerMod(){
		genUI();
	}
	
	public static void genUI(){
		archivePaneUI = new JTextArea(10, 20);
		archivePaneUI.setEditable(false);

		DefaultCaret caret = (DefaultCaret)archivePaneUI.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		frameUI = new JFrame();
		containerUI = frameUI.getContentPane();

		archiveScrollPaneUI = new JScrollPane(archivePaneUI);
		archiveScrollPaneUI.setPreferredSize(new Dimension(500, 300));
		containerUI.add(archiveScrollPaneUI);
		
		frameUI.setTitle("Logger");
		frameUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameUI.pack();
		frameUI.setVisible(true);
	}
	
	@Override
	public void newEvent(Exception arg0) {
		String msg = arg0.toString();
		if(msg.length() > 72){
			msg = msg.substring(0, 70) + "...";
		}
		msg = msg + "\n";
		archivePaneUI.append(msg);
	}
}