
let title = document.getElementById('title');
let quiz = document.getElementById('quiz');
let resultOutput = document.getElementById('result');

try {
  let quizJSON = await fetch('/questions.json');
  if (!quizJSON.ok) {
    throw new Error(`HTTP-error: ${quizJSON.status}`);
  }

  let quizData = await quizJSON.json();

  if (!quizData.questions || !Array.isArray(quizData.questions)) {
    throw new Error("The loaded JSON doesn't contain the question in the right format.");
  }

  title.innerHTML = `<h1>${quizData.quizName}</h1>`;

  let htmlContent = `<form id="quizForm">`;

  quizData.questions.forEach((item, i) => {
      htmlContent += `<div class="question-container"><h3>Question ${i + 1}</h3>${item.question}<br>`
      item.options.forEach((option, j) => {
        htmlContent += `<br>
        <input type="radio" id="option${i}_${j}" name="question${i}" value="${j}">
        <label for="option${i}_${j}">${option}</label>`;
      })
      htmlContent += `</div>`
  });

  htmlContent += `<button type="submit">Submit answers</button>
    <button type="button" id="restart">Restart</button></form>`;
  quiz.innerHTML = htmlContent;

  const form = document.getElementById('quizForm');

  form.addEventListener('submit', (e) => {
    e.preventDefault();
    let howManyCorrect = 0;
    let howManyWrong = 0;
    let howManyQuestions = quizData.questions.length;

    const data = new FormData(form);

    for (let i = 0; i < quizData.questions.length; i++) {
      const item = quizData.questions[i];
      const kayttajanValinta = data.get(`question${i}`);

      if (kayttajanValinta === null) {
        resultOutput.innerHTML = `<h2><span style="color:red;">The answer to the question ${i + 1} is missing.</span></h2>`;
        return;
      }
      
      if (parseInt(kayttajanValinta) === parseInt(item.correctIndex)){
        howManyCorrect++;
      } else {
        howManyWrong++;
      } 
    };
    let htmlResult = `<h2><b>Result: </b>You got ${howManyCorrect} correct and ${howManyWrong} wrong out of ${howManyQuestions}.</h2>`;
    resultOutput.innerHTML = htmlResult;
  })

  const restartButton = document.getElementById('restart');

  restartButton.addEventListener('click', () => {
    form.reset();
    resultOutput.innerHTML = "";
  });

} catch (error) {
  console.error("Loading the questions failed:", error);
  quiz.innerHTML = `<h2>Oh noes, we couldn't load the questions :(</h2>`;
}
