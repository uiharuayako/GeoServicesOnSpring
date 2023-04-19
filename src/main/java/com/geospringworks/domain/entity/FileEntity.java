package com.geospringworks.domain.entity;

import com.geospringworks.helper.ProjectUrl;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件实体类
 * */
@Data
@Entity(name = "file")
@Table(name = "file")
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3847268692205280971L;
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
    @Column(name = "type")
    private String type;
}
