#Implementations
This document explains all the extensions in our projects.
##Mandatory Extension
###Admin Undo Actions
Admin can undo every action taken by regular users that can reasonably be undone.
This includes:
1. [Account] Undo user set password if user hasn't set it again.
2. [Account] Undo user set city if user hasn't set it again.
3. [Meeting] Undo user confirm meeting arrangement if no one involved in the trade has confirmed meeting has occurred.
4. [Meeting] Undo user confirm meeting occurred if the other user hasn't confirm meeting occurred too.
5. [Meeting] Undo user edit meeting if the other user hasn't edit it again.
6. [User] Undo user add other user to block list if the user hasn't remove the blocked user out of block list.
7. [User] Undo user delete other user from block list if the user hasn't add it back in.
8. [User] Undo user set city if the user hasn't set the city again.
9. [User] Undo user switch on-vacation status on/off if the user hasn't switch it again.
10. [Item] Undo user add item to wishlist if the user hasn't remove the item again.
11. [Item] Undo user delete item from wishlist if the user hasn't add it again.
12. [Item] Undo user item request(to add to items the user can lend).
13. [Trade] Undo user deny a trade request from other user.
14. [Trade] Undo user request a trade if the other user hasn't react yet.

However, admin cannot undo:
1. [Meeting] Create meeting: It will change trade information and considered the first edit of the meeting.
2. [Trade] Agree to a trade: It will change user information as well as trade information. 

We considered the above 2 actions as unreasonable to undo based on our assumption and design. 

###Suggestion
Suggest item if it is both in the other person's wishlist, and the user's lending list. A button will appear when you go to the request trade screen for you to browse the suggestion.

###Guest(Demo) User 
There exists a special user in the system which is the guest user account with its special username and password. It allows clients to log in without a username or password. Logging in as a guest allows the client to explore without his/her action being saved.

###Adjust Threshold Values
There is a threshold adjusting screen where admins can adjust all threshold values in the system.

###On Vacation Status
Users have a new status called on-vacation. They are free to turn it on or off at anytime. If the status is on it means that they cannot trade and others cannot request to trade with them.

##Selected Extensions
###Home City
Users can enter the city they are living in, such that they only see items from users living in the same city when they want to trade. It can be changed or deleted at anytime.
###Announcement
Admin can send announcements to all users, including guest account. Therefore users do not miss any events or updates in the program. Guest can also get a feeling as part of the community from it.
###GUI
This program is implemented using Graphic User Interface (GUI).

##Creative Extensions
###Email Your Admin Request Result
You can register an admin account at anytime. As soon as the initial admin processed your request, an email will be send to you, so make sure you enter the right email address!

###Message Box
Users can send private messages to other users. They have a Sent box which contains all the messages they sent, and a Received box which contains all the messages they have received.

###Block List
Users can block other users if they feel like it. If they blocked a user:
 1. That user can still send messages to them, but they will not receive these messages.
 2. They will not see items from any user they blocked while looking for items to trade, and that user will not see items from them either.

###More Item Suggestion
Cannot decide what to borrow? See what the system suggests! Our system calculates at most 3 item types that you traded most frequently, and gives you suggested items to borrow.

###Report Users
If users feel another user's behaviours are concerning, they can report that to admins with their reasoning. Admins will process the report and may froze that user.

###Credit System
There is a trust credit system in the community. It has a default value when they just registered their account.
Good trading behaviours(complete trade) will increase their credit, and bad trading behaviours(abandon a trade, trade being cancelled) will decrease their credit. Credit can be seen when others are looking for items to trade. Increase your credit to show you are a trustworthy trader!

###Admin Searching Tool
Due to some concerns with user privacy, this searching tool can only be used by admins. Admins can search user information by their username, including their account information, trades, and meetings relate to them. 