1. static：
2. relative： 相对于父类进行布局
3. absolute：相对于页面进行布局，通常使用最多的
4. fixed：相对于屏幕进行布局，可以实现浮动的元素
5. stick：relative+fixed，元素可以在什么位置浮动在屏幕上

### 盒子模型与定位
CSS 盒子模型是一种用于描述文档中元素布局的概念。它将 HTML 元素视为一个矩形盒子，包括内容区域、内边距、边框和外边距。这些属性都可以通过 CSS 进行控制。
1. 内容区域（content）：指元素的实际内容，例如文本或图片。内容区域的大小可以通过设置 width 和 height 属性来改变。
2. 内边距（padding）：指内容区域与边框之间的距离。内边距的大小可以通过设置 padding 属性来改变。
3. 边框（border）：指围绕内容区域和内边距的线条。边框的样式、宽度和颜色可以通过设置 border 属性来改变。
4. 外边距（margin）：指边框与相邻元素之间的距离。外边距的大小可以通过设置 margin 属性来改变。

### 响应式布局
1. 使用媒体查询，@media screen and (min-device-width:100px) and (max-device-width:200px){}
2. flex-basis: 设置弹性盒子的伸缩基准值
3. flex-frow: 扩展比例，flex-shrink缩小比例
4. 
