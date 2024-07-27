document.addEventListener('DOMContentLoaded', function() {
    // Find all elements with class "btnAddtoCard"
    const addToCartButtons = document.querySelectorAll('.btnAddtoCard');

    // Loop through each button and attach a click event listener
    addToCartButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Retrieve product details
            const productWrapper = button.closest('.ta-pricecartwrapper'); // Get parent container
            const productImage = productWrapper.querySelector('img').getAttribute('src');
            const productPriceElement = productWrapper.querySelector('.ta-now-price');
            let productPrice = productPriceElement ? productPriceElement.textContent.trim() : '';
            productPrice = productPrice.replace('', '').trim(); // Remove "Rs." and any extra spaces
          
            
            const productTitle = productWrapper.querySelector('.ta-titleof-product').textContent.trim();

            // Create a product object
            const product = {
                image: productImage,
                title: productTitle,
                price: productPrice,
                quantity: 1 // You can set default quantity here
            };

            // Retrieve existing cart items from local storage or initialize empty array
            let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];

            // Add the new product to the cart items array
            cartItems.push(product);

            // Save updated cart items back to local storage
            localStorage.setItem('cartItems', JSON.stringify(cartItems));

            // Example: Display an alert or update UI
            alert(`${productTitle} added to cart!`);
        });
    });
});
