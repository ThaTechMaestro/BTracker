# How to run

- Ensure you have docker Running
- Clone the repo
- Switch to Branch: Abubakar_Bayz_Tracker
- Run **command:** cd BayzTracker
- Run **command:** mvn package
- Run **command:** docker-compose up
- Endpoints can be accessed via BASE_URL: http://localhost:8080
- Accessing BASE_URL(http://localhost:8080) directly on the browser, displays a simple login form
- API Collections can be found in the **PostmanCollection Folder**

- Auth Credentials(Basic Auth):
- AdminUser -> **Username:** Admin | **Password:** admin
- User -> **Username:** User | **Password:** user


# Design Decisions
- Database Modelling via well-defined entities, having separate tables for each entity, keeping them flexible
- Lists are fetched with Pagination for well-defined structure and clean display
- Authentication and Authorization implemented with Spring Security
- Notifications are sent using parallel streams to improve speed and aid scalability
- Well defined responses on implemented endpoints
- Addition of tests covering service validity and handled exceptions
