import { Player } from "./player";

export type CreateGame = {
    id: number | null;
    event: string;
    date: string;
    round: number;
    whitePlayer: number;
    blackPlayer: number;
    whiteELO: number;
    blackELO: number;
    pgn: string;
    result: string;
}

export type ViewGame = {
    id: number;
    event: string;
    date: string;
    round: number;
    whitePlayer: Player;
    blackPlayer: Player;
    whiteELO: number;
    blackELO: number;
    pgn: string;
    result: string;
}