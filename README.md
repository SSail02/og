# Smart Library Assistant Pro

Smart Library Assistant Pro is a Java Swing desktop microproject that demonstrates MVC architecture, file handling, JDBC, socket programming, multithreading, and object-oriented programming for a library management workflow.

## Project Structure

```
SmartLibrary/
├── controller/
├── database/
├── model/
├── network/
├── util/
├── view/
└── Main.java
```

## Features

- User registration and login with SHA-256 password hashing.
- Book add, view, issue, return, and search features.
- Due-date tracking and fine calculation.
- CSV file persistence for books and issue records.
- MySQL persistence using JDBC and prepared statements.
- Socket-based sharing of book records between systems.
- Background thread for overdue alerts.

## How to Run

1. Install Java 17+ and MySQL.
2. Create the database using `SmartLibrary/database/smart_library.sql`.
3. Update credentials in `SmartLibrary/database/DBConnection.java` if needed.
4. Compile the project:
   ```bash
   javac SmartLibrary/Main.java SmartLibrary/model/*.java SmartLibrary/controller/*.java SmartLibrary/database/*.java SmartLibrary/network/*.java SmartLibrary/util/*.java SmartLibrary/view/*.java
   ```
5. Run the project:
   ```bash
   java SmartLibrary.Main
   ```

## Sample Screens

- **Login Screen:** Username, password, and Login/Register buttons.
- **Dashboard:** Add Book, Issue Book, Return Book, Share Records.
- **Library Panel:** JTable with book list and Issue/Return/View Fine actions.

## Notes for Viva

- `LibraryOperations` demonstrates interfaces.
- `Person` and `Student` demonstrate inheritance.
- `AlertThread` demonstrates multithreading.
- `FileHandler` demonstrates file handling.
- `Client` and `Server` demonstrate networking.
- `DBConnection`, `AuthController`, and `LibraryController` demonstrate JDBC.
