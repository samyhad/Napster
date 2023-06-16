package NapsterRMI.model;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public interface IServico extends Remote{

    public ConcurrentMap<String, InetAddress> map_old_old= new ConcurrentHashMap<>();
    public ConcurrentMap<Peer, String> map_old = new ConcurrentHashMap<>();
    public ConcurrentMap<String, ArrayList<Peer>> map = new ConcurrentHashMap<>();

    //public String JOIN(InetAddress address, int porta, ArrayList<String> arquivos) throws RemoteException;

    public String JOIN(Peer p, ArrayList<String> arquivos) throws RemoteException;
    public ArrayList<Peer> SEARCH(String arquivo, Peer p) throws RemoteException;
    public String UPDATE(String arquivo, Peer p) throws RemoteException;
}
