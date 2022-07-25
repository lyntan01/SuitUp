# SuitUp

With a focus on customer experience and customisations, SuitUp offers a wide range of choices when it comes to customers’ preferences. Be it a business event, a wedding event, or for general purposes, SuitUp is able to cater to all needs through its diverse selection of fabrics, jacket styles, pocket styles, and pants cutting.

With the lack of options to order suits online from local stores and preview them before ordering, SuitUp aims to be a revolutionary game-changer by offering this ability while customers select their various customizations step by step till checkout.

[Project Showcase](https://uvents.nus.edu.sg/event/20th-steps/module/IS3106/project/16)

[Project Demo - Detailed walkthrough](https://www.youtube.com/watch?v=a7TURk83MFM)

## Description

An enterprise system that specialises in management and sales of customisable suits (Customer e-commerce & Admin management).

## Tech Stack

The system comprises of 2 frontend platforms and 1 common backend:

### Frontend

SuitUp Customer System: Web application developed using Angular and PrimeNG. [[Link]](https://github.com/lyntan01/SuitUpCustomerSystem)

SuitUp Management System: Web application developed using JavaServer Faces and PrimeFaces. Reports are generated with JasperReport. 
[[Link]](https://github.com/lyntan01/SuitUp/tree/main/SuitUp-war)

### Backend

SuitUp Backend: Common backend developed using Java EE 8, JPQL & MySQL. [[Link]](https://github.com/lyntan01/SuitUp/tree/main/SuitUp-ejb)

The common backend has a component-based architecture and a service-oriented architecture, making it scalable and maintaiable while allowing front end platforms to connect easily. Data storage utilises JPQL that is used in conjuction with MySQL for object relational mapping to store and read data from the database. RESTful Web Services are implemented to allow the angular app to call the backend. 

 ## Screenshots
 
 <img src="https://i.imgur.com/CBp9qzn.jpeg" width="800" height="400" alt="customer default page system"/>
 
 <img src="https://i.imgur.com/3BNFUvW.png" width="800" height="400" alt="customer login page"/>
  
 <img src="https://i.imgur.com/uSHokXX.png" width="800" height="450" alt="admin page"/>
  
 <img src="https://i.imgur.com/lwDlMaW.png" width="250" height="400" alt="admin page"/>
  
  

