window.addEventListener('load', loadCustomers);

document.getElementById("login-button").addEventListener('click', login);

function loadCustomers() {
  console.info('start loading');

  customerService.getAll()
    .then(customers => {
      rebuildSelect(document.getElementById("customer-select"), customers);
    })
}

function rebuildSelect(rootElement, customers) {
  customers.forEach(customer =>
    rootElement.appendChild(createElement("option", {"data-customer-id": customer.id},
      createTextElement(customer.name)))
  );
}

function login() {
  const selectElement = document.getElementById("customer-select");
  if (selectElement.selectedIndex !== -1) {
    const selectedElement = selectElement.item(selectElement.selectedIndex);
    if (selectedElement !== null) {
      const customerId = selectedElement.getAttribute("data-customer-id");
      console.log("customer ID: " + customerId);
      localStorage.setItem("customerId", customerId);

      document.location.replace(document.location.origin + '/' + 'main.html');
    }
  }
}