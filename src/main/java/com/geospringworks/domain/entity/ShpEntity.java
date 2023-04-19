package com.geospringworks.domain.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import java.io.Serial;
import java.io.Serializable;

@Data
@Proxy(lazy = false)
@Entity(name = "shp")
@Table(name = "shp")
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class ShpEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 6801209232978220383L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "url")
    private String url;
    @Column(name = "description")
    private String description;
    @Column(name = "geojson")
    private String geojson;
}
