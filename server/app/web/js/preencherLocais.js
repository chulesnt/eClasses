function preencherSelectUf(doc) {
  for (let i = selUf.options.length - 1; i >= 0; i--) {
    selUf.remove(i);
  }
  var lineItems = doc.getElementsByTagName("uf");
  for (i = 0; i < (lineItems.length); i++) {
    var editar = document.createElement("option");
    editar.id = "id-uf-"+lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
    editar.innerHTML = lineItems[i].getElementsByTagName("sigla")[0].childNodes[0].nodeValue;
    editar.value = lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
    selUf.appendChild(editar);
  }
}

function preencherSelectMunicipio(doc) {
  for (let i = selMunicipio.options.length - 1; i >= 0; i--) {
    selMunicipio.remove(i);
  }
  var lineItems = doc.getElementsByTagName("municipio");
  for (i = 0; i < (lineItems.length); i++) {
    var editar = document.createElement("option");
    editar.id = "id-municipio-"+lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
    editar.innerHTML = lineItems[i].getElementsByTagName("nome")[0].childNodes[0].nodeValue;
    editar.value = lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
    selMunicipio.appendChild(editar);
  }
}

function preencherSelectMateria(doc) {
  for (let i = selMateria.options.length - 1; i >= 0; i--) {
    selMateria.remove(i);
  }
  var lineItems = doc.getElementsByTagName("materia");
  for (i = 0; i < (lineItems.length); i++) {
    if (lineItems[i].getElementsByTagName("nome")[0].childNodes[0].nodeValue != "Outro") {
      var editar = document.createElement("option");
      editar.innerHTML = lineItems[i].getElementsByTagName("nome")[0].childNodes[0].nodeValue;
      editar.value = lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
      selMateria.appendChild(editar);
    }
  }
}

function preencherPreferenciaLocal(doc) {
  for (let i = localidadeAlt.options.length - 1; i >= 0; i--) {
    localidadeAlt.remove(i);
  }
  var lineItems = doc.getElementsByTagName("pref");
  for (i = 0; i < (lineItems.length); i++) {
    var editar = document.createElement("option");
    editar.id = "id-pref-local-"+lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
    editar.innerHTML = lineItems[i].getElementsByTagName("descricao")[0].childNodes[0].nodeValue;
    editar.value = lineItems[i].getElementsByTagName("id")[0].childNodes[0].nodeValue;
    localidadeAlt.appendChild(editar);
  }
}
