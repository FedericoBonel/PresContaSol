# SolPresControl
## _The java purist enterprise application_

Ever found yourself having to keep track of multiple accounting presentations? 
It's a tedious task, but by using SolPresControl you would only need to login, create a call in for the documents you require, and wait for your registered accountants to turn them in. With no hassle in between, the app keeps track of all the necessary roles and their corresponding permissions:

- Administrators
- General Supervisors
- The supervisors of every accountant
- The accountants

## Features

### Administrators 💻
- Manage every single detail of any call-in, presentation, or user

### General Supervisors 👩‍💼
- Schedule new document call-ins
- Assign supervisors to every accountant

### Supervisors 👮
- Supervise your assigned municipalities and accountants
- Keep track of the documents that they have turned in

### Accountants 💼
- Create presentations to open call-ins
- Keep track of all the documents that you need to turn in
- Turn in additional documents if needed

### Data in the cloud
- When this application was initially used and developed, all data was stored and recovered from Google Cloud Platform. That doesn't mean that you can't use other DBMS, you can use any MySQL database without changing the code. And even if you want to use an noSQL one like MongoDB, you can! The only thing you need to do (Given that you already have the database running somewhere) is implement the necessary repositories, and that's it! Nothing else in the code needs to be changed, the magic of interfaces, am I right?

![exactly](https://media0.giphy.com/media/3oz8xDo1q81w2VZG5W/giphy.gif?cid=790b7611f4327b28aefee0dd587b67a72548cf523011e071&rid=giphy.gif&ct=g)

## Requirements 

* Maven 3.8.4
* JDK 17
* A database server with the schema that's contained in the resources folder __(I used a mySQL one, if you decide to change that, be sure to check if the dialect is the same)__

## Take a look at it

![screen?](https://media.giphy.com/media/0iK2Cen5MEI79qK9ii/giphy.gif)

***Note:*** Code is spanish based [Code comments and variable names]
