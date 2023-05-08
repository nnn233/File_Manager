package com.example.filemanager;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MD5Service {
    Context context;

    DBProvider provider;
    GeneratorMD5 generatorMD5;

    FileHelper helper;

    private boolean isFilesWereUpdated = false;

    public MD5Service(Context context) {
        this.context = context;
        provider = new DBProvider(context);
        generatorMD5 = new GeneratorMD5();
    }

    public boolean areMD5CodesChanged(ArrayList<UserFile> files) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HashMap<Integer, String> map = checkMD5Files(files);
            if (!map.isEmpty()) {
                isFilesWereUpdated = true;
            }
            provider.close();
        });
        return isFilesWereUpdated;
    }

    public void checkData(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FilesProvider filesProvider=new FilesProvider();
        helper = new FileHelper();
        executor.execute(() -> checkDBDataAndRefactor(helper.convertToUserFiles(filesProvider.getAllFiles())));
    }

    public void createAllMD5Codes(ArrayList<UserFile> files) {
        for (UserFile file : files)
            provider.insert(generatorMD5.calculateMD5(file), file.getPath());
        provider.close();
    }

    public void checkDBDataAndRefactor(ArrayList<UserFile> files) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        provider = new DBProvider(context);
        executor.execute(() -> {
            if (provider.isNullData()) {
                createAllMD5Codes(files);
            } else {
                checkMD5Files(files);
            }
            provider.close();
        });
    }

    public ArrayList<UserFile> updateFiles(ArrayList<UserFile> files) {
        HashMap<Integer, String> map = checkMD5Files(files);
        for (Map.Entry<Integer, String> pair : map.entrySet()) {
            File file = new File(pair.getValue());
            helper = new FileHelper(file);
            UserFile userFile = new UserFile(helper.getStringDate(), helper.getExtension(), helper.getNumericalSize(), helper.getSize(), file.getPath());
            files.set(pair.getKey(), userFile);
        }
        return files;
    }

    public HashMap<Integer, String> checkMD5Files(ArrayList<UserFile> files) {
        DBProvider provider=new DBProvider(context);
        HashMap<Integer, String> map=new HashMap<>();
        for (int i=0; i<files.size(); i++){
            UserFile file=files.get(i);
            if(file.exists() || !file.isDirectory()) {
                String code = provider.getCodeByPath(file.getPath());
                String newCode=generatorMD5.calculateMD5(file);
                if(newCode!=null) {
                    if (code == null || code.equals(""))
                        provider.insert(newCode, file.getPath());
                    else if (!newCode.equals(code)) {
                        map.put(i, file.getPath());
                        provider.update(newCode, file.getPath());
                    }
                }
            }
            else
                provider.delete(file.getPath());
        }
        provider.close();
        return map;
    }
}
