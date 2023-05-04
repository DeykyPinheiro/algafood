package com.apigaworks.algafood.util;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceUtils {

    public static String getContentFromResource(String resourceName) {
        try {
            return Files.readString(Paths.get(resourceName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}