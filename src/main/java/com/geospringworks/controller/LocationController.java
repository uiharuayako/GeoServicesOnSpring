package com.geospringworks.controller;

import com.alibaba.fastjson2.JSONObject;
import com.geospringworks.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.apachecommons.CommonsLog;
import org.locationtech.jts.io.WKTWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wololo.geojson.*;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;


@CrossOrigin(origins = "*")
@RestController
@CommonsLog
@RequestMapping("/api")
@Tag(name = "位置服务", description = "位置服务 API")
@ApiResponses(@ApiResponse(responseCode = "200", description = "接口请求成功"))
public class LocationController {

    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    private LocationService locationService;


    @Operation(summary = "上传位置", description = "向服务器上传位置信息，格式为GeoJSON，同时删除Redis缓存")
    @PostMapping("/location")
    public ResponseEntity<Object> postLocation(@RequestHeader(value = AUTHORIZATION) String userId,
                                       @RequestBody Feature feature) {
        log.warn("postLocation: " + feature);
        Long id = locationService.saveLocation(userId, feature);

        return ResponseEntity.ok("body内内容已被插入到数据库\n数据id:"+id+"，通过GET /location/"+id+"获取");
    }


    @Operation(summary ="获取位置", description = "获取指定id的GeoJson位置信息，同时添加到Redis缓存")
    @GetMapping("/location/{id}")
    public ResponseEntity<Object> getLocationById(@RequestHeader(value = AUTHORIZATION) String userId,
                                          @PathVariable("id") Long id) {

        // 先获得位置JSON，再转换为Feature对象
        String location = locationService.findLocationById(userId, id);
        Feature featureObj=JSONObject.parseObject(location,Feature.class);
        return ResponseEntity.ok(featureObj);
    }

    /**
     * 获取指定id的位置信息，返回WKT格式
     * */
    @Operation(summary ="获取位置（WKT）", description = "获取指定id的WKT格式位置信息，并添加到Redis缓存")
    @GetMapping("/location/{id}/wkt")
    public ResponseEntity<Object> getLocationByIdWKT(@RequestHeader(value = AUTHORIZATION) String userId,
                                          @PathVariable("id") Long id) throws Exception {

        // 先获得位置JSON，再转换为Feature对象
        String location = locationService.findLocationById(userId, id);
        Feature featureObj=JSONObject.parseObject(location,Feature.class);
        // 仅获取geometry部分
        GeoJSON geojson = featureObj.getGeometry();
        // 转换为WKT格式
        WKTWriter wktWriter = new WKTWriter();
        org.locationtech.jts.geom.Geometry geometry=new GeoJSONReader().read(geojson);
        String wkt = wktWriter.write(geometry);
        return ResponseEntity.ok(wkt);
    }


    @Operation(summary = "更新位置", description = "更新指定id的位置信息，并删除Redis缓存")
    @PutMapping("/location/{id}")
    public ResponseEntity<Object> putLocation(@RequestHeader(value = AUTHORIZATION) String userId,
                                      @PathVariable("id") Long id,
                                      @RequestBody Feature feature) {
        if (!locationService.exists(userId, id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        locationService.updateLocation(userId, id, feature);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "删除位置", description = "删除指定id的位置信息，并删除Redis缓存")
    @DeleteMapping("/location/{id}")
    public ResponseEntity<Object> deleteLocation(@RequestHeader(value = AUTHORIZATION) String userId,
                                         @PathVariable("id") Long id) {

        if (!locationService.exists(userId, id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        locationService.deleteLocation(userId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "获取所有位置", description = "获取所有位置信息，不进行缓存")
    @GetMapping("/locations")
    public ResponseEntity<FeatureCollection> getAllLocations(@RequestHeader(value = AUTHORIZATION) String userId) {
        return new ResponseEntity<>(locationService.findAllLocations(userId), HttpStatus.OK);
    }

    @Operation(summary = "获取指定范围内的位置", description = "获取指定范围内的位置信息，不进行缓存")
    @PostMapping("/locations/within")
    public ResponseEntity<FeatureCollection> getLocationsByGeometry(@RequestHeader(value = AUTHORIZATION) String userId,
                                                                    @RequestBody org.wololo.geojson.Geometry geoJson) {

        return new ResponseEntity<>(locationService.findAllLocationsWithin(userId, geoJson), HttpStatus.OK);
    }
}
