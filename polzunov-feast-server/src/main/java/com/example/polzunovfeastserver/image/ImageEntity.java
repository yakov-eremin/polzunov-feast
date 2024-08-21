package com.example.polzunovfeastserver.image;

import com.example.polzunovfeastserver.image.util.ImagesTableKeys;
import jakarta.persistence.*;
import lombok.*;

@Table(
        name = "images",
        uniqueConstraints = {
                @UniqueConstraint(name = ImagesTableKeys.UNIQUE_URL, columnNames = "url"),
                @UniqueConstraint(name = ImagesTableKeys.UNIQUE_PATH, columnNames = "path")
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
