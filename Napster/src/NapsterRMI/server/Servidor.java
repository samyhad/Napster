package NapsterRMI.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import NapsterRMI.model.IServico;
import NapsterRMI.model.Servico;


public class Servidor {
    public static void main(String[] args) throws Exception{
        IServico sh =  new Servico();
        LocateRegistry.createRegistry(1099);

        Registry reg = LocateRegistry.getRegistry();
        reg.bind("rmi://127.0.0.1/Napster", sh);
        System.out.println("Servidor no ar");
        
    }

}
