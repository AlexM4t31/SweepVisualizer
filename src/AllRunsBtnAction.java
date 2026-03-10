import javax.swing.*;
import java.awt.event.ActionEvent;

public class AllRunsBtnAction extends AbstractAction {

    VarChanger varChanger;

    Browser browser;
    public AllRunsBtnAction(VarChanger varChanger, Browser browser)
    {
        this.varChanger = varChanger;
        this.browser = browser;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        varChanger.runNumberChangerResetValueList();
        varChanger.setLabelWithAllSuffix();
        browser.setVarChangersTextFaded();
        browser.updateValues();
        browser.updateImage();
    }
}
