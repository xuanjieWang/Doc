## 使用vue2引入tailwindCss
1. 安装依赖npm install -D tailwindcss@1 @tailwindcss/postcss7-compat
2. 生成配置文件 npx tailwindcss init # 空的
3. 创建postcss.config.js文件
4. main.js中引入样式

```js

npm install -D tailwindcss@1 @tailwindcss/postcss7-compat

npx tailwindcss init # 空的

//postcss.config.js    配置文件开头是以tw-开始的
module.exports = {
  mode: "jit",
  prefix: "tw-",
  content: ["./src/components/**/*.{vue,js,jsx}", "./src/views/**/*.{vue,js,jsx}"],
  theme: {
    extend: {},
  },
  plugins: [],
};


//main.js
import "tailwindcss/tailwind.css";



```
