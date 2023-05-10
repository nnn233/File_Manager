package com.example.filemanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {

    ArrayList<UserFile> files;
    Context context;

    FileService fileService;

    public AdapterRecyclerView() {
    }

    public AdapterRecyclerView(Context context, ArrayList<UserFile> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public AdapterRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view_for_file, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerView.ViewHolder holder, int position) {
        UserFile file=files.get(holder.getAdapterPosition());
        holder.fileName.setText(file.getName());
        holder.fileDate.setText(file.getDate());
        holder.fileSize.setText(file.getSizeText());
        holder.fileIcon.setImageResource(chooseImage(file.getExtension()));

        holder.itemView.setOnClickListener(v -> {
            if(file.isDirectory()){
                Intent intent=new Intent(context, MainActivity.class);
                String path=file.getPath();
                intent.putExtra("path", path);
                context.startActivity(intent);
            }
            else {
                fileService = new FileService(file, context);
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.getMenuInflater().inflate(R.menu.file_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.open:
                            fileService.openFile();
                            break;
                        case R.id.share:
                            fileService.shareFile();
                            break;
                    }
                    return true;
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fileName, fileDate, fileSize;
        ImageView fileIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileIcon = itemView.findViewById(R.id.file_icon);
            fileName = itemView.findViewById(R.id.file_name);
            fileDate = itemView.findViewById(R.id.creating_date);
            fileSize = itemView.findViewById(R.id.size);
        }
    }

    private int chooseImage(String extension) {
        switch (extension) {
            case ".jpg":
                return R.drawable.jpg;
            case ".doc":
            case ".docx":
                return R.drawable.doc;
            case ".mp3":
                return R.drawable.mp3;
            case ".pdf":
                return R.drawable.pdf;
            case ".png":
                return R.drawable.png;
            case ".svg":
                return R.drawable.svg;
            case ".txt":
                return R.drawable.txt;
            case ".zip":
                return R.drawable.zip;
            case "":
                return R.drawable.folder;
            default:
                return R.drawable.unknown;
        }
    }
}


