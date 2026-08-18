import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './theme.css';
import './App.css';

// Mount the App component inside the HTML root element
ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);
