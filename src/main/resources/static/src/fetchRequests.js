// Utility function to perform an HTTPRequest, returns a promise with an HTTPResponse
async function sendRequest(method, path, body) {
  return await fetch(
    new Request(path, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    })
  );
}

// Add data to database
async function addUser(user) {
  return await sendRequest('POST', '/users/add', user);
}

async function loginUser(user) {
  return await sendRequest('POST', '/users/login', user);
}

async function addItem(item) {
  return await sendRequest('POST', '/items/add', item);
}

async function addAuction(auction) {
  return await sendRequest('POST', '/auctions/add', auction);
}

async function addBid(bid) {
  return await sendRequest('POST', '/bids/add', bid);
}

// Get data from the database
async function getUser(userId) {
  const response = await sendRequest('GET', '/users/' + userId);
  if (!response.ok) return null;
  return response.json();
}

async function getItem(itemId) {
  const response = await sendRequest('GET', '/items/' + itemId);
  if (!response.ok) return null;
  return response.json();
}

async function getAuction(auctionId) {
  const response = await sendRequest('GET', '/auctions/' + auctionId);
  if (!response.ok) return null;
  return response.json();
}

async function getAllActiveAuctions() {
  const response = await sendRequest('GET', '/auctions/active');
  if (!response.ok) return null;
  return response.json();
}

async function getHighestBid(auctionId) {
  const response = await sendRequest('GET', '/bids/highest-bid/' + auctionId);
  if (!response.ok) return null;
  return response.json();
}

async function getAllAvailableItemsForUser(userId) {
  const response = await sendRequest('GET', '/items/' + userId + '/available');
  if (!response.ok) return null;
  return response.json();
}

// Update data on database
async function updateUser(user) {
  console.log(user);
  return await sendRequest('PUT', '/users/' + user.userId, user);
}

async function addTokens(userId, tokens) {
  return await sendRequest('PUT', '/users/' + userId + '/add-token', tokens);
}

module.exports = {
  addUser,
  loginUser,
  addItem,
  addAuction,
  addBid,
  getUser,
  getItem,
  getAuction,
  getAllAvailableItemsForUser,
  getAllActiveAuctions,
  getHighestBid,
  updateUser,
  addTokens,
};
