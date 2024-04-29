import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import './App.css';
import Navbar from './components/Navbar';
import Games from './pages/Games';
import Players from './pages/Players';

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<Navigate to="/games" replace />} />
        <Route path="/players" element={<Players />} />
        <Route path="/games" element={<Games />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;
