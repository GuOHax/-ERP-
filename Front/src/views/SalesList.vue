<template>
  <div class="sales-list-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="keyword"
        placeholder="搜索产品名 / 客户名 / 业务员"
        style="width: 260px"
        clearable
        @keyup.enter="fetchData"
      >
        <template #prefix><el-icon><Search /></el-icon></template>
      </el-input>
      <el-button type="primary" @click="fetchData">
        <el-icon><Search /></el-icon> 查询
      </el-button>
      <el-button @click="keyword = ''; fetchData()">
        <el-icon><RefreshLeft /></el-icon> 重置
      </el-button>
      <div class="toolbar-right">
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon> 新增销售
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ stats.totalCount }}</div>
          <div class="stat-label">销售单数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #67c23a;">¥{{ stats.totalAmount }}</div>
          <div class="stat-label">销售总额</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #409eff;">{{ stats.totalQuantity }}</div>
          <div class="stat-label">销售总量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #e6a23c;">{{ stats.todayCount }}</div>
          <div class="stat-label">今日单数</div>
        </div>
      </el-col>
    </el-row>

    <!-- 销售表格 -->
    <el-table
      :data="filteredData"
      style="width: 100%; margin-top: 16px"
      border
      stripe
      v-loading="tableLoading"
    >
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column prop="salesNo" label="单据编号" width="160" />
      <el-table-column prop="productName" label="产品名称" min-width="150" />
      <el-table-column prop="customerName" label="客户名称" width="140" />
      <el-table-column prop="salesAmount" label="销售金额" width="120" align="right">
        <template #default="scope">
          <span style="color: #67c23a; font-weight: 600;">¥{{ scope.row.salesAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="salesQuantity" label="数量" width="80" align="center" />
      <el-table-column prop="salesMan" label="业务员" width="100" />
      <el-table-column prop="salesDate" label="销售日期" width="110">
        <template #default="scope">{{ formatDate(scope.row.salesDate) }}</template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <div class="btn-group">
            <el-button size="small" type="primary" @click="openEditDialog(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增 / 编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="560px" destroy-on-close>
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="110px">
        <el-form-item label="单据编号" prop="salesNo">
          <el-input v-model="formData.salesNo" :disabled="isEdit" placeholder="系统自动生成" />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="formData.customerName" placeholder="请输入客户名称" />
        </el-form-item>
        <el-form-item label="销售金额" prop="salesAmount">
          <el-input-number v-model="formData.salesAmount" :min="0" :precision="2" :step="100" style="width: 100%" placeholder="0.00" />
        </el-form-item>
        <el-form-item label="销售数量" prop="salesQuantity">
          <el-input-number v-model="formData.salesQuantity" :min="1" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="销售日期" prop="salesDate">
          <el-date-picker v-model="formData.salesDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="业务员" prop="salesMan">
          <el-input v-model="formData.salesMan" placeholder="请输入业务员姓名" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="formData.remark" :rows="2" placeholder="备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getSalesList, addSales, updateSales, deleteSales } from '@/network/sales';
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { formatDate } from '@/common/utils';
import {
  Search, RefreshLeft, Plus, Edit, Delete
} from '@element-plus/icons-vue';

export default {
  name: 'SalesList',
  components: { Search, RefreshLeft, Plus, Edit, Delete },
  setup() {
    const allData = ref([]);
    const keyword = ref('');
    const tableLoading = ref(false);
    const dialogVisible = ref(false);
    const dialogTitle = ref('新增销售');
    const isEdit = ref(false);
    const formRef = ref(null);
    const formData = ref({
      salesId: null, salesNo: '', productName: '', customerName: '',
      salesAmount: 0, salesQuantity: 1, salesDate: '', salesMan: '', remark: ''
    });

    const formRules = {
      productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }]
    };

    const filteredData = computed(() => {
      if (!keyword.value) return allData.value;
      const kw = keyword.value.toLowerCase();
      return allData.value.filter(item =>
        (item.productName || '').toLowerCase().includes(kw) ||
        (item.customerName || '').toLowerCase().includes(kw) ||
        (item.salesMan || '').toLowerCase().includes(kw)
      );
    });

    const stats = computed(() => {
      const today = new Date().toISOString().slice(0, 10);
      const list = allData.value;
      return {
        totalCount: list.length,
        totalAmount: (list.reduce((sum, e) => sum + (e.salesAmount || 0), 0)).toFixed(2),
        totalQuantity: list.reduce((sum, e) => sum + (e.salesQuantity || 0), 0),
        todayCount: list.filter(e => (e.salesDate || '').startsWith(today)).length
      };
    });

    const fetchData = () => {
      tableLoading.value = true;
      getSalesList()
        .then(res => { if (res.code === 0) allData.value = res.data || []; })
        .catch(() => {})
        .finally(() => { tableLoading.value = false; });
    };

    const openAddDialog = () => {
      dialogTitle.value = '新增销售';
      isEdit.value = false;
      formData.value = {
        salesId: null, salesNo: 'XS' + Date.now(), productName: '', customerName: '',
        salesAmount: 0, salesQuantity: 1, salesDate: new Date().toISOString().slice(0, 19).replace('T', ' '), salesMan: '', remark: ''
      };
      dialogVisible.value = true;
    };

    const openEditDialog = (row) => {
      dialogTitle.value = `编辑销售「${row.productName}」`;
      isEdit.value = true;
      formData.value = { ...row };
      dialogVisible.value = true;
    };

    const submitForm = () => {
      formRef.value.validate(valid => {
        if (!valid) return;
        const action = isEdit.value
          ? updateSales(formData.value.salesId, formData.value)
          : addSales(formData.value);
        action.then(res => {
          ElMessage.success(res.message || '操作成功');
          fetchData();
          dialogVisible.value = false;
        }).catch(() => {});
      });
    };

    const handleDelete = (row) => {
      ElMessageBox.confirm(`确定删除销售记录「${row.productName}」吗？`, '确认删除', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        deleteSales(row.salesId).then(res => {
          ElMessage.success(res.message || '删除成功');
          fetchData();
        }).catch(() => {});
      }).catch(() => {});
    };

    onMounted(() => { fetchData(); });

    return {
      allData, filteredData, keyword, tableLoading, dialogVisible, dialogTitle,
      isEdit, formData, formRef, formRules, stats, fetchData,
      openAddDialog, openEditDialog, submitForm, handleDelete, formatDate
    };
  }
};
</script>

<style scoped>
.sales-list-container { padding: 20px; }
.toolbar { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.toolbar-right { margin-left: auto; }
.stat-cards { margin-bottom: 16px; }
.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px 16px;
  text-align: center;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
  border: 1px solid #f0f0f0;
}
.stat-value { font-size: 26px; font-weight: 700; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 6px; }
.btn-group { display: flex; flex-wrap: wrap; gap: 4px; align-items: center; }
</style>
