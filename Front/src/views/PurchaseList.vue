<template>
  <div class="purchase-list-container">
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-input
        v-model="keyword"
        placeholder="搜索产品名 / 供应商"
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
          <el-icon><Plus /></el-icon> 新增采购
        </el-button>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stat-cards">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ stats.totalCount }}</div>
          <div class="stat-label">采购单数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #f56c6c;">¥{{ stats.totalAmount }}</div>
          <div class="stat-label">采购总额</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #409eff;">{{ stats.totalQuantity }}</div>
          <div class="stat-label">采购总量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #e6a23c;">{{ stats.pendingCount }}</div>
          <div class="stat-label">待审核</div>
        </div>
      </el-col>
    </el-row>

    <!-- 采购表格 -->
    <el-table
      :data="filteredData"
      style="width: 100%; margin-top: 16px"
      border
      stripe
      v-loading="tableLoading"
    >
      <el-table-column type="index" label="#" width="60" align="center" />
      <el-table-column prop="purchaseNo" label="单据编号" width="160" />
      <el-table-column prop="productName" label="产品名称" min-width="150" />
      <el-table-column prop="supplierName" label="供应商" width="140" />
      <el-table-column prop="purchaseAmount" label="采购金额" width="120" align="right">
        <template #default="scope">
          <span style="color: #f56c6c; font-weight: 600;">¥{{ scope.row.purchaseAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="purchaseQuantity" label="数量" width="80" align="center" />
      <el-table-column prop="purchaser" label="采购员" width="100" />
      <el-table-column prop="purchaseStatus" label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag
            :type="scope.row.purchaseStatus === '已完成' ? 'success'
              : scope.row.purchaseStatus === '已取消' ? 'info'
              : 'warning'"
            size="small"
          >{{ scope.row.purchaseStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="purchaseDate" label="采购日期" width="110">
        <template #default="scope">{{ formatDate(scope.row.purchaseDate) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="250" fixed="right">
        <template #default="scope">
          <div class="btn-group">
            <el-button size="small" type="primary" @click="openEditDialog(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button size="small" type="success" @click="handleApprove(scope.row)" v-if="scope.row.purchaseStatus === '待审核'">
              <el-icon><Select /></el-icon> 审核
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
        <el-form-item label="单据编号" prop="purchaseNo">
          <el-input v-model="formData.purchaseNo" :disabled="isEdit" placeholder="系统自动生成" />
        </el-form-item>
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="formData.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="供应商" prop="supplierName">
          <el-input v-model="formData.supplierName" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item label="采购金额" prop="purchaseAmount">
          <el-input-number v-model="formData.purchaseAmount" :min="0" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="采购数量" prop="purchaseQuantity">
          <el-input-number v-model="formData.purchaseQuantity" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="采购日期" prop="purchaseDate">
          <el-date-picker v-model="formData.purchaseDate" type="datetime" value-format="YYYY-MM-DD HH:mm:ss" placeholder="选择日期时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="采购员" prop="purchaser">
          <el-input v-model="formData.purchaser" placeholder="请输入采购员姓名" />
        </el-form-item>
        <el-form-item label="状态" prop="purchaseStatus">
          <el-select v-model="formData.purchaseStatus" placeholder="请选择状态" style="width: 100%">
            <el-option label="待审核" value="待审核" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
          </el-select>
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
  </div>
</template>

<script>
import { getPurchases, addPurchase, updatePurchase, deletePurchase } from '@/network/purchase';
import { ref, computed, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { formatDate } from '@/common/utils';
import {
  Search, RefreshLeft, Plus, Edit, Delete, Select
} from '@element-plus/icons-vue';

export default {
  name: 'PurchaseList',
  components: { Search, RefreshLeft, Plus, Edit, Delete, Select },
  setup() {
    const allData = ref([]);
    const keyword = ref('');
    const tableLoading = ref(false);
    const dialogVisible = ref(false);
    const dialogTitle = ref('新增采购');
    const isEdit = ref(false);
    const formRef = ref(null);
    const formData = ref({
      purchaseId: null, purchaseNo: '', productName: '', supplierName: '',
      purchaseAmount: 0, purchaseQuantity: 1, purchaseDate: '', purchaser: '',
      purchaseStatus: '待审核', remark: ''
    });

    const formRules = {
      productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }]
    };

    const filteredData = computed(() => {
      if (!keyword.value) return allData.value;
      const kw = keyword.value.toLowerCase();
      return allData.value.filter(item =>
        (item.productName || '').toLowerCase().includes(kw) ||
        (item.supplierName || '').toLowerCase().includes(kw)
      );
    });

    const stats = computed(() => {
      const list = allData.value;
      return {
        totalCount: list.length,
        totalAmount: list.reduce((sum, e) => sum + (e.purchaseAmount || 0), 0).toFixed(2),
        totalQuantity: list.reduce((sum, e) => sum + (e.purchaseQuantity || 0), 0),
        pendingCount: list.filter(e => e.purchaseStatus === '待审核').length
      };
    });

    const fetchData = () => {
      tableLoading.value = true;
      getPurchases()
        .then(res => { if (res.code === 0) allData.value = res.data || []; })
        .catch(() => {})
        .finally(() => { tableLoading.value = false; });
    };

    const openAddDialog = () => {
      dialogTitle.value = '新增采购';
      isEdit.value = false;
      formData.value = {
        purchaseId: null, purchaseNo: 'CG' + Date.now(), productName: '', supplierName: '',
        purchaseAmount: 0, purchaseQuantity: 1, purchaseDate: new Date().toISOString().slice(0, 19).replace('T', ' '),
        purchaser: '', purchaseStatus: '待审核', remark: ''
      };
      dialogVisible.value = true;
    };

    const openEditDialog = (row) => {
      dialogTitle.value = `编辑采购「${row.productName}」`;
      isEdit.value = true;
      formData.value = { ...row };
      dialogVisible.value = true;
    };

    const submitForm = () => {
      formRef.value.validate(valid => {
        if (!valid) return;
        const action = isEdit.value
          ? updatePurchase(formData.value.purchaseId, formData.value)
          : addPurchase(formData.value);
        action.then(res => {
          ElMessage.success(res.message || '操作成功');
          fetchData();
          dialogVisible.value = false;
        }).catch(() => {});
      });
    };

    const handleDelete = (row) => {
      ElMessageBox.confirm(`确定删除采购记录「${row.productName}」吗？`, '确认删除', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(() => {
        deletePurchase(row.purchaseId).then(res => {
          ElMessage.success(res.message || '删除成功');
          fetchData();
        }).catch(() => {});
      }).catch(() => {});
    };

    const handleApprove = (row) => {
      ElMessageBox.confirm(`确定审核通过采购单「${row.productName}」吗？`, '确认审核', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'info'
      }).then(() => {
        updatePurchase(row.purchaseId, { ...row, purchaseStatus: '已完成' }).then(res => {
          ElMessage.success(res.message || '审核成功');
          fetchData();
        }).catch(() => {});
      }).catch(() => {});
    };

    onMounted(() => { fetchData(); });

    return {
      allData, filteredData, keyword, tableLoading, dialogVisible, dialogTitle,
      isEdit, formData, formRef, formRules, stats, fetchData,
      openAddDialog, openEditDialog, submitForm, handleDelete, handleApprove, formatDate
    };
  }
};
</script>

<style scoped>
.purchase-list-container { padding: 20px; }
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
