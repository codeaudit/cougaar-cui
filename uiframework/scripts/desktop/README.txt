Blackjack Assessment UIs
Installation, Configuration, and Operation

SCOPE
-----

The table and line plot assessment UIs provide views of data from a database
that is populated by an Enterprise Java Bean (EJB).  The EJB acquires it's data
from a cougaar society.  This readme covers the installation,
configuration, and operation of the assessment UIs; not the EJB that populates
the database.


INSTALLATION
------------

Method 1: From checked out uiframework cvs module:
- cd to an installation directory
- cvs checkout uiframework
- cd to uiframework/scripts/desktop
- obtain jars referenced in buildui.bat and place them in uiframework/lib
- run buildui.bat

Method 2: From uiframework zip obtained from ALP web site:
- place uiframework.zip in installation directory
- jar -xvf uiframework.zip
- cd to uiframework/scripts/desktop
- obtain jars referenced in runui.bat and place them in uiframework/lib
- edit runui.bat as follows ->
    change set CP=..\..\classes to set CP=..\..\..\lib\uiframework.jar


CONFIGURATION
-------------

The only configuration required for the table and line plot UIs is to set the
database type, URL, username, and password.  These are all set in
uiframework/scripts/desktop/runui.bat.

To use these UIs with an Access database you must use jdk1.3 or higher and
setup the Assess database as an ODBC datasource.


OPERATION (cliff notes)
---------

Execute runui.bat
Select the type of UI that you would like to launch (Map, Table, or Line Plot).

Map UI:
- Adjust time slider to view the time adjusted locations of the society
  organizations.
- Drag a box to zoom in on a particular location.
- To bring up another UI configured to show data related to a displayed
  organization at a time range centered at the currently selected time:
    - change mouse mode from Navigation to Gestures
    - right-click on organization icon and select UI.

Table UI:
The table UI can be used to
- show raw or aggregated data metrics from the society
- show a stoplight chart based on raw or aggregated metrics from the society

Four variables can be adjusted to define the data shown in this view:
Metric(list), Item(tree), Organization(tree), or time(range).
Metric is always a fixed variable, the other three variables can be set as the
x or y axis of the table or as fixed variables.  The type and values of these
variables are selected by either using the controls at the bottom of the UI or
by right clicking on the axis labels.

To put the table in stoplight mode change the view radio button from Value to
either Color or Both (i.e. both Color and Value).  When UI is in color only
mode checkbox options are available to compress the cells to fit in the
viewport such that vertical and/or horizontal scrollbars are not needed.  The
stoplight thresholds can be dynamically adjusted using the compound slider at
the bottom of the UI.

Right-click on a data cell to bring up another UI configured based on the
row and column of the cell selected and on the current settings of the fixed
variables.  Double click on a cell to bring up a pre-configured line plot
chart.

Line Plot UI:
The Line Plot UI is used to plot raw or aggregated data metrics from the
society.

Four variables can be adjusted to define the data shown in this view:
Metric(tree), Item(tree), Organization(tree), or time(range).
Time is always on the x axis, the other three variables can be set as the
either y axis of the table or as fixed variables.  The type and values of these
variables are selected by either using the controls at the bottom of the UI or
by right clicking on the axis labels.
