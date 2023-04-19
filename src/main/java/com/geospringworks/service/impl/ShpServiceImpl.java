package com.geospringworks.service.impl;

import com.geospringworks.domain.entity.FileEntity;
import com.geospringworks.domain.entity.ShpEntity;
import com.geospringworks.repository.ShpRepository;
import com.geospringworks.service.ShpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShpServiceImpl implements ShpService {
    @Autowired
    private ShpRepository shpRepository;
    @Override
    @Cacheable(value = "shp",key = "#fileName")
    public ShpEntity getShpByFileName(String fileName) {
        return shpRepository.findByFileName(fileName);
    }

    @Override
    @CacheEvict(value = "shp",key = "#fileName")
    public boolean deleteShpByFileName(String fileName) {
        shpRepository.delete(getShpByFileName(fileName));
        return false;
    }
    @Override
    @Cacheable(value = "shp",key = "#fileName"+".geojson")
    public String getShpGeojsonByFileName(String filename) {
        return getShpByFileName(filename).getGeojson();
    }

    @Override
    public List<FileEntity> getShpList() {
        return shpRepository.getAllShp();
    }
}
