import FileIO.PDFHelper;
import Filters.AnswerFilter;
import Filters.Bubble;
import Filters.FixedThresholdFilter;
import Filters.DisplayInfoFilter;
import Interfaces.PixelFilter;
import Filters.*;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;

import java.util.ArrayList;

public class FilterTest {
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {


        RunTheFilter();
        SaveAndDisplayExample();

    }

    private static void RunTheFilter() {
        System.out.println("Loading pdf....");
        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
        DImage img = new DImage(in);       // you can make a DImage from a PImage

        System.out.println("Running filter on page 1....");

        ArrayList<String> answers = new ArrayList<>();

        AnswerFilter aFilter = new AnswerFilter();
        aFilter.processImage(img, answers);
    }

    private static void SaveAndDisplayExample() {
        PImage img = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
        DImage image = new DImage(img);
        image = new AnswerFilterBubbleSearch().processImage(image);
        PImage img2 = image.getPImage();
        img2.save(currentFolder + "assets/page1.png");
        DisplayWindow.showFor("assets/page1.png");
    }


}

//import FileIO.PDFHelper;
//import Filters.AnswerFilter;
//import core.DImage;
//import core.DisplayWindow;
//import processing.core.PImage;
//
//import java.util.ArrayList;
//
//public class FilterTest {
//    public static String currentFolder = System.getProperty("user.dir") + "/";
//
//    public static void main(String[] args) {
//        DisplayWindow.showFor("assets/page1.png", 1000, 1000);
//
//        // RunTheFilter();
//    }
//
//    private static void RunTheFilter() {
//        // System.out.println("Loading pdf....");
//        PImage in = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
//        DImage img = new DImage(in);       // you can make a DImage from a PImage
//
//        // System.out.println("Running filter on page 1....");
//
//        ArrayList<String> answers = new ArrayList<>();
//
//        AnswerFilter aFilter = new AnswerFilter();
//        aFilter.processImage(img, answers);
//
//        checkAnswers(answers);
//    }
//
//    private static void checkAnswers(ArrayList<String> answers) {
//        String[] writtenAnswers = {
//                "A", "C", "E", "B", "B", "B", "E", "A", "E", "D", "E", "C", "A", "A", "B", "D", "E", "E", "B", "A", "D", "E", "B", "D", "B",
//                "A", "B", "A", "B", "E", "B", "D", "A", "B", "E", "B", "D", "D", "D", "D", "A", "E", "D", "A", "E", "C", "C", "C", "C", "C",
//                "C", "C", "C", "C", "C", "C", "A", "D", "B", "C", "E", "C", "B", "A", "D", "C", "B", "A", "D", "E", "C", "B", "B", "A", "A",
//                "E", "E", "D", "B", "A", "C", "D", "E", "A", "B", "C", "D", "E", "B", "C", "A", "E", "D", "B", "E", "A", "D", "A", "D", "E"
//        };
//        int c = 0;
//
//        for (int i = 0; i < 100; i++) {
//            if (answers.get(i).equals(writtenAnswers[i])) c++;
//            else System.out.println((i + 1) + ": " + writtenAnswers[i] + " -> " + answers.get(i));
//        }
//
//        System.out.println(c + "% correct");
//    }
//
//    private static void SaveAndDisplayExample() {
//        PImage img = PDFHelper.getPageImage("assets/omrtest.pdf", 1);
//        img.save(currentFolder + "assets/page1.png");
//
//        DisplayWindow.showFor("assets/page1.png");
//    }
//
//
//}

