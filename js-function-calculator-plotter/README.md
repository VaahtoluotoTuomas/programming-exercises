# Function Plotter & Calculator

A web-based tool for calculating and visualizing mathematical functions. This JavaScript application takes a user-defined mathematical formula, evaluates it over a specified range, and generates both text-based results and graphical plots.

## Features

* **Custom Function Evaluation:** Users can input standard JavaScript math functions (e.g., `sin(x)`, `x*x + 2`).
* **Adjustable Parameters:** Define the starting point (`xStart`), ending point (`xEnd`), step size (`xStep`), and Y-axis scaling (`yScale`).
* **Text Output:** Generates a list of calculated $f(x)$ values for each step.
* **Dual Visualization:**
  * **Chart.js Integration:** Renders a responsive, interactive line chart of the function.
  * **HTML5 Canvas:** Draws a custom 2D graph directly on a canvas element, complete with a center axis.
* **Error Handling:** Validates user inputs and catches syntax errors in the provided formula to prevent crashes.

## Dependencies

* **Chart.js:** Required for the interactive chart rendering. Ensure Chart.js is included in your HTML file.
* **HTML Structure:** The script expects specific DOM elements (e.g., inputs like `xStart`, `xEnd`, and canvases like `myChart` and `myCanvas`).

## Note on Security
This application uses `eval()` to parse user-provided mathematical functions. While perfectly fine for local or personal use, be cautious if adapting this for a public-facing backend application.