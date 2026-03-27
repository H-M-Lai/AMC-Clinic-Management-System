# AMC Clinic Management System

A Java Swing desktop application for managing a small medical centre. The system supports multiple user roles and core clinic workflows such as appointment scheduling, payment handling, feedback review, and report generation.

## Features

- Role-based access for customer, doctor, staff, manager, and super manager
- Appointment booking, viewing, editing, and cancellation
- Customer registration and profile management
- Payment recording and invoice generation
- Feedback and comment management
- PDF report generation for appointments, payments, and feedback
- Dark mode support
- Local file-based storage using text files

## Tech Stack

- Java 24
- Java Swing
- NetBeans / Ant project structure
- PDFBox
- JFreeChart
- LGoodDatePicker

## Project Structure

- `src/` - Java source code
- `data/` - text-based data files
- `lib/` - third-party libraries used by the project
- `nbproject/` - NetBeans project configuration
- `reports/` - generated reports at runtime
- `invoices/` - generated invoices at runtime
- `profilePic/` - local profile image files

## How to Run

### Option 1: NetBeans

1. Open the project in NetBeans.
2. Make sure JDK 24 is selected.
3. Run the main class `javaproj.GUI.Credentials.LoginFrame`.

### Option 2: Ant

If Ant is installed and available on your PATH:

```bash
ant run
```

## Sample Data Setup

This public repository does not include personal-looking demo records, generated PDFs, or profile photos.

- Excluded runtime data files are documented in [`data/README.md`](data/README.md).
- If you want to test the full workflow, create the missing data files with header rows only, then add your own dummy records.
- Keep all sample data fictional before publishing or sharing the project.

## Notes

- The project uses local text files instead of a database.
- Some lookup files such as specialties and service types are included.
- `reports/`, `invoices/`, and `profilePic/` are ignored because they are runtime-generated or local assets.
