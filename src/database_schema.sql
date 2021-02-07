CREATE DATABASE elitehotel;
USE elitehotel;

-- DROP DATABASE elitehotel;

CREATE TABLE Customer (
	customerId INT PRIMARY KEY,
    firstName VARCHAR(60) NOT NULL, CHECK(firstName != ''),
    lastName VARCHAR(60) NOT NULL, CHECK(lastName != ''),
    phoneNumber VARCHAR(20)
);

CREATE TABLE Room (
	roomNumber INT PRIMARY KEY,
    roomType VARCHAR(20) NOT NULL, CHECK(roomType != '')
);

CREATE TABLE Booking (
	bookingId INT PRIMARY KEY AUTO_INCREMENT,
    roomNumber INT, FOREIGN KEY (roomNumber) REFERENCES Room(roomNumber),
    customerId INT, FOREIGN KEY (customerId) REFERENCES Customer(customerId),
    checkInDate DATE NOT NULL,
    checkOutDate DATE
);

CREATE TABLE FoodItem (
    foodItemType VARCHAR(20) PRIMARY KEY
);

INSERT INTO FoodItem VALUES ('SANDWICH'), ('PASTA'), ('NOODLES'), ('DRINK');

CREATE TABLE Bill (
	billId INT PRIMARY KEY AUTO_INCREMENT,
    roomNumber INT, FOREIGN KEY (roomNumber) REFERENCES Room(roomNumber),
    complete BOOL NOT NULL
);

CREATE TABLE BillFoodItem (
	billId INT, FOREIGN KEY (billId) REFERENCES Bill(billId),
    foodItemType VARCHAR(20), FOREIGN KEY (foodItemType) REFERENCES FoodItem(foodItemType)
);

CREATE VIEW billView AS
SELECT Bill.billId, roomNumber, FoodItem.foodItemType, Bill.complete FROM Bill
JOIN BillFoodItem ON Bill.billId = BillFoodItem.billId
JOIN FoodItem ON FoodItem.foodItemType = BillFoodItem.foodItemType