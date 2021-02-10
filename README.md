JvavScript
================

## What is this?

JvavScript is a interactive programming language.(just a joke)

## How to run?

### Linux/Windows/Mac OS

Download JvavScript.jar in [Releases](https://github.com/CrispyXYZ/JvavScript/releases/) and run it. (Require JRE 8+)

Usage:
```ruby
java -jar JvavScript.jar [--debug|-d]

  -d, --debug:   Enable debug mode.
```

### Android

Download JvavScript.dex in [Releases](https://github.com/crispyXYZ/JvavScript/releases/) and run it in a terminal such as [termux](https://termux.com/).

Usage:
```ruby
dalvikvm -cp JvavScript.dex com.crispyxyz.jvavscript.Main [--debug|-d]

  -d, --debug:   Enable debug mode.
```

## How to compile?

(Require JDK 8+)
```ruby
git clone https://github.com/CrispyXYZ/JvavScript/
cd JvavScript
make
```
Tip: run `make dex` to generate dex file (Require dx in android sdk).
