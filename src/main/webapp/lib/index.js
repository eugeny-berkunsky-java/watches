window.addEventListener('load', loadCustomers);

document.getElementById("login-button").addEventListener('click', login);

function loadCustomers() {
    console.info('start loading');

    const endpoint = "http://localhost:8080/api/customer/";
    fetch(endpoint, {method: "GET"})
        .then(response => {

            if (response.ok) {
                return response.json();
            } else {
                throw new Error();
            }
        })
        .then(json => {
            console.log(json);
            rebuildSelect(document.getElementById("customer-select"), json);
        })
        .catch(error => {
            console.error(error);
            console.error("cant load customers");
        });
}

function rebuildSelect(rootElement, customers) {
    customers.forEach(customer => {
        const element = document.createElement("option");
        element.setAttribute("data-customer-id", customer.id);
        element.appendChild(document.createTextNode(customer.name));
        rootElement.appendChild(element);
    });
}

function login() {
    const selectElement = document.getElementById("customer-select");
    if (selectElement.selectedIndex !== -1) {
        const selectedElement = selectElement.item(selectElement.selectedIndex);
        if (selectedElement !== null) {
            const customerId = selectedElement.getAttribute("data-customer-id");
            console.log("customer ID: " + customerId);
            localStorage.setItem("customerId", customerId);
            //todo: move to main page

            document.location.replace(document.location.origin + '/' + 'main.html');
        }
    }
}