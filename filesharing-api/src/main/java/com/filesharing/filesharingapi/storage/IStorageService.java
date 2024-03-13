package com.filesharing.filesharingapi.storage;

import com.filesharing.filesharingapi.exception.storage.StorageInitializationException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface IStorageService {

   // void initialize() throws StorageInitializationException;


    /**
     * Almacena un archivo y devuelve la ruta o identificador del archivo almacenado.
     *
     * @param file el archivo a almacenar.
     * @return la ruta o identificador del archivo almacenado.
     */
    String store(MultipartFile file, String storedFileName);


    /**
     * Elimina un archivo del almacenamiento.
     *
     * @param filename el nombre del archivo a eliminar.
     */
    void delete(String filename);

    Resource download(String filename);
}
