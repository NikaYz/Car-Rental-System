// import java.sql.*;
// import java.util.Scanner;

// public class MainApplication {
//     private DatabaseManager dbManager;
//     private UserService userService;
//     private CustomerService customerService;
//     private DriverService driverService;
//     private CarService carService;
//     private RentalService rentalService;
//     private Scanner scanner;
//     private String currentRole;
    
//     public MainApplication() throws SQLException, ClassNotFoundException {
//         this.dbManager = new DatabaseManager();
//         this.userService = new UserService(dbManager);
//         this.customerService = new CustomerService(dbManager);
//         this.driverService = new DriverService(dbManager);
//         this.carService = new CarService(dbManager);
//         this.rentalService = new RentalService(dbManager);
//         this.scanner = new Scanner(System.in);
//     }
    
//     public void start() throws SQLException {
//         authenticateUser();
//         if (!currentRole.isEmpty()) {
//             showMainMenu();
//         }
//         dbManager.close();
//     }
    
//     private void authenticateUser() throws SQLException {
//         System.out.print("Please enter your username: ");
//         String username = scanner.next();
//         System.out.print("Please enter password: ");
//         String password = scanner.next();
        
//         currentRole = userService.authenticate(username, password);
        
//         if (currentRole.isEmpty()) {
//             System.out.println("You are not registered in database ask the admin to add you");
//         } else {
//             System.out.println("You are successfully login as " + currentRole);
//         }
//     }
    
//     private void showMainMenu() throws SQLException {
//         while (true) {
//             printMenuOptions();
//             int choice = scanner.nextInt();
//             System.out.println("--------------------------------------------------------------");
            
//             switch (choice) {
//                 case 1:
//                     handleAddCustomer();
//                     break;
//                 case 2:
//                     handleAddDriver();
//                     break;
//                 case 3:
//                     handleShowCarMenu();
//                     break;
//                 case 4:
//                     handleBookCar();
//                     break;
//                 case 5:
//                     handleTripDetails();
//                     break;
//                 case 6:
//                     handleRentalCount();
//                     break;
//                 case 7:
//                     handleTotalSales();
//                     break;
//                 case 8:
//                     handleUpdateCustomer();
//                     break;
//                 case 9:
//                     handleUpdateDriver();
//                     break;
//                 case 10:
//                     handleDeleteDriver();
//                     break;
//                 case 11:
//                     handleAddCarModel();
//                     break;
//                 case 12:
//                     handleDriverPerformance();
//                     break;
//                 case 13:
//                     handleCarTypePerformance();
//                     break;
//                 case 14:
//                     handleShowAllCustomers();
//                     break;
//                 case 15:
//                     handleShowAllDrivers();
//                     break;
//                 case 16:
//                     handleShowAllRentalInfo();
//                     break;
//                 case 17:
//                     handleAddEmployee();
//                     break;
//                 case 18:
//                     handleDeleteEmployee();
//                     break;
//                 case 19:
//                     return;
//                 default:
//                     System.out.println("Please enter a correct choice.");
//             }
//         }
//     }
    
//     private void printMenuOptions() {
//         System.out.println("\n--------------------------------------------------------------");
//         System.out.println("1) Add a new customer");
//         System.out.println("2) Add a new driver");
//         System.out.println("3) Show car menu table");
//         System.out.println("4) Book a car");
//         System.out.println("5) Details of a trip");
//         System.out.println("6) No of cars rented during a month or yearly");
//         System.out.println("7) Total sales during a month or yearly");
//         System.out.println("8) Update customer details");
//         System.out.println("9) Update driver details");
//         System.out.println("10) Delete a driver details");
//         System.out.println("11) Add a new car model");
//         System.out.println("12) Get driver tours or amount in ascending order");
//         System.out.println("13) Get amount or tours by car type group");
//         System.out.println("14) Show all the customers");
//         System.out.println("15) Show all the drivers");
//         System.out.println("16) Show all the rentalInfo");
//         System.out.println("17) Enter new employee");
//         System.out.println("18) Delete an employee");
//         System.out.println("19) Exit from the application");
//         System.out.println("Enter your choice: ");
//     }
    
//     private void handleAddCustomer() {
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please enter new customer details: \n");
//             System.out.print("First name: ");
//             String fname = scanner.next();
//             System.out.print("Last name: ");
//             String lname = scanner.next();
//             System.out.print("Email Id: ");
//             String email = scanner.next();
//             System.out.print("Phone Number: ");
//             long phoneNo = scanner.nextLong();
            
//             customerService.addCustomer(fname, lname, email, phoneNo);
//             dbManager.commit();
//             System.out.println("New customer successfully inserted");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleAddDriver() {
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please enter new driver details: \n");
//             System.out.print("First name: ");
//             String fname = scanner.next();
//             System.out.print("Last name: ");
//             String lname = scanner.next();
//             System.out.print("Phone Number: ");
//             long phoneNo = scanner.nextLong();
            
//             System.out.print("Car Type: ");
//             ResultSet rs = carService.getAvailableCarTypes();
//             while(rs.next()) {
//                 String type = rs.getString("type");
//                 System.out.print(type + " , ");
//             }
//             System.out.print("\nSelect from these: ");
//             String type = scanner.next();
            
//             System.out.print("Car model: ");
//             String carModel = scanner.next();
//             System.out.print("Car number: ");
//             String carNo = scanner.next();
            
//             driverService.addDriver(fname, lname, phoneNo, type, carModel, carNo);
//             dbManager.commit();
//             System.out.println("New driver successfully inserted");
            
//             // Show driver details
//             System.out.print("Please provide your phone no which you entered lately: ");
//             phoneNo = scanner.nextLong();
//             rs = driverService.getDriverByPhone(phoneNo);
//             long drivId = 0;
//             String driverFname = "", driverLname = "", carType = "", carNumber = "";
//             carModel = "";
//             while (rs.next()) {
//                 drivId = rs.getLong("driver_id");
//                 driverFname = rs.getString("first_name");
//                 driverLname = rs.getString("last_name");
//                 carType = rs.getString("car_type");
//                 carModel = rs.getString("car_model");
//                 carNumber = rs.getString("car_no");
//                 System.out.println("Name: " + driverFname + " " + driverLname +  
//                                  ", phone number: " + phoneNo + ", car type: "+ carType + 
//                                  ", car model: " + carModel + ", car number: " + carNumber);
//             }
//             rs.close();
            
//             if (drivId == 0) {
//                 System.out.println("Please enter a correct phone no, please try again");
//                 return;
//             }
            
//             // Update driver details if needed
//             boolean updating = true;
//             while(updating) {
//                 System.out.println("Please select number, what you wish to update: ");
//                 System.out.println("1: First Name");
//                 System.out.println("2: Last Name");
//                 System.out.println("3: Phone no");
//                 System.out.println("4: Car type");
//                 System.out.println("5: Car model");
//                 System.out.println("6: Car number");
//                 System.out.println("7: To exit");
//                 int c = scanner.nextInt();
                
//                 switch (c) {
//                     case 1:
//                         System.out.println("Please enter new first name");
//                         driverFname = scanner.next();
//                         break;
//                     case 2:
//                         System.out.print("Please enter new last name: ");
//                         driverLname = scanner.next();
//                         break;
//                     case 3:
//                         System.out.print("Please enter new phone number:");
//                         phoneNo = scanner.nextLong();
//                         break;
//                     case 4:
//                         rs = carService.getAvailableCarTypes();
//                         while(rs.next()) {
//                             type = rs.getString("type");
//                             System.out.print(type + " , ");
//                         }
//                         rs.close();
//                         System.out.print("\nSelect new type from these: ");
//                         carType = scanner.next();
//                         break;
//                     case 5:
//                         System.out.print("Please enter new car model: ");
//                         carModel = scanner.next();
//                         break;
//                     case 6:
//                         System.out.print("Please enter new car number: ");
//                         carNumber = scanner.next();
//                         break;
//                     case 7:
//                         updating = false;
//                         break;
//                     default:
//                         System.out.println("Wrong option selected!");
//                 }
//             }
            
//             driverService.updateDriver(drivId, driverFname, driverLname, phoneNo, carType, carModel, carNumber);
//             dbManager.commit();
//             System.out.println("Successfully updated driver");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleShowCarMenu() {
//         try {
//             ResultSet rs = carService.getAllCarInfo();
//             while (rs.next()) {
//                 String type = rs.getString("type");
//                 Integer seater = rs.getInt("seater");
//                 String description = rs.getString("description");
//                 Integer rentPerDay = rs.getInt("rent_per_Day");
                
//                 System.out.print("Type: " + type);
//                 System.out.print("\nSeater: " + seater);
//                 System.out.println("\nDescription: " + description);
//                 System.out.println("Rent per day: Rs." + rentPerDay + " \n");
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleBookCar() {
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please provide customer's phone no: ");
//             long phoneNo = scanner.nextLong();
            
//             ResultSet rs = customerService.getCustomerByPhone(phoneNo);
//             long custId = 0;
//             while (rs.next()) {
//                 custId = rs.getLong("customer_id");
//             }
//             rs.close();
            
//             if (custId == 0) {
//                 System.out.println("Please enter a correct phone no, please try again");
//                 return;
//             }
            
//             System.out.print("Please provide start date(in format YYYY-MM-DD): ");
//             String startDate = scanner.next();
//             System.out.print("Please provide end date(in format YYYY-MM-DD): ");
//             String endDate = scanner.next();
            
//             rs = driverService.getAvailableDrivers(startDate, endDate);
//             while (rs.next()) {
//                 String driverId = rs.getString("driver_id");
//                 String carType = rs.getString("car_type");
//                 String carModel = rs.getString("car_model");
//                 Integer rentPerDay = rs.getInt("rent_per_Day");
//                 System.out.println("Driver_id: " + driverId + " | Car_type: " + carType + 
//                                    " | Car_model: " + carModel + " | Rent_per_day: Rs." + rentPerDay);
//             }
//             rs.close();
            
//             System.out.print("\nPlease select the driver_id which you want from above:");
//             int driverId = scanner.nextInt();
            
//             String carType = "";
//             rs = driverService.getDriverByPhone(driverId);
//             while (rs.next()) {
//                 carType = rs.getString("car_type");
//             }
//             rs.close();
            
//             int price = carService.getRentPerDay(carType);
            
//             System.out.print("Are you sure you want to continue(y/n): ");
//             String confirm = scanner.next();
//             if (confirm.equalsIgnoreCase("y")) {
//                 int totalAmount = (calculateDaysBetween(startDate, endDate) + 1) * price;
//                 rentalService.bookCar(custId, driverId, carType, startDate, endDate, totalAmount);
//                 dbManager.commit();
//                 System.out.println("Booking confirmed !");
//                 System.out.println("Thanks for booking a car :) ");
                
//                 rs = driverService.getDriverByPhone(driverId);
//                 while (rs.next()) {
//                     String fname = rs.getString("first_name");
//                     String lname = rs.getString("last_name");
//                     long phNo = rs.getLong("phone_no");
//                     String cmodel = rs.getString("car_model");
//                     String cno = rs.getString("car_no");
//                     System.out.println("Driver Details: ");
//                     System.out.println("Name: "+ fname + " " + lname);
//                     System.out.println("Phone number: " + phNo);
//                     System.out.println("Car Type: " + carType);
//                     System.out.println("Car Model: " + cmodel);
//                     System.out.println("Car Number: " + cno);
//                 }
//                 rs.close();
//             } else {
//                 System.out.println("Booking unconfirmed.");
//             }
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private int calculateDaysBetween(String startDate, String endDate) {
//         // Simple calculation - in real app use proper date parsing
//         return (int) ((java.sql.Date.valueOf(endDate).getTime() - 
//                       java.sql.Date.valueOf(startDate).getTime())) / (1000 * 60 * 60 * 24);
//     }
    
//     private void handleTripDetails() {
//         try {
//             System.out.print("Please provide customers phone no: ");
//             long phoneNo = scanner.nextLong();
            
//             ResultSet rs = customerService.getCustomerByPhone(phoneNo);
//             long custId = 0;
//             while (rs.next()) {
//                 custId = rs.getLong("customer_id");
//             }
//             rs.close();
            
//             if (custId == 0) {
//                 System.out.println("Please enter a correct phone no, please try again");
//                 return;
//             }
            
//             System.out.print("Please provide start date(in format YYYY-MM-DD): ");
//             String startDate = scanner.next();
            
//             rs = rentalService.getRentalDetails(custId, startDate);
//             while (rs.next()) {
//                 String customerfname = rs.getString("customer_f");
//                 String customerlname = rs.getString("customer_l");
//                 Long customerphoneno = rs.getLong("customer_p");
//                 String driverfname = rs.getString("first_name");
//                 String dricerlname = rs.getString("last_name");
//                 Long driverphoneno = rs.getLong("phone_no");
//                 String cartype = rs.getString("car_type");
//                 String carmodel = rs.getString("car_model");
//                 String carno = rs.getString("car_no");
//                 String rstart = rs.getString("rental_start");
//                 String rend = rs.getString("rental_end");
//                 int total = rs.getInt("total_amount");
                
//                 System.out.println("Customer Details: ");
//                 System.out.println("Name: "+ customerfname + " " + customerlname);
//                 System.out.println("Phone no: "+ customerphoneno);
//                 System.out.println("\nDriver Details: ");
//                 System.out.println("Name: "+ driverfname + " " + dricerlname);
//                 System.out.println("Phone no: "+ driverphoneno);
//                 System.out.println("Car number: " + carno + " , Car model: " + carmodel + ", Car type: "+ cartype);
//                 System.out.println("Rental start: "+ rstart + ", Rental end: " + rend);
//                 System.out.println("Total amount: "+ total + "\n\n");
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleRentalCount() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             System.out.print("Want to calculate 1)yearly or 2)monthly . Select 1 or 2: ");
//             int periodChoice = scanner.nextInt();
            
//             if (periodChoice == 1) {
//                 System.out.print("Enter year in (YYYY): ");
//                 String year = scanner.next();
//                 int count = rentalService.getRentalCount("yearly", year, "");
//                 System.out.println("No of car rented: " + count + ", in year: "+ year);
//             } else if (periodChoice == 2) {
//                 System.out.print("Enter year in (YYYY): ");
//                 String year = scanner.next();
//                 System.out.print("Enter month in (MM): ");
//                 String month = scanner.next();
//                 int count = rentalService.getRentalCount("monthly", year, month);
//                 System.out.println("No of car rented: " + count + ", in year: "+ year + ", month: " + month);
//             } else {
//                 System.out.println("Please enter correct input again.");
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleTotalSales() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             System.out.print("Want to calculate 1)yearly or 2)monthly . Select 1 or 2: ");
//             int periodChoice = scanner.nextInt();
            
//             if (periodChoice == 1) {
//                 System.out.print("Enter year in (YYYY): ");
//                 String year = scanner.next();
//                 long total = rentalService.getTotalSales("yearly", year, "");
//                 System.out.println("Total sales: " + total + ", in year: "+ year);
//             } else if (periodChoice == 2) {
//                 System.out.print("Enter year in (YYYY): ");
//                 String year = scanner.next();
//                 System.out.print("Enter month in (MM): ");
//                 String month = scanner.next();
//                 long total = rentalService.getTotalSales("monthly", year, month);
//                 System.out.println("Total sales: " + total + ", in year: "+ year + ", month: " + month);
//             } else {
//                 System.out.println("Please enter correct input again.");
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleUpdateCustomer() {
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please provide customers phone no which you entered last: ");
//             long phoneNo = scanner.nextLong();
            
//             ResultSet rs = customerService.getCustomerByPhone(phoneNo);
//             long custId = 0;
//             String fname = "", lname = "", email = "";
//             while (rs.next()) {
//                 custId = rs.getLong("customer_id");
//                 fname = rs.getString("first_name");
//                 lname = rs.getString("last_name");
//                 email = rs.getString("email");
//                 System.out.println("Name: " + fname + " " + lname + ", email: " + email + ", phone number: " + phoneNo);
//             }
//             rs.close();
            
//             if (custId == 0) {
//                 System.out.println("Please enter a correct phone no, please try again");
//                 return;
//             }
            
//             boolean updating = true;
//             while(updating) {
//                 System.out.println("Please select number, what you wish to update: ");
//                 System.out.println("1: First Name");
//                 System.out.println("2: Last Name");
//                 System.out.println("3: Email id");
//                 System.out.println("4: Phone no");
//                 System.out.println("5: To exit");
//                 int c = scanner.nextInt();
                
//                 switch (c) {
//                     case 1:
//                         System.out.println("Please enter new first name");
//                         fname = scanner.next();
//                         break;
//                     case 2:
//                         System.out.print("Please enter new last name: ");
//                         lname = scanner.next();
//                         break;
//                     case 3:
//                         System.out.print("Please enter new email: ");
//                         email = scanner.next();
//                         break;
//                     case 4:
//                         System.out.print("Please enter new phone number:");
//                         phoneNo = scanner.nextLong();
//                         break;
//                     case 5:
//                         updating = false;
//                         break;
//                     default:
//                         System.out.println("Wrong option selected!");
//                 }
//             }
            
//             customerService.updateCustomer(custId, fname, lname, email, phoneNo);
//             dbManager.commit();
//             System.out.println("Successfully updated customer");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleUpdateDriver() {
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please provide your phone no which you entered lately: ");
//             long phoneNo = scanner.nextLong();
            
//             ResultSet rs = driverService.getDriverByPhone(phoneNo);
//             long drivId = 0;
//             String fname = "", lname = "", carType = "", carModel = "", carNo = "";
//             while (rs.next()) {
//                 drivId = rs.getLong("driver_id");
//                 fname = rs.getString("first_name");
//                 lname = rs.getString("last_name");
//                 carType = rs.getString("car_type");
//                 carModel = rs.getString("car_model");
//                 carNo = rs.getString("car_no");
//                 System.out.println("Name: " + fname + " " + lname + ", phone number: " + phoneNo + 
//                                  ", car type: "+ carType + ", car model: " + carModel + ", car number: " + carNo);
//             }
//             rs.close();
            
//             if (drivId == 0) {
//                 System.out.println("Please enter a correct phone no, please try again");
//                 return;
//             }
            
//             boolean updating = true;
//             while(updating) {
//                 System.out.println("Please select number, what you wish to update: ");
//                 System.out.println("1: First Name");
//                 System.out.println("2: Last Name");
//                 System.out.println("3: Phone no");
//                 System.out.println("4: Car type");
//                 System.out.println("5: Car model");
//                 System.out.println("6: Car number");
//                 System.out.println("7: To exit");
//                 int c = scanner.nextInt();
                
//                 switch (c) {
//                     case 1:
//                         System.out.println("Please enter new first name");
//                         fname = scanner.next();
//                         break;
//                     case 2:
//                         System.out.print("Please enter new last name: ");
//                         lname = scanner.next();
//                         break;
//                     case 3:
//                         System.out.print("Please enter new phone number:");
//                         phoneNo = scanner.nextLong();
//                         break;
//                     case 4:
//                         rs = carService.getAvailableCarTypes();
//                         while(rs.next()) {
//                             String type = rs.getString("type");
//                             System.out.print(type + " , ");
//                         }
//                         rs.close();
//                         System.out.print("\nSelect new type from these: ");
//                         carType = scanner.next();
//                         break;
//                     case 5:
//                         System.out.print("Please enter new car model: ");
//                         carModel = scanner.next();
//                         break;
//                     case 6:
//                         System.out.print("Please enter new car number: ");
//                         carNo = scanner.next();
//                         break;
//                     case 7:
//                         updating = false;
//                         break;
//                     default:
//                         System.out.println("Wrong option selected!");
//                 }
//             }
            
//             driverService.updateDriver(drivId, fname, lname, phoneNo, carType, carModel, carNo);
//             dbManager.commit();
//             System.out.println("Successfully updated driver");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleDeleteDriver() {
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please provide your phone no which you entered lately: ");
//             long phoneNo = scanner.nextLong();
            
//             ResultSet rs = driverService.getDriverByPhone(phoneNo);
//             long drivId = 0;
//             String fname = "", lname = "", carType = "", carModel = "", carNo = "";
//             while (rs.next()) {
//                 drivId = rs.getLong("driver_id");
//                 fname = rs.getString("first_name");
//                 lname = rs.getString("last_name");
//                 carType = rs.getString("car_type");
//                 carModel = rs.getString("car_model");
//                 carNo = rs.getString("car_no");
//                 System.out.println("Name: " + fname + " " + lname + ", phone number: " + phoneNo + 
//                                  ", car type: "+ carType + ", car model: " + carModel + ", car number: " + carNo);
//             }
//             rs.close();
            
//             if (drivId == 0) {
//                 System.out.println("Please enter a correct phone no, please try again");
//                 return;
//             }
            
//             System.out.println("Are you sure you want to delete this driver(y,n): ");
//             String confirm = scanner.next();
//             if (confirm.equalsIgnoreCase("y")) {
//                 driverService.deactivateDriver(drivId);
//                 dbManager.commit();
//                 System.out.println("Deleting a driver completed successfully.");
//             } else {
//                 System.out.println("Delete unsuccessful");
//             }
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleAddCarModel() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please enter new car model details: \n");
//             System.out.print("Type: ");
//             String type = scanner.next();
//             System.out.print("Seater: ");
//             int seater = scanner.nextInt();
//             System.out.print("Description(use end at last of string): ");
//             String description = "";
//             while (scanner.hasNext()) {
//                 String word = scanner.next();
//                 if (word.equals("end")) {
//                     break;
//                 }
//                 description = description + word + " ";
//             }
            
//             System.out.print("Rent per day: ");
//             int rentPerDay = scanner.nextInt();
            
//             carService.addCarModel(type, seater, description, rentPerDay);
//             dbManager.commit();
//             System.out.println("Successfully added new car model");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleDriverPerformance() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             System.out.println("You wish to see in terms of 1)tours or 2)amount or 3)both:");
//             System.out.println("Select 1 or 2 or 3:");
//             int choice = scanner.nextInt();
            
//             ResultSet rs = rentalService.getDriverPerformance(choice);
//             while (rs.next()) {
//                 long id = rs.getLong("driver_id");
//                 String fname = rs.getString("first_name");
//                 String lname = rs.getString("last_name");
//                 if (lname == null) lname = "";
                
//                 if (choice == 1) {
//                     int tours = rs.getInt("tours");
//                     System.out.println("Id: "+ id + " | Name:" + fname + " " + lname + " | No. of tours: "+tours);
//                 } else if (choice == 2) {
//                     long amount = rs.getLong("amount");
//                     System.out.println("Id: "+ id + " | Name:" + fname + " " + lname + " | Amount: Rs"+ amount);
//                 } else {
//                     long amount = rs.getLong("amount");
//                     int tours = rs.getInt("tours");
//                     System.out.println("Id: "+ id + " | Name:" + fname + " " + lname + 
//                                       " | Amount: Rs"+ amount + " | Tours: " + tours);
//                 }
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleCarTypePerformance() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             System.out.println("You wish to see in terms of 1)amount or 2)tours or 3)both:");
//             System.out.println("Select 1 or 2 or 3:");
//             int choice = scanner.nextInt();
            
//             ResultSet rs = rentalService.getCarTypePerformance(choice);
//             while (rs.next()) {
//                 String ctype = rs.getString("car_type");
                
//                 if (choice == 1) {
//                     long amount = rs.getLong("amount");
//                     System.out.println("Car type: "+ ctype + " | Amount: Rs" + amount);
//                 } else if (choice == 2) {
//                     int tours = rs.getInt("tours");
//                     System.out.println("Car type: "+ ctype + " | Tours:" + tours);
//                 } else {
//                     long amount = rs.getLong("amount");
//                     int tours = rs.getInt("tours");
//                     System.out.println("Car type: "+ ctype + " | Amount: Rs" + amount + " | Tours: " + tours);
//                 }
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleShowAllCustomers() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             int totalCustomers = customerService.getCustomerCount();
//             System.out.println("There are "+ totalCustomers +" customers");
            
//             ResultSet rs = customerService.getAllCustomers();
//             while (rs.next()) {
//                 Long id = rs.getLong("customer_id");
//                 String fname = rs.getString("first_name");
//                 String lname = rs.getString("last_name");
//                 Long phoneno = rs.getLong("phoneno");
//                 String email = rs.getString("email");
//                 System.out.println("Id: " + id + " | Name: " + fname + " " + lname + 
//                                    " | Phone No: "+ phoneno + " | Email: "+email);
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleShowAllDrivers() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             int totalDrivers = driverService.getDriverCount();
//             System.out.println("There are "+ totalDrivers +" drivers");
            
//             ResultSet rs = driverService.getAllDrivers();
//             while (rs.next()) {
//                 Long id = rs.getLong("driver_id");
//                 String fname = rs.getString("first_name");
//                 String lname = rs.getString("last_name");
//                 if (lname == null) lname = "";
//                 Long phoneno = rs.getLong("phone_no");
//                 String cartype = rs.getString("car_type");
//                 String carmodel = rs.getString("car_model");
//                 String carno = rs.getString("car_no");
//                 Boolean available = rs.getBoolean("working_in_company");

//                 System.out.println("Id: " + id + " | Name: " + fname + " " + lname + 
//                                  " | Phone No: "+ phoneno + " | CarModel: "+ carmodel + 
//                                  " | CarNo: "+ carno + " | Working: "+ available);
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleShowAllRentalInfo() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             int totalRecords = rentalService.getRentalInfoCount();
//             System.out.println("There are "+ totalRecords +" rental records");
            
//             ResultSet rs = rentalService.getAllRentalInfo();
//             while (rs.next()) {
//                 Long did = rs.getLong("driver_id");
//                 Long cid = rs.getLong("customer_id");
//                 String cartype = rs.getString("car_type");
//                 String startDate = rs.getString("rental_start");
//                 String endDate = rs.getString("rental_end");
//                 int amount = rs.getInt("total_amount");

//                 System.out.println("Cust Id: " + cid + " | Driver Id: " + did + 
//                                   " | Start: "+ startDate + " | End Date: "+ endDate + 
//                                   " | Amount: Rs"+ amount);
//             }
//             rs.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
    
//     private void handleAddEmployee() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please enter new employee id details: \n");
//             System.out.print("Username: ");
//             String user = scanner.next();
//             System.out.print("Password: ");
//             String passwd = scanner.next();
            
//             int empId = userService.getNextUserId();
//             userService.addEmployee(empId, user, passwd, "sub");
//             dbManager.commit();
//             System.out.println("New employee id "+ empId + " successfully created");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleDeleteEmployee() {
//         if (!currentRole.equals("admin")) {
//             System.out.println("Only Admin can access this");
//             return;
//         }
        
//         try {
//             dbManager.setAutoCommit(false);
//             System.out.print("Please enter employee id details: \n");
//             System.out.print("Username: ");
//             String user = scanner.next();
//             System.out.print("Password: ");
//             String passwd = scanner.next();
            
//             userService.deleteEmployee(user, passwd);
//             dbManager.commit();
//             System.out.println("Employee with username " + user + " successfully deleted");
//         } catch (SQLException e) {
//             handleSQLException(e);
//         }
//     }
    
//     private void handleSQLException(SQLException e) {
//         System.out.println("Rolling back the transaction");
//         dbManager.rollback();
//         e.printStackTrace();
//     }
    
//     public static void main(String[] args) {
//         try {
//             MainApplication app = new MainApplication();
//             app.start();
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//         System.out.println("End of Code");
//     }
// }