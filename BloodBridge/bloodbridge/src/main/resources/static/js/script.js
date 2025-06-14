const registeredEmails = ['bloodbridge@gmail.com', 'user@example.com'];

/*document.getElementById('registerForm').addEventListener('submit', function (e) {
 // e.preventDefault();

  const email = document.getElementById('email').value.trim().toLowerCase();
  const message = document.getElementById('alreadyRegisteredMsg');

  if (registeredEmails.includes(email)) {
    message.textContent = "This email is already registered. Please login.";
    message.style.display = 'block';
  } else {
    message.style.display = 'none';
    alert('Registration successful!');
    this.reset();
  }
});*/


document.getElementById('registerForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const name = document.getElementById('name').value.trim();
  const email = document.getElementById('email').value.trim().toLowerCase();
  const phoneNumber = document.getElementById('phone').value.trim();
  const address = document.getElementById('address').value.trim();
  const password = document.getElementById('password').value.trim();
  const confirmPassword = document.getElementById('confirm-password').value.trim();
  const message = document.getElementById('alreadyRegisteredMsg');

  if (password !== confirmPassword) {
    alert("Passwords do not match!");
    return;
  }

  const adminData = {
    fullName: name,
    email: email,
    phoneNumber: phoneNumber,
    address: address,
    age: 30, // you can collect from user if needed
    password: password
  };

  fetch("http://localhost:8080/api/admin/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(adminData)
  })
    .then(response => {
      if (response.ok) {
        alert("Registration successful!");
        window.location.href = "AdminLogin.html"; // redirect after register
      } else {
        return response.text().then(msg => {
          throw new Error(msg);
        });
      }
    })
    .catch(error => {
      console.error("Error:", error);
      message.textContent = "Registration failed: " + error.message;
      message.style.display = 'block';
    });
});


function redirectToLogin() {
  alert('Redirect to login page');
  
}

// Feedback form handling for project.html
const feedbackForm = document.getElementById('feedbackForm');
if (feedbackForm) {
  feedbackForm.addEventListener('submit', function (e) {
    e.preventDefault();
    const userType = document.getElementById('userType').value;
    const q1 = document.getElementById('q1').value;
    const q2 = document.getElementById('q2').value;
    const q3 = document.getElementById('q3').value;
    const q4 = document.getElementById('q4').value;
    const q5 = document.getElementById('q5').value;
    const feedback = {
      userType,
      q1,
      q2,
      q3,
      q4,
      q5,
      timestamp: new Date().toISOString()
    };
    let feedbacks = JSON.parse(localStorage.getItem('feedbacks') || '[]');
    feedbacks.push(feedback);
    localStorage.setItem('feedbacks', JSON.stringify(feedbacks));
    
    // Create and show popup
    const popup = document.createElement('div');
    popup.style.cssText = `
      position: fixed;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      background-color: #4CAF50;
      color: white;
      padding: 20px 40px;
      border-radius: 5px;
      box-shadow: 0 2px 10px rgba(0,0,0,0.2);
      z-index: 1000;
      text-align: center;
    `;
    popup.textContent = "Your feedback is submitted successfully.";
    document.body.appendChild(popup);

    // Remove popup after 3 seconds
    setTimeout(() => {
      popup.remove();
      feedbackForm.reset();
    }, 3000);
  });
}
