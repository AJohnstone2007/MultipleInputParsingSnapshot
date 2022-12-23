package uk.ac.rhul.cs.csle.art.core;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.rhul.cs.csle.art.cfg.extract.ExtractJLS;
import uk.ac.rhul.cs.csle.art.cfg.lex.ARTCompressWhiteSpaceJava;
import uk.ac.rhul.cs.csle.art.cfg.lex.ARTLexDFA;
import uk.ac.rhul.cs.csle.art.term.ITerms;
import uk.ac.rhul.cs.csle.art.term.ITermsLowLevelAPI;
import uk.ac.rhul.cs.csle.art.term.TermTraverserText;
import uk.ac.rhul.cs.csle.art.term.termtool.TermTool;
import uk.ac.rhul.cs.csle.art.util.text.ARTText;
import uk.ac.rhul.cs.csle.art.v3.manager.parser.ARTV4Lexer;
import uk.ac.rhul.cs.csle.art.v3.manager.parser.ARTV4Parser;

public class ARTCore {
  public final ITerms iTerms = new ITermsLowLevelAPI();
  private final Map<Integer, ARTModule> modules = new LinkedHashMap<>();

  /* Global variables set by directives and used by !try */
  ARTModule currentModule, mainModule = null;
  final Map<String, String> latexAliases = new HashMap<>();
  final List<Integer> dynamicDirectives = new LinkedList<>();

  int traceLevel = 0;
  int statisticsLevel = 0;
  int verbosityLevel = 5;

  int parseAlgorithm = 0;
  int startNonterminal = 0;
  String inputString = "";

  int strategy = 0;
  int startRelation = 0;
  int inputTerm = 0;
  int resultTerm = 0;

  int goodTest = 0;
  int badTest = 0;

  /* Constructor and initialisation **********************************************************************************/
  public ARTCore(String[] args) throws FileNotFoundException {
    // 1. Create initial main module
    currentModule = findModule(0);

    // 2. Load traverser tables
    loadModuleBuilderTraverser();
    loadTextTraverser();
    loadLaTeXTraverser();

    // 2a. Debug - load text traverser default action to print message if we encounter an unknown constructor
    tt.addOp(-1, (Integer t) -> tt.append("??" + iTerms.toString(t) + "?? "), null, null);

    // 3. Command line munging to support special cases of art x * and art x y * where neither x nor y start with a !
    StringBuilder sb = new StringBuilder();
    if (args.length == 0) throw new ARTUncheckedException("No arguments supplied\n\nART " + ARTVersion.version());

    int restOfLine = 0;
    if (args[0].charAt(0) != '!') {
      sb.append("!merge " + args[0] + "\n");
      restOfLine = 1;
      if (args.length > 1 && args[1].charAt(0) != '!') {
        sb.append("!try \"" + args[1] + "\"\n");
        restOfLine = 2;
      }
    }

    for (int i = restOfLine; i < args.length; i++) // Catenate the rest of the arguments
      sb.append(args[i] + " ");

    // System.out.println("Command line: " + sb);

    int root = parseARTV4("-- Command line --", sb.toString());
    // debugMsg("parsed command line string:\n" + sb + "\nto term:\n" + iTerms.toString(root));

    // 4. Traverse the term and recursively process merge directives, checking for cycles
    processMergeStaticDirective(root);
    // debugMsg("plain print merged term:\n" + iTerms.toString(root));
    // debugMsg("pretty print merged term:\n" + tt.toString(root));

    // 5. Collect modules and their sets and their directive lists, noting default module and default start symbols as we go
    moduleBuilderTraverser.traverse(root);

    // 6. Static checks and normalisation
    if (mainModule == null) mainModule = currentModule; // If no main has been set, then there were no !module directives, so just use the default
    for (Integer m : modules.keySet())
      modules.get(m).normaliseAndStaticChecks();

    // System.out.println("Debug - print modules:");
    // for (Integer m : modules.keySet())
    // System.out.println(modules.get(m).toString(tt));
    // System.out.println("End debug");

    // 7. Run the script, starting from the directive list in currentModule
    interpretDirectives(mainModule);
  }

  private void debugMsg(String str) {
    System.err.println("Debug - " + str + "\nEnd debug");
  }

  ARTModule findModule(int moduleTerm) {
    ARTModule ret = modules.get(moduleTerm);
    if (ret != null) throw new ARTUncheckedException("Attempt to redefine " + (moduleTerm == 0 ? " unnamed module" : " module " + iTerms.toString(moduleTerm)));
    modules.put(moduleTerm, (ret = new ARTModule(this, moduleTerm))); // set up default main module
    return ret;
  }

  int parseARTV4(String specificationFileName, String specificationInput) {
    ARTV4Parser parser = new ARTV4Parser(new ARTV4Lexer()); // Initialisation of V3 style parsers is suspect so make a new one each time

    parser.artParse(specificationInput);
    parser.artDisambiguatePriorityLongestMatch();
    if (!parser.artIsInLanguage) throw new ARTUncheckedException("Syntax error in ART specification for input " + specificationFileName);
    if (parser.artIsAmbiguous()) throw new ARTUncheckedException("Internal error: specification grammar is ambiguous");
    return parser.artDerivationAsTerm(iTerms);
  }

  TermTraverserText moduleBuilderTraverser = new TermTraverserText(iTerms);

  private void loadModuleBuilderTraverser() {
    moduleBuilderTraverser.addBreakOp("directive", (Integer t) -> buildDirective(t), null, null);

    moduleBuilderTraverser.addOp("cfgRule", (Integer t) -> currentModule.buildCFGRule(t), null, null);
    moduleBuilderTraverser.addOp("trRule", (Integer t) -> currentModule.buildTRRule(t), null, null);
    moduleBuilderTraverser.addOp("chooseRule", (Integer t) -> currentModule.buildChooseRule(t), null, null);

    moduleBuilderTraverser.addBreakOp("cfgNonterminal", (Integer t) -> currentModule.nonterminals.add(t), null, null);
    moduleBuilderTraverser.addBreakOp("cfgCaseInsensitiveTerminal", (Integer t) -> currentModule.terminals.add(t), null, null);
    moduleBuilderTraverser.addBreakOp("cfgCaseSensitiveTerminal", (Integer t) -> currentModule.terminals.add(t), null, null);
    moduleBuilderTraverser.addBreakOp("cfgCaseCharacterTerminal", (Integer t) -> currentModule.terminals.add(t), null, null);
    moduleBuilderTraverser.addBreakOp("cfgBuiltinTerminal", (Integer t) -> currentModule.terminals.add(t), null, null);
    moduleBuilderTraverser.addBreakOp("cfgCharacterRangeTerminal", (Integer t) -> currentModule.buildCharacterRangeTerminal(t), null, null);
  }

  TermTraverserText tt = new TermTraverserText(iTerms);

  private void loadTextTraverser() {
    // 0. Directive and top level pretty print controls
    tt.addNoOp("text", "cfgElementDeclarations", "cfgElementDeclaration", "latexDeclarations", "__string");
    tt.addBreakOp("directive", (Integer t) -> processDirective(t), null, null);
    tt.addOp("latexDeclaration", null, " = ", null);
    tt.addBreakOp("__string", (Integer t) -> tt.append(tt.childSymbolString(t, 0)), null, null);
    tt.addBreakOp("idART", (Integer t) -> tt.append(tt.childSymbolString(t, 0)), null, null);

    // 1. Context Free Grammar pretty print controls
    tt.addNoOp("cfgSlot");

    tt.addOp("cfgCat", null, " ", null);
    tt.addOp("cfgRule", null, "::=\n ", "\n");
    tt.addOp("cfgRHS", null, "\n|", "\n");
    tt.addOp("cfgAlt", null, " | ", null);
    tt.addBreakOp("cfgNonterminal", (Integer t) -> tt.append(iTerms.getTermSymbolString(iTerms.getSubterm(t, 0))), null, null);
    tt.addBreakOp("cfgCaseInsensitiveTerminal", (Integer t) -> tt.append(iTerms.getTermSymbolString(iTerms.getSubterm(t, 0))), null, null);
    tt.addBreakOp("cfgCaseSensitiveTerminal", (Integer t) -> tt.append(iTerms.getTermSymbolString(iTerms.getSubterm(t, 0))), null, null);
    tt.addBreakOp("cfgCharacterTerminal", (Integer t) -> tt.append(iTerms.getTermSymbolString(iTerms.getSubterm(t, 0))), null, null);
    tt.addBreakOp("cfgCharacterRangeTerminal",
        (Integer t) -> tt.append(iTerms.getTermSymbolString(iTerms.getSubterm(t, 0)) + ".." + iTerms.getTermSymbolString(iTerms.getSubterm(t, 1))), null, null);
    tt.addOp("cfgOptional", null, null, "?");
    tt.addOp("cfgKleeneClosure", null, null, "*");
    tt.addOp("cfgPositiveClosure", null, null, "+");
    tt.addOp("cfgDoFirst", "(", null, ")");
    tt.addOp("cfgEpsilon", "#", null, null);

    // 2. Chooser pretty print controls
    tt.addNoOp("chooseElement");
    tt.addOp("chooseRule", null, null, "\n");
    tt.addOp("chooseHigher", " > ", null, null);
    tt.addOp("chooseLower", " < ", null, null);
    tt.addOp("chooseLonger", " >> ", null, null);
    tt.addOp("chooseShorter", " << ", null, null);
    tt.addOp("chooseDiff", "(", " \\ ", ")");
    tt.addOp("chooseUnion", "(", " | ", ")");
    tt.addOp("chooseIntersection", "(", " / ", ")");
    tt.addBreakOp("choosePredefinedSet", (Integer t) -> tt.append(tt.childStrippedSymbolString(t, 0)), null, null);

    // 3. Term rewrite pretty print controls
    tt.addOp("trRule", null, null, "\n");
    tt.addOp("tr", null, "\n---\n", null);
    tt.addOp("trPremises", null, "    ", null);
    tt.addBreakOp("trLabel", (Integer t) -> tt.append(iTerms.getTermArity(t) > 0 ? ("-" + tt.childSymbolString(t, 0) + "\n") : "\n"), null, null);
    tt.addBreakOp("trMatch", (Integer t) -> tt.append(iTerms.toString(iTerms.getSubterm(t, 0)) + " |> " + iTerms.toString(iTerms.getSubterm(t, 1))), null,
        null);
    tt.addNoOp("trTransition");
    tt.addBreakOp("TRRELATION", (Integer t) -> tt.append(" " + tt.childStrippedSymbolString(t, 0) + " "), null, null);
    tt.addOp("trConfiguration", "<", null, ">");
    tt.addBreakOp("trPrimaryTerm", (Integer t) -> tt.append(iTerms.toString(iTerms.getSubterm(t, 0))), null, null);
    tt.addOp("trEntityReferences", ", ", ", ", null);
    tt.addBreakOp("trUnamedTerm", (Integer t) -> tt.append(iTerms.toString(iTerms.getSubterm(t, 0))), null, null);
    tt.addBreakOp("trNamedTerm", (Integer t) -> tt.append(iTerms.toString(iTerms.getSubterm(t, 0)) + " = " + iTerms.toString(iTerms.getSubterm(t, 1))), null,
        null);

    // 4. Attribute equation pretty print controls - not fully designed yet
  }

  private void processDirective(Integer t) {
    tt.append(tt.childStrippedSymbolString(t, 0) + " ");
    if (iTerms.getTermArity(t) == 2) tt.traverse(iTerms.getSubterm(t, 1));
  }

  TermTraverserText latexTraverser = new TermTraverserText(iTerms);

  private void loadLaTeXTraverser() {
  }
  /* End of constructor and top level call ***************************************************************************/

  /* Start of directive interpreter **********************************************************************************/
  public void buildDirective(int term) {
    // System.out.println("Building directive " + iTerms.toString(term));
    int firstChild = iTerms.getTermChildren(term)[0];
    int secondChild = 0;
    String firstChildString = iTerms.getTermSymbolString(firstChild);
    if (iTerms.getTermChildren(term).length > 1) secondChild = iTerms.getTermChildren(term)[1];

    // Either directly handle static directives, or add to module dynamicDirectives list
    if (firstChildString.equals("'!module'")) {
      currentModule = findModule(secondChild);
    } else if (firstChildString.equals("'!use'")) {
      int[] children = iTerms.getTermChildren(secondChild);
      for (int i = 0; i < children.length; i++)
        currentModule.useModules.add(children[i]);
    } else if (firstChildString.equals("'!paraterminal'")) {
      System.out.println("Updating paraterminals with: " + iTerms.toString(term));
      int[] children = iTerms.getTermChildren(term);
      for (int i = 1; i < children.length; i++) {
        System.out.println("Putting paraterminals: " + iTerms.toString(children[i]));
        int alias;
        if (iTerms.getTermArity(children[i]) == 1)
          alias = 0;
        else
          alias = iTerms.getSubterm(children[i], 1);
        currentModule.paraterminals.put(iTerms.getSubterm(children[i], 0), alias);
      }
    } else if (firstChildString.equals("'!cfgElements'")) {
      int[] children = iTerms.getTermChildren(secondChild);
      for (int i = 0; i < children.length; i++)
        if (iTerms.getTermSymbolString(iTerms.getTermChildren(children[i])[0]).equals("cfgNonterminal"))
          currentModule.nonterminals.add(children[i]);
        else
          currentModule.terminals.add(children[i]);
    } else if (firstChildString.equals("'!latex'")) {
      int[] children = iTerms.getTermChildren(secondChild);
      for (int i = 0; i < children.length; i++)
        latexAliases.put(strip(iTerms.getTermSymbolString(iTerms.getSubterm(children[i], 0, 0))),
            strip(iTerms.getTermSymbolString(iTerms.getSubterm(children[i], 1, 0))));
    } else
      dynamicDirectives.add(term);

  }

  String strip(String str) {
    return str.substring(1, str.length() - 1);
  }

  void interpretDirectives(ARTModule module) throws FileNotFoundException {
    startNonterminal = module.defaultStartNonterminal;
    startRelation = module.defaultStartRelation;

    for (Integer directiveTerm : dynamicDirectives) {
      // System.out.println("Interpreting " + iTerms.toString(directiveTerm));
      int firstChild = iTerms.getSubterm(directiveTerm, 0);
      int secondChild = 0;
      if (iTerms.getTermArity(directiveTerm) > 1) secondChild = iTerms.getSubterm(directiveTerm, 1);

      if (iTerms.hasSymbol(firstChild, "'!main'")) {
        ARTModule newMain = modules.get(secondChild);

        if (newMain == null) throw new ARTUncheckedException("unknown module in directive !main " + tt.toString(secondChild));

        mainModule = newMain;
      } else if (iTerms.hasSymbol(firstChild, "'!start'")) {
        if (iTerms.hasSymbol(firstChild, "cfgNonterminal"))
          startNonterminal = secondChild;
        else if (iTerms.hasSymbol(firstChild, "TRRELATION")) startRelation = secondChild;

      } else if (iTerms.hasSymbol(firstChild, "'!try'")) {
        System.out.print("*** try " + tt.toString(inputTerm) + " with relation " + tt.toString(startRelation));
        if (resultTerm != 0) System.out.print(" resulting in " + tt.toString(resultTerm));
        System.out.println();
        eSOSStepper(traceLevel, inputTerm, startRelation, resultTerm);
      }

      else if (iTerms.hasSymbol(firstChild, "'!termTool'"))
        new TermTool(iTerms);

      else if (iTerms.hasSymbol(firstChild, "'!lexDFA'")) {
        ARTLexDFA dfa = new ARTLexDFA(mainModule, iTerms);
        dfa.recogniseViaMap("lexTest.txt");

      } else if (iTerms.hasSymbol(firstChild, "'!extractJLS'")) {
        new ExtractJLS(iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 1, 0)), iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 2, 0)),
            iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 3, 0)), iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 4, 0)),
            iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 5)));

      } else if (iTerms.hasSymbol(firstChild, "'!compressWhitespaceJava'")) {
        new ARTCompressWhiteSpaceJava(iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 1, 0)),
            iTerms.getTermSymbolString(iTerms.getSubterm(directiveTerm, 2, 0)));
      } else
        throw new ARTUncheckedException("unknown directive " + tt.toString(directiveTerm));
    }
    if (goodTest != 0 || badTest != 0) System.out.println("try result summary: " + goodTest + " good, " + badTest + " bad");
  }

  void parse(int parser) {
    System.out.println("Parsing using process " + iTerms.getString(parser));
  }
  /* End of directive interpreter ************************************************************************************/

  /* eSOS interpreter ************************************************************************************************/
  private final Map<Integer, Set<Integer>> cycleCheck = new HashMap<>(); // Map from relation to set of configurations
  private int eSOSTraceLevel = 3;
  private int eSOSRewriteCallCounter;

  public void eSOSTrace(int level, int indent, String string) {
    if (eSOSTraceLevel >= level) {
      for (int i = 0; i < indent; i++)
        System.out.print(" ");
      System.out.println(string);
    }
  }

  private String bindingsToString(int[] bindings, Map<Integer, Integer> variableMap) {
    StringBuilder sb = new StringBuilder();
    boolean seen = false;
    sb.append("{ ");
    for (int i = 0; i < bindings.length; i++) {
      if (bindings[i] > 0) {
        if (seen) sb.append(", ");
        sb.append(tt.toString(iTerms.findTerm("_" + i), variableMap) + "=" + tt.toString(bindings[i], variableMap));
        seen = true;
      }
    }
    sb.append(" }");
    return sb.toString();
  }

  private int eSOSRewrite(int configuration, int relationTerm, int level) { // return rewritten term, or 0 if no rules
    // apply
    ++eSOSRewriteCallCounter;
    eSOSTrace(3, level, "Rewrite call " + eSOSRewriteCallCounter + " " + tt.toString(configuration) + " " + tt.toString(relationTerm));
    if (relationTerm == 0) throw new ARTUncheckedException("eSOS rewrite on null relation");
    if (!cycleCheck.containsKey(relationTerm)) cycleCheck.put(relationTerm, new HashSet<Integer>());
    Set<Integer> cycleSet = cycleCheck.get(relationTerm);
    // if (cycleSet.contains(configuration)) throw new ARTExceptionFatal("cycle detected " +tt.toString(configuration) +tt.toString(relationTerm));
    cycleSet.add(configuration);
    if (isTerminatingConfiguration(configuration, relationTerm)) {
      eSOSTrace(3, level + 1, "Terminal " + tt.toString(configuration));
      return configuration;
    }

    int rootTheta = iTerms.getSubterm(configuration, 0);
    Map<Integer, Set<Integer>> ruleMap = mainModule.trRules.get(relationTerm);

    if (ruleMap == null) throw new ARTUncheckedException("no rules found for relation " + tt.toString(relationTerm)); // Make this check static

    Set<Integer> ruleSet = ruleMap.get(iTerms.getTermSymbolIndex(rootTheta));
    if (ruleSet == null) ruleSet = ruleMap.get(iTerms.findString(""));

    if (ruleSet == null) throw new ARTUncheckedException( // Make this check static
        "Error: no rules found for constructor " + iTerms.getTermSymbolString(rootTheta) + " in relation " + iTerms.getTermSymbolString(relationTerm));

    nextRule: for (int ruleIndex : ruleSet) {
      Map<Integer, Integer> variableMap = mainModule.reverseVariableNamesByRule.get(ruleIndex);
      eSOSTrace(3, level, tt.toString(ruleIndex, variableMap)); // Announce the next rule we are going to try
      int lhs = iTerms.getSubterm(ruleIndex, 1, 1, 0);
      int premises = iTerms.getSubterm(ruleIndex, 1, 0);
      int premiseCount = iTerms.getTermArity(premises);
      int rhs = iTerms.getSubterm(ruleIndex, 1, 1, 2);
      int[] bindings = new int[ITerms.variableCount];
      // System.out.println("Test component - lhs: " +tt.toString(lhs) + " rhs: " +tt.toString(rhs) + " premises: " +tt.toString(premises) + " with
      // arity " + premiseCount);

      int ruleLabel = iTerms.getSubterm(ruleIndex, 0);

      if (!iTerms.matchZeroSV(configuration, lhs, bindings)) {
        eSOSTrace(3, level, tt.toString(ruleLabel) + " Theta match failed: seek another rule");
        continue nextRule;
      }
      eSOSTrace(5, level, tt.toString(ruleLabel) + "bindings after Theta match " + bindingsToString(bindings, variableMap));

      // Now work through the premises
      for (int premiseNumber = 0; premiseNumber < premiseCount; premiseNumber++) {
        int premise = iTerms.getSubterm(premises, premiseNumber);
        if (iTerms.hasSymbol(premise, "trMatch")) { // |> match expressions
          eSOSTrace(4, level, tt.toString(ruleLabel) + "premise " + (premiseNumber + 1) + " " + tt.toString(premise, variableMap));
          if (!iTerms.matchZeroSV(iTerms.substitute(bindings, iTerms.getSubterm(premise, 0), 0), iTerms.getSubterm(premise, 1), bindings)) {
            eSOSTrace(4, level, tt.toString(ruleLabel) + "premise " + (premiseNumber + 1) + " failed: seek another rule");
            continue nextRule;
          }
        } else { // transition
          if (iTerms.hasSymbol(iTerms.getSubterm(premises, premiseNumber), "trTransition")) {
            int rewriteTerm = iTerms.substitute(bindings, iTerms.getSubterm(premise, 0), 0);
            int rewriteRelation = iTerms.getSubterm(premise, 1);
            eSOSTrace(4, level, tt.toString(ruleLabel) + "premise " + (premiseNumber + 1) + " " + tt.toString(premise, variableMap));
            int rewrittenTerm;
            rewrittenTerm = eSOSRewrite(rewriteTerm, rewriteRelation, level + 1);
            if (rewrittenTerm < 0) {
              eSOSTrace(4, level, tt.toString(ruleLabel) + "premise" + (premiseNumber + 1) + " failed: seek another rule");
              continue nextRule;
            }
            if (!iTerms.matchZeroSV(rewrittenTerm, iTerms.getSubterm(premise, 2), bindings)) continue nextRule;
          } else
            throw new ARTUncheckedException("Unknown premise kind " + tt.toString(premise));
        }
        eSOSTrace(5, level, tt.toString(ruleLabel) + "bindings after premise " + (premiseNumber + 1) + " " + bindingsToString(bindings, variableMap));
      }

      int ret = iTerms.substitute(bindings, rhs, 0);
      eSOSTrace(level == 1 ? 2 : 3, level, tt.toString(ruleLabel) + "rewrites to " + tt.toString(ret, variableMap));
      return ret;
    }
    // If we get here, then no rules succeeded
    eSOSTrace(level == 1 ? 2 : 3, level, "Failed rewrite call " + eSOSRewriteCallCounter + " " + tt.toString(configuration) + tt.toString(relationTerm));
    return configuration;
  }

  void eSOSStepper(int traceLevel, int oldConfiguration, int relation, int testFinalConfiguration) {
    eSOSTraceLevel = traceLevel;
    eSOSRewriteCallCounter = 0;
    int eSOSStepCounter = 0;
    int newConfiguration;
    while (true) {
      eSOSTrace(2, 0, "Step " + ++eSOSStepCounter);
      for (int i : cycleCheck.keySet())
        cycleCheck.get(i).clear();
      newConfiguration = eSOSRewrite(oldConfiguration, relation, 1);
      if (newConfiguration == oldConfiguration) break; // Nothing changed
      oldConfiguration = newConfiguration;
    }

    eSOSTrace(1, 0,
        (isTerminatingConfiguration(newConfiguration, relation) ? "Normal termination on " : "Stuck on ") + tt.toString(newConfiguration) + " after "
            + eSOSStepCounter + " step" + (eSOSStepCounter == 1 ? "" : "s") + " and " + eSOSRewriteCallCounter + " rewrite"
            + (eSOSRewriteCallCounter == 1 ? "" : "s"));

    if (testFinalConfiguration != 0) {
      if (newConfiguration == testFinalConfiguration) {
        System.out.println("Good result");
        goodTest++;
      } else {
        System.out.println("Bad result: expected " + iTerms.toString(testFinalConfiguration) + " found " + iTerms.toString(newConfiguration));
        badTest++;
      }
    }
  }

  boolean isTerminatingConfiguration(int eSOSConfiguration, int relation) {
    int thetaRoot = iTerms.getSubterm(eSOSConfiguration, 0);
    Set<Integer> terminals = mainModule.eSOSTerminals.get(relation);
    return iTerms.isSpecialTerm(thetaRoot) || (terminals != null && terminals.contains(thetaRoot));
  }
  /* End of eSOS interpreter *****************************************************************************************/

  /* Start of !merge rewriter ****************************************************************************************/
  /* A hand crafted rewriter that does preOrder recursive rewriting of !merge x to the contents of file x, checking for recursion */
  private final int mergePattern = iTerms.findTerm("directive('!merge', _)"); // use wildcard in pattern for ID and iTerms.subTerm to pull it out
  Set<Integer> mergeAncestors = new HashSet<>(); // A set of previously merged terms (not filenames!)

  private int processMergeStaticDirective(int root) {
    int newRoot, oldRoot = root;
    mergeAncestors.clear();
    while (true)
      if ((newRoot = processMerges(oldRoot)) == oldRoot)
        return newRoot;
      else
        oldRoot = newRoot;
  }

  private int processMerges(int root) {
    int[] children = iTerms.getTermChildren(root);

    for (int i = 0; i < children.length; i++) {

      if (iTerms.matchZeroSV(children[i], mergePattern, null)) { // match? No bindings needed as only wildcard in pattern
        String filename = iTerms.getTermSymbolString(iTerms.getSubterm(root, 0, 1, 0)) + ".art"; // Note parser will have removed file type

        // System.out.println("Merging " + filename);
        int parsedTerm = parseARTV4(filename, ARTText.readFile(filename));

        if (mergeAncestors.contains(parsedTerm)) throw new ARTUncheckedException("recursive !merge content on file " + filename);

        mergeAncestors.add(parsedTerm); // Push parsed term to ancestor set
        children[i] = processMerges(parsedTerm); // recurse
        mergeAncestors.remove(parsedTerm); // Pop parsed term from ancestor set - it may be used further along
      } else
        processMerges(children[i]); // all done, so next sibling
    }

    return iTerms.findTerm(iTerms.getTermSymbolIndex(root), children);
  }
  /* End of !merge rewriter ******************************************************************************************/

  /* Start general rewriting using strategies ************************************************************************/

  /*
   * A function which takes a map of rules and a root, and return the first applicable rewrite, or the original term if there are no applicable rewrites.
   *
   * This is a stripped down version of eSOSRewrite which thus performs eSOS style conditional rewriting Instead of passing the relation, we pass the
   * constructor->ruleList map for this relation
   */

  private int rewriteOne(int root, Map<Integer, List<Integer>> ruleMap) {

    List<Integer> ruleList = ruleMap.get(iTerms.getTermSymbolIndex(root));
    if (ruleList == null) return root; // Nothing to do
    int[] bindings = new int[ITerms.variableCount];

    nextRule: for (int ruleIndex : ruleList) {
      Arrays.fill(bindings, 0); // Empty the bindings - probably not strictly necessary if rules are well formed wrt use-def relation over variables
      int lhs = iTerms.getSubterm(ruleIndex, 1, 1, 0);
      if (!iTerms.matchZeroSV(root, lhs, bindings)) continue nextRule;

      // Now work through the premises
      int premises = iTerms.getSubterm(ruleIndex, 1, 0);
      int premiseCount = iTerms.getTermArity(premises);

      for (int premiseNumber = 0; premiseNumber < premiseCount; premiseNumber++) {
        int premise = iTerms.getSubterm(premises, premiseNumber);
        if (iTerms.hasSymbol(premise, "trMatch"))
          if (!iTerms.matchZeroSV(iTerms.substitute(bindings, iTerms.getSubterm(premise, 0), 0), iTerms.getSubterm(premise, 1), bindings))
          continue nextRule;
          else { // transition, so recurse down from here
            int rewriteTerm = iTerms.substitute(bindings, iTerms.getSubterm(premise, 0), 0);
            int rewriteRelation = iTerms.getSubterm(premise, 1);
            int rewrittenTerm = rewriteStrategyFail(rewriteTerm, mainModule.trRules.get(rewriteRelation));
            if (!iTerms.matchZeroSV(rewrittenTerm, iTerms.getSubterm(premise, 2), bindings)) continue nextRule;
          }
      }
      int rhs = iTerms.getSubterm(ruleIndex, 1, 1, 2);
      return iTerms.substitute(bindings, rhs, 0); // return on first successful rule
    }

    return root; // No rules matched
  }

  private int rewriteStrategyFail(int rewriteTerm, Map<Integer, Set<Integer>> map) {
    throw new ARTUncheckedException("Call to dummy rewrite strategy");
  }

  boolean abortRewriting; // Used by the xyzOnce rewriters to crash out of a traversal

  private int rewriteStrategyPreorderOnce(int root, Map<Integer, List<Integer>> ruleMap) {
    if (abortRewriting) return root;
    int[] children = iTerms.getTermChildren(root);

    for (int i = 0; i < children.length; i++) {
      int newRoot, oldRoot = children[i];
      if ((newRoot = rewriteOne(oldRoot, ruleMap)) != oldRoot) {
        children[i] = newRoot;
        abortRewriting = true;
      } else
        rewriteStrategyPreorderOnce(children[i], ruleMap); // recurse into this child
    }

    return iTerms.findTerm(iTerms.getTermSymbolIndex(root), children);

  }

  private int rewriteStrategyExhaustiveGraph(int root, Map<Integer, List<Integer>> ruleMap) {

    return root;
  }

  /* Entry point for all rewriters */
  private int rewrite(int root, Map<Integer, Map<Integer, List<Integer>>> ruleMap, int relation) {
    Map<Integer, List<Integer>> ruleSubMap = ruleMap.get(relation); // Pull out the rules for just this relation
    abortRewriting = false;

    return rewriteStrategyPreorderOnce(root, ruleSubMap);
  }
  /* End of general rewriting using strategies ***********************************************************************/
}
