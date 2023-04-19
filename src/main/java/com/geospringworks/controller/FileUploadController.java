package com.geospringworks.controller;


import com.geospringworks.domain.entity.FileEntity;
import com.geospringworks.service.FileStorageService;
import com.geospringworks.valueobject.UploadFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import com.geospringworks.valueobject.Message;

@CrossOrigin(origins = "*")
@RestController
@CommonsLog
@RequestMapping("/file")
@Tag(name = "文件服务", description = "文件服务 API")
@ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
public class FileUploadController {

    @Autowired
    FileStorageService fileStorageService;

    @Operation(summary = "上传文件", description = "向服务器上传文件，文件放在body中，key为file")
    @PostMapping("/upload")
    public ResponseEntity<Message> upload(@RequestParam("file") MultipartFile file,@RequestParam("info") String info){
        try {
            // 如果info为空，则使用默认值
            if(info == null || info.isEmpty()){
                info = "描述为空";
            }
            fileStorageService.save(file,info);
            // 如果上传的是shp文件，则转换为geojson存入数据库中，但是上文已经完成了逻辑，这里只需要提供返回信息
            if(file.getOriginalFilename().endsWith(".shp")){
                return ResponseEntity.ok(new Message("Upload shp file successfully, file has been transformed to geojson"));
            }

            return ResponseEntity.ok(new Message("Upload file successfully: "+file.getOriginalFilename()));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new Message("Could not upload the file:"+file.getOriginalFilename()));
        }
    }

    @Operation(summary = "获取全部文件", description = "获取服务器的所有文件")
    @GetMapping("")
    public ResponseEntity<List<UploadFile>> files(){
        List<UploadFile> files = fileStorageService.load()
                .map(path -> {
                    String fileName = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder
                            .fromMethodName(FileUploadController.class,
                                    "getFile",
                                    path.getFileName().toString()
                            ).build().toString();
                    return new UploadFile(fileName,url);
                }).collect(Collectors.toList());
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "获取文件列表", description = "获取服务器上所有文件的描述及url")
    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> filesList(){
        List<FileEntity> files = fileStorageService.findAllFiles();
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "下载文件", description = "下载服务器上指定的文件")
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("filename")String filename){
        Resource file = fileStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    /**
     * 删除指定文件
     * */
    @Operation(summary = "删除文件", description = "删除服务器上指定的文件")
    @DeleteMapping("/{filename:.+}")
    public ResponseEntity<Message> deleteFile(@PathVariable("filename")String filename){
        try {
            fileStorageService.delete(filename);
            return ResponseEntity.ok(new Message("Delete file successfully: "+filename));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(new Message("Could not delete the file:"+filename));
        }
    }
}
