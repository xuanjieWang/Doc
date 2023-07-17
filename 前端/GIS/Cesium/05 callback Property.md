## 线段的自增长
1. 实现一条线段的自增长

```js
 //画一条渐进的直线
    let lon, lat, num = 0
    const line = viewer.entities.add({
        polyline: {
            positions: new Cesium.CallbackProperty(() => {
                num += 0.02
                lon = 120 + num
                lat = 30 + num
                if (lon < 130) return Cesium.Cartesian3.fromDegreesArray([120, 30, lon, lat])
                line.polyline.positions = Cesium.Cartesian3.fromDegreesArray([120, 30, 130, 40])
            }, false),
            width: 5,
            material: Cesium.Color.YELLOW
        }
    })

```

## 折线线段的自增长 
