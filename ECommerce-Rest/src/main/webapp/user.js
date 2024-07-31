document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const btnRegister = document.getElementById('btnRegister');
    const btnCancel = document.getElementById('btnCancel');
    const messageDiv = document.getElementById('message'); // Assume you have a div for messages

    btnRegister.addEventListener('click', function() {
        // Prevent form submission if using default form submit button
        event.preventDefault();
        
        // Capture form data
        const firstName = document.getElementById('txtFName').value.trim();
        const lastName = document.getElementById('txtLName').value.trim();
        const username = document.getElementById('txtUsername').value.trim();
        const mobile = document.getElementById('txtMobile').value.trim();
        const password = document.getElementById('txtPassword').value;
        const confirmPassword = document.getElementById('txtConfirmPassword').value;

        // Basic validation
        if (!firstName || !lastName || !username || !mobile || !password || !confirmPassword) {
            messageDiv.textContent = 'All fields are required.';
            return;
        }

        if (password !== confirmPassword) {
            messageDiv.textContent = 'Passwords do not match.';
            return;
        }

        // Create user data object
        const userData = {
            firstName: firstName,
            lastName: lastName,
            username: username,
            mobile: mobile,
            password: password
        };

        // Send registration request to the server
        fetch('/ECommerce-Rest/resources/user/register', { // Adjust the endpoint as needed
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.message === 'Registration successful') {
                messageDiv.textContent = 'Registration successful. Redirecting...';
                setTimeout(() => {
                    window.location.href = '/login'; // Redirect to login page or another page
                }, 2000);
            } else {
                messageDiv.textContent = data.message || 'An error occurred.';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again.';
        });
    });

    btnCancel.addEventListener('click', function() {
        // Handle cancel button (e.g., go back to the previous page)
        window.history.back(); // Navigate back to the previous page
    });
});
