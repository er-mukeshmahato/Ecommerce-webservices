document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('loginForm');
    const messageDiv = document.getElementById('messageDiv');

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const username = document.getElementById('txtUsername').value.trim();
        const password = document.getElementById('txtpassword').value.trim();

        // Basic validation
        if (!username || !password) {
            messageDiv.textContent = 'Please enter both username and password.';
            return;
        }

        // Construct the user data object
        const userData = {
            email: username,
            password: password
        };

        // Send login request to the server
        fetch('http://localhost:9090/login-services/resources/authentication/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.message === "Login successful") {
                // Set a session cookie
                setCookie('session_id', data.token, 1); // Set cookie for 1 day

                // Store user data in local storage
                localStorage.setItem('user', JSON.stringify({
                    email: username,
                    token: data.token
                }));

                // Update UI to show logged-in state
                updateUIOnLogin(username);

                // Redirect to another page
                window.location.href = 'index.html';
            } else {
                // Display error message
                messageDiv.textContent = data.message || 'Login failed. Please check your credentials.';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            messageDiv.textContent = 'An error occurred. Please try again later.';
        });
    });

    // Function to set a cookie
    function setCookie(name, value, days) {
        let expires = "";
        if (days) {
            const date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    }

    // Function to get a cookie
    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
    }

    // Function to update UI when user is logged in
    function updateUIOnLogin(username) {
        // Example: Show a welcome message or update navigation
        document.getElementById('welcomeMessage').textContent = `Welcome, ${username}!`;
        document.getElementById('loginSection').style.display = 'none'; // Hide login form
        document.getElementById('logoutSection').style.display = 'block'; // Show logout button
    }
});
