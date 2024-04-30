import { NavLink } from "react-router-dom";

const Navbar: React.FC = () => {
    const activeLinkStyle = ({ isActive }: { isActive: boolean }) =>
        isActive ? 'px-6 py-2 text-lg font-bold hover:text-gray-300 text-white bg-gray-700 rounded-lg' : 'px-6 py-2 text-lg hover:text-gray-300 text-gray-400 hover:scale-105 transform transition duration-150';

    return (
        <nav className="bg-gray-800 text-white p-4">
            <div className="container mx-auto flex justify-between items-center">
                <div className="text-lg font-semibold">
                    Chess Games App
                </div>
                <div>
                    <NavLink to="/players" className={activeLinkStyle}>Players</NavLink>
                    <NavLink to="/games" className={activeLinkStyle}>Games</NavLink>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
