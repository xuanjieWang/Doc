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
