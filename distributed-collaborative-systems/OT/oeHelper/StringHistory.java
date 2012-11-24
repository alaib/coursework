package oeHelper;

import util.annotations.StructurePattern;

import util.annotations.StructurePatternNames;

/**
 * String History Interface
 * @author ravikirn
 *
 */
@StructurePattern(StructurePatternNames.VECTOR_PATTERN)
public interface StringHistory {
	public int size();
	public String elementAt(int index);
	boolean isFull();
	public void addElement(String element);
}
