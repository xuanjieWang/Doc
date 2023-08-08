### fs模块，将txt文件输出到浏览器中

```js
const fs = require('fs')

/**
 * 读取文件
 * 参数1：文件路径
 * 参数2：字符类型
 * 参数3：回调函数
 *      参数1：失败的结果
 *      参数2：成功的结果
 */
/* fs.readFile(__dirname + '/test.txt', 'utf8', (err, data) => {
  if (!err) {
    console.log('data', data)
  } else {
    console.log('err', err)
  }
}) */

/**
 * 写入文件
 * 参数1：文件路径
 * 参数2：写入的内容
 * 参数3：回调函数
 *      参数1：失败的结果
 */
/* fs.writeFile(__dirname + '/test.txt', '这是替换后的文字', (err) => {
  if (!err) {
    console.log('写入成功')
  } else {
    console.log('err', err)
  }
}) */

/**
 * 追加文字
 * __dirname表示当前文件夹的目录
 */
fs.readFile(__dirname + '/test.txt', 'utf8', (err, data) => {
  if (!err) {
    const newData = data + '\n' + '这是追加的内容'
    fs.writeFile(__dirname + '/test.txt', newData, (err) => {
      if (!err) {
        console.log('追加成功')
      } else {
        console.log('err', err)
      }
    })
  } else {
    console.log('err', err)
  }
})

fs.readFile('./test.txt', 'utf-8', (err, data) => {
  if (err) {
    return console.log("读取文件失败！")
  } else {
    console.log("读取文件成功", data)
  }
})

```
