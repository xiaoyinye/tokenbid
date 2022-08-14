async function addUser(user) {
    fetch(new Request('/auctions/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    }))
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        // Display user added success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display user added failure message
    });
}

async function addItem(item) {
    fetch(new Request('/items/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(item)
    }))
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        // Display item added success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display item added failure message
    });
}

async function addAuction(auction) {
    fetch(new Request('/auctions/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(auction)
    }))
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        // Display auction added success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display auction added failure message
    });
}

async function addBid(bid) {
    fetch(new Request('/bids/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(bid)
    }))
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        // Display bid added success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display bid added failure message
    });
}

async function addImage(image) {
    // TODO add image to S3 bucket
}

//////////////////////////////////////////////////////////
// Event listeners, TODO change when forms are added
//////////////////////////////////////////////////////////

document.getElementById('user-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let user = {};
    user.firstName = data.get('firstName');
    user.lastName = data.get('lastName');
    user.username = data.get('username');
    user.email = data.get('email');
    user.password = data.get('password');
    user.tokens = 0;

    // Add new user to database
    await addUser(user);
});

document.getElementById('item-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let image = data.get('image');
    let item = {};
    item.userId = -1; // TODO: get userID from cache
    item.title = data.get('title');
    item.description = data.get('description');
    item.category = data.get('category');

    // Add new item to database and image to S3 bucket
    await addItem(item);
    await addImage(image);
});

document.getElementById('auction-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let hours = Number(data.get('length')); // auction length in hours
    let start = new Date().getTime();
    let end = start + (1000 * 60 * 60 * hours);
    let auction = {};
    auction.itemId = data.get('itemId');
    auction.startingBid = data.get('startingBid');
    auction.startTime = new Date(start).toJSON();
    auction.endTime = new Date(end).toJSON();

    // Add new auction to database
    await addAuction(auction);
});

document.getElementById('bid-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let bid = {};
    bid.auctionId = -1; // TODO: get auctionId from URL
    bid.bid = data.get('bid');

    // Add new bid to database
    await addBid(bid);
});