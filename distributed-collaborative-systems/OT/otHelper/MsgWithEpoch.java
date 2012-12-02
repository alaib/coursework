package otHelper;

public class MsgWithEpoch {
	public long epoch;
	public String msg;
	
	public MsgWithEpoch(long timestamp, String elem){
		epoch = timestamp;
		msg = elem;
	}

}
