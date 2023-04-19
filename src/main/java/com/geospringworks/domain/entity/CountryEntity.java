package com.geospringworks.domain.entity;

import org.locationtech.jts.geom.Geometry;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import java.io.Serial;
import java.io.Serializable;

@Data
@Proxy(lazy = false)
@Entity(name = "country")
@Table(name = "country")
@NoArgsConstructor
@AllArgsConstructor
public class CountryEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7047400676040917614L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "shape")
    private Geometry geometry;

    @Column(name = "iso_a2", length = 2)
    private String iso2;

    @Column(name = "iso_a3", length = 3)
    private String iso3;

    @Column(name = "name", length = 40)
    private String name;

}