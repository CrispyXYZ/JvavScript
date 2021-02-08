JvavScript.jar: src/com/crispyxyz/jvavscript/ArgumentsException.java \
		src/com/crispyxyz/jvavscript/Main.java \
		src/com/crispyxyz/jvavscript/Tokens.java
	javac --release 8 -d bin src/com/crispyxyz/jvavscript/*.java && \
	jar -c -v -f JvavScript.jar -e com.crispyxyz.jvavscript.Main -C bin .

.PHONY: dex
dex: JvavScript.dex

JvavScript.dex: JvavScript.jar  #requires android_sdk
	dx --dex --output=JvavScript.dex JvavScript.jar

.PHONY: clean
clean: 
	rm -rf bin JvavScript.jar JvavScript.dex
