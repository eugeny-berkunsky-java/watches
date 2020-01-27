const orderDetailsDialog = document.getElementById("order-detail-dialog");
const orderDetailsTitle = document.getElementById("order-detail-dialog-title");
const orderDetailsCloseButton = document.getElementById("order-detail-dialog-close-button");

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

