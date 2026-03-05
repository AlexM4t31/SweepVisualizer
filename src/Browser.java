import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.util.*;

public class Browser extends JPanel {

    private VarChanger runNumberChanger;

    private JPanel varPanel;

    private ImagePanel imagePanel;

    private JPanel hiddenVarsPanel, valuePanel;
    private ArrayList<String> varNames, valueNames;

    private ArrayList<VarChanger> varChangers;
    private ArrayList<Map<String, Set<Result>>> structuredResults;

    private File currentFile;

    private HashMap<Integer, Result> results;

    public Browser( JPanel varPanel, JPanel valuePanel, ImagePanel imagePanel, ArrayList<String> valueNames, ArrayList<String> varNames, ArrayList<Map<String, Set<Result>>> structuredResults, HashMap<Integer,Result> results, File currentFile ){
        this.varNames = varNames;
        this.valueNames = valueNames;
        this.valuePanel = valuePanel;
        this.structuredResults = structuredResults;
        this.results = results;
        this.varChangers = new ArrayList<>();
        this.currentFile = currentFile;
        this.imagePanel = imagePanel;
        this.varPanel = varPanel;
    }

    public void setupBrowsing(){

        //JSeparator jSeparator = new JSeparator();

        runNumberChanger = new VarChanger(true,this, imagePanel, structuredResults.get(0).keySet(), varNames.get(0));
        varPanel.add( runNumberChanger );
        varChangers.add( runNumberChanger );
        //varPanel.add(new JSeparator());

        for ( int i=1; i<varNames.size(); i++ ){

            // jSeparator= new JSeparator();
            VarChanger v = new VarChanger(false, this, imagePanel, structuredResults.get(i).keySet(), varNames.get(i));
            varPanel.add( v );
            varChangers.add( v );
            //varPanel.add(jSeparator);
        }

        hiddenVarsPanel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(hiddenVarsPanel, BoxLayout.Y_AXIS);
        hiddenVarsPanel.setLayout(boxLayout);

        JLabel hiddenVarsLabel = new JLabel("Hidden variables: ");
        hiddenVarsPanel.add(hiddenVarsLabel);

        varPanel.add(hiddenVarsPanel);

        updateImage();
        updateValues();

        varPanel.revalidate();

        //runNumberChanger.getBtnAndBtnContainerParentChain();
        //runNumberChanger.printButtonUI();

        JFrame frame = (JFrame) SwingUtilities.getRoot(runNumberChanger);
        SwingUtilities.updateComponentTreeUI(frame);

        frame.invalidate();
        frame.validate();
        frame.repaint();

    }

    public boolean updateRunIds() {
        Set<Result> finalSet = new HashSet<Result>(results.values());

        for ( int i=1; i<structuredResults.size(); i++)
        {
            String currentVal = varChangers.get(i).getCurrentVal();

            Set<Result> resultsForVal = structuredResults.get(i).get(currentVal);
            if ( resultsForVal == null )
            {
                System.out.println( "resultsForVal is null ");
                //throw new Exception("Value " + currentVal + " was not found among structured results ");
            }
            else {
                finalSet.retainAll(resultsForVal);
            }

        }

        ArrayList<String> finalList = new ArrayList<>();
        Iterator iter = finalSet.iterator();
        while ( iter.hasNext() ){
            Result r = (Result) iter.next();
            finalList.add( r.getRunNumber() );
        }

        Collections.sort(finalList, new StringNumComparator());

        runNumberChanger.setValueList( finalList );

        if ( finalList.size() > 0 ) {
            runNumberChanger.setCurrentValIndex(0);
            return true;
        }
        else {
            runNumberChanger.setCurrentValToNothing();
            return false;
        }
    }

    public void emptyValuePanel(){

        valuePanel.removeAll();

        valuePanel.revalidate();
        valuePanel.repaint();
    }
    public void updateRunIdsImageAndShowValues() {

        boolean resultsFound = updateRunIds();

        updateValues();

        updateImage();

    }

    public void updateValues(){

        try {
            // System.out.println("Reaches the try block");

            int runNo = Integer.parseInt(runNumberChanger.getCurrentVal());

            //System.out.println(runNo);

            Result result = results.get(runNo);
            ArrayList<String> values = result.getValues();

            valuePanel.removeAll();

            for( int i=0; i<valueNames.size();i++){
                valuePanel.add(new JLabel(valueNames.get(i) + ": " + values.get(i)));
                //System.out.println(valueNames.get(i) + ": " + values.get(i));
            }
        } catch ( NoSuchElementException exc )
        {
            //System.out.println("reaches here");
            emptyValuePanel();
        }

        //System.out.println("reaches here");
        valuePanel.revalidate();
        valuePanel.repaint();
    }

    public void updateImage(){

        try {
            int runNo = Integer.parseInt(runNumberChanger.getCurrentVal());

            String imgName = results.get(runNo).getImageName();

            String filePath = currentFile.getPath();
            int ind = filePath.lastIndexOf("\\");
            
            boolean macKindaSlash = false;
            
            if ( ind == -1 )
            {
                macKindaSlash = true;
                ind = filePath.lastIndexOf("/");
            }

            String dirPath = filePath.substring(0, ind);
            System.out.println(dirPath);

            String imagePath = "";

            if ( !macKindaSlash ) {
                imagePath = dirPath + "\\" + imgName;
            } else
            {
                imagePath = dirPath + "/" + imgName;
            }

            imagePanel.setImage(imagePath, 550, 550);

        } catch ( NoSuchElementException exc ) {
            imagePanel.removeImage();
        }

        imagePanel.revalidate();
        imagePanel.repaint();
    }

    public void addHiddenVar(VarChanger varChanger){

        JPanel container = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        container.setLayout(flowLayout);

        container.setMaximumSize(new Dimension(200,30));

        container.add(new JLabel(varChanger.getVarName()));

        ShowBtnAction showBtnAction = new ShowBtnAction(varChanger, container);

        JButton btn = new JButton();
        btn.setAction(showBtnAction);

        container.add(btn);

        hiddenVarsPanel.add(container);

    }
}
