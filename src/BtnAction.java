import javax.swing.*;
import java.awt.event.ActionEvent;

public class BtnAction extends AbstractAction {

    static final int LEFT = 0;
    static final int RIGHT = 1;
    VarChanger varChanger;

    Browser browser;

    int dir = -1;
    public BtnAction(VarChanger varChanger, Browser browser, int dir)
    {
        this.varChanger = varChanger;
        this.browser = browser;
        this.dir = dir;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if ( dir == BtnAction.LEFT )
            varChanger.decreaseCurrentValIndex();
        else
            varChanger.increaseCurrentValIndex();

        browser.updateRunIdsImageAndShowValues();



    }
}
