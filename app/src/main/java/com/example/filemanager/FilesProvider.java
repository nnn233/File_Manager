package com.example.filemanager;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class FilesProvider {

    public ArrayList<File> getAllFiles(){
        File file= new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        return getFilesFromDirectories(file);
    }

    public ArrayList<File> getFilesFromChosenDirectory(String path) {
        File dir =new File(path);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));
    }

    private ArrayList<File> getFilesFromDirectories(File dir) {
        ArrayList<File> inFiles = new ArrayList<>();
        File[] files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory())
                inFiles.addAll(getFilesFromDirectories(file));
            else
                inFiles.add(file);
        }
        return inFiles;
    }
}
