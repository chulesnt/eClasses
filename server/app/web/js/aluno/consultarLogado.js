function consultarLogado() {
  let allDados = document.querySelectorAll(".deletavelDados");
  let allPrefs = document.querySelectorAll(".deletavelPreferencias");
  campoPreco.innerHTML = "R$ ";

  for (let i = 0; i < allDados.length; i++) {
    listaDados.removeChild(allDados[i]);
  }

  for (let i = 0; i < allPrefs.length; i++) {
    listaPreferencias.removeChild(allPrefs[i]);
  }

  let url = "http://localhost:8080/app/consultar/logado";
  fetch(url, { method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
      let decoder = new TextDecoder("iso-8859-1");
      let text = decoder.decode(buffer);
      parser = new DOMParser();
      xmlDoc = parser.parseFromString(text, "text/xml");
      var cargo = xmlDoc.getElementsByTagName("usuario")[0].getElementsByTagName("cargo")[0].childNodes[0].nodeValue;
      idAluno = xmlDoc.getElementsByTagName("usuario")[0].getElementsByTagName("id")[0].childNodes[0].nodeValue;

      if (cargo != "ALUNO") {
        location.href = "login.html";
      } else {

        url = "http://localhost:8080/app/aluno/consultar?id="+idAluno;
        fetch(url, { method: "POST", credentials: 'include' })
          .then(resposta => resposta.arrayBuffer())
          .then(buffer => {
            let decoder = new TextDecoder("iso-8859-1");
            let text = decoder.decode(buffer);
            parser = new DOMParser();
            xmlDoc = parser.parseFromString(text, "text/xml");
            var aluno = xmlDoc.getElementsByTagName("aluno")[0];
            campoNome.innerHTML = aluno.getElementsByTagName("nome")[0].childNodes[0].nodeValue;
            campoEmail.innerHTML = aluno.getElementsByTagName("email")[0].childNodes[0].nodeValue;
            if (aluno.getElementsByTagName("assinante")[0].childNodes[0].nodeValue == "true") {
              campoAssinante.innerHTML = "Sim";
              let dt = document.createElement("dt");
              dt.classList.add("col-6");
              dt.classList.add("deletavelDados");
              dt.innerHTML = "Data do fim da assinatura";
              listaDados.appendChild(dt);
              let dd = document.createElement("dd");
              dd.classList.add("deletavelDados");
              dd.classList.add("col-6");
              dd.innerHTML = conversionData1(aluno.getElementsByTagName("data-fim-assinatura")[0].childNodes[0].nodeValue);
              listaDados.appendChild(dd);
            } else
              campoAssinante.innerHTML = "Não";
            idUf = aluno.getElementsByTagName("id-uf")[0].childNodes[0].nodeValue;
            idMunicipio = aluno.getElementsByTagName("id-municipio")[0].childNodes[0].nodeValue;

            url = "http://localhost:8080/app/consultar/uf?id="+idUf;
            fetch(url, { method: "POST", credentials: 'include' })
              .then(resposta => resposta.arrayBuffer())
              .then(buffer => {
                let decoder = new TextDecoder("iso-8859-1");
                let text = decoder.decode(buffer);
                parser = new DOMParser();
                xmlDoc = parser.parseFromString(text, "text/xml");
                campoUf.innerHTML = xmlDoc.getElementsByTagName("uf")[0].getElementsByTagName("sigla")[0].childNodes[0].nodeValue;
            });

            url = "http://localhost:8080/app/consultar/municipio?id="+idMunicipio;
            fetch(url, { method: "POST", credentials: 'include' })
              .then(resposta => resposta.arrayBuffer())
              .then(buffer => {
                let decoder = new TextDecoder("iso-8859-1");
                let text = decoder.decode(buffer);
                parser = new DOMParser();
                xmlDoc = parser.parseFromString(text, "text/xml");
                campoMunicipio.innerHTML = xmlDoc.getElementsByTagName("municipio")[0].getElementsByTagName("nome")[0].childNodes[0].nodeValue;
            });

            let preco = aluno.getElementsByTagName("preferencia-preco")[0].childNodes[0].nodeValue;
            let precoParsed = parseFloat(preco);
            precoParsed = precoParsed.toFixed(2);
            campoPreco.innerHTML += convertComma(precoParsed.toString());
            campoNumeroAlunos.innerHTML = aluno.getElementsByTagName("preferencia-numero-alunos")[0].childNodes[0].nodeValue;

            let idPrefLocal = aluno.getElementsByTagName("id-preferencia-local")[0].childNodes[0].nodeValue;
            url = "http://localhost:8080/app/consultar/preferencialocal?id="+idPrefLocal;
            fetch(url, { method: "POST", credentials: 'include' })
              .then(resposta => resposta.arrayBuffer())
              .then(buffer => {
                let decoder = new TextDecoder("iso-8859-1");
                let text = decoder.decode(buffer);
                parser = new DOMParser();
                xmlDoc = parser.parseFromString(text, "text/xml");
                campoLocalidade.innerHTML = xmlDoc.getElementsByTagName("preferencia-localizacao")[0].getElementsByTagName("descricao")[0].childNodes[0].nodeValue;
            });

            let lineMaterias = aluno.getElementsByTagName("id-materia");
            for (let i = 0; i < lineMaterias.length; i++) {
              let nomeMateria;

              url = "http://localhost:8080/app/consultar/materia?id="+lineMaterias[i].childNodes[0].nodeValue;
              fetch(url, { method: "POST", credentials: 'include' })
                .then(resposta => resposta.arrayBuffer())
                .then(buffer => {
                  let decoder = new TextDecoder("iso-8859-1");
                  let text = decoder.decode(buffer);
                  parser = new DOMParser();
                  xmlDoc = parser.parseFromString(text, "text/xml");
                  nomeMateria = xmlDoc.getElementsByTagName("materia")[0].getElementsByTagName("nome")[0].childNodes[0].nodeValue;
                  let dd = document.createElement("dd");
                  dd.classList.add("col-12");
                  dd.classList.add("deletavelPreferencias");
                  dd.classList.add("materias");
                  dd.innerHTML = nomeMateria;
                  listaPreferencias.appendChild(dd);
              });
            }

          });
      }
  });
}
