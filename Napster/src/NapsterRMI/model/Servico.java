package NapsterRMI.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Servico extends UnicastRemoteObject implements IServico{

    public Servico() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
    }

    @Override
    public String JOIN(String cliente, ArrayList<String> arquivos) throws RemoteException {
        for (String arquivo : arquivos) {
            map.put(arquivo, cliente);
        }
        
        return "JOIN_OK";
    }
    
}
