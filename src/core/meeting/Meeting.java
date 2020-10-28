package core.meeting;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represent a meeting between two users using this system.
 */
public class Meeting implements Serializable {
    private String location;
    private LocalDateTime meetingTime;
    private final int[] timesEdited;
    private final Boolean[] meetingConfirmed;
    private final Boolean[] meetingOccurred;
    private final int meetingId;

    /**
     * Creates a meeting with its <code>location</code>, <code>meetingTime</code>, and unique <code>meetingId</code> as
     * its identifier. Initially, <code>timesEdited</code>, representing the number of time meeting is edited, is 0.
     * <code>meetingConfirmed</code> and <code>meetingOccurred</code> is false for both users that involved in this trade.
     *
     * @param location    the location of the meeting.
     * @param meetingTime the time of the meeting.
     * @param meetingId   an int which is an unique identifier of this meeting.
     */
    public Meeting(String location, LocalDateTime meetingTime, int meetingId) {
        this.location = location;
        this.meetingTime = meetingTime;
        timesEdited = new int[]{0, 0};
        meetingConfirmed = new Boolean[]{false, false};
        meetingOccurred = new Boolean[]{false, false};
        this.meetingId = meetingId;
    }

    /**
     * Gets the <code>meetingId</code>, which is the unique identifier of this meeting.
     *
     * @return <code>meetingId</code> of this meeting.
     */
    public Integer getMeetingId() {
        return meetingId;
    }

    /**
     * Sets <code>timesEdited</code>, the number of time an user has edited this meeting, with
     * <code>newTimesEdited</code> and given <code>userOrder</code>.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder      an int representing the user that is performing this action. 0 is the initiator of
     *                       the trade, 1 is the responder of the trade.
     * @param newTimesEdited an int representing the new number of time user edited in this meeting.
     */
    public void setTimesEdited(int userOrder, int newTimesEdited) {
        timesEdited[userOrder] = newTimesEdited;
    }

    /**
     * Gets <code>timesEdited</code> of a user, given <code>userOrder</code>.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder an int representing the user that is performing the action. 0 is the initiator of the trade, 1
     *                  is the responder of the trade.
     * @return an int which is the corresponding number of time meeting is edited by this user, from TimesEdited.
     */
    public int getTimesEdited(int userOrder) {
        return timesEdited[userOrder];
    }

    /**
     * Sets the <code>location</code> of this meeting, with <code>newLocation</code>.
     *
     * @param newLocation new <code>location</code> of this meeting.
     */
    public void setLocation(String newLocation) {
        this.location = newLocation;
    }

    /**
     * Gets the <code>location</code> of this meeting.
     *
     * @return <code>location</code> of this meeting.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the <code>time</code> of this meeting.
     *
     * @param meetingTime new <code>meetingTime</code> of this meeting.
     */
    public void setMeetingTime(LocalDateTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    /**
     * Getter for the <code>time</code> of this meeting.
     *
     * @return <code>time</code> of this meeting.
     */
    public LocalDateTime getMeetingTime() {
        return meetingTime;
    }

    /**
     * Sets confirmation of <code>time</code> and <code>location</code> from a user involved in this meeting with
     * <code>confirmed</code>, given <code>userOrder</code>.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the trade,
     *                  1 is the responder of the trade.
     * @param confirmed a boolean that can be either true or false. If it is true, it means the user has confirmed
     *                  that the time and place, and vice versa.
     */
    public void setMeetingConfirmed(int userOrder, boolean confirmed) {
        meetingConfirmed[userOrder] = confirmed;
    }

    /**
     * Gets confirmation of <code>time</code> and <code>location</code> from a user involved in this meeting,
     * given <code>userOrder</code>.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the trade,
     *                  1 is the responder of the trade.
     * @return a boolean value, true if and only if the <code>meeting</code> is successfully confirmed by given
     * <code>user</code> involved.
     */
    public boolean getMeetingConfirmed(int userOrder) {
        return meetingConfirmed[userOrder];
    }

    /**
     * Sets confirmation for whether this meeting has occurred from a user involved in the meeting with
     * <code>occurred</code>, given <code>userOrder</code>.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the trade,
     *                  1 is the responder of the trade.
     * @param occurred  a boolean that can be either true or false. If it is true, it means the user claim the meeting
     *                  has occurred, and vice versa.
     */
    public void setMeetingOccurred(int userOrder, boolean occurred) {
        meetingOccurred[userOrder] = occurred;
    }

    /**
     * Gets confirmation for whether the meeting has occurred from a user involved in this meeting,
     * given <code>userOrder</code>.
     * <p>
     * Precondition: <code>userOrder</code> must be 0 or 1.
     *
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the trade,
     *                  1 is the responder of the trade.
     * @return boolean value, true if and only if the <code>meeting</code> is successfully confirmed occurred by given
     * <code>user</code> involved.
     */
    public boolean getMeetingOccurred(int userOrder) {
        return meetingOccurred[userOrder];
    }

    /**
     * Gives a string representation of this meeting.
     *
     * @return information about the meeting as a string, including <code>location</code>, <code>time</code>,
     * arrangement status, occurrence confirmation status.
     */
    public String toString() {
        StringBuilder str = new StringBuilder("<html>The proposed location: [" + location + "]<br>");
        str.append("The proposed time: [").append(meetingTime).append("]<br>").append("Meeting Status: ");
        if (meetingConfirmed[0] && meetingConfirmed[1]) {
            str.append("[Arranged. Waiting for occurrence...]<br>").append("&ensp;Trade Initiator ");
            str.append(confirmStr(meetingOccurred[0])).append(" the occurrence of this meeting.");
            str.append("<br>&ensp;Trade Responder ").append(confirmStr(meetingOccurred[1]));
            str.append(" the occurrence of this meeting.");
        } else {
            str.append("[Arranging...]<br>").append("Trade Initiator edited ").append(timesEdited[0]);
            str.append(" time(s).<br>").append(timesEdited[1]).append("<br>Trade Responder edited");
            str.append(" time(s).<br>").append("Trade Initiator ");
            str.append(confirmStr(meetingConfirmed[0])).append(" the arrangement of this meeting.");
            str.append("<br>Trade Responder ").append(confirmStr(meetingConfirmed[1]));
            str.append(" the arrangement of this meeting.");
        }
        return str.toString();
    }

    /* Set up confirmation string */
    private String confirmStr(boolean bool) {
        if (bool) return "has confirmed";
        else return "has not confirm";
    }
}
