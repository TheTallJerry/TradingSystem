package client.guiandpresenter.userscreen;

import client.guiandpresenter.SystemPresenter;

public class InventoryPresenter extends SystemPresenter {

    /**
     * Format the given item, represented as a string
     *
     * @param selected the given item, represented as a string
     * @return the formatted version of the given item, represented as a string
     */
    public String itemDetails(String selected) {
        return "<html> " + selected;
    }

    @Override
    public String getTitle() {
        return null;
    }
}
