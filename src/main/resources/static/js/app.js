// S3 bucket configuration
var bucketName = 'tokenbid';
var bucketRegion = 'US East (N. Virginia) us-east-1';
var IdentityPoolId = 'us-east-1:ff5fa4fc-7bd5-427c-ae5a-44f6801508f7';

AWS.config.update({
    region: bucketRegion,
    credentials: new AWS.CognitoIdentityCredentials({
        IdentityPoolId: IdentityPoolId
    })
});

var s3 = new AWS.S3({
    apiVersion: '2006-03-01',
    params: {Bucket: bucketName}
});


// Utility function to perform a POST request, returns a promise
async function fetchPOST(path, body) {
    return await fetch(new Request(path, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }));
}

// Utility function to return a PUT request, returns a promise
async function fetchPUT(path, body) {
    return await fetch(new Request(path, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }));
}

// Add data to database
async function addUser(user) {
    fetchPOST('/users/add', user)
    .then((response) => response || response.json())
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
    fetchPOST('/items/add', item)
    .then((response) => response || response.json())
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
    fetchPOST('/auctions/add', auction)
    .then((response) => response || response.json())
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
    fetchPOST('/bids/add', bid)
    .then((response) => response || response.json())
    .then((data) => {
        console.log(data);
        // Display bid added success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display bid added failure message
    });
}

// TODO: Add image to S3 bucket
async function addImage(image) {
    let filePath = 'images/' + image.name;
    let fileUrl = 'https://' + bucketRegion + '.amazonaws.com/tokenbid/' + filePath;
    s3.upload({        
        Key: filePath,
        Body: file,
        ACL: 'public-read'
        }, function(err, data) {
            if(err) {
                reject('error');
            }
            alert('Successfully Uploaded!');
        }).on('httpUploadProgress', function (progress) {
        var uploaded = parseInt((progress.loaded * 100) / progress.total);
        $("progress").attr('value', uploaded);
    });
}

// Update data on database
async function updateUser(user) {
    fetchPUT('/users/' + user.Id, user)
    .then((response) => response || response.json())
    .then((data) => {
        console.log(data);
        // Display user updated success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display user updated failure message
    });
}
async function updateItem(item) {
    fetchPUT('/items/' + item.Id, item)
    .then((response) => response || response.json())
    .then((data) => {
        console.log(data);
        // Display item updated success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display item updated failure message
    });
}
async function updateAuction(auction) {
    fetchPUT('/auctions/' + auction.Id, auction)
    .then((response) => response || response.json())
    .then((data) => {
        console.log(data);
        // Display auction updated success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display auction updated failure message
    });
}
async function updateBid(bid) {
    fetchPUT('/bids/' + bid.Id, bid)
    .then((response) => response || response.json())
    .then((data) => {
        console.log(data);
        // Display bid updated success message
    })
    .catch((error) => {
        console.error("Error:", error);
        // Display bid updated failure message
    });
}

// TODO: Update image on S3 bucket
async function updateImage(image) {

}

/////////////////////////////////////////////////////////////////////////////
// Event listeners, TODO change names when forms are added, add input validation
/////////////////////////////////////////////////////////////////////////////

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
    item.userId = 1; // TODO: get userID from cache
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

    // TODO: Start a timer if auction was successfully added
});

document.getElementById('bid-form').addEventListener('submit', async function(e) {
    e.preventDefault();

    // Get the form values
    const data = new FormData(e.target);
    let bid = {};
    bid.userId = 1;     // TODO: get userId from cache
    bid.auctionId = 7;  // TODO: get auctionId from URL
    bid.bid = data.get('bid');

    // Add new bid to database
    await addBid(bid);

    // TODO: Check if the bid is high enough before submitting
});