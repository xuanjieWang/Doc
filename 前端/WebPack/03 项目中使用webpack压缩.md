使用webpack压缩JS、CSS和HTML，并将图片组合到输出的文件中，可以通过以下步骤实现：

1. 安装相关插件

为了使用Webpack进行压缩和图片组合，您需要安装以下插件：

```
npm install --save-dev webpack webpack-cli css-loader style-loader html-webpack-plugin url-loader file-loader
```

其中：
- `webpack` 和 `webpack-cli`是Webpack核心依赖项，必须安装。
- `css-loader` 和 `style-loader`能够导入和处理CSS样式文件。
- `html-webpack-plugin`能够自动生成HTML文件，并将切割好的CSS、JS文件注入到HTML文件中。
- `url-loader` 和 `file-loader`用于将图片等文件转换成base64编码，并嵌入到输出的文件中。

2. 配置Webpack

创建一个名为webpack.config.js的文件，用于配置webpack。以下是一个基本的配置示例：

```javascript
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
  entry: {
    app: './src/index.js'
  },
  output: {
    filename: '[name].[chunkhash].js',
    path: path.resolve(__dirname, 'dist')
  },
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [ 'style-loader', 'css-loader']
      },
      {
        test: /\.(png|svg|jpg|jpeg|gif)$/,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 8192,  //小于8k的转化成base64
              name:'images/[name].[hash:8].[ext]' // 指定生成的文件路径和文件名
            }
          }
        ]
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      title: 'Webpack Template',
      filename: 'index.html',
      template: './src/index.html'
    })
  ]
};
```

上述配置中，`entry` 属性指定了入口文件为 `./src/index.js`。 `output` 属性指定了输出的目录和文件名，其中 `[name]` 表示入口文件的名称，`[chunkhash]` 表示模块的版本号，用于缓存验证。

该配置中还包含了两个 `module` 规则，一个用于处理CSS样式文件，另一个用于处理图片文件。CSS文件通过 `css-loader` 和 `style-loader` 进行处理，并注入到HTML页面中。图片文件通过 `url-loader` 和 `file-loader` 进行处理，小于 `8192bit` 的图片转换成 `base64` 编码，大于该大小的图片会被打包到 `images` 文件夹中。

`plugins` 部分包含了一个 `HtmlWebpackPlugin`。这个插件自动生成HTML文件，并在HTML文件中自动注入JS和CSS文件。

3. 执行Webpack

安装依赖和配置好webpack后，执行以下命令来打包和压缩：

```
npx webpack --mode production
```

该命令使用 `--mode` 来指定模式，可以为 `production` 或 `development`。生产环境下将会进行压缩和优化，而开发环境下则会进行更多的调试信息输出。

通过以上步骤，Webpack将会根据配置生成压缩过的JS、CSS和HTML文件，并嵌入图片文件。
