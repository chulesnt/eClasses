function inserir() {
  let nome = document.querySelector("#nome-aluno").value;
  let email = document.querySelector("#email").value;
  let senha = document.querySelector("#senha").value;
  let senha2 = document.querySelector("#confirma-senha").value;
  let uf = selUf.value;
  let municipio = selMunicipio.value;

  if (nome == "" || email == "" || senha == "" || uf == "" || municipio == "") {
      gerarToastErro("Preencha todos os campos");
  } else if (senha != senha2) {
    gerarToastErro("As senhas estão diferentes");
  } else if (!email.includes("@")) {
    gerarToastErro("Email inválido");
  } else if (senha.length < 5 || senha.length > 15) {
    gerarToastErro("A senha deve ter entre 5 e 15 caracteres");
  } else {
    let url = "http://localhost:8080/app/aluno/cadastrar?email="+email
    +"&nome="+nome
    +"&senha="+senha
    +"&idUf="+uf
    +"&idMunicipio="+municipio;
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
