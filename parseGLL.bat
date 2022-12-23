@echo off
echo Use a classical GLL parser to build an SPPF

del ARTGeneratedParser.* >nul 2>&1
del ARTGeneratedLexer.* >nul 2>&1

call %arthome%\artV3 %1 !gllGeneratorPool
call %arthome%\artV3CompileGenerated
call %arthome%\artV3TestGenerated %2 %3 %4 %5 %6 %7 %8 %9

