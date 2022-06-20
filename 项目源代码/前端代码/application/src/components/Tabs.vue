<template>
  <div id="tabs">
    <el-tabs @tab-remove="removeTab" v-model="currentTab" type="card" editable @tab-click="clickTab">
      <el-tab-pane
        :key="item.name"
        v-for="(item, index) in tabs"
        :label="item.title"
        :name="item.name"
      >
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
export default {
  name: "Tabs",
  data() {
    return {

    }
  },
  methods: {
    clickTab(target) {
      this.$router.push(target.name)
    },

    removeTab(targetName) {
      let tabs = this.tabs;
      let currentTab = this.currentTab;

      // 若点击目标为首页，则不可删除
      if (targetName === '/home/index') {
        return
      }

      if (currentTab === targetName) {
        tabs.forEach((tab, index) => {
          // 若寻找到指定的标签页，则往后移一位
          if (tab.name === currentTab) {
            let targetTab = tabs[index - 1].name
            currentTab = targetTab
            this.$router.push(currentTab)
          }
        })
      }

      this.tabs = tabs.filter(item => item.name !== targetName)
      this.currentTab = currentTab
    },
  },
  computed: {
    tabs: {
      get() {
        return this.$store.state.menu.tabs
      },
      set(tabs) {
        this.$store.state.menu.tabs = tabs
      }
    },

    currentTab: {
      get() {
        return this.$store.state.menu.currentTab
      },
      set(currentTab) {
        this.$store.state.menu.currentTab = currentTab
      }
   }
  }
}
</script>

<style scoped>

</style>
