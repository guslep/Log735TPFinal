package Banque;

import java.beans.Transient;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
 import sun.misc.Lock;

/**
 * Created by Gus on 6/4/2015.
 */
public class FileServer implements Serializable {

    private int id;

    private    InetAddress succursaleIPAdresse;
    private int montant;
    private String nom;
    private String port;

    private transient Lock montantLock=new Lock() {
    };

    public FileServer(InetAddress succursaleIPAdresse, int montant, String nom, String portNumber) {
        this.succursaleIPAdresse = succursaleIPAdresse;
        this.montant = montant;
        this.nom = nom;
        this.port=portNumber;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public InetAddress getSuccursaleIPAdresse() {
        return succursaleIPAdresse;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public String getNom() {
        return nom;
    }

    public void setSuccursaleIPAdresse(InetAddress succursaleIPAdresse) {
        this.succursaleIPAdresse = succursaleIPAdresse;
    }

    public String toString(){

        return id+","+nom+","+montant+","+succursaleIPAdresse.getHostAddress()+","+port;

    }

    public synchronized void receiveDeposit(int depot) {


        try {
            montantLock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
                montant+=depot;

            }finally {

                montantLock.unlock();
                System.out.println("Montant total de la succursale est de "+ montant +" "+ depot +"ont été ajouté");

            }





    }



    public synchronized int doWHitdraw(int whitdraw){
        if(whitdraw<0){
            return -1;
        }

        try {
            montantLock.lock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            if(montant-whitdraw<0)
        {
            return -1;//retourne -1 si le retrait a été impossible

        }else{
                System.out.println("Montant avant retrait "+montant);
            montant-=whitdraw;}

        }finally {

            montantLock.unlock();
            System.out.println("Montant total de la succursale est de "+ montant+" "+whitdraw +" ont été retiré");

        }



    return whitdraw;
    }
}
