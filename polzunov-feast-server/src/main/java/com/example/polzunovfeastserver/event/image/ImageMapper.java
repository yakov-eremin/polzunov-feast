package com.example.polzunovfeastserver.event.image;

import com.example.polzunovfeastserver.event.image.entity.ImageEntity;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public final class ImageMapper {
    private ImageMapper() {
        throw new UnsupportedOperationException("This is an utility class");
    }

    public static Set<String> toImageUrls(ImageEntity mainImageEntity, Set<ImageEntity> imageUrls) {
        var set = new LinkedHashSet<String>(imageUrls.size() + 1);
        if (mainImageEntity != null) {
            set.add(mainImageEntity.getUrl());
        }
        set.addAll(imageUrls.stream().map(ImageEntity::getUrl).collect(toSet()));
        return set;
    }
}
