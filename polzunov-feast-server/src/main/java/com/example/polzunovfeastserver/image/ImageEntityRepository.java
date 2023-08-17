package com.example.polzunovfeastserver.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ImageEntityRepository extends JpaRepository<ImageEntity, Long> {
    @Query("SELECT i FROM ImageEntity i WHERE i.url IN :urls")
    Set<ImageEntity> findAllByUrl(Iterable<String> urls);
}
