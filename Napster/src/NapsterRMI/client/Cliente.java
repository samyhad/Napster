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
    private static String path;
    private static Peer peer;
    public static IServico shc;
    
    public static void main(String[] args) throws Exception{
        
        // Criando um objeto Socket
        Socket s = new Socket("127.0.0.1", 9000);
       
        // Obtendo o InetAddress associado ao Socket
        InetAddress address = s.getLocalAddress();
        int porta = s.getLocalPort();
        peer = new Peer(address, porta);

        //criando conexão RMI
        Registry reg = LocateRegistry.getRegistry();
        shc = (IServico) reg.lookup("rmi://127.0.0.1/Napster");


        menu();    
    }

    public static void menu() throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("------------------ PEER ------------------");
        Boolean condicao = true;

        while(condicao == true){
            System.out.println("Qual a requisi\u00E7\u00E3o desejada? (apenas n\u00FAmero)");
            System.out.println("[0]: JOIN");
            System.out.println("[1]: SEARCH - op\u00E7\u00E3o indispon\u00EDvel");
            System.out.println("[2]: DOWNLOAD - op\u00E7\u00E3o indispon\u00EDvel");
            System.out.println("[3]: LEAVE - op\u00E7\u00E3o indispon\u00EDvel");

            int input = scanner.nextInt();

            if(input == 0){
                try {
                    scanner.nextLine();
                    if (path.isEmpty()){
                        System.out.println("Qual o nome do diretório que se encontra os seus arquivos?");
                        path = scanner.nextLine();
                    }
                    joinRequest();
                } catch (ServerNotActiveException | IOException | NotBoundException e) {
                    e.printStackTrace();
                }  
            } 
            else if(input == 1){
                
                scanner.nextLine();
                System.out.println("Qual o nome do arquivo que você deseja procurar?");
                String arquivo = scanner.nextLine();
                
                if (path.isEmpty()){
                    System.out.println("Você quer salvar esse arquivo em qual diretório?");
                    path = scanner.nextLine();
                }
                
                searchRequest(arquivo);
            }
            else if(input == 2){
                System.out.println("Opção indisponível");
            }
            else if(input == 3){
                System.out.println("Opção indisponível");
                condicao = false;
            }
            else{
                System.out.println("Opção inválida");
            }

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

        //listando arquivos que estão na pasta
        listFiles(); 

        String r = shc.JOIN(peer, arquivos);
        if(r.equals("JOIN_OK")){
            System.out.println(
                "Sou peer [" + peer.IP + "]:["+ peer.PORTA +"] com arquivos " + arquivos         
            );
        }
        

    }

    public static void listFiles() {
        
        File directory = new File(path);
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

    public static void searchRequest(String arquivo) throws RemoteException{

        ArrayList<Peer> peers = shc.SEARCH(arquivo, peer);
        
        if(peers.isEmpty()){
            System.out.println("Arquivo não foi encontrado");
        }else{
            System.out.println("peers com arquivo solicitado:");
            for (Peer p : peers){
                System.out.println("["+p.IP+":"+p.PORTA+"]");
            }
        }

    }

}
