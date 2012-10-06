import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.rmi.RemoteException;

import util.models.PropertyListenerRegisterer;
import bus.uigen.ObjectEditor;

public class Circle implements Oval, PropertyListenerRegisterer {
	int x, y, width, height;
	boolean filled;
	Color color;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this);
	PropertyChangeListener pListener;
	Circle self;
	ChatClientModel ch;
	int calledFromCustom = 0;
	int sendEvt = 0;

	public Circle(int initX, int initY, int initWidth, int initHeight, Color initColor, boolean initFilled) {
		x = initX;
		y = initY;
		width = initWidth;
		height = initHeight;
		color = initColor;
		filled = initFilled;
		self = this;
		addListeners();
		this.addPropertyChangeListener(pListener);
	}
	
	public void addCH(ChatClientModel chWindow){
		ch = chWindow;
	}

	public void addListeners() {
		pListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
				//System.out.println(property + " changed from " + e.getOldValue() + " to " + e.getNewValue());
				if (property == "x") {
					if(self.calledFromCustom == 0){
						ch.cui.myGlassPane.setPoint(new Point(x, y));
						ch.cui.myGlassPane.repaint();
						if(self.sendEvt == 1){
							try {
								ch.serverInt.handleTelePointerEvent(ch.clientName, new Point(x, y), ch.MOVE_POINTER);
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}else if(property == "y"){
					if(self.calledFromCustom == 0){
						ch.cui.myGlassPane.setPoint(new Point(x, y));
						ch.cui.myGlassPane.repaint();
						if(self.sendEvt == 1){
							try {
								ch.serverInt.handleTelePointerEvent(ch.clientName, new Point(x, y), ch.MOVE_POINTER);
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		};
	}

	public int getX() {
		return x;
	}

	public void setXOnly(int newX) {
		x = newX;
	}

	public void setX(int newX) {
		x = newX;
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 
		String calleeMethod = elements[1].getMethodName(); 
		if(calleeMethod.equals("redispatchMouseEvent") || calleeMethod.equals("addCircle")){
			this.calledFromCustom = 1;
		}else{
			this.calledFromCustom = 0;
		}
		if(calleeMethod.equals("handleTelePointerNotify") || calleeMethod.equals("addCircle")){
			this.sendEvt = 0;
		}else{
			this.sendEvt = 1;
		}
		propertyChangeSupport.firePropertyChange("x", null, x);
	}

	
	public int getY() {
		return y;
	}

	public void setY(int newVal) {
		y = newVal;
		Throwable t = new Throwable(); 
		StackTraceElement[] elements = t.getStackTrace(); 
		String calleeMethod = elements[1].getMethodName(); 
		if(calleeMethod.equals("redispatchMouseEvent")){
			this.calledFromCustom = 1;
		}else{
			this.calledFromCustom = 0;
		}
		if(calleeMethod.equals("handleTelePointerNotify")){
			this.sendEvt = 0;
		}else{
			this.sendEvt = 1;
		}
		propertyChangeSupport.firePropertyChange("y", null, y);
	}

	public void setYOnly(int newVal) {
		y = newVal;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int newVal) {
		width = newVal;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int newVal) {
		height = newVal;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color newVal) {
		color = newVal;
	}

	public boolean getFilled() {
		return filled;
	}

	public void setFilled(boolean initFilled) {
		filled = initFilled;
	}

	public void addPropertyChangeListener(PropertyChangeListener pListener) {
		// Do nothing
		propertyChangeSupport.addPropertyChangeListener(pListener);
	}

	public static void main(String args[]) {
	}
}