package com.anthology.service;

import com.anthology.config.CloudinaryConfig;
import com.anthology.exception.FileConversionException;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final CloudinaryConfig cloudinaryConfig;
    private Cloudinary cloudinary;

    @PostConstruct
    public void init(){
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryConfig.getCloudName(),
                "api_key", cloudinaryConfig.getApiKey(),
                "api_secret", cloudinaryConfig.getApiSecret()
        ));
    }

    public String uploadPdf(byte[] pdfBytes, String filename){
        try {
            Map uploadResult = cloudinary.uploader().upload(pdfBytes, ObjectUtils.asMap(
                    "resource_type", "raw",
                    "public_id", filename,
                    "format", "pdf"
            ));
            return uploadResult.get("public_id").toString();
        }catch (IOException e){
            log.error("Error al subir el pdf a Cloudinary: {}", e.getMessage());
            throw new FileConversionException("Error al subir el archivo a Cloudinary");
        }
    }

    public String buildFileUrl(String publicId) {
        return cloudinary.url()
                .resourceType("raw")
                .format("pdf")
                .generate(publicId);
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap(
                    "resource_type", "raw"
            ));
        } catch (IOException e) {
            log.warn("No se pudo eliminar el archivo de Cloudinary con ID {}: {}", publicId, e.getMessage());
        }
    }
}
