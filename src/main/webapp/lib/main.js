const customerId = localStorage.getItem("customerId");
document.getElementById("logout-button").addEventListener("click", logoutCustomer);

// toggle default plane
togglePlane(planes, 'countries-plane');


if (customerId == null) {
    console.log("customerId not found");
    document.location.replace(document.location.origin + '/');
} else {

    loadCustomer(customerId)
        .then(updateNavBar);

    storage.customerId = Number.parseInt(customerId);

    menuItems.forEach(item => item.addEventListener('click', menuItemClicked));
}

function loadCustomer(id) {
    const endpoint = `http://localhost:8080/api/customer/${id}`;
    return new Promise((resolve, reject) => {
        fetch(endpoint)
            .then(response => response.json())
            .then(resolve)
            .catch(reject);
    })
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

function menuItemClicked(event) {
    const item = event.target.getAttribute("data-menu-item");
    menuItems.forEach(item => {
        if (item.getAttribute('id') === event.target.getAttribute('id')) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });

    switch (item) {
        case 'customers': {

            storage.selectedCustomer = -1;
            document.getElementById("customer-edit-button").classList.add("disabled");
            document.getElementById("customer-delete-button").classList.add("disabled");

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

            if (storage.customers.length > 0) {
                buildCustomerTable(document.getElementById("customers-table"), storage.customers);
            } else {
                loadDataFromServer('customer')
                    .then(customers => {
                        storage.customers = customers;
                        buildCustomerTable(document.getElementById("customers-table"),
                            storage.customers);
                    });
            }

            // show plane
            togglePlane(planes, 'customers-plane');
            break;
        }

        case 'countries': {
            storage.selectedCountry = -1;
            document.getElementById("country-edit-button").classList.add("disabled");
            document.getElementById("country-delete-button").classList.add("disabled");

            function buildCountryTable(rootElement, data) {

                function selectTableRow() {
                    if (storage.selectedCountry !== -1) {
                        const previousRow = this.parentElement.querySelector(`tr[data-country-id="${storage.selectedCountry}"]`);
                        if (previousRow !== null) {
                            previousRow.classList.remove("table-info");
                        }
                    }

                    document.getElementById("country-edit-button").classList.remove("disabled");
                    document.getElementById("country-delete-button").classList.remove("disabled");

                    storage.selectedCountry = Number.parseInt(this.getAttribute("data-country-id"));
                    this.classList.add("table-info");
                }

                const tbody = rootElement.querySelector("tbody");
                removeChildElements(tbody);

                data.forEach(country => {

                    const tr = createElement("tr", {"data-country-id": country.id},
                        createElement("td", {}, createTextElement(country.id)),
                        createElement("td", {}, createTextElement(country.name)),
                    );

                    tr.addEventListener('click', selectTableRow);
                    tbody.appendChild(tr);
                });
            }

            if (storage.countries.length > 0) {
                buildCountryTable(document.getElementById("countries-table"), storage.countries);
            } else {
                loadDataFromServer('country')
                    .then(countries => {
                        storage.countries = countries;
                        buildCountryTable(document.getElementById("countries-table"),
                            storage.countries);
                    });
            }

            togglePlane(planes, 'countries-plane');
            break;
        }

        case 'vendors': {
            storage.selectedVendor = -1;
            document.getElementById("vendor-edit-button").classList.add("disabled");
            document.getElementById("vendor-delete-button").classList.add("disabled");

            function buildVendorTable(rootElement, data) {
                function selectTableRow() {
                    if (storage.selectedVendor !== -1) {
                        const previousRow = this.parentElement.querySelector(`tr[data-vendor-id="${storage.selectedVendor}"]`);
                        if (previousRow !== null) {
                            previousRow.classList.remove("table-info");
                        }
                    }

                    document.getElementById("vendor-edit-button").classList.remove("disabled");
                    document.getElementById("vendor-delete-button").classList.remove("disabled");

                    storage.selectedVendor = Number.parseInt(this.getAttribute("data-vendor-id"));
                    this.classList.add("table-info");
                }

                const tbody = rootElement.querySelector("tbody");
                removeChildElements(tbody);

                data.forEach(vendor => {
                    const tr = createElement("tr", {"data-vendor-id": vendor.id},
                        createElement("td", {}, createTextElement(vendor.id)),
                        createElement("td", {}, createTextElement(vendor.name)),
                        createElement("td", {}, createTextElement(vendor.country.name))
                    );

                    tr.addEventListener('click', selectTableRow);
                    tbody.appendChild(tr);
                });
            }

            if (storage.vendors.length > 0) {
                buildVendorTable(document.getElementById("vendors-table"), storage.vendors);
            } else {
                loadDataFromServer('vendor')
                    .then(vendors => {
                        storage.vendors = vendors;
                        buildVendorTable(document.getElementById("vendors-table"),
                            storage.vendors);
                    });
            }

            togglePlane(planes, 'vendors-plane');
            break;
        }

        case 'watches': {
            storage.selectedWatch = -1;
            document.getElementById("watch-edit-button").classList.add("disabled");
            document.getElementById("watch-delete-button").classList.add("disabled");

            function buildWatchTable(rootElement, data) {
                function selectTableRow() {
                    if (storage.selectedWatch !== -1) {
                        const previousRow = this.parentElement.querySelector(`tr[data-watch-id="${storage.selectedWatch}"]`);
                        if (previousRow !== null) {
                            previousRow.classList.remove("table-info");
                        }
                    }

                    document.getElementById("watch-edit-button").classList.remove("disabled");
                    document.getElementById("watch-delete-button").classList.remove("disabled");

                    storage.selectedWatch = Number.parseInt(this.getAttribute("data-watch-id"));
                    this.classList.add("table-info");
                }


                const tbody = rootElement.querySelector("tbody");
                removeChildElements(tbody);

                data.forEach(watch => {
                    const tr = createElement("tr", {"data-watch-id": watch.id},
                        createElement("td", {}, createTextElement(watch.brand)),
                        createElement("td", {}, createTextElement(watch.type)),
                        createElement("td", {}, createTextElement(watch.qty)),
                        createElement("td", {}, createTextElement(watch.price.toFixed(2))),
                        createElement("td", {}, createTextElement(watch.vendor.name)),
                    );

                    tr.addEventListener('click', selectTableRow);
                    tbody.appendChild(tr);
                })
            }

            if (storage.watches.length > 0) {
                buildWatchTable(document.getElementById("watches-table"), storage.watches);
            } else {
                loadDataFromServer('watch')
                    .then(watches => {
                        storage.watches = watches;
                        buildWatchTable(document.getElementById("watches-table"),
                            storage.watches);
                    });
            }

            togglePlane(planes, 'watches-plane');
            break;
        }

        case 'orders': {
            storage.selectedOrder = -1;
            document.getElementById("order-details-button").classList.add("disabled");
            document.getElementById("order-edit-button").classList.add("disabled");
            document.getElementById("order-delete-button").classList.add("disabled");

            function buildOrderTable(rootElement, data) {
                function selectTableRow() {
                    if (storage.selectedOrder !== -1) {
                        const previousRow = this.parentElement.querySelector(`tr[data-order-id="${storage.selectedOrder}"]`);
                        if (previousRow !== null) {
                            previousRow.classList.remove("table-info");
                        }
                    }

                    document.getElementById("order-details-button").classList.remove("disabled");
                    document.getElementById("order-edit-button").classList.remove("disabled");
                    document.getElementById("order-delete-button").classList.remove("disabled");

                    storage.selectedOrder = Number.parseInt(this.getAttribute("data-order-id"));
                    this.classList.add("table-info");
                }

                const tbody = rootElement.querySelector("tbody");
                removeChildElements(tbody);

                data.forEach(order => {
                    // const date = new Date(order.date).toGMTString();
                    const tr = createElement("tr", {"data-order-id": order.id},
                        createElement("td", {}, createTextElement(order.id)),
                        createElement("td", {}, createTextElement(formatDate(order.date))),
                        createElement("td", {}, createTextElement(order.customer.name)),
                        createElement("td", {}, createTextElement(order.totalPrice.toFixed(2))));

                    tr.addEventListener('click', selectTableRow);
                    tbody.appendChild(tr);
                });
            }

            if (storage.orders.length > 0) {
                buildOrderTable(document.getElementById("orders-table"), storage.orders);
            } else {
                loadDataFromServer('order')
                    .then(orders => {
                        storage.orders = orders.filter(order =>
                            order.customer.id === storage.customerId
                        );

                        storage.orders.sort((a, b) => a.id - b.id);

                        buildOrderTable(document.getElementById("orders-table"), storage.orders);
                    });
            }

            togglePlane(planes, 'orders-plane');
            break;
        }

        default: {
            console.log('unknown menu item');
            break;
        }
    }
}

function togglePlane(planesArray, activePlane) {
    planesArray.forEach(plane => {
        if (plane.getAttribute('id') === activePlane) {
            plane.style.display = 'block';
        } else {
            plane.style.display = 'none';
        }
    });
}
