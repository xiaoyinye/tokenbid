const itemForm = document.getElementById('item-form');

itemForm.addEventListener('submit', (e) => {
    e.preventDefault();

    const data = new FormData(e.target);

    let values = {};
    values.userId = -1; // TODO: get userID
    values.title = data.get('title');
    values.description = data.get('description');
    values.category = data.get('category');

    let image = data.get('image');
    // TODO: add image to S3 bucket

    const request = new Request('/items/add', {
        method: 'POST',
        body: JSON.stringify(values),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    console.log(request);
})