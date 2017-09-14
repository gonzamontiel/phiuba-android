window.onload = function() {
    console.log('Página cargada, comienza la funcion.');
    console.log(document.body.innerHTML);
    setTimeout(function() {
        console.log(document.getElementsByClassName('boton_link')[0]);
    }, 2000);
}