package com.example.fileseeker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
public class FileSeeker implements Runnable{
    private String path, ext;
    public static File folder;
    public static int threadId;
    public static Map<String, List<String>> searchResult = new HashMap<>();
    public FileSeeker(String path, String ext){
        this.ext = ext;
        this.path = path;
    }
    @Override
    public void run() {
        findFiles(path, ext);
    }
    private void findFiles(String dir, String ext){
        File f = new File(dir);
        File[] files = f.listFiles();
        boolean flag = true;
        String threadName = "Thread-" + ++threadId;
        List<String> fileList = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory() && ext.equals('.' + FilenameUtils.getExtension(file.toString()))){
                fileList.add("file: " + file);
                flag = false;
            }
        }
        if (flag){
            fileList.add("Files in directory \"" + f + "\" not found.");
        }
        searchResult.put(threadName, new ArrayList<String>());
        for (String value : fileList){
            searchResult.get(threadName).add(value);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                FileSeeker fs = new FileSeeker(file.toString(), ext);
                Thread thread = new Thread(fs);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

