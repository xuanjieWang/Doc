

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
#### 开启水世界地图
```js

    var tee = Cesium.createWorldTerrain({
        requestWaterMask: true,
        requestVertexNormals: true,
    });
    viewer.terrainProvider = tee
```
#### 多种地图
```js
    //默认地图
    // var ell = new Cesium.EllipsoidTerrainProvider();
    // viewer.terrainProvider = ell

    //arcGis地形
    // var arc = new Cesium.ArcGISTiledElevationTerrainProvider();
    // viewer.terrainProvider = arc

    //世界地形
    // var cesium = new Cesium.CesiumTerrainProvider()
    // viewer.terrainProvider = cesium

    //加载水地图
    // var tee = Cesium.createWorldTerrainAsync({
    //     requestWaterMask: true,
    //     requestVertexNormals: true,
    // });
    // viewer.terrainProvider = tee
```

#### 其他参数
```js
 viewer.scene.globe.depthTestAgainstTerrain = true; //开启深度测试
    viewer.scene.globe.enableLighting = true;   //对大雾和雾启动照明效果
    // viewer.scene.globe.terrainExaggeration = 2.0;   //相对高度
    // viewer.scene.globe.terrainExaggerationRelativeHeight = 3000;
```
####  imagery创建影响图层类
1. imagerLayerCollection类 可以装载放置多个imagerLayer或者imagerprovide类对象
2. imaferLayer：设置透明度亮度
3. imagerProvider类：核心类
4. UrlTemplateImageryProvider：通过url加载地图

#### 加载高德地图
```js
    var layer = new Cesium.UrlTemplateImageryProvider({
        url: "http://webrd02.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}",
        minimumLevel: 4,
        maximumLevel: 18
    })
    viewer.imageryLayers.addImageryProvider(layer);
```

#### 加载OSM地图
```js
    var osm = new Cesium.OpenStreetMapImageryProvider({
        url: "https://a.tile.openstreetmap.org/",
        minimumLevel: 0,
        maximumLevel: 18,
        fileExtension: "png"
    })
    viewer.imageryLayers.addImageryProvider(osm)

```


###### Cesium将图叫做imagery，但是不包括卫星影响数据，还包括互联网地图，tms，wms，wmts单个图片
