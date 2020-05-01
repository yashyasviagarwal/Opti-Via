# Opti-Via


This application finds the safest route to your destination and plots it on the map for you.
This application uses KNN clustering to cluster areas with high crime rate together(taken on Crimes in UK Dataset), and then the algorithm used tries
to find the safest path between the source and the destination by the help of some predefined weights of the waypoints in between,
then it plots the waypoints on google maps.
It also has a SOS feature added which sends an sms alert to the Emergency Services.


# App Demo




![Application Demo](demo/appdemo.gif)




# SOS Demo
![SOS Demo](demo/sosdemo.gif)





## Tech Stack:
1. ANDROID APPLICATION (JAVA)
2. NODEJS
3. PYTHON3

The Backend is hosted on heroku: https://morning-shelf-67965.herokuapp.com/

NOTE : Add the google maps API key wherever specified in order to make this work propoerly.
