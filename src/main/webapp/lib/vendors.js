const vendorDialog = document.getElementById("vendor-dialog");

function redrawVendorsPlane() {
  storage.selectedVendor = -1;
  document.getElementById("vendor-edit-button").classList.add("disabled");
  document.getElementById("vendor-delete-button").classList.add("disabled");

  const table = document.getElementById("vendors-table");

  if (storage.vendors.length > 0) {
    buildVendorTable(table, storage.vendors);
  } else {
    vendorService.getAll()
      .then(vendors => {
        storage.vendors = vendors;
        storage.vendors.sort((a, b) => a.id - b.id);
        buildVendorTable(table, storage.vendors);
      })
  }
}

function buildVendorTable(rootElement, data) {
  function selectTableRow() {
    if (storage.selectedVendor !== -1) {
      const previousRow = this.parentElement.querySelector(
        `tr[data-vendor-id="${storage.selectedVendor}"]`
      );
      if (previousRow !== null) {
        previousRow.classList.remove("table-info");
      }
    }

    document.getElementById("vendor-edit-button").classList.remove("disabled");
    document
      .getElementById("vendor-delete-button")
      .classList.remove("disabled");

    storage.selectedVendor = Number.parseInt(
      this.getAttribute("data-vendor-id")
    );
    this.classList.add("table-info");
  }

  const tbody = rootElement.querySelector("tbody");
  removeChildElements(tbody);

  data.forEach(vendor => {
    const tr = createElement(
      "tr",
      {"data-vendor-id": vendor.id},
      createElement("td", {}, createTextElement(vendor.id)),
      createElement("td", {}, createTextElement(vendor.name)),
      createElement("td", {}, createTextElement(vendor.country.name))
    );

    tr.addEventListener("click", selectTableRow);
    tbody.appendChild(tr);
  });
}

function showCreateVendorDialog() {
  showVendorDialog("Add New Vendor", null, "Create", data =>
    vendorService.create(data)
      .then(vendor => {
        storage.vendors = [...storage.vendors, vendor];
        redrawVendorsPlane();
      })
      .catch(console.error)
  );
}

function showUpdateVendorDialog() {
  if (storage.selectedVendor !== -1) {
    const vendor = storage.vendors.find(v => v.id === storage.selectedVendor);
    showVendorDialog("Update Vendor", vendor, "Update", data =>
      vendorService.update(data)
        .then(vendor => {
          storage.vendors = storage.vendors.map(v => v.id === vendor.id ? vendor : v);
          redrawVendorsPlane();
        })
        .catch(console.error)
    );
  }
}

function showVendorDialog(title, model, buttonTitle, processFunction) {
  const nameField = document.getElementById("vendor-dialog-name");
  const selectCountryField = document.getElementById("vendor-dialog-country");

  const dialogTitle = document.getElementById("vendor-dialog-title");
  dialogTitle.innerText = title;

  const buttonHandler = () => {
    const countryId = selectCountryField
      .item(selectCountryField.selectedIndex)
      .getAttribute("data-country-id");

    processFunction({id: storage.selectedVendor, name: nameField.value, country: {id: countryId}});
    closeVendorDialog();
    dialogButtonTitle.removeEventListener('click', buttonHandler);
  };
  const dialogButtonTitle = document.getElementById("vendor-dialog-button-title");
  dialogButtonTitle.innerText = buttonTitle;
  dialogButtonTitle.addEventListener('click', buttonHandler);

  // fill countries
  const countriesList = document.getElementById("vendor-dialog-country");

  removeChildElements(countriesList);

  new Promise((resolve, reject) => {
    return storage.countries.length > 0
      ? resolve(storage.countries)
      : countryService.getAll()
        .then(countries => resolve(countries))
        .catch(error => reject(error));
  }).then(countries => {
    storage.countries = countries;
    storage.countries.sort((a, b) => a.id - b.id);
    storage.countries.forEach(country => {
      countriesList.appendChild(
        createElement("option", {"data-country-id": country.id}, createTextElement(country.name))
      );
    });

    if (model) {
      nameField.value = model.name;
      selectCountryField.value = model.country.name;

    } else {
      nameField.value = '';
      selectCountryField.selectedIndex = 0;
    }

    vendorDialog.style.display = "block";
  });


}

function closeVendorDialog() {
  vendorDialog.style.display = "none";
}

function showDeleteVendorDialog() {
  if (storage.selectedVendor !== -1) {
    const vendor = storage.vendors.find(v => v.id === storage.selectedVendor);
    if (vendor) {
      console.log(vendor);
      showDeleteDialog("Delete Vendor", vendor.name, () => {
        vendorService.delete(vendor.id)
          .then(() => {
            const removedId = storage.selectedVendor;
            storage.vendors = storage.vendors.filter(v => v.id !== removedId);
            redrawVendorsPlane();
          })
          .catch(console.error);
      });
    }
  }
}