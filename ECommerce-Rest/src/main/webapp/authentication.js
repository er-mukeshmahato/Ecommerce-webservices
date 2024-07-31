document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');
    const messageDiv = document.getElementById('messageDiv');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const username = document.getElementById('txtUsername').value;
        const password = document.getElementById('txtpassword').value;

        // Construct the user data object
        const userData = {
            email: username,
            password: password
        };

        // Send login request to the server
        fetch('/ECommerce-Rest/resources/authentication/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.message === "Login successful") {
                // Redirect or display success message
                window.location.href = 'index.html'; // Redirect to another page
            } else {
                // Display error message
                messageDiv.textContent = data.message;
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again.';
        });
    });
});
