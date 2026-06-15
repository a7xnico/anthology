package com.anthology.service;

import com.anthology.config.MuseScoreConfig;
import com.anthology.exception.FileConversionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MuseScoreService {
    private final MuseScoreConfig museScoreConfig;

    public byte[] convertToPdf(MultipartFile file){

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank())
            throw new FileConversionException("El archivo no tiene un nombre válido");

        if(originalFilename.toLowerCase().endsWith(".pdf"))
            try{
                return file.getBytes();
            } catch (IOException e) {
                throw new FileConversionException("Error al leer el archivo PDF");
            }

        validateFileExtensions(originalFilename);

        Path tempDirectory = null;
        try{
            tempDirectory = Files.createTempDirectory("musescore_");
            Path inputFile = tempDirectory.resolve(originalFilename);
            Path outputFile = tempDirectory.resolve("output.pdf");

            file.transferTo(inputFile);

            ProcessBuilder pb = new ProcessBuilder(
                    museScoreConfig.getPath(),
                    "-o", outputFile.toString(),
                    inputFile.toString()
            );
            pb.directory(tempDirectory.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                String error = new String(process.getInputStream().readAllBytes());
                log.error("MuseScore falló con código {}: {}", exitCode, error);
                throw new FileConversionException("Error al convertir el archivo a PDF");
            }

            return Files.readAllBytes(outputFile);

        } catch (IOException | InterruptedException e) {
            log.error("Error durante la conversión con MuseScore: {}", e.getMessage());
            throw new FileConversionException("Error al procesar el archivo");
        }finally{
            if (tempDirectory != null)
                deleteTempDirectory(tempDirectory);
        }

    }

    private void deleteTempDirectory(Path tempDir){
        try (Stream<Path> paths = Files.walk(tempDir)){
            paths.sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {Files.delete(path);}
                        catch (IOException e){
                            log.warn("No se pudo eliminar el archivo temporal: {}", path);
                        }
                    });
        }catch (IOException e){
            log.warn("No se pudo eliminar el directorio temporal: {}", tempDir);
        }
    }

    private void validateFileExtensions(String filename){
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        List<String> validExtensions = List.of(".musicxml", ".xml", ".gp", ".gpx", ".gp5", ".pdf");

        if (!validExtensions.contains(extension))
            throw new FileConversionException("Formato de archivo no soportado, Use MusicXML o GuitarPro");
    }
}
