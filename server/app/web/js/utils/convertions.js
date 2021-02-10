function conversionData1(dataInicial) {
  let dataFinal = "";
    dataFinal += dataInicial.charAt(8);
    dataFinal += dataInicial.charAt(9);
    dataFinal += "/";
    dataFinal += dataInicial.charAt(5);
    dataFinal += dataInicial.charAt(6);
    dataFinal += "/";
    dataFinal += dataInicial.charAt(0);
    dataFinal += dataInicial.charAt(1);
    dataFinal += dataInicial.charAt(2);
    dataFinal += dataInicial.charAt(3);
    return dataFinal;
}

function convertComma(valor) {
  let x = valor.indexOf(".");
  valor = setCharAt(valor, x, ",")
  return valor;
}

function rep() {
    var str = 'Hello World';
    str = setCharAt(str,4,'a');
    alert(str);
}

function setCharAt(str,index,chr) {
    if(index > str.length-1) return str;
    return str.substring(0,index) + chr + str.substring(index+1);
}
