@echo off
del artCharacterGrammar.art >nul 2>&1
del artLexerGrammar.art >nul 2>&1
del artParserGrammar.art >nul 2>&1
del artTokenGrammar.art >nul 2>&1
del artPrettyGrammar.art >nul 2>&1
del artchoose*.art >nul 2>&1

echo Print lexer, parser and token grammars corresponding to %1
call %arthome%\artV3 !grammarWrite %*

