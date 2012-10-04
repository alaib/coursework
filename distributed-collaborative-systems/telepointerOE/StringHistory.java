import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;

@StructurePattern(StructurePatternNames.VECTOR_PATTERN)
public interface StringHistory {
	public int size();
	public String elementAt(int index);
	boolean isFull();
	public void addElement(String element);
}
