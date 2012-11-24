package otHelper;

import java.io.Serializable;

public class OTTimeStamp implements Serializable{
	public int localCount;
	public int remoteCount;
	
	public OTTimeStamp(){
		localCount = 0;
		remoteCount = 0;
	}
	
	public OTTimeStamp(int lCount, int RCount){
		localCount = lCount;
		remoteCount = RCount;
	}
	
	public void incrementLocalCount(){
		localCount += 1;
	}
	
	public void incrementRemoteCount(){
		remoteCount += 1;
	}
	
	public int isConcurrent(OTTimeStamp t2){
		if(this.localCount > t2.localCount && this.remoteCount < t2.remoteCount){
			return 1;
		}
		if(this.localCount < t2.localCount && this.remoteCount > t2.remoteCount){
			return 1;
		}
		return 0;
	}
	
	public int isGreaterOrEqualTo(OTTimeStamp t2){
		if(this.localCount > t2.localCount && this.remoteCount >= t2.remoteCount){
			return 1;
		}
		if(this.localCount >= t2.localCount && this.remoteCount > t2.remoteCount){
			return 1;
		}
		return 0;
	}
	
	public OTTimeStamp deepCopy(){
		OTTimeStamp newOT = new OTTimeStamp();
		newOT.localCount = this.localCount;
		newOT.remoteCount = this.remoteCount;
		return newOT;
	}

}
