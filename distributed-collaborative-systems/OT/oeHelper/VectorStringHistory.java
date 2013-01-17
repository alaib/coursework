package oeHelper;

import java.util.List;

import otHelper.MsgWithEpoch;
import util.annotations.StructurePattern;
import util.annotations.StructurePatternNames;
import util.models.VectorChangeEvent;
import util.models.VectorChangeSupport;
import util.models.VectorListener;
import util.models.VectorListenerRegisterer;


/**
 * Provides data model for History Buffer in the chatClient
 * @author ravikirn
 *
 */
@StructurePattern(StructurePatternNames.VECTOR_PATTERN)
public class VectorStringHistory implements StringHistory, VectorListenerRegisterer {
	public final int MAX_SIZE = 10000;
	String[] contents = new String[MAX_SIZE];
	VectorChangeSupport<String> vChangeSupport = new VectorChangeSupport<String>();
	VectorListener vListener;
	VectorStringHistory self;
	int size = 0;
	
	public VectorStringHistory(){
		self = this;
		addListeners();
		addVectorListener(vListener);
	}
	
	public void addListeners(){
		vListener = new VectorListener(){
			@Override
			public void updateVector(VectorChangeEvent evt) {
				// TODO Auto-generated method stub
				int evtType = evt.getEventType();
				String newVal = (String)evt.getNewValue();
				String oldVal = (String)evt.getOldValue();
				//System.out.println("EvtType = "+evtType+", OldValue = "+oldVal+",NewValue = "+newVal);
			}
			
		};
	}

	public int size() {
		return size;
	}

	public String elementAt(int index) {
		return contents[index];
	}

	public boolean isFull(){
		return size == MAX_SIZE;
	}

	public void addElementAtPos(int pos, String element, List<MsgWithEpoch> msgList) {
		if (isFull())
			System.out.println("Cannot add item to a full history");
		else {
			if(size == 0){
				System.out.println("AddElemAtPos, pos = "+pos+", size = "+size+", elem = "+element);
				this.addElement(element);
			}else{
				size++;
				System.out.println("AddElemAtPos, pos = "+pos+", size = "+size+", elem = "+element);
				for(int i = size; i > pos; i--){
					contents[i] = contents[i-1];
				}
				contents[pos] = element;
				//Object, EventType, oldSize, null, element, newSize
				VectorChangeEvent ve = new VectorChangeEvent(this, VectorChangeEvent.AddComponentEvent, pos, null, element, size);
				vChangeSupport.fireUpdateVector(ve);
			}
				
		}
	}
	
	public void clear(){
		for(int i = size-1; i > 0; i--){
			String item = contents[i];
			contents[i] = "";
			//Object, EventType, oldSize, null, element, newSize
			VectorChangeEvent ve = new VectorChangeEvent(this, VectorChangeEvent.DeleteComponentEvent, i, item, "", size-1);
			vChangeSupport.fireUpdateVector(ve);
		}
		size = 0;
	}
	
	public void addElement(String element) {
		if (isFull())
			System.out.println("Cannot add item to a full history");
		else {
			contents[size] = element;
			size++;
			//Object, EventType, oldSize, null, element, newSize
			VectorChangeEvent ve = new VectorChangeEvent(this, VectorChangeEvent.AddComponentEvent, size-1, null, element, size);
			vChangeSupport.fireUpdateVector(ve);
		}
	}
	
	@Override
	public void addVectorListener(VectorListener vL) {
		vChangeSupport.addVectorListener(vL);
	}
}