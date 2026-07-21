package com.example.ilovecoffee.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PathConstant {
    public static final Path THUMBNAIL_DIRECTORY = Paths.get("thumbnails").toAbsolutePath().normalize();
}
