function removeChildElements(rootElement) {
    let child = rootElement.lastElementChild;
    while (child) {
        rootElement.removeChild(child);
        child = rootElement.lastElementChild;
    }
}

function createElement(tagName, attributes, ...children) {
    const element = document.createElement(tagName);
    Object.keys(attributes).forEach(key => {
        element.setAttribute(key, attributes[key]);
    });

    children.forEach(child => element.appendChild(child));

    return element;
}

function createTextElement(data) {
    return document.createTextNode(data);
}

function formatDate(isoString) {
    const date = new Date(isoString);
    const day = ("0" + date.getDate()).slice(-2);
    const month = ("0" + (date.getMonth() + 1)).slice(-2);
    const year = date.getFullYear();

    const hours = ("0" + date.getHours()).slice(-2);
    const minutes = ("0" + date.getMinutes()).slice(-2);
    const seconds = ("0" + date.getSeconds()).slice(-2);

    return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
}

function loadDataFromServer(data) {
    const endpoint = `http://localhost:8080/api/${data}/`;

    return new Promise((resolve, reject) => {
        fetch(endpoint)
            .then(response => response.json())
            .then(resolve)
            .catch(reject);
    });
}