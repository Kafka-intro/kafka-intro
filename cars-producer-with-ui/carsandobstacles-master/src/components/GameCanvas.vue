<template>
	<div class="canvas-container">
		<canvas ref="gameCanvas" class="game-canvas"></canvas>
		<car :canvas-width="canvasWidth" :canvas-height="canvasHeight" :y="120" :x="160" :id="1" ref="car1" 
			:obstacles="obstacles" :interval-id="car1IntervalId"
		>
		</car>
		<car :canvas-width="canvasWidth" :canvas-height="canvasHeight" :y="530" :x="1220" :id="2" ref="car2" 
			:obstacles="obstacles" :interval-id="car2IntervalId"
		>
		</car>
	</div>
</template>
<script>
import axios from "axios";
import Obstacle from "../classes/Obstacle";
import Car from "./Car";

export default {
	name: "game-canvas",
	components: {
		Car
	},
	data: () => ({
		canvas: null,
		context: null,
		squareSize: 20,
		canvasHeight: 0,
		canvasWidth: 0,
		obstacles: [],
		obstaclesWidth: 0,
		car1IntervalId: 0,
		car2IntervalId: 0
	}),
	methods: {
		drawGrid() {
			let width = this.canvas.width = document.body.clientWidth;
			let height = this.canvas.height = document.body.clientHeight;
			let squareSize = 20;
			let xNumber = Math.floor(width / squareSize) - 2;
			let yNumber = Math.floor(height / squareSize) - 2;
			this.canvasWidth = xNumber * squareSize;
			this.canvasHeight = yNumber * squareSize;
			let xPadding = width - this.canvasWidth;
			let yPadding = height - this.canvasHeight;
			let leftPadding = Math.ceil(xPadding / 2) - 0.5;
			let rightPadding = width - this.canvasWidth - leftPadding;
			let topPadding = Math.ceil(yPadding / 2) - 0.5;
			let bottomPadding = height - this.canvasHeight - topPadding;

			this.context.strokeStyle = "lightgrey";
			this.context.beginPath();
			for (var x = leftPadding; x <= width - rightPadding; x += squareSize) {
				this.context.moveTo(x, topPadding);
				this.context.lineTo(x, height - bottomPadding);
			}
			
			for (var y = topPadding; y <= height - bottomPadding; y += squareSize) {
				this.context.moveTo(leftPadding, y);
				this.context.lineTo(width - rightPadding, y);
			}
			this.context.stroke();
		},
		drawIcons() {
			this.context.font = "80px FontAwesome";
			this.context.fillStyle = "#3773B2";

			// Houses
			this.context.fillText("\uF015", 145, 88);
			this.context.fillText("\uF015", 1198, 88);
			this.context.fillText("\uF015", 1120, 580);

			// Office
			this.context.fillText("\uF135", 300, 465);
			this.context.fillStyle = "black";
		},
		drawExternalContour() {
			const leftBound = new Obstacle(this.squareSize * 5, this.squareSize * 6, this.obstaclesWidth, 
				this.squareSize * 25);
			const bottomBound = new Obstacle(this.squareSize * 5, this.squareSize * 35, 
				this.squareSize * 66 + this.squareSize / 2, this.obstaclesWidth);
			const rightBound = new Obstacle(this.squareSize * 71, this.squareSize * 2, this.obstaclesWidth, 
				this.squareSize * 33);
			const upperRightDeadEndTopBound = new Obstacle(this.squareSize * 65, this.squareSize * 2, 
				this.squareSize * 6, this.obstaclesWidth);
			const upperRightDeadEndLeftBound = new Obstacle(this.squareSize * 65, this.squareSize * 2, 
				this.obstaclesWidth, this.squareSize * 9);
			const upperRightHouseLeftEdge = new Obstacle(this.squareSize * 58, this.squareSize * 2,
				this.obstaclesWidth, this.squareSize * 9);
			const topBound = new Obstacle(this.squareSize * 14, this.squareSize * 2, this.squareSize * 44, 
				this.obstaclesWidth);

			this.context.rect(leftBound.x, leftBound.y, leftBound.width, leftBound.height);
			this.context.rect(bottomBound.x, bottomBound.y, bottomBound.width, bottomBound.height);
			this.context.rect(rightBound.x, rightBound.y, rightBound.width, rightBound.height);
			this.context.rect(upperRightDeadEndTopBound.x, upperRightDeadEndTopBound.y, 
				upperRightDeadEndTopBound.width, upperRightDeadEndTopBound.height);
			this.context.rect(upperRightDeadEndLeftBound.x, upperRightDeadEndLeftBound.y, 
				upperRightDeadEndLeftBound.width, upperRightDeadEndLeftBound.height);
			this.context.rect(upperRightHouseLeftEdge.x, upperRightHouseLeftEdge.y, 
				upperRightHouseLeftEdge.width, upperRightHouseLeftEdge.height);
			this.context.rect(topBound.x, topBound.y, topBound.width, topBound.height);
			this.obstacles.push(leftBound, bottomBound, rightBound, upperRightDeadEndTopBound,
				upperRightDeadEndLeftBound, upperRightHouseLeftEdge, topBound);
		},
		drawTeslaHQContour() {
			const leftBound = new Obstacle(this.squareSize * 12, this.squareSize * 10, this.obstaclesWidth,
				this.squareSize * 20);
			const topBound = new Obstacle(this.squareSize * 12, this.squareSize * 10, this.squareSize * 25,
				this.obstaclesWidth); 
			const bottomBound = new Obstacle(this.squareSize * 12, this.squareSize * 30, 
				this.squareSize * 37 + this.squareSize / 2, this.obstaclesWidth);
			const bottomDeadEndRightBound = new Obstacle(this.squareSize * 49, this.squareSize * 25,
				this.obstaclesWidth, this.squareSize * 5);
			const bottomDeadEndTopBound = new Obstacle(this.squareSize * 23, this.squareSize * 25, 
				this.squareSize * 26, this.obstaclesWidth);
			const topDeadEndBottomBound = new Obstacle(this.squareSize * 23, this.squareSize * 20,
				this.squareSize * 14 + this.squareSize / 2, this.obstaclesWidth);
			const topDeadEndRightBound = new Obstacle(this.squareSize * 37, this.squareSize * 15, 
				this.obstaclesWidth, this.squareSize * 5);
			const topDeadEndTopBound = new Obstacle(this.squareSize * 23, this.squareSize * 15, 
				this.squareSize * 14, this.obstaclesWidth);

			this.context.rect(leftBound.x, leftBound.y, leftBound.width, leftBound.height);
			this.context.rect(topBound.x, topBound.y, topBound.width, topBound.height);
			this.context.rect(bottomBound.x, bottomBound.y, bottomBound.width, bottomBound.height);
			this.context.rect(bottomDeadEndRightBound.x, bottomDeadEndRightBound.y, 
				bottomDeadEndRightBound.width, bottomDeadEndRightBound.height);
			this.context.rect(bottomDeadEndTopBound.x, bottomDeadEndTopBound.y, bottomDeadEndTopBound.width, 
				bottomDeadEndTopBound.height);
			this.context.rect(topDeadEndBottomBound.x, topDeadEndBottomBound.y, topDeadEndBottomBound.width, 
				topDeadEndBottomBound.height);
			this.context.rect(topDeadEndRightBound.x, topDeadEndRightBound.y, topDeadEndRightBound.width, 
				topDeadEndRightBound.height);
			this.context.rect(topDeadEndTopBound.x, topDeadEndTopBound.y, topDeadEndTopBound.width, 
				topDeadEndTopBound.height);
			this.obstacles.push(leftBound, topBound, bottomBound, bottomDeadEndRightBound, 
				bottomDeadEndTopBound, topDeadEndBottomBound, topDeadEndRightBound, topDeadEndTopBound);
		},
		drawHousePocket() {
			const bottomLeftBound = new Obstacle(this.squareSize * 55, this.squareSize * 30, this.squareSize * 5,
				this.obstaclesWidth);
			const leftBound = new Obstacle(this.squareSize * 55, this.squareSize * 25, this.obstaclesWidth,
				this.squareSize * 5);
			const topBound = new Obstacle(this.squareSize * 55, this.squareSize * 25, this.squareSize * 12,
				this.obstaclesWidth);
			const rightBound = new Obstacle(this.squareSize * 67, this.squareSize * 25, this.obstaclesWidth,
				this.squareSize * 5);
			const bottomRightBound = new Obstacle(this.squareSize * 64, this.squareSize * 30, 
				this.squareSize * 3 + this.squareSize / 2, this.obstaclesWidth);

			this.context.rect(bottomLeftBound.x, bottomLeftBound.y, bottomLeftBound.width, 
				bottomLeftBound.height);
			this.context.rect(leftBound.x, leftBound.y, leftBound.width, leftBound.height);
			this.context.rect(topBound.x, topBound.y, topBound.width, topBound.height);
			this.context.rect(rightBound.x, rightBound.y, rightBound.width, rightBound.height);
			this.context.rect(bottomRightBound.x, bottomRightBound.y, bottomRightBound.width, 
				bottomRightBound.height);
			this.obstacles.push(bottomLeftBound, leftBound, topBound, rightBound, bottomRightBound);
		},
		drawRightGut() {
			const leftOuterBound = new Obstacle(this.squareSize * 45, this.squareSize * 10, this.obstaclesWidth,
				this.squareSize * 10);
			const bottomOuterBound = new Obstacle(this.squareSize * 45, this.squareSize * 20, 
				this.squareSize * 26, this.obstaclesWidth);
			const rightInnerBound = new Obstacle(this.squareSize * 50, this.squareSize * 10, this.obstaclesWidth,
				this.squareSize * 6);
			const topInnerBound = new Obstacle(this.squareSize * 50, this.squareSize * 16, this.squareSize * 16,
				this.obstaclesWidth);

			this.context.rect(leftOuterBound.x, leftOuterBound.y, leftOuterBound.width, leftOuterBound.height);
			this.context.rect(bottomOuterBound.x, bottomOuterBound.y, bottomOuterBound.width, 
				bottomOuterBound.height);
			this.context.rect(rightInnerBound.x, rightInnerBound.y, rightInnerBound.width, 
				rightInnerBound.height);
			this.context.rect(topInnerBound.x, topInnerBound.y, topInnerBound.width, topInnerBound.height);
			this.obstacles.push(leftOuterBound, bottomOuterBound, rightInnerBound, topInnerBound);
		},
		drawRoads() {
			this.obstaclesWidth = this.squareSize / 2;
			this.drawExternalContour();
			this.drawTeslaHQContour();
			this.drawHousePocket();
			this.drawRightGut();
			this.context.fill();
		},
		addCarsHandlers() {
			const car1 = this.$refs.car1;
			const car2 = this.$refs.car2;
			window.addEventListener("keydown", event => {
				switch (event.code) {
					case "KeyW":
						car1.stopSlowingDown();
						car1.goUpAcceleration();
						break;
					case "KeyS":
						car1.stopSlowingDown();
						car1.goDownAcceleration();
						break;
					case "KeyA":
						car1.stopSlowingDown();
						car1.goLeftAcceleration();
						break;
					case "KeyD":
						car1.stopSlowingDown();
						car1.goRightAcceleration();
						break;
					case "Space":
						car1.brake();
						break;
					case "ArrowUp":
						car2.stopSlowingDown();
						car2.goUpAcceleration();
						break;
					case "ArrowDown":
						car2.stopSlowingDown();
						car2.goDownAcceleration();
						break;
					case "ArrowLeft":
						car2.stopSlowingDown();
						car2.goLeftAcceleration();
						break;
					case "ArrowRight":
						car2.stopSlowingDown();
						car2.goRightAcceleration();
						break;
					case "Numpad0":
						car2.brake();
						break;
				}
			});
			window.addEventListener("keyup", event => {
				switch (event.code) {
					case "KeyW":
						this.car1IntervalId = setInterval(car1.goUpDeceleration, 100);
						break;
					case "KeyS":
						this.car1IntervalId = setInterval(car1.goDownDeceleration, 100);
						break;
					case "KeyA":
						this.car1IntervalId = setInterval(car1.goLeftDeceleration, 100);
						break;
					case "KeyD":
						this.car1IntervalId = setInterval(car1.goRightDeceleration, 100);
						break;
					case "ArrowUp":
						this.car2IntervalId = setInterval(car2.goUpDeceleration, 100);
						break;
					case "ArrowDown":
						this.car2IntervalId = setInterval(car2.goDownDeceleration, 100);
						break;
					case "ArrowLeft":
						this.car2IntervalId = setInterval(car2.goLeftDeceleration, 100);
						break;
					case "ArrowRight":
						this.car2IntervalId = setInterval(car2.goRightDeceleration, 100);
						break;
				}
			});
		},
		async sendDataToKafka() {
			const kafkaUrl = "http://localhost:8082";
			const carsInfoTopic = "carsInfo";
			const carsErrorTopic = "carsErrors";
			let car;
			for (car of [this.$refs.car1, this.$refs.car2]) {
				if (car.isMoving() && !car.crashed) {
					await axios.post(`${kafkaUrl}/topics/${carsInfoTopic}`, {
						"records": [{
							"key": car.id,
							"value": {
								"coordX": car.coordX,
								"coordY": car.coordY,
								"direction": car.direction,
								"timestamp": Date.now()
							}
						}]
					}, {
						headers: {
							"Content-Type": "application/vnd.kafka.json.v2+json"
						}
					});
				} else if (car.crashed && !car.reportedCrash) {
					await axios.post(`${kafkaUrl}/topics/${carsErrorTopic}`, {
						"records": [{
							"key": car.id,
							"value": {
								"errorMsg": `Car with id ${car.id} is crashed`,
								"timestamp": Date.now()
							}
						}]
					}, {
						headers: {
							"Content-Type": "application/vnd.kafka.json.v2+json"
						}
					});
					car.reportedCrash = true;
				}
			}
		}
	},
	mounted() {
		this.canvas = this.$refs.gameCanvas;
		this.context = this.canvas.getContext("2d");
		this.drawGrid();
		this.drawIcons();
		this.drawRoads();
		this.addCarsHandlers();

		// Change this in order to send data to Kafka more or less often
		const sendDataFrequency = 1000
		window.setInterval(this.sendDataToKafka, sendDataFrequency);
	}
}
</script>
<style scoped>
.game-canvas {
	border: lightgrey 1px solid;
	box-shadow: 0 2px 3px rgba(0, 0, 0, 0.2);
}

.canvas-container {
	position: relative;
}
</style>