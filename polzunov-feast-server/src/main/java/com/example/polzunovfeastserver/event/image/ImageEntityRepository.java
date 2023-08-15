package com.example.polzunovfeastserver.event.image;

import com.example.polzunovfeastserver.event.image.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ImageEntityRepository extends JpaRepository<ImageEntity, Long> {
    @Query("SELECT i FROM ImageEntity i WHERE i.url IN :urls")
    Set<ImageEntity> findAllByUrl(Iterable<String> urls);

    Optional<ImageEntity> findByUrl(String imageUrl);
}
