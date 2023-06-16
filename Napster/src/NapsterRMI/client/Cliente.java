package NapsterRMI.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import NapsterRMI.model.IServico;
import NapsterRMI.model.Peer;

public class Cliente {

    private static ArrayList<String> arquivos = new ArrayList<>();
    private static String caminho;
    private static InetAddress address;
    
    public static void main(String[] args) throws Exception{
        menu();
              
        
    }

    public static void menu() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("------------------ PEER ------------------");
        System.out.println("Qual a requisi\u00E7\u00E3o desejada? (apenas n\u00FAmero)");
        System.out.println("[0]: JOIN");
        System.out.println("[1]: SEARCH - op\u00E7\u00E3o indispon\u00EDvel");
        System.out.println("[2]: DOWNLOAD - op\u00E7\u00E3o indispon\u00EDvel");

        int input = scanner.nextInt();

        if(input == 0){
            try {
                joinRequest();
            } catch (ServerNotActiveException | IOException | NotBoundException e) {
                e.printStackTrace();
            }  
        } 
        else if(input == 1){
            System.out.println("Op\\u00E7\\u00E3o indispon\\u00EDvel");
        }
        else if(input == 2){
            System.out.println("Op\\u00E7\\u00E3o indispon\\u00EDvel");
        }
        else if(input == 3){
            System.out.println("Op\\u00E7\\u00E3o indispon\\u00EDvel");
        }
        else{
            System.out.println("Op\u00E7\u00E3o inv\u00E1lida");
        }

        scanner.close();
    }

    /**
     * @throws NotBoundException
     * @throws ServerNotActiveException
     * @throws IOException
     * @throws UnknownHostException
     */
    public static void joinRequest() throws NotBoundException, ServerNotActiveException, UnknownHostException, IOException{

        Registry reg = LocateRegistry.getRegistry();
        IServico shc = (IServico) reg.lookup("rmi://127.0.0.1/Napster");
        caminho = "C:/temp/peer1";
        //Scanner scanner = new Scanner(System.in);
        //System.out.println("Qual o nome do diretório que se encontra os seus arquivos?");
        //caminho = scanner.nextLine();
        listarArquivos(caminho);
        // Criando um objeto Socket
        Socket s = new Socket("127.0.0.1", 9000);
        // Obtendo o InetAddress associado ao Socket
        address = s.getLocalAddress();
        int porta = s.getLocalPort();
        Peer p = new Peer(address, porta);
    

        String r = shc.JOIN(p, arquivos);
        System.out.println(r);
        if(r.equals("JOIN_OK")){
            System.out.println(
                "Sou peer [" + address + "]:["+ porta +"] com arquivos " + arquivos         
            );
        }
        

    }

    public static void listarArquivos(String caminho) {
        
        File directory = new File(caminho);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    arquivos.add(fileName);
                }
            }
        }
    }
}
