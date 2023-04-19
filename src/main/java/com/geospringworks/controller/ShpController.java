package com.geospringworks.controller;

import com.geospringworks.domain.entity.FileEntity;
import com.geospringworks.domain.entity.ShpEntity;
import com.geospringworks.service.ShpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@CommonsLog
@RequestMapping("/api/shp")
@Tag(name = "矢量数据服务", description = "矢量服务 API")
@ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
public class ShpController {
    @Autowired
    private ShpService ShpService;
    /**
     * 获取指定文件名的矢量数据
     * */
    @Operation(summary = "获取矢量数据", description = "获取指定文件名的矢量数据")
    @GetMapping("/{filename}")
    public ResponseEntity<ShpEntity> getShp(@PathVariable String filename){

        return ResponseEntity.ok(ShpService.getShpByFileName(filename));
    }
    /**
     * 获取矢量数据列表
     * */
    @Operation(summary = "获取矢量数据列表", description = "获取矢量数据列表")
    @GetMapping("/list")
    public ResponseEntity<List<FileEntity>> getShpList(){
        return ResponseEntity.ok(ShpService.getShpList());
    }
    /**
     * 直接获取指定文件名的矢量数据的geojson
     * */
    @Operation(summary = "获取矢量数据的geojson", description = "获取指定文件名的矢量数据的geojson")
    @GetMapping("/geojson/{filename}")
    public ResponseEntity<String> getShpGeojson(@PathVariable String filename){
        return ResponseEntity.ok(ShpService.getShpGeojsonByFileName(filename));
    }
}
