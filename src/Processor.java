import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.*;
import java.sql.Array;
import java.util.*;
import java.io.BufferedReader;

public class Processor implements ActionListener {

    private static final int NUMBER_OF_PARAMETERS = 4;
    private static final int NUMBER_OF_METRICS = 2;

    private JButton button;
    private JFileChooser filechooser;

    private File currentFile;

    private JPanel varsPanel, topPanel, valuePanel;

    private ImagePanel imagePanel;

    Processor(JPanel topPanel, JPanel varsPanel, ImagePanel imagePanel, JPanel valuePanel) {

        this.varsPanel = varsPanel;
        this.imagePanel = imagePanel;
        this.topPanel = topPanel;
        this.valuePanel = valuePanel;

        filechooser = new JFileChooser();
        filechooser.setMultiSelectionEnabled(true);

    }

    private ArrayList<Object> newPrepareFileReading(File file) {
        ArrayList<Object> resArrList = new ArrayList<>();

        // number of files is ONE. 1. UNO
        // I'll worry about edge cases, ways to fuck this function up just a bit later

        BufferedReader bReader = null;

        try {
            bReader = new BufferedReader(new FileReader(file));
        } catch (IOException exc) {
            System.out.println("The app was not able to open the provided file.");
            return resArrList;
        }

        try {

            // I think runs doesn't have any reason to be here at this time
            // in the 'past' it was very useful bcs we were using multiple files, and it was the no of
            // param combos per file
            // now, we only have a need for an overall runs sort of deal
            // yeah
            // maybe for the sake of separation of versions, conceptually, it would be wise
            // to use a new term i.e instances or something
            // also we don't have any use for spacing anymore, too

            // nice, so all of that gets deleted.

            String namesLine = bReader.readLine().replaceAll("\"", "");
            String[] splitNames = namesLine.split(",");
            // id, paramnames, metricnames, imagename
            // how many paramNames? hardcoded


            ArrayList<String> varNames = MyUtilities.subListFromArray(splitNames, 0, 1 + NUMBER_OF_PARAMETERS);
            ArrayList<String> valNames = MyUtilities.subListFromArray(splitNames, 1 + NUMBER_OF_PARAMETERS, 1 + NUMBER_OF_PARAMETERS + NUMBER_OF_METRICS);

            resArrList.add(varNames);
            resArrList.add(valNames);

            return resArrList;

        } catch (Exception exc) {
            System.out.println("CSV reader failed reading initial lines");
            return resArrList;
        }

    }

    private ArrayList<Object> prepareFileReading(File[] files){

        ArrayList<Object> resArrList = new ArrayList<>();

        int numberOfFiles = files.length;

        if ( numberOfFiles == 0 )
        {
            System.out.println("No files were selected.");

            return resArrList;
        }
        else
        {
            BufferedReader bReader = null;

            try {
                bReader = new BufferedReader(new FileReader(files[0]));
            } catch (IOException exc) {
                System.out.println("The app was not able to open the provided file.");
                return resArrList;
            }

            try {
                for (int i = 0; i < 6; i++) {
                    bReader.readLine();
                } // get rid of unnecessary first lines

                String line = bReader.readLine().replaceAll("\"", "");

                //System.out.print("That specific line: " + line);

                String[] split_line = line.split(",");


                int startRunNo = Integer.parseInt(split_line[1]);
                int l = split_line.length;
                int endRunNo = Integer.parseInt(split_line[l-1]);
                int runs = endRunNo - startRunNo + 1;

                int spacing = (l - 1) / runs;

                ArrayList<String> varNames = new ArrayList<>();

                line = bReader.readLine().replaceAll("\"","");

                //System.out.println("First var line: " + line);

                while (line != null && !(line.startsWith("[total steps]"))) {
                    int commaPos = line.indexOf(',');
                    String tmpVarName = line.substring(0, commaPos);
                    varNames.add(tmpVarName);

                    line = bReader.readLine().replaceAll("\"","");
                }

                varNames.sort(Comparator.naturalOrder());

                varNames.add(0, "[run number]");

                ArrayList<ArrayList<String>> columns = new ArrayList<>();

                for (int i = 0; i < runs * numberOfFiles; i++) {
                    ArrayList<String> tmpCol = new ArrayList<>();
                    String runNo = Integer.toString(i);

                    tmpCol.add(runNo);

                    columns.add(tmpCol);
                }

                ArrayList<String> valNames = new ArrayList<>();

                bReader.readLine(); // skip empty line

                String[] splitValNamesLine = bReader.readLine().replaceAll("\"","").split(",");

                for ( int i=1; i<=spacing; i++){
                    valNames.add(splitValNamesLine[i]);
                }

                resArrList.add(numberOfFiles);
                resArrList.add(runs);
                resArrList.add(spacing);
                resArrList.add(varNames);
                resArrList.add(valNames);
                resArrList.add(columns);

                return resArrList;

            } catch (Exception exc) {
                System.out.println("CSV reader failed reading initial lines");
                return resArrList;
            }

        }
    }

    private void newReadValuesAndResults(File file, ArrayList<ArrayList<String>> paramValues, HashMap<Integer, Result> results, int noOfVars){

        BufferedReader bReader = null;

        try {
            bReader = new BufferedReader(new FileReader(file));
        } catch (IOException exc) {

            paramValues = new ArrayList<>();
            System.out.println("The app was not able to open the provided file.");
        }

        try {

            String crtLine = null;

            String x = bReader.readLine(); // skip parameter name line
            System.out.println("x: " + x);

            //while ( (crtLine = bReader.readLine()) != null )
            crtLine = bReader.readLine();

            while ( crtLine != null )
            {

                String[] splitCrtLine = crtLine.replaceAll("\"","").split(",");

                try {
                    paramValues.add(MyUtilities.subListFromArray(splitCrtLine, 0, 1 + NUMBER_OF_PARAMETERS));

                    ArrayList<String> tmpResValues = MyUtilities.subListFromArray(splitCrtLine,1 + NUMBER_OF_PARAMETERS, 1 + NUMBER_OF_PARAMETERS + NUMBER_OF_METRICS);

                    int arrLen = splitCrtLine.length;
                    String imageName = splitCrtLine[arrLen-1];
                    String newResNo = splitCrtLine[0];

                    System.out.println("newResNo: " + newResNo + " for " + crtLine);

                    results.put(Integer.parseInt(newResNo), new Result(newResNo, imageName, tmpResValues));

                    crtLine = bReader.readLine();
                } catch (Exception exc) {
                    System.out.println("Uhhh problem: " + exc.toString());
                }


            }

        } catch (IOException exc) {
            System.out.println("nu merge done");
        }


    }

    private void readValuesAndResults(File[] files, ArrayList<ArrayList<String>> columns, ArrayList<Result> results, int noOfVars, int runs, int spacing){


        for ( int fInd = 0; fInd < files.length; fInd++ ){

            BufferedReader bReader = null;

            try {
                bReader = new BufferedReader(new FileReader(files[fInd]));
            } catch (IOException exc) {

                columns = new ArrayList<>();
                System.out.println("The app was not able to open the provided file.");
            }

            try {

                // noOfVars also includes runNumber which we treat separately so need to use noOfVars - 1


                // getting rid of the unnecessary lines + the run number line
                for (int i = 0; i < 7; i++)
                {
                    String tmpLine = bReader.readLine();
                }

                ArrayList<String> varLines = new ArrayList<>();

                for (int i=0; i < (noOfVars - 1) ; i++)
                {
                    String tmpVarLine = bReader.readLine();
                    varLines.add(tmpVarLine);
                }

                try {
                    varLines.sort(Comparator.naturalOrder());
                } catch(NullPointerException e){
                    System.out.println("varLines that fucked it up: " + varLines);
                }

                System.out.println(varLines.get(0));
                System.out.println(varLines.get(1));

                ArrayList<String[]> splitVarLines = new ArrayList<>();

                for (int i=0; i<varLines.size(); i++)
                {
                    splitVarLines.add(varLines.get(i).replaceAll("\"","").split(","));
                }

                for (int i=0; i<runs; i++){
                    for (int j=0; j<splitVarLines.size(); j++)
                    {
                        columns.get(fInd*runs + i).add(splitVarLines.get(j)[1 + spacing*i]);
                    }
                }

                for (int i=0; i<3; i++ ){
                    bReader.readLine();
                }

                String[] resSplitLine = bReader.readLine().replaceAll("\"","").split(",");

                for(int i=0; i<runs; i++){

                    ArrayList<String> tmpResArr = new ArrayList<>();

                    for (int j=0; j<spacing; j++)
                    {
                        tmpResArr.add( resSplitLine[ spacing * i + 1 + j ] );
                    }

                    String imageName = tmpResArr.get(spacing - 1);

                    String newResNo = Integer.toString(results.size() ); // ugh, the indexing 

                    results.add(new Result(newResNo, imageName, tmpResArr));
                }

            } catch (IOException exc) {
                System.out.println("nu merge domne");
            }

        }
    }

    private ArrayList[] legacyReadValues(BufferedReader bReader, int runs, int spacing){
        //


        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        ArrayList<String> varNames = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            columns.add(new ArrayList<String>());
            columns.get(i).add(Integer.toString(i+1)); // It is fine to fill in the run number rather than from the file
        }

        varNames.add("[run number]"); // It is also necessary to add the run number var by hand.

        try {

            String line = bReader.readLine().replaceAll("\"","");
            String[] split_line = null;

            while (line != null && !(line.startsWith("[total steps]"))) {

                split_line = line.split(",");

                varNames.add(split_line[0]);

                // be mindful of the fact that each line starts with the name of the variable, thus the first variable value is at pos 1
                // here
                for (int i = 0; i * spacing + i + 1 < split_line.length; i++) {
                    columns.get(i).add(split_line[i * spacing + i + 1]);
                }

                line = bReader.readLine().replaceAll("\"", "");

            }


        } catch ( Exception exc ) {
            // add popup
            columns = null;
            varNames = null;
            System.out.println("The extension not able to read the run parameter values from the provided file");
        }

        ArrayList[] r = {columns, varNames};

        return r;
    }

    private ArrayList<String> getValueNames(BufferedReader bReader, int spacing) {

        ArrayList<String> valueNames = new ArrayList<>();

        try {
            bReader.readLine(); // skip empty line
            String line = bReader.readLine().replaceAll("\"",""); // get results line
            String[] split_line = line.split(",");


            for ( int i=1; i<=spacing+1; i++){
                valueNames.add(split_line[i]);
            }
        } catch ( Exception exc ) {
            valueNames = null;
            System.out.println("Values names could not be read");
        }

        return valueNames;
    }

    private ArrayList<Result> getResults(BufferedReader bReader, int spacing) {

        ArrayList<Result> results = new ArrayList<>();

        try {

            String line = bReader.readLine().replaceAll("\"", "");
            String[] split_line = line.split(",");

            for (int i = 0; i * spacing + i + 1 < split_line.length; i++) {
                {
                    ArrayList<String> tmparr = new ArrayList<>();
                    String imageName = split_line[i * spacing + i + spacing + 1];

                    for (int j = i * spacing + i + 1; j < i * spacing + i + spacing + 2; j++) {
                        tmparr.add(split_line[j]);
                    }

                    results.add(new Result(Integer.toString(i + 1), imageName, tmparr)); // careful w that i + 1
                }

            }
        } catch ( Exception exc ) {
            System.out.println("App was not able to read experiment results");
            results = null;
        }

        return results;

    }

    private ArrayList<Map<String, Set<Result>>> getStructuredResults(ArrayList<ArrayList<String>> idAndParamsRows, HashMap<Integer, Result> results) {

        // NOW WE TURN PARAMS TO COLUMNS, DON'T WE?

        // n_cols gets the number of elements in a given row. as far as I can guess it'll always be the same, so
        // this should be chill, and, given the current setup, will be equal to 4

        int nCols = idAndParamsRows.get(0).size(); // take first row of whatever 'params' contains, get its size

        int nRows = idAndParamsRows.size();

        ArrayList<Map<String, Set<Result>>> structuredResults = new ArrayList<>();

        for ( int i=0; i< nCols; i++ )
            structuredResults.add(new HashMap<String, Set<Result>>());

        // the question is exceedingly simple:
        // does the result row index - result id mismatch affect things whatsoever in the following fragment of code?
        // and the answer is: yes!
        // because here I'm getting the result with the index of the result row,
        // rather than with the parsed result Id. so let's fix that.

        // the index variable names (i,j) are used in the opposite way from normal
        // so, i goes with cols, j goes with rows.
        for ( int i=0; i < nCols; i++ ){
            for ( int j=0; j < nRows; j++ )
            {
                String strResId = idAndParamsRows.get(j).get(0);

                int parsedResId = Integer.parseInt(strResId);

                String currVal = idAndParamsRows.get(j).get(i);

                Set<Result> tmpSet = structuredResults.get(i).get(currVal);

                if ( tmpSet != null )
                {
                    tmpSet.add( results.get(parsedResId) );
                }
                else {
                    Set<Result> newSet = new HashSet<Result>();
                    newSet.add( results.get(parsedResId) );
                    structuredResults.get(i).put( currVal, newSet );
                }
            }
        }

        return structuredResults;

    }

    private void printVarNames( ArrayList<String> varNames ){

        String varNamesStr = "";
        for (int i=0; i<varNames.size(); i++){
            varNamesStr = varNamesStr + " " + varNames.get(i);
        }

        System.out.println(varNamesStr);
    }

    private void printColStr( ArrayList<ArrayList<String>> columns, int colInd){
        ArrayList<String> tmpCol = columns.get(colInd);

        String tmpStr = "";

        for ( int i=0; i<tmpCol.size(); i++){
            tmpStr = tmpStr + tmpCol.get(i) + " ";
        }

        System.out.println(tmpStr);
    }

    private void printResStr( ArrayList<Result> results, int resInd){

        Result tmpRes = results.get(resInd);

        ArrayList<String> tmpResValues = tmpRes.getValues();

        String resValString = "";

        for (int i=0; i<tmpResValues.size(); i++){
            resValString = resValString + tmpResValues.get(i) + " ";
        }

        System.out.println(resValString);
    }

    private void processFiles(File file) {

        ArrayList<Object> res = newPrepareFileReading(file);

        ArrayList<String> varNames = (ArrayList<String>) res.get(0);
        ArrayList<String> valNames = (ArrayList<String>) res.get(1);
        ArrayList<ArrayList<String>> paramValues = new ArrayList<ArrayList<String>>();

        HashMap<Integer, Result> results = new HashMap<>();

//            System.out.println("Number of files: " + numberOfFiles);
//            System.out.println("spacing: " + spacing);
//            System.out.println("runs: " + runs);
//
//            String varNamesStr = "";
//            for (int i = 0; i < varNames.size(); i++) {
//                varNamesStr = varNamesStr + varNames.get(i) + " ";
//            }
//            System.out.println(varNamesStr);
//
//            String valNamesStr = "";
//            for (int i = 0; i < valNames.size(); i++) {
//                valNamesStr = valNamesStr + valNames.get(i) + " ";
//            }
//            System.out.println(valNamesStr);
//

        newReadValuesAndResults(file, paramValues, results, varNames.size());

        //System.out.println("Printing result string for result with index 0.");
        //printResStr(results, 0);

//        if ( true ){
//
//            System.out.println("Varnames: ");
//            printVarNames(varNames);
//
//            System.out.println("");
//            System.out.println("Let's show the col + result combos of the first two runs of the first two files ");
//
//            System.out.println("");
//            System.out.println("Run 1:");
//            printColStr(columns, 0);
//            printResStr(results, 0);
//
//            System.out.println("Run 2:");
//            printColStr(columns, 1);
//            printResStr(results, 1);
//
//            if (numberOfFiles > 1) {
//                System.out.println("Run " + Integer.toString(runs) + ":");
//                printColStr(columns, runs);
//                printResStr(results, runs);
//
//                System.out.println("Run " + Integer.toString(runs + 1) + ":");
//                printColStr(columns, runs + 1);
//                printResStr(results, runs + 1);
//            }
//        }


        ArrayList<Map<String,Set<Result>>> structuredResults = getStructuredResults(paramValues, results);

        //displayStructuredResults(structuredResults, varNames);

        Browser browser = new Browser(varsPanel, valuePanel, imagePanel, valNames, varNames, structuredResults, results, file);

        browser.setupBrowsing();

        }

//    private void legacyProcessFiles(File[] files){
//
//        BufferedReader bReader = null;
//        try {
//            bReader = new BufferedReader(new FileReader(currentFile));
//        } catch ( IOException exc ) {
//            // add popup
//            bReader = null;
//            System.out.println("The app was not able to open the provided file.");
//        }
//
//        ArrayList<ArrayList<String>> columns = null;
//
//        ArrayList<String> varNames = null;
//
//        ArrayList<String> valueNames= null;
//
//        ArrayList<Result> results = null;
//
//        ArrayList<Map<String,Set<Result>>> structuredResults = null;
//
//        int runs = 12960;
//        int spacing = 31;
//
////        if ( bReader!= null ) {
////            int[] dataParams = getDataParams(bReader);
////            runs = dataParams[0];
////            spacing = dataParams[1];
////        }
//
//        if ( runs > 0 )
//        {
//            ArrayList[] returnedValues = readValues(bReader, runs, spacing);
//            columns = returnedValues[0];
//            varNames = returnedValues[1];
//        }
//
//        if ( columns!= null )
//        {
//            valueNames = getValueNames(bReader, spacing);
//        }
//
//        if ( valueNames!= null )
//        {
//            results = getResults(bReader, spacing);
//        }
//
//        structuredResults = getStructuredResults(columns, results, runs);
//
//        displayStructuredResults(structuredResults, varNames);
//
//        Browser browser = new Browser(varsPanel, valuePanel, imagePanel, valueNames, varNames, structuredResults, results, currentFile);
//
//        browser.setupBrowsing();
//
//    }

    public void actionPerformed( ActionEvent e ) {
        int r = filechooser.showOpenDialog(topPanel);

        if ( r == JFileChooser.APPROVE_OPTION ){

            File[] currentFiles = filechooser.getSelectedFiles();
            // the files are sorted by their filenames by default

            processFiles(currentFiles[0]);
            //System.out.println(currentFile.getName());
        }

    }

    private void displayStructuredResults(ArrayList<Map<String,Set<Result>>> structuredResults, ArrayList<String> varNames) {

        for (int i=0; i<varNames.size(); i++){
            System.out.println(varNames.get(i));

            Set<String> valuesForVar = structuredResults.get(i).keySet();

            Iterator iter = valuesForVar.iterator();
            while ( iter.hasNext() ){
                String tmpValue = (String) iter.next();
                System.out.println("   " + tmpValue);

                Set<Result> tmpResultSet = structuredResults.get(i).get(tmpValue);

                Iterator iterTwo = tmpResultSet.iterator();
                String resultString = "";
                while ( iterTwo.hasNext() )
                {
                    Result tmpResult = (Result) iterTwo.next();
                    resultString = resultString + "   " + tmpResult;//.getImageName();
                }
                System.out.println(resultString);

            }

        }

    }


}
