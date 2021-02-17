JvavScript
================

语言: [English](https://github.com/CrispyXYZ/JvavScript/) | [简体中文](https://github.com/CrispyXYZ/JvavScript/blob/main/README-zh.md)

## 这是什么？

JvavScript是一种交互式和脚本式编程语言。
帮助文档： [wiki](https://github.com/CrispyXYZ/JvavScript/wiki/Documentation#%E5%B8%AE%E5%8A%A9%E6%96%87%E6%A1%A3-%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87)

## 如何运行？

### Linux/Windows/Mac OS

在[Releases](https://github.com/CrispyXYZ/JvavScript/releases/)中下载JvavScript.jar并运行它(需要JRE 8+)

用法：
```ruby
java -jar JvavScript.jar [--debug|-d] [脚本文件]

  -d, --debug:   启用调试模式。
```

### Android

在[Releases](https://github.com/crispyXYZ/JvavScript/releases/)中下载JvavScript.dex并在终端中（例如[termux](https://termux.com/)）运行它

用法：
```ruby
dalvikvm -cp JvavScript.dex com.crispyxyz.jvavscript.Main [--debug|-d] [脚本文件]

  -d, --debug:   启用调试模式。
```

## 如何构建？

在IDEA中打开这个项目然后Run。

