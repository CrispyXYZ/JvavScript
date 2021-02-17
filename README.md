JvavScript
================

Language: [English](https://github.com/CrispyXYZ/JvavScript/tree/kotlin) | [简体中文](https://github.com/CrispyXYZ/JvavScript/blob/kotlin/README-zh.md)

## What is this?

JvavScript is a interactive programming language.(just a joke)

This is the Kotlin version.

Documentation: [wiki](https://github.com/CrispyXYZ/JvavScript/wiki/Documentation#documentation-english)

## How to run?

### Linux/Windows/Mac OS

Download `JvavScriptKt.jar` in [Releases](https://github.com/CrispyXYZ/JvavScript/releases/) and run it. (Require JRE 8+)

Usage:
```ruby
java -jar JvavScriptKt.jar [--debug|-d] [scriptFile]

  -d, --debug:   Enable debug mode.
```

### Android

Download `JvavScriptKt.dex` in [Releases](https://github.com/crispyXYZ/JvavScript/releases/) and run it in a terminal such as [termux](https://termux.com/).

Usage:
```ruby
dalvikvm -cp JvavScriptKt.dex com.crispyxyz.jvavscript.Main [--debug|-d] [scriptFile]

  -d, --debug:   Enable debug mode.
```

## How to build?

Open this project in IDEA and run.

OR 

```ruby
git clone https://github.com/CrispyXYZ/JvavScript/
cd JvavScript
git checkout kotlin
make
```
Tip: run make dex to generate dex file (Require dx in android sdk).
