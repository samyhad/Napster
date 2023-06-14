package RMIExample.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServicoHoraImpl extends UnicastRemoteObject implements ServicoHora {

    public ServicoHoraImpl() throws RemoteException {
        super();
    }

    @Override
    public Hora obterHora(String nomeClient) throws RemoteException {
        Hora horaServidor = new Hora(nomeClient, System.currentTimeMillis());
        return horaServidor;
    }
    
}
