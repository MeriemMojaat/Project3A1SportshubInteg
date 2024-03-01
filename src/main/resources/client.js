// Stripe publishable key
var stripe = Stripe('pk_test_51Omk7aIZrZQYiX0L2lHO9SCm3IBGCPGe7tooA550xSCGdXADLBNeGjxXaLzPN0ZRs8383c0CnkvnHM8oGLc5qN7400X3It5xJA');

// Initialize Stripe.js with your publishable key

// Create an instance of Elements
var elements = stripe.elements();

// Create CardNumber, CardExpiry, and CardCvc elements
var cardNumber = elements.create('cardNumber');
var cardExpiry = elements.create('cardExpiry');
var cardCvc = elements.create('cardCvc');

// Mount the CardNumber, CardExpiry, and CardCvc elements to your payment form
cardNumber.mount('#card-number');
cardExpiry.mount('#card-expiry');
cardCvc.mount('#card-cvc');

// Handle form submission
var form = document.getElementById('payment-form');
form.addEventListener('submit', function(event) {
    event.preventDefault();
    // Create payment method and handle response
    stripe.createPaymentMethod({
        type: 'card',
        card: cardNumber,
        billing_details: {
            // Add billing details if needed
        }
    }).then(function(result) {
        if (result.error) {
            // Show error to your customer
            console.log(result.error.message);
        } else {
            // Send payment method ID to your server
            var paymentMethodId = result.paymentMethod.id;
            fetch('/create-payment-intent', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ payment_method_id: paymentMethodId })
            }).then(function(response) {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            }).then(function(data) {
                // Handle response from the backend if needed
                console.log(data);
                if (data.clientSecret) {
                    stripe.confirmCardPayment(data.clientSecret).then(function(result) {
                        if (result.error) {
                            console.log(result.error.message);
                            // Show error message to the user
                            document.getElementById('payment-message').textContent = result.error.message;
                        } else {
                            // Payment successful, redirect to index.html
                            alert('Payment successful!');
                            window.location.href = "/index.html";
                        }
                    });
                }
            }).catch(function(error) {
                console.error('There was a problem with the fetch operation:', error);
            });
        }
    });
});
