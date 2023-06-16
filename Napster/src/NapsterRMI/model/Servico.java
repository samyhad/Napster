package NapsterRMI.model;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Servico extends UnicastRemoteObject implements IServico{

    public Servico() throws RemoteException {
        super();
    }

    /*@Override
    public String JOIN(InetAddress ip, int porta, ArrayList<String> arquivos) throws RemoteException {
        for (String arquivo : arquivos) {
            map.put(arquivo, ip);
        }
        return "JOIN_OK";
    }*/

    @Override
    public String JOIN(Peer p, ArrayList<String> arquivos) throws RemoteException {
        /*for (String arquivo : arquivos) {
            map.put(p, arquivo);
        }*/

        for (String arquivo: arquivos){

            //Verificar se esse arquivo já existe no nosso SD ou não
            ArrayList<Peer> valor = map.get(arquivo);

            // Verificando se a chave existe, se existir apenas adicionar um novo peer, c.c, add uma nova chave.
            if (valor != null) {
                valor.add(p);
                map.put(arquivo, valor);
            } else {
                ArrayList<Peer> peerList = new ArrayList<>();
                peerList.add(p);
                map.put(arquivo, peerList);
            }

            System.out.println("Peer ["+p.IP+"]:["+p.PORTA+"] adicionado com arquivos"+arquivos);

        }
        
        return "JOIN_OK";
    }

    @Override
    public ArrayList<Peer> SEARCH(String arquivoProcurado, Peer p) throws RemoteException {

        System.out.println("Peer ["+p.IP+"]:["+p.PORTA+"] solicitou arquivo"+arquivoProcurado);

        // Procurar o valor dentro do mapa
        ArrayList<Peer> resultado = map.get(arquivoProcurado);

        return resultado;
    }

    @Override
    public String UPDATE(String arquivo, Peer p) throws RemoteException {

        //Verificar se esse arquivo já existe no nosso SD ou não
            ArrayList<Peer> valor = map.get(arquivo);

            // Verificando se a chave existe, 
            //se existir apenas adicionar um novo peer, c.c, add uma nova chave.
            if (valor != null) {
                valor.add(p);
                map.put(arquivo, valor);
            } else {
                ArrayList<Peer> peerList = new ArrayList<>();
                peerList.add(p);
                map.put(arquivo, peerList);
            }

        return "UPDATE_OK";
    }
    
    
}
