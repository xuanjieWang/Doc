## 下载所有得依赖
pnpm install @element-plus/icons-vue

## 引入所有得图标，在main.js中引入
```
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
```
## 使用方式
 icon: "Promotion",//菜单文字左侧的图标,支持element-plus全部图标

