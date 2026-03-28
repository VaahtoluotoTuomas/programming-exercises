# Dynamic JSON Quiz App

A lightweight, vanilla JavaScript quiz application that asynchronously loads questions from a local JSON file, dynamically builds an interactive form, and evaluates the user's answers.

## Features

* **Dynamic Data Fetching:** Automatically retrieves quiz content (title, questions, and options) from a `questions.json` file using the Fetch API.
* **Auto-Generated UI:** Constructs the quiz interface dynamically, creating radio button inputs for each available option.
* **Input Validation:** Prevents submission and alerts the user if any question is left unanswered.
* **Instant Scoring:** Calculates and displays the total number of correct and incorrect answers immediately upon submission.
* **Quick Restart:** Includes a reset button to clear all selections and hide previous results without needing to reload the page.
* **Robust Error Handling:** Catches HTTP errors and invalid JSON structures, displaying a friendly fallback message if the quiz fails to load.

## Setup Requirements

To run this application, ensure you have a valid `questions.json` file in the root directory. The HTML file must also contain elements with the following IDs: `title`, `quiz`, and `result`.