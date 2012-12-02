package otHelper;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client.ClientCallbackInterface;

public class EditWithOTTimeStamp  extends UnicastRemoteObject implements EditWithOTTimeStampInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int pos;
	Character c;
	String op;
	public OTTimeStamp t;
	int isServerFlag;
	int id;
	
	public EditWithOTTimeStamp(int p, Character ch, String operation, int serverFlag, int priority, OTTimeStamp ts) throws RemoteException{
		pos = p;
		c = ch;
		op = operation;
		isServerFlag = serverFlag;
		id = priority;
		t = ts;
	}
	
	public void print() throws RemoteException{
		System.out.format("pos = %d, char = %c, op = %s, isServer = %d, id = %d, localCount = %d, remoteCount = %d\n",
						   pos, c, op, isServerFlag, id, t.localCount, t.remoteCount);
	}
	
	public int getPos() throws RemoteException{
		return pos;
	}
	
	public void setPos(int val) throws RemoteException{
		pos = val;
	}
	
	public EditWithOTTimeStampInterface copy() throws RemoteException{
		EditWithOTTimeStampInterface newEdit = (EditWithOTTimeStampInterface) new 
					EditWithOTTimeStamp(this.pos, this.c, this.op, this.isServerFlag, this.id, new OTTimeStamp(t.localCount, t.remoteCount));
		return newEdit;
	}

	@Override
	public void incrementRemote() throws RemoteException {
		// TODO Auto-generated method stub
		this.t.incrementRemoteCount();
		
	}

	@Override
	public void incrementLocal() throws RemoteException {
		// TODO Auto-generated method stub
		this.t.incrementLocalCount();
		
	}

	@Override
	public String printStr() throws RemoteException {
		// TODO Auto-generated method stub
		String res = "pos = "+pos+", char = "+c+", op = "+op+", isServer = "+isServerFlag+", id = "+id+", localCount = "+t.localCount+
					 ", remoteCount = "+t.remoteCount+"\n";
		return res;
	}

	@Override
	public Character getChar() throws RemoteException {
		// TODO Auto-generated method stub
		return c;
	}

	@Override
	public int getLocalCount() throws RemoteException {
		// TODO Auto-generated method stub
		return t.localCount;
	}

	@Override
	public int getRemoteCount() throws RemoteException {
		// TODO Auto-generated method stub
		return t.remoteCount;
	}

	@Override
	public int isGreaterThanOrEqualTo(EditWithOTTimeStampInterface t2) throws RemoteException {
		if(this.getLocalCount() > t2.getLocalCount() && this.getRemoteCount() >= t2.getRemoteCount()){
			return 1;
		}
		if(this.getLocalCount() >= t2.getLocalCount() && this.getRemoteCount() > t2.getRemoteCount()){
			return 1;
		}
		return 0;
	}

	@Override
	public int isServer() throws RemoteException {
		return this.isServerFlag;
	}

	@Override
	public void setServer(int serverFlag) throws RemoteException {
		this.isServerFlag = serverFlag;
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}

	@Override
	public void setId(int priority) throws RemoteException {
		this.id = priority;
	}
}
