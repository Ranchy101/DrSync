Dr. Sync is HIPAA Compliant Java and C++ Application that receives real-time external sensor data wirelessly and can be sent to any existing database. For the purpose of this demo, data is sent to AWS RDS (MySQL database) along with patient information. Data is then organized on a secure web application hosted by Retool. Compatible with ANT+ and/or Bluetooth Sensors including heartrate, temperature, and blood pressure. Application UI designed using Swing.  

Prerequisites:  
Latest Version of Java  
Visual Studio 2013 (or the dll file requested at launch)  

Instructions:  
Extract Files  
Launch DrSync.exe  

Limitations:  
This contains only the heartrate functionality. However, this can be used as a base template for additional sensors such as heartrate, temperature, and blood pressure. The UI buttons for the extra sensors are already made along with the selection logic in the java program. You will need to download the specific C++ sensor file from the ANT+ SDK API libraries found at https://www.thisisant.com/developer/resources/downloads/#software_tab  
  
While the original use of this project was to send to a MySQL database, there is only a few lines of code that will need to be altered to make it work for your database of choice. The front end web portion of this project is not included in this repository as it is hosted on an application called Retool.

Demo:  


Creators:   
Amy Raval  
Daniel Ranchpar  
Anthony Schanen  
Anahit Meloyan  
Angel Reyes  
Glen Roe  

