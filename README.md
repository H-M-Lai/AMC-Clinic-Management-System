# AMC Clinic Management System

A Java Swing desktop application for managing a small medical centre. The system supports different user roles and core clinic operations such as appointment booking, payment tracking, feedback management, and report generation.

## Features

- Role-based access for customer, doctor, staff, manager, and super manager
- Appointment booking, viewing, updating, and cancellation
- Customer registration and profile management
- Payment recording and invoice generation
- Feedback and comment management
- PDF report generation for appointments, payments, and feedback
- Dark mode support
- Local file-based data storage

## Tech Stack

- Java 24
- Java Swing
- NetBeans / Ant project structure
- PDFBox
- JFreeChart
- LGoodDatePicker

## Project Structure

- `src/` - Java source code
- `data/` - text-based data storage
- `lib/` - external libraries
- `reports/` - generated reports
- `invoices/` - generated invoices
- `profilePic/` - profile images

## How to Run

### Option 1: NetBeans
1. Open the project in NetBeans.
2. Make sure JDK 24 is selected.
3. Run the main class: `javaproj.GUI.Credentials.LoginFrame`

### Option 2: Ant
If Ant is installed:
```bash
ant run
