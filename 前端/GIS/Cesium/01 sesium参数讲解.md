

```js
<template>
    <div id="CesiumContainer"></div>
</template>
 
<script>
import * as Cesium from "cesium";
import "cesium/Source/Widgets/widgets.css";
import { onMounted } from 'vue'

export default {
    name: 'HelloWorld',
    setup() {
        onMounted(() => {
            let viewer = new Cesium.Viewer('CesiumContainer', {
                timeline: false,//时间轴
                animation: false,//动画按钮
                geocoder: false,//搜索按钮
                homeButton: false,//主页按钮
                sceneModePicker: false,//投影方式按钮
                baseLayerPicker: false,//图层选择
                navigationHelpButton: false,//帮助手势
                fullscreenButton: false,//全屏按钮

            })
        })
        return {}
    },
}
</script>
 
<style>
#CesiumContainer {
    width: 100vw;
    height: 100vh;
    overflow: hidden;
}

.cesium-viewer-bottom {
    display: none;
}
</style>
```
