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
            redrawCustomersPlane();
            togglePlane(planes, 'customers-plane');
            break;
        }

        case 'countries': {
            redrawCountriesPlane();
            togglePlane(planes, 'countries-plane');
            break;
        }

        case 'vendors': {
            redrawVendorsPlane();
            togglePlane(planes, 'vendors-plane');
            break;
        }

        case 'watches': {
            redrawWatchesTable();
            togglePlane(planes, 'watches-plane');
            break;
        }

        case 'orders': {
            redrawOrdersTable();
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
