function gerarToastErro(texto) {
  let toast = document.querySelector('.toastErro');
  toast.innerHTML = "";
  let resp = document.createElement("p");
  resp.innerHTML = texto;
  toast.appendChild(resp);
  $(document).ready(function(){
    $(".toastErro").toast({
      delay: 3000
    });
    $('.toastErro').toast('show');
  });
}

function produzirToastServer(xmlDoc) {
  if (xmlDoc.getElementsByTagName("erro").length != 0) {
    let toast = document.querySelector('.toastErro');
    toast.innerHTML = "";
    var lineItems = xmlDoc.getElementsByTagName("erro");
    let resp = document.createElement("p");
    resp.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
    toast.appendChild(resp);
    $(document).ready(function(){
      $(".toastErro").toast({
        delay: 3000
      });
      $('.toastErro').toast('show');
    });
  } else {
    let toast = document.querySelector('.toastSucesso');
    toast.innerHTML = "";
    var lineItems = xmlDoc.getElementsByTagName("sucesso");
    let resp = document.createElement("p");
    resp.innerHTML = lineItems[0].getElementsByTagName("mensagem")[0].childNodes[0].nodeValue;
    toast.appendChild(resp);
    $(document).ready(function(){
      $(".toastSucesso").toast({
        delay: 3000
      });
      $('.toastSucesso').toast('show');
    });
  }
}
