package NapsterRMI.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
    public static String searchFile;
    public static Scanner scanner;
    
    public static void main(String[] args) throws Exception{
        
        scanner = new Scanner(System.in);

        //criando conexão RMI
        Registry reg = LocateRegistry.getRegistry();
        shc = (IServico) reg.lookup("rmi://127.0.0.1/Napster");

        path = null;
        searchFile = null;

        System.out.println("------------------ PEER ------------------");
        
        
        menu();
        
    }

    public static void menu() throws IOException{
        
        Boolean condicao = true;
        String address_str = null;
        int porta = -1;

        while(condicao == true){
            System.out.println("Qual a requisi\u00E7\u00E3o desejada? (apenas n\u00FAmero)");
            System.out.println("[0]: JOIN");
            System.out.println("[1]: SEARCH");
            System.out.println("[2]: DOWNLOAD");
            System.out.println("[3]: LEAVE");
            int input = scanner.nextInt();

            if(input == 0){
                try {
                    if(address_str == null && porta == -1){
                        scanner.nextLine();
                        System.out.println("Qual o IP desse peer?");
                        address_str = scanner.nextLine();
                        System.out.println("Qual a porta desse peer?");
                        porta = scanner.nextInt();

                        InetSocketAddress endereco = new InetSocketAddress(address_str, porta);
                        ServerSocket serverSocket = new ServerSocket();
                        serverSocket.bind(endereco);
                        //ServerSocket serverSocket = new ServerSocket(0);
                        //porta = serverSocket.getLocalPort();
                        InetAddress address = serverSocket.getInetAddress();
                        
                        peer = new Peer(address, porta);
                        
                        
                        //ThreadAtendimento th_atendimento = new ThreadAtendimento(address_str, porta);
                        ThreadAtendimento th_atendimento = new ThreadAtendimento(serverSocket);
                        th_atendimento.start();
                    }
                        
                    if (path == null){
                        path = scanner.nextLine();
                        System.out.println("Qual o nome do diretório que se encontra os seus arquivos?");
                        path = scanner.nextLine();
                    }
                    joinRequest();
                } catch (ServerNotActiveException | IOException | NotBoundException e) {
                    e.printStackTrace();
                }  
            } 
            else if(input == 1){
                if(porta == -1 && address_str == null){
                    System.out.println("Você ainda não se juntou ao Napster, se junte e depois faça o download!");
                } else {
                    scanner.nextLine();
                    System.out.println("Qual o nome do arquivo que você deseja procurar?");
                    searchFile = scanner.nextLine();
                    
                    searchRequest(searchFile);
                }
                
            }
            else if(input == 2){
                

                if(searchFile.equals(null)){
                    System.out.println("Você ainda não pesquisou por nenhum arquivo, faça uma pesquisa antes de fazer o download");
                }
                else if (porta == -1 && address_str == null){
                    System.out.println("Você ainda não se juntou ao Napster, se junte e depois faça o download!");
                }
                else{
                    scanner.nextLine();
                    System.out.println("Qual o IP do peer que tem esse arquivo?");
                    String ipStrSearch = scanner.nextLine();
                    
                    System.out.println("Qual a porta do peer que tem esse arquivo?");
                    int portaSearch = scanner.nextInt();

                    downloadRequest(searchFile, ipStrSearch, portaSearch);
                }
                
                
                
            }
            else if(input == 3){
                
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
        OutputStream os = socket_download.getOutputStream();
        DataOutputStream writer = new DataOutputStream(os);

        writer.writeBytes(arquivo + "\n");

        receiveFile(arquivo, socket_download);

        socket_download.close();
    }

    public static void receiveFile(String arquivo, Socket socket) throws IOException{
        //recebe arquivo enviado pelo sevidor
        String savePath = path + '\\' + arquivo;

        // cria a cadeia de entrada (leitura) de informações do socket
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);

        long fileSize = dataInputStream.readLong();

        // Recebe os dados do arquivo
        byte[] buffer = new byte[8192];
        int bytesRead;
        long totalBytesRead = 0;
        float porc;
        while (totalBytesRead < fileSize && (bytesRead = dataInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            porc = 100*((float) totalBytesRead/ (float)fileSize);
            System.out.println(String.format("%.2f", porc)+"% recebido");
        }
        
        String r = shc.UPDATE(arquivo, peer);
        if(r.equals("UPDATE_OK")){
            System.out.println("Arquivo recebido com sucesso.");
        }
        
    }
  
    public static class ThreadAtendimento extends Thread{
        /*public static String ip;
        public static int porta;
        public ThreadAtendimento(String ip, int porta){
            this.ip = ip;
            this.porta = porta;
        }*/
        public static ServerSocket serverSocket;

        public ThreadAtendimento(ServerSocket ss) {
            serverSocket = ss;
        }

        public void sendFile(String filePath, Socket socket) throws IOException{

            FileInputStream fileInputStream = new FileInputStream(filePath);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            File file = new File(filePath);
            long fileSize = file.length();
            
            // Envia o tamanho do arquivo para o servidor
            dataOutputStream.writeLong(fileSize);
            
            // Envia os dados do arquivo
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }
            

        }
        
        public void run(){
            try{

                /*InetSocketAddress endereco = new InetSocketAddress(this.ip, this.porta);
                //ServerSocket serverSocket = new ServerSocket();
                //serverSocket.bind(endereco);
                ServerSocket serverSocket = new ServerSocket(0);
                int porta = serverSocket.getLocalPort();
                InetAddress address = serverSocket.getInetAddress();
                
                peer = new Peer(address, porta);*/
                while(true) {
                    Socket no = serverSocket.accept(); // Espera por uma conexão
                    Thread th_accept = new Thread(() -> {
                        try (InputStreamReader is = new InputStreamReader(no.getInputStream())) {
                            BufferedReader reader = new BufferedReader(is);
                            String fileName = reader.readLine();
                            boolean estaPresente = arquivos.contains(fileName);
                            
                            if (estaPresente) {

                                String filePath = path + '\\' + fileName;
                                sendFile(filePath, no);

                            } 
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    });
                    th_accept.start();
                }
                
            }catch(Exception e){
                System.err.println(e);
            }

        }
    }

}
