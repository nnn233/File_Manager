package com.example.filemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    static int STORAGE_PERMISSION_CODE = 100;
    static String MAIN_DIRECTORY=Environment.getExternalStorageDirectory().getAbsolutePath();

    TextView filesNotFound, pathTextView, sortText, filterText;
    RecyclerView recyclerView;
    ArrayList<UserFile> filesAll;
    ArrayList<UserFile> currentFiles;
    AdapterRecyclerView adapter;
    ImageView sortButton, filterButton, home;

    boolean isPermissionGiven=false;
    boolean wereFilesUpdated=false;
    FilesProvider filesProvider;
    FileHelper fileHelper;
    MD5Service md5Service;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filesAll =new ArrayList<>();
        currentFiles=new ArrayList<>();
        filesProvider=new FilesProvider();
        fileHelper=new FileHelper();
        md5Service=new MD5Service(MainActivity.this);

        filesNotFound=findViewById(R.id.text_null_files);
        recyclerView=findViewById(R.id.files);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);

        path=getIntent().getStringExtra("path");
        if(savedInstanceState!=null)
            path=savedInstanceState.getString("PATH");

        checkPermission(STORAGE_PERMISSION_CODE);
        if (isPermissionGiven) {
            getFiles();
            pathTextView=findViewById(R.id.path);
            pathTextView.setText(path.replace(MAIN_DIRECTORY, ""));
        }
        showRecyclerViews(filesAll);

        sortButton=findViewById(R.id.sort_button);
        sortButton.setOnClickListener(v -> showPopupMenuForSorting());
        sortText=findViewById(R.id.sort_text);

        filterButton=findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> showPopupMenuForFilter());
        filterText=findViewById(R.id.filter_text);

        home=findViewById(R.id.home);
        home.setOnClickListener(v -> {
            currentFiles=filesAll=fileHelper.convertToUserFiles(filesProvider.getFilesFromChosenDirectory(MAIN_DIRECTORY));
            path=MAIN_DIRECTORY;
            pathTextView.setText("");
            sortText.setText(R.string.defaultSort);
            filterText.setText(R.string.anyExtension);
            showRecyclerViews(currentFiles);
        });

        Timer t = new Timer();
        Handler handler = new Handler(Looper.getMainLooper());
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(filesAll.size()>0)
                    if(md5Service.areMD5CodesChanged(filesAll)){
                        filesAll=md5Service.updateFiles(filesAll);
                        currentFiles=md5Service.updateFiles(currentFiles);
                        wereFilesUpdated=true;
                    }
                    handler.post(() -> {
                        if(wereFilesUpdated)
                            showRecyclerViews(currentFiles);
                    });
                }
            },
                0,
                60000);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PATH", path);
    }

    private void getFiles(){
        if(path==null || path.equals("")) {
            filesAll = fileHelper.convertToUserFiles(filesProvider.getFilesFromChosenDirectory(MAIN_DIRECTORY));
            path=MAIN_DIRECTORY;
            md5Service.checkData();
        }
        else
            filesAll=fileHelper.convertToUserFiles(filesProvider.getFilesFromChosenDirectory(path));
        currentFiles=filesAll;
    }

    private void setRecyclerViewAdapter(ArrayList<UserFile> files){
        adapter = new AdapterRecyclerView(MainActivity.this, files);
        recyclerView.setAdapter(adapter);
    }

    private void showFilesByExtension(String extension){
        currentFiles = new ArrayList<>(new FilesFilter(filesAll).filterByExtension(extension));
        showRecyclerViews(currentFiles);
        sortText.setText(R.string.defaultSort);
    }

    private void showRecyclerViews(ArrayList<UserFile> files) {
        if (files.size() > 0) {
            files = new FilesSorter(files).sortByNameAsc();
            filesNotFound.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            setRecyclerViewAdapter(files);
        } else {
            filesNotFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

   private void checkPermission(int requestCode){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
        else
            isPermissionGiven=true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGiven=true;
                getFiles();
                showRecyclerViews(filesAll);
            }
            else
                if(!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE))
                    buildErrorDialog();
                else
                    Toast.makeText(MainActivity.this, "Без этого разрешения приложение не сможет отобразить файлы", Toast.LENGTH_SHORT).show();

    }

    private void buildErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Сбой");
        builder.setMessage("Приложению необходим доступ к файлам для их отображения, Вы можете предоставить права вручную в настройках приложения");

        builder.setPositiveButton("Перейти в настройки", (dialogInterface, i) -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        Button buttonNegative=alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(getResources().getColor(R.color.black, getTheme()));
        Button buttonPositive=alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(getResources().getColor(R.color.black, getTheme()));
    }

    private void showPopupMenuForSorting(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, sortButton);
        popupMenu.getMenuInflater().inflate(R.menu.sorting_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (adapter != null) {
                ArrayList<UserFile> sortedFiles;
                switch (menuItem.getItemId()) {
                    case R.id.itemSizeAsc:
                        sortedFiles = new ArrayList<>(new FilesSorter(currentFiles).sortBySizeAsc());
                        sortText.setText(R.string.sizeAsc);
                        setRecyclerViewAdapter(sortedFiles);
                        break;
                    case R.id.itemSizeDesc:
                        sortedFiles = new ArrayList<>(new FilesSorter(currentFiles).sortBySizeDesc());
                        sortText.setText(R.string.sizeDesc);
                        setRecyclerViewAdapter(sortedFiles);
                        break;
                    case R.id.itemDateDesc:
                        sortedFiles = new ArrayList<>(new FilesSorter(currentFiles).sortByDateDesc());
                        sortText.setText(R.string.DateDesc);
                        setRecyclerViewAdapter(sortedFiles);
                        break;
                    case R.id.itemDateAsc:
                        sortedFiles = new ArrayList<>(new FilesSorter(currentFiles).sortByDateAsc());
                        sortText.setText(R.string.DateAsc);
                        setRecyclerViewAdapter(sortedFiles);
                        break;
                    default:
                        sortText.setText(R.string.defaultSort);
                        showRecyclerViews(currentFiles);
                }
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showPopupMenuForFilter(){
        if (adapter != null) {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, filterButton);
            popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                if (adapter != null) {
                    switch (item.getItemId()) {
                        case R.id.jpg:
                            showFilesByExtension(".jpg");
                            filterText.setText(R.string.jpgExtension);
                            break;
                        case R.id.docx:
                            showFilesByExtension(".doc");
                            filterText.setText(R.string.docExtension);
                            break;
                        case R.id.svg:
                            showFilesByExtension(".svg");
                            filterText.setText(R.string.svgExtension);
                            break;
                        case R.id.mp3:
                            showFilesByExtension(".mp3");
                            filterText.setText(R.string.mp3Extension);
                            break;
                        case R.id.pdf:
                            showFilesByExtension(".pdf");
                            filterText.setText(R.string.pdfExtension);
                            break;
                        case R.id.png:
                            showFilesByExtension(".png");
                            filterText.setText(R.string.pngExtension);
                            break;
                        case R.id.txt:
                            showFilesByExtension(".txt");
                            filterText.setText(R.string.txtExtension);
                            break;
                        case R.id.zip:
                            showFilesByExtension(".zip");
                            filterText.setText(R.string.zipExtension);
                            break;
                        case R.id.any:
                            currentFiles=filesAll;
                            filterText.setText(R.string.anyExtension);
                            showRecyclerViews(currentFiles);
                            break;
                    }
                    return true;
                }
                return false;
            });
            popupMenu.show();
        }
    }
}