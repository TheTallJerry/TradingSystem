package client.guiandpresenter.adminscreen.adminthreshold;

import client.guiandpresenter.SystemPresenter;

/**
 * Presents threshold setting information.
 */
public class AdminThresholdPresenter extends SystemPresenter {
    /**
     * @return title of a frame in the AdminThresholdScreen
     */
    @Override
    public String getTitle() {
        return "Change Threshold";
    }

    /**
     * get an arraylist of thresholds that an admin can set
     *
     * @return an arraylist of thresholds that an admin can set
     */
    String[] targetThreshold() {
        return new String[]{"Borrow-Lend difference", "Weekly transaction", "Meeting edit",
                "Incomplete trade", "Day after Meeting without Occurrence"};
    }

    /**
     * @return string displayed to direct users to adjust limits
     */
    String limitType() {
        return "Thresholds that can be changed: ";
    }


    /**
     * @return string displayed to direct users to the current limit
     */
    String currentLimit() {
        return "Current limit: ";
    }

    /**
     * @return string displayed to direct users to the new limit
     */
    String newLimit() {
        return "New limit: ";
    }

    /**
     * @return string displayed on the change button
     */
    String changeBtn() {
        return "Change";
    }

    /**
     * get title of the AdminThresholdScreen
     *
     * @return title of the AdminThresholdScreen
     */
    String thresholdScreenTitle() {
        return "Admin Threshold Screen";
    }

    /**
     * @param success the input value representing the status of threshold changing,
     *                acting as a flag
     * @return string displayed to indicate whether change threshold is successful
     */
    String changeThreshold(boolean success) {
        if (success) return "Change succeeded";
        else return "Change failed - Check your input validity: Must be positive integer";
    }
}
