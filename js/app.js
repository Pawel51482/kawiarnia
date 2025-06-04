document.addEventListener('DOMContentLoaded', () => {
    const token = localStorage.getItem('token');
  
    const loginForm = document.getElementById('loginForm');
    if (loginForm) loginForm.onsubmit = async e => {
      e.preventDefault();
      const email = document.getElementById('email').value;
      const password = document.getElementById('password').value;
      const res = await fetch('http://localhost:8000/token', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      const data = await res.json();
      if (res.ok) {
        localStorage.setItem('token', data.access_token);
        window.location.href = 'dashboard.html';
      } else alert(data.detail);
    };
  
    const registerForm = document.getElementById('registerForm');
    if (registerForm) registerForm.onsubmit = async e => {
      e.preventDefault();
      const email = document.getElementById('reg_email').value;
      const password = document.getElementById('reg_password').value;
      const res = await fetch('http://localhost:8000/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      const data = await res.json();
      if (res.ok) {
        localStorage.setItem('token', data.access_token);
        window.location.href = 'dashboard.html';
      } else alert(data.detail);
    };
  
    const coffeeList = document.getElementById('coffeeList');
    if (coffeeList && token) {
      fetch('http://localhost:8000/coffees', {
        headers: { 'Authorization': 'Bearer ' + token }
      })
        .then(res => res.json())
        .then(coffees => {
          coffeeList.innerHTML = coffees.map(coffee => `
            <div>
              <h3>${coffee.name}</h3>
              <p>${coffee.description || ''}</p>
              <p>${coffee.price} PLN</p>
              <button onclick="orderCoffee(${coffee.id})">Zamów</button>
            </div>
          `).join('');
        });
    }
  
    const reservationForm = document.getElementById('reservationForm');
    if (reservationForm && token) reservationForm.onsubmit = async e => {
      e.preventDefault();
      const table = document.getElementById('table').value;
      const date = document.getElementById('date').value;
      const time = document.getElementById('time').value;
      const res = await fetch('http://localhost:8000/reservations/', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ table_number: +table, date, time })
      });
      alert(res.ok ? 'Zarezerwowano stolik ✅' : 'Błąd rezerwacji ❌');
    };
  });
  
  async function orderCoffee(coffee_id) {
    const token = localStorage.getItem('token');
    const res = await fetch('http://localhost:8000/orders/', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + token
      },
      body: JSON.stringify({ coffee_id, quantity: 1 })
    });
    alert(res.ok ? 'Zamówienie przyjęte ✅' : 'Błąd zamówienia ❌');
  }