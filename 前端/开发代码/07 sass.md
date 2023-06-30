在src/styles目录下创建一个index.scss文件，当然项目中需要用到清除默认样式，因此在index.scss引入reset.scss
@import reset.scss
import '@/styles'


在style/variable.scss创建一个variable.scss文件！

在vite.config.ts文件配置如下:
```
export default defineConfig((config) => {
	css: {
      preprocessorOptions: {
        scss: {
          javascriptEnabled: true,
          additionalData: '@import "./src/styles/variable.scss";',
        },
      },
    },
	}
}
```
**`@import "./src/styles/variable.less";`后面的`;`不要忘记，不然会报错**!


