# Introduction
Welcome! This is a trading software designed by a group of students at the University of Toronto while taking CSC207H1, <em>Software Design</em>.

## Set up
To compile our software, you will need to download a jar file(mail.Jar) here: https://github.com/javaee/javamail/releases/download/JAVAMAIL-1_6_2/javax.mail.jar. 
This allows us to send email notifications. You will also need JDK 8 for compilation. For simplicity, download IntelliJ community version and open the repository as a project, set JDK version to 8, and add the aforementioned jar file to project dependency. 
Then click compile, and the compilation is done! You should be able to run our application now.

(Important!: the program wouldn't be able to run if mail.Jar is not downloaded.)

This application is very straight forward to use, as it is eqquiped with GUI. There are three types of intended users for this application: normal users (those who sign up to trade), guests (those who want to browse without signing up), and admins (the administrators); the tutorial below summarizes our application for all three types of users.

## Run

To run the program, simply compile and run the main method.

Note: Upon execution for the first time, DataBundle.ser will appear in the directory - it stores the data required to save the progress of the trading system.

To delete all previous data stored in the program, simply delete DataBundle.ser before running the program.

You can hit <em>Return to previous page</em> to go back to the previous page, you can also log out of your account by pressing the <em>Log Out</em> button in the main menu.

### Registration
If you do not have an account, you can register by pressing this button.
Follow the instructions and type the username and password you'd like.
If you choose to create a User Account, it can be used right away.
If you choose to create an Admin Account, you also need to enter you email. 
The initial admin will need to first approve your request, and an email will then be sent to you.
 

### Login
If you already have an account, input your username and password here to log in.
You can also enter as a guest, but none of the action you did will not be stored.
Initial Admin has the default username: **admin**, and password: **password**.


### Credits
Contains information about who made this system and when.

### Exit
Exit the system.

## After Logging in as a User

### Trade
Enter our Trading Screen.

#### View your trades
Pressing the button "**_View Trades_**" gives you all trades that you are involved in. By double-clicking on the trade, 
you can see more information about the trade
By choosing and trade and right-clicking it, you can do actions such as view/update meeting, agree/refuse to a trade requests. 
If you choose to view/update meeting, you will be directed to a new page where you can view the current meeting information, 
create a meeting, edit a meeting, confirm a meeting arrangement or confirm that a transaction has occurred.

Note: If the trade you initiated is not yet accepted by another user, you cannot create a meeting regarding this trade.
Also, a meeting needs to first be created in order to be edited.
Once a meeting has been created, a user can choose to edit or confirm the meeting arrangement.

#### Request a new trade
You will be displayed with all items in the public item inventory, all the items that you can lend to other people(i.e.
your item available list) and all the items in your wishlist. You can request a new trade by double clicking the item 
in public item inventory that you would like to borrow, and you will be presented with a popup menu, giving you the 
option to request a trade with this item.

Explanation of the "Items You Might Like" button: we look at all your past trades, and find the item type that you most
frequently borrow(note that users can choose to enter item type when creating an item). Then we tell you all the items 
with this item type in the public item inventory)

After you choose to request a trade with an item, you will see a popup menu allowing you to
1. Enter id of the item that you would like to lend (note that this is only needed if you want to request a two-way trade.
If this part is left blank, then the trade will be a one-way trade. You can consult part3 when making this decision)
2. Whether you would like the trade to be permanent or temporary
3. "See Item they may like" means: We find the person that owns the item you wanted to borrow and find the items that are
both in their wishlist and your available list (note that this helps to increase the possibility that the trade be 
accepted by the other user)
4. Hit <em>Next</em> to request this trade

#### Three most recent trading partners
Get 3 users that you trade with most frequently.

#### Three most recent traded items
Get 3 items that you recently traded.

#### View Trade Threshold Values
Check current threshold values related to trade.

### <em>Account</em>
Enter your Account Center.

* <em>Item Creation Request</em>
Request to create an item so that you can lend it to others. If you didn't follow the requirement for
item name, type, description you will fail to request. Hit the confirm button and it will tell you the
requirement if you violated it.

* <em>Modify Wishlist</em>
It will browse all items you can lend for you to add in wishlist. Add item to your wishlist by enter the item
Id and hit <em>Add</em>. You can also remove items in your wishlist by hit <em>Remove</em>. Go to <em>Get Account Info</em> to check
what is in your wishlist.

* <em>Change Password</em>
Change your current password.

* <em>Get Account Info</em>
It will give all basic information about your account. Including:
1. Username and password
2. All of the items you can lend
3. All of the items in your wishlist
4. Your city
5. All username of Users you blocked
6. Number of item you have lent
7. Number of item you have borrowed
8. Is your account frozen
9. Is your account on-Vacation
10. Number of credit you have(Trust Credit)

* <em>Change On-Vacation Status</em>
It will switch your on-vacation status to on/off. If this status is on, it means you cannot trade with others
and others cannot trade with you.

* <em>Add/Remove Users From BlockList</em>
It will browse your current BlockList. You can add users to your BlockList or remove them by entering
their username. You cannot receive message from them, see their items. They cannot see your items either.
* <em>Set Your City</em>
By setting your city, you will only see items from users living in the city you set when you are looking for
items to trade. You can delete your city by hitting <em>Delete City</em>

* <em>Request Account Unfreeze</em>
It will send request to admins for them to unfreeze your account. Request cannot be sent if your account is
not frozen.

* <em>Report Users</em>
You can report a user to admin here by entering the user's username and your reasoning.

### Message
Enter your message box.

* <em>View Received Message</em>
See message you've received so far. It includes private message you've received and announcement admin
has sent.

* <em>View Sent Message</em>
See message that you sent to others.

* <em>Send new message</em>
Send private message to other User.
### Log Out
Go back to login screen.

## After logged in as a Admin
### View Requests
(Try resize the window if you cannot see something)
Here, you can view different type of requests send by users. Including:
1. Requests to add an item to the user's item available list.
2. Requests from users to unfreeze their account.
3. Requests from users to report other users. (if the admin accepted this request, the account of the user
being reported will be frozen)
4. Requests to create a new admin account.
By double click any request description, you can see more details about the request and choose to
either accept, deny the request or go back.
### View & Modify Thresholds
On the first line, choose the threshold that you want to change,and then the current value of the threshold will be
 displayed on the second line. Enter the new threhold limit that you desire on the third line, and then
  hit the "<em>Change</em>" button to change the threshold.
### Create New Admin
You can only do this if you are the initial admin. Enter the username and password to create a new admin account
### Search Information by username
Enter the username that you would like to search on the second line. Press search, and  information about the user and 
their trades will show up.
### Undo Actions
You choose to undo actions either by user or by action type. If you choose to undo actions by user, first select the 
user in the box on the left, and then all their past actions will appear in the box on the right. Choose the action that you
want to undo by double-clicking the action in the box, and then click the undo button in the popup menu.
### Send Announcement
Send message to all users in the system, including guest(demo) users.
### Change Password
Change your current password.
### Check Violation
The place where an admin can freeze or unfreeze users based on their violation of threshold limits. Admin can also check current forzen user and unfreeze them here.
### Log Out
Go back to the login screen.


##Assumptions
* The Threshold <em>[Maximum Number of Trades in a Week]</em> considers each week as from Monday to Sunday.
A user cannot initiate a one-way trade where they only lend. (i.e we define a one-way trade to be the trade where you 
only borrow.)
* Create a new meeting is considered the first edit of this meeting.
* The Threshold <em>[Minimum lend and borrow difference]</em> is the difference between number of items lend and items borrowed, with items lend being the larger one.
For instance, if the threshold value is 3, then for an arbitrary user, having lent 4 times and borrowed twice would 
not satisfy the thresholds (since 4 - 2 = 2 < 3), where having lent 4 times and borrowed 0 times would satisfy it (since
4 - 0 = 4 > 3)
* An incomplete trade means any trade with ONGOING status, ABANDONED status, or CANCELLED status.
* <em>[Three most recent trade]</em> include items that the user lend to others.
* A user cannot send a private message to admin, only to another user. 