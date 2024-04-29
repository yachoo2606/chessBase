import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { CreateGame } from '../models/game';
import { Player } from '../models/player'; // Assume you have a type for Player

interface Props {
    closeModal: () => void;
    onGameAdded: () => void;
}

const AddGameModal = ({ closeModal, onGameAdded }: Props) => {
    const [formData, setFormData] = useState<CreateGame>({
        event: '',
        date: '',
        round: 0,
        whitePlayer: 0,
        blackPlayer: 0,
        whiteELO: 0,
        blackELO: 0,
        pgn: '',
        result: '',
        id: null,
    });
    const [players, setPlayers] = useState<Player[]>([]);
    const [canSubmit, setCanSubmit] = useState<boolean>(false);

    useEffect(() => {
        fetchPlayers();
    }, []);

    const fetchPlayers = async () => {
        try {
            const response = await axios.get(`${process.env.REACT_APP_API_URL}/players`);
            setPlayers(response.data.content);
        } catch (error) {
            console.error("Failed to fetch players:", error);
        }
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        try {
            const res = await axios.post(`${process.env.REACT_APP_API_URL}/tokens?post=true`);
            await axios.post(`${process.env.REACT_APP_API_URL}/games`, formData, {
                headers: {
                    "Token": res.data
                },
            });
            onGameAdded();
            closeModal(); 
        } catch (error) {
            console.error("Failed to create game:", error);
        }
    };
  

    useEffect(() => {
        setCanSubmit(formData.event.trim() !== '' &&
            formData.date.trim() !== '' &&
            formData.round > 0 &&
            formData.whitePlayer > 0 &&
            formData.blackPlayer > 0 &&
            formData.whitePlayer !== formData.blackPlayer &&
            formData.whiteELO > 0 &&
            formData.blackELO > 0 &&
            formData.pgn.trim() !== '' &&
            formData.result.trim() !== '');
    }, [formData]);
    const today = new Date().toISOString().split('T')[0];
    return (
        <div className="overflow-y-auto fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
            <div className="bg-white p-4 rounded space-y-4 w-1/2">
                <h2 className="text-xl font-semibold text-center">Add New Game</h2>
                <form onSubmit={handleSubmit} className="flex flex-col items-center space-y-4">
                    <label className="w-full">
                        Event
                        <input type="text" name="event" placeholder="Event" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
                    </label>
                    <label className="w-full">
                        Date
                        <input type="date" max={today} name="date" placeholder="Date" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
                    </label>
                    <label className="w-full">
                        Round
                        <input type="number" name="round" placeholder="Round" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
                    </label>
                    <label className="w-full">
                        White Player
                        <select name="whitePlayer" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300">
                            <option value="">Select Player</option>
                            {players
                                .map(player => (
                                <option key={player.id} value={player.id ? player.id : 0}>{player.name}</option>
                            ))}
                        </select>
                    </label>
                    <label className="w-full">
                        Black Player
                        <select name="blackPlayer" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300">
                            <option value="">Select Player</option>
                            {players
                                .map(player => (
                                <option key={player.id} value={player.id ? player.id : 0}>{player.name}</option>
                            ))}
                        </select>
                    </label>
                    <input type="number" name="whiteELO" placeholder="White ELO" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
                    <input type="number" name="blackELO" placeholder="Black ELO" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
                    <input type="text" name="pgn" placeholder="PGN" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
                    <label className="w-full">
                        Result
                        <select name="result" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300">
                            <option value="">Select Result</option>
                            <option value="WHITE">WHITE</option>
                            <option value="BLACK">BLACK</option>
                            <option value="DRAW">DRAW</option>
                        </select>
                    </label>
                    <div className="flex justify-center w-full space-x-2">
                        <button type="submit" disabled={!canSubmit} className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-blue-300">Submit</button>
                        <button onClick={closeModal} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600">Cancel</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AddGameModal;