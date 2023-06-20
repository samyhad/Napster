package NapsterRMI.server;

import java.net.ServerSocket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import NapsterRMI.model.IServico;
import NapsterRMI.model.Servico;


public class Servidor {
    public static void main(String[] args) throws Exception{
        //ServerSocket serverSocket = new ServerSocket(9000);
        IServico sh =  new Servico();
        LocateRegistry.createRegistry(1099);

        Registry reg = LocateRegistry.getRegistry();
        reg.bind("rmi://127.0.0.1/Napster", sh);
        System.out.println("Servidor Napster no ar");
        
    }

}
