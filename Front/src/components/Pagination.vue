<!-- src/components/Pagination.vue -->
<template>
  <el-pagination
    background
    layout="prev, pager, next, jumper"
    :page-size="pageSize"
    :total="total"
    @current-change="currentChange"
  />
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    total: {
      type: Number,
      default: 0
    },
    pageSize: {
      type: Number,
      default: 10
    }
  },
  emits: ['current-change'],
  methods: {
    currentChange(page) {
      this.$emit('current-change', page);
    }
  }
};
</script>

<style scoped>
/* 可自行设定样式 */
</style>
