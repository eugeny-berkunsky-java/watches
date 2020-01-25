const removeChildElements = rootElement => {
  let child = rootElement.lastElementChild;
  while (child) {
    rootElement.removeChild(child);
    child = rootElement.lastElementChild;
  }
};

const createElement = (tagName, attributes, ...children) => {
  const element = document.createElement(tagName);
  Object.keys(attributes).forEach(key => {
    element.setAttribute(key, attributes[key]);
  });

  children.forEach(child => element.appendChild(child));

  return element;
};

const createTextElement = data => document.createTextNode(data);

const formatDate = isoString => {
  const date = new Date(isoString);
  const day = ("0" + date.getDate()).slice(-2);
  const month = ("0" + (date.getMonth() + 1)).slice(-2);
  const year = date.getFullYear();

  const hours = ("0" + date.getHours()).slice(-2);
  const minutes = ("0" + date.getMinutes()).slice(-2);
  const seconds = ("0" + date.getSeconds()).slice(-2);

  return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
};

const loadDataFromServer = data => {
  const endpoint = `http://localhost:8080/api/${data}/`;

  return new Promise((resolve, reject) => {
    fetch(endpoint)
      .then(response => response.json())
      .then(resolve)
      .catch(reject);
  });
};

const showDeleteDialog = (title, message, onDeleteDo, onCancelDo = () => {
}) => {
  const deleteDialog = document.getElementById('dialog-delete');
  const deleteButton = document.getElementById('dialog-delete-button-ok');
  const cancelButton = document.getElementById('dialog-delete-button-cancel');

  const deleteDialogTitle = document.getElementById('dialog-delete-title');
  const deleteDialogMessage = document.getElementById('dialog-delete-message');

  const deleteButtonHandler = () => {
    onDeleteDo();
    deleteDialog.style.display = 'none';
    deleteButton.removeEventListener('click', deleteButtonHandler);
    cancelButton.removeEventListener('click', cancelButtonHandler);
  };

  const cancelButtonHandler = () => {
    onCancelDo();
    deleteDialog.style.display = 'none';
    deleteButton.removeEventListener('click', deleteButtonHandler);
    cancelButton.removeEventListener('click', cancelButtonHandler);
  };

  deleteDialogTitle.innerText = title;
  deleteDialogMessage.value = message;

  deleteButton.addEventListener('click', deleteButtonHandler);
  cancelButton.addEventListener('click', cancelButtonHandler);

  deleteDialog.style.display = 'block';
};

const service = {
  get: endpoint => new Promise((resolve, reject) =>
    fetch(endpoint)
      .then(response => response.ok
        ? response.json()
        : Promise.reject(response.json()))
      .then(resolve)
      .catch(promise => promise.then(json => reject(json.message)))),

  create: (endpoint, data) => new Promise((resolve, reject) =>
    fetch(endpoint, {
      method: 'POST',
      headers: {'Content-type': 'application-json'},
      body: JSON.stringify(data)
    })
      .then(response => response.ok
        ? response.json()
        : Promise.reject(response.json()))
      .then(resolve)
      .catch(promise => promise.then(json => reject(json.message)))),

  update: (endpoint, data) => new Promise((resolve, reject) =>
    fetch(endpoint, {
      method: 'PUT',
      headers: {'Content-type': 'application/json'},
      body: JSON.stringify(data)
    })
      .then(response => response.ok
        ? response.json()
        : Promise.reject(response.json())
      )
      .then(resolve)
      .catch(promise => promise.then(json => reject(json.message)))),

  delete: endpoint => new Promise((resolve, reject) =>
    fetch(endpoint, {
      method: 'DELETE'
    })
      .then(response => response.ok
        ? Promise.resolve()
        : Promise.reject(response.json()))
      .then(resolve)
      .catch(promise => promise.then(json => reject(json.message)))),
};

const countryService = {
  getAll: () =>
    service.get("http://localhost:8080/api/country/"),

  getById: id =>
    service.get(`http://localhost:8080/api/country/${id}`),

  update: data =>
    service.update(`http://localhost:8080/api/country/${data.id}`, data),

  create: data =>
    service.create('http://localhost:8080/api/country/', data),

  delete: id =>
    service.delete(`http://localhost:8080/api/country/${id}`),
};

const vendorService = {
  getAll: () =>
    service.get("http://localhost:8080/api/vendor/"),

  create: data =>
    service.create('http://localhost:8080/api/vendor/', data),

  update: data =>
    service.update(`http://localhost:8080/api/vendor/${data.id}`, data),

  delete: id =>
    service.delete(`http://localhost:8080/api/vendor/${id}`),
};

const watchService = {
  getAll: () =>
    service.get("http://localhost:8080/api/watch/"),

  create: data =>
    service.create('http://localhost:8080/api/watch/', data),

  update: data =>
    service.update(`http://localhost:8080/api/watch/${data.id}`, data),

  delete: id =>
    service.delete(`http://localhost:8080/api/watch/${id}`),
};
