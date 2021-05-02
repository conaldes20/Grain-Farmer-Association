# Grain Farmer Association Management
There are various farmer groups in Nigeria. In a state, there could be many groups. Each group has officers managing farmers in the group. In view of the fact that farmers are many in each state, aggregation points are also created. The umbrella authority gives farmers inputs and loans. In addition, production and income of farmers are monitored.

As of today, all necessary information in the fields is collected through phone calls and text messages. Clearly, these methods are not only inefficient but will make collation and analysis of data difficult.
This is a demo application and it is to be presented this coming week. 

All the forms for capturing data are in a page. When it loads, all group select boxes get updated with groups in database. If a new group record is entered, all select boxes get updated after the record is submitted. It is not possible to enter records into officer, aggregation and farmer tables if there is no group. Likewise, it is not possible to enter records into farm, farminput and loan tables. A user has to login to enter data and all user’s activities are recorded in audittral table. Reports are available to everybody.

To enter a farmer’s record for instance, a group is selected, then, a state is selected. If the state selected is not the same as the state where the group is, alert message is displayed. If an appropriate state is selected, all local government in the state are displayed in local-govt select box. In all the forms, users are properly guided.

In addition, all fields must have data and satisfy validation criteria before written into database. Other constraints to ensure the integrity of the data are contained in the database definition. 

## Built With
-	Pure JavaScript
-	jQuery
-	Bootstrap
-	MySQL (wampserver3.2.3_x86)
-	Websocket Technology
-	Glassfish5
-	Java 8 
-	netbeans-8.2-windows

## Get Started
-	Download jdk-8, glassfish5, netbeans-8.2-windows and wampserver3.2.3_x86 
-	Double click and follow instructions to install. In case of glassfish5 , unzip into a an appropriate folder.
-	Create grfmassocdb database in wampserver with fmassocdb.sql script. Set your own user and password. 
-	Open NGrainFarmerAssociation folder in netbeans and explore the codes.
-	Once the wampserver is running with the database, run start_glassfish.bat. Edit the content properly. Browse [http://localhost:8080/FarmerAssociation/](http://localhost:8080/FarmerAssociation)
-	In case the program is recompiled, instructions for deploying and undeploying are contained ‘How to deploy NGrainFarmerAssociation war.txt’ file. 

