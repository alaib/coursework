import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import util.models.PropertyListenerRegisterer;
import bus.uigen.OEFrame;
import bus.uigen.ObjectEditor;

public class Circle implements Oval, PropertyListenerRegisterer {
	int x, y, width, height;
	boolean filled;
	Color color;
	PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport( this);
	PropertyChangeListener pListener;
	Circle self;

	public Circle(int initX, int initY, int initWidth, int initHeight,
			Color initColor, boolean initFilled) {
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

	public void addListeners() {
		pListener = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				String property = e.getPropertyName();
				//System.out.println(property + " changed from " + e.getOldValue() + " to " + e.getNewValue());
				if (property == "x") {
					int newVal = (int) e.getNewValue() + 10;
					//System.out.println("Trying to change property x to " + newVal);
					self.setXOnly(newVal);
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
		propertyChangeSupport.firePropertyChange("x", null, x);
	}

	public int getY() {
		return y;
	}

	public void setY(int newVal) {
		y = newVal;
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
		int x = 10, y = 10, width = 20, height = 20;
		Color col = Color.red;
		boolean filled = true;
		Circle c = new Circle(x, y, width, height, col, filled);
		ObjectEditor.edit(c);
	}
}