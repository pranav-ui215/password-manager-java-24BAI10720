# password-manager-java-24BAI10720
A secure Java-based password manager with AES encryption and master password authentication for storing and managing credentials safely.

## Description

This project is a secure password manager developed in Java. It allows users to store, retrieve, update, and manage passwords for different websites using a command-line interface.

The system includes master password authentication and uses encryption to ensure that passwords are not stored in plain text.

## Problem Statement

Managing multiple passwords securely is a common challenge. Many users store passwords in plain text or reuse them across platforms, which is unsafe. This project provides a simple and secure solution using core Java concepts.

## Features

### Security

* Master password authentication
* Passwords stored in encrypted format (AES using Java Cryptography API)
* Prevents duplicate entries

### Password Management

*  Add new passwords
*  Retrieve stored passwords
*  Update existing passwords
*  File-based storage (no database required) 
*  View all saved websites
*  Search websites using keywords
*  Count total saved accounts
*  Generate strong random passwords

### Smart Validation

* Password strength detection (Weak / Medium / Strong)
* Weak passwords are rejected and must be re-entered

##  Technologies Used

* Java
* File Handling (BufferedReader, FileWriter)
* Exception Handling
* Java Cryptography API (AES)

## Relevance to Course Concepts

This project applies core concepts from the course:

* OOPs (class design, modular structure)
* File I/O operations
* Exception handling (try-catch blocks)
* Control structures and logic building

Additionally, encryption is implemented using Java's built-in libraries to enhance security.

## Project Structure

PasswordManager/
│
├── src/
│   └── com.passwordmanager/
│       ├── Main.java
│       └── PasswordManager.java
│
├── data/
│   ├── passwords.txt
│   └── master.txt
│
└── README.md

## Usage

### First Run:

* The program will prompt you to set a master password

### Subsequent Runs:

* Enter the master password to access the system

### Menu Options:

1. Add Password
2. Retrieve Password
3. Update Password
4. View All Sites
5. Search Site
6. Generate Password
7. Count Passwords
8. Exit

## Important Notes

* Ensure `master.txt` is empty before first run
* Passwords are stored in encrypted form
* This is a CLI-based application

## Limitations

* No graphical user interface
* Encryption key is fixed in code
* No password recovery mechanism


* Name: Pranav Shyam Nair
* Registration Number: 24BAI10720


