/*const registeredEmails = ['bloodbridge@gmail.com', 'user@example.com'];

document.getElementById('registerForm').addEventListener('submit', function (e) {
  e.preventDefault();

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
});

function redirectToLogin() {
  alert('Redirect to login page');
  
}*/


document.getElementById('registerForm').addEventListener('submit', function (e) {
  e.preventDefault();

  const fullName = document.getElementById('name').value.trim();
  const email = document.getElementById('email').value.trim().toLowerCase();
  const phone = document.getElementById('phone').value.trim();
  const address = document.getElementById('address').value.trim();
  const age = parseInt(document.getElementById('age').value.trim());
  const bloodGroup = document.getElementById('blood-group').value.trim();
  const disease = document.getElementById('disease').value.trim();
  const password = document.getElementById('password').value.trim();
  const confirmPassword = document.getElementById('confirm-password').value.trim();

  if (password !== confirmPassword) {
    alert("Passwords do not match!");
    return;
  }

  const donorData = {
    fullName,
    email,
    phone,
    address,
    age,
    bloodGroup,
    disease,
    password
  };

  fetch("http://localhost:8080/api/donors/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(donorData)
  })
    .then(response => {
      if (response.ok) {
        alert("Registration successful!");
        window.location.href = "DonorLogin.html";
      } else {
        return response.text().then(msg => {
          throw new Error(msg);
        });
      }
    })
    .catch(error => {
      alert("Registration failed: " + error.message);
    });
});

function redirectToLogin() {
  window.location.href = "DonorLogin.html";
}

