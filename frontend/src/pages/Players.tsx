import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AddPlayerModal from '../components/AddPlayerModal';
import { Player } from '../models/player';
import DeleteIcon from '@mui/icons-material/Delete';

const Players = () => {
    const [players, setPlayers] = useState<Player[]>([]);
    const [isModalOpen, setModalOpen] = useState(false);
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);
    const [currentPlayerId, setCurrentPlayerId] = useState<number|null>(null);
  
    useEffect(() => {
      fetchPlayers();
    }, []);
  
    const fetchPlayers = async () => {
      const response = await axios.get(`${process.env.REACT_APP_API_URL}/players`);
      setPlayers(response.data.content);
    };
  
    const openModal = () => setModalOpen(true);
    const closeModal = () => setModalOpen(false);
    const openDeleteConfirm = (playerId: number | null) => {
      setCurrentPlayerId(playerId);
      setDeleteConfirmOpen(true);
    };
    const closeDeleteConfirm = () => {
      setDeleteConfirmOpen(false);
      setCurrentPlayerId(null);
    };

    const deletePlayer = async () => {
      if (currentPlayerId) {
        await axios.delete(`${process.env.REACT_APP_API_URL}/players/${currentPlayerId}`);
        fetchPlayers();
        closeDeleteConfirm();
      }
    };
  
    const handlePlayerAdded = () => {
      fetchPlayers();
    };

  return (
    <div className="flex flex-col items-center p-4">
      <div className="self-end">
        <button onClick={openModal} className="p-2 bg-blue-500 text-white rounded hover:bg-blue-600">Create New Player</button>
      </div>
      <div className="w-3/4 bg-white shadow overflow-hidden rounded-lg">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
              <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Last Name</th>
              <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Gender</th>
              <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Birth Date</th>
              <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200 text-center">
          {
          players.map(player => (
              <tr key={player.id} className={`${currentPlayerId === player.id ? 'bg-red-100' : ''}`}>
                <td className="px-6 py-4 whitespace-nowrap">{player.id}</td>
                <td className="px-6 py-4 whitespace-nowrap">{player.name}</td>
                <td className="px-6 py-4 whitespace-nowrap">{player.lastName}</td>
                <td className="px-6 py-4 whitespace-nowrap">{player.gender}</td>
                <td className="px-6 py-4 whitespace-nowrap">{player.birthDate}</td>
                <td className="px-6 py-4 whitespace-nowrap">
                    <DeleteIcon className="text-red-500 hover:text-red-700 cursor-pointer" onClick={() => openDeleteConfirm(player.id)}/>
                </td>
              </tr>
            ))
          }
          </tbody>
        </table>
      </div>
      {isModalOpen && <AddPlayerModal closeModal={closeModal} onPlayerAdded={handlePlayerAdded} />}
      {deleteConfirmOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-4 rounded space-y-4">
            <h2 className="text-xl">Are you sure you want to delete this player?</h2>
            <div className="flex justify-center space-x-4">
              <button onClick={deletePlayer} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700">Yes</button>
              <button onClick={closeDeleteConfirm} className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-700">No</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Players;
