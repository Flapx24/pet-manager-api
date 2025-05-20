# API Documentation - Pet Manager

This document provides a comprehensive overview of all endpoints in the Pet Manager API.

## Table of Contents
1. [Authentication](#authentication)
    - [Register](#register)
    - [Login](#login)
2. [Animals](#animals)
    - [Get All Animals](#get-all-animals)
    - [Get Animal by ID](#get-animal-by-id)
    - [Create Animal](#create-animal)
    - [Update Animal](#update-animal)
    - [Delete Animal](#delete-animal)
3. [Health Issues](#health-issues)
    - [Get All Health Issues](#get-all-health-issues)
    - [Get Health Issue by ID](#get-health-issue-by-id)
    - [Create Health Issue](#create-health-issue)
    - [Update Health Issue](#update-health-issue)
    - [Delete Health Issue](#delete-health-issue)
4. [Vaccines](#vaccines)
    - [Get All Vaccines](#get-all-vaccines)
    - [Get Vaccine by ID](#get-vaccine-by-id)
    - [Create Vaccine](#create-vaccine)
    - [Update Vaccine](#update-vaccine)
    - [Confirm Vaccine Application](#confirm-vaccine-application)
    - [Delete Vaccine](#delete-vaccine)
    - [Get Non-Expired Vaccines](#get-non-expired-vaccines)
    - [Get Confirmed Vaccines](#get-confirmed-vaccines)
    - [Get Animals with Pending Vaccines](#get-animals-with-pending-vaccines)

---

## Authentication

### Register

Creates a new user account.

- **URL**: `/api/auth/register`
- **Method**: `POST`
- **Authentication**: Not required

**Request Body:**
```json
{
  "name": "John",
  "surname": "Doe",
  "email": "john.doe@example.com",
  "password": "Password1!"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Registro exitoso"
}
```

### Login

Authenticates a user and returns a JWT token.

- **URL**: `/api/auth/login`
- **Method**: `POST`
- **Authentication**: Not required

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "Password1!"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login exitoso",
  "name": "John",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## Animals

### Get All Animals

Returns all animals belonging to the authenticated user with optional filtering, pagination, and sorting.

- **URL**: `/api/animals`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| detailLevel | No | "BASIC" | Level of detail (BASIC or FULL) |
| name | No | null | Filter by animal name |
| animalType | No | null | Filter by animal type |
| startDate | No | null | Filter by birthdate range (start) |
| endDate | No | null | Filter by birthdate range (end) |
| page | No | 0 | Page number (0-indexed) |
| size | No | 10 | Number of items per page |
| sortBy | No | "name" | Field to sort by |
| direction | No | "ASC" | Sort direction (ASC or DESC) |
| paginated | No | false | Whether to return paginated results |

**Response:**
```json
{
  "success": true,
  "message": "Animales recuperados correctamente",
  "data": [
    {
      "id": 1,
      "name": "Max",
      "birthDate": "2018-05-10",
      "registrationDate": "2023-01-15",
      "weightKg": 25.5,
      "color": "Brown",
      "gender": "Male",
      "animalType": "DOG",
      "notes": "Very friendly",
      "diet": "Royal Canin",
      "neutered": true,
      "lastDeworming": "2022-12-05"
    },
    {
      "id": 2,
      "name": "Luna",
      "birthDate": "2020-03-22",
      "registrationDate": "2023-02-10",
      "weightKg": 4.2,
      "color": "Gray",
      "gender": "Female",
      "animalType": "CAT",
      "neutered": true,
      "indoorOnly": true
    }
  ]
}
```

For paginated response:
```json
{
  "success": true,
  "message": "Animales recuperados correctamente",
  "data": {
    "animals": [
      {
        "id": 1,
        "name": "Max",
        "birthDate": "2018-05-10",
        "animalType": "DOG"
      }
    ],
    "currentPage": 0,
    "totalItems": 10,
    "totalPages": 1
  }
}
```

### Get Animal by ID

Returns a specific animal by its ID.

- **URL**: `/api/animals/{id}`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| id | Animal ID |

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| detailLevel | No | "FULL" | Level of detail (BASIC or FULL) |

**Response:**
```json
{
  "success": true,
  "message": "Animal recuperado correctamente",
  "data": {
    "id": 1,
    "name": "Max",
    "birthDate": "2018-05-10",
    "registrationDate": "2023-01-15",
    "weightKg": 25.5,
    "color": "Brown",
    "gender": "Male",
    "animalType": "DOG",
    "notes": "Very friendly",
    "diet": "Royal Canin",
    "neutered": true,
    "lastDeworming": "2022-12-05",
    "specificFields": {
      "breed": "Golden Retriever",
      "size": "Large",
      "coatType": "Double coat",
      "pedigree": true
    },
    "healthIssues": [
      {
        "id": 1,
        "name": "Ear infection",
        "diagnosisDate": "2022-10-15",
        "recoveryDate": "2022-10-30",
        "description": "Right ear infection",
        "treatment": "Antibiotics"
      }
    ]
  }
}
```

### Create Animal

Creates a new animal for the authenticated user.

- **URL**: `/api/animals`
- **Method**: `POST`
- **Authentication**: Required (JWT Token)

**Request Body:**

Different animal types require different fields:

For a dog:
```json
{
  "name": "Max",
  "birthDate": "2018-05-10",
  "weightKg": 25.5,
  "color": "Brown",
  "gender": "Male",
  "neutered": true,
  "animalType": "DOG",
  "notes": "Very friendly",
  "diet": "Royal Canin",
  "lastDeworming": "2022-12-05",
  "breed": "Golden Retriever",
  "size": "Large",
  "coatType": "Double coat",
  "pedigree": true
}
```

For a cat:
```json
{
  "name": "Luna",
  "birthDate": "2020-03-22",
  "weightKg": 4.2,
  "color": "Gray",
  "gender": "Female",
  "neutered": true,
  "animalType": "CAT",
  "notes": "Very independent",
  "diet": "Purina",
  "lastDeworming": "2023-01-15",
  "breed": "Mixed",
  "indoorOnly": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Animal creado correctamente",
  "data": {
    "id": 1,
    "name": "Max",
    "birthDate": "2018-05-10",
    "registrationDate": "2023-05-19",
    "weightKg": 25.5,
    "color": "Brown",
    "gender": "Male",
    "animalType": "DOG",
    "notes": "Very friendly",
    "diet": "Royal Canin",
    "neutered": true,
    "lastDeworming": "2022-12-05",
    "specificFields": {
      "breed": "Golden Retriever",
      "size": "Large",
      "coatType": "Double coat",
      "pedigree": true
    }
  }
}
```

### Update Animal

Updates an existing animal.

- **URL**: `/api/animals/{id}`
- **Method**: `PUT`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| id | Animal ID |

**Request Body:**
Same structure as the Create Animal endpoint.

**Response:**
```json
{
  "success": true,
  "message": "Animal actualizado correctamente",
  "data": {
    "id": 1,
    "name": "Max",
    "birthDate": "2018-05-10",
    "registrationDate": "2023-01-15",
    "weightKg": 27.5,
    "color": "Brown and White",
    "gender": "Male",
    "animalType": "DOG",
    "notes": "Very friendly",
    "diet": "Royal Canin",
    "neutered": true,
    "lastDeworming": "2023-05-05",
    "specificFields": {
      "breed": "Golden Retriever",
      "size": "Large",
      "coatType": "Double coat",
      "pedigree": true
    }
  }
}
```

### Delete Animal

Deletes an animal.

- **URL**: `/api/animals/{id}`
- **Method**: `DELETE`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| id | Animal ID |

**Response:**
```json
{
  "success": true,
  "message": "Animal eliminado correctamente"
}
```

---

## Health Issues

### Get All Health Issues

Returns all health issues for a specific animal with optional filtering, pagination, and sorting.

- **URL**: `/api/animals/{animalId}/health-issues`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| name | No | null | Filter by issue name |
| startDate | No | null | Filter by diagnosis date range (start) |
| endDate | No | null | Filter by diagnosis date range (end) |
| page | No | 0 | Page number (0-indexed) |
| size | No | 10 | Number of items per page |
| sortBy | No | "diagnosisDate" | Field to sort by |
| direction | No | "DESC" | Sort direction (ASC or DESC) |
| paginated | No | false | Whether to return paginated results |

**Response:**
```json
{
  "success": true,
  "message": "Problemas de salud recuperados correctamente",
  "data": [
    {
      "id": 1,
      "name": "Ear infection",
      "diagnosisDate": "2022-10-15",
      "recoveryDate": "2022-10-30",
      "description": "Right ear infection",
      "treatment": "Antibiotics"
    },
    {
      "id": 2,
      "name": "Skin allergy",
      "diagnosisDate": "2023-01-20",
      "recoveryDate": null,
      "description": "Redness and itching on belly",
      "treatment": "Special shampoo and diet"
    }
  ]
}
```

For paginated response:
```json
{
  "success": true,
  "message": "Problemas de salud recuperados correctamente",
  "data": {
    "healthIssues": [
      {
        "id": 1,
        "name": "Ear infection",
        "diagnosisDate": "2022-10-15",
        "recoveryDate": "2022-10-30",
        "description": "Right ear infection",
        "treatment": "Antibiotics"
      }
    ],
    "currentPage": 0,
    "totalItems": 5,
    "totalPages": 1
  }
}
```

### Get Health Issue by ID

Returns a specific health issue by its ID.

- **URL**: `/api/animals/{animalId}/health-issues/{healthIssueId}`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| healthIssueId | Health Issue ID |

**Response:**
```json
{
  "success": true,
  "message": "Problema de salud recuperado correctamente",
  "data": {
    "id": 1,
    "name": "Ear infection",
    "diagnosisDate": "2022-10-15",
    "recoveryDate": "2022-10-30",
    "description": "Right ear infection",
    "treatment": "Antibiotics"
  }
}
```

### Create Health Issue

Creates a new health issue for a specific animal.

- **URL**: `/api/animals/{animalId}/health-issues`
- **Method**: `POST`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |

**Request Body:**
```json
{
  "name": "Ear infection",
  "diagnosisDate": "2022-10-15",
  "recoveryDate": "2022-10-30",
  "description": "Right ear infection",
  "treatment": "Antibiotics"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Problema de salud creado correctamente",
  "data": {
    "id": 1,
    "name": "Ear infection",
    "diagnosisDate": "2022-10-15",
    "recoveryDate": "2022-10-30",
    "description": "Right ear infection",
    "treatment": "Antibiotics"
  }
}
```

### Update Health Issue

Updates an existing health issue.

- **URL**: `/api/animals/{animalId}/health-issues/{healthIssueId}`
- **Method**: `PUT`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| healthIssueId | Health Issue ID |

**Request Body:**
```json
{
  "name": "Ear infection",
  "diagnosisDate": "2022-10-15",
  "recoveryDate": "2022-10-31",
  "description": "Right ear infection, severe",
  "treatment": "Antibiotics and ear drops"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Problema de salud actualizado correctamente",
  "data": {
    "id": 1,
    "name": "Ear infection",
    "diagnosisDate": "2022-10-15",
    "recoveryDate": "2022-10-31",
    "description": "Right ear infection, severe",
    "treatment": "Antibiotics and ear drops"
  }
}
```

### Delete Health Issue

Deletes a health issue.

- **URL**: `/api/animals/{animalId}/health-issues/{healthIssueId}`
- **Method**: `DELETE`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| healthIssueId | Health Issue ID |

**Response:**
```json
{
  "success": true,
  "message": "Problema de salud eliminado correctamente"
}
```

---

## Vaccines

### Get All Vaccines

Returns all vaccines for a specific animal with optional date filtering, pagination, and sorting.

- **URL**: `/api/animals/{animalId}/vaccines`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| page | No | 0 | Page number (0-indexed) |
| size | No | 10 | Number of items per page |
| sortBy | No | "applicationDate" | Field to sort by |
| direction | No | "DESC" | Sort direction (ASC or DESC) |
| startDate | No | null | Filter by date range (start) |
| endDate | No | null | Filter by date range (end) |
| dateType | No | "application" | Type of date to filter by ("application" or "expiration") |

**Response:**
```json
{
  "success": true,
  "message": "Vaccine history successfully retrieved",
  "data": {
    "vaccines": [
      {
        "id": 1,
        "name": "Rabies",
        "applicationDate": "2022-05-15",
        "expirationDate": null,
        "description": "Annual rabies vaccination",
        "animalId": 1
      },
      {
        "id": 2,
        "name": "DHPP",
        "applicationDate": "2022-05-15",
        "expirationDate": null,
        "description": "Distemper, Hepatitis, Parainfluenza, Parvo",
        "animalId": 1
      },
      {
        "id": 3,
        "name": "Rabies",
        "applicationDate": null,
        "expirationDate": "2023-05-15",
        "description": "Annual rabies booster due",
        "animalId": 1
      }
    ],
    "currentPage": 0,
    "totalItems": 3,
    "totalPages": 1
  }
}
```

### Get Vaccine by ID

Returns a specific vaccine by its ID.

- **URL**: `/api/animals/{animalId}/vaccines/{vaccineId}`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| vaccineId | Vaccine ID |

**Response:**
```json
{
  "success": true,
  "message": "Vaccine successfully retrieved",
  "data": {
    "id": 1,
    "name": "Rabies",
    "applicationDate": "2022-05-15",
    "expirationDate": null,
    "description": "Annual rabies vaccination",
    "animalId": 1
  }
}
```

### Create Vaccine

Creates a new vaccine for a specific animal.

- **URL**: `/api/animals/{animalId}/vaccines`
- **Method**: `POST`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |

**Request Body:**
```json
{
  "name": "Rabies",
  "expirationDate": "2023-05-15",
  "description": "Annual rabies vaccination due"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Vaccine successfully created",
  "data": {
    "id": 3,
    "name": "Rabies",
    "applicationDate": null,
    "expirationDate": "2023-05-15",
    "description": "Annual rabies vaccination due",
    "animalId": 1
  }
}
```

### Update Vaccine

Updates an existing vaccine.

- **URL**: `/api/animals/{animalId}/vaccines/{vaccineId}`
- **Method**: `PUT`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| vaccineId | Vaccine ID |

**Request Body:**
```json
{
  "name": "Rabies",
  "expirationDate": "2023-06-01",
  "description": "Annual rabies vaccination due (rescheduled)"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Vaccine successfully updated",
  "data": {
    "id": 3,
    "name": "Rabies",
    "applicationDate": null,
    "expirationDate": "2023-06-01",
    "description": "Annual rabies vaccination due (rescheduled)",
    "animalId": 1
  }
}
```

### Confirm Vaccine Application

Marks a vaccine as applied by setting applicationDate to the current date and removing the expirationDate.

- **URL**: `/api/animals/{animalId}/vaccines/{vaccineId}/apply`
- **Method**: `PATCH`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| vaccineId | Vaccine ID |

**Response:**
```json
{
  "success": true,
  "message": "Vaccine successfully applied",
  "data": {
    "id": 3,
    "name": "Rabies",
    "applicationDate": "2023-05-19",
    "expirationDate": null,
    "description": "Annual rabies vaccination due (rescheduled)",
    "animalId": 1
  }
}
```

### Delete Vaccine

Deletes a vaccine.

- **URL**: `/api/animals/{animalId}/vaccines/{vaccineId}`
- **Method**: `DELETE`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |
| vaccineId | Vaccine ID |

**Response:**
```json
{
  "success": true,
  "message": "Vaccine successfully deleted"
}
```

### Get Non-Expired Vaccines

Returns all vaccines that have not expired (future expiration date).

- **URL**: `/api/animals/{animalId}/vaccines/non-expired`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| page | No | 0 | Page number (0-indexed) |
| size | No | 10 | Number of items per page |
| sortBy | No | "expirationDate" | Field to sort by |
| direction | No | "ASC" | Sort direction (ASC or DESC) |

**Response:**
```json
{
  "success": true,
  "message": "Non-expired vaccines successfully retrieved",
  "data": {
    "vaccines": [
      {
        "id": 3,
        "name": "Rabies",
        "applicationDate": null,
        "expirationDate": "2023-06-01",
        "description": "Annual rabies vaccination due (rescheduled)",
        "animalId": 1
      },
      {
        "id": 4,
        "name": "Bordetella",
        "applicationDate": null,
        "expirationDate": "2023-08-15",
        "description": "Kennel cough vaccination due",
        "animalId": 1
      }
    ],
    "currentPage": 0,
    "totalItems": 2,
    "totalPages": 1
  }
}
```

### Get Confirmed Vaccines

Returns all vaccines that have been applied (have an application date).

- **URL**: `/api/animals/{animalId}/vaccines/confirmed`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Path Parameters:**

| Parameter | Description |
|-----------|-------------|
| animalId | Animal ID |

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| page | No | 0 | Page number (0-indexed) |
| size | No | 10 | Number of items per page |
| sortBy | No | "applicationDate" | Field to sort by |
| direction | No | "DESC" | Sort direction (ASC or DESC) |

**Response:**
```json
{
  "success": true,
  "message": "Confirmed vaccines successfully retrieved",
  "data": {
    "vaccines": [
      {
        "id": 1,
        "name": "Rabies",
        "applicationDate": "2022-05-15",
        "expirationDate": null,
        "description": "Annual rabies vaccination",
        "animalId": 1
      },
      {
        "id": 2,
        "name": "DHPP",
        "applicationDate": "2022-05-15",
        "expirationDate": null,
        "description": "Distemper, Hepatitis, Parainfluenza, Parvo",
        "animalId": 1
      }
    ],
    "currentPage": 0,
    "totalItems": 2,
    "totalPages": 1
  }
}
```

---

## Pending Vaccines

### Get Animals with Pending Vaccines

Returns all animals that have pending vaccines (vaccines with future expiration dates that haven't been applied yet).

- **URL**: `/api/vaccines/pending-animals`
- **Method**: `GET`
- **Authentication**: Required (JWT Token)

**Query Parameters:**

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| page | No | 0 | Page number (0-indexed) |
| size | No | 10 | Number of items per page |
| sortBy | No | "name" | Field to sort by |
| direction | No | "ASC" | Sort direction (ASC or DESC) |

**Response:**
```json
{
  "success": true,
  "message": "Animals with pending vaccines successfully retrieved",
  "data": {
    "animals": [
      {
        "id": 1,
        "name": "Max",
        "type": "Dog",
        "breed": "Labrador Retriever",
        "birthDate": "2019-03-15",
        "userId": 1
      },
      {
        "id": 3,
        "name": "Whiskers",
        "type": "Cat",
        "breed": "Siamese",
        "birthDate": "2020-01-10",
        "userId": 1
      }
    ],
    "currentPage": 0,
    "totalItems": 2,
    "totalPages": 1
  }
}
```

For paginated response:
```json
{
  "success": true,
  "message": "Animales con vacunas pendientes recuperados correctamente",
  "data": {
    "animals": [
      {
        "id": 1,
        "name": "Max",
        "nextVaccineDate": "2023-06-01",
        "vaccinesPending": 1
      }
    ],
    "currentPage": 0,
    "totalItems": 2,
    "totalPages": 1
  }
}
```
