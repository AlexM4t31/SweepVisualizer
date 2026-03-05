import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    protected static JComponent[] buildGUI() {

        JFrame frame = new JFrame();

        frame.getContentPane().setLayout(new GridBagLayout());

        //frame.setPreferredSize(new Dimension(2000,1000));
        //frame.setMinimumSize(new Dimension(1000,1000));
        //frame.setMaximumSize(new Dimension(2000,1000));

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // Create and add top panel, without button
        JPanel topPanel = new JPanel();

        topPanel.setMinimumSize(new Dimension(620, 90));
        topPanel.setPreferredSize(new Dimension(620, 90));
        topPanel.setMaximumSize(new Dimension(620,90));

        GridBagConstraints cOne = new GridBagConstraints();
        cOne.gridx = 0;
        cOne.gridy = 0;
        cOne.gridheight = 1;
        cOne.gridwidth = 1;
        cOne.weightx = 1;
        cOne.weighty = 0.2;

        frame.getContentPane().add(topPanel, cOne);

        // Specify top panel layout, create and add button

        topPanel.setLayout(new GridBagLayout());

        JPanel paramNoPanel = new JPanel();
        paramNoPanel.setLayout(new BoxLayout(paramNoPanel, BoxLayout.Y_AXIS));

        JTextField paramNoTextField = new JTextField();
        JLabel paramNoLabel = new JLabel("No. of params:");

        GridBagConstraints topPanelCOne = new GridBagConstraints();

        topPanelCOne.weightx = 1;
        topPanelCOne.gridheight = 1;
        topPanelCOne.gridwidth = 1;
        topPanelCOne.fill = GridBagConstraints.HORIZONTAL;
        topPanelCOne.gridx = 0;
        topPanelCOne.gridy = 0;

        JPanel metricNoPanel = new JPanel();
        metricNoPanel.setLayout(new BoxLayout(metricNoPanel, BoxLayout.Y_AXIS));

        JLabel metricNoLabel = new JLabel("No. of metrics:");
        JTextField metricNoTextField = new JTextField();

        GridBagConstraints topPanelCTwo = new GridBagConstraints();

        topPanelCTwo.weightx = 1;
        topPanelCTwo.fill = GridBagConstraints.HORIZONTAL;
        topPanelCTwo.gridx = 1;
        topPanelCTwo.gridy = 0;
        topPanelCTwo.gridheight = 1;
        topPanelCTwo.gridwidth = 1;

        JButton button =  new JButton("Search for CSV");
        button.setMinimumSize(new Dimension(200, 60));
        button.setPreferredSize(new Dimension(200, 60));
        button.setMaximumSize(new Dimension(200, 60));

        GridBagConstraints topPanelCThree = new GridBagConstraints();
        // topPanelCThree.weightx = 1;
        topPanelCThree.fill = GridBagConstraints.HORIZONTAL;
        topPanelCThree.gridx = 0;
        topPanelCThree.gridy = 1;
        topPanelCThree.gridwidth = 2;
        topPanelCThree.gridheight = 1;

        paramNoPanel.add(paramNoLabel);
        paramNoPanel.add(paramNoTextField);

        metricNoPanel.add(metricNoLabel);
        metricNoPanel.add(metricNoTextField);

        topPanel.add(paramNoPanel, topPanelCOne);
        topPanel.add(metricNoPanel, topPanelCTwo);
        topPanel.add(button,topPanelCThree);



        // create bottom panel structure

        JPanel bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(820, 650));
        bottomPanel.setPreferredSize(new Dimension(820, 650));
        bottomPanel.setMaximumSize(new Dimension(820,780));

        GridBagConstraints cTwo = new GridBagConstraints();
        cTwo.gridx = 0;
        cTwo.gridy = 1;
        cTwo.gridheight = 1;
        cTwo.gridwidth = 1;
        cTwo.weightx = 1;
        cTwo.weighty = 0.8;

        frame.getContentPane().add(bottomPanel, cTwo);

        //bottomPanel.setLayout(new GridLayout(1,2)); // behaves better for splitting the bottom panel in two

        bottomPanel.setLayout(new GridBagLayout());

        GridBagConstraints bottomPanelConstraintsOne = new GridBagConstraints();
        bottomPanelConstraintsOne.gridx = 0;
        bottomPanelConstraintsOne.gridy = 0;
        bottomPanelConstraintsOne.gridheight = 1;
        bottomPanelConstraintsOne.gridwidth = 1;
        bottomPanelConstraintsOne.weightx = 0.5;
        bottomPanelConstraintsOne.weighty = 1;

        GridBagConstraints bottomPanelConstraintsTwo = new GridBagConstraints();
        bottomPanelConstraintsTwo.gridx = 1;
        bottomPanelConstraintsTwo.gridy = 0;
        bottomPanelConstraintsTwo.gridheight = 1;
        bottomPanelConstraintsTwo.gridwidth = 1;
        bottomPanelConstraintsTwo.weightx = 0.5;
        bottomPanelConstraintsTwo.weighty = 1;


        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS)); // behaves well for listing items
        //resultsPanel.setLayout(new GridLayout(0,1));
        //resultsPanel.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setMinimumSize(new Dimension(640, 660));
        scrollPane.setPreferredSize(new Dimension(640, 660));
        scrollPane.setMaximumSize((new Dimension(640,660)));

//        resultsPanel.setMinimumSize(new Dimension(260,660));
//        resultsPanel.setPreferredSize(new Dimension(260,660));
//        resultsPanel.setMaximumSize(new Dimension(260,660));
//        resultsPanel.setBackground(Color.orange);

        bottomPanel.add(scrollPane, bottomPanelConstraintsOne);

        JPanel btmRightPanel = new JPanel();
        FlowLayout btmRightLayout = new FlowLayout();
        btmRightLayout.setAlignment(FlowLayout.RIGHT);
        btmRightPanel.setLayout(btmRightLayout);

        btmRightPanel.setMinimumSize(new Dimension(950, 660));
        btmRightPanel.setPreferredSize(new Dimension(950, 660));

        ImagePanel imagePanel = new ImagePanel();
        imagePanel.setMinimumSize(new Dimension(550, 550));
        imagePanel.setPreferredSize(new Dimension(550, 550));
        imagePanel.setMaximumSize((new Dimension(550,550)));

        imagePanel.removeImage();

        //imagePanel.setBackground(Color.RED);

        btmRightPanel.add(imagePanel);

        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new BoxLayout( valuePanel, BoxLayout.Y_AXIS));

        JScrollPane valueScrollPane = new JScrollPane(valuePanel);
        valueScrollPane.setMinimumSize(new Dimension(550, 200));
        valueScrollPane.setPreferredSize(new Dimension(550, 200));
        valueScrollPane.setMaximumSize(new Dimension(550, 200));

        btmRightPanel.add(valueScrollPane);

        bottomPanel.add(btmRightPanel, bottomPanelConstraintsTwo);

        JComponent[] r = { button, resultsPanel, imagePanel, topPanel, valuePanel, paramNoTextField, metricNoTextField};

        frame.pack();
        frame.setVisible(true);

        return r;

    }

    public static void main(String[] args) {

        JComponent[] components = Main.buildGUI();

        JButton button = ( JButton ) components[0];
        JPanel resultsPanel = ( JPanel ) components[1];
        ImagePanel imagePanel = ( ImagePanel ) components[2];
        JPanel topPanel = ( JPanel ) components[3];
        JPanel valuePanel = ( JPanel ) components[4];
        JTextField paramNoTextField = ( JTextField ) components[5];
        JTextField metricNoTextField = ( JTextField ) components[6];

        Processor processor = new Processor( topPanel, resultsPanel, imagePanel, valuePanel, paramNoTextField, metricNoTextField );

        button.addActionListener(processor);


    }

}

