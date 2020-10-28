package core.meeting;

import core.meeting.reverter.ConfirmMeetingArrangementReverter;
import core.meeting.reverter.ConfirmMeetingOccurrenceReverter;
import core.meeting.reverter.EditMeetingReverter;
import core.reverter.ActionReverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manage actions and information stored in <code>Meeting</code> and meeting-related thresholds.
 */
public class MeetingManager {
    private final List<Meeting> meetings;
    private int maxMeetingEdits;
    private int newestMeetingId;
    private int maxMeetingLateTime;

    /**
     * Construct a <code>UserMeetingManager</code> with a collection of <code>meetings</code>,
     * <code>maxMeetingEdits</code>, <code>newestMeetingID</code>. <code>maxMeetingLateTime</code>.
     *
     * @param meetings           a collection of <code>meeting</code>s in the system.
     * @param maxMeetingEdits    maximum number of editing time and place an user can do to a <code>meeting</code>.
     * @param newestMeetingId    a new ID that has not been assigned to any <code>meeting</code> in the system.
     * @param maxMeetingLateTime the maximum number of day user have to confirm meeting occurred.
     */
    public MeetingManager(List<Meeting> meetings, int maxMeetingEdits, int newestMeetingId, int maxMeetingLateTime) {
        this.meetings = meetings;
        this.maxMeetingEdits = maxMeetingEdits;
        this.newestMeetingId = newestMeetingId;
        this.maxMeetingLateTime = maxMeetingLateTime;
    }

    /**
     * Return the newest meeting id
     *
     * @return the newest meeting id
     */
    public int getNewestMeetingId() {
        return newestMeetingId;
    }

    /* A helper function that get meeting from meetingID,take in a meetingId, return the corresponding
     meeting of this meetingId */
    private Meeting getMeeting(int meetingId) {
        for (Meeting meeting : meetings) if (meeting.getMeetingId().equals(meetingId)) return meeting;
        return null;
    }

    /**
     * Find the information of Trade of given <code>meetingID</code>
     *
     * @param meetingId the id of meeting
     * @return String of information of meeting, otherwise, empty string
     */
    public String getMeetingInfo(int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null) return meeting.toString();
        return "";
    }

    /**
     * Creates a new the meeting with location, time, and userOrder. Increase TimesEdited of the user who creates the
     * meeting.
     *
     * @param location  the location of the meeting.
     * @param time      the time of the meeting.
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the Trade,
     *                  1 is the responder of the Trade.
     * @return The meetingId generated for the meeting.
     */
    public int createMeeting(String location, LocalDateTime time, int userOrder) {
        Meeting meeting = new Meeting(location, time, ++newestMeetingId);
        meeting.setMeetingConfirmed(userOrder, true);
        meeting.setTimesEdited(userOrder, meeting.getTimesEdited(userOrder) + 1);
        meetings.add(meeting);
        return meeting.getMeetingId();
    }

    /**
     * Confirm time and place of this meeting, given userOrder. Confirmation of the meeting can be made only when the
     * <code>user</code> has not confirmed and the other one has.
     *
     * @param meetingId The id of the <code>meeting</code>  <code>user</code>  want to perform action on.
     * @param userOrder An int representing the user that is performing this action. 0 is the initiator of the Trade,
     *                  1 is the responder of the Trade.
     * @param username  the username of current <code>user</code> who wants to perform action
     * @return <code>ActionReverter</code> for undoing this action, null if the action failed.
     */
    public ActionReverter confirmMeetingArrangement(int meetingId, int userOrder, String username) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null && canConfirmMeetingArrangement(meeting, userOrder)) {
            meeting.setMeetingConfirmed(userOrder, true);
            return new ConfirmMeetingArrangementReverter(meeting, username, userOrder);
        }
        return null;
    }

    /* Called by confirmMeetingArrangement to check the given meeting can be confirmed under this order or not. */
    private boolean canConfirmMeetingArrangement(Meeting meeting, int userOrder) {
        return !meeting.getMeetingConfirmed(userOrder) && meeting.getMeetingConfirmed(Math.abs(userOrder - 1));
    }

    /* Called by canConfirmMeetingOccurred and getLateMeetingIds to check whether a meeting can be arranged */
    private boolean meetingArranged(int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null) return meeting.getMeetingConfirmed(0) && meeting.getMeetingConfirmed(1);
        return false;
    }

    /**
     * Confirm that the meeting has taken place offline. This confirmation will be successful only when the time and
     * place of the meeting is both confirmed and the user has not confirm meeting occurred before.
     *
     * @param meetingId the id of the meeting the user want to confirm.
     * @param userOrder An int representing the user that is performing this action. 0 is the initiator of the Trade,
     *                  1 is the responder of the Trade.
     * @param username  the username of current <code>user</code> who wants to perform action
     * @return <code>ActionReverter</code> for undoing this action, null if the action failed.
     */
    public ActionReverter confirmMeetingOccurred(int meetingId, int userOrder, String username) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null && canConfirmMeetingOccurred(meeting, userOrder)) {
            meeting.setMeetingOccurred(userOrder, true);
            return new ConfirmMeetingOccurrenceReverter(meeting, username, userOrder);
        }
        return null;
    }

    /* Called by confirmMeetingOccurred to check whether this meeting is occurred or not. */
    private boolean canConfirmMeetingOccurred(Meeting meeting, int userOrder) {
        return !meeting.getMeetingOccurred(userOrder) && meetingArranged(meeting.getMeetingId());
    }

    /**
     * Get whether or not the meeting has occurred, that is, both users have indicated the meeting toke place.
     *
     * @param meetingId the meeting user want to confirm.
     * @return true iff the both users indicated the meeting occurred.
     */
    public boolean meetingOccurred(int meetingId) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null) return meeting.getMeetingOccurred(0) && meeting.getMeetingOccurred(1);
        return false;
    }

    /*
     * Difference between confirmation and maxMeetingEdit:
     * User can still try edit if he exceeded maxMeetingEdit, but the edition will fail and trade should be cancelled.
     * User cannot try edit at all if he has confirmed the time and place of the meeting.
     */

    /**
     * Gets whether or not a user can edit this meeting, given userOrder. An user can edit a meeting only when the user
     * has not confirmed the time and place of the meeting.
     *
     * @param meetingId the meeting user want to confirm.
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of
     *                  the Trade, 1 is the responder of the Trade.
     * @return a boolean, true if and only if the given <code>user</code> involved can edit this <code>meeting</code>
     */
    public boolean canEditMeeting(int meetingId, int userOrder) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null) return !meeting.getMeetingConfirmed(userOrder);
        return false;
    }

    /**
     * Edits the time and location of the current meeting. Edit is successful only if the user haven't exceed edit limit.
     *
     * @param meetingId   the id of meeting
     * @param meetingTime the meeting date and time
     * @param location    the meeting location
     * @param userOrder   a number indicating which User is performing the action.
     *                    1 if it is the initiator, 2 if it is the responder.
     * @param username    the username of current <code>user</code> who wants to edit meeting
     * @return <code>ActionReverter</code> for undoing this action, null if the action failed.
     */
    public ActionReverter editMeeting(int meetingId, LocalDateTime meetingTime, String location,
                                      int userOrder, String username) {
        Meeting meeting = getMeeting(meetingId);
        if (meeting != null && meeting.getTimesEdited(userOrder) <= maxMeetingEdits) {
            EditMeetingReverter reverter = new EditMeetingReverter(
                    meeting, meeting.getLocation(), meeting.getMeetingTime(),
                    meeting.getTimesEdited(Math.abs(userOrder - 1)), username, userOrder);
            meeting.setLocation(location);
            meeting.setMeetingTime(meetingTime);
            meeting.setMeetingConfirmed(userOrder, true);
            meeting.setMeetingConfirmed(Math.abs(userOrder - 1), false);
            meeting.setTimesEdited(userOrder, meeting.getTimesEdited(userOrder) + 1);
            return reverter;
        }
        return null;
    }

    /**
     * Gets a list of id from meetings that is late. A <code>Meeting</code> is late if <code>time</code> arranged and
     * confirmed has past for <code>maxMeetingLateTime</code> days.
     *
     * @return a list of id from meetings that is late.
     */
    public List<Integer> getLateMeetingIds() {
        List<Integer> lateMeetingIds = new ArrayList<>();
        for (Meeting meeting : meetings) {
            if (meeting != null && meetingArranged(meeting.getMeetingId()) && !meetingOccurred(meeting.getMeetingId()) &&
                    LocalDateTime.now().isAfter(meeting.getMeetingTime().withDayOfMonth(
                            meeting.getMeetingTime().getDayOfMonth() + maxMeetingLateTime))) {
                lateMeetingIds.add(meeting.getMeetingId());
            }
        }
        return lateMeetingIds;
    }

    /**
     * Set the given threshold type to the threshold value
     *
     * @param type  the type indicating which of the two threshold it is, maxMeetingEdits or maxMeetingLateTime
     * @param value the value of the threshold type to be set to
     */
    public void setThreshold(MeetingThresholdType type, int value) {
        switch (type) {
            case MAX_EDITS:
                maxMeetingEdits = value;
                break;
            case MAX_LATE_TIMES:
                maxMeetingLateTime = value;
                break;
        }
    }

    /**
     * @param type the type indicating which of the two threshold it is, maxMeetingEdits or maxMeetingLateTime
     * @return the maximum num of times a user can edit a meeting or the maximum time given to a user to confirm a meeting
     * after the time the meeting should have occurred.
     */
    public int getThreshold(MeetingThresholdType type) {
        switch (type) {
            case MAX_EDITS:
                return maxMeetingEdits;
            case MAX_LATE_TIMES:
                return maxMeetingLateTime;
        }
        return 0;
    }
}
