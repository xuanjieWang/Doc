## entity实体

```js
  //写法1
  const point = new Cesium.Entity({
      position: Cesium.Cartesian3.fromDegrees(120, 20),
      point: {
          pixelSize: 10,
          color: Cesium.Color.RED
      }
  })
  viewer.entities.add(point)
  viewer.zoomTo(point)
  //写法2推荐
  viewer.entities.add({
      id: 'point',
      position: Cesium.Cartesian3.fromDegrees(121, 20),
      point: {
          pixelSize: 10,
          color: Cesium.Color.WHITE
      }
  })
```
## 标注
```js
 viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(113, 20, 10),

        //图标
        // billboard: {
        // image: '/src/assets/wxj.jpg',
        // scale: 0.5,
        // color: Cesium.Color.BLUE
        // }

        //文字
        // label: {
        //     text: 'Cesium',
        //     fillColor: Cesium.Color.YELLOWGREEN,
        //     showBackground: true,
        //     backgroundColor: new Cesium.Color(255, 255, 0   )
        // }

        //线
        // polyline: {
        //     positions: Cesium.Cartesian3.fromDegreesArray([114, 20, 115, 20, 115, 21]),
        //     width: 5,
        //     material: Cesium.Color.YELLOW
        // }

        //多边形
        // polygon: {
        //     hierarchy: {
        //         positions: Cesium.Cartesian3.fromDegreesArray([114, 20, 115, 20, 115, 21]),
        //     },
        //     material: Cesium.Color.RED,
        //     height: 100000,
        //     extrudedHeight: 100000,
        //     outline: true,
        //     outlineColor: Cesium.Color.WHITE,
        //     fill: false
        // }

        //盒子
        positions: Cesium.Cartesian3.fromDegreesArray([114, 20, 115, 20, 115, 21]),
        box: {
            dimensions: new Cesium.Cartesian3(2000, 1000, 3000),
            material: Cesium.Color.BLUE
        }

    })
```
#### 删除实体,添加一个按钮进行删除
1. viewer.remove(point)  删除指定点
2. viewer.removeall()    删除所有的点
3. viewer.removeById()   根据id删除
4. 将一个数组当作图层使用，一个分类的数据存放在一个类别中，循环删除数组中的内容
```js

```
