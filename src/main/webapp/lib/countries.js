const updateCountryDialog = document.getElementById("country-dialog");
const deleteCountryDialog = document.getElementById("country-dialog-delete");
const nameField = document.getElementById("country-dialog-name");
const deleteNameField = document.getElementById("country-dialog-delete-name");

function redrawCountriesPlane() {
    storage.selectedCountry = -1;
    document.getElementById("country-edit-button").classList.add("disabled");
    document.getElementById("country-delete-button").classList.add("disabled");

    //
    const table = document.getElementById("countries-table");

    if (storage.countries.length > 0) {
        buildCountryTable(table, storage.countries);
    } else {
        loadDataFromServer('country')
            .then(countries => {
                storage.countries = countries;
                storage.countries.sort((a, b) => a.id - b.id);
                buildCountryTable(table, storage.countries);
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

function showUpdateCountryDialog() {
    function processUpdate(data) {

        const endpoint = `http://localhost:8080/api/country/${data.id}`;
        fetch(endpoint, {
            method: 'PUT',
            headers: {'Content-type': 'application/json'},
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error();
                }
            })
            .then(country => {
                storage.countries = storage.countries.map(c => {
                    return c.id === country.id ? country : c;
                });
                redrawCountriesPlane();
            })
            .catch(error => console.error(error));
    }

    if (storage.selectedCountry !== -1) {
        const country = storage.countries.find(c => c.id === storage.selectedCountry);
        showCountryDialog("Update Country", country, "Update", processUpdate);
    }
}

function showCreateCountryDialog() {
    function processCreate(data) {

        const endpoint = 'http://localhost:8080/api/country/';
        fetch(endpoint, {
            method: 'POST',
            headers: {'Content-type': 'application-json'},
            body: JSON.stringify(data)
        })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error();
                }
            })
            .then(country => {
                storage.countries = [...storage.countries, country];
                redrawCountriesPlane();
            })
            .catch(error => console.error(error))

    }

    showCountryDialog("Add new Country", null, "Create", processCreate);
}

function showDeleteCountryDialog() {
    if (storage.selectedCountry !== -1) {
        const country = storage.countries.find(country => country.id === storage.selectedCountry);
        if (country) {
            deleteNameField.value = country.name;
        }

        deleteCountryDialog.style.display = 'block';

    }
}

function cancelDeleteCountryHandler() {
    deleteCountryDialog.style.display = 'none';
}

function proceedDeleteCountryHandler() {
    const endpoint = `http://localhost:8080/api/country/${storage.selectedCountry}`;
    fetch(endpoint, {
        method: 'DELETE'
    })
        .then(response => {
            if (response.ok) {
                const removedId = storage.selectedCountry;
                storage.countries = storage.countries.filter(c => c.id !== removedId);
                redrawCountriesPlane();
            }
        });

    deleteCountryDialog.style.display = 'none';
}

function showCountryDialog(title, model, buttonTitle, processFunction) {
    const buttonHandler = () => {
        processFunction({name: nameField.value, id: storage.selectedCountry});
        closeCountryDialog();
        dialogButtonTitle.removeEventListener('click', buttonHandler);
    };

    const dialogTitle = document.getElementById("country-dialog-title");
    dialogTitle.innerText = title;

    const dialogButtonTitle = document.getElementById("country-dialog-button-title");
    dialogButtonTitle.innerText = buttonTitle;
    dialogButtonTitle.addEventListener('click', buttonHandler);

    if (model) {
        nameField.value = model.name;
    } else {
        nameField.value = '';
    }

    updateCountryDialog.style.display = 'block';
}

function closeCountryDialog() {
    updateCountryDialog.style.display = 'none';
}
