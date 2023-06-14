package Conn;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerConcorrente {
    public static void main(String[] args) throws IOException {
        //socket serverSocket é o socket receptivo
        ServerSocket serverSocket = new ServerSocket(9000);
        while(true) {
            //socket nó é o socket conectivo
            // socket nó terá um porta designada pelo SP - entre 1-24 e 65535
            //accept(): O método accept() escuta uma conexão e aceita se alguma for encontrada. 
            //O accept() bloqueia todo o restante até que uma conexão seja feita, 
            //ele fica em espera aguardando que alguém conecte. Quando alguma conexão é aceita ele 
            //retorna um objeto Socket, que veremos mais à frente.
            System.out.println("Esperando conexão com cliente");
            Socket no = serverSocket.accept(); //bloqueante - fica nesse ponto esperando ação
            System.out.println("Conexão aceita");
            
            //thread para atender novo nó
            ThreadAtendimento thread = new ThreadAtendimento(no);
            thread.start(); //executa a thread -> chamada ao método run()
        }
        
    }
}
