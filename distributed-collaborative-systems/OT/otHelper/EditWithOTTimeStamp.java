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
	
	public EditWithOTTimeStamp(int p, Character ch, String operation, OTTimeStamp ts) throws RemoteException{
		pos = p;
		c = ch;
		op = operation;
		t = ts;
	}
	
	public void print() throws RemoteException{
		System.out.format("pos = %d, char = %c, op = %s, localCount = %d, remoteCount = %d\n", pos, c, op, t.localCount, t.remoteCount);
	}
}
