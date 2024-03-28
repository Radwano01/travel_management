### Country Service API

#### Create Country

- **Method:** `POST`
- **Path:** `/api/v1/countries`
- **Input:**
  - JSON object with the following fields:
    - `country` (string): Name of the country to be created
- **Response:**
  - `200 OK` if the country is successfully created
  - `409 CONFLICT` if the country already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Countries

- **Method:** `GET`
- **Path:** `/api/v1/countries`
- **Response:**
  - List of all countries as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single Country

- **Method:** `GET`
- **Path:** `/api/v1/countries/{countryID}`
- **Input:**
  - `countryID` (integer): ID of the country to retrieve
- **Response:**
  - JSON object representing the country
  - `200 OK` if successful
  - `404 NOT FOUND` if the country with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Country

- **Method:** `PUT`
- **Path:** `/api/v1/countries/{countryID}`
- **Input:**
  - `countryID` (integer): ID of the country to edit
  - JSON object with optional fields to update:
    - `country` (string): New name of the country
- **Response:**
  - `200 OK` if the country is successfully edited
  - `404 NOT FOUND` if the country with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Delete Country

- **Method:** `DELETE`
- **Path:** `/api/v1/countries/{countryID}`
- **Input:**
  - `countryID` (integer): ID of the country to delete
- **Response:**
  - `200 OK` if the country is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Create Country Details

- **Method:** `POST`
- **Path:** `/api/v1/countries/details/{countryID}`
- **Input:**
  - JSON object with the following fields:
    - `place` (string): Place details
    - `description` (string): Description of the place
    - `imageOne` (string): Image URL
    - `imageTwo` (string): Image URL
    - `imageThree` (string): Image URL
- **Response:**
  - `200 OK` if the country details are successfully created
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Countries Details

- **Method:** `GET`
- **Path:** `/api/v1/countries/details`
- **Response:**
  - List of all country details as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single Country Detail

- **Method:** `GET`
- **Path:** `/api/v1/countries/details/{countryDetailsID}`
- **Input:**
  - `countryDetailsID` (integer): ID of the country details to retrieve
- **Response:**
  - JSON object representing the country details
  - `200 OK` if successful
  - `404 NOT FOUND` if the country details with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Country Details

- **Method:** `PUT`
- **Path:** `/api/v1/countries/details/{countryDetailsID}`
- **Input:**
  - `countryDetailsID` (integer): ID of the country details to edit
  - JSON object with optional fields to update:
    - `place` (string): New place details
    - `description` (string): New description of the place
    - `imageOne` (string): New image URL
    - `imageTwo` (string): New image URL
    - `imageThree` (string): New image URL
- **Response:**
  - `200 OK` if the country details are successfully edited
  - `404 NOT FOUND` if the country details with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Delete Country Details

- **Method:** `DELETE`
- **Path:** `/api/v1/countries/details/{countryDetailsID}`
- **Input:**
  - `countryDetailsID` (integer): ID of the country details to delete
- **Response:**
  - `200 OK` if the country details are successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs


### Hotel Service API

#### Create Hotel

- **Method:** `POST`
- **Path:** `/api/v1/hotels`
- **Input:**
  - JSON object with the following fields:
    - `hotelName` (string): Name of the hotel to be created
    - `country` (string): Country of the hotel
- **Response:**
  - `200 OK` if the hotel is successfully created
  - `409 CONFLICT` if the hotel already exists
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Hotels

- **Method:** `GET`
- **Path:** `/api/v1/hotels`
- **Response:**
  - List of all hotels as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single Hotel

- **Method:** `GET`
- **Path:** `/api/v1/hotels/{hotelID}`
- **Input:**
  - `hotelID` (integer): ID of the hotel to retrieve
- **Response:**
  - JSON object representing the hotel
  - `200 OK` if successful
  - `404 NOT FOUND` if the hotel with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Hotel

- **Method:** `PUT`
- **Path:** `/api/v1/hotels/{hotelID}`
- **Input:**
  - `hotelID` (integer): ID of the hotel to edit
  - JSON object with optional fields to update:
    - `hotelName` (string): New name of the hotel
    - `country` (string): New country of the hotel
- **Response:**
  - `200 OK` if the hotel is successfully edited
  - `404 NOT FOUND` if the hotel with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Delete Hotel

- **Method:** `DELETE`
- **Path:** `/api/v1/hotels/{hotelID}`
- **Input:**
  - `hotelID` (integer): ID of the hotel to delete
- **Response:**
  - `200 OK` if the hotel is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Hotels Info

- **Method:** `GET`
- **Path:** `/api/v1/hotels/info`
- **Response:**
  - List of hotel information as JSON objects
  - `200 OK` if successful
  - `404 NOT FOUND` if no hotels are found
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs


### Room Service API

#### Create New Room

- **Method:** `POST`
- **Path:** `/api/v1/rooms/{hotelID}`
- **Input:**
  - JSON object with the following fields:
    - `hotelName` (string): Name of the hotel associated with the room
    - `floor` (string): Floor of the room
    - `doorNumber` (string): Door number of the room
    - `roomsNumber` (integer): Number of rooms
    - `bathroomsNumber` (integer): Number of bathrooms
    - `bedsNumber` (integer): Number of beds
    - `price` (double): Price of the room
    - `status` (string): Status of the room (e.g., empty, full)
- **Response:**
  - `200 OK` if the room is successfully created
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Hotel Rooms

- **Method:** `GET`
- **Path:** `/api/v1/rooms`
- **Response:**
  - List of rooms associated with hotels as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single Room

- **Method:** `GET`
- **Path:** `/api/v1/rooms/{roomID}`
- **Input:**
  - `roomID` (integer): ID of the room to retrieve
- **Response:**
  - JSON object representing the room
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Valid Rooms

- **Method:** `GET`
- **Path:** `/api/v1/rooms/valid/rooms`
- **Response:**
  - List of available (empty) rooms as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Room

- **Method:** `PUT`
- **Path:** `/api/v1/rooms/{roomID}`
- **Input:**
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
- **Response:**
  - `200 OK` if the room is successfully updated
  - `404 NOT FOUND` if the room with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Delete Room

- **Method:** `DELETE`
- **Path:** `/api/v1/rooms/{roomID}`
- **Input:**
  - `roomID` (integer): ID of the room to delete
- **Response:**
  - `200 OK` if the room is successfully deleted
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get User Room Orders

- **Method:** `GET`
- **Path:** `/api/v1/rooms/orders/{token}`
- **Input:**
  - `token` (string): Access token for user authentication
- **Response:**
  - List of rooms booked by the user as JSON objects
  - `200 OK` if successful
  - `404 NOT FOUND` if no rooms are found for the user
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Payment

- **Method:** `POST`
- **Path:** `/api/v1/rooms/payment`
- **Input:**
  - JSON object with the following fields:
    - `accessToken` (string): Access token for user authentication
    - `paymentId` (integer): ID of the room to make payment for
- **Response:**
  - `200 OK` if payment is successful
  - `400 BAD REQUEST` if the room is already taken or the user is not verified
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Room Status

- **Method:** `POST`
- **Path:** `/api/v1/rooms/switch/status`
- **Response:**
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Single Room Status

- **Method:** `POST`
- **Path:** `/api/v1/rooms/switch/status/{roomID}`
- **Input:**
  - `roomID` (integer): ID of the room to switch status
- **Response:**
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs



### Package Service API

#### Create Package

- **Method:** `POST`
- **Path:** `/api/v1/packages`
- **Input:**
  - JSON object with the following fields:
    - `packageName` (string): Name of the package
    - `country` (string): Country associated with the package
    - `price` (double): Price of the package
    - `description` (string): Description of the package
- **Response:**
  - `200 OK` if the package is successfully created
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Create Benefit for Package

- **Method:** `POST`
- **Path:** `/api/v1/packages/benefits/{packageID}`
- **Input:**
  - JSON object with the following fields:
    - `benefit` (string): Benefit to add to the package
- **Response:**
  - `200 OK` if the benefit is successfully created for the package
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Create Roadmap for Package

- **Method:** `POST`
- **Path:** `/api/v1/packages/roadmaps/{packageID}`
- **Input:**
  - JSON object with the following fields:
    - `roadmap` (string): Roadmap to add to the package
- **Response:**
  - `200 OK` if the roadmap is successfully created for the package
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Create Todos for Package

- **Method:** `POST`
- **Path:** `/api/v1/packages/todos/{packageID}`
- **Input:**
  - JSON object with the following fields:
    - `todos` (string): Todos to add to the package
    - `coins` (integer): Coins associated with the todos
- **Response:**
  - `200 OK` if the todos are successfully created for the package
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Packages

- **Method:** `GET`
- **Path:** `/api/v1/packages`
- **Response:**
  - List of packages as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single Package

- **Method:** `GET`
- **Path:** `/api/v1/packages/{packageID}`
- **Input:**
  - `packageID` (integer): ID of the package to retrieve
- **Response:**
  - JSON object representing the package
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs
 
#### Get Packages (packageName,country,description) from Country

- **Method:** `GET`
- **Path:** `/api/v1/packages/package/{countryID}`
- **Response:**
- List of packages from the specified country as JSON objects
  - `200 OK` if successful
  - `404 NOT FOUND` if no packages are found for the provided country
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Benefits

- **Method:** `GET`
- **Path:** `/api/v1/packages/benefits`
- **Response:**
  - List of benefits as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Roadmaps

- **Method:** `GET`
- **Path:** `/api/v1/packages/roadmaps`
- **Response:**
  - List of roadmaps as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get All Todos

- **Method:** `GET`
- **Path:** `/api/v1/packages/todos`
- **Response:**
  - List of todos as JSON objects
  - `200 OK` if successful
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Package

- **Method:** `PUT`
- **Path:** `/api/v1/packages/{packageID}`
- **Input:**
  - `packageID` (integer): ID of the package to edit
  - JSON object with optional fields to update:
    - `packageName` (string): New name of the package
    - `country` (string): New country associated with the package
    - `price` (double): New price of the package
    - `description` (string): New description of the package
- **Response:**
  - `200 OK` if the package is successfully updated
  - `404 NOT FOUND` if the package with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Benefit

- **Method:** `PUT`
- **Path:** `/api/v1/packages/benefit/{benefitID}`
- **Input:**
  - `benefitID` (integer): ID of the benefit to edit
  - JSON object with optional fields to update:
    - `benefit` (string): New name of the benefit
- **Response:**
  - `200 OK` if the benefit is successfully updated
  - `404 NOT FOUND` if the benefit with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Roadmap

- **Method:** `PUT`
- **Path:** `/api/v1/packages/roadmap/{roadmapID}`
- **Input:**
  - `roadmapID` (integer): ID of the roadmap to edit
  - JSON object with optional fields to update:
    - `roadmap` (string): New roadmap
- **Response:**
  - `200 OK` if the roadmap is successfully updated
  - `404 NOT FOUND` if the roadmap with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Todos

- **Method:** `PUT`
- **Path:** `/api/v1/packages/todo/{todoID}`
- **Input:**
  - `todoID` (integer): ID of the todo to edit
  - JSON object with optional fields to update:
    - `todos` (string): New todos
    - `coins` (integer): New coins associated with the todos
- **



# Visa Service

This service manages visas for planes.

## Endpoints

### Create Visa

- **Method:** `POST`
- **Path:** `/api/v1/visas/{planeID}`
- **Request Body:**
  ```json
  {
    "placeNumber": "string",
    "planeName": "string",
    "price": 0,
    "status": "string"
  }
- **Response:**
  - `200 OK` if the visa is created successfully
  - `400 BAD REQUEST` if the plane's visa count exceeds 100
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Single Visa

- **Method:** `GET`
- **Path:** `/api/v1/visas/{visaID}`
- **Response:**
  - `200 OK` along with the visa details if found
  - `404 NOT FOUND` if the visa with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get Valid Visas

- **Method:** `GET`
- **Path:** `/api/v1/visas/valid/visas`
- **Response:**
  - `200 OK` along with a list of valid visas (status: empty)
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit Visa

- **Method:** `PUT`
- **Path:** `/api/v1/visas/{visaID}`
- **Request Body:**
  ```json
  {
    // Optional fields to update
    "placeNumber": "string",
    "price": 0,
    "status": "string"
  }
- **Response:**
  - `200 OK` if the visa is successfully updated
  - `404 NOT FOUND` if the visa with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete Visa

- **Method:** `DELETE`
- **Path:** `/api/v1/visas/{visaID}`
- **Response:**
  - `200 OK` if the visa is deleted successfully
  - `404 NOT FOUND` if the visa with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Payment

- **Method:** `POST`
- **Path:** `/api/v1/visas/payment`
- **Request Body:**
  ```json
  {
    "accessToken": "string",
    "paymentId": 0
  }
- **Response:**
  - `200 OK` if the payment is successful
  - `400 BAD REQUEST` if the visa has already been taken or user is not verified
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Get User Visa Payment

- **Method:** `GET`
- **Path:** `/api/v1/visas/payment`
- **Request Header:**
  - `Authorization: Bearer {token}`
- **Response:**
  - `200 OK` along with a list of visas belonging to the user
  - `400 BAD REQUEST` if the user ID in token doesn't match
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs



 # User Service

This service manages user-related operations.

## Endpoints

### Register User

- **Method:** `POST`
- **Path:** `/api/v1/register`
- **Request Body:**
  ```json
  {
    "email": "string",
    "username": "string",
    "password": "string"
  }
- **Response:**
  - `200 OK` if the account is created successfully
  - `400 BAD REQUEST` if email already exists or username is taken
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Login User

- **Method:** `POST`
- **Path:** `/api/v1/login`
- **Request Body:**
  ```json
  {
    "username": "string",
    "password": "string"
  }
- **Response:**
  - `200 OK` along with authentication token if login is successful
  - `401 UNAUTHORIZED` if username or password is incorrect
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Delete User

- **Method:** `DELETE`
- **Path:** `/api/v1/{userID}`
- **Response:**
  - `200 OK` if the account is deleted successfully
  - `400 BAD REQUEST` if the user ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Edit User

- **Method:** `PUT`
- **Path:** `/api/v1/{userID}`
- **Request Body:**
  ```json
  {
    "email": "string",
    "username": "string",
    "password": "string"
  }
- **Response:**
  - `200 OK` along with a message indicating the updated fields
  - `404 NOT FOUND` if the user with the provided ID does not exist
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Verify User

- **Method:** `GET`
- **Path:** `/api/v1/verification/{email}`
- **Response:**
  - `200 OK` if the user is successfully verified
  - `400 BAD REQUEST` if the user is already verified or email is not found
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### Send Verification Link

- **Method:** `POST`
- **Path:** `/api/v1/verification`
- **Request Body:**
  ```json
  {
    "token": "string"
  }
- **Response:**
  - `200 OK` if the verification email is sent successfully
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

### User Package Service API



#### Make Payment for User Package

- **Method:** `POST`
- **Path:** `/api/v1/user_packages/payment/{token}`
- **Input:**
  - JSON object with the following fields:
    - `paymentIntent` (string): Payment intent for the transaction
- **Response:**
  - `200 OK` if the payment is successful
  - `400 BAD REQUEST` if the user is not verified
  - `404 NOT FOUND` if there are no empty rooms available
  - `402 PAYMENT REQUIRED` if the payment fails
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single User Packages

- **Method:** `GET`
- **Path:** `/api/v1/user_packages/{token}`
- **Response:**
  - JSON object containing details of the user's packages
  - `200 OK` if packages are retrieved successfully
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single User Todos

- **Method:** `GET`
- **Path:** `/api/v1/user_packages/todos/{token}`
- **Response:**
  - JSON object containing available todos for the user
  - `200 OK` if todos are retrieved successfully
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Edit Todos Status

- **Method:** `PUT`
- **Path:** `/api/v1/user_packages/todo/{id}/{token}`
- **Input:**
  - `id` (integer): ID of the todo to mark as completed
- **Response:**
  - `200 OK` if the todo status is successfully updated
  - `404 NOT FOUND` if the todo or todo ID is not found
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs

#### Get Single User Coins

- **Method:** `GET`
- **Path:** `/api/v1/user_packages/coins/{token}`
- **Response:**
  - JSON object containing the number of coins for the user
  - `200 OK` if coins are retrieved successfully
  - `500 INTERNAL SERVER ERROR` if an unexpected error occurs
