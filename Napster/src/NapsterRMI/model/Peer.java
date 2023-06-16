package NapsterRMI.model;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class Peer implements Serializable {
    public InetAddress IP;
    public int PORTA;
    
    public Peer(InetAddress iP, int pORTA) {
        IP = iP;
        PORTA = pORTA;
    }

    public InetAddress getIP() {
        return IP;
    }

    public void setIP(InetAddress iP) {
        IP = iP;
    }

    public int getPORTA() {
        return PORTA;
    }

    public void setPORTA(int pORTA) {
        PORTA = pORTA;
    }

    
    
}
