# dropwizard-library

This project is simple libray application.
DB is postgresql
Techstack for backend service:
- Java 8
- Dropwizard
- Jersey
- Liquibase

UI is built using Angular 8.

#Run instruction
Run instructions for both services are in README files in particular service folders.

## Features
1. User is able to store book by sending following request
HTTP POST
URL: http://localhost/book
Headers: Accept: application/json
         Content-Type: application/json
Body: [{
  "title": "string",
  "genre": "string",
  "authors": ["string"],
  "pageNumber": integer,
  "isbn": "string"
}]

If everyhting is ok service will return 200 OK HTTP Response.
If there is some validation error, service will return 400 Bad Request with Error Message in body.
Some validation rules are: 
- ISBN should have 10 or 13 characters
- ISBN cannot be empty
- Authors cannot be empty

2. User is able to get all books by sending following request:
HTTP POST
URL: http://localhost/book
Headers: Accept: application/json

Service will return 200 OK reponse with JSON array in body:
Body: [{
  "id": "Long"
  "title": "string",
  "genre": "string",
  "authors": ["string"],
  "pageNumber": integer,
  "isbn": "string"
}]

3. User is able to search book by ISBNs by sending following request:
HTTP POST
URL: http://localhost/book?isbn=1234567890,1234567891
Headers: Accept: application/json

Service will return empty array if there is no books with sent ISBNs or JSON array of books in body:
Body: [{
  "id": "Long"
  "title": "string",
  "genre": "string",
  "authors": ["string"],
  "pageNumber": integer,
  "isbn": "string"
}]
