# SolPresControl
## _The java purist enterprise application_

Ever found your self having to keep track of multiple accountants presentations? 
It's a tedious task I know, but by using SolPresControl you would only need to login, create the call in for the documents, and wait for your registered accountants to turn them in and mark them as such. No hassle in between, the app keeps track of all the necessary roles and their corresponding permitions:

- The administrators
- The general supervisors
- The supervisors of every accountant
- The accountants

## Features

### Administrators 💻
- Manage every single detail of any call in, presentation or user

### General Supervisors 👩‍💼
- Schedule new documents call ins
- Assign supervisors to every accountant

### Supervisors 👮
- Supervise your assigned municipalities and accountants
- Keep track of the documents that they have turned in

### Accountant 💼
- Create presentations to open call ins
- Keep track of all the documents that you need to turn in
- Turn in additional documents if needed

### Data in the cloud
- When this application was initially used and developed all data was stored and recovered from Google Cloud Platform, but that doesn't mean that you can't use other DBMS, you can use any MySQL database without changing the code. And even if you want to use a noSQL one like MongoDB, you can! The only thing that you need to do (Given that you already have the database running somewhere) is add the implementation of the necessary repositories and that's it! Nothing else in the code needs to be changed, the magic of inversion of control am I right?

![exactly](https://media0.giphy.com/media/3oz8xDo1q81w2VZG5W/giphy.gif?cid=790b7611f4327b28aefee0dd587b67a72548cf523011e071&rid=giphy.gif&ct=g)

## Requirements 

* Maven 3.8.4
* JDK 17
* A database server with the schema that's contained in the resources folder __(I used mySQL)__

![screen?](https://media.giphy.com/media/0iK2Cen5MEI79qK9ii/giphy.gif)

***Note:*** Code is spanish based [Code comments and variable names]
