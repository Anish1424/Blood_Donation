const registeredEmails = ['bloodbridge@gmail.com', 'user@example.com'];

/*document.getElementById('registerForm').addEventListener('submit', function (e) {
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


document.getElementById('registerForm').addEventListener('submit', async function (e) {
  e.preventDefault();

  const patientData = {
    fullName: document.getElementById('full-name').value.trim(),
    email: document.getElementById('email').value.trim().toLowerCase(),
    phone: document.getElementById('phone').value.trim(),
    address: document.getElementById('address').value.trim(),
    age: parseInt(document.getElementById('age').value),
    bloodGroup: document.getElementById('blood-group').value,
    disease: document.getElementById('disease').value.trim(),
    doctorName: document.getElementById('doctor-name').value.trim(),
    password: document.getElementById('password').value,
    confirmPassword: document.getElementById('confirm-password').value
  };

  // Check password match
  if (patientData.password !== patientData.confirmPassword) {
    alert("Passwords do not match!");
    return;
  }

  delete patientData.confirmPassword; // Remove confirmPassword before sending

  const message = document.getElementById('alreadyRegisteredMsg');

  try {
    const response = await fetch('http://localhost:8080/api/patients/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(patientData)
    });

    const result = await response.text();

    if (!response.ok) {
      message.textContent = result;
      message.style.display = 'block';
    } else {
      alert('Registration successful! Redirecting to login...');
      window.location.href = 'PatientLogin.html'; // Redirects to login page
    }
  } catch (error) {
    console.error('Error during registration:', error);
    alert('Something went wrong. Please try again.');
  }
});


function redirectToLogin() {
  window.location.href = "PatientLogin.html";
}
