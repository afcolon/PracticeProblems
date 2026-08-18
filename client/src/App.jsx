import React, { useState } from 'react';

export default function App() {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch('/api/leads', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
      });
      const data = await res.json();
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
          placeholder="Enter work email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <button type="submit">Submit Lead</button>
      </form>
      {message && <p className="lead-message">{message}</p>}
    </div>
  );
}