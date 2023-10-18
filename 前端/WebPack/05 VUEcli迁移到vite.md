## 1. 步骤#1：更新依赖项
```js
// package.json
"@vue/cli-plugin-babel": "~4.5.0", // remove
"@vue/cli-plugin-eslint": "~4.5.0", // remove
"@vue/cli-plugin-router": "~4.5.0", // remove
"@vue/cli-plugin-vuex": "~4.5.0", // remove
"@vue/cli-service": "~4.5.0", // remove

// package.json
"sass-loader": "^8.0.2" // remove
```
我们还可以删除 sass-loader，因为 Vite 为最常见的预处理器提供了开箱即用的内置支持。这将使我们能够继续使用我们选择的 CSS 预处理器。</br>
请注意，Vite 建议将原生 CSS 变量与 PostCSS 插件一起使用，这些插件可以实现 CSSWG 草稿并编写简单的、符合未来标准的 CSS。
```js
// package.json
"@vitejs/plugin-vue": "^1.6.1",
"vite": "^2.5.4",
```

#### 此外，由于我们正在迁移 Vue 2 项目，除了官方的 Vue 插件之外，我们还需要包含社区维护的 Vue 2 插件。如果我们使用 Vue 3，那就没有必要了。
```js
"vite-plugin-vue2" : "1.9.0" // add for Vue 2

// package.json
"vue-template-compiler": "^2.6.11" //remove (SFC support provided by vite vue plugin)
```
## 2.仅为现代浏览器提供支持

```
由于 Vite 是下一代构建工具，让我们乐观地继续只支持最现代的浏览器。这将使我们的构建尽可能精简和快速。

实际上，这意味着我们可以从依赖项中完全删除 Babel，因为大多数移动和桌面常青浏览器几乎完全支持所有 ES6 功能。如果您仍然需要支持像Internet Explorer 11这样的旧浏览器，Vite确实为此提供了一个官方插件。

因此，要删除 Babel，首先我们将删除该文件。babel.config.js

接下来，由于我们已经删除了需要 babel 本身的依赖项，我们只需要从 package.json 中删除其他几个与 babel 相关的依赖项。@vue/cli-plugin-babel
// package.json
"babel-eslint": "^10.1.0", // remove
"core-js": "^3.6.5", // remove


现在删除后，我们需要将其作为解析器从我们的文件中删除。babel-eslint.eslintrc

// .eslintrc
// remove
parserOptions: {
    parser: "babel-eslint",
},

```

## 步骤#3：添加Vite配置
```js
在此步骤中，让我们为 Vue.js 项目配置 Vite。Vite 是通过项目根目录中的文件配置的。这是使用 为 Vue 生成全新 Vite 项目时的默认文件的样子。vite.config.jsvite.config.jsnpm init vite@latest


// vite.config.js

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()]
})


我们想再添加 2 件事。

首先，我们将从 Vue 插件导入，而不是官方的 Vite Vue 插件。vite-plugin-vue2
// vite.config.js
import vue from '@vitejs/plugin-vue' // remove
import { createVuePlugin as vue } from "vite-plugin-vue2";



// vite.config.js
//...
const path = require("path");
export default defineConfig({
  //...
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
});

```


## 步骤#4：移动索引.html
```js
与 Vue CLI 相反，Vite 实际上将保存 Vue.js 应用程序的索引.html文件放在项目的根目录中而不是公共目录中，因此您需要移动它。

同样在索引中.html您需要进行一些更改。

首先，我们将占位符的实例更改为硬编码值。<%= htmlWebpackPlugin.options.title %>


// index.html

<!--remove-->
<title><%= htmlWebpackPlugin.options.title %></title> 
<!--add-->
<title>Hard Coded Title</title>

//...
<!--remove-->
<strong>We're sorry but <%= htmlWebpackPlugin.options.title %> doesn't work properly without JavaScript enabled. Please enable it to continue.</strong>
<!--add-->
<strong>We're sorry but this app doesn't work properly without JavaScript enabled. Please enable it to continue.</strong>

我们还需要将占位符替换为绝对路径。<%= BASE_URL %>
// index.html

<!--remove-->
<link rel="icon" href="<%= BASE_URL %>favicon.ico">
<!--add-->
<link rel="icon" href="/favicon.ico">

```

## 步骤#5：更新脚本
回到package.json，我们还需要更新脚本。我们将旧命令更改为 Vite 特定命令。vue-cli-service
```
// package.json
"serve": "vue-cli-service serve", // remove
"build": "vue-cli-service build", // remove
"dev": "vite",
"build": "vite build",
"serve": "vite preview",

请注意，启动开发服务器的命令不再是 。Vite 改为使用，用于在本地预览生产版本。servedevserve
此外，如果您启用了 linting，则应更新 lint 脚本以直接运行 eslint。

"lint": "eslint --ext .js,.vue --ignore-path .gitignore --fix src"

```

## 步骤#6：更新环境变量
```js
环境变量在 Vite 中的工作方式与它们在 Vue CLI 中的工作方式之间存在很多交叉。例如，您的 .env 命名约定可以保持不变。
.env                # loaded in all cases
.env.local          # loaded in all cases, ignored by git
.env.[mode]         # only loaded in specified mode
.env.[mode].local   # only loaded in specified mode, ignored by git

// router/index.js
base: process.env.BASE_URL, //remove
base: import.meta.env.BASE_URL,


此外，用于使声明客户端公开的 env 变量更明显的前缀更改为 ，因此如果您有任何此类环境变量，则必须相应地更新它们。VUE_APP_VITE_
```

## 步骤 #7：将 .vue 扩展名添加到 SFC 导入

```js
虽然我们新创建的 Vue CLI 项目已经这样做了，但我敢打赌你现有的应用程序可能不会这样做。因此，必须确保单个文件组件的所有导入都以扩展名结尾。.vue

// Home.vue
import HelloWorld from "@/components/HelloWorld.vue"; // .vue is required

如果此过程由于代码库的大小而过于压倒性，则可以配置 vite，以便不需要这样做。这是通过添加到 中的配置选项来实现的。确保还手动包含所有默认扩展名，因为此选项会覆盖默认值。.vueresolve.extensionsvite.config.js

// vite.config.js
//...
export default defineConfig({
  plugins: [vue()],
  resolve: {
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
    //...
  },
});
```

## 步骤#8：清理魔术评论

```js
最后，您可以删除所有用于命名动态导入的魔术注释，因为这些是特定于 webpack 的注释，对 Vite 没有任何意义。

// router/index.js
import(
    /* webpackChunkName: "about" */  // remove
    "../views/About.vue"
),

相反，Vite 将根据原始 .vue 文件名和缓存破坏哈希自动命名您的块，如下所示：About.37a9fa9f.js
```

