import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;
import java.util.concurrent.Flow;

public class VarChanger extends JPanel{

    private Browser browser;
    private ImagePanel imagePanel;

    private boolean runNumber;
    private ArrayList<String> originalValueList;
    private ArrayList<String> valueList;

    private int currentValIndex;

    private String varName;
    private JLabel valLabel;


    private JPanel theButtonContainer;
    private JButton theButton;

    public static ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        Image img = icon.getImage();
        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return new ImageIcon(scaled);
    }

    public VarChanger(boolean runNumber, Browser browser, ImagePanel imagePanel, Set<String> valueSet, String varName) {

        this.runNumber = runNumber;

        this.browser = browser;

        this.varName = varName;

        setPreferredSize(new Dimension(250, 100));
        setMaximumSize(new Dimension(250,100));

        //setBackground(Color.CYAN);

        this.imagePanel = imagePanel;

        this.valueList = VarChanger.convertStringSetToArrayList(valueSet);

        if ( runNumber ) {
            originalValueList = this.valueList;

            System.out.println(valueSet);
        }

        setLayout(new GridLayout(2,3));
        //setLayout(new GridBagLayout());

        JPanel hideBtnBox = new JPanel();
        FlowLayout hideBtnLayout = new FlowLayout();
        hideBtnLayout.setAlignment(FlowLayout.LEFT);
        hideBtnBox.setLayout(hideBtnLayout);

        JButton hideBtn = new JButton();

        hideBtn.setMinimumSize(new Dimension(60,30));
        hideBtn.setPreferredSize(new Dimension(60,30));

        hideBtnBox.add(hideBtn);

        hideBtn.setAction(new HideBtnAction(this, browser));
        hideBtn.setText("Hide");

        JLabel nameLabel = new JLabel(varName);

        JPanel allRunsBtnContainer = new JPanel();
         if ( runNumber )
        {
            JButton allRunsBtn = new JButton();

            FlowLayout allRunsLayout = new FlowLayout();
            allRunsBtnContainer.setLayout(allRunsLayout);

            ImageIcon resetIcon = new ImageIcon("C:\\Work\\Code\\SweepVisualizer\\src\\reset.png");
            //System.out.println("initial icon dimensions: " + resetIcon.getIconWidth() + "x" + resetIcon.getIconHeight());

            ImageIcon scaledIcon = scaleImage(resetIcon, 32, 32);

            //System.out.println("scaled image dimensions: " + scaledIcon.getIconWidth() + "x" + scaledIcon.getIconHeight());


            allRunsBtn.setMinimumSize(new Dimension(32,32));
            allRunsBtn.setPreferredSize(new Dimension(32,32));

            allRunsBtn.setAction(new AllRunsBtnAction(this, browser));

            allRunsBtn.setIcon(scaledIcon);
            allRunsBtnContainer.add(allRunsBtn);


            theButton = allRunsBtn;
            theButtonContainer = allRunsBtnContainer;
        }

        JPanel leftBtnContainer = new JPanel();
        leftBtnContainer.setLayout(new GridBagLayout());

        JButton leftBtn = new JButton();
        leftBtn.setMinimumSize(new Dimension(50,40));
        leftBtn.setPreferredSize(new Dimension(50,40));
        BtnAction leftBtnAction;
        if ( runNumber )
            leftBtnAction = new RunNumBtnAction(this, browser, BtnAction.LEFT);
        else
            leftBtnAction = new BtnAction(this, browser, BtnAction.LEFT);
        leftBtn.setAction(leftBtnAction);
        leftBtn.setText("<");

        leftBtnContainer.add(leftBtn);

        valLabel = new JLabel(valueList.get(0));

        JPanel rightBtnContainer = new JPanel();
        rightBtnContainer.setLayout(new GridBagLayout());

        JButton rightBtn = new JButton();
        rightBtn.setMinimumSize(new Dimension(50,40));
        rightBtn.setPreferredSize(new Dimension(50,40));
        BtnAction rightBtnAction;
        if ( runNumber )
            rightBtnAction = new RunNumBtnAction(this, browser, BtnAction.RIGHT);
        else
            rightBtnAction = new BtnAction(this, browser, BtnAction.RIGHT);
        rightBtn.setAction(rightBtnAction);
        // rightBtn.setText(">");

        ImageIcon resetIcon = new ImageIcon("C:\\Work\\Code\\SweepVisualizer\\src\\reset.png");
        //System.out.println("initial icon dimensions: " + resetIcon.getIconWidth() + "x" + resetIcon.getIconHeight());

        ImageIcon scaledIcon = scaleImage(resetIcon, 32, 32);
        rightBtn.setIcon(scaledIcon);

        rightBtnContainer.add(rightBtn);

        add(hideBtnBox);
        add(nameLabel);
        add(allRunsBtnContainer);

        add(leftBtnContainer);
        add(valLabel);
        add(rightBtnContainer);

        setBorder(BorderFactory.createMatteBorder(
                2, 0, 2, 0, Color.black));


    }

    public void getBtnAndBtnContainerParentChain() {
        System.out.println("Is the button showing:" + theButton.isShowing());

        System.out.println("Button parent chain:");
        Container c = theButton.getParent();
        while (c != null) {
            System.out.println(c.getClass().getName() + " / showing = " + c.isShowing());
            c = c.getParent();
        }

        System.out.println("Root from varPanel = " + SwingUtilities.getRoot(theButtonContainer));
        System.out.println("Root from button = " + SwingUtilities.getRoot(theButton));
    }

    public void setButtonText() {
        theButton.setText("a");

        JFrame frm = (JFrame) SwingUtilities.getRoot(theButton);

        frm.pack();
        frm.revalidate();
        frm.repaint();
    }

    public void printButtonUI(){
        System.out.println("Button UI: " + theButton.getUI());
    }

    public String getVarName() {
        return  varName;
    }
    public void showChangerAndSeparator(){
        setVisible(true);
        //separator.setVisible(true);
    }
    //public JSeparator getSeparator(){
    //    return separator;
    //}
    public void setValueList(ArrayList<String> valueList){
        this.valueList = valueList;
    }
    public ArrayList<String> getValueList()
    {
        return valueList;
    }

    public void setCurrentValIndex(int currentValIndex){
        this.currentValIndex = currentValIndex;
        this.valLabel.setText(valueList.get(currentValIndex));
    }

    public void setCurrentValToNothing(){
        this.valLabel.setText("X");
    }

    public void runNumberChangerResetValueList(){
        valueList = originalValueList;
        currentValIndex = 0;
        this.valLabel.setText(valueList.get(0));
    }
    public int getCurrentValIndex() {
        if ( valueList.size() == 0 ){
            throw new NoSuchElementException();
        }
        else
            return currentValIndex;
    }

    public String getCurrentVal() {
        if ( valueList.size() == 0 ){
            throw new NoSuchElementException();
        }
        else
            return valueList.get(currentValIndex);
    }
    public void increaseCurrentValIndex() {
        if ( valueList.size() == 0 ){
            throw new NoSuchElementException();
        }
        else {
            currentValIndex = (currentValIndex + 1) % valueList.size();
            valLabel.setText(valueList.get(currentValIndex));
        }
        }

    public void decreaseCurrentValIndex() {
        if ( valueList.size() == 0 ){
            throw new NoSuchElementException();
        }
        else {
            currentValIndex = (valueList.size() + currentValIndex - 1) % valueList.size();
            valLabel.setText(valueList.get(currentValIndex));

        }
    }

    public void changeCurrentValToIndex(int ind) throws Exception {
        if ( ind > 0 && ind < valueList.size() ) {
            currentValIndex = ind;
        } else {
            throw new Exception("Wrong index provided to VarChanger");
        }
    }

    static public ArrayList<String> convertStringSetToArrayList( Set<String> set ) {
        ArrayList<String> arrList = new ArrayList<>();

        Iterator setIter = set.iterator();
        while(setIter.hasNext()){
            arrList.add( (String) setIter.next());
        }

        try {
            float tmp = Float.parseFloat(arrList.get(0));
            // if the exception wasn't triggered, we're dealing with a float
            Collections.sort(arrList, new StringNumComparator());
        } catch(NumberFormatException e) {
            Collections.sort(arrList);
        }

        return arrList;
    }
}
