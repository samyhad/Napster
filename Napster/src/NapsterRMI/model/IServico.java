package NapsterRMI.model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface IServico extends Remote{

    public ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

    public String JOIN(String cliente, ArrayList<String> arquivos) throws RemoteException;
}
