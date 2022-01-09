# AllegroSpringTech2021 - Task 3
Project for third stage in Allegro Spring Tech e-Xperience 2021

## How to run application:
Project created on Java 11 and Maven 3.8.3.

In the project directory type:
>mvn clean install && mvn spring-boot:run

## How to use application:
You have two endpoints available:

1. To get all repositories (name and stars):

    `http://localhost:8080/github/repositories/{username}`

   Curl example:

    `curl http://localhost:8080/github/repositories/wojciechsova`


2. To get sum of stars in repositories:
   
    `http://localhost:8080/github/stars/{username}`
   
    Curl example: 
   
    `curl http://localhost:8080/github/stars/wojciechsova`


3. To get most popular languages used in user repositories:

   `http://localhost:8080/github/languages/{username}`

   Curl example:

   `curl http://localhost:8080/github/languages/wojciechsova`

## Limits
Github API have limits for sending requests per hour. 

Current limits can be found at: https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting

## Future of project:
- Add proper exception handlers
- Use multi-threading to send request to get used languages in repositories
