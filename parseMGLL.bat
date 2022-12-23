@echo off
echo Use an MGLL parser to build a multi-SPPF

del ARTGeneratedParser.* >nul 2>&1
del ARTGeneratedLexer.* >nul 2>&1

call %arthome%\artV3 artParserGrammar.art artChooseParseSPPF.art artChooseParseTWE.art !mgllGeneratorPool
call %arthome%\artV3CompileGenerated
call %arthome%\artV3TestGenerated %*
