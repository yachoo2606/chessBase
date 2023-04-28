import asyncio
import struct
import websockets
import tracemalloc
import uuid

tracemalloc.start()

games = {}

PORT = 5555
ADDRESS = '0.0.0.0'


async def server(websocket, path):
    game_id = None
    player_num = None
    reconnect_timeout = 60

    async def assign_player():
        nonlocal game_id, player_num

        while True:
            message = await websocket.recv()
            print(f"ASSIGN PLAYER({websocket.remote_address}): {message}")
            if message[0] == 0:
                for gid, game in games.items():
                    if len(game) == 1:
                        game_id = gid
                        player_num = 1
                        game.append(websocket)
                        print(games)
                        print(f"CLIENT CONNECTED FROM: {websocket.remote_address} to {game_id}")
                        return
                game_id = uuid.uuid4().bytes
                player_num = 0
                games[game_id] = [websocket]
                print(f"CLIENT CONNECTED FROM: {websocket.remote_address} to {game_id}")
                print(games)
                break
            elif message[0] == 1:
                # Extract game_id from the message
                game_id = message[2:]
                print(games)
                print(f"PLAYER {message[1]} tries to recconect to game {game_id}")
                print(f"{game_id}, {len(game_id)}")
                if game_id in games:
                    player_num = int(message[1])
                    games[game_id][player_num] = websocket
                    print(f"PLAYER {player_num} RECONNECTED - CLIENT CONNECTED FROM: {websocket.remote_address} to {game_id}")
                    break
                else:
                    # await websocket.send(f"GameNotFound|{game_id}")
                    await websocket.send(struct.pack(">h", 4))
                    continue

    await assign_player()

    async def game_loop():
        nonlocal game_id, player_num

        while len(games[game_id]) < 2:
            await asyncio.sleep(1)

        await games[game_id][player_num].send(struct.pack(">hh16s", 0, player_num, game_id))
        print(struct.pack(">hh16s", 0, player_num, game_id))

        while True:
            try:
                message = await asyncio.wait_for(websocket.recv(), reconnect_timeout)
                print(f"\tmessage from: {websocket.remote_address} : {message}")
                await games[game_id][not player_num].send(message)
            except asyncio.TimeoutError:
                print(f"PLAYER {player_num} DID NOT RECONNECT IN {reconnect_timeout} SECONDS - GAME ENDED")
                await asyncio.gather(*[player.send(struct.pack(">h", 4)) for player in games[game_id]])
                del games[game_id]
                break
            except websockets.exceptions.ConnectionClosedOK:
                print(f"GAME {game_id} USER DISCONNECTED")
            await asyncio.gather(*[ws.send("Ready") for ws in games[game_id]])

    await game_loop()


async def main():
    async with websockets.serve(server, ADDRESS, PORT):
        print(f"SERVER WAITING FOR CONNECTIONS: {ADDRESS}:{PORT}")
        await asyncio.Future()


if __name__ == '__main__':
    asyncio.run(main())
