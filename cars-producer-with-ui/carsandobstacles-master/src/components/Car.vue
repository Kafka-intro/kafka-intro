<template>
	<font-awesome-icon icon="car" class="car" 
		:style="{ top: `${top}px`, left: `${left}px`, width: `${width}px`, height: `${height}px`}"
	>
	</font-awesome-icon>
</template>
<script>
export default {
	name: "car",
	props: {
		id: Number,
		canvasWidth: Number,
		canvasHeight: Number,
		x: Number,
		y: Number,
		obstacles: Array,
		intervalId: Number
	},
	data: () => ({
		speed: 0,
		direction: null,
		directions: {
			UP: "up",
			DOWN: "down",
			LEFT: "left",
			RIGHT: "right"
		},
		width: 50,
		height: 50,
		top: 0,
		left: 0,
		//Because car image height do not take 100% of it's container height
		heightCorrection: 6,
		crashed: false,
		reportedCrash: false
	}),
	created() {
		this.top = this.y;
		this.left = this.x
	},
	methods: {
		goUp() {
			if (this.top > 0) {
				this.changeDirectionIfDifferent(this.directions.UP);
				if (!this.isCrashed()) {
					this.top -= this.speed;
				}
			}
		},
		goUpAcceleration() {
			this.speed += 0.2;
			this.goUp();
		},
		goUpDeceleration() {
			if (this.isMoving()) {
				this.speed -= 0.1;
				this.goUp();
			}
		},
		goDown() {
			if (this.top < this.canvasHeight) {
				this.changeDirectionIfDifferent(this.directions.DOWN);
				if (!this.isCrashed()) {
					this.top += this.speed;
				}
			}
		},
		goDownAcceleration() {
			this.speed += 0.2;
			this.goDown();
		},
		goDownDeceleration() {
			if (this.isMoving()) {
				this.speed -= 0.1;
				this.goDown();
			}
		},
		goLeft() {
			if (this.left > 0) {
				this.changeDirectionIfDifferent(this.directions.LEFT);
				if (!this.isCrashed()) {
					this.left -= this.speed;
				}
			}
		},
		goLeftAcceleration() {
			this.speed += 0.2;
			this.goLeft();
		},
		goLeftDeceleration() {
			if (this.isMoving()) {
				this.speed -= 0.1;
				this.goLeft();
			}
		},
		goRight() {
			if (this.left < this.canvasWidth) {
				this.changeDirectionIfDifferent(this.directions.RIGHT);
				if (!this.isCrashed()) {
					this.left += this.speed;
				}
			}
		},
		goRightAcceleration() {
			this.speed += 0.2;
			this.goRight();
		},
		goRightDeceleration() {
			if (this.isMoving()) {
				this.speed -= 0.1;
				this.goRight();
			}
		},
		isCrashed() {
			const bottom = this.top + this.height - this.heightCorrection;
			const right = this.left + this.width;
			let obstacleBottom = 0, obstacleRight = 0;
			let obstacle;
			for (obstacle of this.obstacles) {
				obstacleBottom = obstacle.y + obstacle.height;
				obstacleRight = obstacle.x + obstacle.width;
				if (bottom >= obstacle.y && this.top + this.heightCorrection <= obstacleBottom && 
						right >= obstacle.x && this.left <= obstacleRight && this.isCrashedInAMoment(obstacle)) {
					this.crashed = true;
					return true;
				}
			}

			this.crashed = false;
			this.reportedCrash = false;
			return false;
		},
		isMoving() {
			return this.speed > 0;
		},
		stopSlowingDown() {
			if (this.intervalId) {
				clearInterval(this.intervalId);
			}
		},
		brake() {
			if (this.isMoving()) {
				this.speed -= 2;
			}
		},
		changeDirectionIfDifferent(desiredDirection) {
			if (this.direction !== desiredDirection) {
				this.speed = 0;
				this.direction = desiredDirection;
			}
		},
		isCrashedInAMoment(obstacle) {
			const bottom = this.top + this.height - this.heightCorrection;
			const right = this.left + this.width;
			switch (this.direction) {
				case this.directions.UP:
					return bottom - this.speed >= obstacle.y;
				case this.directions.DOWN:
					return this.top + this.heightCorrection + this.speed <= obstacle.y + obstacle.height;
				case this.directions.LEFT:
					return right - this.speed >= obstacle.x;
				case this.directions.RIGHT:
					return this.left + this.speed < obstacle.x + obstacle.width;
			}
		}
	},
	computed: {
		coordX() {
			return this.left + this.width / 2;
		},
		coordY() {
			return this.top + this.height / 2;
		}
	}
}
</script>
<style scoped>
.car {
	transition: top 1s ease-out, left 1s ease-out;
	position: absolute;
}
</style>