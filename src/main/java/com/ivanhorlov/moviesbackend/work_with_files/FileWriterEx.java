package com.ivanhorlov.moviesbackend.work_with_files;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterEx {
    public static void main(String[] args) throws IOException {
        String rubai = "Много лет размышлял я над жизнью земной.\n" +
                "Непонятного нет для меня под луной.\n" +
                "Мне известно, что мне ничего не известно! — \n" +
                "Вот последняя правда, открытая мной.\n";

        String s = "Privet";

        FileWriter writer = null;

        try{
            writer = new FileWriter("test1.txt", true);

//            for (int i = 0; i < rubai.length(); i++){
//                writer.write(rubai.charAt(i));
//            }

            writer.write(s);

            System.out.println("Done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            writer.close();
        }
    }
}
