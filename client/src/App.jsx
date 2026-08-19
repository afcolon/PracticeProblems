import React, { useState } from 'react';

export default function App() {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch('/api/subscriptions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
      });
      const data = await response.json();
      setMessage(data.message);
      
    } catch (err) {
      setMessage('Error connecting to microservice API gateway.');
    }
  };

  return (
    <div className="app">
      <h1>Microservice Landing Page</h1>
      <p>Decoupled architecture running behind an Nginx Reverse Proxy.</p>

      <form className="lead-form" onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Enter email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <button type="submit">Submit</button>
      </form>
      {message && <p className="lead-message">{message}</p>}
    </div>
  );
}