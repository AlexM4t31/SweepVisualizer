import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowBtnAction extends AbstractAction {

    VarChanger varChanger;

    JPanel containerInHiddenList;
    public ShowBtnAction(VarChanger varChanger, JPanel containerInHiddenList)
    {
        this.varChanger = varChanger;
        this.containerInHiddenList = containerInHiddenList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        varChanger.showChangerAndSeparator();
        containerInHiddenList.getParent().remove(containerInHiddenList);

        containerInHiddenList.revalidate();

    }
}
