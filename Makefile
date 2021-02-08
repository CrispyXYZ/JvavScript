JvavScript.jar: bin/com/crispyxyz/jvavscript/Main.class \
		bin/com/crispyxyz/jvavscript/ArgumentsException.class \
		bin/com/crispyxyz/jvavscript/Tokens.class
	jar -c -v -f JvavScript.jar -e com.crispyxyz.jvavscript.Main -C bin .

.PHONY: dex
dex: JvavScript.jar #requires android_sdk
	dx --dex --output=bin/JvavScript.dex bin/JvavScript.jar

bin/com/crispyxyz/jvavscript/Main.class: src/com/crispyxyz/jvavscript/Main.java
	javac -d bin src/com/crispyxyz/jvavscript/Main.java

bin/com/crispyxyz/jvavscript/ArgumentsException.class: src/com/crispyxyz/jvavscript/ArgumentsException.java
	javac -d bin src/com/crispyxyz/jvavscript/ArgumentsException.java

bin/com/crispyxyz/jvavscript/Tokens.class: src//com/crispyxyz/jvavscript/Tokens.java
	javac -d bin src/com/crispyxyz/jvavscript/Tokens.java

.PHONY: clean
clean: 
	rm -rf bin JvavScript.jar JvavScript.dex
