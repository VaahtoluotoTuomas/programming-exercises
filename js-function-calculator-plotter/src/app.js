const calcBtn = document.getElementById('calcBtn');
const errorMessageDiv = document.getElementById('error-message');
const textOutputDiv = document.getElementById('text-output');

let myChart = null;

calcBtn.addEventListener('click', function() {
    errorMessageDiv.textContent = '';
    textOutputDiv.textContent = '';

    const funcStr = document.getElementById('funcInput').value.trim();
    const xStart = parseFloat(document.getElementById('xStart').value);
    const xEnd = parseFloat(document.getElementById('xEnd').value);
    const xStep = parseFloat(document.getElementById('xStep').value);
    const yScale = parseFloat(document.getElementById('yScale').value);

    if (funcStr === '') {
        showError("Please enter a function formula.");
        return;
    }

    if (isNaN(xStart) || isNaN(xEnd) || isNaN(xStep) || isNaN(yScale)) {
        showError("All numerical fields must contain a valid numbers.");
        return;
    }

    if (xStart >= xEnd) {
        showError("Start of x must be less than end of x.");
        return
    }

    if (xStep <= 0) {
        showError("x step must be greater than 0.");
        return;
    }

    let mathFunc;
    try {
        eval(`mathFunc = function(x) {with(Math) { return ${funcStr}; } }`);

        mathFunc(xStart);
    } catch (error) {
        showError("Invalid function syntax!");
        return
    }

    const xValues = [];
    const yValues = [];
    let textOutputString = "";

    for (let x = xStart; x <= xEnd; x += xStep) {
        let currentX = Math.round(x * 100000) / 100000;

    try {
        let y = mathFunc(currentX);

        xValues.push(currentX);
        yValues.push(y);

        textOutputString += `f(${currentX}) = ${y}\n`;
    } catch (error) {
        showError("Error calculating value at x = " + currentX);
        return;
    }
}

    textOutputDiv.textContent = textOutputString;

    updateChart(xValues, yValues, funcStr);

    drawOnCanvas(xValues, yValues, xStart, xEnd, yScale);
});

function showError(message) {
    errorMessageDiv.textContent = message;
}

function updateChart(xValues, yValues, funcStr) {
    const ctx = document.getElementById('myChart').getContext('2d');

    if (myChart !== null) {
        myChart.data.labels = xValues;
        myChart.data.datasets[0].data = yValues;
        myChart.data.datasets[0].label = `f(x) = ${funcStr}`;
        myChart.update();
    } else {
        myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: xValues,
                datasets: [{
                    label: `f(x) = ${funcStr}`,
                    data: yValues,
                    borderColor: 'blue',
                    backgroundColor: 'rgba(0, 0, 255, 0.1)',
                    borderWidth: 2,
                    pointRadius: 3,
                    fill: false,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                scales: {
                    x: {
                        title: { display: true, text: 'x' }
                    },
                    y: {
                        title: { display: true, text: 'f(x)' }
                    }
                }
            }
        });
    }
}

function drawOnCanvas(xValues, yValues, xStart, xEnd, yScale) {
    const canvas = document.getElementById('myCanvas');
    const ctx = canvas.getContext('2d');

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    const xScale = canvas.width / (xEnd - xStart);

    const centerY = canvas.height / 2;

    ctx.beginPath();
    ctx.strokeStyle = '#dddddd';
    ctx.moveTo(0, centerY);
    ctx.lineTo(canvas.width, centerY);
    ctx.stroke();

    ctx.beginPath();
    ctx.strokeStyle = '#ff0000';
    ctx.lineWidth = 2;

    for (let i = 0; i < xValues.length; i++) {
        
        let canvasX = (xValues[i] - xStart) * xScale;

        let canvasY = centerY - (yValues[i] * yScale);

        if (i === 0) {
            ctx.moveTo(canvasX, canvasY);
        } else {
            ctx.lineTo(canvasX, canvasY);
        }
    }

    ctx.stroke();

}