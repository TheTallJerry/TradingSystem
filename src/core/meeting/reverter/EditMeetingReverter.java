package core.meeting.reverter;

import core.meeting.Meeting;
import core.reverter.ActionReverter;

import java.time.LocalDateTime;

/**
 * Reverts the action of a <code>User</code> editing time and place of a <code>Meeting</code>.
 */
public class EditMeetingReverter extends ActionReverter {
    private final Meeting meeting;
    private final String location;
    private final LocalDateTime time;
    private final int timeEditedOtherUser;
    private final int userOrder;

    /**
     * Constructs an EditMeetingReverter with <code>meeting</code>, <code>timeEditedOtherUser</code>,
     * <code>username</code> and <code>userOrder</code>.
     *
     * @param meeting             the <code>Meeting</code> the <code>User</code> with <code>username</code> confirmed
     *                            the arrangement of.
     * @param location            old location before the <code>User</code> with <code>username</code> edited.
     * @param time                old time before the <code>User</code> with <code>username</code> edited.
     * @param timeEditedOtherUser the number of time the other <code>User</code> has edited <code>meeting</code> as this
     *                            action is done.
     * @param username            the username of the <code>User</code> who did the action.
     * @param userOrder           an int representing the user that is performing this action. 0 is the initiator of the
     *                            trade(related to this meeting), 1 is the responder of the trade.
     */
    public EditMeetingReverter(Meeting meeting, String location, LocalDateTime time, int timeEditedOtherUser,
                               String username, int userOrder) {
        super(username);
        this.meeting = meeting;
        this.location = location;
        this.time = time;
        this.timeEditedOtherUser = timeEditedOtherUser;
        this.userOrder = userOrder;
    }

    /**
     * Undo the action of <code>User</code> with <code>username</code> edit time and place of <code>meeting</code>.
     * Cannot undo if the other <code>User</code> involved in <code>meeting</code> has edited or confirmed the
     * arrangement of <code>meeting</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (meeting.getMeetingConfirmed(Math.abs(userOrder - 1)) ||
                meeting.getTimesEdited(Math.abs(userOrder - 1)) != timeEditedOtherUser) {
            return "The other user has edited or confirmed arrangement of this meeting. Undo failed.";
        }
        meeting.setTimesEdited(userOrder, meeting.getTimesEdited(userOrder) - 1);
        meeting.setLocation(location);
        meeting.setMeetingTime(time);
        meeting.setMeetingConfirmed(userOrder, false);
        meeting.setMeetingConfirmed(Math.abs(userOrder - 1), true);
        return "Revert user edit meeting succeeded!";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Edit Meeting Time and Place";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return getAssociatedUsername() + " edit meeting " + meeting.getMeetingId() + ".";
    }
}
