package NapsterRMI.model;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Servico extends UnicastRemoteObject implements IServico{

    public Servico() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
    }

    @Override
    public String JOIN(InetAddress ip, int porta, ArrayList<String> arquivos) throws RemoteException {
        for (String arquivo : arquivos) {
            map.put(arquivo, ip);
        }
        return "JOIN_OK";
    }
    
}
