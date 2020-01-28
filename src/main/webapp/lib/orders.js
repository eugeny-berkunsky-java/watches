const orderDetailsDialog = document.getElementById("order-detail-dialog");
const orderDetailsTitle = document.getElementById("order-detail-dialog-title");
const orderDetailsCloseButton = document.getElementById("order-detail-dialog-close-button");

const orderAddDialog = document.getElementById("order-add-dialog");
const orderAddDialogCloseButton = document.getElementById("order-add-dialog-close-button");
const orderAddDialogButton = document.getElementById("order-add-dialog-button");

const itemAddDialog = document.getElementById("item-add-dialog");
const itemAddDialogCloseButton = document.getElementById("item-add-dialog-close-button");
const itemAddDialogAddButton = document.getElementById("item-add-dialog-button");

function redrawOrdersTable() {
  storage.selectedOrder = -1;
  document.getElementById("order-details-button").classList.add("disabled");
  document.getElementById("order-delete-button").classList.add("disabled");

  if (storage.orders.length > 0) {
    buildOrderTable(document.getElementById("orders-table"), storage.orders);
  } else {
    orderService.getAll()
      .then(orders => {
        storage.orders = orders.filter(order =>
          order.customer.id === storage.customerId
        );

        storage.orders.sort((a, b) => a.id - b.id);

        buildOrderTable(document.getElementById("orders-table"), storage.orders);
      });
  }
}

function buildOrderTable(rootElement, data) {
  function selectTableRow() {
    if (storage.selectedOrder !== -1) {
      const previousRow = this.parentElement.querySelector(`tr[data-order-id="${storage.selectedOrder}"]`);
      if (previousRow !== null) {
        previousRow.classList.remove("table-info");
      }
    }

    document.getElementById("order-details-button").classList.remove("disabled");
    document.getElementById("order-delete-button").classList.remove("disabled");

    storage.selectedOrder = Number.parseInt(this.getAttribute("data-order-id"));
    this.classList.add("table-info");
  }

  const tbody = rootElement.querySelector("tbody");
  removeChildElements(tbody);

  data.forEach(order => {
    const tr = createElement("tr", {"data-order-id": order.id},
      createElement("td", {}, createTextElement(order.id)),
      createElement("td", {}, createTextElement(formatDate(order.date))),
      createElement("td", {}, createTextElement(order.customer.name)),
      createElement("td", {}, createTextElement(order.totalPrice.toFixed(2))));

    tr.addEventListener('click', selectTableRow);
    tbody.appendChild(tr);
  });
}

function buildOrderDetailsTable(rootElement, data) {
  const tbody = rootElement.querySelector("tbody");
  removeChildElements(tbody);

  data.forEach(item => {
    tbody.appendChild(
      createElement("tr", {},
        createElement("td", {}, createTextElement(item.watch.brand)),
        createElement("td", {}, createTextElement(item.qty)),
        createElement("td", {}, createTextElement(item.price.toFixed(2))),
      )
    );
  });
}

function showOrderDetailsDialog() {
  const closeButtonHandler = () => {
    orderDetailsCloseButton.removeEventListener('click', closeButtonHandler);
    orderDetailsDialog.style.display = 'none';
  };

  if (storage.selectedOrder !== -1) {
    const order = storage.orders.find(o => o.id === storage.selectedOrder);
    if (order) {
      orderDetailsTitle.innerText = `Order #${order.id} details`;
      orderDetailsCloseButton.addEventListener('click', closeButtonHandler);

      const detailsTable = document.getElementById("order-details-table");
      orderService.getById(order.id)
        .then(od => {
          buildOrderDetailsTable(detailsTable, od.items);
        })
        .finally(() => orderDetailsDialog.style.display = 'block')
    }
  }
}

function showDeleteOrderDialog() {
  if (storage.selectedOrder !== -1) {
    const order = storage.orders.find(o => o.id === storage.selectedOrder);
    if (order) {
      showDeleteDialog("Delete Order", `Order #${order.id}`, () => {
        orderService.delete(order.id)
          .then(() => {
            storage.orders
              = storage.orders.filter(o => o.id !== storage.selectedOrder);
            redrawOrdersTable();
          });
      });
    }
  }
}

function showAddOrderDialog() {
  const buttonHandler = () => {
    orderService.create({
      items: storage.items,
      customer: {id: localStorage.getItem("customerId")}
    })
      .then(order => {
        storage.orders = [...storage.orders, order];
        storage.items = [];
        redrawOrdersTable();
      });

    orderAddDialogCloseButton.removeEventListener('click', closeDialogHandler);
    orderAddDialogButton.removeEventListener('click', buttonHandler);
    orderAddDialog.style.display = 'none';
  };

  const closeDialogHandler = () => {
    orderAddDialogCloseButton.removeEventListener('click', closeDialogHandler);
    orderAddDialogButton.removeEventListener('click', buttonHandler);
    orderAddDialog.style.display = 'none';
  };

  redrawItemsTable();
  orderAddDialogCloseButton.addEventListener('click', closeDialogHandler);
  orderAddDialogButton.addEventListener('click', buttonHandler);
  orderAddDialog.style.display = 'block';
}

///

function redrawItemsTable() {
  const table = document.getElementById("order-add-table").querySelector("tbody");
  removeChildElements(table);

  const removeItemRowHandler = event => {
    console.info(event);
    removeItemButton.removeEventListener('click', removeItemRowHandler);
  };

  const removeItemButton = id => {
    const handler = () => {
      // console.log(`I was made to destroy ${id}`);
      storage.items = storage.items.filter(i => i.watch.id !== id);
      redrawItemsTable();

      element.removeEventListener('click', handler);
    };

    const element = createElement("button", {
      "class": "btn btn-danger font-weight-bolder",
    }, createTextElement("-"));
    element.addEventListener('click', handler);

    return element;
  };

  storage.items.forEach(item => table.appendChild(
    createElement("tr", {},
      createElement("td", {},
        removeItemButton(item.watch.id)),
      createElement("td", {"class": "align-middle"}, createTextElement(item.watch.brand)),
      createElement("td", {"class": "align-middle"}, createTextElement(item.qty)),
      createElement("td", {"class": "align-middle"}, createTextElement(item.price))
    )
  ))
}

function showAddItemDialog() {
  const closeButtonHandler = () => {
    itemAddDialogCloseButton.removeEventListener('click', closeButtonHandler);
    itemAddDialogAddButton.removeEventListener('click', addButtonHandler);
    itemAddDialog.style.display = 'none';
  };

  const addButtonHandler = () => {
    const watchField = document.getElementById("item-add-watch");

    const watchId = Number.parseInt(
      watchField.item(watchField.selectedIndex).getAttribute("data-watch-id"));

    const watchBrand = watchField.value;

    const item = {
      qty: Number.parseInt(document.getElementById("item-add-qty").value),
      price: Number.parseFloat(document.getElementById("item-add-price").value),
      watch: {id: watchId, brand: watchBrand}
    };

    storage.items = [...storage.items, item];
    redrawItemsTable();
    itemAddDialogCloseButton.removeEventListener('click', closeButtonHandler);
    itemAddDialogAddButton.removeEventListener('click', addButtonHandler);
    itemAddDialog.style.display = 'none';
  };

  new Promise((resolve, reject) => {
    if (storage.watches.length > 0) {
      resolve(storage.watches);
    } else {
      watchService.getAll()
        .then(watches => {
          storage.watches = watches;
          storage.watches.sort((a, b) => a.id - b.id);
          resolve(watches);
        })
        .catch(reject);
    }
  })
    .then(watches => {
      const selectWatch = document.getElementById("item-add-watch");
      removeChildElements(selectWatch);

      watches
        .filter(watch => !storage.items.some(i => i.watch.id === watch.id))
        .forEach(watch => selectWatch.appendChild(
          createElement("option", {"data-watch-id": watch.id}, createTextElement(watch.brand))
        ));
    })
    .catch(console.error)
    .finally(() => {
      document.getElementById("item-add-qty").value = 1;
      document.getElementById("item-add-price").value = 0;
      itemAddDialogCloseButton.addEventListener('click', closeButtonHandler);
      itemAddDialogAddButton.addEventListener('click', addButtonHandler);
      itemAddDialog.style.display = 'block';
    });
}