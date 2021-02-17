.PHONY: JvavScript.jar
JvavScript.jar: JvavScriptKt.jar

JvavScriptKt.jar: src/com/crispyxyz/jvavscript/ArgumentsException.kt \
		src/com/crispyxyz/jvavscript/Main.kt \
		src/com/crispyxyz/jvavscript/Tokens.kt
	kotlinc -include-runtime -d JvavScriptKt.jar src/com/crispyxyz/jvavscript/* && \
	jar -u -v -f JvavScriptKt.jar -e com.crispyxyz.jvavscript.Main JvavScriptKt.jar

.PHONY: dex
dex: JvavScriptKt.dex

JvavScriptKt.dex: JvavScriptKt.jar  #requires android_sdk
	dx --dex --output=JvavScriptKt.dex JvavScriptKt.jar

.PHONY: clean
clean: 
	rm -rf bin/ oat/ JvavScriptKt.jar JvavScriptKt.dex
