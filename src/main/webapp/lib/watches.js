const watchDialog = document.getElementById("watch-dialog");


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

function showCreateWatchDialog() {
  showWatchDialog("Create New Watch", "Create", null, model => {
    watchService.create(model)
      .then(watch => {
        storage.watches = [...storage.watches, watch];
        redrawWatchesTable();
      }).catch(console.error);
  });
}

function showUpdateWatchDialog() {
  if (storage.selectedWatch !== -1) {
    const watch = storage.watches.find(w => w.id === storage.selectedWatch);
    if (watch) {
      showWatchDialog("Update Watch", "Update", watch, model => {
        watchService.update(model)
          .then(watch => {
            storage.watches = storage.watches.map(w => w.id === watch.id
              ? watch : w);
            redrawWatchesTable();
          })
          .catch(console.error);
      });
    }
  }
}

function showDeleteWatchDialog() {
  if (storage.selectedWatch !== -1) {
    const watch = storage.watches.find(w => w.id === storage.selectedWatch);
    if (watch) {
      showDeleteDialog("Delete Watch", watch.brand, () => {
        watchService.delete(storage.selectedWatch)
          .then(() => {
            storage.watches = storage.watches.filter(w => w.id !== storage.selectedWatch);
            redrawWatchesTable();
          })
          .catch(console.error);
      });
    }
  }
}

function showWatchDialog(title, buttonTitle, model, processFunction) {
  function closeWatchDialog() {
    dialogButtonTitle.removeEventListener('click', buttonHandler);
    dialogCloseButton.removeEventListener('click', closeWatchDialog);
    watchDialog.style.display = 'none';
  }

  const dialogTitle = document.getElementById("watch-dialog-title");
  const dialogButtonTitle = document.getElementById("watch-dialog-button-title");
  const dialogCloseButton = document.getElementById("watch-dialog-close-button");
  const brandField = document.getElementById("watch-dialog-brand");
  const typeField = document.getElementById("watch-dialog-type");
  const quantityField = document.getElementById("watch-dialog-qty");
  const priceField = document.getElementById("watch-dialog-price");
  const vendorField = document.getElementById("watch-dialog-vendor");

  dialogTitle.innerText = title;
  dialogButtonTitle.innerText = buttonTitle;

  if (model) {
    brandField.value = model.brand;
    typeField.value = model.type;
    quantityField.value = model.qty;
    priceField.value = model.price;
  } else {
    brandField.value = "";
    typeField.value = "DIGITAL";
    quantityField.value = 1;
    priceField.value = 0.00;
  }

  //fill vendor list
  new Promise((resolve, reject) => {
    if (storage.vendors.length !== 0) {
      resolve(storage.vendors);
    } else {
      vendorService.getAll()
        .then(vendors => {
          storage.vendors = vendors;
          storage.vendors.sort((a, b) => a.id - b.id);
          resolve(storage.vendors);
        })
        .catch(reject);
    }
  })
    .then(vendors => {
      removeChildElements(vendorField);
      vendors.forEach(vendor => {
        vendorField.appendChild(createElement("option",
          {"data-vendor-id": vendor.id}, createTextElement(vendor.name)));
      });
      if (model) {
        vendorField.value = model.vendor.name;
      } else {
        vendorField.selectedIndex = 0;
      }
    });

  // set button handler
  const buttonHandler = () => {
    // get all data
    const vendorId = Number.parseInt(
      vendorField.item(vendorField.selectedIndex).getAttribute("data-vendor-id"));

    const model = {
      id: storage.selectedWatch,
      brand: brandField.value,
      type: typeField.value,
      qty: Number.parseInt(quantityField.value),
      price: Number.parseFloat(priceField.value),
      vendor: {
        id: vendorId
      }
    };

    processFunction(model);
    closeWatchDialog();
  };

  dialogButtonTitle.addEventListener('click', buttonHandler);
  dialogCloseButton.addEventListener('click', closeWatchDialog);

  watchDialog.style.display = 'block';
}
