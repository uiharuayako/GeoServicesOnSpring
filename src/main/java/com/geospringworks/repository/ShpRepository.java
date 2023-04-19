package com.geospringworks.repository;

import com.geospringworks.domain.entity.FileEntity;
import com.geospringworks.domain.entity.ShpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShpRepository extends JpaRepository<ShpEntity, Long> {
    @Query("select f from file as f where f.type = 'shp'")
    List<FileEntity> getAllShp();
    ShpEntity findByFileName(String filename);
}
