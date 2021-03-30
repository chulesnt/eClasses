let headerProf = document.querySelector("#headerProf");
let headerAluno = document.querySelector("#headerAluno");

const w = 22; // Largura da estrela
const aj = 1 / 5;
const me = 4; // Margem entre estrelas
const dpfp = "img/empty-profile.png";
const sc = document.querySelector("#seuCom");
const pg = document.querySelector("#pg");
const btnConv = document.querySelector("#conversar");
const forms = document.querySelectorAll("#modalProf input:not(#tf),#modalProf select,#modalProf textarea");
const btnEd = document.querySelector("#botaoEditar");
const ts = document.querySelector("#respostaPerfil");
const tc = document.querySelector("#respCom");
const inMin = document.querySelector("#numAMin");
const inMax = document.querySelector("#numAMax");
const inVideo = document.querySelector("#link");
const plink = document.querySelector("#linkv");
const ew = document.querySelector("#ew");

const photoContainer = document.querySelector(".photo-container");
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
            idProf: idUpload.value
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

    XHR.open('POST', 'http://localhost:8080/app/foto/upload');
    XHR.send(FD);
}

let selUf = document.querySelector("#idUf");
let selMun = document.querySelector("#idMunicipio");
let prim = true;

let to;
function torrada(el, msg, suc) {
    clearTimeout(to);
    el.innerHTML = "<span class=\"" + (suc ? "sucesso" : "erro2") + "\">" + msg + "</span>";
    el.classList.remove("trans");
    to = setTimeout(function () {
        el.classList.add("trans");
    }, 3000);
}

function comida(url, sel, nome = "nome", id = "id", cb = function () { }) {
    fete(url, function (xml) {
        const v = xmlGetAll(xml, nome);
        const v2 = xmlGetAll(xml, id);
        sel.innerHTML = "";
        for (let i = 0; i < v.length; i++) {
            let o = document.createElement("option");
            o.textContent = v[i];
            o.setAttribute("value", v2[i]);
            sel.appendChild(o);
        }
        cb();
    });
}
function qs(id) {
    return document.querySelector("#" + id);
}
function atMun() {
    comida("http://localhost:8080/app/listar/municipio?uf=" + selUf.options[selUf.selectedIndex].textContent, selMun, "nome", "id", function () {
        if (prim) {
            prim = false;
            megafete();
        }
    });
}

comida("http://localhost:8080/app/listar/uf", selUf, "sigla", "id", function () {
    atMun();
    selUf.addEventListener("change", atMun);
});
comida("http://localhost:8080/app/listar/materia", qs("idMateria"));

btnEd.addEventListener("click", function () {
    if (inMax.value < inMin.value) {
        torrada(ts, "Erro: O número máximo de alunos é menor que o mínimo", false);
        return;
    }
    let str = "http://localhost:8080/app/professor/editar?";
    for (let i = 0; i < forms.length; i++) {
        if (forms[i].value == "") {
            torrada(ts, "Erro: Há campos não preenchidos", false);
            return;
        }
        if (i != 0) str += "&";
        str += forms[i].id + "=" + forms[i].value;
    }
    str += "&titulo=&premium=&dataPremium=";
    console.log(str);
    fete(str, function () {
        torrada(ts, "Dados editados com sucesso", true);
    })
});

function xmlget(xml, nome) {
    return xml.querySelector(nome).textContent;
}
function xmlGetAll(xml, nome) {
    let v = xml.querySelectorAll(nome);
    let ret = [];
    for (let i = 0; i < v.length; i++) {
        ret[i] = v[i].textContent;
    }
    return ret;
}
function setInput(nome, val) {
    let el = document.querySelector("[data-rec='" + nome + "']");
    el.value = val;
}
function setInput2(xml, nome) {
    setInput(nome, xmlget(xml, nome));
}
function htmlset(xml, nome, id = nome, inp = false) {
    let ret = xmlget(xml, nome);
    document.querySelector("#" + id).textContent = ret;
    if (inp) setInput(nome, ret);
}
function pau() {
    pg.src = "img/ha.png";
    document.querySelector("#cont").classList.add("erro");
    document.querySelector("#nome").textContent = "Professor não encontrado";
    document.querySelector("#descricao-apresentacao").innerHTML = "Não era para você estar vendo isso. Mas já que você está, você pode tentar <a href='feed.html'>procurar um outro professor</a>.";
}

function fete(url, f) {
    fetch(url, { method: "POST", credentials: 'include' })
        .then(re => re.arrayBuffer())
        .then(buffer => {
            let decoder = new TextDecoder("iso-8859-1");
            let text = decoder.decode(buffer);
            parser = new DOMParser();
            xml = parser.parseFromString(text, "text/xml");
            f(xml);
        });
}

function zeroAEsquerda(x) {
    return (x < 10 ? "0" : "") + x;
}
function dateFormat(dt) {
    return zeroAEsquerda(dt.getDate()) + "/" + zeroAEsquerda(dt.getMonth() + 1) + "/" + dt.getFullYear();
}
function parsePedro(dt) {
    return dt.getFullYear() + "-" +
        zeroAEsquerda(dt.getMonth() + 1) + "-" +
        zeroAEsquerda(dt.getDate()) + " " +
        zeroAEsquerda(dt.getHours()) + ":" +
        zeroAEsquerda(dt.getMinutes()) + ":" +
        zeroAEsquerda(dt.getSeconds());
}

function megafete() {
    fete("http://localhost:8080/app/consultar/logado", function (xml) {
        let idprof = null;
        let URL = new URLSearchParams(location.search);
        if (URL.get("id") != null) {
            idprof = URL.get("id");
        }

        let idu, cargou;
        let erro = xml.querySelector("erro");
        if (erro != null && erro.querySelector("mensagem").textContent == "Nenhum usuário logado") {
            idu = null;
            cargou = null;
        } else {
            idu = xmlget(xml, "id");
            cargou = xmlget(xml, "cargo");
            if (cargou == "PROFESSOR" && idprof == null)
                idprof = idu;
        }
        if (idprof == null) {
            pau();
            return;
        }

        if (cargou == null) {
            btnConv.classList.add("hidden");
            document.querySelector("#faca-login-conv").classList.remove("hidden");
        }
        if (cargou == "PROFESSOR" && idprof == idu) {
            headerAluno.classList.add("none");
            btnConv.textContent = "Editar perfil";
            btnConv.addEventListener("click", function () {
                $('#modalProf').modal("toggle");
            });
            photoContainer.addEventListener('click', (e) => {
                $('#modalPhoto').modal('toggle')
            })
            idUpload.value = idprof
        }
        else {
            photoContainer.classList.add('not-hoverable')
            document.querySelector(".photo-text").style.display = 'none'
            document.querySelector(".photo-text-container").style.display = 'none'
            headerProf.classList.add("none");
            btnConv.addEventListener("click", function () {
            });
        }

        fete("http://localhost:8080/app/professor/consultar?id=" + idprof, function (xml) {
            let e = xml.querySelector("erro");
            if (e != null) {
                pau();
                return;
            }

            let ph = +xmlget(xml, "preco-hora");
            let eph = document.querySelector("#preco-hora");
            let dec = Math.round((ph % 1) * 100);
            let st = Math.floor(ph) + "," + zeroAEsquerda(dec);
            eph.textContent = st;
            setInput2(xml, "preco-hora");

            htmlset(xml, "descricao-apresentacao", undefined, true);
            htmlset(xml, "nome", undefined, true);
            setInput2(xml, "numero-alunos-min");
            setInput2(xml, "numero-alunos-max");

            let nota = +xmlget(xml, "avaliacao");
            let nav = +xmlget(xml, "numero-avaliacoes");
            let ap = document.querySelector("#avProf");
            makeAv(ap, nota, true, nav);

            let idm = xmlget(xml, "id-municipio");
            setInput("id-municipio", idm);
            fete("http://localhost:8080/app/consultar/municipio?id=" + idm, function (xml) {
                htmlset(xml, "nome", "nome-mun");
                htmlset(xml, "uf");
            });

            let idma = xmlget(xml, "id-materia");
            fete("http://localhost:8080/app/consultar/materia?id=" + idma, function (xml) {
                htmlset(xml, "nome", "nome-mat");
            });

            let ft = xmlget(xml, "foto");
            if (ft == "null") {
                pg.src = dpfp;
            }
            else {
                pg.src = "foto/consultar?idProf=" + idprof;
            }

            let xlink=xmlget(xml,"link");
            //console.log(xlink);
            if(xlink=="null" || xlink==""){
                link.value="";
            }
            else{
                link.value=xlink;
                let ok=true,https=false;
                if(xlink.startsWith("http")){
                    let ind=4;
                    if(xlink[ind]=="s"){
                        ind++;
                        https=true;
                    }
                    if(xlink[ind]==":" && xlink[ind+1]=="/" && xlink[ind+2]=="/"){
                        ok=false;
                    }
                }
                if(ok){
                    xlink="http://"+xlink;
                }

                let ind2=https? 8: 7;
                if(xlink.substring(ind2,ind2+4)=="www."){
                    ind2+=4;
                }
                //console.log(xlink+" "+xlink.substring(ind2,ind2+18));
                let ok2=true;
                if(xlink.substring(ind2,ind2+12)=="youtube.com/"){
                    ind2+=12;
                    if(xlink.substring(ind2,ind2+8)=="watch?v="){
                        let fim=xlink.lastIndexOf("&");
                        if(fim==-1) fim=xlink.length;
                        xlink=xlink.substring(0,ind2)+"embed/"+xlink.substring(ind2+8,fim);
                        ok2=false;
                    }
                    else if(xlink.substring(ind2,ind2+6)=="embed/"){
                        ok2=false;
                    }
                }
                if(ok2){
                    const a=document.createElement("a");
                    a.textContent="Link do vídeo";
                    plink.appendChild(a);
                    plink.style.marginTop="8px";
                    a.href=xlink;
                }
                else{
                    const ifr=document.createElement("iframe");
                    ifr.src=xlink;
                    ifr.style.width="448px";
                    ifr.style.height="252px";
                    ifr.setAttribute("allow","accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture");
                    ifr.setAttribute("allowfullscreen","");
                    plink.appendChild(ifr);
                    plink.style.marginTop="15px";
                }
            }

            let xc = xml.querySelector("comentarios");
            let ida = xmlGetAll(xc, "id-aluno");
            let tx = xmlGetAll(xc, "texto");
            let dt = xmlGetAll(xc, "data");
            let podecom = cargou == "ALUNO";
            for (let i = 0; i < ida.length; i++) {
                let cond = +ida[i] == idu;
                if (cond) {
                    podecom = false;
                }
                fete("http://localhost:8080/app/aluno/consultar?id=" + ida[i], function (xml) {
                    let na = xmlget(xml, "nome");
                    fete("http://localhost:8080/app/consultar/alunoavaliaprof?idAluno=" + ida[i] + "&idProf=" + idprof, function (x2) {
                        let elava = x2.querySelector("avaliacao");
                        let ava = elava == null ? null : +elava.textContent;
                        fete("http://localhost:8080/app/aluno/consultar?id=" + ida[i], function(x3){
                            if(xmlget(x3, "foto") != "null") showComm("aluno/foto/consultar?idAluno=" + ida[i], na, ava, tx[i], dateFormat(new Date(+dt[i])), cond ? 0 : 1);
                            else showComm(dpfp, na, ava, tx[i], dateFormat(new Date(+dt[i])), cond ? 0 : 1);
                        });
                    });
                });
            }

            let btncom = document.querySelector("#comentar");
            let nma;
            if (podecom) {
                fete("http://localhost:8080/app/aluno/consultar?id=" + idu, function (xml) {
                    nma = xmlget(xml, "nome");
                    if(xmlget(xml, "foto") != "null") sc.appendChild(makeComm("aluno/foto/consultar?idAluno=" + idu, nma, 0, null, dateFormat(new Date())));
                    else sc.appendChild(makeComm(dpfp, nma, 0, null, dateFormat(new Date())));
                });
            }
            else {
                ew.classList.add("hidden");
                const fl = document.querySelector("#faca-login");
                fl.classList.remove("hidden");
                if (cargou == "ALUNO") {
                    fl.textContent = "Você já comentou nesse professor.";
                }
            }

            btncom.addEventListener("click", function () {
                if (cargou == "ALUNO") {
                    let nota = document.querySelector("#seuCom .div-av").av;
                    if (nota != 0) {
                        let tval = document.querySelector("#seuCom textarea").value;
                        let dta = new Date();
                        fete("http://localhost:8080/app/apresentacao/comentar?idProf=" + idprof + "&data=" + parsePedro(dta) + "&comentario=" + encodeURIComponent(tval), function () {
                            fete("http://localhost:8080/app/apresentacao/avaliar?idProf=" + idprof + "&nota=" + nota, function () {
                                seuCom.innerHTML = "";
                                ew.classList.add("hidden");
                                fete("http://localhost:8080/app/aluno/consultar?id=" + idu, function(x3){
                                    if(xmlget(x3, "foto") != "null") showComm("aluno/foto/consultar?idAluno=" + idu, nma, nota, tval, dateFormat(dta), 0);
                                    else showComm(dpfp, nma, nota, tval, dateFormat(dta), 0);
                                });
                            });
                        });
                    }
                    else {
                        torrada(tc, "Por favor dê uma avaliação", false);
                    }
                }
                else {
                    torrada(tc, "wpufquwfbquefn", true);
                }
            });
        });
    });
}

function setAv(el, x) {
    el.av = x;
    if (el.nota) {
        let arr = Math.round(x * 10) / 10;
        if (arr % 1 == 0) {
            arr += ".0";
        }
        el.nota.innerHTML = arr;
    }

    let pi = Math.floor(x);
    let pd = x - pi;
    let i = 0;
    for (; i < pi; i++) {
        setEst(el.ests[i], 1);
    }

    if (i < 5) {
        setEst(el.ests[i], pd);
        i++;
        for (; i < 5; i++) {
            setEst(el.ests[i], 0);
        }
    }
}

function setEst(est, qtd) {
    let w2;
    if (qtd == 0) {
        w2 = 0;
    }
    else if (qtd == 1) {
        w2 = w;
    }
    else {
        w2 = w * aj + qtd * w * (1 - 2 * aj);
    }
    est.style.width = w2 + "px";
}

function makeAv(ret, x = 0, nota = false, num = -1, edit = false) {
    ret.className = "fr div-av";
    let av = document.createElement("div");
    av.className = "av fr";

    ret.ests = [];
    for (let i = 0; i < 5; i++) {
        let d = document.createElement("div");
        let img = document.createElement("img");
        img.src = "img/est.png";
        d.appendChild(img);
        ret.ests[i] = document.createElement("div");
        d.appendChild(ret.ests[i]);
        av.appendChild(d);
    }

    if (nota) {
        ret.nota = document.createElement("strong");
        ret.nota.className = "nprof";
        ret.appendChild(ret.nota);
    }
    ret.appendChild(av);
    if (num != -1) {
        let sn = document.createElement("span");
        sn.className = "numAval";
        ret.appendChild(sn);
        sn.appendChild(document.createTextNode("("));
        ret.num = document.createElement("span");
        ret.num.innerHTML = num;
        sn.appendChild(ret.num);
        sn.appendChild(document.createTextNode(")"));
    }

    if (edit) {
        av.addEventListener("mousemove", function (e) {
            let x = e.pageX - av.offsetLeft;
            let lt = w + me;
            let cheias = Math.floor(x / lt);
            //let ult=x%lt;
            setAv(ret, x < 0 ? 1 : Math.min(cheias + 1, 5));
        });
    }

    setAv(ret, x);
}

function makeComm(img, nome, av, txt, data) {
    let d = document.createElement("div");
    d.className = "fc2";

    let r = document.createElement("div");
    r.className = "fr";
    let imContainer = document.createElement("div");
    imContainer.className = "pfp";
    let im = document.createElement("img");
    im.src = img;
    imContainer.appendChild(im)
    r.appendChild(imContainer);

    let c = document.createElement("div");
    c.className = "fc2";

    let dnd = document.createElement("div");
    dnd.className = "fr tc";
    let dnome = document.createElement("h4");
    dnome.textContent = nome;
    dnd.appendChild(dnome);
    let ddata = document.createElement("span");
    ddata.textContent = data;
    dnd.appendChild(ddata);
    c.appendChild(dnd);

    let dav = document.createElement("div");
    if (av != -1) {
        makeAv(dav, av, false, -1, txt == null);
    }
    else {
        dav.textContent = "Sem avaliação";
    }
    c.appendChild(dav);
    r.appendChild(c);
    d.appendChild(r);

    if (txt == null) {
        let ta = document.createElement("textarea");
        ta.placeholder = "Escreva seu comentário";
        d.appendChild(ta);
    }
    else {
        let p = document.createElement("p");
        p.textContent = txt;
        d.appendChild(p);
    }

    return d;
}

function showComm(img, nome, av, txt, data, ord = 1) {
    let co = makeComm(img, nome, av, txt, data);
    co.style.order = ord;
    coms.appendChild(co);
}
