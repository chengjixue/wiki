<template>
  <a-layout>
    <a-layout-content style="background:#fff;padding:24px;margin: 0;minHeight:280px" class="ant-layout-content">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-button type="primary" @click="handleQuery()" size="large">查询</a-button>
          </a-form-item>
          <a-button type="primary" @click="add()" size="large">添加</a-button>
        </a-form>

      </p>

      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar"/>
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除？"
                ok-text="是"
                cancel-text="否 "
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>

  </a-layout>

  <a-modal
      title="文档表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="doc" :label-col="{ span: 6 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name"/>
      </a-form-item>
      <a-form-item label="名称">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeSelectedData"
            placeholder="请选择父文档"
            tree-default-expand-all
            :replaceFields="{title: 'name', value: 'id', key: 'id'}"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="父文档">
        <a-input v-model:value="doc.parent"/>
        <a-select
            v-model:value="doc.parent"
            ref="select"
        >
          <a-select-option :value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
            {{ c.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort"/>
      </a-form-item>
      <a-form-item label="内容">
        <div id="content"></div>
      </a-form-item>
    </a-form>
  </a-modal>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import E from 'wangeditor';


export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route =useRoute();
    console.log("路由",route);
    console.log("路由",route.query);
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    const columns = [
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '父文档',
        key: 'parent',
        dataIndex: 'parent',
      },
      {
        title: '顺序',
        dataIndex: 'sort'
      },
      {
        title: 'Action',
        key: 'action',
        slots: {customRender: 'action'}
      }
    ];
    /*
    * 一级文档树，children是二级文档
    * [{
    * id:"",
    * name:"",
    * children:[{
    * id:"",
    * name:"",
    * }]
    * */
    const level1 = ref();
    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      //如果不清空数据，则编辑保存重新加载数据后在点编辑会显示未编辑前的数据
      level1.value = [];
      axios.get("/doc/all",).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("原始数据", docs.value);
          //构建一级文档树
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };
    //--------表单-------------
    //因为树选择组件的属性状态，会随当前编辑的节点而变化,所以单独声明一个响应式变量
    const treeSelectedData = ref();
    treeSelectedData.value = [];
    const doc = ref({});
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const editor=new E('#content');


    const handleModalOK = () => {
      modalLoading.value = true;
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data => CommonResp
        if (data.success) {
          modalVisible.value = false;
          // 重新加载列表
          handleQuery();
        }
      });
    }
    /*
    * 将某节点及其子孙节点全部设置为disabled
    * */
    const setDisabled = (treeSelectedData: any, id: any) => {
      //  遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectedData.length; i++) {
        const node = treeSelectedData[i];
        if (node.id === id) {
          //如果当前就是目标节点
          console.log("disabled", node);
          //将目标节点设置为disabled
          node.disabled = true;
          // 如果有子节点，则递归调用，将其子节点设置为disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              const child = children[j];
              setDisabled(children, children[j].id);
            }
          }
        } else {
          //  如果当前目标节点不是目标节点，则其子系节点在找
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisabled(children, id);
          }
        }
      }
    };

 /*
    * 查找整根树枝
    * */
    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectedData: any, id: any) => {
      //  遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectedData.length; i++) {
        const node = treeSelectedData[i];
        if (node.id === id) {
          //如果当前就是目标节点
          console.log("delete", node);
          //将目标id放入结果集ids
          // node.disabled = true;
          ids.push(id);
          // 如果有子节点，则递归调用
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              const child = children[j];
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          //  如果当前目标节点不是目标节点，则其子系节点在找
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };


    // 编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      doc.value = Tool.copy(record);
      //不能选择当前节点及其所有子孙节点作为父节点，会导致树断开
      treeSelectedData.value = Tool.copy(level1.value);
      setDisabled(treeSelectedData.value, record.id);
      //  为选择树添加一个无
      treeSelectedData.value.unshift({
        id: 0,
        name: "无"
      });
      setTimeout(function () {
        editor.create();
      }, 100);
    }
    //添加
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        ebookId:route.query.ebookId
      };

      treeSelectedData.value = Tool.copy(level1.value);

      // 为选择树添加一个无
      treeSelectedData.value.unshift({
        id: 0,
        name: "无"
      });
      setTimeout(function () {
        editor.create();
      }, 100);
    }
    //删除

    const handleDelete = (id: number) => {
     console.log(level1,level1.value, id);
      getDeleteIds(level1.value, id);
      console.log("============="+ids);
      axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
        const data = response.data; // data => CommonResp
        if (data.success) {
          // 重新加载列表
          handleQuery();
        }
      });
    }
    onMounted(() => {

      handleQuery();
    });
    return {
      param,
      // docs,
      level1,
      columns,
      loading,
      edit,
      add,
      doc,
      modalVisible,
      modalLoading,
      handleModalOK,
      handleDelete,
      handleQuery,
      treeSelectedData,
    }
  }
});
</script>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
