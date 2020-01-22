function redrawWatchesTable() {
    storage.selectedWatch = -1;
    document.getElementById("watch-edit-button").classList.add("disabled");
    document.getElementById("watch-delete-button").classList.add("disabled");
    //
    const table = document.getElementById("watches-table");
    if (storage.watches.length > 0) {
        buildWatchTable(table, storage.watches);
    } else {
        loadDataFromServer('watch')
            .then(watches => {
                storage.watches = watches;
                buildWatchTable(table, storage.watches);
            });
    }
}

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

