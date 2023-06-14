package Conn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.midi.Soundbank;
import javax.swing.text.StyledEditorKit;

public class TCPClient {
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        //teta criar uma conexao com o host remoto 127.0.0.1. na porta 9000 (porta do servidor)
        //socket s terá uma porta designada pelo SO -  entre 1024 d 65535
        Socket s = new Socket("127.0.0.1", 9000);


        // cria a cadeia de saída (escrita) de informações do socket
        OutputStream os = s.getOutputStream();
        DataOutputStream writer = new DataOutputStream(os);

        // cria a cadeia de entrada (leitura) de informações do socket
        InputStreamReader is = new InputStreamReader(s.getInputStream());
        BufferedReader reader = new BufferedReader(is);

        //cria um buffer que lê informações do teclado
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // leitura do teclado
        String texto = inFromUser.readLine(); //código bloqueante - não passa dessa linha até finalizar ela

        // escrita no socket (envio de informação ao host remoto - 'servidor')
        writer.writeBytes(texto + "\n");

        //leitura do socket (recebimento de informaão do host remoto)
        String response = reader.readLine(); //código bloqueante - não passa dessa linha até finalizar ela
        System.out.println("DoServidor:" + response);

        //fechamento do canal (socket)
        s.close();
    }   
}
