package com.geospringworks.service;

import com.geospringworks.domain.entity.FileEntity;
import com.geospringworks.domain.entity.ShpEntity;

import java.util.List;

public interface ShpService {
    ShpEntity getShpByFileName(String fileName);
    boolean deleteShpByFileName(String fileName);
    List<FileEntity> getShpList();

    String getShpGeojsonByFileName(String filename);
}
