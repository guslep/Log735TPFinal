package succursale.Message;

import Banque.FileServer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Gus on 6/11/2015.
 */
public   class UpdateListFileServer extends Message implements Serializable {
    private String initiator;

    private  int initiatorId;
    private ArrayList<FileServer> listFileServerInTheSystem= new ArrayList<FileServer>();

    public UpdateListFileServer(String initiator, int initiatorId, ArrayList<FileServer> listFileServerInTheSYstem) {
        this.initiator = initiator;
        this.initiatorId = initiatorId;
        this.listFileServerInTheSystem = (ArrayList<FileServer>)listFileServerInTheSYstem.clone();
    }

    public int getInitiatorId() {
        return initiatorId;
    }

    public String getInitiator() {
        return initiator;
    }




    public ArrayList<FileServer> getListFileServerInTheSystem() {
        return listFileServerInTheSystem;
    }
}
