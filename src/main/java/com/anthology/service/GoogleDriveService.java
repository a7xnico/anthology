package com.anthology.service;

import com.anthology.config.GoogleDriveConfig;
import com.anthology.exception.FileConversionException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service

public class GoogleDriveService {

    private final GoogleDriveConfig googleDriveConfig;
    private final ResourceLoader resourceLoader;
    private Drive driveClient;

    // Constructor manual solo con las dependencias reales
    public GoogleDriveService(GoogleDriveConfig googleDriveConfig,
                              ResourceLoader resourceLoader) {
        this.googleDriveConfig = googleDriveConfig;
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void initDriveCliente(){
        try{
            InputStream credentialsStream = resourceLoader
                    .getResource(googleDriveConfig.getCredentialsPath())
                    .getInputStream();

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(credentialsStream)
                    .createScoped(Collections.singleton(DriveScopes.DRIVE));

            driveClient = new Drive.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    new HttpCredentialsAdapter(credentials)
            )
                    .setApplicationName("musicsheets")
                    .build();

        } catch (Exception e) {
            log.error("Error al inicializar Google Drive client", e);
            throw new RuntimeException("No se pudo conectar con Google Drive", e);
        }
    }

    public String uploadPdf(byte[] pdfBytes, String filename){
        try {
            File fileMetadata = new File();
            fileMetadata.setName(filename);
            fileMetadata.setParents(List.of(googleDriveConfig.getFolderId()));

            ByteArrayContent mediaContent = new ByteArrayContent("application/pdf", pdfBytes);

            File uploadedFile = driveClient.files()
                    .create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            makeFilePublic(uploadedFile.getId());

            return uploadedFile.getId();
        } catch (IOException e){
            log.error("Error al subir el PDF a Google Drive {}", e.getMessage());
            throw new FileConversionException("Error al subir el archivo a Google Drive");
        }
    }

    public String buildFileUrl(String fileId){
        return "https://drive.google.com/uc?export=download&id=" + fileId;
    }

    public void deleteFile(String fileId) {
        try {
            driveClient.files()
                    .delete(fileId)
                    .execute();
        } catch (IOException e) {
            log.warn("No se pudo eliminar el archivo de Drive con ID {}: {}", fileId, e.getMessage());
        }
    }

    private void makeFilePublic(String fileId) throws IOException {
        Permission permission = new Permission();
        permission.setType("anyone");
        permission.setRole("reader");

        driveClient.permissions()
                .create(fileId, permission)
                .execute();
    }
}
