package client.guiandpresenter.userscreen.usermessage;

import client.guiandpresenter.SystemPresenter;

import java.util.List;

class UserMessagePresenter extends SystemPresenter {
    @Override
    public String getTitle() {
        return "Message";
    }

    /**
     * @return a String displaying "Message: " label
     */
    String messageLbl() {
        return "Message: ";
    }

    /**
     * @return a string indicating the "View Received messages" button
     */
    String viewReceivedBtn() {
        return "View Received Messages";
    }

    /**
     * @return a string indicating the "View sent messages" button
     */
    String viewSentBtn() {
        return "View Sent Messages";
    }

    /**
     * @return a string indicating the "Send new messages" button
     */
    String sendBtn() {
        return "Send New Messages";
    }

    /**
     * @param flag the input value that is true iff the message was successfully sent, acting as a flag
     * @return a string displaying "Message sent"
     */
    String messageSentStatus(boolean flag) {
        if(!flag)
            return "<html> Message couldn't be sent. <br> Please make sure you have entered a valid username. ";
        return "Message sent. ";
    }

    /**
     * @param msgs the list of messages received
     * @return a string displaying the messages received
     */
    String messageReceived(List<String> msgs) {
        if (msgs.isEmpty())
            return "You haven't received a message. ";
        StringBuilder sb = new StringBuilder("<html> The messages received are: <br>");
        for (String str : msgs) {
            sb.append(str).append("<br>");
        }
        return sb.toString();
    }

    /**
     * @param msgs the list of messages sent
     * @return a string displaying the messages sent
     */
    String messageSent(List<String> msgs) {
        if (msgs.isEmpty())
            return "You haven't sent a message. ";
        StringBuilder sb = new StringBuilder("<html> The messages sent are: <br>");
        for (String str : msgs) {
            sb.append(str).append("<br>");
        }
        return sb.toString();
    }

}
