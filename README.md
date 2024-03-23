# User Service API

This backend service provides endpoints for user management, including registration, authentication, profile editing, and verification.

## Endpoints

### Register User
- Method: POST
- Path: `/api/v1/register`
- Input:
  - JSON object with the following fields:
    - `email` (string): Email address of the user
    - `username` (string): Username of the user
    - `password` (string): Password of the user
- Response:
  - `200 OK` if the account is successfully created
  - `400 BAD REQUEST` if the email already exists
  - `409 CONFLICT` if the username already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Login User
- Method: POST
- Path: `/api/v1/login`
- Input:
  - JSON object with the following fields:
    - `username` (string): Username of the user
    - `password` (string): Password of the user
- Response:
  - JSON object containing authentication token if login is successful
  - `200 OK` if successful
  - `401 UNAUTHORIZED` if the username or password is invalid
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete User
- Method: DELETE
- Path: `/api/v1/users/{userID}`
- Input:
  - `userID` (integer): ID of the user to delete
- Response:
  - `200 OK` if the account is successfully deleted
  - `400 BAD REQUEST` if the user with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit User
- Method: PUT
- Path: `/api/v1/users/{userID}`
- Input:
  - `userID` (integer): ID of the user to edit
  - JSON object with optional fields to update:
    - `email` (string): New email address
    - `username` (string): New username
    - `password` (string): New password
- Response:
  - `200 OK` with a success message if the user information is successfully updated
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Send Verification Link
- Method: POST
- Path: `/api/v1/verification`
- Input:
  - JSON object with the following fields:
    - `token` (string): JWT token for verification
- Response:
  - `200 OK` if the verification email is successfully sent
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Verify User
- Method: GET
- Path: `/api/v1/verification/{email}/{token}`
- Input:
  - `email` (string): Email address of the user
  - `token` (string): JWT token for verification
- Response:
  - `200 OK` if the user is successfully verified
  - `400 BAD REQUEST` if the user is already verified
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs




# Country Service API

This backend service provides endpoints for managing countries.

## Endpoints

### Create Country
- Method: POST
- Path: `/api/v1/countries`
- Input:
  - JSON object with the following fields:
    - `country` (string): Name of the country to be created
- Response:
  - `200 OK` if the country is successfully created
  - `409 CONFLICT` if the country already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Countries
- Method: GET
- Path: `/api/v1/countries`
- Response:
  - List of all countries as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Single Country
- Method: GET
- Path: `/api/v1/countries/{countryID}`
- Input:
  - `countryID` (integer): ID of the country to retrieve
- Response:
  - JSON object representing the country
  - `200 OK` if successful
  - `404 NOT FOUND` if the country with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Country
- Method: DELETE
- Path: `/api/v1/countries/{countryID}`
- Input:
  - `countryID` (integer): ID of the country to delete
- Response:
  - `200 OK` if the country is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Country
- Method: PUT
- Path: `/api/v1/countries/{countryID}`
- Input:
  - `countryID` (integer): ID of the country to edit
  - JSON object with the following fields:
    - `country` (string): New name of the country
- Response:
  - `200 OK` if the country is successfully edited
  - `404 NOT FOUND` if the country with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs



# Hotel Service API

This backend service provides endpoints for managing hotels.

## Endpoints

### Create Hotel
- Method: POST
- Path: `/api/v1/hotels`
- Input:
  - JSON object with the following fields:
    - `hotelName` (string): Name of the hotel to be created
- Response:
  - `200 OK` if the hotel is successfully created
  - `409 CONFLICT` if the hotel already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Hotels
- Method: GET
- Path: `/api/v1/hotels`
- Response:
  - List of all hotels as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Single Hotel
- Method: GET
- Path: `/api/v1/hotels/{hotelID}`
- Input:
  - `hotelID` (integer): ID of the hotel to retrieve
- Response:
  - JSON object representing the hotel
  - `200 OK` if successful
  - `404 NOT FOUND` if the hotel with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Hotel
- Method: PUT
- Path: `/api/v1/hotels/{hotelID}`
- Input:
  - `hotelID` (integer): ID of the hotel to edit
  - JSON object with optional fields to update:
    - `hotelName` (string): New name of the hotel
- Response:
  - `200 OK` if the hotel is successfully edited
  - `404 NOT FOUND` if the hotel with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Hotel
- Method: DELETE
- Path: `/api/v1/hotels/{hotelID}`
- Input:
  - `hotelID` (integer): ID of the hotel to delete
- Response:
  - `200 OK` if the hotel is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs


# Room Service API

This backend service provides endpoints for managing rooms in hotels.

## Endpoints

### Create New Room
- Method: POST
- Path: `/api/v1/rooms`
- Input:
  - JSON object with the following fields:
    - `hotelName` (string): Name of the hotel associated with the room
    - `floor` (string): Floor of the room
    - `doorNumber` (string): Door number of the room
    - `roomsNumber` (integer): Number of rooms
    - `bathroomsNumber` (integer): Number of bathrooms
    - `bedsNumber` (integer): Number of beds
    - `price` (double): Price of the room
    - `status` (string): Status of the room (e.g., empty, full)
- Response:
  - `200 OK` if the room is successfully created
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Rooms From Hotel
- Method: GET
- Path: `/api/v1/rooms`
- Response:
  - List of rooms associated with a specific hotel as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Valid Rooms
- Method: GET
- Path: `/api/v1/rooms/valid/rooms`
- Response:
  - List of available (empty) rooms as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Rooms
- Method: DELETE
- Path: `/api/v1/rooms/{roomID}`
- Input:
  - `roomID` (integer): ID of the room to delete
- Response:
  - `200 OK` if the room is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Payment
- Method: POST
- Path: `/api/v1/rooms/payment`
- Input:
  - JSON object with the following fields:
    - `accessToken` (string): Access token for user authentication
    - `paymentId` (integer): ID of the room to make payment for
- Response:
  - `200 OK` if payment is successful
  - `400 BAD REQUEST` if the room is already taken or the user is not verified
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get User Room Payment
- Method: GET
- Path: `/api/v1/rooms/orders/{token}`
- Input:
  - `token` (string): Access token for user authentication
- Response:
  - List of rooms booked by the user as JSON objects
  - `200 OK` if successful
  - `404 NOT FOUND` if no rooms are found for the user
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Room
- Method: PUT
- Path: `/api/v1/rooms/{roomID}`
- Input:
  - `roomID` (integer): ID of the room to edit
  - JSON object with optional fields to update:
    - `hotelName` (string): New name of the hotel
    - `floor` (string): New floor of the room
    - `doorNumber` (string): New door number of the room
    - `roomsNumber` (integer): New number of rooms
    - `bathroomsNumber` (integer): New number of bathrooms
    - `bedsNumber` (integer): New number of beds
    - `price` (double): New price of the room
    - `status` (string): New status of the room
- Response:
  - `200 OK` if the room is successfully updated
  - `404 NOT FOUND` if the room with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Switch Rooms Status To Empty
- Method: POST
- Path: `/api/v1/rooms/switch/status`
- Response:
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Switch Single Room Status To Empty
- Method: POST
- Path: `/api/v1/rooms/switch/status/{roomID}`
- Input:
  - `roomID` (integer): ID of the room to switch status
- Response:
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs



# Plane Service API

This backend service provides endpoints for managing planes.

## Endpoints

### Create Plane
- Method: POST
- Path: `/api/v1/planes`
- Input:
  - JSON object with the following fields:
    - `planeName` (string): Name of the plane
    - `sitsCount` (integer): Number of seats in the plane
- Response:
  - `200 OK` if the plane is successfully created
  - `409 CONFLICT` if the plane name already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Planes
- Method: GET
- Path: `/api/v1/planes`
- Response:
  - List of all planes as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Single Plane
- Method: GET
- Path: `/api/v1/planes/{planeID}`
- Input:
  - `planeID` (integer): ID of the plane to retrieve
- Response:
  - JSON object representing the plane with the specified ID
  - `200 OK` if successful
  - `404 NOT FOUND` if the plane with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Plane
- Method: PUT
- Path: `/api/v1/planes/{planeID}`
- Input:
  - `planeID` (integer): ID of the plane to edit
  - JSON object with optional fields to update:
    - `planeName` (string): New name of the plane
    - `sitsCount` (integer): New number of seats in the plane
- Response:
  - `200 OK` if the plane is successfully updated
  - `404 NOT FOUND` if the plane with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Plane
- Method: DELETE
- Path: `/api/v1/planes/{planeID}`
- Input:
  - `planeID` (integer): ID of the plane to delete
- Response:
  - `200 OK` if the plane is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs


# Visa Service API

This backend service provides endpoints for managing visas.

## Endpoints

### Get Single Visa
- Method: GET
- Path: `/api/v1/visas/{visaID}`
- Input:
  - `visaID` (integer): ID of the visa to retrieve
- Response:
  - JSON object representing the visa with the specified ID
  - `200 OK` if successful
  - `404 NOT FOUND` if the visa with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Valid Visas
- Method: GET
- Path: `/api/v1/visas/valid/visas`
- Response:
  - List of all valid visas (status = "empty") as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Visa
- Method: PUT
- Path: `/api/v1/visas/{visaID}`
- Input:
  - `visaID` (integer): ID of the visa to edit
  - JSON object with optional fields to update:
    - `airportLaunch` (string): New launch airport of the visa
    - `airportLand` (string): New land airport of the visa
    - `timeLaunch` (string): New launch time of the visa
    - `timeLand` (string): New land time of the visa
    - `placeNumber` (string): New number of the visa place
    - `price` (double): New price of the visa
    - `status` (string): New status of the visa
- Response:
  - `200 OK` if the visa is successfully updated
  - `404 NOT FOUND` if the visa with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Visa
- Method: DELETE
- Path: `/api/v1/visas/{visaID}`
- Input:
  - `visaID` (integer): ID of the visa to delete
- Response:
  - `200 OK` if the visa is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Payment for Visa
- Method: POST
- Path: `/api/v1/visas/payment`
- Input:
  - JSON object with the following fields:
    - `accessToken` (string): Access token for user authentication
    - `paymentId` (integer): ID of the visa for payment
- Response:
  - `200 OK` if the payment is successful
  - `400 BAD REQUEST` if the visa has already been taken or user verification is not completed
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get User Visa Payment
- Method: GET
- Path: `/api/v1/visas/payment`
- Input:
  - `accessToken` (string): Access token for user authentication
- Response:
  - List of all visas associated with the authenticated user as JSON objects
  - `200 OK` if successful
  - `400 BAD REQUEST` if the user ID does not match or no visas found for the user
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Switch Visas Status to Empty
- Method: PUT
- Path: `/api/v1/visas/switch/status`
- Response:
  - `200 OK` if the status of all full visas is successfully switched to empty
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Switch Single Visa Status to Empty
- Method: PUT
- Path: `/api/v1/visas/switch/status/{visaID}`
- Input:
  - `visaID` (integer): ID of the visa to switch status
- Response:
  - `200 OK` if the status of the visa is successfully switched to empty
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs


# Package Service API

This backend service provides endpoints for managing packages, benefits, roadmaps, and todos.

## Endpoints

### Create Package
- Method: POST
- Path: `/api/v1/packages`
- Input:
  - JSON object with fields:
    - `packageName` (string): Name of the package
    - `country` (string): Name of the country associated with the package
    - `price` (double): Price of the package
    - `description` (string): Description of the package
- Response:
  - `200 OK` if the package is successfully created
  - `409 CONFLICT` if the package name already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Create Benefit for Package
- Method: POST
- Path: `/api/v1/packages/benefits`
- Input:
  - JSON object with fields:
    - `packageId` (integer): ID of the package to associate the benefit with
    - `benefit` (string): Description of the benefit
- Response:
  - `200 OK` if the benefit is successfully created and associated with the package
  - `409 CONFLICT` if the benefit already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Create Roadmap for Package
- Method: POST
- Path: `/api/v1/packages/roadmaps`
- Input:
  - JSON object with fields:
    - `packageId` (integer): ID of the package to associate the roadmap with
    - `roadmap` (string): Description of the roadmap
- Response:
  - `200 OK` if the roadmap is successfully created and associated with the package
  - `409 CONFLICT` if the roadmap already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Create Todos for Package
- Method: POST
- Path: `/api/v1/packages/todos`
- Input:
  - JSON object with fields:
    - `packageId` (integer): ID of the package to associate the todos with
    - `todos` (string): Description of the todos
    - `coins` (integer): Number of coins associated with the todos
- Response:
  - `200 OK` if the todos are successfully created and associated with the package
  - `409 CONFLICT` if the todos already exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Packages
- Method: GET
- Path: `/api/v1/packages`
- Response:
  - List of all packages as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Single Package
- Method: GET
- Path: `/api/v1/packages/{packageID}`
- Input:
  - `packageID` (integer): ID of the package to retrieve
- Response:
  - JSON object representing the package with the specified ID
  - `200 OK` if successful
  - `404 NOT FOUND` if the package with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Packages From Country
- Method: GET
- Path: `/api/v1/packages/country/packages`
- Input:
  - JSON object with fields:
    - `country` (string): Name of the country to retrieve packages from
- Response:
  - List of all packages associated with the specified country as JSON objects
  - `200 OK` if successful
  - `404 NOT FOUND` if no packages found for the country
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Benefits
- Method: GET
- Path: `/api/v1/packages/benefits`
- Response:
  - List of all benefits as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Roadmaps
- Method: GET
- Path: `/api/v1/packages/roadmaps`
- Response:
  - List of all roadmaps as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get All Todos
- Method: GET
- Path: `/api/v1/packages/todos`
- Response:
  - List of all todos as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Package
- Method: PUT
- Path: `/api/v1/packages/{packageID}`
- Input:
  - `packageID` (integer): ID of the package to edit
  - JSON object with optional fields to update:
    - `packageName` (string): New name of the package
    - `country` (string): New country associated with the package
    - `price` (double): New price of the package
    - `description` (string): New description of the package
- Response:
  - `200 OK` if the package is successfully updated
  - `404 NOT FOUND` if the package with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Benefit
- Method: PUT
- Path: `/api/v1/packages/benefit/{benefitID}`
- Input:
  - `benefitID` (integer): ID of the benefit to edit
  - JSON object with optional fields to update:
    - `benefit` (string): New description of the benefit
- Response:
  - `200 OK` if the benefit is successfully updated
  - `404 NOT FOUND` if the benefit with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Roadmap
- Method: PUT
- Path: `/api/v1/packages/roadmap/{roadmapID}`
- Input:
  - `roadmapID` (integer): ID of the roadmap to edit
  - JSON object with optional fields to update:
    - `roadmap` (string): New description of the roadmap
- Response:
  - `200 OK` if the roadmap is successfully updated
  - `404 NOT FOUND` if the roadmap with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Todos
- Method: PUT
- Path: `/api/v1/packages/todo/{todoID}`
- Input:
  - `todoID` (integer): ID of the todos to edit
  - JSON object with optional fields to update:
    - `todos` (string): New description of the todos
    - `coins` (integer): New number of coins associated with the todos
- Response:
  - `200 OK` if the todos are successfully updated
  - `404 NOT FOUND` if the todos with the provided ID do not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Package
- Method: DELETE
- Path: `/api/v1/packages/{packageID}`
- Input:
  - `packageID` (integer): ID of the package to delete
- Response:
  - `200 OK` if the package is successfully deleted
  - `404 NOT FOUND` if the package
