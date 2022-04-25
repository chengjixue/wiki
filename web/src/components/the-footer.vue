<template>
  <a-layout-footer style="text-align: center">
    Java电子书 <span v-show="user.id"> 欢迎：{{ user.name }}</span>
  </a-layout-footer>
</template>
<script lang="ts">
import {computed, defineComponent, onMounted} from 'vue';
import store from "@/store";
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);

    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log("websocket链接成功，状态码：", websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log("websocket收到消息：", event.data);
    };
    const onError = () => {
      console.log("websocket链接错误：", websocket.readyState);
    };
    const onClose = () => {
      console.log("websocket链接关闭：", websocket.readyState);
    };
    const initWebsocket = () => {
      //链接成功
      websocket.onopen = onOpen;
      //收到消息的回调
      websocket.onmessage = onMessage;
      //链接错误
      websocket.onerror = onError;
      //链接关闭
      websocket.onclose = onClose;
    };
    onMounted(() => {
      if ('WebSocket' in window) {
        token=Tool.uuid(10);
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebsocket();
      } else {
        alert("您的浏览器不支持websocket！");
      }

    });

    return {
      user
    }
  }
});
</script>