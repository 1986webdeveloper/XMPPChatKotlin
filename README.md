	 	 	
XMPP Chat

This Project demonstrates use of XMPP for instant messaging. Tested and developed in all versions till Oreo. We will use Openfire server which is a real-time-collaboration (RTC) server provided by Ignite Realtime under the Open Source GPL. XMPP i.e, Extensible Messaging and Presence Protocol is the protocol that Openfire uses. Some of the largest messaging providers use like WhatsApp and Google Talk uses various forms of XMPP based protocols in their backend systems. WhatsApp makes use of Ejabberd (XMPP) server. Hence if you wish to provide real time messaging service to your clients just as WhatsApp and Google Talk, here is an XMPP Chat Demo which readily fulfills your requirement.
We have used Smack as a client library for instant messaging and presence. Smack is a pure Java library which can be embedded readily into our applications to use XMPP. 

To use the demo readily, import the project in your IDE and apply the following changes:

	
In 	strings.xml replace “DomainName” with your domain name and 	“xxx.xxx.x.xx” 	with your IP Address.

 <string name="txt_domain_name">DomainName</string>
<string name="txt_server_address">xxx.xxx.x.xx</string>

To make this demo run, we need to install Open Fire server.


To install Open Fire Server please follow the steps described below:


Step 1: Download the server setup from  <a href="https://www.igniterealtime.org/downloads/download-landing.jsp?file=openfire/openfire_4_4_0.tar.gz">here </a>


Step 2: Copy the downloaded zip file to the location where we want to setup the server.

Step 3: Open Terminal. Navigate to the folder where zip file is saved.

Step 4: Extract the zip file using the following command:
tar -xvzf openfire_3_6_4.tar.gz

Step 5: After extracting, navigate to the bin directory of the extracted folder.

cd openfire/bin/

Step 6: Make Openfire executable

chmod +x openfire

Step 7: To Check status

./openfire status

It will return whether it is connected or stopped. If the status is stopped, we can execute below command to start the open fire server.

./openfire start


Step 8: Server Configuration:
Now visit the http://localhost:9090/ in browser window to start the server configuration.

Once the server is loaded below window will be seen.




Step 8.1: Select language for the server and click continue.


Step 8.2: In the next screen, we can see our domain name in this screen. Note the domain name as it will be required while setting up the client. Click continue.


Step 8.3 In the next screen, we can configure our database settings.Here embedded database is selected. Click continue after selecting database. database. After selecting and configuring the database, click on continue.

Step 8.4: The next screen we will see is profile setting screen. Here, we can save our profile as either admin or user. Currently, we have selected default. Click on continue after selecting profile settings to create the admin access.
Here we will be asked to set the password for admin access. Admin name will be “admin” by default.


Step 8.5: After setting password, we will be asked to login as admin to see server data. Click on Login. A new screen will appear which will ask you to enter admin Id and Password.

Note: Your first time login will fail. So don’t Worry!! Refresh!! Enter your Id and Password again and you are on your way!!


Step 8.6: After successful login, we can view the admin panel of our Openfire server.


Kudos!! We have configured our own Openfire server!!


Tools Used:

Android Studio 3.1.3
Openfire 4.2.3
Smack 4.2.4

![alt text](http://dev.acquaintsoft.com/xmppchat.gif)

