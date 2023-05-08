package com.example.filemanager;

import java.io.File;

public class UserFile extends File{
    String date;
    String extension;
    Long size;
    String sizeText;
    String path;

    public UserFile(String date, String extension, Long size, String sizeText, String path) {
        super(path);
        this.date = date;
        this.extension = extension;
        this.size = size;
        this.sizeText = sizeText;
        this.path=path;
    }


    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
