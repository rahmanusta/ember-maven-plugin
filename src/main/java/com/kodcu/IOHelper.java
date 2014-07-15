package com.kodcu;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by usta on 15.07.2014.
 */
public class IOHelper {


    public static String toString(InputStream inputStream) {

        String temp = "";
        try {
            temp = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(inputStream);
        return temp;
    }

    public static String toString(String input) {

        InputStream inputStream = IOHelper.class.getResourceAsStream(input);
        String temp = "";
        try {
            temp = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IOUtils.closeQuietly(inputStream);
        return temp;
    }

    public static byte[] readAllBytes(Path path) {
        byte[] temp = new byte[]{};
        try {
            temp = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
