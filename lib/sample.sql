-- Insert records into drivers table
INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company)
VALUES (1, 'John', 'Doe', 9876543210, 'SUV', 'Toyota Highlander', 123, TRUE);

INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company)
VALUES (2, 'Alice', 'Smith', 1234567890, 'Sedan', 'Honda Accord', 124, TRUE);

INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company)
VALUES (3, 'Bob', 'Johnson', 1122334455, 'Truck', 'Ford F-150', 125, FALSE);

INSERT INTO drivers (driver_id, first_name, last_name, phone_no, car_type, car_model, car_no, working_in_company)
VALUES (4, 'Charlie', 'Williams', 5566778899, 'SUV', 'Toyota Highlander', 126, TRUE);

-- Insert records into carInfo table
INSERT INTO carInfo (id, type, seater, description, rent_per_day)
VALUES (1, 'SUV', 5, 'Comfortable family car with good mileage', 100);

INSERT INTO carInfo (id, type, seater, description, rent_per_day)
VALUES (2, 'Sedan', 4, 'Compact car ideal for city drives', 75);

INSERT INTO carInfo (id, type, seater, description, rent_per_day)
VALUES (3, 'Luxury', 4, 'Luxury car with premium features', 250);

INSERT INTO carInfo (id, type, seater, description, rent_per_day)
VALUES (4, 'Van', 12, 'Spacious van for large groups or families', 150);
