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

    await assign_player()

    async def game_loop():
        nonlocal game_id, player_num

        # Wait for "ready" messages from both players
        while len(games[game_id]) < 2:
            await asyncio.sleep(1)

        await games[game_id][player_num].send(f"GameStart|{game_id}|{player_num}")
        # await games[game_id][1].send(f"GameStart,gameEstablished,{game_id},1")

        while True:
            try:
                message = await websocket.recv()
                print(f"\tmessage from: {websocket.remote_address} : {message}")
                await games[game_id][not player_num].send(message)

            except websockets.exceptions.ConnectionClosed:
                # wait for player to reconnect
                while True:
                    try:
                        await websocket.ping()
                        await assign_player()
                        await games[game_id][player_num].send("Reconnected")
                        break
                    except websockets.exceptions.ConnectionClosed:
                        await asyncio.sleep(1)

                await asyncio.wait([ws.send("Ready") for ws in games[game_id]])

    await game_loop()


async def main():
    async with websockets.serve(server, ADDRESS, PORT):
        print(f"SERVER WAITING FOR CONNECTIONS: {ADDRESS}:{PORT}")
        await asyncio.Future()


if __name__ == '__main__':
    asyncio.run(main())
