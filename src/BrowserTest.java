import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

    // as weird as it sounds I think I'm gonna replicate the beforeeach from the processor test


class BrowserTest {

    private JButton button;
    private JPanel resultsPanel;
    private ImagePanel imagePanel;
    private JPanel topPanel;
    private JPanel valuePanel;
    private JTextField paramNoTextField;
    private JTextField metricNoTextField;
    private Processor processor;

    private ArrayList<VarChanger> paramVarChangers;
    private VarChanger runNumVarChanger;
    private Browser browser;

    static boolean compareMetricValueArrays(String[] refValues, String[] compValues) {

        if (refValues.length != compValues.length)
            return false;

        for (int i=0;i<=refValues.length;i++){
            if (refValues[i] != compValues[i])
                return false;
        }

        return true;
    }

    void procTestSetup(String testFilePath, int inParamNo, int inMetricNo) {

        JComponent[] components = Main.buildGUI();

        button = ( JButton ) components[0];
        resultsPanel = ( JPanel ) components[1];
        imagePanel = ( ImagePanel ) components[2];
        topPanel = ( JPanel ) components[3];
        valuePanel = ( JPanel ) components[4];
        paramNoTextField = ( JTextField ) components[5];
        metricNoTextField = ( JTextField ) components[6];

        processor = new Processor( topPanel, resultsPanel, imagePanel, valuePanel, paramNoTextField, metricNoTextField );

        // realistically I don't need to actually press the button,
        // I can just call processFiles ( which is what happens when the button is pressed and
        // all goes according to plan )

        File crtFile = new File(testFilePath);

        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(crtFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        String crtline = null;

        try {
            crtline = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(crtline);

        paramNoTextField.setText(Integer.toString(inParamNo));
        metricNoTextField.setText(Integer.toString(inMetricNo));

        processor.parseNumInputs();
        processor.processFiles(crtFile);

        Component[] resultsPanelComponents = resultsPanel.getComponents();

        Component firstComponent = resultsPanelComponents[0];

        runNumVarChanger = (VarChanger) firstComponent;

        paramVarChangers = new ArrayList<>();

        for(int i=1;i<resultsPanelComponents.length-1;i++){
            paramVarChangers.add((VarChanger) resultsPanelComponents[i]);
        }

        browser = runNumVarChanger.getBrowser();
    }

    // ok I see what's up

    // ok, as far as getting access to stuff, that all seems prepped now. good shit.

    // ok I want a break from just doing setup work,
    // so let me just do the test to see what happens when 1)

    // let's also add to 1 that there should be no instances of an empty string id

    // first q: do I do it here, in the context of the browser tests,
    // or do I do it in the processorTest bit

    // of the top of my head, let's just do it in the processorTest bit
    //

    @Test
    void getElementWithIdWhenChangingIdSuccessfully() { // aka browserTestOne

        String testFilePath = "";

        int inParamNo = 2;
        int inMetricNo = 2;

        procTestSetup(testFilePath,inParamNo,inMetricNo);



        // self explanatory
        // have id set, get right element

        // in order to do this test, I have to consider:
        // abstracting away from the actual implementation
        // how do I expect to have the ID in there, and how do I expect the
        // program to respond to the ID being in there?

        // usually, I would expect to be able to browse Id's via the first varChanger
        // of course, there is the matter that that varChanger behaves in two different ways
        // which could perhaps be worth signalling, now that I think about it.\

        // so before I implement this test, let me actually see if
        // I can easily mark the fact of all id's being browsed?

        // actual implementation:
        // the first quesiton is
        // how do I do the actual setting of the id
        // it would be via pressing one of the runNumChanger buttons
        // or alternatively I can just call whatever method that would end up calling normally
        // let's just have a look there and get an idea of how that goes

        // so there's a difference between the normal btnAction
        // and the runNumBtnAction

        // and there seems to be an actual
        // difference between what the
        // different actionPerformeds do

        // the runNum one
        // does updateValues
        // and updateImage
        // doesnt call the overarching method though
        // and I think I know why,
        // I think it would be down to y

        // what I seem to be nearing in terms of doing
        // just intuitively is that maybe I could
        // go with each way of getting a certain ID
        // it to show in the runnumvarchanger

        // the first way being
        // 'just pressing the increase btn on the id varchanger'
        //
        // the other being
        // changing a parameter value
        // and getting the first of the id's out of
        // those that have the resulting
        // combination of parameter values

        // arguably those are just perfectly valid tests
        // and I reckon I should just implement them

        // and I might realise after implementing them that I don't actually need
        // anything more than them
        // and if I do need anything extra
        // well I'll have a super solid foundation for adding those additional tests

        // so what I would do right now
        // is just put together a csv for which I know exactly how it would be loaded
        // and then 'click' the increase button on the id
        //
        // and then check that the right metric values are shown

        // ok let's start with this utility function

        // the first thing to do is look at how the metrics are shown
        // so look at the whole pipeline after selecting increase id btn

    }

    void getElementWithIdWhenIdDoesntChange(){
        // doesnt warrant implementation yet, to be fair, it's quite a daft test
    }

}
