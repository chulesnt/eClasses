let showingId = 0, cargo = "";

function openMensagens(id){
    showingId = id;
    closeChats();
    document.querySelector(".msg-background").style.display = '';
    document.querySelector(".msg-container").style.display = '';
    let url = "http://localhost:8080/app/consultar/logado";
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        cargo = xmlDoc.querySelector("cargo").textContent;
        if(xmlDoc.querySelector("cargo").textContent == "ALUNO"){
            loadMensagens(xmlDoc.querySelector("id").textContent, id, "ALUNO");
            updateContatoProf(id);
        }
        if(xmlDoc.querySelector("cargo").textContent == "PROFESSOR"){
            loadMensagens(id, xmlDoc.querySelector("id").textContent, "PROFESSOR");
            updateContatoAluno(id)
        }
    });
}

function closeMensagens(){
    document.querySelector(".msg-background").style.display = 'none';
    document.querySelector(".msg-container").style.display = 'none';
}

function loadMensagens(idAluno, idProf, cargo){
    let url = "http://localhost:8080/app/mensagens/exibir?idAluno=" + idAluno + "&idProf=" + idProf;
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        criaMensagensDivs(xmlDoc.querySelectorAll("mensagem"), cargo);
    });
}

function criaMensagensDivs(xml, cargo){
    let d = document.querySelector(".msg-content");
    let str = "";
    for(let i = 0; i < xml.length; i++){
        if(xml[i].querySelector("alunoEnviou").textContent == "true" && cargo == "ALUNO" || xml[i].querySelector("alunoEnviou").textContent == "false" && cargo == "PROFESSOR") str += `<div class="msg-sent">` + xml[i].querySelector("texto").textContent + `</div>`;
        else str += `<div class="msg-received">` + xml[i].querySelector("texto").textContent + `</div>`
    }
    d.innerHTML = str;
}

function updateContatoProf(id){
    let url = "http://localhost:8080/app/professor/consultar?id=" + id;
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        let f = document.querySelector(".msg-photo");
        f.style.display = '';
        let n = document.querySelector(".msg-header > h4");
        if(xmlDoc.querySelector("foto").textContent != "null"){
            f.innerHTML = `<img src="foto/consultar?idProf=` + xmlDoc.querySelector("id").textContent + `">`;
        } else f.innerHTML = `<img src="img/empty-profile.png">`;
        n.innerHTML = xmlDoc.querySelector("nome").textContent;
    });
}

function updateContatoAluno(id){
    let url = "http://localhost:8080/app/aluno/consultar?id=" + id;
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        let f = document.querySelector(".msg-photo");
        f.style.display = 'none';
        let n = document.querySelector(".msg-header > h4");
        n.innerHTML = xmlDoc.querySelector("nome").textContent;
    });
}

function enviarMensagem(){
    let i = document.querySelector(".msg-input");
    let str = "";
    if(cargo == "ALUNO") str = "&idProf=" + showingId;
    if(cargo == "PROFESSOR") str = "&idAluno=" + showingId;
    let url = "http://localhost:8080/app/mensagens/enviar?texto=" + i.value + "&data=" + formatDate(new Date()) + str;
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        let d = document.querySelector(".msg-content");
        d.innerHTML += `<div class="msg-sent">` + i.value + `</div>`;
        i.value = "";
    });
}

function openChats(cargo){
    document.querySelector(".msg-container").style.display = 'none';
    document.querySelector(".chat-container").style.display = '';
    document.querySelector(".msg-background").style.display = '';
    if(cargo.toLowerCase() == "aluno"){
        let url = "http://localhost:8080/app/chat/aluno";
        fetch(url, {  method: "POST", credentials: 'include' })
        .then(resposta => resposta.arrayBuffer())
        .then(buffer => {
            let decoder = new TextDecoder("iso-8859-1");
            let text = decoder.decode(buffer);
            parser = new DOMParser();
            xmlDoc = parser.parseFromString(text, "text/xml");
            let xml = xmlDoc.querySelectorAll("prof");
            console.log(xml);
            criaAlunoChatItems(xml)
        });
    }
    if(cargo.toLowerCase() == "professor"){
        let url = "http://localhost:8080/app/chat/professor";
        fetch(url, {  method: "POST", credentials: 'include' })
        .then(resposta => resposta.arrayBuffer())
        .then(buffer => {
            let decoder = new TextDecoder("iso-8859-1");
            let text = decoder.decode(buffer);
            parser = new DOMParser();
            xmlDoc = parser.parseFromString(text, "text/xml");
            let xml = xmlDoc.querySelectorAll("aluno");
            console.log(xml);
            criaProfChatItems(xml)
        });
    }
}

function criaAlunoChatItems(xml){
    let d = document.querySelector(".chat-container");
    let str = "<h2>Conversas</h2>";
    for(let i = 0; i < xml.length; i++){
        str += `<div class="chat-item" onclick="openMensagens(` + xml[i].querySelector("idProf").textContent + `)">`
        str += `<div class='msg-photo'>`;
        if(xml[i].querySelector("foto").textContent != "null"){
            str += `<img src="foto/consultar?idProf=` + xml[i].querySelector("idProf").textContent + `">`;
        } else str += `<img src="img/empty-profile.png">`;
        str += `</div>`;
        str += `<h6>` + xml[i].querySelector("nome").textContent + `</h6>`;
        str += `</div>`;
    }
    d.innerHTML = str;
}

function criaProfChatItems(xml){
    let d = document.querySelector(".chat-container");
    let str = "<h2>Conversas</h2>";
    for(let i = 0; i < xml.length; i++){
        str += `<div class="chat-item" onclick="openMensagens(` + xml[i].querySelector("idAluno").textContent + `)">`
        str += `<h6>` + xml[i].querySelector("nome").textContent + `</h6>`;
        str += `</div>`;
    }
    d.innerHTML = str;
}

function closeChats(){
    document.querySelector(".chat-container").style.display = 'none';
}

function closeAll(){
    closeMensagens()
    closeChats()
}

function handleMensagemButtonClick(){
    let u = new URLSearchParams(location.search);
    let url = "http://localhost:8080/app/consultar/logado";
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        if(xmlDoc.querySelector("cargo").textContent == "ALUNO") openMensagens(u.get("id"));
    });
}

function handleChatButtonClick(){
    let url = "http://localhost:8080/app/consultar/logado";
    fetch(url, {  method: "POST", credentials: 'include' })
    .then(resposta => resposta.arrayBuffer())
    .then(buffer => {
        let decoder = new TextDecoder("iso-8859-1");
        let text = decoder.decode(buffer);
        parser = new DOMParser();
        xmlDoc = parser.parseFromString(text, "text/xml");
        openChats(xmlDoc.querySelector("cargo").textContent);
    });
}

function formatDate(d){
    let str = "";
    str += d.getFullYear() + "-";
    str += (d.getMonth() + 1) + "-";
    str += d.getDate() + "%20";
    str += d.getHours() + ":";
    str += d.getMinutes() + ":";
    str += d.getSeconds();
    return str;
}