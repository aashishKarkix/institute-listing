# Listing Project

This is the Institute Listing project built with Spring Boot and PostgreSQL.  
It includes OAuth2 login with Google and a role-based user system.

## Features
- User registration via Google OAuth2
- Role-based access (USER, ADMIN, SUPER_ADMIN)
- PostgreSQL persistence
- Retry logic for service calls
- Global exception handling
- Security headers and CSRF protection

## Getting Started

1. Clone the repository

2. Configure your environment variables in a `.env` file.

3. Build and run the project:
   ./gradlew bootRun


## Database

The application uses PostgreSQL. Liquibase handles the database migrations.

## Security

- OAuth2 login with Google
- CSRF protection enabled
- Security headers configured for XSS, caching, and frame options

