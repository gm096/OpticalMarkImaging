import FileIO.PDFHelper;
import Filters.AnswerFilter;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);

        ArrayList<PImage> pdf = PDFHelper.getPImagesFromPdf(pathToPdf);

        ArrayList<ArrayList<String>> allAnswers = new ArrayList<>();
        AnswerFilter key = new AnswerFilter();
        key.processImage(new DImage(pdf.get(0)), allAnswers.get(0));

        System.out.println(allAnswers.get(0));


//        for (PImage page : pdf) {
//            DImage image = new DImage(page);
//            ArrayList<String> answers = new ArrayList<>();
//
////            AnswerFilter filter = new AnswerFilter();
////            filter.processImage(image, answers);
//
//        }
    }

    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }
}

