function redrawCustomersPlane() {
  storage.selectedCustomer = -1;
  document.getElementById("customer-edit-button").classList.add("disabled");
  document.getElementById("customer-delete-button").classList.add("disabled");

  //
  const table = document.getElementById("customers-table");

  if (storage.customers.length > 0) {
    buildCustomerTable(table, storage.customers);
  } else {
    customerService.getAll()
      .then(customers => {
        storage.customers = customers;
        buildCustomerTable(table, storage.customers);
      });
  }
}

function buildCustomerTable(rootElement, data) {

  function selectTableRow() {
    if (storage.selectedCustomer !== -1) {
      const previousRow = this.parentElement.querySelector(`tr[data-customer-id="${storage.selectedCustomer}"]`);
      if (previousRow !== null) {
        previousRow.classList.remove("table-info");
      }
    }

    document.getElementById("customer-edit-button").classList.remove("disabled");
    document.getElementById("customer-delete-button").classList.remove("disabled");

    storage.selectedCustomer = Number.parseInt(this.getAttribute("data-customer-id"));
    this.classList.add("table-info");
  }

  const tbody = rootElement.querySelector("tbody");
  removeChildElements(tbody);

  data.forEach(customer => {
    const tr = createElement("tr", {"data-customer-id": customer.id},
      createElement("td", {}, createTextElement(customer.name)),
      createElement("td", {}, createTextElement(customer.sumOfOrders.toFixed(2))),
      createElement("td", {}, createTextElement(customer.discountCard.number)),
      createElement("td", {}, createTextElement(customer.discountCard.percent.toFixed(2)))
    );

    tr.addEventListener('click', selectTableRow);
    tbody.appendChild(tr);
  });
}
