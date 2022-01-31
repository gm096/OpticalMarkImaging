    import FileIO.PDFHelper;
    import core.DImage;
    import core.DisplayWindow;
    import processing.core.PImage;

    import javax.swing.*;
    import java.io.File;
    import java.util.ArrayList;

    public class OpticalMarkReaderMain {
        public static void main(String[] args) {
            String pathToPdf = fileChooser();
            System.out.println("Loading pdf at " + pathToPdf);

            ArrayList<PImage> pdf = PDFHelper.getPImagesFromPdf(pathToPdf);
            ArrayList<String[]> answers = new ArrayList<>();
            for (PImage page : pdf) {
                DImage image = new DImage(page);
                String[] answer = AnswerFilter.processImage(image);
            }
            /*
            Your code here to...
            (1).  Load the pdf
            (2).  Loop over its pages
            (3).  Create a DImage from each page and process its pixels
            (4).  Output 2 csv files
             */

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
