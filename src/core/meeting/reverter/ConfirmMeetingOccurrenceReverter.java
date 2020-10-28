package core.meeting.reverter;

import core.meeting.Meeting;
import core.reverter.ActionReverter;

/**
 * Reverts the action of a <code>User</code> confirm the occurrence of a <code>Meeting</code>.
 */
public class ConfirmMeetingOccurrenceReverter extends ActionReverter {
    private final Meeting meeting;
    private final int userOrder;

    /**
     * Constructs an ConfirmMeetingOccurrenceReverter with <code>meeting</code>, <code>username</code> and
     * <code>userOrder</code>.
     *
     * @param meeting   the <code>Meeting</code> the <code>User</code> with <code>username</code> confirmed the
     *                  arrangement of.
     * @param username  the username of the <code>User</code> who did the action.
     * @param userOrder an int representing the user that is performing this action. 0 is the initiator of the
     *                  trade(related to this meeting), 1 is the responder of the trade.
     */
    public ConfirmMeetingOccurrenceReverter(Meeting meeting, String username, int userOrder) {
        super(username);
        this.meeting = meeting;
        this.userOrder = userOrder;
    }

    /**
     * Undo the action of <code>User</code> with <code>username</code> confirm the occurrence of <code>meeting</code>.
     * Cannot undo if the other <code>User</code> involved in <code>meeting</code> has confirmed the occurrence
     * of <code>meeting</code>.
     *
     * @return whether the undo is successful or not.
     */
    @Override
    public String execute() {
        if (meeting.getMeetingOccurred(Math.abs(userOrder - 1))) {
            return "The other user has confirmed occurrence of this meeting. Meeting completed thus undo failed.";
        }
        meeting.setMeetingOccurred(userOrder, false);
        return "Revert user confirm meeting occurrence succeeded!";
    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Confirm Meeting Occurrence";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return getAssociatedUsername() + " confirmed occurrence of meeting " + meeting.getMeetingId() + ".";
    }
}