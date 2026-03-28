# Taxi Fare Calculator

A multi-screen Android application built with Kotlin and Jetpack Compose that calculates taxi fares based on travel distance. 

## Features

* **Fare Calculation:** Users can input the distance of their journey to instantly calculate the total fare based on a base fee and a per-kilometer rate.
* **Customizable Rates:** Includes a dedicated settings screen (accessible via the top app bar) where users can modify the starting fee and the kilometer rate.
* **Persistent Data:** Utilizes Android's `SharedPreferences` to save the customized rates, ensuring they remain saved even after the app is closed and reopened.
* **Input Validation:** Safely handles numeric inputs, automatically converting commas to dots and providing clear error states if invalid data is entered.

## Technologies Used
* Kotlin
* Jetpack Compose (UI & State Management)
* Material Design 3 Components (Scaffold, TopAppBar, FloatingActionButton)
* SharedPreferences