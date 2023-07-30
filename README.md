Hey this is my first time uploading to github so this is a test! I made this project for a CS class and my school and I just wanted to publish it. 

This program connects underclassmen "riders" to upperclassmen "drivers." For each rider, this program provides a list of the five drivers that are closest to their house. For each driver, the program also provides a list of the five closest riders to the address that they submitted. Additionally, riders have the ability to opt into a parent driven carpool group. If they opt-in, the program will also provide a list of the five closest riders that opted-in to the parent driven carpool group, in addition to the list of five drivers. The student's contact information and parent contact information is also included in the list.

I collected students information through a Google Forms survey and imported it to Google Sheets. I used Sheets as a database for this project. Before running the Java code that is included in this project, I processed the data in Sheets by converting the addresses to their geolocations using Google's App Script. 

In this Java program, I used the Haversine formula (as opposed to an expensive mapping API), which calculates the straight line milage between two addresses. I iterated through all the options and stored the five riders/drivers with the shortest distance. The program completes this process for each student in the database. When the results are generated, they are sent back to the Google Sheets database. I used Google Sheets API implemented through a Gradle build to read/write cells through batch updates to reduce run-time. 

Once the program finishes and the process is completed, I distribute the results to the participating students through email using Mail Merge (Google's App Script).
