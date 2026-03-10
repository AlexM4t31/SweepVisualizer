import javax.swing.*;
import javax.swing.border.Border;
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

    public void resetBrowsing(){
        varPanel.removeAll();
        valuePanel.removeAll();

        this.varNames = null;
        this.valueNames = null;
        this.valuePanel = null;
        this.structuredResults = null;
        this.results = null;
        this.varChangers = null;
        this.currentFile = null;
        this.imagePanel = null;
        this.varPanel = null;
    }

    public void setupBrowsing(){

        //JSeparator jSeparator = new JSeparator();

        runNumberChanger = new VarChanger(true,this, imagePanel, structuredResults.get(0).keySet(), varNames.get(0));
        varPanel.add( runNumberChanger );
        varChangers.add( runNumberChanger );
        //varPanel.add(new JSeparator());

        for ( int i=1; i<varNames.size(); i++ ){
            VarChanger v = new VarChanger(false, this, imagePanel, structuredResults.get(i).keySet(), varNames.get(i));
            varPanel.add( v );
            varChangers.add( v );
        }

        hiddenVarsPanel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(hiddenVarsPanel, BoxLayout.Y_AXIS);

        hiddenVarsPanel.setLayout(boxLayout);

        hiddenVarsPanel.setMaximumSize(new Dimension(225, 1000));

        hiddenVarsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //JPanel hiddenVarsLabelPanel = new JPanel();
        //hiddenVarsLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel hiddenVarsLabel = new JLabel("Hidden variables: ");

        hiddenVarsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        varPanel.add(hiddenVarsLabel);

        setVarChangersTextFaded();

        JPanel hiddenVarsLabelPanel = new JPanel();

        //hiddenVarsLabelPanel.setMaximumSize(new Dimension(225, 20));
//        hiddenVarsLabelPanel.setMinimumSize(new Dimension(2, 20));
//
//        hiddenVarsLabelPanel.setLayout(new BoxLayout(hiddenVarsLabelPanel, BoxLayout.Y_AXIS));
//
//        hiddenVarsLabelPanel.add(hiddenVarsLabel);
//
//        hiddenVarsLabelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//
//        hiddenVarsLabelPanel.setBackground(Color.blue);

        //Border blackline = BorderFactory.createLineBorder(Color.black);

//        hiddenVarsLabelPanel.setBorder(blackline);
//
//        hiddenVarsPanel.add(hiddenVarsLabelPanel);

        varPanel.add(hiddenVarsPanel);

        //hiddenVarsPanel.setBackground(Color.red);

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

        int initCount = finalSet.size();

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

            if (finalList.size() == initCount){
                runNumberChanger.setLabelWithAllSuffix();
                setVarChangersTextFaded();
            } else {
                runNumberChanger.setNameLabelWithoutSuffix();
                setVarChangersTextNormal();
            }

            runNumberChanger.setCurrentValIndex(0);
            return true;
        }
        else {
            runNumberChanger.setCurrentValToNothing();
            return false;
        }
    }

    public String[] getValuePanelValueList() {
        Component[] valuePanelComponents = valuePanel.getComponents();

        String[] valuePanelStrings = new String[valuePanelComponents.length];

        for (int i=0; i<valuePanelComponents.length;i++){

            JLabel tmpLabel = (JLabel) valuePanelComponents[i];

            valuePanelStrings[i] = tmpLabel.getText();
        }

        return valuePanelStrings;
    }

    public void setVarChangersTextFaded(){
        for(int i=1; i< varChangers.size();i++){
            VarChanger tmpvc = varChangers.get(i);

            tmpvc.setTextFaded();
        }
    }

    public void setVarChangersTextNormal() {
        for(int i=0; i< varChangers.size();i++){
            VarChanger tmpvc = varChangers.get(i);

            tmpvc.setTextNormal();
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
        //FlowLayout flowLayout = new FlowLayout();
        //flowLayout.setAlignment(FlowLayout.LEFT);
        container.setLayout(new GridLayout(1,2));

        container.setMaximumSize(new Dimension(200,30));

        // container.setBackground(Color.red);

        JPanel leftContainer = new JPanel();

        JLabel tmpJlabel = new JLabel(varChanger.getVarName());

        // leftContainer.setBackground(Color.orange);

        leftContainer.add(tmpJlabel);

        leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));

        JPanel rightContainer = new JPanel();

        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));

        // rightContainer.setBackground(Color.CYAN);

        ShowBtnAction showBtnAction = new ShowBtnAction(varChanger, container);

        JButton btn = new JButton();
        btn.setAction(showBtnAction);

        btn.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightContainer.add(btn);

        //container.add(btn);

        container.add(leftContainer);//, leftGBConstraints);
        container.add(rightContainer);//, rightGBConstraints);

        hiddenVarsPanel.add(container);

    }
}
