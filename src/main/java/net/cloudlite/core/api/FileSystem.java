package net.cloudlite.core.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class FileSystem {

    public static JsonObject getJsonData(@Nonnull final File file) {
        try {
            return new JsonParser().parse(new FileReader(file)).getAsJsonObject();
        } catch (@Nonnull final FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
