package com.example.filemanager;

import java.util.ArrayList;

public class FilesFilter {

    ArrayList<UserFile> files;

    public FilesFilter(ArrayList<UserFile> files){
        this.files=files;
    }

    public ArrayList<UserFile> filterByExtension(String filter){
        ArrayList<UserFile> result=new ArrayList<>();
        for(int i=0; i<files.size(); i++){
            if(files.get(i).getExtension().equals(filter))
                result.add(files.get(i));
        }
        return result;
    }
}
