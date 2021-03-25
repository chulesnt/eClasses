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
    let url = "http://localhost:8080/app/aluno/editar?nome="+nomeAlt.value
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

function reqPreferencias() {
  let respostaPreferencias = document.querySelector("#respostaPreferencias");

  respostaPreferencias.classList.remove("red");
  respostaPreferencias.classList.remove("green");
  if (precoAlt.value == "" || precoAlt.value < 0) {
    respostaPreferencias.classList.add("red");
    respostaPreferencias.innerHTML = "O preço inserido é invalido";
    respostaPreferencias.classList.remove("displayNone");
  } else if (numeroAlunosAlt.value == "" || numeroAlunosAlt.value <= 0) {
    respostaPreferencias.classList.add("red");
    respostaPreferencias.innerHTML = "Esse número de alunos é invalido";
    respostaPreferencias.classList.remove("displayNone");
  }else {

    let url = "http://localhost:8080/app/aluno/materias?";
    for (let i = 0; i < listaIDM.length; i++) {
      if (i != listaIDM.length-1)
        url+= "materia"+(i+1)+"="+listaIDM[i]+"&";
      else
        url+= "materia"+(i+1)+"="+listaIDM[i];
    }
    fetch(url, { method: "POST", credentials: 'include' })
      .then(resposta => resposta.arrayBuffer())
      .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
      });

    url = "http://localhost:8080/app/aluno/editar?nome="
    +"&idUf="
    +"&idMunicipio="
    +"&preferenciaPreco="+precoAlt.value
    +"&preferenciaLocal="+localidadeAlt.value
    +"&preferenciaNumeroAlunos="+numeroAlunosAlt.value
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
          respostaPreferencias.classList.add("red");
          respostaPreferencias.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
          respostaPreferencias.classList.remove("displayNone");
        } else {
          var lineItems = xmlDoc.getElementsByTagName("sucesso");
          respostaPreferencias.classList.add("green");
          respostaPreferencias.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
          respostaPreferencias.classList.remove("displayNone");
          setTimeout(function(){
            consultarLogado();
            $('#modalPreferencias').modal('hide');
          }, 500);
        }
      });
    }
  setTimeout(function(){ respostaPreferencias.classList.add("displayNone");; }, 1500);
}

function add() {
  deletButtons = document.querySelectorAll(".delet");
  for (let i = 0; i < deletButtons.length; i++) {
    deletButtons[i].addEventListener("click",function(){ removeElement(event); }, false);
  }
}

function removeElement(event) {
  let index = listaIDM.indexOf(event.currentTarget.parentNode.id);
  listaIDM.splice(index, 1);
  let removivel = event.currentTarget.parentNode;
  event.currentTarget.parentNode.parentNode.removeChild(removivel);
}

function addMateria() {
  if (!listaIDM.includes(selMateria.value)) {
    listaIDM.push(selMateria.value);
    var button = document.createElement("button");
    button.classList.add("delet");
    button.classList.add("btn-outline-danger");
    button.innerHTML = "x";
    var editar = document.createElement("li");
    editar.classList.add("linha");
    editar.appendChild(button);
    editar.innerHTML += selMateria.options[selMateria.selectedIndex].text;
    exibirMaterias.appendChild(editar);
    deletButtons = document.querySelectorAll(".delet");
    add();
  }
}

function abrirPreferencias() {
  precoAlt.value = parseFloat(campoPreco.innerHTML.replace("R$ ", ""));
  numeroAlunosAlt.value = campoNumeroAlunos.innerHTML;
  carregarPreferenciaLocal();
  carregarMaterias();
  exibirMaterias.innerHTML = "";
  listaIDM.length = 0;
      setTimeout(function(){
        document.getElementById("id-pref-local-"+idPrefLocal).selected = true;
        for(let i = 0; i < selMateria.options.length; i++) {
          for(let j = 0; j < listaMaterias.length; j++) {
            if (selMateria.options[i].innerHTML == listaMaterias[j]) {
              listaIDM.push(selMateria.options[i].value);
              var button = document.createElement("button");
              button.classList.add("delet");
              button.classList.add("btn-outline-danger");
              button.innerHTML = "x";
              var editar = document.createElement("li");
              editar.classList.add("linha");
              editar.id = selMateria.options[i].value;
              editar.appendChild(button);
              editar.innerHTML += listaMaterias[j];
              exibirMaterias.appendChild(editar);
              deletButtons = document.querySelector(".delet");
            }
          }
        }
        add();
      }, 500);
}

const defaultPhotoUpload = document.querySelector("#default-photo-upload")
const uploadPreview = document.querySelector("#upload-preview")
const btnUpload = document.querySelector("#botaoUpload");
const idUpload = document.querySelector("#id-upload")
const resUpload = document.querySelector("#respostaUpload")

let photoPrev = ""

function defaultUploadActive(){
    defaultPhotoUpload.click()
}

btnUpload.addEventListener('click', (e) => {
    if(defaultPhotoUpload.files[0]){
        sendMultipartFormData({
            foto: defaultPhotoUpload.files[0],
            idAluno: idUpload.value
        })
    }
    else{
        torrada(resUpload, "Insira uma imagem primeiro", false)
    }
})

defaultPhotoUpload.addEventListener("change", function(){
  const file = this.files[0];
  if(file){
      if(file.type != "image/jpeg" && file.type != "image/jpg" && file.type != "image/png"){
          torrada(resUpload, "Só são aceitas imagens .png ou .jpg", false)
          defaultPhotoUpload.value = null
          uploadPreview.src = "";
          return;
      }
      if(file.size > 5000000){
          torrada(resUpload, "Imagem muito pesada", false)
          defaultPhotoUpload.value = null
          uploadPreview.src = "";
          return;
      }
      const reader = new FileReader();
      reader.onload = function(){
          const result = reader.result;
          uploadPreview.src = result;
          photoPrev = result
      }
      reader.readAsDataURL(file);
  }
});

function sendMultipartFormData(data){
  const XHR = new XMLHttpRequest()
  const FD  = new FormData();

  for(key in data) {
      FD.append(key, data[key]);
  }

  XHR.addEventListener('load', (e) => {
      if(e.target.status == 200){
        document.querySelector(".photo-container > img").src = photoPrev
        torrada(resUpload, "Foto alterada com sucesso!", true)
      }
      else torrada(resUpload, "Não foi possível alterar a foto", false)
  });

  XHR.addEventListener(' error', (e) => {
      torrada(resUpload, "Não foi possível alterar a foto", false)
  });

  XHR.open('POST', 'http://localhost:8080/app/aluno/foto/upload');
  XHR.send(FD);
}

let to;
function torrada(el, msg, suc) {
    clearTimeout(to);
    el.innerHTML = "<span class=\"" + (suc ? "sucesso" : "erro2") + "\">" + msg + "</span>";
    el.classList.remove("trans");
    to = setTimeout(function () {
        el.classList.add("trans");
    }, 3000);
}
