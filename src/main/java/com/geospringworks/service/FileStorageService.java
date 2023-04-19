package com.geospringworks.service;

import com.geospringworks.domain.entity.FileEntity;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;


public interface FileStorageService {
    void init();

    void save(MultipartFile multipartFile,String description);

    void delete(String filename);

    Resource load(String filename);

    Stream<Path> load();

    List<FileEntity> findAllFiles();

    void clear();
}
