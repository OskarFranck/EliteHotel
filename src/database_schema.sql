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