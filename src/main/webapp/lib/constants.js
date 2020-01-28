// planes
const planes = [
  document.getElementById('customers-plane'),
  document.getElementById('countries-plane'),
  document.getElementById('vendors-plane'),
  document.getElementById('watches-plane'),
  document.getElementById('orders-plane'),
];

// menu items
const menuItems = [
  document.getElementById('item-customers'),
  document.getElementById('item-countries'),
  document.getElementById('item-vendors'),
  document.getElementById('item-watches'),
  document.getElementById('item-orders'),
];

const storage = {
  customerId: -1,
  //
  customers: [],
  countries: [],
  vendors: [],
  watches: [],
  orders: [],
  items: [],
  //
  selectedCustomer: -1,
  selectedCountry: -1,
  selectedVendor: -1,
  selectedWatch: -1,
  selectedOrder: -1,
};