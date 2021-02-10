function prepararEdicaoPerfil() {
  nomeAlt.value = campoNome.innerHTML;
  carregarUf();
  setTimeout(function(){
    document.getElementById("id-uf-"+idUf).selected = true;
    carregarMunicipio();
  }, 300);
  setTimeout(function(){
    document.getElementById("id-municipio-"+idMunicipio).selected = true;
  }, 700);
}


function editarPerfil() {
  let respostaPerfil = document.querySelector("#respostaPerfil");

  respostaPerfil.classList.remove("red");
  respostaPerfil.classList.remove("green");
  if (nomeAlt.value == "") {
    respostaPerfil.classList.add("red");
    respostaPerfil.innerHTML = "O nome inserido é invalido";
    respostaPerfil.classList.remove("displayNone");
  } else {
    let url = "http://localhost:8080/app/aluno/editar?id="+idAluno
    +"&nome="+nomeAlt.value
    +"&idUf="+selUf.value
    +"&idMunicipio="+selMunicipio.value
    +"&preferenciaPreco="
    +"&preferenciaLocal="
    +"&preferenciaNumeroAlunos="
    +"&assinante="
    +"&dataFimAssinatura=";
    fetch(url, { method: "POST", credentials: 'include' })
      .then(resposta => resposta.arrayBuffer())
      .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        if (xmlDoc.getElementsByTagName("erro").length != 0) {
          var lineItems = xmlDoc.getElementsByTagName("erro");
          respostaPerfil.classList.add("red");
          respostaPerfil.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
          respostaPerfil.classList.remove("displayNone");
        } else {
          var lineItems = xmlDoc.getElementsByTagName("sucesso");
          respostaPerfil.classList.add("green");
          respostaPerfil.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
          respostaPerfil.classList.remove("displayNone");
          setTimeout(function(){
            consultarLogado();
            $('#modalAluno').modal('hide');
          }, 500);
        }
      });
  }
  setTimeout(function(){ respostaPerfil.classList.add("displayNone");; }, 1500);
}
