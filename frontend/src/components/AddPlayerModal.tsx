import React, { useState } from 'react';
import axios from 'axios';
import { Player } from '../models/player';

interface Props {
    closeModal: () => void;
    onPlayerAdded: () => void;
}

const AddPlayerModal = (props: Props) => {
  const [formData, setFormData] = useState<Player>({
    id: null,
    name: '',
    lastName: '',
    birthDate: '',
    gender: 'MALE',
    title: null,
    club: null,
    elo: 1000,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const createPlayer = async () => {
    try {
      const res = await axios.post(`${process.env.REACT_APP_API_URL}/tokens?post=true`);
      await axios.post(`${process.env.REACT_APP_API_URL}/players`, formData, {
          headers: {
              "Token": res.data
          },
      });
      props.onPlayerAdded();
      props.closeModal();
    } catch (error) {
      console.error("Failed to create player:", error);
    }
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    createPlayer();
  };

  const isFormIncomplete = !formData.name || !formData.lastName || !formData.birthDate || !formData.gender;

  const today = new Date().toISOString().split('T')[0];

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
      <div className="bg-white p-4 rounded space-y-4 w-1/2"> 
        <h2 className="text-xl font-semibold text-center">Add New Player</h2>
        <form onSubmit={handleSubmit} className="flex flex-col items-center space-y-4">
          <input type="text" name="name" placeholder="Name" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />
          <input type="text" name="lastName" placeholder="Last Name" onChange={handleChange} className="w-full p-2 rounded border-2 border-gray-300" />

          <label htmlFor="gender" className="w-full flex flex-col">
            Gender
            <select name="gender" id="gender" onChange={handleChange} value={formData.gender} className="mt-1 p-2 rounded border-2 border-gray-300">
              <option value="MALE">MALE</option>
              <option value="FEMALE">FEMALE</option>
            </select>
          </label>

          <label htmlFor="birthDate" className="w-full flex flex-col">
            Birth Date
            <input type="date" name="birthDate" id="birthDate" max={today} onChange={handleChange} className="mt-1 p-2 rounded border-2 border-gray-300" />
          </label>

          <div className="flex justify-center w-full space-x-2">
            <button type="submit" disabled={isFormIncomplete} className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:bg-blue-300">Submit</button>
            <button onClick={props.closeModal} className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddPlayerModal;
