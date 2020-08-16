FROM node:13.7

WORKDIR /usr/src/app
COPY . /usr/src/app

RUN npm install && npm run build && npm install -g serve

CMD ["serve", "-s", "/usr/src/app/dist", "--listen", "5000"]