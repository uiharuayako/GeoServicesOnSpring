package com.geospringworks.domain.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;
import org.locationtech.jts.geom.Geometry;

import java.io.Serial;
import java.io.Serializable;

@Data
@Proxy(lazy = false)
@Entity(name = "location")
@Table(name = "location")
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class LocationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 804592545067290572L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 30)
    private String user;

    @Column(name = "name")
    private String name;


    @Column(name = "geometry")
    private Geometry geometry;

}