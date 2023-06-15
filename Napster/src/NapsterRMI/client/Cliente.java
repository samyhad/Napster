package NapsterRMI.client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import NapsterRMI.model.Servico;

public class Cliente {
    public static void main(String[] args) throws Exception{
        menu();
              
        
    }

    public static void menu(){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("------------------ PEER ------------------");
        System.out.println("Qual a requisi\u00E7\u00E3o desejada? (apenas n\u00FAmero)");
        System.out.println("[0]: JOIN");
        System.out.println("[1]: SEARCH - - op\u00E7\u00E3o indispon\u00EDvel");
        System.out.println("[2]: DOWNLOAD - op\u00E7\u00E3o indispon\u00EDvel");
        System.out.println("[3]: LEAVE - op\u00E7\u00E3o indispon\u00EDvel");

        int input = scanner.nextInt();

        if(input == 0){
            try {
                joinRequest();
            } catch (RemoteException | NotBoundException e) {
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
            System.out.println("Opção inválida");
        }

        scanner.close();
    }

    public static void joinRequest() throws AccessException, RemoteException, NotBoundException{

        Registry reg = LocateRegistry.getRegistry();
        Servico shc = (Servico) reg.lookup("rmi://127.0.0.1/Napster");
        
        String arquivo1 = "Modelando a classe Aula.pdf";
        String arquivo2 = "Conhecendo mais de listas.mp3";
        String arquivo3 = "Trabalhando com Cursos e Sets.mp4";

        ArrayList<String> aulas = new ArrayList<>();
        aulas.add(arquivo1);
        aulas.add(arquivo2);
        aulas.add(arquivo3);

        System.out.println(shc.JOIN("Cliente 1", null));
        

    }
}
