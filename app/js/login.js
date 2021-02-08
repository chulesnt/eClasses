function loginProf() {
  let email = document.querySelector("#email-prof");
  let senha = document.querySelector("#senha-prof");
  if (email.value == "" || senha.value == "") {
    gerarToastErro("Preencha todos os campos");
  } else {
    let url = "http://localhost:8080/app/professor/logar?email="+email.value
    +"&senha="+senha.value;
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
      let decoder = new TextDecoder("iso-8859-1");
      let text = decoder.decode(buffer);
      parser = new DOMParser();
      xmlDoc = parser.parseFromString(text, "text/xml");
      produzirToastServer(xmlDoc);
      limpaInputs();
    });
  }
}

function loginAluno() {
  let email = document.querySelector("#email-aluno");
  let senha = document.querySelector("#senha-aluno");
  if (email.value == "" || senha.value == "") {
    gerarToastErro("Preencha todos os campos");
  } else {
    let url = "http://localhost:8080/app/aluno/logar?email="+email.value
    +"&senha="+senha.value;
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
      let decoder = new TextDecoder("iso-8859-1");
      let text = decoder.decode(buffer);
      parser = new DOMParser();
      xmlDoc = parser.parseFromString(text, "text/xml");
      produzirToastServer(xmlDoc);
      limpaInputs();
    });
  }
}
