function gerarFeed(){
	let s = document.querySelectorAll("select");
	console.log(s[0].value, s[1].value);
	let url = "http://localhost:8080/app/aluno/feed?orderBy=" + s[0].value + "&order=" + s[1].value;
	fetch(url, {  method: "POST", credentials: 'include' })
	.then(resposta => {
		if(resposta.status == 403){
			alert("Você não tem permissão para fazer isso. Faça login como aluno.")
			window.location.replace("http://localhost:8080/app/login.html")
		}
		else return resposta.arrayBuffer();
	})
	.then(buffer => {
		let decoder = new TextDecoder("iso-8859-1");
		let text = decoder.decode(buffer);
		parser = new DOMParser();
		xmlDoc = parser.parseFromString(text, "text/xml");
		xmlArr = xmlDoc.querySelectorAll("professor");
		console.log(xmlArr);
		criarDivs(xmlArr);
	});
}

function criarDivs(xml){
	let d = document.querySelector(".feed-container");
	let str = "";
	console.log(xml.length);
	if(xml.length > 0){
		d.innerHTML = "Carregando...";
		for(let i = 0; i < xml.length; i++){
			str += `<div class="feed-row">
				<div class='img-container'>` + checkImage(xml, i) + `</div>
				<div class="row-content">
					<div class="row-content-header">
						<h4>` + xml[i].querySelector("nome").textContent + `</h4>
						<h6><img src="img/local.png" class="icon"> ` + xml[i].querySelector("municipio").textContent + ` / ` + xml[i].querySelector("uf").textContent + `</h6>
					</div>
					<div class="row-content-footer">
						<div class="av-container">
							<p><b style="font-size: 1.5em;">` + formatAvaliacao(xml[i].querySelector("avaliacao").textContent) + `</b> (` + generateAvaliacoes(xml[i].querySelector("numeroAvaliacoes").textContent) + `)</p>
							<div class="star-container">
								<img src="img/est2.png" class="star">
								<img src="img/est2.png" class="star">
								<img src="img/est2.png" class="star">
								<img src="img/est2.png" class="star">
								<img src="img/est2.png" class="star">
								<div class="star-background" style="width: ` + calcAvaliacao(xml[i].querySelector("avaliacao").textContent) + `%"></div>
							</div>
						</div>
						<div style="display: flex; justify-content: flex-end; align-items: flex-start; flex-direction: column;">
							<p><img src="img/pessoa.png" class="icon">` + formatAlunos(xml[i].querySelector("numeroAlunosMin").textContent, xml[i].querySelector("numeroAlunosMax").textContent) + `</p>
							<p><img src="img/livros.png" class="icon"><span id="matSpan">` + xml[i].querySelector("materia").textContent + `</span></p>
						</div>
						<div style="display: flex; justify-content: flex-start; align-items: flex-end;">
							<p>R$ ` + formatPrice(xml[i].querySelector("precoHora").textContent) + ` / hora</p>
						</div>
						<div style="display: flex; justify-content: flex-end; align-items: flex-end;">
							<a href="http://localhost:8080/app/apresentacao_professor.html?id=` + xml[i].querySelector("idProf").textContent + `" class="btn btn-primary" style="margin: 0;">Visualizar professor</a>
						</div>
					</div>
				</div>
			</div>`
		}
	} else{
		str = "<h6>Não foi encontrado nenhum professor.</h6>"
	};
	d.innerHTML = str;
}

function checkImage(xml, i){
	let str = "";
	if(xml[i].querySelector("foto").textContent != "null"){
		str = `<img src="foto/consultar?idProf=` + xml[i].querySelector("idProf").textContent + `">`;
	} else str = `<img src="img/empty-profile.png">`;
	console.log(str);
	return str;
}

function formatPrice(str){
	let f = parseFloat(str);
	f = f.toFixed(2);
	return f.toString().replace(".", ",");
}

function formatAvaliacao(str){
	let f = parseFloat(str);
	f = f.toFixed(1);
	return f;
}

function calcAvaliacao(av){
	let f = formatAvaliacao(av);
	return (f / 5.0) * 100;
}

function generateAvaliacoes(str){
	let i = parseInt(str);
	if(i == 0) return "Nenhuma avaliação";
	else if(i == 1) return i + " avaliação";
	else return i + " avaliações";
}

function formatAlunos(min, max){
	if(min == '0' && max == '0') return "-"
	else return min + " - " + max;
}