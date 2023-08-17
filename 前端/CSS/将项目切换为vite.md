## 移除    
```js
    "@vue/cli-plugin-babel": "4.4.6",
    "@vue/cli-plugin-eslint": "4.4.6",
    "@vue/cli-service": "4.4.6",
    "core-js": "3.25.3",


    "babel-eslint": "10.1.0",

    
    "sass-loader": "10.1.1",

    "vue-template-compiler": "2.6.12"

    //移除eslintrc中的
  // parserOptions: {
  //   parser: 'babel-eslint',
  //   sourceType: 'module'
  // },

```
## 添加
```js
    "@vitejs/plugin-vue": "^1.6.1",
    "vite": "^2.5.4",
    "vite-plugin-vue2" : "1.9.0",
```
、
    "dev": "vue-cli-service serve",
    "build:prod": "vue-cli-service build",
    "build:stage": "vue-cli-service build --mode staging",
    "preview": "node build/index.js --preview",
    "lint": "eslint --ext .js,.vue src"
    

    
