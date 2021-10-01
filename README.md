# CrowdfundingPlatform
Crowdfunding platform that allows users to launch projects and purchase related products.

## Description
This a microservice-based web project developed in Java and deployed on AWS EC2, you can reach it at http://18.221.173.223/.


## Dependencies
 * Spring Boot 2.1.6
 * Spring Cloud 
 * Spring Session
 * MySQL
 * Redis
 * Mybatis

For more detail, please refer to pom.xml in modules.

## Third-party Services
 * AWS SNS
 * AWS S3
 * AWS EC2
 * Paypal API

## Usage
 * Enter 18.221.173.223
 * Click "login" on the top-right to login. This will take you to the member-center page.
    Due to the reason that my AWS SNS service is still limited in sandbox, the registration may not work.
 * You can launch a project by click "My crowdfunding" in member-center and then launch a new project by clicking "start my crowdfunding"
    Once the new project is created, it will then be shown on the home page.
 * You can check the projects listed in the home page and click "support" to continue.
    On the paypal page, you can use 
	sb-xzci67322366@personal.example.com 
	'sEe0BZ&
    to pay for the cost. This account is in sandbox so you don't have to worry its security.

## Author
Andong Zhang - andong.zhang@mail.concordia.ca
