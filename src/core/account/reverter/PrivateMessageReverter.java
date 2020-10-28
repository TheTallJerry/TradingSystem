package core.account.reverter;

import core.account.Account;
import core.reverter.ActionReverter;

/**
 * A reverter responsible for reverting private message send by an <code>Account</code>.
 */
public class PrivateMessageReverter extends ActionReverter {
    private final Account sender, receiver;
    private final String message;

    /**
     * Construct a Action with a list of <code>meetings</code> and <code>reportedUsers</code>
     *
     * @param sender   the sender of this message
     * @param receiver the receiver of this message
     * @param message  the content of this message
     */
    public PrivateMessageReverter(Account sender, Account receiver, String message) {
        super(sender.getUsername());
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Reverts the action of an <code>Account</code> sending message to another <code>Account</code>. Action undo will
     * only be successful if the string message sent is in the sender's list of <code>massageSent</code>.
     *
     * @return a string message that will be sent to the sender's list of <code>massageSent</code>
     */
    @Override
    public String execute() {
        if (!sender.getMessageSent().contains(message))
            return "Action undo failed! Message not in sender's message received list";
        StringBuilder response = new StringBuilder();
        if (sender.deleteFromMessageSent(message)) {
            response.append(message).append(" has been removed from sender ").append(sender.getUsername()).append(". ");
        }
        if (receiver.deleteFromMessageReceived(message)) {
            response.append(message).append(" has been removed from receiver ").append(receiver.getUsername()).append(". ");
        } else {
            response.append(message).append(" does not exist in the message box of receiver ").append(
                    receiver.getUsername()).append(". ");
        }
        return response.toString();

    }

    /**
     * Gets the type of action it will undo.
     *
     * @return the type of action it will undo.
     */
    @Override
    public String getActionType() {
        return "Sent private message";
    }

    /**
     * Gets a description of action it will undo.
     *
     * @return a description of action it will undo.
     */
    @Override
    public String getActionDescriptionReverted() {
        return "Message sent: <br>" + message;
    }
}
