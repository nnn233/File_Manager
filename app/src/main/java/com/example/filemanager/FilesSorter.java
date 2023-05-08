package com.example.filemanager;

import java.util.ArrayList;
import java.util.Collections;

public class FilesSorter {

    ArrayList<UserFile> files;

    public FilesSorter(ArrayList<UserFile> files){
        this.files=files;
    }

    public ArrayList<UserFile> sortByNameAsc(){
        Collections.sort(files, (o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName()));
        return files;
    }

    public ArrayList<UserFile> sortBySizeAsc(){
        Collections.sort(files, (o1, o2) -> Long.compare(o1.getSize(), o2.getSize()));
        return files;
    }

    public ArrayList<UserFile> sortBySizeDesc(){
        Collections.sort(files, (o1, o2) -> Long.compare(o2.getSize(), o1.getSize()));
        return files;
    }

    public ArrayList<UserFile> sortByDateAsc(){
        Collections.sort(files, (o1, o2) -> Long.compare(new FileHelper(o1).getNumericalDate(), new FileHelper(o2).getNumericalDate()));
        return files;
    }

    public ArrayList<UserFile> sortByDateDesc(){
        Collections.sort(files, (o1, o2) -> Long.compare(new FileHelper(o2).getNumericalDate(), new FileHelper(o1).getNumericalDate()));
        return files;
    }
}
