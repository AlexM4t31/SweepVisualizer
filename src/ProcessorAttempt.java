import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ProcessorAttempt implements ActionListener {

    private JButton button;
    private JFileChooser filechooser;

    private File currentFile;

    private JPanel varsPanel, topPanel, valuePanel;

    private ImagePanel imagePanel;

    ProcessorAttempt(JPanel topPanel, JPanel varsPanel, ImagePanel imagePanel, JPanel valuePanel) {

        this.varsPanel = varsPanel;
        this.imagePanel = imagePanel;
        this.topPanel = topPanel;
        this.valuePanel = valuePanel;

        filechooser = new JFileChooser();
        filechooser.setMultiSelectionEnabled(true);

    }

    private int[] getDataParams(BufferedReader bReader){

        int[] r = {0, 0};

        try {
            for (int i = 0; i < 6; i++) {
                bReader.readLine();
            } // get rid of unnecessary first lines

            String line = bReader.readLine().replaceAll("\"", "");
            String[] split_line = line.split(",");

            int l = split_line.length;
            int runs = 60;//Integer.parseInt(split_line[l - 1]);
            int spacing = 31;//(l - 1) / runs - 1;

            r = new int[]{runs, spacing};

        } catch ( Exception exc ) {
            System.out.println("CSV reader failed reading initial lines");
        }

        return r;
    }
    private ArrayList[] readValues(BufferedReader bReader, int runs_start, int runs_end, int spacing){

        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        ArrayList<String> varNames = new ArrayList<>();

        for (int i = 0; i <= runs_end; i++) {
            columns.add(new ArrayList<String>());

        }
        System.out.println(spacing);

        for (int i = runs_start; i <= runs_end; i++) {

            columns.get(i).add(Integer.toString(i+1)); // It is fine to fill in the run number rather than from the file
        }

        varNames.add("[run number]"); // It is also necessary to add the run number var by hand.

        try {

            ArrayList<String> lines = new ArrayList<String>();

            String line = bReader.readLine().replaceAll("\"","");

            while (line != null && !(line.startsWith("[total steps]"))) {
                lines.add(line);
                line = bReader.readLine().replaceAll("\"", "");
            }

            Collections.sort(lines);

            String[] split_line = null;

            int n = lines.size();

            for(int i=0; i < n; i++){
                split_line = lines.get(i).split(",");

                varNames.add(split_line[0]);

                for (int j = 0; j * spacing + j + 1 < split_line.length; j++) {
                    columns.get(j).add(split_line[j * spacing + j + 1]);
                }
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

    private HashMap<Integer,Result> getResults(BufferedReader bReader, int spacing) {

        HashMap<Integer,Result> results = new HashMap<>();

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

                    results.put( i + 1, new Result(Integer.toString(i + 1), imageName, tmparr));
                }

            }
        } catch ( Exception exc ) {
                System.out.println("App was not able to read experiment results");
                results = null;
        }

        return results;

    }

    private ArrayList<Map<String, Set<Result>>> getStructuredResults(ArrayList<ArrayList<String>> columns, HashMap<Integer,Result> results, int runs_start, int runs_end) {

        int n_vars = columns.get(0).size();

        ArrayList<Map<String, Set<Result>>> structuredResults = new ArrayList<>();

        for ( int i=0; i<n_vars; i++ )
            structuredResults.add(new HashMap<String, Set<Result>>());

        for ( int i=0; i<n_vars; i++ ){
            for ( int j=0; j < runs_end - runs_start + 1; j++ )
            {
                String currVal = columns.get(j).get(i);
                Set<Result> tmpSet = structuredResults.get(i).get(currVal);
                if ( tmpSet != null )
                {
                    tmpSet.add( results.get(j) );
                }
                else {
                    Set<Result> newSet = new HashSet<Result>();
                    newSet.add( results.get(j) );
                    structuredResults.get(i).put( currVal, newSet );
                }
            }
        }

        return structuredResults;

    }

    private void addToStructuredResults(ArrayList<Map<String, Set<Result>>> structuredResults, ArrayList<ArrayList<String>> columns, HashMap<Integer,Result> results, int runs_start, int runs_end) {

        int n_vars = columns.get(0).size();

        for ( int i=0; i<n_vars; i++ ){
            for ( int j=0; j < runs_end - runs_start + 1 ; j++ )
            {
                String currVal = columns.get(j).get(i);
                Set<Result> tmpSet = structuredResults.get(i).get(currVal);
                if ( tmpSet != null )
                {
                    tmpSet.add( results.get(j) );
                }
                else {
                    Set<Result> newSet = new HashSet<Result>();
                    newSet.add( results.get(j) );
                    structuredResults.get(i).put( currVal, newSet );
                }
            }
        }

    }

    private void processFiles(File[] myFiles){

        ArrayList<Map<String, Set<Result>>> structuredResults = null;

        ArrayList<String> retValueNames = null;
        ArrayList<String> retVarNames = null;
        HashMap<Integer,Result> retResults = null;

        for (int fileindex = 0; fileindex < myFiles.length; fileindex++) {

            currentFile = myFiles[fileindex];

            String myFileName = currentFile.getName();

            int ch_ind = 0;
            while (ch_ind < myFileName.length())
            {
                if (Character.isDigit(myFileName.charAt(ch_ind)))
                    break;

                ch_ind += 1;
            }

            int dot_ind = myFileName.indexOf('.');

            String file_number = myFileName.substring(ch_ind, dot_ind);
            int int_file_number = Integer.parseInt(file_number);
            System.out.println("file_number: " + int_file_number);


            BufferedReader bReader = null;
            try {
                bReader = new BufferedReader(new FileReader(currentFile));
            } catch (IOException exc) {
                // add popup
                bReader = null;
                System.out.println("The app was not able to open the provided file.");
            }

            ArrayList<ArrayList<String>> columns = null;

            ArrayList<String> varNames = null;

            ArrayList<String> valueNames = null;

            HashMap<Integer,Result> results = null;

            int runs_start = int_file_number * 60 + 1 ;
            int runs_end = ( int_file_number + 1) * 60 ;

            int spacing = 0;

            if (bReader != null) {
                int[] dataParams = getDataParams(bReader);
                spacing = dataParams[1];
            }

            if (runs_start > 0) {
                ArrayList[] returnedValues = readValues(bReader, runs_start, runs_end, spacing);
                columns = returnedValues[0];
                varNames = returnedValues[1];
                System.out.println("varNames: " + varNames);
            }

            if (columns != null) {
                valueNames = getValueNames(bReader, spacing);

            }

            if (valueNames != null) {
                results = getResults(bReader, spacing);
            }

            if (fileindex == 0)
            {
                structuredResults = getStructuredResults(columns, results, runs_start, runs_end);

            }
            else {
                addToStructuredResults(structuredResults, columns, results, runs_start, runs_end);
            }

            if (fileindex == 0){
                retValueNames = valueNames;
                retVarNames = varNames;
                retResults = results;
            }
            //displayStructuredResults(structuredResults, varNames);
        }

        //displayStructuredResults(structuredResults, retVarNames);

        Browser browser = new Browser(varsPanel, valuePanel, imagePanel, retValueNames, retVarNames, structuredResults, retResults, currentFile);

        browser.setupBrowsing();

    }

    private static int getRuns(int runs) {
        return runs;
    }

    public void actionPerformed( ActionEvent e ) {
        int r = filechooser.showOpenDialog(topPanel);

        if ( r == JFileChooser.APPROVE_OPTION ){

            File[] currentFiles = filechooser.getSelectedFiles();
            processFiles(currentFiles);
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
                    resultString = resultString + "   " + tmpResult.getImageName();
                }
                System.out.println(resultString);

            }

        }

    }


}
