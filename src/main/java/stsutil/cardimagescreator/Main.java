package stsutil.cardimagescreator;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        try {
            CardImagesCreator cardImagesCreator = new CardImagesCreator();

            File folder = new File("cards");
            File[] listOfFiles = folder.listFiles();

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    try {
                        cardImagesCreator.createCardImages(file);
                        System.out.println("Correctly processed file: " + file.getName());
                    } catch (Exception e){
                        System.out.println("Exception during processing file " + file.getName());
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Exception during Main method ");
            e.printStackTrace();
        }

    }

}
