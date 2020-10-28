package client.guiandpresenter.userscreen.usertrade.usermeetingscreen;

import client.guiandpresenter.SystemPresenter;

class UserMeetingPresenter extends SystemPresenter {

    @Override
    public String getTitle() {
        return "View/Edit Meeting";
    }

    /**
     * @param flag a boolean indicating the meeting confirmation status
     * @return a string indicating whether the meeting arrangement is successful
     */
    String displayMeetingArrStatus(boolean flag) {
        if (flag)
            return "Arrangement successfully confirmed!";
        return "<html>Couldn't confirm arrangement. Check your meeting info or trade info for more information.<br>";
    }

    /**
     * @param flag a boolean indicating the meeting occurrence confirmation status
     * @return a string indicating whether confirmation that the meeting has occurred is successful
     */
    String displayMeetingOccStatus(boolean flag) {
        if (flag) return "Occurrence successfully confirmed!";
        return "<html>Couldn't confirm occurrence. Check your meeting info or trade info for more information.<br>";
    }

    /**
     * @param flag a boolean indicating the meeting creation status
     * @return a string indicating whether meeting creation is successful
     */
    String meetingCreateStatus(int flag) {
        switch (flag) {
            case 1:
                return "Meeting successfully created!";
            case 2:
                return "Location is not formatted correctly, please try again.";
            case 3:
                return "Date & time is not formatted correctly. Make sure the time is in the future as well!";
            default:
                return "<html>Failed to create meeting. Possible reasons are:<br>1. You have not finish the last meeting" +
                        "related to this trade yet. <br>2. The trade has finished.<br>Double click the trade for more " +
                        "information.";
        }
    }

    /**
     * @param flag a boolean indicating the meeting editing status
     * @return a string indicating whether editing meeting is successful
     */
    String meetingEditStatus(int flag) {
        switch (flag) {
            case 1:
                return "Meeting successfully edited";
            case 2:
                return "Place is not formatted correctly";
            case 3:
                return "Date & time is not formatted correctly";
            default:
                return "<html>Failed to edit meeting. Possible reasons are:<br> 1. you are the last editor of this" +
                        " meeting, waiting for the other user to edit or confirm your arrangement.<br>The arrangement" +
                        "is confirmed. <br>You exceeded maximum meeting edit time, the trade has been cancelled." +
                        "<br>Double click the trade for more information.";
        }
    }

    /**
     * @param thresholds an arraylist of threshold values
     * @return a string representing thresholds and their corresponding value
     */
    String getMeetingThresholds(int[] thresholds) {
        return "<html> The meeting thresholds are: " + "<br>" +
                "Max meeting edits: " + thresholds[0] + "<br>" +
                "Max meeting late days: " + thresholds[1];
    }

    String getMeetingInfo(String info) {
        if (info.equals(""))
            return "<html>No on going meeting for this trade. Create one or check your trade information.";
        return info;
    }

    /**
     * @return string indicating "Get current meeting info" button
     */
    String getMeetingInfoBtn() {
        return "Current Meeting Information";
    }

    /**
     * @return string array with 2 String indicating "Create Meeting", "Edit Meeting" button respectively.
     */
    String[] createEditBtn() {
        return new String[]{"Create Meeting", "Edit Meeting"};
    }

    /**
     * @return string indicating "Confirm Meeting Arrangement" button
     */
    String confirmMeetingArrangementBtn() {
        return "Confirm Meeting Arrangement";
    }

    /**
     * @return string indicating "Confirm Meeting Occurred" button
     */
    String confirmMeetingOccurredBtn() {
        return "Confirm Meeting Occurred";
    }

    /**
     * @return string indicating label for <code>meeting</code> time
     */
    String meetingTimeLbl() {
        return "Meeting time in valid format (such as 2019-03-10 13:30):";
    }

    /**
     * @return string indicating label of "Meeting location: "
     */
    String meetingLocLbl() {
        return "Meeting location in valid format(Only contain letters, digits, " +
                "comma, figure dash(-) or hash(#)): ";
    }
}
