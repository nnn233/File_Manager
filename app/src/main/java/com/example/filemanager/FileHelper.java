package com.example.filemanager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FileHelper {
    File file;

    public FileHelper(File file){
        this.file=file;
    }

    public FileHelper() {}

    public String getStringDate(){
        long date=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                date = attr.creationTime().toMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            date=file.lastModified(); //если версия SDK меньше 26, то невозможно узнать дату создания файла
        return new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH).format(date);
    }

    public Long getNumericalDate(){
        long date=0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                date = attr.creationTime().toMillis();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            date=file.lastModified(); //если версия SDK меньше 26, то невозможно узнать дату создания файла
        return date;
    }

    public String getSize() {
        long size;
        if(file.isDirectory())
            size=getFolderSize(file);
        else
            size = file.length();
        if(size<1024)
            return size + " Байт";
        else size=size/1024;

        if (size >= 1024)
            return (size/1024) + " Мб";
        else
            return size + " Кб";
    }

    public Long getNumericalSize() {
        long size;
        if(file.isDirectory())
            return getFolderSize(file);
        else
            return file.length();
    }

    private long getFolderSize(File folder){
        File[] fileList = folder.listFiles();
        long result = 0;
        if (fileList != null)
            for (File value : fileList)
                if (value.isDirectory())
                    result += getFolderSize(value);
                else
                    result += value.length();
        return result;
    }

    public String getExtension(){
        if(file.isDirectory())
            return "";
        return file.getPath().substring(file.getPath().lastIndexOf("."));
    }

    public ArrayList<UserFile> convertToUserFiles(ArrayList<File> files){
        ArrayList<UserFile> userFiles=new ArrayList<>();
        File file;
        FileHelper helper;
        for(int i=0; i<files.size(); i++) {
            file=files.get(i);
            helper=new FileHelper(file);
            userFiles.add(new UserFile(helper.getStringDate(), helper.getExtension(),  helper.getNumericalSize(), helper.getSize(), file.getAbsolutePath()));
        }
        return userFiles;
    }

}
