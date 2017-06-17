package ru.sur_pavel.Library_Client.util;

import java.io.File;
import java.util.*;

/**
 * Class for tracking changes to files in a directory
 */
public abstract class DirWatcher extends TimerTask {
    private String path;
    private File filesArray [];
    private HashMap<File, Long> dir = new HashMap<>();
    private DirFilterWatcher dfw;

    public DirWatcher(String path) {
        this(path, "");
    }

    DirWatcher(String path, String filter) {
        this.path = path;
        dfw = new DirFilterWatcher(filter);
        filesArray = new File(path).listFiles(dfw);

        // transfer to the hashmap be used a reference and keep the
        // lastModfied value
        assert filesArray != null;
        for (File aFilesArray : filesArray) {
            dir.put(aFilesArray, aFilesArray.lastModified());
        }
    }

    @Override
    public final void run() {
        HashSet<File> checkedFiles = new HashSet<>();
        filesArray = new File(path).listFiles(dfw);

        // scan the files and check for modification/addition
        assert filesArray != null;
        for (File aFilesArray : filesArray) {
            Long current = dir.get(aFilesArray);
            checkedFiles.add(aFilesArray);
            if (current == null) {
                // new file
                dir.put(aFilesArray, aFilesArray.lastModified());
                onChange(aFilesArray, "add");
            } else if (current != aFilesArray.lastModified()) {
                // modified file
                dir.put(aFilesArray, aFilesArray.lastModified());
                onChange(aFilesArray, "modify");
            }
        }

        // now check for deleted files
        Set ref = ((HashMap)dir.clone()).keySet();
        ref.removeAll(checkedFiles);
        for (Object aRef : ref) {
            File deletedFile = (File) aRef;
            dir.remove(deletedFile);
            onChange(deletedFile, "delete");
        }
    }

    protected abstract void onChange( File file, String action );
}