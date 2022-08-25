[comment]: <> ( Documentation for MakeMyBudget)

# ABSTRACT

As the name itself suggests, this project is an attempt to manage our daily expenses in a more
efficient and manageable way. Sometime we can’t remember where our money goes. And we can’t handle
our cash flow. For this problem, we need a solution that everyone can manage their expenses. So we
decided to find an easier way to get rid of this problem. So, our application attempts to free the
user with as much as possible the burden of manual calculation and to keep the track of the
expenditure. Instead of keeping a diary or a log of the expenses, this application enables the user
to not just keep the control on the expenses but also to generate and save reports. With the help of
this application, the user can manage their expenses on a daily, weekly and monthly basis. Users can
insert and delete transactions as well as can generate and save their reports.

The graphical representation of the application is the main part of the system as it appeals to the
user more and is easy to understand.

# Tools and Technologies

The application is developed using the following tools and technologies:

## Software

Android Studio

## Database Server

SQLite Database

## Client-Side

Android Mobile

# Flaws in the current system

- No offline data storage Overcrowded interface and inappropriate color schemes Unable to create
  multiple accounts Users get interrupted by annoying advertisements No privacy function.
- Unable to generate PDF reports Unable to set budget mode (Weekly/Monthly)
- Features of Daily Expense Tracker Android App Project Create multiple accounts/budget Delete
  account Background color Modify Transactions Offline datastore Passcode security Selecting budget
  mode(
  Weekly/Monthly)
  Generate reports as PDF files Fully customizable categories Cash flow (Pie/Bar/Graph)
  Expenses percentage Carryover Show transaction note Currency Symbol Modules of Daily Expense
  Tracker Android App Project The modules which are currently covered are:

Add income/add expense

This module deals with adding income and expenses. The user has both options available for adding
income and expense. But there is a condition if the user has not entered the amount yet then the user
can’t enter expenses. When the user enters any transaction then that transaction will be added in
both Spending and Transaction tabs. If the user wants to delete that transaction then the user has
to long click the transaction available in the spending tab then that transaction will be deleted
from both tabs.

Modify Transactions

If the user wants to delete that transaction then the user has to click the transaction available in
the spending tab then that transaction will be deleted from both tabs.

Filter Transaction view

In the transaction tab, the user can filter the transactions. In the Spinner, users can select the
day, month and year and then click the filter button and according to the day, month and year
transactions will appear. If the user wants to filter the transactions only on the basis of day, for
example, user-selected Monday then all transactions will appear that were made on Monday.

PDF Report

In the transaction, the tab user has an option available for creating a report in PDF. Users click
on the PDF button then PDF report will be generated and the user can view that report and that
report will be automatically saved in the device.

Multiple Accounts

Users can create multiple accounts. In the account tab. User has the option available for creating a
new account. Users will click the “+” sign button then a dialog will appear on the screen and the
user can enter the name of the account then that name will be saved in the account tab. If a user
wants to delete the particular account then the user has to l click the account name user want to
delete. Then that account will be deleted.

Transactions overview as Pie/Bar/Graph

The user has three options available for graphical representation. When the user rotates the device
then the pie chart will appear on the screen and also switch is available on the screen when the
user will click on the bar chart will appear on the screen and when the user clicks on graph then
Graph will appear on the screen.

Themes

At the top bar, the user has a setting option when the user clicks that then background option will
appear user can select different background colors. After selecting the particular color background
color will be changed.

Passcode

The passcode is available in setting option at the top bar. When the user clicks on the passcode
switch when the user switches on then the passcode screen will appear and the user can choose the
password and that password will be saved in the database. After that when the user will open the
application user have to enter the passcode and that passcode will be matched with passcode saved in
the database. If the user entered the wrong passcode then the error message will appear.

Currency Symbol

The currency symbol option is available at the top bar setting button. Users can select different
currency symbols. If the user selects the dollar symbol then that symbol will appear on the spending
tab.

Functional Requirements

Identifier Requirement Req:1 Add transaction This application will allow adding transaction. Req:2
Delete transaction This application will allow the deleting transactions. Req:3 Amount spent in
categories This application will allow adding the amount spent in a particular category. Req:4 View
all transactions This application will allow viewing all previous transactions Req:5 Total amount
This application will allow seeing the total amount, the amount spent in different categories and
balance left. Req:6 Overview This application will allow viewing overall transactions. Req:7 Graph
representation This application will show the graph which will help the users to visualize the
budget. Req:8 Pie representation This application will show the pie. Req:9 Bar representation This
application will show the bar. Req:10 Change background This application has the option to change
the background. Req:11 Passcode This application has the option to set a passcode for security. Req:
12 Add multiple accounts This application Req:13 Transaction time/date This application has the
ability to show the transaction time along with the date on which it was created. Req:14 Currency
symbol This application has many currency symbols as per user requirements. Req:15 Reminder This
application has the option to set a reminder to make the transaction. Req:16 Delete account This
application will generate PDF reports of the transactions. Req:17 PDF report This application has
the option to view and filter transactions by day, month and year. Req:18 Note This application has
the option to add a note about income and expenses.