package com.example.polzunovfeastserver.event.image.entity;

import com.example.polzunovfeastserver.event.image.util.table_key.ImageTableKeys;
import jakarta.persistence.*;
import lombok.*;

@Table(
        name = "images",
        uniqueConstraints = {
                @UniqueConstraint(name = ImageTableKeys.UNIQUE_URL, columnNames = "url"),
                @UniqueConstraint(name = ImageTableKeys.UNIQUE_PATH, columnNames = "path")
        }
)
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    @EqualsAndHashCode.Include
    private Long id;

    private String url;

    /**
     * Relative path to this image in file system.
     * Root of this path is a folder where all static data is stored (root is not included in the path).
     * Name of the root folder should be specified in application.yaml
     */
    private String path;
}
