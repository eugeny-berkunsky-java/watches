function redrawCountriesPlane() {
    storage.selectedCountry = -1;
    document.getElementById("country-edit-button").classList.add("disabled");
    document.getElementById("country-delete-button").classList.add("disabled");

    //
    const table = document.getElementById("countries-table");

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
}

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