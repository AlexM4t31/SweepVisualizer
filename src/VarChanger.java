import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;
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

    private JSeparator separator;

    public VarChanger(boolean runNumber, Browser browser, ImagePanel imagePanel, Set<String> valueSet, String varName, JSeparator separator) {

        this.runNumber = runNumber;

        this.browser = browser;

        this.separator = separator;

        this.varName = varName;

        setPreferredSize(new Dimension(100, 60));

        this.imagePanel = imagePanel;

        this.valueList = VarChanger.convertStringSetToArrayList(valueSet);

        if ( runNumber ) {
            originalValueList = this.valueList;
        }

        setLayout(new GridLayout(2,3));


        JPanel hideBtnBox = new JPanel();
        FlowLayout hideBtnLayout = new FlowLayout();
        hideBtnLayout.setAlignment(FlowLayout.LEFT);
        hideBtnBox.setLayout(hideBtnLayout);

        JButton hideBtn = new JButton();

        hideBtn.setMinimumSize(new Dimension(10,10));
        hideBtn.setPreferredSize(new Dimension(10,10));

        hideBtnBox.add(hideBtn);

        hideBtn.setAction(new HideBtnAction(this, browser));

        JLabel nameLabel = new JLabel(varName);

        JPanel allRunsBtnContainer = new JPanel();
        if ( runNumber )
        {
            FlowLayout allRunsLayout = new FlowLayout();
            allRunsLayout.setAlignment(FlowLayout.CENTER);
            allRunsBtnContainer.setLayout(allRunsLayout);
            JButton allRunsBtn = new JButton("ALL");
            allRunsBtn.setAction(new AllRunsBtnAction(this, browser));
            allRunsBtnContainer.add(allRunsBtn);
        }

        JPanel leftBtnContainer = new JPanel();
        leftBtnContainer.setLayout(new GridBagLayout());

        JButton leftBtn = new JButton();
        leftBtn.setText("AAAAA");
        leftBtn.setMinimumSize(new Dimension(30,20));
        leftBtn.setPreferredSize(new Dimension(30,20));
        BtnAction leftBtnAction;
        if ( runNumber )
            leftBtnAction = new RunNumBtnAction(this, browser, BtnAction.LEFT);
        else
            leftBtnAction = new BtnAction(this, browser, BtnAction.LEFT);
        leftBtn.setAction(leftBtnAction);

        leftBtnContainer.add(leftBtn);

        valLabel = new JLabel(valueList.get(0));

        JPanel rightBtnContainer = new JPanel();
        rightBtnContainer.setLayout(new GridBagLayout());

        JButton rightBtn = new JButton(">");
        rightBtn.setMinimumSize(new Dimension(30,20));
        rightBtn.setPreferredSize(new Dimension(30,20));
        BtnAction rightBtnAction;
        if ( runNumber )
            rightBtnAction = new RunNumBtnAction(this, browser, BtnAction.RIGHT);
        else
            rightBtnAction = new BtnAction(this, browser, BtnAction.RIGHT);
        rightBtn.setAction(rightBtnAction);

        rightBtnContainer.add(rightBtn);

        add(hideBtnBox);
        add(nameLabel);
        add(allRunsBtnContainer);
        add(leftBtnContainer);
        add(valLabel);
        add(rightBtnContainer);


    }


    public String getVarName() {
        return  varName;
    }
    public void showChangerAndSeparator(){
        setVisible(true);
        separator.setVisible(true);
    }
    public JSeparator getSeparator(){
        return separator;
    }
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
