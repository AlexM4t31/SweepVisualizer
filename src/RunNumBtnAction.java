import java.awt.event.ActionEvent;

public class RunNumBtnAction extends BtnAction {

    public RunNumBtnAction(VarChanger varChanger, Browser browser, int dir){
        super(varChanger, browser, dir);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if ( dir == BtnAction.LEFT )
            varChanger.decreaseCurrentValIndex();
        else
            varChanger.increaseCurrentValIndex();

        browser.updateValues();
        browser.updateImage();

    }

}
