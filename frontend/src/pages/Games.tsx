import React, { useState, useEffect } from 'react';
import axios from 'axios';
import AddGameModal from '../components/AddGameModal'; // This will be your modal component for adding games
import { ViewGame } from '../models/game';
import DeleteIcon from '@mui/icons-material/Delete';

const Games = () => {
    const [games, setGames] = useState<ViewGame[]>([]);
    const [isModalOpen, setModalOpen] = useState(false);
    const [deleteConfirmOpen, setDeleteConfirmOpen] = useState(false);
    const [currentGameId, setCurrentGameId] = useState<number|null>(null);

    useEffect(() => {
        fetchGames();
    }, []);

    const fetchGames = async () => {
        const response = await axios.get(`${process.env.REACT_APP_API_URL}/games`);
        setGames(response.data.content);
    };

    const openModal = () => setModalOpen(true);
    const closeModal = () => setModalOpen(false);
    const openDeleteConfirm = (gameId: number | null) => {
        setCurrentGameId(gameId);
        setDeleteConfirmOpen(true);
    };
    const closeDeleteConfirm = () => {
        setDeleteConfirmOpen(false);
        setCurrentGameId(null);
    };
    const deleteGame = async () => {
        if (currentGameId) {
            await axios.delete(`${process.env.REACT_APP_API_URL}/games/${currentGameId}`);
            fetchGames();
        }
        closeDeleteConfirm();
    };

    const handleGameAdded = () => {
        fetchGames();
    };

    return (
        <div className="flex flex-col items-center p-4 w-full">
            <div className="self-end mb-4">
                <button onClick={openModal} className="p-2 bg-blue-500 text-white rounded hover:bg-blue-600">Add New Game</button>
            </div>
            <div className="overflow-x-auto w-3/4 bg-white shadow overflow-hidden rounded-lg">
                <div>
                    <table className="min-w-full divide-y divide-gray-200">
                        <thead className="bg-gray-50">
                            <tr>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Event</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Round</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">White Player</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Black Player</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">White Elo</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Black Elo</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Result</th>
                                <th className="px-6 py-4 text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                            </tr>
                        </thead>
                        <tbody className="bg-white divide-y divide-gray-200 text-center">
                            {games.map(game => (
                                <tr key={game.id} className={`${currentGameId === game.id ? 'bg-red-100' : ''}`}>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.event}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.date}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.round}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.whitePlayer.name + " " + game.whitePlayer.lastName}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.blackPlayer.name + " " + game.blackPlayer.lastName}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.whitePlayer.elo}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.blackPlayer.elo}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">{game.result}</td>
                                    <td className="px-6 py-4 whitespace-nowrap">
                                        <DeleteIcon className="text-red-500 hover:text-red-700 cursor-pointer" onClick={() => openDeleteConfirm(game.id)}/>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
            {isModalOpen && <AddGameModal closeModal={closeModal} onGameAdded={handleGameAdded}/>}
            {deleteConfirmOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
                    <div className="bg-white p-4 rounded space-y-4">
                        <h2 className="text-xl">Are you sure you want to delete this game?</h2>
                        <div className="flex justify-center space-x-4">
                            <button onClick={deleteGame} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-700">Yes</button>
                            <button onClick={closeDeleteConfirm} className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-700">No</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default Games;
