# m1-opa-Indoor-Localization-App

Project by : @othboury - @papemor94 - @azzibert

As part of the Master DID (Data Development and Engineering) at the University of Toulon, we were led to carry out a very ambitious project that allowed us to put into effect our knowledge in mobile development, web development, machine learning, and Docker. The project consists of creating an android application allowing the user to locate themselves within the university's campus (Indoor localization) using Wifi Networks.

The main functionalities of the application:

      - User Login
      - User indoor localization (predicting the position)
      - User feeding the learning algorithm by scanning new Wifi networks 

The first approach in the development of the application was to collect data to train the machine learning model. The wifi networks of different classrooms were collected
using the mobile application below:

https://github.com/Othboury/m1-opa-Wifi-Scan-Data

And sent via REST API into a Python Server containing the learning model, which uses random forest, to be able to predict the correct classroom afterward when the user tries and locate themselves:

https://github.com/papemor94/m1-opa-projet-flask-reseaudeneurone-projet-2021

The data was then put into the training model to achieve higher scores when predicting the position of the user. On the other hand, user management was made possible via a Java server allowing the crud functionalities on users and eventually admins. The Java server uses Hibernate to create and update a MySQL database. The communication with the server was via REST API.

https://github.com/Othboury/m1-opa-projet-java-android-backend-rest/tree/develop-pape1

The Java server allowed to implement the login functionality on the android application using the Basic Authentication method and associating to each user a unique Token when 
authenticated, but on the admin side, it allowed the creation of a web application using Angular that plays the role of a dashboard allowing the management of users.

https://github.com/papemor94/m1-opa-projet-frontend

<h3>Security:</h3>

On the security side, the exchange of data and the communication between the different actors of the application are secured using SSL certificates. Users' confidential pieces of information is hashed before being saved in the database.


<h3>App's Demo:</h3>

- The first step is to define the IP address of the server as well as the port on which the various REST API calls will be made to perform different tasks and exploit the functionalities of the application.

<img width="176" alt="step 1" src="https://user-images.githubusercontent.com/16863893/157408162-c28e04cb-c3fc-4ecc-ad99-6e99bad5227c.PNG">                                                    <img width="176" alt="step 2" src="https://user-images.githubusercontent.com/16863893/157408214-d25870fc-ce36-4270-b8d3-ff3893b86b08.PNG">

- It is mandatory to authenticate in order to use the functionalities of the application. The user must fill in his login and password and click on the connection button, this will launch a REST API call to carry out the verification at the database level and display either an error message if the coordinates are incorrect or the app home page.

<img width="194" alt="Login 1" src="https://user-images.githubusercontent.com/16863893/157408424-fecbf8d6-3a12-4fb9-ae3e-63de9df55be1.PNG">

Error handling:   

<img width="201" alt="loginErr" src="https://user-images.githubusercontent.com/16863893/157408501-08770308-9ba4-4095-a93a-a8ff5759d6de.PNG">

- After authentication, the user will be redirected to the home page containing two buttons, the first allowing geolocation, and the second allowing the addition of new wifi networks.

<img width="220" alt="Step 5" src="https://user-images.githubusercontent.com/16863893/157408651-ad6e91c2-11c1-40d8-8f1a-abf9211dd983.PNG">

- The add networks feature allows you to feed the database of wifi networks in order to improve the skills of the prediction model and to add new rooms. The user must define the room number, the server IP address and the port in order to be able to launch a scan of Wifi networks and save them in the database.

<img width="191" alt="addWifi" src="https://user-images.githubusercontent.com/16863893/157408758-e8bd9358-e77c-44eb-90d2-7fe208153ada.PNG">                                                  <img width="209" alt="addWifi2" src="https://user-images.githubusercontent.com/16863893/157408795-f06a6dea-b208-47f9-b804-3aaf3ae0f43f.PNG">

- The main functionality of the application is to geolocate the user within the building by indicating the room in which he is positioned. This prediction is made by clicking on the “Locate” button which allows redirection to the predictions page displaying the scores and room prediction percentages.

<img width="137" alt="prediction1" src="https://user-images.githubusercontent.com/16863893/157408941-9582e3b5-9de0-4cdb-9966-f9f185666081.PNG">                                              <img width="136" alt="prediction2" src="https://user-images.githubusercontent.com/16863893/157408978-8f0dbc11-10c4-49df-ae00-ca3d5427e8ef.PNG">









