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
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import NapsterRMI.model.IServico;
import NapsterRMI.model.Peer;

public class Cliente {

    private static ArrayList<String> arquivos = new ArrayList<>();
    private static String path;
    private static Peer peer;
    public static IServico shc;
    public static Socket s;
    
    public static void main(String[] args) throws Exception{
        
        // Criando um objeto Socket
        s = new Socket("127.0.0.1", 9000);
       
        // Obtendo o InetAddress associado ao Socket
        InetAddress address = s.getLocalAddress();
        int porta = s.getLocalPort();
        peer = new Peer(address, porta);

        //criando conexão RMI
        Registry reg = LocateRegistry.getRegistry();
        shc = (IServico) reg.lookup("rmi://127.0.0.1/Napster");

        path = null;
        ThreadAtendimento th_atendimento = new ThreadAtendimento(porta);
        th_atendimento.start();
        
        Thread th_menu = new Thread(() -> {
            try {
                menu();
            } catch (IOException e) {
                e.printStackTrace();
            } 
        });
        th_menu.start();
        
        
    }

    public static void menu() throws IOException{
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("------------------ PEER ------------------");
        Boolean condicao = true;

        while(condicao == true){
            System.out.println("Qual a requisi\u00E7\u00E3o desejada? (apenas n\u00FAmero)");
            System.out.println("[0]: JOIN");
            System.out.println("[1]: SEARCH");
            System.out.println("[2]: DOWNLOAD - op\u00E7\u00E3o indispon\u00EDvel");
            System.out.println("[3]: LEAVE - op\u00E7\u00E3o indispon\u00EDvel");

            int input = scanner.nextInt();

            if(input == 0){
                try {
                    if (path == null){
                        scanner.nextLine();
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
                
                searchRequest(arquivo);
            }
            else if(input == 2){
                

                scanner.nextLine();
                System.out.println("Qual o nome do arquivo que você deseja procurar?");
                String arquivo = scanner.nextLine();

                System.out.println("Qual o IP do peer que tem esse arquivo?");
                String ipStr = scanner.nextLine();
                
                System.out.println("Qual a porta do peer que tem esse arquivo?");
                int porta = scanner.nextInt();

                
                if (path == null){
                    path = scanner.nextLine();
                    System.out.println("Você quer salvar esse arquivo em qual diretório?");
                    path = scanner.nextLine();
                }
                
                downloadRequest(arquivo, ipStr, porta);
            }
            else if(input == 3){
                s.close();
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
        
        if(peers == null){
            System.out.println("Arquivo não foi encontrado");
        }else{
            System.out.println("peers com arquivo solicitado:");
            for (Peer p : peers){
                System.out.println("["+p.IP+":"+p.PORTA+"]");
            }
        }

    }

    public static void downloadRequest(String arquivo, String ipPeer, int portaPeer) throws IOException{
        // Criando o socket - conexão TCP entre os dois Peers
        Socket socket_download = new Socket(ipPeer, portaPeer);

        // cria a cadeia de saída (escrita) de informações do socket
        OutputStream os = s.getOutputStream();
        DataOutputStream writer = new DataOutputStream(os);

        // escrita no socket (envio de informação ao host remoto - 'servidor')
        writer.writeBytes(arquivo + "\n");

        // cria a cadeia de entrada (leitura) de informações do socket
        InputStreamReader is = new InputStreamReader(socket_download.getInputStream());
        BufferedReader reader = new BufferedReader(is);

        //leitura do socket (recebimento de informaão do host remoto)
        String response = reader.readLine(); //código bloqueante - não passa dessa linha até finalizar ela

        System.out.println(response);
    }

    public static void receiveFile(String savePath, Socket socket) throws IOException{
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);

        // Recebe o nome do arquivo e o tamanho do cliente
        String fileName = dataInputStream.readUTF();
        long fileSize = dataInputStream.readLong();

        // Recebe os dados do arquivo
        byte[] buffer = new byte[8192];
        int bytesRead;
        long totalBytesRead = 0;
        while (totalBytesRead < fileSize && (bytesRead = dataInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
        }
        
        System.out.println("Arquivo recebido com sucesso.");
    }

    public static class ThreadAtendimento extends Thread{

        private int porta;


        public ThreadAtendimento(int porta){
            this.porta = porta;
        }

        public void sendFile(String filePath, Socket socket) throws IOException{

            FileInputStream fileInputStream = new FileInputStream(filePath);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            File file = new File(filePath);
            String fileName = file.getName();
            long fileSize = file.length();
            
            // Envia o nome do arquivo e o tamanho para o servidor
            dataOutputStream.writeUTF(fileName);
            dataOutputStream.writeLong(fileSize);
            
            // Envia os dados do arquivo
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }
            
            System.out.println("Arquivo enviado com sucesso.");
        }

        public void run(){
            try{
                ServerSocket serverSocket = new ServerSocket(porta); // Porta do servidor
                System.out.println("Aguardando conexão...");

                Socket no = serverSocket.accept(); // Espera por uma conexão
                System.out.println("Conexão estabelecida com o cliente.");

                    InputStreamReader is =  new InputStreamReader(no.getInputStream());
                    BufferedReader reader = new BufferedReader(is);
                    String fileName = reader.readLine();
                    boolean estaPresente = arquivos.contains(fileName);
                    
                    if (estaPresente) {

                        OutputStream os = no.getOutputStream();
                        DataOutputStream writer = new DataOutputStream(os);
                        writer.writeBytes("Esse peer realmente possui esse arquivo" + '\n');


                        /*FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fileOutputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }*/

                    } else {
                        OutputStream os = no.getOutputStream();
                        DataOutputStream writer = new DataOutputStream(os);
                        writer.writeBytes("Esse peer NÃO possui esse arquivo" + '\n');
                    }

                    //FileOutputStream fileOutputStream = new FileOutputStream(savePath);
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                    //String message = reader.readLine();
                    //System.out.println("Mensagem recebida: " + message);

                    //FileInputStream fileInputStream = new FileInputStream(filePath);
                    //OutputStream outputStream = socket.getOutputStream()) {

                    //byte[] buffer = new byte[1024];
                    //int bytesRead;
                    //while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        //outputStream.write(buffer, 0, bytesRead);
                    //}


                    /*InputStreamReader is =  new InputStreamReader(no.getInputStream());
                    BufferedReader reader = new BufferedReader(is);

                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    
                    
                    OutputStream os = no.getOutputStream();
                    DataOutputStream writer = new DataOutputStream(os);
                    
                    String arquivo = reader.readLine();
                    boolean estaPresente = arquivos.contains(arquivo);
                    
                    if (estaPresente) {
                        writer.writeBytes("OK");
                        String filePath = this.path + arquivo;
                    } else {
                        writer.writeBytes("NOK");
                    }

                    FileInputStream fileInputStream = new FileInputStream(filePath);
                    OutputStream outputStream = socket.getOutputStream()) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }*/



                    // Fechando conexões
                    //in.close();
                    //out.close();
                    //clientSocket.close();
                
            }catch(Exception e){
                //
            }

        }
    }

}
