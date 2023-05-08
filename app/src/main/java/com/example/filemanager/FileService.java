package com.example.filemanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class FileService {

    UserFile file;
    Context context;

    public FileService(UserFile file, Context context) {
        this.file = file;
        this.context = context;
    }

    public void openFile(){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
            String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            intent.setDataAndType(Uri.parse(file.getAbsolutePath()), mimetype);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        catch(Exception e){
            Toast.makeText(context, "Не удалось открыть файл", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareFile(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(file.getAbsolutePath());
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        context.startActivity(sharingIntent);
    }
}
