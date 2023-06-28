### 找到vite.config.ts配置文件

```typescript
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
//引入node提供的内置模块path，可以获取绝对路径
import path from 'path';


// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  //src配置别名
  resolve:{
    alias:{
      "@":path.resolve(__dirname,'src')
    } 
  }
})
```
安装依赖 npn install @type/node    消除错误依赖
```typescript
npn install @type/node 
```
找到配置文件tsconfig.json文件，编译项添加配置项
```typescript
    "baseUrl": ".",
    "paths": {
      "@/*": ["src/*"]
    },
```
