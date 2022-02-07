import FileIO.PDFHelper;
import Filters.AnswerFilter;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);

        ArrayList<PImage> pdf = PDFHelper.getPImagesFromPdf(pathToPdf);
        ArrayList<ArrayList<String>> allAnswers = new ArrayList<>();
        for (PImage page : pdf) {
            DImage image = new DImage(page);
            ArrayList<String> answers = new ArrayList<>();

            AnswerFilter filter = new AnswerFilter();
            filter.processImage(image, answers);
            allAnswers.add(answers);
    }
        ArrayList<String> answerKey = allAnswers.get(0);
        outputCSV(allAnswers, answerKey);
    }

    private static void outputCSV(ArrayList<ArrayList<String>> allAnswers, ArrayList<String> answerKey) {
        ArrayList<ArrayList<Boolean>> testGrades = new ArrayList<>();
        for (int i = 0; i < allAnswers.size(); i++) {
            ArrayList<Boolean> testGrade = new ArrayList<>();
            for (int j = 0; j < allAnswers.get(i).size(); j++) {
                if(allAnswers.get(i).get(j).equals(answerKey.get(j))){
                    testGrade.add(true);
                }
                else{
                    testGrade.add(false);
                }
            }
            testGrades.add(testGrade);
        }
        try{
            PrintWriter out = new PrintWriter(new FileWriter("answers.csv"));
            for (ArrayList<Boolean> testGrade:testGrades) {
                int counter = 0;
                for (Boolean answer:
                     testGrade) {
                    if(answer == true){
                        counter++;
                        out.print(answer + ", ");
                    }
                    else{
                        out.print(answer + ", ");
                    }
                }
                out.println(counter + "/" + testGrade.size() + ", " + (double)counter/testGrade.size());
            }
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        outputItemAnalysis(testGrades);
    }

    private static void outputItemAnalysis(ArrayList<ArrayList<Boolean>> testGrades) {
        int[] itemAnalysis = new int[testGrades.get(0).size()];
        for (int i = 1; i < testGrades.size(); i++) {
            for (int j = 0; j < testGrades.get(i).size(); j++) {
                if(testGrades.get(i).get(j) == false){
                    itemAnalysis[j]++;
                }
            }
        }
        try{
            PrintWriter out = new PrintWriter(new FileWriter("itemanalysis.csv"));
            for (int i = 0; i < itemAnalysis.length; i++) {
                out.println(i + ", " + itemAnalysis[i]);
            }
            out.close();
        }catch(Exception e){
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
