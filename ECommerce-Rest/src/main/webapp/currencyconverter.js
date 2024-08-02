// Create and style CSS
const style = document.createElement('style');
style.textContent = `
    body {
        font-family: Arial, sans-serif;
    }

    /* Currency Icon */
    #currency-icon {
        position: fixed;
        bottom: 20px;
        right: 30px;
        cursor: pointer;
        z-index: 10000; /* Ensure it's on top */  
        height: 60px;
        width: 60px;
        border-radius: 50%;
        background: #b39eeb;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    #currency-icon img {
        width: 40px;
        height: 40px;
    }

    /* Currency Converter Popup */
    #currency-converter {
        position: fixed;
        bottom: 90px;
        right: 33px;
        width: 354px;
        height: 341px;
        padding: 25px;
        background-color: #5c71c4;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        z-index: 9999; /* Ensure it's on top of other elements */
    }

    #currency-converter.hidden {
        display: none;
    }

    #currency-converter h3 {
        margin-top: 0;
        color: white; /* Ensuring text is visible on a colored background */
    }

    #currency-converter select {
        height: 40px;
    }

    #currency-converter button {
        width: 40%;
        height: 35px;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 8px;
    }

    #currency-converter label,
    #currency-converter select,
    #currency-converter input {
        display: block;
        width: 100%;
        margin-bottom: 10px;
    }

    #conversion-result {
        margin-top: 10px;
        font-weight: bold;
        color: black; /* Ensuring text is visible on a colored background */
    }

    .input {
        width: 100%;
        padding: 8px;
        margin-top: 5px;
    }

    .ta-lg-icon i {
        font-size: 24px; /* Adjust size as needed */
    }

    /* Close Button */
    #close-button {
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;
        color: white;
        font-size: 20px;
    }
`;
document.head.appendChild(style);

// JavaScript functionality
document.addEventListener('DOMContentLoaded', () => {
    const currencyData = {
    "USD": 1,
    "EUR": 0.85,
    "GBP": 0.75,
    "JPY": 110.0,
    "CAD": 1.25,
    "AUD": 1.4,
    "CHF": 0.93,
    "CNY": 6.9,
    "NZD": 1.5,
    "SGD": 1.35,
    "MXN": 18.0,
    "INR": 82.0,
    "BRL": 5.0,
    "ZAR": 19.0,
    "KRW": 1300.0,
    "TRY": 26.0,
    "SEK": 9.2,
    "NOK": 9.1,
    "DKK": 6.3,
    "HUF": 350.0,
    "PLN": 4.0
   
};

    // Create and append the currency icon
    const currencyIcon = document.createElement('div');
    currencyIcon.id = 'currency-icon';
    currencyIcon.onclick = toggleConverter;
    currencyIcon.innerHTML = '<div class="ta-lg-icon"><i class="fas fa-coins"></i></div>';
    document.body.appendChild(currencyIcon);

    // Create and append the currency converter popup
    const currencyConverter = document.createElement('div');
    currencyConverter.id = 'currency-converter';
    currencyConverter.classList.add('hidden');
    currencyConverter.innerHTML = `
        <div id="close-button">✖</div>
        <h3>Currency Converter</h3>
        <div class="ta-stogrp-form">
            <input type="number" id="amount" name="amount" placeholder="Enter Amount" class="input">
        </div>
        <label for="from-currency">From:</label>
        <select id="from-currency">
            <!-- Options will be populated by JavaScript -->
        </select>
        <label for="to-currency">To:</label>
        <select id="to-currency">
            <!-- Options will be populated by JavaScript -->
        </select>
        <button id="convert-button">Convert</button>
        <p id="conversion-result"></p>
    `;
    document.body.appendChild(currencyConverter);

    // Populate currency options
    function populateCurrencyOptions() {
        const fromCurrencySelect = document.getElementById('from-currency');
        const toCurrencySelect = document.getElementById('to-currency');

        for (const currency in currencyData) {
            const optionFrom = document.createElement('option');
            optionFrom.value = currency;
            optionFrom.textContent = currency;
            fromCurrencySelect.appendChild(optionFrom);

            const optionTo = document.createElement('option');
            optionTo.value = currency;
            optionTo.textContent = currency;
            toCurrencySelect.appendChild(optionTo);
        }
    }

    // Toggle currency converter visibility
    function toggleConverter() {
        const converter = document.getElementById('currency-converter');
        if (converter) {
            converter.classList.toggle('hidden');
        }
    }

    // Convert currency and display the result
  async function convertCurrency() {
    const amount = parseFloat(document.getElementById('amount').value);
    const fromCurrency = document.getElementById('from-currency').value;
    const toCurrency = document.getElementById('to-currency').value;

    if (!amount || !fromCurrency || !toCurrency) {
        alert("Please fill out all fields");
        return;
    }

    try {
        // Create the request payload
        const payload = {
            fromCurrency: fromCurrency,
            toCurrency: toCurrency
        };

        // Send POST request to the server
        const response = await fetch('http://localhost:9090/currency-services/resources/currency', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        // Check if the response is ok
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        // Parse the response
        const data = await response.json();

        // Extract the conversion rate from the response data
        const conversionRate = data.data[toCurrency];
        if (conversionRate !== undefined) {
            const result = amount * conversionRate;
            document.getElementById('conversion-result').textContent = `Result: ${result.toFixed(2)} ${toCurrency}`;
        } else {
            alert("Conversion rate not found in the response.");
        }
    } catch (error) {
        console.error("Error during currency conversion:", error);
        document.getElementById('conversion-result').textContent = "An error occurred while fetching conversion data.";
    }
}

    // Initialize currency options
    populateCurrencyOptions();

    // Add event listener to close button
    const closeButton = document.getElementById('close-button');
    if (closeButton) {
        closeButton.addEventListener('click', () => {
            document.getElementById('currency-converter').classList.add('hidden');
        });
    }

    // Add event listener to convert button
    const convertButton = document.getElementById('convert-button');
    if (convertButton) {
        convertButton.addEventListener('click', convertCurrency);
    }
});
