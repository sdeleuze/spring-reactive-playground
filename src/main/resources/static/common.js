function addEvent(evnt, elem, func) {
    if (elem.addEventListener)  // W3C DOM
        elem.addEventListener(evnt,func,false);
    else if (elem.attachEvent) { // IE DOM
        elem.attachEvent("on"+evnt, func);
    }
    else { // No much to do
        elem[evnt] = func;
    }
}

function print(message) {
    var elem = document.createElement('span');
    elem.innerHTML = message + '<br />';
    document.getElementById('output').appendChild(elem);
}