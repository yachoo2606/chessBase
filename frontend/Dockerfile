FROM node:20-alpine
LABEL authors="yachoo2606 - Jan Biały, Rychutech - Ryszard Dotka"

WORKDIR /chessbaseApp/

COPY public/ public/
COPY src/ src/
COPY package.json /chessbaseApp/
COPY tailwind.config.js /chessbaseApp/
COPY tsconfig.json /chessbaseApp/
COPY .env /chessbaseApp/

RUN npm install


CMD ["npm", "start"]
