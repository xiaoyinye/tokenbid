// // S3 bucket configuration
// var bucketName = 'tokenbid';
// var bucketRegion = 'US East (N. Virginia) us-east-1';
// var IdentityPoolId = 'us-east-1:ff5fa4fc-7bd5-427c-ae5a-44f6801508f7';

// AWS.config.update({
//     region: bucketRegion,
//     credentials: new AWS.CognitoIdentityCredentials({
//         IdentityPoolId: IdentityPoolId
//     })
// });

// var s3 = new AWS.S3({
//     apiVersion: '2006-03-01',
//     params: {Bucket: bucketName}
// });

// // TODO: Add image to S3 bucket
// async function addImage(image) {
//     let filePath = 'images/' + image.name;
//     let fileUrl = 'https://' + bucketRegion + '.amazonaws.com/tokenbid/' + filePath;
//     s3.upload({        
//         Key: filePath,
//         Body: file,
//         ACL: 'public-read'
//         }, function(err, data) {
//             if(err) {
//                 reject('error');
//             }
//             alert('Successfully Uploaded!');
//         }).on('httpUploadProgress', function (progress) {
//         var uploaded = parseInt((progress.loaded * 100) / progress.total);
//         $("progress").attr('value', uploaded);
//     });
// }

// // TODO: Update image on S3 bucket
// async function updateImage(image) {

// }

// Utility function to perform an HTTPRequest, returns a promsie
async function sendRequest(method, path, body) {
    return await fetch(new Request(path, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    }));
}

// Add data to database
async function addUser(user) {
    return await sendRequest('POST', '/users/add', user);
}
async function addItem(item) {
    return await sendRequest('POST', 'items/add', item);
}
async function addAuction(auction) {
    return await sendRequest('POST', '/auctions/add', auction);
}
async function addBid(bid) {
    return await sendRequest('POST', '/bids/add', bid);
}

// Update data on database
async function updateUser(user) {
    return await sendRequest('PUT', '/users/' + user.Id, user);
}
async function updateItem(item) {
    return await sendRequest('PUT', '/items/' + item.Id, item);
}
async function updateAuction(auction) {
    return await sendRequest('PUT', '/auctions/' + auction.Id, auction);
}
async function updateBid(bid) {
    return await sendRequest('PUT', '/bids/' + bid.Id, bid);
}

/////////////////////////////////////////////////////////////////////////////
// Event listeners, TODO change names when forms are added, add input validation
/////////////////////////////////////////////////////////////////////////////

const userForm = document.getElementById('user-form');
if (userForm) {
    userForm.addEventListener('submit', async function(e) {
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
        let response = await addUser(user);
        console.log(response);
        if (response.ok) {
            console.log("User added!");
        } else {
            console.log("Failed to add user");
        }
    });
}

const itemForm = document.getElementById('item-form');
if (itemForm) {
    itemForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Get the form values
        const data = new FormData(e.target);
        
        let item = {};
        item.userId = 1; // TODO: get userID from cache
        item.title = data.get('title');
        item.description = data.get('description');
        item.category = data.get('category');

        // Add new item to database and image to S3 bucket
        let response = await addItem(item);
        if (response.ok) {
            console.log("Item added!");
        } else {
            console.log("Failed to add item");
        }

        //let image = data.get('image');
        //await addImage(image);
    });
}

const auctionForm = document.getElementById('auction-form');
if (auctionForm) {
    auctionForm.addEventListener('submit', async function(e) {
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
        let response = await addAuction(auction);
        if (response.ok) {
            console.log("Auction added!");

            // TODO: Start a timer if auction was successfully added

        } else {
            console.log("Failed to add auction");
        }
    });
}

const bidForm = document.getElementById('bid-form');
if (bidForm) {
    bidForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        // Get the form values
        const data = new FormData(e.target);
        let bid = {};
        bid.userId = 1;     // TODO: get userId from cache
        bid.auctionId = 7;  // TODO: get auctionId from URL
        bid.bid = data.get('bid');

        // Add new bid to database
        let response = await addBid(bid);
        if (response.ok) {
            console.log("Bid added!");
        } else {
            console.log("Failed to add bid");
        }
    });
}