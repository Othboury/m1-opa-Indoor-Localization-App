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

And sent via REST API into a Python Server containing the learning model, whic usereRandom forest, to be able to predict the correct classroom afterward when the user tries and locate themselves:

https://github.com/papemor94/m1-opa-projet-flask-reseaudeneurone-projet-2021

The data was then put into the training model to achieve higher scores when predicting the position of the user. On the other hand, user management was made possible via a Java server allowing the crud functionalities on users and eventually admins. The Java server uses Hibernate to create and update a MySQL database. The communication with the server was via REST API.

https://github.com/Othboury/m1-opa-projet-java-android-backend-rest/tree/develop-pape1

The Java server allowed to implement the login functionality on the android application using the Basic Authentication method and associating to each user a unique Token when 
authenticated, but on the admin side, it allowed the creation of a web application using Angular that plays the role of a dashboard allowing the management of users.

https://github.com/papemor94/m1-opa-projet-frontend

<h3>Security:</h3>

On the security side, the exchange of data and the communication between the different actors of the application are secured using SSL certificates. Users' confidential pieces of information is hashed before being saved in the database.
