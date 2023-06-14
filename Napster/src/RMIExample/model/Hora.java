package RMIExample.model;

import java.io.Serializable;

public class Hora implements Serializable {
    public String nomeCliente;
    public long timestamp;

    public Hora(String nomeCli, long timestamp){
        this.nomeCliente = nomeCli;
        this.timestamp = timestamp;
    }
}
