<template>
  <div class="inventory-list-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="keyword"
        placeholder="搜索产品名 / 仓库 / 分类"
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
          <el-icon><Plus /></el-icon> 新增库存
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ stats.totalCount }}</div>
          <div class="stat-label">库存品类</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #409eff;">{{ stats.totalQuantity }}</div>
          <div class="stat-label">库存总量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #e6a23c;">¥{{ stats.totalAmount }}</div>
          <div class="stat-label">库存总值</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #f56c6c;">{{ stats.lowStockCount }}</div>
          <div class="stat-label">库存不足</div>
        </div>
      </el-col>
    </el-row>

    <!-- 库存表格 -->
    <el-table
      :data="filteredData"
      style="width: 100%; margin-top: 16px"
      border
      stripe
      v-loading="tableLoading"
    >
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column prop="productName" label="产品名称" min-width="160" />
      <el-table-column prop="productCode" label="产品编码" width="140" />
      <el-table-column prop="warehouse" label="仓库" width="120" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="inventoryQuantity" label="库存数量" width="110" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.inventoryQuantity <= 10 ? 'danger' : scope.row.inventoryQuantity <= 50 ? 'warning' : 'success'" size="small">
            {{ scope.row.inventoryQuantity }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="unitPrice" label="单价" width="100" align="right">
        <template #default="scope">¥{{ scope.row.unitPrice }}</template>
      </el-table-column>
      <el-table-column prop="inventoryAmount" label="库存金额" width="120" align="right">
        <template #default="scope">
          <span style="color: #409eff; font-weight: 600;">¥{{ scope.row.inventoryAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="scope">
          <div class="btn-group">
            <el-button size="small" type="primary" @click="openEditDialog(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button size="small" type="warning" @click="handleRestock(scope.row)">
              <el-icon><Plus /></el-icon> 补货
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
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="产品编码" prop="productCode">
          <el-input v-model="formData.productCode" placeholder="请输入产品编码" />
        </el-form-item>
        <el-form-item label="仓库" prop="warehouse">
          <el-select v-model="formData.warehouse" placeholder="请选择仓库" style="width: 100%">
            <el-option label="主仓库" value="主仓库" />
            <el-option label="A仓" value="A仓" />
            <el-option label="B仓" value="B仓" />
            <el-option label="C仓" value="C仓" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-input v-model="formData.category" placeholder="请输入分类" />
        </el-form-item>
        <el-form-item label="库存数量" prop="inventoryQuantity">
          <el-input-number v-model="formData.inventoryQuantity" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="单价" prop="unitPrice">
          <el-input-number v-model="formData.unitPrice" :min="0" :precision="2" :step="10" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="formData.remark" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认</el-button>
      </template>
    </el-dialog>

    <!-- 补货弹窗 -->
    <el-dialog title="库存补货" v-model="restockVisible" width="400px">
      <el-form :model="restockForm" label-width="100px">
        <el-form-item label="当前库存">
          <el-input :value="restockForm.currentQty" disabled />
        </el-form-item>
        <el-form-item label="补货数量">
          <el-input-number v-model="restockForm.addQty" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="补货后">
          <el-input :value="restockForm.currentQty + (restockForm.addQty || 0)" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="restockVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmRestock">确认补货</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { getInventories, addInventory, updateInventory, deleteInventory } from '@/network/inventory';
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  Search, RefreshLeft, Plus, Edit, Delete
} from '@element-plus/icons-vue';

export default {
  name: 'InventoryList',
  components: { Search, RefreshLeft, Plus, Edit, Delete },
  setup() {
    const allData = ref([]);
    const keyword = ref('');
    const tableLoading = ref(false);
    const dialogVisible = ref(false);
    const restockVisible = ref(false);
    const dialogTitle = ref('新增库存');
    const isEdit = ref(false);
    const formRef = ref(null);
    const formData = ref({
      inventoryId: null, productName: '', productCode: '', warehouse: '',
      category: '', inventoryQuantity: 0, unitPrice: 0, remark: ''
    });
    const restockForm = ref({ inventoryId: null, currentQty: 0, addQty: 0 });

    const formRules = {
      productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }]
    };

    const filteredData = computed(() => {
      if (!keyword.value) return allData.value;
      const kw = keyword.value.toLowerCase();
      return allData.value.filter(item =>
        (item.productName || '').toLowerCase().includes(kw) ||
        (item.warehouse || '').toLowerCase().includes(kw) ||
        (item.category || '').toLowerCase().includes(kw)
      );
    });

    const stats = computed(() => {
      const list = allData.value;
      return {
        totalCount: list.length,
        totalQuantity: list.reduce((sum, e) => sum + (e.inventoryQuantity || 0), 0),
        totalAmount: list.reduce((sum, e) => sum + (e.inventoryAmount || 0), 0).toFixed(2),
        lowStockCount: list.filter(e => e.inventoryQuantity <= 10).length
      };
    });

    const fetchData = () => {
      tableLoading.value = true;
      getInventories()
        .then(res => { if (res.code === 0) allData.value = res.data || []; })
        .catch(() => {})
        .finally(() => { tableLoading.value = false; });
    };

    const openAddDialog = () => {
      dialogTitle.value = '新增库存';
      isEdit.value = false;
      formData.value = {
        inventoryId: null, productName: '', productCode: '', warehouse: '主仓库',
        category: '', inventoryQuantity: 0, unitPrice: 0, remark: ''
      };
      dialogVisible.value = true;
    };

    const openEditDialog = (row) => {
      dialogTitle.value = `编辑库存「${row.productName}」`;
      isEdit.value = true;
      formData.value = { ...row };
      dialogVisible.value = true;
    };

    const submitForm = () => {
      formRef.value.validate(valid => {
        if (!valid) return;
        const action = isEdit.value
          ? updateInventory(formData.value.inventoryId, formData.value)
          : addInventory(formData.value);
        action.then(res => {
          ElMessage.success(res.message || '操作成功');
          fetchData();
          dialogVisible.value = false;
        }).catch(() => {});
      });
    };

    const handleDelete = (row) => {
      ElMessageBox.confirm(`确定删除库存「${row.productName}」吗？`, '确认删除', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        deleteInventory(row.inventoryId).then(res => {
          ElMessage.success(res.message || '删除成功');
          fetchData();
        }).catch(() => {});
      }).catch(() => {});
    };

    const handleRestock = (row) => {
      restockForm.value = { inventoryId: row.inventoryId, currentQty: row.inventoryQuantity || 0, addQty: 0 };
      restockVisible.value = true;
    };

    const confirmRestock = () => {
      const newQty = restockForm.value.currentQty + restockForm.value.addQty;
      updateInventory(restockForm.value.inventoryId, {
        ...allData.value.find(e => e.inventoryId === restockForm.value.inventoryId),
        inventoryQuantity: newQty
      }).then(res => {
        ElMessage.success(res.message || '补货成功');
        fetchData();
        restockVisible.value = false;
      }).catch(() => {});
    };

    onMounted(() => { fetchData(); });

    return {
      allData, filteredData, keyword, tableLoading, dialogVisible, restockVisible,
      dialogTitle, isEdit, formData, formRef, formRules, restockForm, stats, fetchData,
      openAddDialog, openEditDialog, submitForm, handleDelete, handleRestock, confirmRestock
    };
  }
};
</script>

<style scoped>
.inventory-list-container { padding: 20px; }
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
