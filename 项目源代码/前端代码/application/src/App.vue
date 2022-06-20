<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style scoped>

</style>

<style>
  html body {
    height: calc(100vh);
  }

  ::-webkit-scrollbar {
    width: 7px;
    height: 5px;
    border-radius:15px;
    -webkit-border-radius:  15px;
  }
  ::-webkit-scrollbar-track-piece {
    background-color: #ffff;
    border-radius:15px;
    -webkit-border-radius:  15px;
  }
  ::-webkit-scrollbar-thumb:vertical {
    height: 5px;
    background-color: rgba(144, 147, 153, 0.5);
    border-radius: 15px;
    -webkit-border-radius:  15px;
  }
  ::-webkit-scrollbar-thumb:horizontal {
    width: 7px;
    background-color: rgba(144, 147, 153, 0.5);
    border-radius: 15px;
    -webkit-border-radius: 15px;
  }
</style>

<script>
import { SET_TAB } from '@/store/mutation_name'

export default {
  name: 'App',
  methods: {
  },
  created () {
  },
  watch: {
    $route (to, from) {
      if (to.path !== '/login') {
        console.log(to)
        const tab = {
          name: to.fullPath,
          title: to.meta.title
        }
        this.$store.commit({
          type: SET_TAB,
          tab: tab
        })
      }
    }
  }
}
</script>
