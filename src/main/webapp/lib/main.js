const customerId = localStorage.getItem("customerId");
document.getElementById("logout-button").addEventListener("click", logoutCustomer);

// planes
const planes = [
    document.getElementById('customers-plane'),
    document.getElementById('countries-plane'),
    document.getElementById('vendors-plane'),
    document.getElementById('watches-plane'),
    document.getElementById('orders-plane'),
];

// menu items
const menuItems = [
    document.getElementById('item-customers'),
    document.getElementById('item-countries'),
    document.getElementById('item-vendors'),
    document.getElementById('item-watches'),
    document.getElementById('item-orders'),
];

const storage = {
    customers: [],
    countries: [],
    vendors: [],
    watches: [],
    orders: [],
    //
    selectedCustomer: -1,
    selectedCountry: -1,
    selectedVendor: -1,
};

// toggle default plane
togglePlane(planes, 'countries-plane');


if (customerId == null) {
    console.log("customerId not found");
    document.location.replace(document.location.origin + '/');
} else {
    console.log("customerId is:" + customerId);
    loadCustomer(customerId);

    const itemCustomers = document.getElementById("item-customers");
    itemCustomers.addEventListener('click', menuItemClicked);

    const itemCountries = document.getElementById("item-countries");
    itemCountries.addEventListener('click', menuItemClicked);

    const itemVendors = document.getElementById("item-vendors");
    itemVendors.addEventListener('click', menuItemClicked);

    const itemWatches = document.getElementById("item-watches");
    itemWatches.addEventListener('click', menuItemClicked);

    const itemOrders = document.getElementById("item-orders");
    itemOrders.addEventListener('click', menuItemClicked);
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

                function selectTableRow(event) {
                    if (storage.selectedCustomer !== -1) {
                        console.log("deselect row:", storage.selectedCustomer);
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
                    const tr = document.createElement("tr");
                    tr.setAttribute("data-customer-id", customer.id);
                    tr.addEventListener('click', selectTableRow);

                    const tdName = document.createElement("td");
                    tdName.appendChild(document.createTextNode(customer.name));
                    tr.appendChild(tdName);

                    const tdSum = document.createElement("td");
                    tdSum.appendChild(document.createTextNode(customer.sumOfOrders.toFixed(2)));
                    tr.appendChild(tdSum);

                    const tdCardNumber = document.createElement("td");
                    tdCardNumber.appendChild(document.createTextNode(customer.discountCard.number));
                    tr.appendChild(tdCardNumber);

                    const tdPercent = document.createElement("td");
                    tdPercent.appendChild(document.createTextNode(customer.discountCard.percent.toFixed(2)));
                    tr.appendChild(tdPercent);

                    tbody.appendChild(tr);
                });
            }

            loadDataFromServer('customer')
                .then(customers => {
                    storage.customers = customers;
                    buildCustomerTable(document.getElementById("customers-table"),
                        storage.customers);
                });
            // build table
            togglePlane(planes, 'customers-plane');
            break;
        }

        case 'countries': {
            storage.selectedCountry = -1;
            document.getElementById("country-edit-button").classList.add("disabled");
            document.getElementById("country-delete-button").classList.add("disabled");

            function buildCountryTable(rootElement, data) {

                function selectTableRow(event) {
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
                    const tr = document.createElement("tr");
                    tr.setAttribute("data-country-id", country.id);
                    tr.addEventListener('click', selectTableRow);
                    tr.addEventListener('click', selectTableRow);

                    const tdId = document.createElement("td");
                    tdId.appendChild(document.createTextNode(country.id));
                    tr.appendChild(tdId);

                    const tdName = document.createElement("td");
                    tdName.appendChild(document.createTextNode(country.name));
                    tr.appendChild(tdName);

                    tbody.appendChild(tr);
                });
            }

            loadDataFromServer('country')
                .then(countries => {
                    storage.countries = countries;
                    buildCountryTable(document.getElementById("countries-table"),
                        storage.countries);
                });

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
                        console.log("deselect row:", storage.selectedVendor);
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
                    const tr = document.createElement("tr");
                    tr.setAttribute("data-vendor-id", vendor.id);
                    tr.addEventListener('click', selectTableRow);

                    const tdId = document.createElement("td");
                    tdId.appendChild(document.createTextNode(vendor.id));
                    tr.appendChild(tdId);

                    const tdName = document.createElement("td");
                    tdName.appendChild(document.createTextNode(vendor.name));
                    tr.appendChild(tdName);

                    const tCountry = document.createElement("td");
                    tCountry.appendChild(document.createTextNode(vendor.country.name));
                    tr.appendChild(tCountry);

                    tbody.appendChild(tr);
                });
            }

            loadDataFromServer('vendor')
                .then(vendors => {
                    storage.vendors = vendors;
                    buildVendorTable(document.getElementById("vendors-table"),
                        storage.vendors);
                });

            togglePlane(planes, 'vendors-plane');
            break;
        }

        case 'watches': {
            console.log('watches menu clicked');
            storage.watches = loadDataFromServer('watch');

            togglePlane(planes, 'watches-plane');
            break;
        }

        case 'orders': {
            console.log('orders menu clicked');
            storage.orders = loadDataFromServer('order');

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

function loadDataFromServer(data) {
    const endpoint = `http://localhost:8080/api/${data}/`;

    return new Promise((resolve, reject) => {
        fetch(endpoint)
            .then(response => response.json())
            .then(resolve)
            .catch(reject);
    });
}

function removeChildElements(rootElement) {
    let child = rootElement.lastElementChild;
    while (child) {
        rootElement.removeChild(child);
        child = rootElement.lastElementChild;
    }
}