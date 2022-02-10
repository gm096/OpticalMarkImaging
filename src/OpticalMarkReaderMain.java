import FileIO.PDFHelper;
import Filters.AnswerFilter;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = "assets/omrtest.pdf", pathToFormat = "format.csv";
        System.out.println("Loading pdf at " + pathToPdf);

        ArrayList<PImage> pdf = PDFHelper.getPImagesFromPdf(pathToPdf);
        ArrayList<ArrayList<String>> allAnswers = new ArrayList<>();
        ArrayList<ArrayList<String>> allIDs = new ArrayList<>();
        String[] input = readFormatCSV(pathToFormat).split(" ");

        for (PImage page : pdf) {
            DImage image = new DImage(page);
            ArrayList<String> answers = new ArrayList<>();
            ArrayList<String> IDs = new ArrayList<>();

            AnswerFilter filter = new AnswerFilter(input);
            filter.processImage(image, answers, IDs);
            allAnswers.add(answers);
            allIDs.add(IDs);
        }

        outputCSV(allAnswers, allIDs);
    }

    private static String readFormatCSV(String path) {
        String line = "";
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path));) {
            line = br.readLine();
        } catch (Exception errorObj) {
            System.err.println("There was a problem reading the " + path);
        }

        return line;
    }

    private static void outputCSV(ArrayList<ArrayList<String>> allAnswers, ArrayList<ArrayList<String>> allIDs) {
        ArrayList<ArrayList<Boolean>> testGrades = new ArrayList<>();

        gradeAnswer(allAnswers, testGrades);
        createCSV(testGrades, allIDs);
        outputItemAnalysis(testGrades);

        System.out.println("\nFinished grading and analysis is complete!");
    }

    private static void gradeAnswer(ArrayList<ArrayList<String>> allAnswers, ArrayList<ArrayList<Boolean>> testGrades) {
        ArrayList<String> answerKey = allAnswers.get(0);

        for (int i = 1; i < allAnswers.size(); i++) {
            ArrayList<Boolean> testGrade = new ArrayList<>();
            ArrayList<String> currPage = allAnswers.get(i);

            for (int j = 0; j < currPage.size(); j++) {
                if (currPage.get(j).equals(answerKey.get(j))) testGrade.add(true);
                else testGrade.add(false);
            }

            testGrades.add(testGrade);
        }
    }

    private static void createCSV(ArrayList<ArrayList<Boolean>> testGrades, ArrayList<ArrayList<String>> allIDs) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter("answers.csv"));
            int IDCounter = 0;
            for (ArrayList<Boolean> testGrade : testGrades) {
                int correct = 0;
                out.println("Student ID: " + allIDs.get(IDCounter).get(0) + " Teacher ID: " + allIDs.get(IDCounter).get(1) + " ");
                IDCounter++;

                for (Boolean answer : testGrade) {
                    if (answer) correct++;
                    out.print(answer + ", ");
                }

                out.println("\nCorrect Answers: " + correct + "/" + testGrade.size() + ", Score: " + (((double) correct / testGrade.size()) * 100) + "%\n");
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void outputItemAnalysis(ArrayList<ArrayList<Boolean>> testGrades) {
        int[] itemAnalysis = new int[testGrades.get(0).size()];
        for (int i = 1; i < testGrades.size(); i++) {
            for (int j = 0; j < testGrades.get(i).size(); j++) {
                if (!testGrades.get(i).get(j)) {
                    itemAnalysis[j]++;
                }
            }
        }
        try {
            PrintWriter out = new PrintWriter(new FileWriter("itemanalysis.csv"));
            for (int i = 0; i < itemAnalysis.length; i++) {
                if (itemAnalysis[i] != 0)
                    out.println("Question " + i + " was gotten wrong " + itemAnalysis[i] + " times.");
            }
            out.println("Analysis Complete!");
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
