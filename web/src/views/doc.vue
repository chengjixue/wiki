<template>
  <a-layout>
    <a-layout-content style="background:#fff;padding:24px;margin: 0;minHeight:280px">
      <a-row>
        <a-col :span="6">
          <a-tree
              v-if="level1.length>0"
              :tree-data="level1"
              @select="onSelect"
              :replaceFields=" {title: 'name', value: 'id',key: 'id'}"
              :defaultExpandAllRows="true">
          </a-tree>
        </a-col>
        <a-clo :span="18">
        </a-clo>
      </a-row>
      <div class="doc">
        <h1>欢迎来到文档页面</h1>
      </div>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from "ant-design-vue";
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'AdminDoc',
  setup() {
    const route =useRoute();
    const docs = ref();


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
    level1.value = [];
    /**
     * 数据查询
     **/
    const handleQuery = () => {
      axios.get("/doc/all").then((response) => {
        const data = response.data;
        if (data.success) {
          docs.value = data.content;

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
        } else {
          message.error(data.message);
        }
      });
    };
    onMounted(() => {
      handleQuery();
    });
    return {
      level1,
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