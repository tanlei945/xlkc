<template>
  <a-card :bordered="false">

    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">

          <a-col :md="6" :sm="8">
            <a-form-item label="联系我们的电话">
              <a-input placeholder="请输入联系我们的电话" v-model="queryParam.contactPhone"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="公司邮箱">
              <a-input placeholder="请输入公司邮箱" v-model="queryParam.companyMail"></a-input>
            </a-form-item>
          </a-col>
        <template v-if="toggleSearchStatus">
        <a-col :md="6" :sm="8">
            <a-form-item label="公司地址">
              <a-input placeholder="请输入公司地址" v-model="queryParam.companyAddress"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="公司版本号">
              <a-input placeholder="请输入公司版本号" v-model="queryParam.versionsNumber"></a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="8">
            <a-form-item label="公司logo图标">
              <a-input placeholder="请输入公司logo图标" v-model="queryParam.companyLogo"></a-input>
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
    <aboutUs-modal ref="modalForm" @ok="modalFormOk"></aboutUs-modal>
  </a-card>
</template>

<script>
  import AboutUsModal from './modules/AboutUsModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: "AboutUsList",
    mixins:[JeecgListMixin],
    components: {
      AboutUsModal
    },
    data () {
      return {
        description: '关于我们和联系我们管理管理页面',
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
            title: '联系我们的电话',
            align:"center",
            dataIndex: 'contactPhone'
           },
		   {
            title: '公司邮箱',
            align:"center",
            dataIndex: 'companyMail'
           },
		   {
            title: '公司地址',
            align:"center",
            dataIndex: 'companyAddress'
           },
		   {
            title: '公司版本号',
            align:"center",
            dataIndex: 'versionsNumber'
           },
		   {
            title: '公司logo图标',
            align:"center",
            dataIndex: 'companyLogo'
           },
		   {
            title: '微信二维码',
            align:"center",
            dataIndex: 'wechatQrcode'
           },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            scopedSlots: { customRender: 'action' },
          }
        ],
		url: {
          list: "/aboutus/aboutUs/list",
          delete: "/aboutus/aboutUs/delete",
          deleteBatch: "/aboutus/aboutUs/deleteBatch",
          exportXlsUrl: "aboutus/aboutUs/exportXls",
          importExcelUrl: "aboutus/aboutUs/importExcel",
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