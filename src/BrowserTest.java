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

        // realistically I don't need to actually press the button,
        // I can just call processFiles ( which is what happens when the button is pressed and
        // all goes according to plan )

        File crtFile = new File("/Users/mateia/Work/sweepVis/SweepVisualizer/exampleForSweepVisualizer/testone.csv");

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

        paramNoTextField.setText("4");
        metricNoTextField.setText("2");

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
    void dummyTest() {


    }


}
