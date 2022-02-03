import FileIO.PDFHelper;
import Filters.AnswerFilter;
import Filters.FixedThresholdFilter;
import Filters.DisplayInfoFilter;
import Interfaces.PixelFilter;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;

import java.util.ArrayList;

public class FilterTest {
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
        // DisplayWindow.showFor("assets/page1.png");

        RunTheFilter();
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
        img.save(currentFolder + "assets/page1.png");

        DisplayWindow.showFor("assets/page1.png");
    }


}
