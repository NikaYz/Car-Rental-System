

//STEP 1. Import required packages

import java.sql.*;
import java.util.Scanner;

public class JdbcDemo {

   // Set JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost:3306/vroomrentals?useSSL=false";

   // Database credentials for SQL
   static final String USER = "root";// add your user
   static final String PASSWORD = "********";// add password

   public static void main(String[] args) {
      Connection conn = null;
      Statement stmt = null;

      String createUsers = "Create table users ( user_id int primary key,username varchar(255) unique,password varchar(255) ,role varchar(30));";
      String createCustomer = "create table customers (customer_id int primary key, first_name varchar(30) not null, last_name varchar(30), email varchar(50) not null, phoneno bigint not null unique );";
      String createDriver = " CREATE TABLE drivers (     driver_id INT,     first_name VARCHAR(30) NOT\n" + //
                  "NULL,     last_name VARCHAR(30),     phone_no BIGINT UNIQUE,     car_type VARCHAR(30) NOT NULL,     car_model VARCHAR(30) not null,     car_no INT NOT NULL, working_in_company BOOLEAN default true" + //
                  " PRIMARY KEY (driver_id, car_no) );";
      String createCarInfo = "create table carInfo ( id int unique, type varchar(10) primary key , seater smallint not null, description text , rent_per_day int not null ); ";
      String createRentalInfo = " create table rentalinfo(customer_id int, driver_id int, car_type varchar(10),rental_start date not null, rental_end date not null, total_amount int, primary key (customer_id,driver_id,rental_start) );";
      // STEP 2. Connecting to the Database
      try {
         // STEP 2a: Register JDBC driver
         Class.forName(JDBC_DRIVER);

         // STEP 2b: Open a connection
         System.out.println("Connecting to database...");
         conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
         // STEP 2c: Execute a query
         System.out.println("Creating statement...");
         stmt = conn.createStatement();

         // INSERT, UPDATE, DELETE
         // stmt.executeUpdate(createCustomer);
         // stmt.executeUpdate(createDriver);
         // stmt.executeUpdate(createRentalInfo);
         // stmt.executeUpdate(createCarInfo);

         Scanner sc = new Scanner(System.in);
         
         System.out.print("Please enter your username: ");
         String username = sc.next();
         System.out.print("Please enter password: ");
         String password = sc.next();
         String role = "";
         String rolequery = "SELECT role as ro FROM users where (username = '" + username + "' and password = '" + password+ "');";
         ResultSet r = stmt.executeQuery(rolequery);
         while(r.next()){
               role = r.getString("ro");
         }
         r.close();
         if(role.equals("")){
            System.out.println("You are not registered in database ask the admin to add you");
            return ;
         }
         else{
            System.out.println("You are succesfully login as  " + role);
         }
         while(true){
            System.out.println("\n");
            System.out.println("--------------------------------------------------------------");
            System.out.println("1) Add a new customer");
            System.out.println("2) Add a new driver");
            System.out.println("3) Show car menu table");
            System.out.println("4) Book a car");
            System.out.println("5) Details of a trip");
            System.out.println("6) No of cars rented during a month or yearly");
            System.out.println("7) Total sales during a month or yearly");
            System.out.println("8) Update customer details");
            System.out.println("9) Update driver details");
            System.out.println("10) Delete a driver details");
            System.out.println("11) Add a new car model");
            System.out.println("12) Get driver tours or amount in ascending order");
            System.out.println("13) Get amount or tours by car type group");
            System.out.println("14) Show all the customers");
            System.out.println("15) Show all the drivers");
            System.out.println("16) Show all the rentalInfo");
            System.out.println("17) Enter new employee");
            System.out.println("18) Delete an employee");
            System.out.println("19) Exit from the application");
            System.out.println("Enter your choice: ");
            int choice = sc.nextInt();
            System.out.println("--------------------------------------------------------------");

            if(choice == 1){
               try{
               conn.setAutoCommit(false);
               System.out.print("Please enter new customer details: \n");
               System.out.print("First name: ");
               String fname= sc.next();
               System.out.print("Last name: ");
               String lname= sc.next();
               System.out.print("Email Id: ");
               String email= sc.next();
               System.out.print("Phone Number: ");
               long phone_no= sc.nextLong();
               String record_count = "SELECT COUNT(*) AS record_count FROM customers;";
               ResultSet rs = stmt.executeQuery(record_count);
               int cust_id = 0;
               while(rs.next()){
                  cust_id = rs.getInt("record_count") + 1;
               }
               rs.close();
               //System.out.println("cust_no: " + cust_id);
               String insertCustomer = "INSERT INTO customers values ( " +
                                       cust_id + " , '" + fname +  "' , '" + lname + "' , '"+ email + "' , " + phone_no + ");";
               stmt.executeUpdate(insertCustomer);
               conn.commit();
               System.out.println("New customer successfully inserted");
            }
            catch (SQLException se) {
               // Rollback the transaction in case of exception
               System.out.println("Rolling back the transaction");
               if (conn != null) {
                   try {
                       conn.rollback();
                   } catch (SQLException se2) {
                       se2.printStackTrace();
                   }
               }
               se.printStackTrace();
            }
            }
            else if(choice == 2){
               try{
               conn.setAutoCommit(false);
               System.out.print("Please enter new driver details: \n");
               System.out.print("First name: ");
               String fname= sc.next();
               System.out.print("Last name: ");
               String lname= sc.next();
               System.out.print("Phone Number: ");
               long phone_no= sc.nextLong();
               System.out.print("Car Type: ");
               String record_type = "SELECT type  FROM carinfo;";
               ResultSet rs = stmt.executeQuery(record_type);
               
               while(rs.next()){
                  String type = rs.getString("type");
                  System.out.print(type + " , ");
               }
               System.out.print("\nSelect from these: ");
               String type = sc.next();
               System.out.print("Car model: ");
               String carmodel= sc.next();
               System.out.print("Car number: ");
               String carnumber= sc.next();
               String record_count = "SELECT count(*) AS record_count FROM drivers;";
               rs = stmt.executeQuery(record_count);
               long driv_id = 0;
               while(rs.next()){
                  driv_id = rs.getLong("record_count") + 1;
               }
               //System.out.println("here");
               String checkAlreadyExist =" SELECT COUNT(*) AS num_rows FROM drivers WHERE (phone_no = " + phone_no + " OR car_no = '"+ carnumber +"');";
               
              // System.out.println("here");
               rs = stmt.executeQuery(checkAlreadyExist);
               int c = 0;
               while(rs.next()){
                  c = rs.getInt("num_rows");
               }
               if(c == 0){
               System.out.println("cust_no: " + driv_id);
                  String insertDriver = "INSERT INTO drivers values ( " +
                                          driv_id + " , '" + fname +  "' , '" + lname + "' , "  + phone_no + ", '" +type +"' , '"+ carmodel +"','" + carnumber +  "',1);";
                  stmt.executeUpdate(insertDriver);
                  System.out.println("New driver successfully inserted");
               }
               else{
                  
                  String getupdate =" SELECT driver_id FROM drivers WHERE (phone_no = " + phone_no + " OR car_no = '"+ carnumber +"');";
                  rs = stmt.executeQuery(getupdate);
                  long tempid = 0;
                  while(rs.next()){
                     tempid = rs.getInt("driver_id");
                  }
                  rs.close();
                  String updateDriver = "UPDATE drivers "+
                                         "SET working_in_company = TRUE where driver_id = " + tempid  + ";" ;
                  stmt.executeUpdate(updateDriver);
                  System.out.println("Driver previously was working  with company so go to update and change the required things.");
               }
               System.out.print("Please provide your phone no which you entered lately: ");
                  phone_no = sc.nextLong();
                  String query = "SELECT * from drivers where " + phone_no + "  = drivers.phone_no;";
                  rs = stmt.executeQuery(query);
                  driv_id = 0;
                  fname = "";
                  lname = "";
                  String cartype = "";
                  carmodel = "";
                  carnumber = ""; 
                  while (rs.next()) {
                     // Retrieve by column name
                     driv_id = rs.getLong("driver_id");
                     fname = rs.getString("first_name");
                     lname = rs.getString("last_name");
                     cartype = rs.getString("car_type");
                     carmodel = rs.getString("car_model");
                     carnumber = rs.getString("car_no");
                     System.out.println("Name: " + fname + " " + lname +  ",  phone number: " + phone_no + ",  car type: "+ cartype + ",  car model: " + carmodel + ", car number: " + carnumber);
                  
                  }
                  if(driv_id == 0){
                     System.out.println("Please enter a correct phone no, please try again");
                     continue;
                  }
                  rs.close();
                  while(true){
                     System.out.println("Please select number ,what you wish to update: ");
                     System.out.println("1: First Name");
                     System.out.println("2: Last Name");
                     System.out.println("3: Phone no");
                     System.out.println("4: Car type");
                     System.out.println("5: Car model");
                     System.out.println("6: Car number");
                     System.out.println("7: To exit");
                     c = sc.nextInt();
                     if(c == 1){
                        System.out.println("Please enter new first name");
                        fname = sc.next();
                     }
                     else if(c == 2){
                        System.out.print("Please enter new last name: ");
                        lname = sc.next();
                     }
                     else if(c == 3){
                        System.out.print("Please enter new phone number:");
                        phone_no = sc.nextLong();
                     }
                     else if(c == 4){
                       record_type = "SELECT type  FROM carinfo;";
                        ResultSet rq = stmt.executeQuery(record_type);
                  
                        while(rq.next()){
                           type = rq.getString("type");
                           System.out.print(type + " , ");
                        }
                        rq.close();
                        System.out.print("\nSelect new type from these: ");
                        cartype = sc.next();
                        
                     }
                     else if(c == 5){
                        System.out.print("Please enter new car model: ");
                        carmodel = sc.next();
                     }
                     else if(c == 6){
                        System.out.print("Please enter new car number: ");
                        carnumber = sc.next();
                     }
                     
                     else if(c == 7){
                        break;
                     }
                     else{
                        System.out.println("Wrong option selected!");
                     }
                  }

                  String updateCustomer = "UPDATE drivers " + 
                                       "SET phone_no = " + phone_no + 
                                       " ,   first_name = '"+ fname + 
                                       "'  ,  last_name = '" + lname +
                                       "'  ,  car_model = '" + carmodel +
                                       "'  ,  car_no = '" + carnumber +
                                       "'  ,  car_type = '" + cartype +
                                       "' WHERE driver_id = " +driv_id +";";
                  stmt.executeUpdate(updateCustomer);
                  System.out.println("Successfully updated driver");
               conn.commit();
            }
            catch (SQLException se) {
               // Rollback the transaction in case of exception
               System.out.println("Rolling back the transaction");
               if (conn != null) {
                   try {
                       conn.rollback();
                   } catch (SQLException se2) {
                       se2.printStackTrace();
                   }
               }
               se.printStackTrace();
            }
            }
            else if(choice == 3){
               System.out.println("Car information chart: ");
               String query = "SELECT type, seater, description, rent_per_day from carInfo";
               ResultSet rs = stmt.executeQuery(query);
               while (rs.next()) {
                  
                  // Retrieve by column name
                  String type = rs.getString("type");
                  Integer seater = rs.getInt("seater");
                  String description = rs.getString("description");
                  Integer rent_per_day = rs.getInt("rent_per_Day");
                  System.out.print("Type: " + type);
                  System.out.print("\nSeater: " + seater);
                  System.out.println("\nDescription: " + description);
                  System.out.println("Rent per day: Rs." + rent_per_day+" \n");
               }  
               rs.close();
            }

            else if(choice == 4){
               try{
                  conn.setAutoCommit(false);
               System.out.print("Please provide customer's phone no: ");
               long phone_no = sc.nextLong();
               String query = "SELECT customer_id from customers where phoneno =" + phone_no + ";";
               ResultSet rs = stmt.executeQuery(query);
               long cust_id = 0;
               while (rs.next()) {
                  // Retrieve by column name
                  cust_id = rs.getLong("customer_id");
               }
               if(cust_id == 0){
                  System.out.println("Please enter a correct phone no, please try again");
                  continue;
               }
               System.out.print("Please provide start date(in format YYYY-MM-DD): ");
               String start_date = sc.next();
               System.out.print("Please provide end date(in format YYYY-MM-DD): ");
               String end_date = sc.next();
               String qu = "SELECT d.driver_id, d.car_model, d.car_type, cp.rent_per_day FROM drivers d " +
                           "JOIN carinfo cp ON d.car_type = cp.type " + 
                           "LEFT JOIN rentalinfo r ON d.driver_id = r.driver_id " + 
                           " where  (r.driver_id IS NULL " + 
                           " OR NOT (     r.rental_end >= '" + start_date + "' AND r.rental_start <= '" + end_date + "' )) AND d.working_in_company = TRUE ;";
               rs = stmt.executeQuery(qu);
               while (rs.next()) {
                              
                  // Retrieve by column name
                  String driver_id = rs.getString("driver_id");
                  String car_type = rs.getString("car_type");
                  String car_model = rs.getString("car_model");
                  Integer rent_per_day = rs.getInt("rent_per_Day");
                  System.out.println("Driver_id: " + driver_id + " | Car_type: " + car_type + " | Car_model: " + car_model + " | Rent_per_day: Rs." + rent_per_day);
               }  
               System.out.print("\nPlease select the driver_id which you want from above:");
               int driver_id = sc.nextInt();
               query = "SELECT car_type from drivers where " + driver_id + "  = drivers.driver_id;";
               rs = stmt.executeQuery(query);
               String cartype = "";
               while (rs.next()) {
                  // Retrieve by column name
                  cartype = rs.getString("car_type");
               }
               query = "SELECT rent_per_day from carinfo where '" + cartype + "'  = carinfo.type;";
               rs = stmt.executeQuery(query);
               int price = 0;
               while (rs.next()) {
                  // Retrieve by column name
                  price = rs.getInt("rent_per_day");
               }

               System.out.print("Are you sure you want to continue(y/n): ");
               String c = sc.next();
               if( c.equals("y")){
               String insertRentalInfo = "INSERT INTO rentalinfo values ( " +
                                       cust_id + " , " + driver_id +  " , '" + cartype + "' , '"  + start_date + "', '" +end_date +"' , (DATEDIFF('" + end_date + "','" + start_date + "')+1)*" +price +"  );";
               stmt.executeUpdate(insertRentalInfo);
               System.out.println("Booking confirmed !");
               System.out.println("Thanks for booking a car :) ");
               query = "SELECT * from drivers where " + driver_id + "  = drivers.driver_id;";
               rs = stmt.executeQuery(query);
               while (rs.next()) {
                  // Retrieve by column name
                  String fname = rs.getString("first_name");
                  String lname = rs.getString("last_name");
                  long ph_no = rs.getLong("phone_no");
                  String cmodel = rs.getString("car_model");
                  String cno = rs.getString("car_no");
                  System.out.println("Driver Details: ");
                  System.out.println("Name: "+fname + " " + lname);
                  System.out.println("Phone number: " + ph_no);
                  System.out.println("Car Type: " + cartype);
                  System.out.println("Car Model: " + cmodel);
                  System.out.println("Car Number: " + cno);
                  }
                  rs.close();
               }
               else{
                  System.out.println("Booking unconfirmed.");
               }
               conn.commit();
            }
            catch (SQLException se) {
               // Rollback the transaction in case of exception
               System.out.println("Rolling back the transaction booking unconfirmed");
               if (conn != null) {
                   try {
                       conn.rollback();
                   } catch (SQLException se2) {
                       se2.printStackTrace();
                   }
               }
               se.printStackTrace();
            }
            }
            else if(choice == 5){
               
               System.out.print("Please provide customers phone no: ");
               long phone_no = sc.nextLong();
               String query = "SELECT customer_id from customers where " + phone_no + "  = customers.phoneno;";
               ResultSet rs = stmt.executeQuery(query);
               long cust_id = 0;
               while (rs.next()) {
                  // Retrieve by column name
                  cust_id = rs.getLong("customer_id");
               }
               if(cust_id == 0){
                  System.out.println("Please enter a correct phone no, please try again");
                  continue;
               }
               System.out.print("Please provide start date(in format YYYY-MM-DD): ");
               String start_date = sc.next();
               String getdriverInfo = "SELECT driver_id from rentalinfo where customer_id = " + cust_id + " AND rental_start = '" + start_date + "';";
               rs = stmt.executeQuery(getdriverInfo);
               long driv_id = 0;
               while (rs.next()) {
                  // Retrieve by column name
                  driv_id = rs.getLong("driver_id");
               }
               String getRentalInfo = "SELECT c.first_name as customer_f, c.last_name as customer_l, c.phoneno as customer_p, d.first_name, d.last_name,d.phone_no,d.car_type,d.car_model,d.car_no,r.rental_start,r.rental_end, r.total_amount from rentalinfo r join customers c on r.customer_id = " + cust_id + " join drivers d on r.driver_id = "+ driv_id +" where (r.customer_id = " + cust_id + " and r.driver_id = "+ driv_id +" ) limit 1;";
               rs = stmt.executeQuery(getRentalInfo);
               while (rs.next()) {
                  // Retrieve by column name
                 String customerfname = rs.getString("customer_f");
                 String customerlname = rs.getString("customer_l");
                 Long customerphoneno = rs.getLong("customer_p");
                 String driverfname = rs.getString("first_name");
                 String dricerlname = rs.getString("last_name");
                 Long driverphoneno = rs.getLong("phone_no");
                 String cartype = rs.getString("car_type");
                 String carmodel = rs.getString("car_model");
                 String carno = rs.getString("car_no");
                 String rstart = rs.getString("rental_start");
                 String rend = rs.getString("rental_end");
                 int total = rs.getInt("total_amount");
                  System.out.println("Customer Details: ");
                  System.out.println("Name: "+ customerfname + " " + customerlname);
                  System.out.println("Phone no: "+ customerphoneno);
                  System.out.println("\nDriver Details: ");
                  System.out.println("Name: "+ driverfname + " " + dricerlname);
                  System.out.println("Phone no: "+ driverphoneno);
                  System.out.println("Car number: " + carno + " , Car model: " + carmodel + ", Car type: "+ cartype);
                  System.out.println("Rental start: "+ rstart + ", Rental end: " + rend);
                  System.out.println("Total amount: "+ total + "\n\n");               
               }
               rs.close();
            }
            else if(choice == 6){
               if(role.equals("admin")){
                  System.out.print("Want to calculate 1)yearly or 2)monthly . Select 1 or 2: ");
                  int c = sc.nextInt();
                  if(c == 1){
                     System.out.print("Enter year in (YYYY): ");
                     String yearly = sc.next();
                     String getnoofcars = "SELECT COUNT(*) AS record_count " + //
                                          " FROM rentalinfo " + 
                                          "WHERE YEAR(rental_start) = '" + yearly + "';";
                     ResultSet rs = stmt.executeQuery(getnoofcars);
                     while(rs.next()){
                        int no_of_cars = rs.getInt("record_count");
                        System.out.println("No of car rented: " + no_of_cars + ", in year: "+ yearly );
                     }
                     rs.close();
                  }
                  else if(c == 2){
                     System.out.print("Enter year in (YYYY): ");
                     String yearly = sc.next();
                     System.out.print("Enter month in (MM): ");
                     String monthly = sc.next();
                     String getnoofcars = "SELECT COUNT(*) AS record_count " + //
                                          " FROM rentalinfo " + 
                                          "WHERE YEAR(rental_start) = '" + yearly + "' AND MONTH(rental_start) = '" + monthly+ "';";
                     ResultSet rs = stmt.executeQuery(getnoofcars);
                     while(rs.next()){
                        int no_of_cars = rs.getInt("record_count");
                        System.out.println("No of car rented: " + no_of_cars + ", in year: "+ yearly );
                     }
                     rs.close();
                  }
                  else{
                     System.out.println("Please enter correct input again.");
                  }
               }
               else{
                  System.out.println("Only Admin can access this");
               }
            }
            else if(choice == 7){
               if(role.equals("admin")){
                  System.out.print("Want to calculate 1)yearly or 2)monthly . Select 1 or 2: ");
                  int c = sc.nextInt();
                  if(c == 1){
                     System.out.print("Enter year in (YYYY): ");
                     String yearly = sc.next();
                     String gettotal = "SELECT SUM(total_amount) AS record_count " + //
                                          " FROM rentalinfo " + 
                                          "WHERE YEAR(rental_start) = '" + yearly + "';";
                     ResultSet rs = stmt.executeQuery(gettotal);
                     while(rs.next()){
                        long total = rs.getLong("record_count");
                        System.out.println("Total sales: " + total + ", in year: "+ yearly );
                     }
                     rs.close();
                  }
                  else if(c == 2){
                     System.out.print("Enter year in (YYYY): ");
                     String yearly = sc.next();
                     System.out.print("Enter month in (MM): ");
                     String monthly = sc.next();
                     String gettotal = "SELECT SUM(total_amount) AS record_count " + //
                                          " FROM rentalinfo " + 
                                          "WHERE YEAR(rental_start) = '" + yearly + "' AND MONTH(rental_start) = '" + monthly+ "';";
                     ResultSet rs = stmt.executeQuery(gettotal);
                     while(rs.next()){
                        long total = rs.getLong("record_count");
                        System.out.println("Total sales: " + total + ", in year: "+ yearly + ", in month: " + monthly );
                     }
                     rs.close();
                  }
                  else{
                     System.out.println("Please enter correct input again.");
                  }
               }
               else{
                  System.out.println("Only Admin can access this");
               }
            }
            else if(choice == 8){
               try{
                  conn.setAutoCommit(false);
               System.out.print("Please provide customers phone no which you entered last: ");
               long phone_no = sc.nextLong();
               String query = "SELECT * from customers where " + phone_no + "  = customers.phoneno;";
               ResultSet rs = stmt.executeQuery(query);
               long cust_id = 0;
               String fname = "";
               String lname = "";
               String email = ""; 
               while (rs.next()) {
                  // Retrieve by column name
                  cust_id = rs.getLong("customer_id");
                  fname = rs.getString("first_name");
                  lname = rs.getString("last_name");
                  email = rs.getString("email");
                  System.out.println("Name: " + fname + " " + lname + ",  email: " + email + ",  phone number: " + phone_no); 
               }
               if(cust_id == 0){
                  System.out.println("Please enter a correct phone no, please try again");
                  continue;
               }
               rs.close();
               while(true){
                  System.out.println("Please select number ,what you wish to update: ");
                  System.out.println("1: First Name");
                  System.out.println("2: Last Name");
                  System.out.println("3: Email id");
                  System.out.println("4: Phone no");
                  System.out.println("5: To exit");
                  int c = sc.nextInt();
                  if(c == 1){
                     System.out.println("Please enter new first name");
                     fname = sc.next();
                  }
                  else if(c == 2){
                     System.out.print("Please enter new last name: ");
                     lname = sc.next();
                  }
                  else if(c == 3){
                     System.out.print("Please enter new email: ");
                     email = sc.next();
                  }
                  else if(c == 4){
                     System.out.print("Please enter new phone number:");
                     phone_no = sc.nextLong();
                  }
                  else if(c == 5){
                     break;
                  }
                  else{
                     System.out.println("Wrong option selected!");
                  }
               }

               String updateCustomer = "UPDATE customers " + 
                                    "SET phoneno = " + phone_no + 
                                    " ,   email = '" + email + 
                                    "' ,   first_name = '"+ fname + 
                                    "'  ,  last_name = '" + lname +
                                    "' WHERE customer_id = " +cust_id +";";
               stmt.executeUpdate(updateCustomer);
               System.out.println("Successfully updated customer");
               conn.commit();
            }
            catch (SQLException se) {
               // Rollback the transaction in case of exception
               System.out.println("Rolling back the transaction");
               if (conn != null) {
                   try {
                       conn.rollback();
                   } catch (SQLException se2) {
                       se2.printStackTrace();
                   }
               }
               se.printStackTrace();
            }
            }
            else if(choice == 9){
               try{
                  conn.setAutoCommit(false);
                  System.out.print("Please provide your phone no which you entered lately: ");
                  long phone_no = sc.nextLong();
                  String query = "SELECT * from drivers where " + phone_no + "  = drivers.phone_no;";
                  ResultSet rs = stmt.executeQuery(query);
                  long driv_id = 0;
                  String fname = "";
                  String lname = "";
                  String cartype = "";
                  String carmodel = "";
                  String carnumber = ""; 
                  while (rs.next()) {
                     // Retrieve by column name
                     driv_id = rs.getLong("driver_id");
                     fname = rs.getString("first_name");
                     lname = rs.getString("last_name");
                     cartype = rs.getString("car_type");
                     carmodel = rs.getString("car_model");
                     carnumber = rs.getString("car_no");
                     System.out.println("Name: " + fname + " " + lname +  ",  phone number: " + phone_no + ",  car type: "+ cartype + ",  car model: " + carmodel + ", car number: " + carnumber);
                  
                  }
                  if(driv_id == 0){
                     System.out.println("Please enter a correct phone no, please try again");
                     continue;
                  }
                  rs.close();
                  while(true){
                     System.out.println("Please select number ,what you wish to update: ");
                     System.out.println("1: First Name");
                     System.out.println("2: Last Name");
                     System.out.println("3: Phone no");
                     System.out.println("4: Car type");
                     System.out.println("5: Car model");
                     System.out.println("6: Car number");
                     System.out.println("7: To exit");
                     int c = sc.nextInt();
                     if(c == 1){
                        System.out.println("Please enter new first name");
                        fname = sc.next();
                     }
                     else if(c == 2){
                        System.out.print("Please enter new last name: ");
                        lname = sc.next();
                     }
                     else if(c == 3){
                        System.out.print("Please enter new phone number:");
                        phone_no = sc.nextLong();
                     }
                     else if(c == 4){
                        String record_type = "SELECT type  FROM carinfo;";
                        ResultSet rq = stmt.executeQuery(record_type);
                  
                        while(rq.next()){
                           String type = rq.getString("type");
                           System.out.print(type + " , ");
                        }
                        rq.close();
                        System.out.print("\nSelect new type from these: ");
                        cartype = sc.next();
                        
                     }
                     else if(c == 5){
                        System.out.print("Please enter new car model: ");
                        carmodel = sc.next();
                     }
                     else if(c == 6){
                        System.out.print("Please enter new car number: ");
                        carnumber = sc.next();
                     }
                     
                     else if(c == 7){
                        break;
                     }
                     else{
                        System.out.println("Wrong option selected!");
                     }
                  }

                  String updateCustomer = "UPDATE drivers " + 
                                       "SET phone_no = " + phone_no + 
                                       " ,   first_name = '"+ fname + 
                                       "'  ,  last_name = '" + lname +
                                       "'  ,  car_model = '" + carmodel +
                                       "'  ,  car_no = '" + carnumber +
                                       "'  ,  car_type = '" + cartype +
                                       "' WHERE driver_id = " +driv_id +";";
                  stmt.executeUpdate(updateCustomer);
                  System.out.println("Successfully updated driver");
                  conn.commit();
               }
               catch (SQLException se) {
                  // Rollback the transaction in case of exception
                  System.out.println("Rolling back the transaction");
                  if (conn != null) {
                     try {
                        conn.rollback();
                     } catch (SQLException se2) {
                        se2.printStackTrace();
                     }
                  }
                  se.printStackTrace();
               }
            }
            else if(choice == 10){
               try{
                  conn.setAutoCommit(false);
                  System.out.print("Please provide your phone no which you entered lately: ");
                  long phone_no = sc.nextLong();
                  String query = "SELECT * from drivers where " + phone_no + "  = drivers.phone_no;";
                  ResultSet rs = stmt.executeQuery(query);
                  long driv_id = 0;
                  String fname = "";
                  String lname = "";
                  String cartype = "";
                  String carmodel = "";
                  String carnumber = ""; 
                  while (rs.next()) {
                     // Retrieve by column name
                     driv_id = rs.getLong("driver_id");
                     fname = rs.getString("first_name");
                     lname = rs.getString("last_name");
                     cartype = rs.getString("car_type");
                     carmodel = rs.getString("car_model");
                     carnumber = rs.getString("car_no");
                     System.out.println("Name: " + fname + " " + lname +  ",  phone number: " + phone_no + ",  car type: "+ cartype + ",  car model: " + carmodel + ", car number: " + carnumber);
                  }
                  if(driv_id == 0){
                     System.out.println("Please enter a correct phone no, please try again");
                     continue;
                  }
                  rs.close();
                  System.out.println("Are you sure you want to delete this driver(y,n): ");
                  String c = sc.next();
                  if(c.equals("y")){
                     String deleteDriver = "UPDATE drivers " + 
                                    "SET working_in_company = false " +
                                    " WHERE driver_id = " +driv_id +";";
                     stmt.executeUpdate(deleteDriver);
                     System.out.println("Deleting a driver completed successfully.");
                  }
                  else{
                     System.out.println("Delete unsuccessful");
                  }
                  conn.commit();
               }
               catch (SQLException se) {
                  // Rollback the transaction in case of exception
                  System.out.println("Rolling back the transaction");
                  if (conn != null) {
                     try {
                        conn.rollback();
                     } catch (SQLException se2) {
                        se2.printStackTrace();
                     }
                  }
                  se.printStackTrace();
               }
            }
            else if(choice == 11){
               try{
                  conn.setAutoCommit(false);
                  if(role.equals("admin")){
                     System.out.print("Please enter new car model details: \n");
                     System.out.print("Type: ");
                     String type= sc.next();
                     System.out.print("Seater: ");
                     int seater= sc.nextInt();
                     System.out.print("Description(use end at last of string): ");
                     String description= "";
                     while (sc.hasNext()) {
                        String word = sc.next();
                        if(word.equals("end")){
                           break;
                        }
                        description = description + word + " " ;
                  }
            
                     System.out.print("Rent per day: ");
                     int rent_per_day= sc.nextInt();
                     String record_count = "SELECT COUNT(*) AS record_count FROM carinfo;";
                     ResultSet rs = stmt.executeQuery(record_count);
                     int id = 0;
                     while(rs.next()){
                        id = rs.getInt("record_count") + 1;
                     }
                     //System.out.println("cust_no: " + cust_id);
                     rs.close();
                     String insertCarInfo = "INSERT INTO carinfo values ( " +
                                             id + " , '" + type +  "' , " + seater + " , '"+ description + "' , " + rent_per_day + ");";
                     stmt.executeUpdate(insertCarInfo);
                     System.out.println("Successfully added new car model");
                  }
                  else{
                     System.out.println("Only Admin can access this");
                  }
                  conn.commit();
               }
               catch (SQLException se) {
                  // Rollback the transaction in case of exception
                  System.out.println("Rolling back the transaction");
                  if (conn != null) {
                     try {
                        conn.rollback();
                     } catch (SQLException se2) {
                        se2.printStackTrace();
                     }
                  }
                  se.printStackTrace();
               }
            }
            else if(choice == 12){
               if(role.equals("admin")){
                  System.out.println("You wish to see in terms of 1)tours or 2)amount or 3)both:");
                  System.out.println("Select 1 or 2 or 3:");
                  int c = sc.nextInt();
                  if(c == 1){
                     String getrecords = "SELECT r.driver_id, d.first_name, d.last_name, COUNT(*) AS tours " +
                                             "FROM rentalinfo r " + 
                                             "JOIN drivers d ON r.driver_id = d.driver_id " +
                                             "GROUP BY r.driver_id, d.first_name, d.last_name " +
                                          "ORDER BY TOURS;";
                     ResultSet rs = stmt.executeQuery(getrecords);
                     while(rs.next()){
                        long id = rs.getLong("driver_id");
                        String fname = rs.getString("first_name");
                        String lname = rs.getString("last_name");
                        if(lname == null){
                           lname = "";
                        }
                        int tours = rs.getInt("tours");
                        System.out.println("Id: "+ id + " | Name:" + fname + " " + lname +" | No. of tours: "+tours);
                        
                     }
                     rs.close();
                  }
                  else if(c == 2){
                     String getrecords = "SELECT r.driver_id, d.first_name, d.last_name, SUM(total_amount) AS amount " +
                                             "FROM rentalinfo r " + 
                                             "JOIN drivers d ON r.driver_id = d.driver_id " +
                                             "GROUP BY r.driver_id, d.first_name, d.last_name order by amount";
                     ResultSet rs = stmt.executeQuery(getrecords);
                     while(rs.next()){
                        long id = rs.getLong("driver_id");
                        String fname = rs.getString("first_name");
                        String lname = rs.getString("last_name");
                        if(lname == null){
                           lname = "";
                        }
                        long amount = rs.getLong("amount");
                        System.out.println("Id: "+ id + " | Name:" + fname + " " + lname +" | Amount: Rs"+ amount);
                        
                     }
                     rs.close();
                  }
                  else if(c == 3){
                     String getrecords = "SELECT r.driver_id, d.first_name, d.last_name, COUNT(*) AS tours, SUM(r.total_amount) AS amount " +
                                             "FROM rentalinfo r " + 
                                             "JOIN drivers d ON r.driver_id = d.driver_id " + 
                                             "GROUP BY r.driver_id, d.first_name, d.last_name " + 
                                             "ORDER BY amount ASC, tours ASC;";
                     ResultSet rs = stmt.executeQuery(getrecords);
                     while(rs.next()){
                        long id = rs.getLong("driver_id");
                        String fname = rs.getString("first_name");
                        String lname = rs.getString("last_name");
                        if(lname == null){
                        lname = "";
                        }
                        long amount = rs.getLong("amount");
                        int tours = rs.getInt("tours");
                        System.out.println("Id: "+ id + " | Name:" + fname + " " + lname +" | Amount: Rs"+ amount + " | Tours: " + tours);

                     }
                     rs.close();
                  }
                  else{
                     System.out.println("Please select a correct option.");
                  }
               }
               else{
                  System.out.println("Only Admin can access this");
               }

            }
            else if(choice == 13){
               if(role.equals("admin")){
                  System.out.println("You wish to see in terms of 1)amount or 2)tours or 3)both:");
                  System.out.println("Select 1 or 2 or 3:");
                  int c = sc.nextInt();
                  if(c == 1){
                     String getrecords = "SELECT car_type, SUM(total_amount) as amount from rentalinfo GROUP BY car_type ORDER BY amount;";
                     ResultSet rs = stmt.executeQuery(getrecords);
                     while(rs.next()){
                     
                        String ctype = rs.getString("car_type");
                        long amount = rs.getLong("amount");
                        System.out.println("Car type: "+ ctype + " | Amount: Rs" + amount );
                        
                     }
                     rs.close();
                  }
                  else if(c == 2){
                     String getrecords = " SELECT car_type, count(*) as tours from rentalinfo GROUP BY car_type ORDER BY tours;";
                     ResultSet rs = stmt.executeQuery(getrecords);
                     while(rs.next()){
                        
                        String ctype = rs.getString("car_type");
                        int tours = rs.getInt("tours");
                        System.out.println("Car type: "+ ctype + " | Tours:" + tours );
                        
                     }
                     rs.close();
                  }
                  else if(c == 3){
                     String getrecords = "SELECT car_type, SUM(total_amount) as amount, COUNT(*) as tours from rentalinfo GROUP BY car_type ORDER BY amount,tours;";
                     ResultSet rs = stmt.executeQuery(getrecords);
                     while(rs.next()){
                     
                        String ctype = rs.getString("car_type");
                        long amount = rs.getLong("amount");
                        int tours = rs.getInt("tours");
                        System.out.println("Car type: "+ ctype + " | Amount: Rs" + amount +  " | Tours: " + tours );
                        
                     }
                     rs.close();
                     
                  }
                  else{
                     System.out.println("Please select a correct option.");
                  }
               }
               else{
                  System.out.println("Only Admin can access this");
               }
            }
            else if(choice == 14){
               if(role.equals("admin")){
                  String record_count = "SELECT count(*) AS record_count FROM customers;";
                  ResultSet rs = stmt.executeQuery(record_count);
                  int total_customers = 0;
                  while(rs.next()){
                     total_customers = rs.getInt("record_count");
                  }
                  System.out.println("There are "+ total_customers +" customers ");
                  String getRentalInfo = "SELECT * from customers;";
                  rs = stmt.executeQuery(getRentalInfo);
                  while (rs.next()) {
                     // Retrieve by column name
                     Long id = rs.getLong("customer_id");
                     String fname = rs.getString("first_name");
                     String lname = rs.getString("last_name");
                     Long phoneno = rs.getLong("phoneno");
                     String email = rs.getString("email");
                     System.out.println("Id: " + id + " | Name: " + fname + " " + lname + " | Phone No: "+ phoneno + " | Email: "+email );
                  }
                  rs.close();
               }
               else{
                  System.out.println("Only Admin can access this");
               }
            }
            else if(choice == 15){
               if(role.equals("admin")){
                  String record_count = "SELECT count(*) AS record_count FROM drivers;";
                  ResultSet rs = stmt.executeQuery(record_count);
                  int total_drivers = 0;
                  while(rs.next()){
                     total_drivers = rs.getInt("record_count");
                  }
                  System.out.println("There are "+ total_drivers +" drivers ");
                  String getDriversInfo = "SELECT * from drivers;";
                  rs = stmt.executeQuery(getDriversInfo);
                  while (rs.next()) {
                     Long id = rs.getLong("driver_id");
                     String fname = rs.getString("first_name");
                     String lname = rs.getString("last_name");
                     if(lname == null){
                        lname = "";
                     }
                     Long phoneno = rs.getLong("phone_no");
                     String cartype = rs.getString("car_type");
                     String carmodel = rs.getString("car_model");
                     String carno = rs.getString("car_no");
                     Boolean available = rs.getBoolean("working_in_company");

                     System.out.println("Id: " + id + " | Name: " + fname + " " + lname + " | Phone No: "+ phoneno + " | CarModel: "+ carmodel + " | CarNo: "+ carno + " | Working: "+ available);
                  }
                  rs.close();
               }
               else{
                  System.out.println("Only Admin can access this");
               }
            }
            else if(choice == 16){
               if(role.equals("admin")){
                  String record_count = "SELECT count(*) AS record_count FROM rentalinfo;";
                  ResultSet rs = stmt.executeQuery(record_count);
                  int total_records = 0;
                  while(rs.next()){
                     total_records = rs.getInt("record_count");
                  }
                  System.out.println("There are "+ total_records +" rental records ");
                  String getDriversInfo = "SELECT * from rentalinfo;";
                  rs = stmt.executeQuery(getDriversInfo);
                  while (rs.next()) {
                     Long did = rs.getLong("driver_id");
                     Long cid = rs.getLong("customer_id");
                     String cartype = rs.getString("car_type");
                     String startDate = rs.getString("rental_start");
                     String endDate = rs.getString("rental_end");
                     int amount = rs.getInt("total_amount");

                     System.out.println("Cust Id: " + cid + " | Driver Id: " + did +  " | Start: "+ startDate + " | End Date: "+ endDate + " | Amount: Rs"+ amount );
                  }     
                  rs.close();
               }
               else{
                  System.out.println("Only Admin can access this");
               }
            }
            else if(choice == 17){
               try{
                  conn.setAutoCommit(false);
                  if(role.equals("admin")){
                     System.out.print("Please enter new employee id details: \n");
                     System.out.print("Username: ");
                     String user= sc.next();
                     System.out.print("Password: ");
                     String passwd= sc.next();
                     String record_count = "SELECT COUNT(*) AS record_count FROM users;";
                     ResultSet rs = stmt.executeQuery(record_count);
                     int emp_id = 0;
                     while(rs.next()){
                        emp_id = rs.getInt("record_count") + 1;
                     }
                     //System.out.println("cust_no: " + cust_id);
                     String insertEmployee = "INSERT INTO users values ( " +
                                             emp_id + " , '" + user +  "' , '" + passwd + "' ,'sub' );";
                     stmt.executeUpdate(insertEmployee);
                     System.out.println("New employee id "+ emp_id + " successfully created");
                  }  
                  else{
                     System.out.println("Only Admin can access this");
                  } 
                  conn.commit();
               }
               catch (SQLException se) {
                  // Rollback the transaction in case of exception
                  System.out.println("Rolling back the transaction");
                  if (conn != null) {
                     try {
                        conn.rollback();
                     } catch (SQLException se2) {
                        se2.printStackTrace();
                     }
                  }
                  se.printStackTrace();
               }
            }
            else if(choice == 18){
               try{
                     conn.setAutoCommit(false);
                     if(role.equals("admin")){
                        System.out.print("Please enter employee id details: \n");
                        System.out.print("Username: ");
                        String user= sc.next();
                        System.out.print("Password: ");
                        String passwd= sc.next();
                        
                        //System.out.println("cust_no: " + cust_id);
                        String deleteEmployee = "DELETE  from users where ( username = '" + user + "' and password = '" + passwd + "');";
                        stmt.executeUpdate(deleteEmployee);
                        System.out.println("Employee with username " + user + " successfully deleted");
                     }  
                     else{
                        System.out.println("Only Admin can access this");
                     } 
                     conn.commit();
                  
               }
               catch (SQLException se) {
                  // Rollback the transaction in case of exception
                  System.out.println("Rolling back the transaction");
                  if (conn != null) {
                     try {
                        conn.rollback();
                     } catch (SQLException se2) {
                        se2.printStackTrace();
                     }
                  }
                  se.printStackTrace();
               }
            }
            else if(choice == 19){
               break;    
            }
            else{
               System.out.println("Please enter a correct choice.");
            }
         }
         // STEP 3: Query to database
         // String query = "SELECT type, seater, description, rent_per_day from carInfo";
         // ResultSet rs = stmt.executeQuery(query);
         
         // System.out.println(" Type | Seater |        Description          | Rent(per day) ");
         // // STEP 4: Extract data from result set
         // while (rs.next()) {

         // // Retrieve by column name
         // String type = rs.getString("type");
         // Integer seater = rs.getInt("seater");
         // String description = rs.getString("description");
         // Integer rent_per_day = rs.getInt("rent_per_Day");
         //System.out.println(" " + type + " |   " + seater+ "  |  " +description+ "  |   " + rent_per_day );
         // Display values
      //    System.out.print("Type: " + type);
      //    System.out.print("\nSeater: " + seater);
      //   // System.out.print(", birth date: " + birthDate.toString());
      //   System.out.println("\nDescription: " + description);
        
      //   System.out.println("Rent per day: Rs." + rent_per_day+" \n\n");
         // }

         
         // STEP 5: Clean-up environment
         // rs.close();
         
         stmt.close();
         conn.close();
      } catch (SQLException se) { // Handle errors for JDBC
         se.printStackTrace();
      } catch (Exception e) { // Handle errors for Class.forName
         e.printStackTrace();
      } finally { // finally block used to close resources regardless of whether an exception was
                  // thrown or not
         try {
            if (stmt != null)
               stmt.close();
         } catch (SQLException se2) {
         }
         try {
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         } // end finally try
      } // end try
      System.out.println("End of Code");
   } // end main
} // end class

// Note : By default autocommit is on. you can set to false using
// con.setAutoCommit(false)
