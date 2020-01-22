function redrawVendorsPlane() {

    storage.selectedVendor = -1;
    document.getElementById("vendor-edit-button").classList.add("disabled");
    document.getElementById("vendor-delete-button").classList.add("disabled");

    const table = document.getElementById("vendors-table");

    if (storage.vendors.length > 0) {
        buildVendorTable(table, storage.vendors);
    } else {
        loadDataFromServer('vendor')
            .then(vendors => {
                storage.vendors = vendors;
                buildVendorTable(table, storage.vendors);
            });
    }
}

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


