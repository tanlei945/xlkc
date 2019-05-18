<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="课程名称">
              <a-input placeholder="请输入课程名称" v-model="queryParam.courseName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="课程描述">
              <a-input placeholder="请输入课程描述" v-model="queryParam.comment"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="课程价格">
              <a-input placeholder="请输入课程价格" v-model="queryParam.price"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="课程人数">
              <a-input placeholder="请输入课程人数" v-model="queryParam.num"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="上课时间">
              <a-input placeholder="请输入上课时间" v-model="queryParam.starttime"></a-input>
            </a-form-item>
          </a-col>
        </template>
          <a-col :md="6" :sm="8" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        @change="handleTableChange">

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <courseDTO-modal ref="modalForm" @ok="modalFormOk"></courseDTO-modal>
  </a-card>
</template>

<script>
  import CourseDTOModal from './modules/CourseDTOModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "CourseDTOList",
    mixins:[JeecgListMixin],
    components: {
      CourseDTOModal
    },
    data () {
      return {
        description: '课程模块管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
           },
		   {
            title: '课程名称',
            align:"center",
            dataIndex: 'courseName'
           },
		   {
            title: '课程描述',
            align:"center",
            dataIndex: 'comment'
           },
		   {
            title: '课程价格',
            align:"center",
            dataIndex: 'price'
           },
		   {
            title: '课程人数',
            align:"center",
            dataIndex: 'num'
           },
		   {
            title: '上课时间',
            align:"center",
            dataIndex: 'starttime'
           },
		   {
            title: '结束时间',
            align:"center",
            dataIndex: 'endtime'
           },
		   {
            title: '上课地址',
            align:"center",
            dataIndex: 'address'
           },
		   {
            title: '语种 0/普通话 1/粤语',
            align:"center",
            dataIndex: 'language'
           },
		   {
            title: '上课简介',
            align:"center",
            dataIndex: 'intro'
           },
		   {
            title: '课程部分内容',
            align:"center",
            dataIndex: 'courseContent'
           },
		   {
            title: '课程图片地址',
            align:"center",
            dataIndex: 'pictureUrl'
           },
		   {
            title: '课程视频地址',
            align:"center",
            dataIndex: 'videoUrl'
           },
		   {
            title: '课程预约确认 1/确认 0/待定',
            align:"center",
            dataIndex: 'courseVerify'
           },
		   {
            title: '课程退款 0/可以申请退款 1/退款 2/退款完成',
            align:"center",
            dataIndex: 'courseRefund'
           },
		   {
            title: '课程完成 1/已完成 0/未完成',
            align:"center",
            dataIndex: 'achieve'
           },
		   {
            title: '创建人时间',
            align:"center",
            dataIndex: 'createTiem'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/course/courseDTO/list",
          delete: "/course/courseDTO/delete",
          deleteBatch: "/course/courseDTO/deleteBatch",
          exportXlsUrl: "course/courseDTO/exportXls",
          importExcelUrl: "course/courseDTO/importExcel",
       },
    }
  },
  computed: {
    importExcelUrl: function(){
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
    }
  },
    methods: {
     
    }
  }
</script>
<style lang="less" scoped>
/** Button按钮间距 */
  .ant-btn {
    margin-left: 3px
  }
  .ant-card-body .table-operator{
    margin-bottom: 18px;
  }
  .ant-table-tbody .ant-table-row td{
    padding-top:15px;
    padding-bottom:15px;
  }
  .anty-row-operator button{margin: 0 5px}
  .ant-btn-danger{background-color: #ffffff}

  .ant-modal-cust-warp{height: 100%}
  .ant-modal-cust-warp .ant-modal-body{height:calc(100% - 110px) !important;overflow-y: auto}
  .ant-modal-cust-warp .ant-modal-content{height:90% !important;overflow-y: hidden}
</style>