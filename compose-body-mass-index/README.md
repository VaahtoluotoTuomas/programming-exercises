# Jetpack Compose BMI Calculator

A simple Android application built with Kotlin and Jetpack Compose for calculating Body Mass Index (BMI). 

## Features

* **Modern BMI Formula:** This app uses the updated formula proposed by Oxford University mathematicians ($1.3 \times \frac{\text{weight}}{\text{height}^{2.5}}$) for a more accurate representation of body mass scaling.
* **Input Validation:** Safely handles user inputs, automatically converting comma decimal separators to dots to prevent application crashes.
* **Age Verification:** Includes a checkbox to verify that the user is an adult, as standard BMI calculations are not directly applicable to growing children.
* **Responsive UI:** Built entirely with Jetpack Compose using standard Material Design 3 components (`OutlinedTextField`, `Checkbox`, `Scaffold`).

## Technologies Used
* Kotlin
* Jetpack Compose
* Material Design 3