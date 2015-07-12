package succursale.Message;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 2015-07-11.
 */
public class InitSymchronizerMessage extends Message implements Serializable {
    private File[] listContainedFile;
    private String rootDirectory;


    public InitSymchronizerMessage(File[] listContainedFile, String rootDirectory) {
        this.listContainedFile = listContainedFile;
        this.rootDirectory = rootDirectory;
    }

    public File[] getListContainedFile() {
        return listContainedFile;
    }

    public String getRootDirectory() {
        return rootDirectory;
    }
}
