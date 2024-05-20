package com.example.send.sendmail.test;

import java.io.FileWriter;
import java.io.IOException;

public class FileCreator {
    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("file.txt");
            writer.write("This is the content of the file.");
            writer.close();
            System.out.println("File created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
