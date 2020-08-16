<template>
  <div id="app">
    <game-canvas></game-canvas>
  </div>
</template>

<script>
import GameCanvas from "./components/GameCanvas";
export default {
  name: 'App',
  components: {
    GameCanvas
  }
}
</script>

<style>
#app {
  height: calc(100vh - 16px);
}
</style>
