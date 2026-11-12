# Java Quiz Application

## Project Overview
The Java Quiz Application is a web-based quiz system developed using Java. It allows administrators to add questions through a website and students to attend quizzes and view their results.

## Technologies Used
- Java 23
- HTML
- CSS
- JavaScript
- VS Code
- HTTP Server

## Features
- Admin panel for adding questions
- Multiple-choice questions (MCQs)
- Four answer options
- Student quiz interface
- Question navigation
- 60-second timer for the entire quiz
- Automatic quiz submission when time expires
- Automatic score calculation
- Result display with correct and incorrect answers

## Project Files
- `QuizServer.java` – Java backend server and quiz operations.
- `index.html` – Frontend website.
- `Question.java` – Question model (if included).
- `Quiz.java` – Quiz management class (if included).
- `Main.java` – Main class (if included).

## How to Run

1. Install Java JDK 23.
2. Open the project folder in VS Code.
3. Open the terminal.
4. Compile the server:

```bash
javac QuizServer.java
```

5. Start the server:

```bash
java QuizServer
```

6. Open `index.html` using Live Server.

Backend server:

`http://localhost:8082`

## Learning Outcomes
- Developing web-based Java applications.
- Understanding client-server communication.
- Implementing multiple-choice quizzes.
- Handling user input.
- Implementing timers and automatic scoring.

## Conclusion
This project provides an interactive quiz platform with administrator question management, student participation and automatic result calculation.
