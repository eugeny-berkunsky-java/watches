const customerId = localStorage.getItem("customerId");
document.getElementById("logout-button").addEventListener("click", logoutCustomer);

if (customerId == null) {
    console.log("customerId not found");
    //todo: back to login screen
} else {
    console.log("customerId is:" + customerId);
    loadCustomer(customerId);

    //todo: build main screen
}

function loadCustomer(id) {
    const endpoint = `http://localhost:8080/api/customer/${id}`;
    fetch(endpoint)
        .then(response => response.json())
        .then(updateNavBar);
}

function updateNavBar(customer) {
    console.log("customer: ", customer);
    const customerNameElement = document.getElementById('customer-name');
    customerNameElement.setAttribute("value", customer.name);
}

function logoutCustomer(event) {
    event.preventDefault();
    localStorage.removeItem("customerId");
    document.location.replace(document.location.origin + '/');
}