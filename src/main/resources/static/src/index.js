const fetchRequests = require("./fetchRequests");
const s3 = require("./s3");
const addUser = fetchRequests.addUser;
const loginUser = fetchRequests.loginUser;
const addItem = fetchRequests.addItem;
const addAuction = fetchRequests.addAuction;
const addBid = fetchRequests.addBid;
const getUser = fetchRequests.getUser;
const getItem = fetchRequests.getItem;
const getAuction = fetchRequests.getAuction;
const getBid = fetchRequests.getBid;
const getItemsByCategory = fetchRequests.getItemsByCategory;
const getAllItems = fetchRequests.getAllItems;
const getAllAvailableItemsForUser = fetchRequests.getAllAvailableItemsForUser;
const getAllActiveAuctions = fetchRequests.getAllActiveAuctions;
const getHighestBid = fetchRequests.getHighestBid;
const updateUser = fetchRequests.updateUser;
const updateItem = fetchRequests.updateItem;
const updateAuction = fetchRequests.updateAuction;
const updateBid = fetchRequests.updateBid;

/////////////////////////////////////////////////////////////////////////////
// Event listeners, TODO change names when forms are added, add input validation
/////////////////////////////////////////////////////////////////////////////

/*
  Register a new user and redirect to /login.html if successful
*/
const registerForm = document.getElementById("register-form");
if (registerForm) {
  registerForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let user = {};
    user.firstName = data.get("firstName");
    user.lastName = data.get("lastName");
    user.username = data.get("username");
    user.email = data.get("email");
    user.password = data.get("password");
    user.tokens = 0;

    // Add new user to database
    let response = await addUser(user);
    if (response.ok) {
      console.log("User added!");
      window.location.href = "/login.html";
    } else if (response.status === 409) {
      let body = await response.text();
      alert(body);
    } else {
      console.log("Failed to add user");
    }
  });
}

/*
  Validate a user login and redirect to /explore.html if successful
*/
const loginForm = document.getElementById("login-form");
if (loginForm) {
  loginForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let user = {};
    user.username = data.get("username");
    user.password = data.get("password");
    console.log(user);

    // Add new user to database
    let response = await loginUser(user);
    console.log(response);

    if (response.ok) {
      let validatedUser = await response.json();
      console.log("User logged in!");
      console.log(validatedUser);
      window.sessionStorage.setItem("userId", validatedUser.userId);
      window.location.href = "/explore.html";
    } else {
      window.sessionStorage.removeItem("userId");
      if (response.status === 409)
        alert("Please verify your email");
      else
        alert("Invalid login credentials");
      console.log("Failed to log in user");
    }
  });
}

/*
  Logout the current user and redirect to /login.html
*/
const logOut = document.getElementById("logout");
if (logOut) {
  logOut.addEventListener("click", async function (e) {
    e.preventDefault();

    let userId = window.sessionStorage.getItem("userId");
    if (userId != null) {
      window.sessionStorage.removeItem("userId");
      console.log("Logout successful.");
      window.location.href = "/login.html";
    } else {
      console.log("Failed to logout");
    }
  });
}

/*
  Update a user's information and reload the page if successful
*/
const profileForm = document.getElementById("profile-form");
if (profileForm) {
  profileForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    let userId = window.sessionStorage.getItem("userId");
    if (userId === null) {
      window.location.href = "/login.html";
    }

    // Validate form input
    const data = new FormData(e.target);
    let firstName = data.get("firstName");
    let lastName = data.get("lastName");
    let password = data.get("password");
    let confirm = data.get("confirmPassword");

    let updatedUser = {};
    updatedUser.userId = userId;

    if (firstName || firstName.trim() !== "") {
      updatedUser.firstName = firstName.trim();
    } else updatedUser.firstName = null;

    if (lastName || lastName.trim() !== "") {
      updatedUser.lastName = lastName.trim();
    } else updatedUser.lastName = null;

    if (password || password !== "") {
      if (password !== confirm) {
        alert("Passwords do not match");
        return;
      }
      updatedUser.password = password;
    } else updatedUser.password = null;

    let response = await updateUser(updatedUser);
    if (response.ok) {
      console.log("User updated!");
      window.location.href = "/profile.html";
    } else {
      alert("Failed to update user");
    }
  });
}

/*
  Add a new item and redirect to /action.html if successful
*/
const itemForm = document.getElementById("item-form");
if (itemForm) {
  itemForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    let userId = window.sessionStorage.getItem("userId");
    if (userId === null) {
      window.location.href = "/login.html";
    }

    // Get the form values
    const data = new FormData(e.target);

    if (data.get("title") === "" || data.get("description") === "") {
      alert("Please fill out all fields");
      return;
    }

    let item = {};
    item.userId = userId;
    item.title = data.get("title");
    item.description = data.get("description");
    item.category = data.get("category");

    const image = document.getElementById("image").files[0];

    if (image === undefined) {
      alert("Please upload an image");
      return;
    }

    // Create a random image name
    const randomImageName =
      "image-" + Math.floor(Math.random() * 1000000) + ".png";

    // Get secured url to upload image to S3
    const url = await s3.generateUploadUrl(randomImageName);

    // Add image to S3 and item to database
    let uploadImageResponse = await s3.uploadImage(url, image);

    if (uploadImageResponse.ok) {
      item.imageUrl = url.split("?")[0];
    } else {
      console.log("Failed to upload image");
    }

    let response = await addItem(item);
    if (response.ok) {
      console.log("Item added!");
      window.location.href = "/action.html";
    } else {
      alert("Error adding new item");
      console.log("Failed to add item");
    }
  });
}

/*
  Start an auction for an item and redirect to /explore.html if successful
*/
const auctionForm = document.getElementById("auction-form");
if (auctionForm) {
  auctionForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let hours = Number(data.get("length")); // auction length in hours
    let start = new Date().getTime();
    let end = start + 1000 * 60 * 60 * hours;
    let auction = {};
    auction.itemId = data.get("itemId").split(" ")[0];
    auction.startingBid = data.get("startingBid");
    auction.startTime = new Date(start).toJSON();
    auction.endTime = new Date(end).toJSON();

    // Add new auction to database
    let response = await addAuction(auction);
    if (response.ok) {
      console.log("Auction added!");
      window.location.href = "/explore.html";
    } else {
      alert("Error adding auction");
      console.log("Failed to add auction");
    }
  });
}

/*
  Place a bid on a current auction and update the displayed current bid value if successful
*/
const bidForm = document.getElementById("bid-form");
if (bidForm) {
  bidForm.addEventListener("submit", async function (e) {
    e.preventDefault();

    let userId = window.sessionStorage.getItem("userId");
    if (userId === null) {
      window.location.href = "/login.html";
    }

    // Array of url query parameters
    const urlParams = new Proxy(new URLSearchParams(window.location.search), {
      get: (searchParams, prop) => searchParams.get(prop),
    });

    // Get the form values
    const data = new FormData(e.target);
    let bid = {};
    bid.userId = userId;
    bid.auctionId = urlParams.auctionId;
    bid.bid = data.get("bidAmount");

    // Add new bid to database
    let response = await addBid(bid);
    if (response.ok) {
      alert("Bid added!");
      let currentBidEle = document.getElementById("current-bid");
      if (currentBidEle)
        currentBidEle.textContent = "Current Bid: " + bid.bid + " tokens";
    } else if (response.status === 409) {
      let body = await response.text();
      alert(body);
    } else {
      console.log("Failed to add bid");
    }
  });
}

/*
  Add items belonging to the current user not currently in auction to the item selector on the item-form
*/
async function populateItemsSelector(itemsSelector) {
  let userId = window.sessionStorage.getItem("userId");

  if (userId === null) {
    window.location.href = "/login.html";
  }

  // Find the items not in an auction
  let availableItems = await getAllAvailableItemsForUser(userId);

  // Add available items to the select element
  itemsSelector.textContent = ""; // remove all children
  for (let i = 0; i < availableItems.length; i++) {
    let option = document.createElement("option");
    option.textContent =
      availableItems[i].itemId + " (" + availableItems[i].title + ")";
    itemsSelector.appendChild(option);
  }
}

/*
  Display currently ongoing auctions
*/
async function populateAuctionsContainer(auctionsContainer) {
  // Get current ongoing auctions
  let auctions = await getAllActiveAuctions();

  // Get information for each item in an ongoing auction
  let items = [];
  for (let i = 0; i < auctions.length; i++) {
    let item = await getItem(auctions[i].itemId);
    if (item) {
      item.associatedAuction = auctions[i];
      items.push(item);
    }
  }

  // Add each item to the auctions container
  auctionsContainer.textContent = ""; // remove all children
  for (let i = 0; i < items.length; i++) {
    let item = items[i];
    let card = this.document.createElement("div");
    card.classList.add("gallery");

    let imgEle = this.document.createElement("img");
    imgEle.src = item.imageUrl;
    imgEle.style.height = "180px";
    imgEle.style.width = "180px";
    imgEle.style.objectFit = "cover";
    card.appendChild(imgEle);

    let titleEle = this.document.createElement("p");
    titleEle.textContent = item.title;
    card.appendChild(titleEle);

    let descEle = this.document.createElement("p");
    descEle.textContent = item.description;
    card.appendChild(descEle);

    let categoryEle = this.document.createElement("p");
    categoryEle.textContent = "Category: " + item.category;
    card.appendChild(categoryEle);

    let timerEle = this.document.createElement("p");
    startCountdownTimer(item.associatedAuction.endTime, timerEle);
    card.appendChild(timerEle);

    let viewEle = this.document.createElement("button");
    viewEle.textContent = "View";
    viewEle.addEventListener("click", () => {
      this.window.location.href = "/auction.html?auctionId=" + item.associatedAuction.auctionId;
    });
    card.appendChild(viewEle);
    auctionsContainer.appendChild(card);
  }

  console.log(auctions);
  console.log(items);
}

/*
  Start a countdown timer on the display element with a specified end time
*/
function startCountdownTimer(endTime, display) {
  let startTime = Date.now();
  let end = Date.parse(endTime);
  let duration = Math.floor((end - startTime) / 1000);
  if (!endTime || duration < 0) {
    display.textContent = "00:00:00";
    return;
  }
  let diff, hours, minutes, seconds;
  function timer() {
    diff = duration - Math.floor(((Date.now() - startTime) / 1000));  // in seconds
    hours = Math.floor(diff / 3600);
    seconds = diff - hours*3600;
    minutes = Math.floor(seconds / 60);
    seconds = seconds - minutes*60;
    hours = hours < 10 ? "0" + hours : hours;
    minutes = minutes < 10 ? "0" + minutes : minutes;
    seconds = seconds < 10 ? "0" + seconds : seconds;
    display.textContent = hours + ":" + minutes + ":" + seconds;
  };
  timer();  // display immediately
  setInterval(timer, 1000);
}

/*
  Display auction details
*/
async function displayAuctionDetails(auctionId, detailContainer) {
  detailContainer.textContent = ""; // remove all children

  const auction = await getAuction(auctionId);
  if (!auction) return;
  console.log(auction.itemId);
  const item = await getItem(auction.itemId);
  if (!item) return;
  const highestBid = await getHighestBid(auctionId);
  console.log(highestBid);

  let imgEle = document.createElement("img");
  imgEle.src = item.imageUrl;
  imgEle.style.height = "180px";
  imgEle.style.width = "180px";
  imgEle.style.objectFit = "cover";
  detailContainer.appendChild(imgEle);

  let titleEle = document.createElement("div");
  titleEle.classList.add("desc");
  titleEle.textContent = item.title;
  detailContainer.appendChild(titleEle);

  let descEle = document.createElement("div");
  descEle.classList.add("desc");
  descEle.textContent = item.description;
  detailContainer.appendChild(descEle);

  let currentBidEle = document.createElement("div");
  currentBidEle.classList.add("desc");
  currentBidEle.id = "current-bid";
  let bid = highestBid ? highestBid.bid : auction.startingBid;
  currentBidEle.textContent = "Current Bid: " + bid + " tokens";
  detailContainer.appendChild(currentBidEle);

  let timeLeftEle = document.createElement("div");
  timeLeftEle.classList.add("desc");
  timeLeftEle.id = "time-left";
  console.log(auction.endTime);
  startCountdownTimer(auction.endTime, timeLeftEle);
  // let end = new Date(auction.endTime);
  // let timeLeft = end.getHours() - new Date().getHours();
  // if (timeLeft <= 1) {
  //   let minutes = Math.floor((end.getTime() - new Date().getTime()) / 60000);
  //   timeLeftEle.textContent = "Time Left: " + minutes + " minutes";
  // } else {
  //   timeLeftEle.textContent = "Time Left: " + timeLeft + " hours";
  // }

  detailContainer.appendChild(timeLeftEle);
}

/*
  Display user's details
*/
async function displayProfileInformation(profileContainer) {
  const userId = sessionStorage.getItem("userId");
  if (userId === null) {
    window.location.href = "/login.html";
  }

  let firstNameEle = document.getElementById("profile-first-name");
  let lastNameEle = document.getElementById("profile-last-name");
  let emailEle = document.getElementById("profile-email");
  let usernameEle = document.getElementById("profile-username");
  let tokensEle = document.getElementById("profile-tokens");

  // Remove current information
  if (firstNameEle) firstNameEle.textContent = "";
  if (lastNameEle) lastNameEle.textContent = "";
  if (emailEle) emailEle.textContent = "";
  if (usernameEle) usernameEle.textContent = "";
  if (tokensEle) tokensEle.textContent = "";

  const user = await getUser(userId);
  if (!user) return;

  if (firstNameEle) firstNameEle.textContent = user.firstName;
  if (lastNameEle) lastNameEle.textContent = user.lastName;
  if (emailEle) emailEle.textContent = user.email;
  if (usernameEle) usernameEle.textContent = user.username;
  if (tokensEle) tokensEle.textContent = user.tokens;
}

/*
  Actions to take on page load
*/
window.addEventListener("DOMContentLoaded", async function (e) {
  // Array of url query parameters
  const urlParams = new Proxy(new URLSearchParams(window.location.search), {
    get: (searchParams, prop) => searchParams.get(prop),
  });

  // On explore.html display current items for auction
  const auctionsContainer = this.document.getElementById("auctions-container");
  if (auctionsContainer) populateAuctionsContainer(auctionsContainer);

  // On auction.html display auction details
  const detailContainer = this.document.getElementById("item-detail-container");
  if (detailContainer)
    displayAuctionDetails(urlParams.auctionId, detailContainer);

  // On action.html display items available for auction
  const itemsSelector = this.document.getElementById("auction-form-items");
  if (itemsSelector) populateItemsSelector(itemsSelector);

  // On profile.html display profile information
  const profileContainer = this.document.getElementById("profile-container");
  if (profileContainer) displayProfileInformation(profileContainer);
});
