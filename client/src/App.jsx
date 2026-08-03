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
    <div style={{ background: '#1e1e2e', color: '#cdd6f4', minHeight: '100vh', padding: '40px', fontFamily: 'sans-serif', textAlign: 'center' }}>
      <h1>Microservice Landing Page</h1>
      <p>Decoupled architecture running behind an Nginx Reverse Proxy.</p>
      
      <form onSubmit={handleSubmit} style={{ marginTop: '20px' }}>
        <input 
          type="email" 
          placeholder="Enter work email" 
          value={email} 
          onChange={(e) => setEmail(e.target.value)}
          style={{ padding: '10px', width: '250px', borderRadius: '4px', border: '1px solid #45475a', background: '#313244', color: '#cdd6f4' }}
          required 
        />
        <button type="submit" style={{ padding: '10px 20px', marginLeft: '10px', background: '#a6e3a1', color: '#11111b', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: 'bold' }}>
          Submit Lead
        </button>
      </form>
      {message && <p style={{ color: '#f9e2af', marginTop: '15px' }}>{message}</p>}
    </div>
  );
}
