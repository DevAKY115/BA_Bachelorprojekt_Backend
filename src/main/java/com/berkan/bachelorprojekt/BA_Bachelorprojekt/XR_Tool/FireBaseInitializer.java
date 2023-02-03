package com.berkan.bachelorprojekt.BA_Bachelorprojekt.XR_Tool;

import com.berkan.bachelorprojekt.BA_Bachelorprojekt.Exceptions.XRToolError;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;

@Service
public class FireBaseInitializer {

    FirebaseOptions options;

    @PostConstruct
    public void initialize(){

        try{
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/berkan-akkaya-bachelorprojekt-firebase-adminsdk-g1ah1-5bfab3e8a0.json");
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("berkan-akkaya-bachelorprojekt")
                    .setStorageBucket("berkan-akkaya-bachelorprojekt.appspot.com")
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void uploadFile(String titel, MultipartFile multipartFile) throws IOException {

        try {
            Storage storage = StorageClient.getInstance().bucket().getStorage();

            BlobId id = BlobId.of("berkan-akkaya-bachelorprojekt.appspot.com", titel + "/" + multipartFile.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(id).setContentType(multipartFile.getContentType()).build();

            storage.create(blobInfo, multipartFile.getBytes());
        }catch (StorageException storageException){
            throw new XRToolError("Couldn't connect to the filestorage server");
        }

    }

    public static String getFile(String titel, String filename){

        try{

            Storage storage = StorageClient.getInstance().bucket().getStorage();


            Blob blob = storage.get(BlobId.of(
                    "berkan-akkaya-bachelorprojekt.appspot.com", titel + "/" + filename));

            return Base64Utils.encodeToString(blob.getContent());
        }catch (StorageException storageException){
            throw new XRToolError("File couldn't be found on the filestorage server");
        }
    }

    public static String getFileURL(String toolTitel, String filename){

        try {

            Storage storage = StorageClient.getInstance().bucket().getStorage();


            Blob blob = storage.get(BlobId.of(
                    "berkan-akkaya-bachelorprojekt.appspot.com", toolTitel + "/" + filename));


            return "data:" + blob.getContentType() + ";base64," + Base64Utils.encodeToString(blob.getContent());
        }catch (StorageException storageException){
            throw new XRToolError("File couldn't be found on the filestorage server");
        }
    }

    public static void deleteFile(String toolTitel, String filename){

        try{

        Storage storage = StorageClient.getInstance().bucket().getStorage();

        storage.delete(BlobId.of("berkan-akkaya-bachelorprojekt.appspot.com", toolTitel + "/" + filename));

        }catch (StorageException storageException){
            throw new XRToolError("File couldn't be deleted from the filestorage server");
        }

    }



}
