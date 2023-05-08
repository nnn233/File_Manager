package com.example.filemanager;

public class Contracts {
    public static final String TABLE_NAME_FOR_FILES_CODES ="FilesCodes";
    public static final String CODE ="Code";
    public static final String PATH = "Path";

    public static final String CREATE_TABLE_NOTE ="CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME_FOR_FILES_CODES + " (" +
            CODE +" TEXT PRIMARY KEY NOT NULL, "+
            PATH+" TEXT NOT NULL, " +
            "UNIQUE("+CODE+"));";
}
