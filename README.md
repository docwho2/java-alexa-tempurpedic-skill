# Tempurpedic Ergo Premier Base Alexa Skill Control

Controlling your [TEMPUR-Ergo® Premier](https://www.tempurpedic.com/bases-and-foundations/tempur-ergo-premier/) with an Alexa skill could be as
easy as deploying this to AWS and setting your base IP Address.  At least this was the case for me because my home network is integrated with AWS
and reachable within a VPC.
There are however some challenges you are likely to face, mostly around network access and how to allow the code to send UDP packets to your bed
from a topology standpoint.  You will need to understand how to configure your home router to likely assign static IP addresses and perform static
mappings from the outside (Internet) to your bed's IP address on port 50007.

## Limitations

* This skill has no account linkages, thus the skill must know and can only be hard coded to one bed IP Address
* Due to above, this skill must be deployed to your developer account so that it is made available to devices you own (Echo, Echo DOT's, etc.) 
* Currently there is only support for memory positions 1 through 4 and to go to flat position, adding more (like vibration shouldn't be too hard)

## Prerequisites
* Java, GIT, and Maven installed in order to pull the code, compile, and deploy the skill.
* Your bed should have a static IP address and be configured onto your local wireless network. 
Refer to the [Owners Manual](https://www.tempurpedic.com/documents/9/Tempur_Ergo_Premier_Owners_Manual.pdf) for information on setup with your
"Home Network".  By default the Wi-FI module has it's own SSID you connect to with the app.
* Assign Static IP to Wi-Fi module (recommended).  Obviously if the bed's IP address changes from time to time, the skill won't be able to contact
the bed, so you should configure your DHCP server to hand out a static IP address for the base.  Simply open the mobile app and it will tell you the
MAC address so that you can configure your DHCP server as needed.  It will also tell you the current IP address if you just want to use that and hope
it doesn't change lol
* [Developer Account with Amazon](https://developer.amazon.com) to deploy the Alexa Skill with
* [Amazon AWS Account](https://aws.amazon.com) to deploy Lambda function and knowledge of VPC Networking if you intend to use AWS (instead of servlet engine elsewhere)
* [Servlet Engine](http://www.servlets.com/engines/) to deploy skill as a speechlet if you're not going to be using AWS Lambda.  This would be a viable
option if you have some kind of server running locally in your house.  Using this option, you will need to configure the Alexa Skill to contact your
servlet engine on the outside IP address of your home router and have a static mapping rule to support this.


## Installation

Having never done the servlet deployment model for Alexa, I won't have much detail in the steps, just an overview of what needs to be done for that model.

### AWS Lambda Scenario

Check out the code into a working directory
```
git clone https://github.com/docwho2/java-alexa-tempurpedic-skill.git 
```
TODO...

### Servlet (Speechlet) Engine Scenario

TODO...