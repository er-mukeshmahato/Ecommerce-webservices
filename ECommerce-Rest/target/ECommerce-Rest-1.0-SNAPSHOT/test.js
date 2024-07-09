$(document).ready(function () {
    // Function to fetch colors data from the web service
    function fetchColors() {
        $.ajax({
            url: "resources/test", // Endpoint to fetch JSON data from
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
    function displayProducts(data) {
        // Clear previous results
        $("#colorsList").empty();

        // Iterate over each product in the JSON array
        data.forEach(function (product) {
            // Create HTML string for product card
            var productCardHtml =
                '<div class="product-card" style="width: 300px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); overflow: hidden;">' +
                '<div class="product-image" style="padding: 16px;">' +
                '<img src="product-image.jpg" alt="Product Image" style="width: 100%; height: auto; display: block;">' +
                '</div>' +
                '<div class="product-info" style="padding: 16px;">' +
                '<h2 class="product-title" style="font-size: 20px; font-weight: bold; color: #333333; margin-bottom: 8px;">' + product.productname + '</h2>' +
                '<p class="product-description" style="font-size: 14px; color: #666666; margin-bottom: 12px;">' + product.description + '</p>' +
                '<div class="product-price" style="font-size: 18px; font-weight: bold; color: #333333; margin-bottom: 12px;">$XX.XX</div>' +
                '<button class="btn btn-primary" style="display: inline-block; background-color: #007bff; color: white; padding: 8px 16px; text-align: center; text-decoration: none; border: none; border-radius: 4px; cursor: pointer; font-size: 14px;">Add to Cart</button>' +
                '</div>' +
                '</div>';

            // Append product card HTML to the #colorsList element
            $("#colorsList").append(productCardHtml);
        });
    }

    // Fetch products data when the page is ready
    fetchColors();
});
