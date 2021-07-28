# Stock App

Stock app is a simple Spring + React Application for storing Companies , Stock Exchanges and Sectors and then analyze their Stock rates. This **stockexchangebackend** repository is the backend Spring Boot part of the application. For the **React Front End** part , check [this](https://github.com/Dibyanshu-gtm/clientapp). In this repository, we use Java Spring Boot along with Spring Security JWT Security Token for Authorization. This app is also deployed in Heroku server 
* Backend: https://stockexchangebackend.herokuapp.com
* Frontend: https://stockexchangefrontend.herokuapp.com

Screenshots of applications are added in Working and Postman folders respectiviely

## Technology Stack Used

* Java: Version 8
* Spring : 2.5.2
* Maven
* Database:  In memory ( H2 database )
* Database persistence: Hibernate JPA
* Spring Boot Security
* JWT Token Authorization

## How to deploy locally

Being a spring application it is comparatively easy to deploy . I have listed the steps that needs to be done in order for you to run the back end smoothly.
* Download or Clone the code files from the repository
* Install all the pom Dependencies in your code editor ( You can use mvn commands)
* Configure H2 Database according to your local settings
* Check that in UserController you have changed the email to your preferred email
* Run the application and check for any other errors

## How to deploy on Heroku

* Register on Heroku and Create a free account
* Select New -> Create new App
* Give your application a new name and Link your GitHub repository 
* Deploy the application 
