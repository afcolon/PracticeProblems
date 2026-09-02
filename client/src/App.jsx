import { Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import SubscriptionsPage from './pages/SubscriptionsPage';
import AboutPage from './pages/AboutPage';

export default function App() {

  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<SubscriptionsPage />}/>
        <Route path="about" element={<AboutPage />} />
      </Route>
    </Routes>

  );
}