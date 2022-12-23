@echo off
echo Use a GLL recogniser to build a TWE set

del ARTGeneratedParser.* >nul 2>&1
del ARTGeneratedLexer.* >nul 2>&1

call %arthome%\artV3 artLexerGrammar.art artChooseLexTWE.art !gllTWEGeneratorPool
call %arthome%\artV3CompileGenerated
call %arthome%\artV3TestGenerated !twePrint %1 %2 %3 %4 %5 %6 %7 %8 %9 
