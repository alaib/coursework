package oeHelper;

import java.awt.Color;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import tracer.MVCTracerInfo;
import util.models.PropertyListenerRegisterer;
import client.ChatClient;
/**
 * Telepointer Model 
 * @author ravikirn
 *
 */
public class Circle implements Oval, PropertyListenerRegisterer {
	int x, y, width, height;
	boolean filled;
	Color color;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this);
	PropertyChangeListener pListener;
	Circle self;
	ChatClient ch;
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
	
	public void addCH(ChatClient chWindow){
		ch = chWindow;
	}

	public void addListeners() {
		pListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
				//System.out.println(property + " changed from " + e.getOldValue() + " to " + e.getNewValue());
				if (property == "x") {
					if(self.calledFromCustom == 0){
						ch.drawPoint(new Point(x,y));
						if(self.sendEvt == 1){
							ch.sendTelePointerEvtToServer(new Point(x, y));
							MVCTracerInfo.newInfo("Move Telepointer to = ("+Integer.toString(x)+","+Integer.toString(y)+")", this);
						}
					}
				}else if(property == "y"){
					if(self.calledFromCustom == 0){
						ch.drawPoint(new Point(x,y));
						if(self.sendEvt == 1){
							ch.sendTelePointerEvtToServer(new Point(x, y));
							MVCTracerInfo.newInfo("Move Telepointer to = ("+Integer.toString(x)+","+Integer.toString(y)+")", this);
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
		if(calleeMethod.equals("redispatchMouseEvent") || calleeMethod.equals("addCircle") || calleeMethod.equals("drawTelePointersInOE")){
			this.calledFromCustom = 1;
		}else{
			this.calledFromCustom = 0;
//			this.ch.insertToList(new Point(newX, this.y));
		}
		if(calleeMethod.equals("handleTelePointerNotify") || calleeMethod.equals("addCircle")  || calleeMethod.equals("drawTelePointersInOE")){
			this.sendEvt = 0;
		}else{
			this.sendEvt = 1;
		}
		//propertyChangeSupport.firePropertyChange("x", null, x);
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
		//propertyChangeSupport.firePropertyChange("y", null, y);
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