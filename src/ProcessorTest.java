import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Processor implements ActionListener, and its actionPerformed is what is triggered when
// the 'search for CSV' button is pressed, so I want to make sure that everything that can happen when that is
// pressed is accounted for

// getMetricNoTextField,getParamNoTextField

class ProcessorTest {

    private JButton button;
    private JPanel resultsPanel;
    private ImagePanel imagePanel;
    private JPanel topPanel;
    private JPanel valuePanel;
    private JTextField paramNoTextField;
    private JTextField metricNoTextField;
    private Processor processor;

    @BeforeEach
    void procTestSetup() {

        JComponent[] components = Main.buildGUI();

        button = ( JButton ) components[0];
        resultsPanel = ( JPanel ) components[1];
        imagePanel = ( ImagePanel ) components[2];
        topPanel = ( JPanel ) components[3];
        valuePanel = ( JPanel ) components[4];
        paramNoTextField = ( JTextField ) components[5];
        metricNoTextField = ( JTextField ) components[6];

        processor = new Processor( topPanel, resultsPanel, imagePanel, valuePanel, paramNoTextField, metricNoTextField );

        button.addActionListener(processor);

    }

    @Test
    void parseNumInputsInvalidParamNo() {
        paramNoTextField.setText("a");
        metricNoTextField.setText("2");



        assertFalse(processor.parseNumInputs());
    }

    @Test
    void parseNumInputsInvalidMetricNo() {
        paramNoTextField.setText("4");
        metricNoTextField.setText("a");


        assertFalse(processor.parseNumInputs());
    }

    @Test
    void parseNumInputsBothValid() {
        paramNoTextField.setText("4");
        metricNoTextField.setText("2");

        assertTrue(processor.parseNumInputs());
    }

    //@Test
    void actionPerformedParamNoInvalid() {

       paramNoTextField.setText("A");
       metricNoTextField.setText("2");

       //button.

    }

    // test confirming that files with repeat ids get btfo'd

    // test for getting the right instance when you put its id in in the runNumberVarChanger

    // test for getting sth if the values are there

    @Test
    void repeatIdsDetectedAsInvalid(){
        File crtFile = new File("/Users/mateia/Work/sweepVis/SweepVisualizer/exampleForSweepVisualizer/testrepeatids.csv");

        paramNoTextField.setText("4");
        metricNoTextField.setText("2");

        boolean validInputs = processor.parseNumInputs();

        boolean validFirstLines = processor.validateFileStructureFromFirstLines(crtFile);

        assertTrue(validInputs);
        assertTrue(validFirstLines);

        ArrayList<Map<String, Set<Result>>> structuredRes = processor.processFiles(crtFile);

        assertNull(structuredRes);
    }

    @Test
    void emptyIdDetectedAsInvalid(){
        File crtFile = new File("/Users/mateia/Work/sweepVis/SweepVisualizer/exampleForSweepVisualizer/testemptyid.csv");

        paramNoTextField.setText("4");
        metricNoTextField.setText("2");

        boolean validInputs = processor.parseNumInputs();

        boolean validFirstLines = processor.validateFileStructureFromFirstLines(crtFile);

        assertTrue(validInputs);
        assertTrue(validFirstLines);

        ArrayList<Map<String, Set<Result>>> structuredRes = processor.processFiles(crtFile);

        assertNull(structuredRes);
    }



}