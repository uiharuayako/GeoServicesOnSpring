package com.geospringworks.service.impl;


import com.geospringworks.domain.entity.FileEntity;
import com.geospringworks.domain.entity.ShpEntity;
import com.geospringworks.helper.ProjectUrl;
import com.geospringworks.repository.FileRepository;
import com.geospringworks.repository.ShpRepository;
import com.geospringworks.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static com.geospringworks.helper.GeometryHelper.transformShpToGeoJson;

@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {

    private final Path path = Paths.get("fileStorage");
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private ProjectUrl projectUrl;
    @Autowired
    private ShpRepository shpRepository;


    @Override
    public void init() {
        try {
            // 如果不存在文件夹，则创建
            if(!Files.exists(path)){
                Files.createDirectory(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile multipartFile,String description) {
        try {
            Files.copy(multipartFile.getInputStream(),this.path.resolve(multipartFile.getOriginalFilename()));
            // 保存文件信息到数据库
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(multipartFile.getOriginalFilename());
            fileEntity.setDescription(description);
            fileEntity.setUrl(projectUrl.getProjectUrl()+"/file/"+multipartFile.getOriginalFilename());
            // 设置文件后缀名
            fileEntity.setType(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1));
            fileRepository.save(fileEntity);
            // 如果文件后缀为shp，则保存到shp表中
            if(multipartFile.getOriginalFilename().endsWith(".shp")){
                // 创建shp实体类
                ShpEntity shpEntity = new ShpEntity();
                shpEntity.setFileName(multipartFile.getOriginalFilename());
                shpEntity.setUrl(projectUrl.getProjectUrl()+"/file/"+multipartFile.getOriginalFilename());
                shpEntity.setDescription(description);
                shpEntity.setGeojson(transformShpToGeoJson(this.path.resolve(multipartFile.getOriginalFilename()).toString()));
                // 保存到数据库
                shpRepository.save(shpEntity);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error:"+e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        Path file = path.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new RuntimeException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error:"+e.getMessage());
        }
    }

    @Override
    public Stream<Path> load() {
        try {
            return Files.walk(this.path,1)
                    .filter(path -> !path.equals(this.path))
                    .map(this.path::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files.");
        }
    }

    @Override
    public void delete(String filename) {
        Path file = path.resolve(filename);
        try {
            Files.delete(file);
            // 在数据库中删除文件信息
            fileRepository.delete(fileRepository.findByFileName(filename));
        } catch (IOException e) {
            throw new RuntimeException("Could not delete the file. Error:"+e.getMessage());
        }
    }

    /**
     * 返回文件列表
     * */
    @Override
    public List<FileEntity> findAllFiles(){
        return fileRepository.findAll();
    }
    @Override
    public void clear() {
        FileSystemUtils.deleteRecursively(path.toFile());
    }
}