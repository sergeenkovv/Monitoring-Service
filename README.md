# Monitoring-Service

### Application designed to transmit readings of heating, hot and cold water meters.

## Content

- [Peculiarity](#peculiarity)
- [Tech stack](#tech-stack)
- [Functionality](#functionality)
- [Startup instructions](#startup-instructions)
- [Contact me](#contact-me)

## Peculiarity

I decided to show in practice my understanding of the internal
structure of the application and principles of operation of the Spring Framework.

So I decided to write a console application that incorporates dependency injection principles similar to Spring and has
a minimum of dependencies.

+ The ApplicationContext class simulates the operation of an IoC Container.
+ the ApplicationRunner class emulates API creation service (for example Postman)
+ DAO classes emulate working with a database. Since the application does not have a data store, information is stored
  in
  collections
+ Controller classes handle requests from the ApplicationRunner class
  Therefore the application has the minimum possible number of dependencies.

## Tech stack

+ Java 17
+ ~~Spring Boot 3.2.4~~ 😁
+ ~~Spring Data Jpa~~ 😁
+ ~~Spring Web~~ 😁
+ Lombok
+ Log4j2
+ JUnit 5
+ AssertJ
+ Mockito

## Functionality

- Player registration
- Player authorization
- Getting the current meter readings of the player
- Sending meter readings
- Getting meter readings for a specific month
- Getting the history of sending meter readings

## Startup instructions

+ Run the ```Main``` class to start the application.

## Contact me

+ Email: [itproger181920@gmail.com](https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&to=itproger181920@gmail.com) 📬
+ Telegram: [@itproger181920](https://t.me/itproger181920) ✈️
+ LinkedIn: [Ivan Sergeenkov](https://www.linkedin.com/in/ivan-sergeenkov-553419294?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app) 🌊