# 适用于在IntelliJ IDEA上开发用友NCC的插件

### 功能

1. 在一个功能中可以创建多个NCC模块，用友的eclipse版本插件一个项目是一个NCC模块，这样会导致导出的补丁模块不对，该插件可以将不同模块的代码一同打补丁。
2. 复制鉴权文件到Home。
3. 复制upm文件到Home。
4. 复制*.properties资源文件到Home。
5. OpenApi接口测试功能。

### 更新记录
<h3>1.0.6</h3>
<ul>
          <li>1. 修复第一次读取数据源报错，导致失败的问题</li>
</ul>

<h3>1.0.5</h3>
<ul>
          <li>1. 新增OpenApi接口测试功能</li>
</ul>

<h3>1.0.4</h3>
<ul>
          <li>1. 新增打开NC Home功能</li>
          <li>2. 新增打开NC配置功能</li>
          <li>3. 新增打开社区(https://nccdev.yonyou.com/index) 功能</li>
          <li>4. 兼容IntelliJ IDEA 2022.2.2</li>
</ul>

<h3>1.0.3</h3>
<ul>
            <li>1. 调整插件使用的api，避免新版本开发工具不兼容</li>
            <li>2. 导出补丁时默认勾选包含源代码</li>
            <li>3. 支持复制资源文件(*.properties)到home</li>
            <li>4. 修复部分情况下插件报错的问题</li>
</ul>

### 插件使用

1. 创建项目：
   创建一个空项目

![](https://s1.ax1x.com/2022/09/28/xm3L8g.md.png)

![](https://s1.ax1x.com/2022/09/28/xm3xrn.md.png)

2. 配置NCC环境

![](https://s1.ax1x.com/2022/09/28/xm3zbq.md.png)

![](https://s1.ax1x.com/2022/09/28/xm3O2Q.md.png)

3. 创建模块

![](https://s1.ax1x.com/2022/09/28/xm3Xvj.md.png)

![](https://s1.ax1x.com/2022/09/28/xm8pV0.md.png)

4. 在模块下创建组件

![](https://s1.ax1x.com/2022/09/28/xm8C5T.md.png)

![](https://s1.ax1x.com/2022/09/28/xm89aV.md.png)

5. 创建服务

![](https://s1.ax1x.com/2022/09/28/xm8FGF.md.png)

![](https://s1.ax1x.com/2022/09/28/xm8iPU.md.png)

5. 启动服务

![](https://s1.ax1x.com/2022/09/28/xm8eq1.md.png)

### 其他功能

1. 复制鉴权文件、upm文件到home

![](https://s1.ax1x.com/2022/09/28/xm8k24.md.png)

2. 复制*.properties资源文件到home

![](https://s1.ax1x.com/2022/09/28/xm8AxJ.md.png)

3. OpenApi接口测试

![](https://s1.ax1x.com/2022/09/28/xm8ZrR.md.png)

配置接口参数

![](https://s1.ax1x.com/2022/09/28/xm8nVx.md.png)

4. 导出补丁

![](https://s1.ax1x.com/2022/09/28/xm8ua6.md.png)

![](https://s1.ax1x.com/2022/09/28/xm8KIK.md.png)
