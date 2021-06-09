package com.astra.app.chatprocess.playground;
import java.io.File;
import com.astra.app.btree4j.utils.io.FileUtils;
import com.astra.app.btree4j.BTree;

import com.astra.app.chatprocess.server.Server;

public class ServerStarter {

    public static void main(String[] args) {
	    Server server = new Server();
	    File tmpDir = FileUtils.getTempDir();
	    File tmpFile = new File(tmpDir, "BTreeTest1.idx");
	    tmpFile.deleteOnExit();
	    BTree btree = new BTree(tmpFile);

            server.run();


    }
}
