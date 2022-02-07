import FileIO.PDFHelper;
import Filters.AnswerFilter;
import core.DImage;
import jogamp.graph.font.typecast.ot.table.ID;
import processing.core.PImage;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = "assets/omrtest.pdf";
        System.out.println("Loading pdf at " + pathToPdf);

        ArrayList<PImage> pdf = PDFHelper.getPImagesFromPdf(pathToPdf);
        ArrayList<ArrayList<String>> allAnswers = new ArrayList<>();
        ArrayList<ArrayList<String>> allIDs = new ArrayList<>();

        for (PImage page : pdf) {
            DImage image = new DImage(page);
            ArrayList<String> answers = new ArrayList<>();
            ArrayList<String> IDs = new ArrayList<>();

            AnswerFilter filter = new AnswerFilter();
            filter.processImage(image, answers, IDs);
            allAnswers.add(answers);
            allIDs.add(IDs);
        }
        ArrayList<String> answerKey = allAnswers.get(0);
        outputCSV(allAnswers, allIDs, answerKey);
    }

    private static void outputCSV(ArrayList<ArrayList<String>> allAnswers, ArrayList<ArrayList<String>> allIDs, ArrayList<String> answerKey) {
        ArrayList<ArrayList<Boolean>> testGrades = new ArrayList<>();
        for (int i = 0; i < allAnswers.size(); i++) {
            ArrayList<Boolean> testGrade = new ArrayList<>();
            ArrayList<String> currPage = allAnswers.get(i);

            for (int j = 0; j < currPage.size(); j++) {
                if (currPage.get(j).equals(answerKey.get(j))) testGrade.add(true);
                else testGrade.add(false);
            }

            testGrades.add(testGrade);
        }
        try {
            PrintWriter out = new PrintWriter(new FileWriter("answers.csv"));
            for (ArrayList<Boolean> testGrade : testGrades) {
                int counter = 0;
                int IDCounter = 0;
                out.println("Student ID: " + allIDs.get(IDCounter).get(0) + " Teacher ID: " + allIDs.get(IDCounter).get(1) + " ");
                IDCounter++;
                for (Boolean answer : testGrade) {
                    if (answer) counter++;
                    out.print(answer + ", ");
                }
                out.println(counter + "/" + testGrade.size() + ", " + (((double) counter / testGrade.size()) * 100) + "%");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputItemAnalysis(testGrades);
    }

    private static void outputItemAnalysis(ArrayList<ArrayList<Boolean>> testGrades) {
        int[] itemAnalysis = new int[testGrades.get(0).size()];
        for (int i = 1; i < testGrades.size(); i++) {
            for (int j = 0; j < testGrades.get(i).size(); j++) {
                if (testGrades.get(i).get(j) == false) {
                    itemAnalysis[j]++;
                }
            }
        }
        try {
            PrintWriter out = new PrintWriter(new FileWriter("itemanalysis.csv"));
            for (int i = 0; i < itemAnalysis.length; i++) {
                out.println(i + ", " + itemAnalysis[i]);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
