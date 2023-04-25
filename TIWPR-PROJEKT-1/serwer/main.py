import asyncio
import websockets
import tracemalloc
import uuid
import shortuuid

tracemalloc.start()

games = {}

PORT = 5555
ADDRESS = 'localhost'


async def server(websocket, path):
    game_id = None
    player_num = None

    async def assign_player():
        nonlocal game_id, player_num

        while True:
            message = await websocket.recv()
            print(f"ASSIGN PLAYER: {message}")
            if message.startswith("NewGame"):
                for gid, game in games.items():
                    if len(game) == 1:
                        game_id = gid
                        player_num = 1
                        game.append(websocket)
                        print(f"CLIENT CONNECTED FROM: {websocket.remote_address} to {game_id}")
                        return
                game_id = str(shortuuid.ShortUUID().random(length=16))
                player_num = 0
                games[game_id] = [websocket]
                print(f"CLIENT CONNECTED FROM: {websocket.remote_address} to {game_id}")
                break
            elif message.startswith("Reconnect"):
                # Extract game_id from the message
                game_id = message.split("|")[1]
                if game_id in games:
                    player_num = int(message.split("|")[2])
                    games[game_id][int(message.split("|")[2])] = websocket
                    print(f"PLAYER RECONNECTED - CLIENT CONNECTED FROM: {websocket.remote_address} to {game_id}")
                    break
                else:
                    await websocket.send(f"GameNotFound|{game_id}")
                    continue

    await assign_player()

    async def game_loop():
        nonlocal game_id, player_num

        while len(games[game_id]) < 2:
            await asyncio.sleep(1)

        await games[game_id][player_num].send(f"GameStart|{game_id}|{player_num}")

        while True:
            try:
                message = await websocket.recv()
                print(f"\tmessage from: {websocket.remote_address} : {message}")
                await games[game_id][not player_num].send(message)
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
