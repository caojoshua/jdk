#!/usr/bin/bash
java -ea -XX:+UnlockExperimentalVMOptions -Xbatch -XX:CompileCommand=compileOnly,GetFieldIsAlias::main -XX:CompileCommand=quiet $* GetFieldIsAlias
