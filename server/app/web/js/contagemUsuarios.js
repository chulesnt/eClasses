function contarUsuarios() {
  let url = "http://localhost:8080/app/consultar/usuarios";
  fetch(url, { method: "POST",
    credentials: 'include'})
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
      let decoder = new TextDecoder("iso-8859-1");
      let text = decoder.decode(buffer);
      parser = new DOMParser();
      xmlDoc = parser.parseFromString(text, "text/xml");
      preencherHTML(xmlDoc);
    });
}

function preencherHTML(doc) {
	let pAlunos = document.querySelector("#numAlunos");
	let pProfs = document.querySelector("#numProfs");
	
	var lineItems = doc.getElementsByTagName("usuarios");
	
	pAlunos.innerHTML = (lineItems[0].getElementsByTagName("alunos")[0].childNodes[0].nodeValue).toString() + " alunos já cadastrados";
	pProfs.innerHTML = (lineItems[0].getElementsByTagName("professores")[0].childNodes[0].nodeValue).toString() + " professores já cadastrados";
}

contarUsuarios();

