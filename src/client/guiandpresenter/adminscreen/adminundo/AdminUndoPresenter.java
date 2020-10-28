package client.guiandpresenter.adminscreen.adminundo;

import client.guiandpresenter.SystemPresenter;

/**
 * Presents undo related information.
 */
public class AdminUndoPresenter extends SystemPresenter {
    /**
     * @return title of a frame in the AdminThresholdScreen
     */
    @Override
    public String getTitle() {
        return "Undo User Action";
    }

    /**
     * @return string displayed to give users two options to sort
     */
    String undoInstruction() {
        return "Choose to sort by user or action type.";
    }

    /**
     * @return string displayed on JLabel
     */
    String undoProperties() {
        return "Users/ActionTypes";
    }

    /**
     * @return string displayed on JLabel
     */
    String undoActionProp() {
        return "Actions of Selected User/ActionType (double click for options)";
    }

    /**
     * @return string displayed on JLabel
     */
    String undoByUser() {
        return "By User";
    }

    /**
     * @return string displayed on JLabel
     */
    String undoByActionType() {
        return "By Action Type";
    }

    /**
     * @return string displayed on JOptionPanel
     */
    String undoAsk() {
        return "Undo Action?";
    }

    /**
     * @return string displaying undo options
     */
    String[] undoOption() {
        return new String[]{"Undo", "Cancel"};
    }
}
