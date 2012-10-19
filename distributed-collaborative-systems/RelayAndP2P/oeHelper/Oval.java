package oeHelper;

import java.awt.Color;


import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;

/**
 * Oval Interface provides the template for telepointer model
 * @author ravikirn
 *
 */

@StructurePattern(StructurePatternNames.OVAL_PATTERN)
public interface Oval {
    public int getX(); 
    public void setX(int newVal);
    public int getY();
    public void setY(int newVal);
    public int getWidth();
    public void setWidth(int newVal);
    public int getHeight() ;
    public void setHeight(int newVal);
    public void setFilled(boolean newVal);
    public Color getColor() ;
    public void setColor(Color newVal);
}