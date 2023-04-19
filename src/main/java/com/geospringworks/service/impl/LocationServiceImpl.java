package com.geospringworks.service.impl;

import com.alibaba.fastjson2.JSON;
import com.geospringworks.domain.entity.LocationEntity;
import com.geospringworks.repository.LocationRepository;
import com.geospringworks.service.LocationService;
import lombok.extern.apachecommons.CommonsLog;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;

import java.lang.reflect.Field;
import java.util.*;

import static com.geospringworks.helper.GeometryHelper.convertGeoJsonToJtsGeometry;
import static com.geospringworks.helper.GeometryHelper.convertJtsGeometryToGeoJson;

@Service
@CommonsLog
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;

    /**
     * 获取的同时存入缓存
     * */
    @Override
    @Cacheable(cacheNames = "locations", key = "#id",unless = "#result == null")
    public boolean exists(String userId, Long id) {
        return locationRepository.existsLocationEntityByUserAndId(userId, id);
    }


    /**
     * 添加位置的同时删除缓存
     * */
    @Override
    @CacheEvict(cacheNames = "locations", key = "#id",condition = "#id!= null")
    public Long saveLocation(String userId, Feature feature) {
        LocationEntity locationEntity = convertFeatureToEntity(feature);
        locationEntity.setUser(userId);
        locationRepository.save(locationEntity);
        return locationEntity.getId();
    }

    /**
     * 更新位置的同时删除缓存
     * */
    @Override
    @CacheEvict(cacheNames = "locations", key = "#id",condition = "#id!= null")
    public void updateLocation(String userId, Long id, Feature feature) {
        LocationEntity locationEntity = convertFeatureToEntity(feature);
        locationEntity.setId(id);
        locationEntity.setUser(userId);
        locationRepository.save(locationEntity);
    }

    /**
     * 删除位置的同时删除缓存
     * */
    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "locations", key = "#id",condition = "#id!= null")
    })
    public void deleteLocation(String userId, Long id) {
        locationRepository.delete(locationRepository.findByUserAndId(userId, id).get());
    }

    /**
     * 获取位置信息的同时存入缓存，如果缓存中存在则直接返回，否则从数据库中获取
     * 为了让redis正确缓存数据，这里返回的是JSON字符串，在controller中再转换为Feature对象
     * @param userId 用户id
     * @param id   位置id
     * @return 位置信息JSON
     */
    @Override
    @Cacheable(cacheNames = "locations", key = "#id",unless = "#result == null")
    public String findLocationById(String userId, Long id) {
        Optional<LocationEntity> locationEntity = locationRepository.findByUserAndId(userId, id);
        Optional<Feature> feature = locationEntity.map(this::convertEntityToFeature);
        return feature.map(JSON::toJSONString).orElse(null);
    }

    /**
     * 获取全部位置信息，不存入缓存
     * 因为数据量太大而且变化快，不适合缓存，直接从数据库中获取
     * @param userId 用户id
     * @return 位置信息列表
     */
    @Override
    public FeatureCollection findAllLocations(String userId) {
        List<LocationEntity> locationEntityList = locationRepository.findAllByUser(userId);
        Feature[] features = mapEntityListToFeatures(locationEntityList);
        return new FeatureCollection(features);
    }

    @Override
    public FeatureCollection findAllLocationsWithin(String userId, org.wololo.geojson.Geometry geoJson) {
        Geometry geometry = convertGeoJsonToJtsGeometry(geoJson);
        List<LocationEntity> locationEntityList = locationRepository.findWithin(userId, geometry);
        Feature[] features = mapEntityListToFeatures(locationEntityList);
        return new FeatureCollection(features);
    }

    private Feature[] mapEntityListToFeatures(List<LocationEntity> locationEntityList) {
        return locationEntityList.stream()
                .map(this::convertEntityToFeature)
                .toArray(Feature[]::new);
    }

    private LocationEntity convertFeatureToEntity(Feature feature) {
        LocationEntity entity = new LocationEntity();
        Map<String, Object> propertiesList = feature.getProperties();
        Arrays.stream(LocationEntity.class.getDeclaredFields())
                .filter(i -> !i.isSynthetic())
                .forEach(i -> {
                    try {
                        Field f = LocationEntity.class.getDeclaredField(i.getName());
                        f.setAccessible(true);
                        f.set(entity, propertiesList.getOrDefault(i.getName(), null));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.warn(e.getMessage());
                    }
                });
        entity.setGeometry(convertGeoJsonToJtsGeometry(feature.getGeometry()));
        return entity;
    }

    private Feature convertEntityToFeature(LocationEntity entity) {
        Long id = entity.getId();
        org.wololo.geojson.Geometry geometry = convertJtsGeometryToGeoJson(entity.getGeometry());
        Map<String, Object> properties = new HashMap<>();
        Arrays.stream(LocationEntity.class.getDeclaredFields())
                .filter(i -> !i.isSynthetic())
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        if (field.getType() != Geometry.class && !field.getName().equals("id") && !field.getName().equals("user")) {
                            properties.put(field.getName(), field.get(entity));
                        }
                    } catch (IllegalAccessException e) {
                        log.warn(e.getMessage());
                    }
                });
        return new Feature(id, geometry, properties);
    }
}
