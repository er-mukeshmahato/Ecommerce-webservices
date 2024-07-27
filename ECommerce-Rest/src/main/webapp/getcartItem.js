document.addEventListener("DOMContentLoaded", function () {
    let cartItems = JSON.parse(localStorage.getItem("cartItems")) || [];

    // Function to render cart items
    function renderCartItems() {
        const cartContainer = document.querySelector(".ta-my-cart-wrapper");
        cartContainer.innerHTML = ""; // Clear existing content

        cartItems.forEach((item) => {
            const cartItem = document.createElement("div");
            cartItem.classList.add("ta-my-cart-item");
            cartItem.innerHTML = `
                <div class="ta-my-cart-img">
                    <img src="${item.image}" />
                </div>
                <div class="ta-pBody">
                    <div class="pName">${item.title}</div>
                    <div class="pQty">               
                        <div class="quantity-controls">
                            <i class="fa-solid fa-minus"></i>
                            <input type="text" class="quantity" value="${item.quantity}" min="1">
                            <i class="fa-solid fa-plus"></i>
                        </div>
                    </div>
                    <div class="pPrice">${item.price}</div>
                </div>
                <div class="my-remove">
                    <button class="remove-item"><i class="fa-solid fa-trash"></i></button>
                </div>
            `;
            cartContainer.appendChild(cartItem);

            // Attach event listeners for quantity controls
            const quantityInput = cartItem.querySelector('.quantity');
            const minusButton = cartItem.querySelector('.fa-minus');
            const plusButton = cartItem.querySelector('.fa-plus');
            const priceElement = cartItem.querySelector('.pPrice'); // Get the price element for this item
            updatePrice(priceElement);

            minusButton.addEventListener('click', function () {
                if (parseInt(quantityInput.value) > 1) {
                    quantityInput.value = parseInt(quantityInput.value) - 1;
                    updateCartItem(item, parseInt(quantityInput.value, 10));
                    updatePrice(priceElement); // Pass the price element to updatePrice
                }
            });

            plusButton.addEventListener('click', function () {
                quantityInput.value = parseInt(quantityInput.value) + 1;
                updateCartItem(item, parseInt(quantityInput.value, 10));
                updatePrice(priceElement); // Pass the price element to updatePrice
            });

            quantityInput.addEventListener('change', function () {
                const newQuantity = parseInt(quantityInput.value, 10);
                if (newQuantity >= 1) {
                    updateCartItem(item, newQuantity);
                    updatePrice(priceElement); // Pass the price element to updatePrice
                } else {
                    quantityInput.value = item.quantity; // Restore previous value if invalid input
                }
            });

            // Attach event listener for remove button
            const removeButton = cartItem.querySelector('.remove-item');
            removeButton.addEventListener('click', function () {
                removeCartItem(item);
            });
        });

        updateTotalPrice(); // Update the total price displayed in UI
    }

    // Function to update cart item quantity
    function updateCartItem(item, newQuantity) {
        item.quantity = newQuantity;
        localStorage.setItem('cartItems', JSON.stringify(cartItems));
        updateTotalPrice(); // Update the total price displayed in UI
    }

    // Function to remove cart item
    function removeCartItem(item) {
        const index = cartItems.indexOf(item);
        if (index !== -1) {
            cartItems.splice(index, 1);
            localStorage.setItem('cartItems', JSON.stringify(cartItems));
            renderCartItems(); // Re-render cart items after removal
            updateTotalPrice(); // Update the total price displayed in UI
        }
    }

    // Function to calculate total price of items in cart
    function calculateTotalPrice() {
        let totalPrice = 0;
        cartItems.forEach(item => {
            price=item.price.trim().replace('¥', '');
            totalPrice += price * item.quantity;
        });
        return totalPrice;
    }

    // Function to update total price displayed in UI
    function updateTotalPrice() {
        const totalAmountElement = document.getElementById('totalAmtInCart');
        if (totalAmountElement) {
            const totalCartPrice = calculateTotalPrice();
            totalAmountElement.textContent = `Total: ¥ ${totalCartPrice.toFixed(2)}`;
        }
    }

    // Function to update individual item prices displayed in UI
    function updatePrice(priceElement) {
        const itemIndex = Array.from(priceElement.parentNode.parentNode.parentNode.children).indexOf(priceElement.parentNode.parentNode); // Get the index of the item
        const totalPrice = (cartItems[itemIndex].price.trim().replace('¥', '') * cartItems[itemIndex].quantity).toFixed(2); // Calculate total price for the specific item
        priceElement.textContent = `¥ ${totalPrice}`; // Update the price element's text content
    }

    // Initial rendering of cart items
    renderCartItems();
});
