package NapsterRMI.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Dados implements Serializable {
    public String cliente;
    public ArrayList<String> arquivos;

    public Dados(String cliente, ArrayList<String> arquivos){
        this.cliente = cliente;
        this.arquivos = arquivos;
    }
}
