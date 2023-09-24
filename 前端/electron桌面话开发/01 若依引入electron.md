### 进入ruoyi-ui 按顺序执行安装以下4个插件
``` js
   yarn add electron 
 
   yarn add electron-devtools-installer
 
   yarn add electron-store
 
   yarn add vue-cli-plugin-electron-builder
```

###  在vue.conf.js 文件中查看添加这几行代码,或者是package.json文件中
  "main":"background.js"    //指定electron入口
  "electron:serve": "vue-cli-service electron:serve",  //开发时用的
  "electron:build": "vue-cli-service electron:build",   //打包时用的

``` js
  "main": "background.js",
  "scripts": {
    "dev": "vue-cli-service serve",
    "build:prod": "vue-cli-service build",
    "build:stage": "vue-cli-service build --mode staging",
    "electron:serve": "vue-cli-service electron:serve",
    "electron:build": "vue-cli-service electron:build",
    "electron:build:win32": "vue-cli-service electron:build --win --ia32",
    "preview": "node build/index.js --preview",
    "lint": "eslint --ext .js,.vue src"
  },
```
### 在ruoyi-ui/src目录下添加background.js文件
``` js
'use strict'
 
import { app, protocol, BrowserWindow, ipcMain } from 'electron'
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'
import installExtension, { VUEJS_DEVTOOLS } from 'electron-devtools-installer'
const isDevelopment = process.env.NODE_ENV !== 'production'
const Store = require('electron-store');
 
// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } }
])
 
async function createWindow() {
  // Create the browser window.
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      // Use pluginOptions.nodeIntegration, leave this alone
      // See nklayman.github.io/vue-cli-plugin-electron-builder/guide/security.html#node-integration for more info
      contextIsolation:false,     //上下文隔离
      enableRemoteModule: true,   //启用远程模块
      nodeIntegration: true, //开启自带node环境
      webviewTag: true,     //开启webview
      webSecurity: false,
      allowDisplayingInsecureContent: true,
      allowRunningInsecureContent: true
    }
  })
  win.maximize()
  win.show()
  win.webContents.openDevTools()
  ipcMain.on('getPrinterList', (event) => {
    //主线程获取打印机列表
    win.webContents.getPrintersAsync().then(data=>{
      win.webContents.send('getPrinterList', data);
    });
    //通过webContents发送事件到渲染线程，同时将打印机列表也传过去
 
  });
 
  if (process.env.WEBPACK_DEV_SERVER_URL) {
    // Load the url of the dev server if in development mode
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    // Load the index.html when not in development
    win.loadURL('app://./index.html')
   
 
  }
}
 
// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})
 
app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (BrowserWindow.getAllWindows().length === 0) createWindow()
})
 
// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', async () => {
  Store.initRenderer();
  if (isDevelopment && !process.env.IS_TEST) {
    // Install Vue Devtools
    try {
      await installExtension(VUEJS_DEVTOOLS)
    } catch (e) {
      console.error('Vue Devtools failed to install:', e.toString())
    }
  }
  createWindow()
})
 
// Exit cleanly on request from parent process in development mode.
if (isDevelopment) {
 
  if (process.platform === 'win32') {
    process.on('message', (data) => {
      if (data === 'graceful-exit') {
        app.quit()
      }
    })
  } else {
    process.on('SIGTERM', () => {
      app.quit()
    })
  }
}
```

### 修改接口 若依管理系统/开发环境
#VUE_APP_BASE_API = '/dev-api'

VUE_APP_BASE_API='http://localhost:8086'

### 注释掉clipboard.js文件

### npm run electron:build,然后安装可以看到可以正常使用
