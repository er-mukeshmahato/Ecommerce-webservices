$(document).ready(function () {

// Function to fetch products data from the web service
    function fetchProducts() {
        $.ajax({
            url: "resources/product", // Endpoint to fetch JSON data from
            type: "GET",
            success: function (data) {
                // Process the JSON response
                displayProducts(data);
            },
            error: function (xhr, status, error) {
                console.error("Error fetching products:", status, error);
            }
        });
    }

// Function to display products in the UI
// Function to display products in the UI
    function displayProducts(products) {
// Clear previous results
        $("#product-list").empty();
        let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
        // Limit to first 6 products
        products.slice(0, 6).forEach(function (product) {
// Check if the product is already in the cart
            const productInCart = cartItems.some(item => item.productId === product.productId);
            
            console.log('Product:', product);
            console.log('Product in Cart:', productInCart); // Log productInCart to debug

            // Create HTML string for product card
            let productCardHtml = `
            <div class="ta-pricecartwrapper" productid="${product.productId}">
                <div class="ta-photo-product">
                    <a>
                        <img src="./image/product/${product.imageUrl}" />
                    </a>
                </div>
                <div class="ta-priceofproduct">
                    <div class="ta-now-price">${product.price}</div>
                </div>
                <div class="ta-productdetails">
                    <div class="ta-titleof-product">${product.productname}</div>
                </div>
                <div class="ta-cart-wishlist">
                    <div class="ta-cartbox btnAddtoCard">${productInCart ? 'Added to Cart' : 'Add to Cart'}</div>
                    <div class="ta-wishlist">Buy Now</div>
                </div>
            </div>
        `;
            // Append product card HTML to the #product-list element
            $("#product-list").append(productCardHtml);
            // After displaying products, attach event listeners to Add to Cart buttons
            attachAddToCartListeners();
        });
    }
    // Function to attach click event listeners to Add to Cart buttons
    function attachAddToCartListeners() {
        $('.btnAddtoCard').off('click').on('click', function () {
            // Retrieve product details
            const productWrapper = $(this).closest('.ta-pricecartwrapper'); // Get parent container
            const productId = productWrapper.attr('productid');
            const productImage = productWrapper.find('img').attr('src');
            const productPrice = parseFloat(productWrapper.find('.ta-now-price').text().trim().replace('¥', ''));
            const productTitle = productWrapper.find('.ta-titleof-product').text().trim();
            // Create a product object
            const product = {
                productId: productId,
                image: productImage,
                title: productTitle,
                price: productPrice,
                quantity: 1 // Default quantity (can be modified)
            };
            // Retrieve existing cart items from local storage or initialize empty array
            let cartItems = JSON.parse(localStorage.getItem('cartItems')) || [];
            // Check if the product is already in the cart
            if (CheckProductInCart(cartItems, productId)) {
                return; // Exit function if product is already in cart
            }

            // Add the new product to the cart items array
            cartItems.push(product);
            // Update cart count displayed
            setCartCount(cartItems);
            // Change button text to indicate product has been added to cart
            changeCartButtonText(productWrapper);
            // Save updated cart items back to local storage
            localStorage.setItem('cartItems', JSON.stringify(cartItems));
            // Example: Display an alert or update UI
            alert(`${productTitle} added to cart!`);
        });
    }

    function CheckProductInCart(cartItems, productId) {
        const productInCart = cartItems.find(item => item.productId === productId);
        if (productInCart) {
            // Product already in cart
            alert(`${productInCart.title} is already in your cart!`);
            return true; // Product found in cart
        }
        return false; // Product not found in cart
    }

    function changeCartButtonText(productWrapper) {
        const btn = productWrapper.find('.btnAddtoCard');
        if (btn.length > 0) {
            btn.text('Added to Cart');
        } else {
            console.error('Button with class "btnAddtoCard" not found in productWrapper:', productWrapper);
        }
    }

    function setCartCount(items) {
        $("#cartNumber").text(items.length);
        console.log(items.length);
    }

    // Call fetchProducts() when the page is ready to load products
    fetchProducts();
});
