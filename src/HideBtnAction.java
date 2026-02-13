import javax.swing.*;
import java.awt.event.ActionEvent;

public class HideBtnAction extends AbstractAction {

    VarChanger varChanger;

    Browser browser;


    public HideBtnAction(VarChanger varChanger, Browser browser)
    {
        this.varChanger = varChanger;
        this.browser = browser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        varChanger.setVisible(false);
        //varChanger.getSeparator().setVisible(false);

        browser.addHiddenVar(varChanger);

        browser.revalidate();

    }
}
