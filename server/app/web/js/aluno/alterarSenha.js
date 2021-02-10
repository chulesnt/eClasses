function alterarSenha() {
  let i1 = document.querySelector("#senha1");
  let i2 = document.querySelector("#senha2");
  let i3 = document.querySelector("#senha3");
  let senha1 = i1.value;
  let senha2 = i2.value;
  let senha3 = i3.value;
  let respostaSenha = document.querySelector("#respostaSenha");

  respostaSenha.classList.remove("red");
  respostaSenha.classList.remove("green");
  respostaSenha.classList.remove("btn-outline-danger");
  if (senha1 == "" || senha2 == "" || senha3 == "") {
    respostaSenha.classList.add("red");
    respostaSenha.innerHTML = "Preencha todos os campos!";
    respostaSenha.classList.remove("displayNone");
  } else if (senha2 != senha3) {
    i2.value = "";
    i3.value = "";
    respostaSenha.classList.add("red");
    respostaSenha.innerHTML = "As senhas estão diferentes!";
    respostaSenha.classList.remove("displayNone");
  } else {
    let url = "http://localhost:8080/app/aluno/senha?senhaAntiga="+senha1
    +"&senhaNova="+senha2;
    fetch(url, { method: "POST", credentials: 'include' })
      .then(resposta => resposta.arrayBuffer())
      .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        if (xmlDoc.getElementsByTagName("erro").length != 0) {
          i1.value = "";
          i2.value = "";
          i3.value = "";
          var lineItems = xmlDoc.getElementsByTagName("erro");
          respostaSenha.classList.add("red");
          respostaSenha.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
          respostaSenha.classList.remove("displayNone");
        } else {
          i1.value = "";
          i2.value = "";
          i3.value = "";
          var lineItems = xmlDoc.getElementsByTagName("sucesso");
          respostaSenha.classList.add("green");
          respostaSenha.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
          respostaSenha.classList.remove("displayNone");
        }
      });
  }
  setTimeout(function(){ respostaSenha.classList.add("displayNone");; }, 1500);

}
