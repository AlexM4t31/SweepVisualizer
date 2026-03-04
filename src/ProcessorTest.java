import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

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

        button.doClick();

        Component[] componentsOfResPanel = resultsPanel.getComponents();

        assertEquals(0, componentsOfResPanel.length);
    }


    void metricNumInputsInvalidParamNo() {
        paramNoTextField.setText("4");
        metricNoTextField.setText("a");

        button.doClick();

        Component[] componentsOfResPanel = resultsPanel.getComponents();

        assertEquals(0, componentsOfResPanel.length);
    }

    void bothNumInputsInvalidParamNo() {
        paramNoTextField.setText("4");
        metricNoTextField.setText("2");

        button.doClick();

        Component[] componentsOfResPanel = resultsPanel.getComponents();

        assertEquals(0, componentsOfResPanel.length);
    }

    @Test
    void actionPerformedParamNoInvalid() {

       paramNoTextField.setText("A");
       metricNoTextField.setText("2");

       //button.

    }





}