package NapsterRMI.server;

import java.net.ServerSocket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import NapsterRMI.model.IServico;
import NapsterRMI.model.Peer;
import NapsterRMI.model.Servico;


public class Servidor {

    private static String IP;
    private static int PORTA;
    
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------ SERVIDOR ------------------");
        System.out.println("Qual o IP do servidor?");
        IP = scanner.nextLine();

        System.out.println("Qual a porta desse servidor?");
        PORTA = scanner.nextInt();

        IServico sh =  new Servico();
        LocateRegistry.createRegistry(PORTA);
        //LocateRegistry.createRegistry(1099);

        Registry reg = LocateRegistry.getRegistry();
        reg.bind("rmi://127.0.0.1/Napster", sh);
        //reg.bind("rmi://"+IP+"/Napster", sh);
        System.out.println("Servidor Napster no ar");
        
    }


}
