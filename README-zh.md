JvavScript
================

语言: [English](https://github.com/CrispyXYZ/JvavScript/) | [简体中文](https://github.com/CrispyXYZ/JvavScript/README-zh.md)

## 这是什么？

JvavScript是一种交互式和脚本式编程语言。

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

## 如何编译？

(需要JDK 8+)
```ruby
git clone https://github.com/CrispyXYZ/JvavScript/
cd JvavScript
make
```
提示：使用`make dex`以生成dex文件(需要Android SDK中的dx)。