import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {

    private static JComponent[] buildGUI() {

        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new GridBagLayout());

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

        JButton button =  new JButton("Search for CSV");
        button.setMinimumSize(new Dimension(200, 60));
        button.setPreferredSize(new Dimension(200, 60));
        button.setMaximumSize(new Dimension(200, 60));

        topPanel.add(button); // Adding a component to a container with a gridbaglayout without and gridbagconstraints leads to centering the component

        // create bottom panel structure

        JPanel bottomPanel = new JPanel();
        bottomPanel.setMinimumSize(new Dimension(600, 780));
        bottomPanel.setPreferredSize(new Dimension(600, 780));
        bottomPanel.setMaximumSize(new Dimension(600,780));

        GridBagConstraints cTwo = new GridBagConstraints();
        cTwo.gridx = 0;
        cTwo.gridy = 1;
        cTwo.gridheight = 1;
        cTwo.gridwidth = 1;
        cTwo.weightx = 1;
        cTwo.weighty = 0.8;

        frame.getContentPane().add(bottomPanel, cTwo);

        bottomPanel.setLayout(new GridLayout(1,2)); // behaves better for splitting the bottom panel in two

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS)); // behaves well for listing items


        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setMinimumSize(new Dimension(280, 200));
        scrollPane.setPreferredSize(new Dimension(280, 200));
        scrollPane.setMaximumSize((new Dimension(280,200)));

        bottomPanel.add(scrollPane);

        JPanel btmRightPanel = new JPanel();
        FlowLayout btmRightLayout = new FlowLayout();
        btmRightLayout.setAlignment(FlowLayout.RIGHT);
        btmRightPanel.setLayout(btmRightLayout);

        ImagePanel imagePanel = new ImagePanel();
        imagePanel.setMinimumSize(new Dimension(280, 560));
        imagePanel.setPreferredSize(new Dimension(280, 560));
        imagePanel.setMaximumSize((new Dimension(280,560)));

        imagePanel.setPlaceholder();

        btmRightPanel.add(imagePanel);

        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new BoxLayout( valuePanel, BoxLayout.Y_AXIS));

        JScrollPane valueScrollPane = new JScrollPane(valuePanel);
        valueScrollPane.setMinimumSize(new Dimension(280, 200));
        valueScrollPane.setPreferredSize(new Dimension(280, 200));
        valueScrollPane.setMaximumSize(new Dimension(280, 200));

        System.out.println(valueScrollPane.getInsets());

        btmRightPanel.add(valueScrollPane);

        bottomPanel.add(btmRightPanel);

        JComponent[] r = { button, resultsPanel, imagePanel, topPanel, valuePanel};

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

        Processor processor = new Processor( topPanel, resultsPanel, imagePanel, valuePanel );

        button.addActionListener(processor);


    }

}

