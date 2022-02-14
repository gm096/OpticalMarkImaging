# Optical Mark Reader
A simple scantron reader for grading the default images (doesn't currently support other forms)

## How to run it!
1. First run `FilterTest.java` and load up the **DAnswerFilter** (press f to choose a filter)
    - Input the number of columns, rows and bubbles per question
    - Hit S to load the downsampled image
    - Click on the upper left corner of the question
    - Click on the bottom right corner of the question
    - Hit P to unpause and filter and the load the boxes
      - The first question box will be tinted
    - If the current boxes aren't perfect, hit R and reclick on 2 points to create the boxes
    - Once you've gotten the desired boxes, hit C to save the locations to a format.csv
2. Once the format has been generated, run `OpticalMarkReaderMain.java`
    - The scores will be created in an `answers.csv`
    - An analysis will be generated in `itemanalysis.csv`
