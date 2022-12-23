package uk.ac.rhul.cs.csle.art.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import uk.ac.rhul.cs.csle.art.term.ITerms;
import uk.ac.rhul.cs.csle.art.term.TermTraverserText;

public class ARTModule {
  /**
  *
  */
  public final ITerms iTerms;
  public final TermTraverserText tt;
  final int nameTerm;
  int defaultStartNonterminal = 0; // This is set by the first Context Free Grammar rule encountered
  int defaultStartRelation = 0; // This is set by the first term rewrite rule encountered
  public int slotCount = 0;

  final Set<Integer> useModules = new LinkedHashSet<>();
  final Set<Integer> terminals = new LinkedHashSet<>();
  final Set<Integer> nonterminals = new LinkedHashSet<>();
  final Map<Integer, Integer> paraterminals = new LinkedHashMap<>(); // A map of paraterminals names to aliases
  private final Map<Integer, Set<Integer>> cfgRules = new LinkedHashMap<>(); // Map of LHS term to list of RHS term; LinkedHashMap maintains ordering
  final Map<Integer, Map<Integer, Set<Integer>>> trRules = new LinkedHashMap<>();
  final Set<Integer> chooseRules = new LinkedHashSet<>();

  final Map<Integer, Integer> termToEnumElementMap = new HashMap<>();
  final Map<Integer, Integer> enumElementToTermMap = new HashMap<>();
  final Map<Integer, Integer> termRewriteConstructorDefinitions = new HashMap<>(); // The number of times a constructor appears as the root of a term
  final Map<Integer, Integer> termRewriteConstructorUsages = new HashMap<>(); // The number of times a constructor appears

  final Set<Integer> functionsInUse = new HashSet<>(); // The set of functions in use

  final Map<Integer, Map<Integer, Integer>> variableNamesByRule = new HashMap<>(); // Map from term to the variable aliases used in that term
  final Map<Integer, Map<Integer, Integer>> reverseVariableNamesByRule = new HashMap<>(); // Map from term to the variable aliases used in that term

  public ARTModule(ARTCore host, int nameTerm) {
    this.iTerms = host.iTerms;
    this.tt = host.tt;
    this.nameTerm = nameTerm;
    if (host.mainModule == null) host.mainModule = this;
    if (host.mainModule.isEmpty()) host.mainModule = this;
  }

  private boolean isEmpty() {
    return useModules.isEmpty() && terminals.isEmpty() && nonterminals.isEmpty() && paraterminals.isEmpty() && getCfgRules().isEmpty() && trRules.isEmpty()
        && chooseRules.isEmpty();
  }

  public String toString(TermTraverserText tt) {
    StringBuilder sb = new StringBuilder();

    if (nameTerm == 0)
      sb.append("(* Unnamed module *)\n\n");
    else
      sb.append("(* Module " + tt.toString(nameTerm) + " *)\n\n!module " + tt.toString(nameTerm) + "\n\n");

    sb.append(defaultStartNonterminal == 0 ? "(* No start nonterminal *)\n" : "!start " + tt.toString(defaultStartNonterminal) + "\n");
    sb.append(defaultStartRelation == 0 ? "(* No start relation *)\n\n" : "!start " + tt.toString(defaultStartRelation) + "\n\n");

    sb.append("(* Uses *)\n");
    for (Integer t : useModules)
      sb.append(tt.toString(t) + "\n");
    sb.append("(* Terminals *)\n");
    for (Integer t : terminals)
      sb.append(tt.toString(t) + "\n");
    sb.append("(* Paraterminals *)\n");
    for (Integer n : paraterminals.keySet())
      sb.append(tt.toString(n) + " = " + "\n");
    sb.append("(* Nonterminals *)\n");
    for (Integer n : nonterminals)
      sb.append(tt.toString(n) + "\n");
    sb.append("(* Context Free Grammar rules *)\n");
    for (Integer lc : getCfgRules().keySet()) {
      sb.append(tt.toString(lc) + " ::=\n ");
      for (Integer c : getCfgRules().get(lc))
        sb.append(tt.toString(c) + "\n");
    }
    sb.append("(* Choose rules *)\n");
    for (Integer c : chooseRules)
      sb.append(tt.toString(c));

    sb.append("(* Term rewrite rules *)\n");
    for (Integer rel : trRules.keySet()) {
      sb.append("  (* Relation " + tt.toString(rel) + " *)\n");
      for (Integer cons : trRules.get(rel).keySet()) {
        sb.append("    (* Constructor " + iTerms.getString(cons) + " *)\n");
        for (Integer rule : trRules.get(rel).get(cons))
          sb.append(tt.toString(rule));
      }
    }
    sb.append("(* Attribute equations *)\n");

    if (nameTerm == 0)
      sb.append("(* End of unnamed module *)\n\n");
    else
      sb.append("(* End of module " + tt.toString(nameTerm) + " *)\n\n");
    return sb.toString();
  }

  public Integer termAsEnumElement(Integer termIndex) {
    Integer ret = termToEnumElementMap.get(termIndex);
    if (ret == null) throw new ARTUncheckedException("attempt to map term which is not in enumeration");
    return ret;
  }

  public Integer enumElementAsTerm(Integer enumElement) {
    Integer ret = enumElementToTermMap.get(enumElement);
    if (ret == null) throw new ARTUncheckedException("attempt to enum element which is not in enumeration");
    return ret;
  }

  /* Preprocess eSOS rules **********************************************************************************************/
  final Map<Integer, Set<Integer>> eSOSTerminals = new HashMap<>();;

  void normaliseAndStaticChecks() {
    Map<Integer, Integer> constructorCount = new HashMap<>(); // The number of defined rules for each constructor Map<Integer, Integer>

    // Stage one - collect information
    termRewriteConstructorDefinitions.put(iTerms.findString("_"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("_*"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'->'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'->*'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'->>'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'=>'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'=>*'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'=>>'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'~>'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'~>*'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("'~>>'"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("True"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("False"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("trLabel"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("trTransition"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("trMatch"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("trPremises"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("tr"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("trConfiguration"), 1);
    termRewriteConstructorDefinitions.put(iTerms.findString("termRewrite"), 1);

    for (Integer scanRelationIndex : trRules.keySet()) { // Step through the relations
      // System.out.println("Scanning rules for relation " +tt.toString(scanRelationIndex));
      for (Integer ruleRoot : trRules.get(scanRelationIndex).keySet()) { // Step through constructor symbol strings
        // System.out.println("Processing constructor " + iTerms.getString(ruleRoot));
        if (iTerms.hasSymbol(ruleRoot, "")) { // Add in the 'empty' constructer rules at the end of this list
          Set<Integer> emptyConstructorSet = trRules.get(scanRelationIndex).get(iTerms.findString(""));
          if (emptyConstructorSet != null) for (Integer emptyConstructorRule : emptyConstructorSet) {
            trRules.get(scanRelationIndex).get(ruleRoot).add(emptyConstructorRule);
            // System.out.println("Adding empty constructor rule " +tt.toString(emptyConstructorRule));
          }
        }
        // Collect the map of rules for this relation
        for (Integer ruleIndex : trRules.get(scanRelationIndex).get(ruleRoot)) {// Step through the list of rules
          // String tmp = iTerms.getString(ruleRoot);
          // System.out.println("Scanning rule: " +tt.toString(ruleIndex, null) + " with rule root " + tmp + " - " + tmp);
          if (termRewriteConstructorDefinitions.get(ruleRoot) == null)
            termRewriteConstructorDefinitions.put(ruleRoot, 1);
          else
            termRewriteConstructorDefinitions.put(ruleRoot, termRewriteConstructorDefinitions.get(ruleRoot) + 1);

          reportInvalidFunctionCallsRec(ruleIndex, iTerms.getSubterm(ruleIndex, 1, 1, 0));

          Map<Integer, Integer> variableNumbers = new HashMap<>();
          Set<Integer> numericVariablesInUse = new HashSet<>(); // The set of functions in use
          nextFreeVariableNumber = 2;
          collectVariablesAndConstructorsRec(ruleIndex, variableNumbers, constructorCount, functionsInUse, numericVariablesInUse, ruleIndex);

          if (numericVariablesInUse.size() > 0 && variableNumbers.size() > 0)
            System.out.println("*** Error - mix of numeric and alphanumeric variables in " + tt.toString(ruleIndex));
          for (int v : numericVariablesInUse)
            if (!iTerms.isVariableSymbol(v))
              System.out.println("*** Error - variable outside available range of _1 to _" + ITerms.variableCount + " in " + tt.toString(ruleIndex));
          if (variableNumbers.size() > ITerms.variableCount)
            System.out.println("*** Error - more than " + ITerms.variableCount + " variables used in " + tt.toString(ruleIndex));

          Map<Integer, Integer> reverseVariableNumbers = new HashMap<>();
          for (int v : variableNumbers.keySet())
            reverseVariableNumbers.put(variableNumbers.get(v), v);
          variableNamesByRule.put(ruleIndex, variableNumbers);
          reverseVariableNamesByRule.put(ruleIndex, reverseVariableNumbers);

          // System.out.print("Variable map: ");
          // for (int v : variableNumbers.keySet())
          // System.out.print(iTerms.getString(v) + " = " + variableNumbers.get(v) + " ");
          // System.out.println();
        }
      }
    }
    for (int c : constructorCount.keySet())
      if (termRewriteConstructorDefinitions.get(c) == null) {

        String label = iTerms.getString(c);

        if (label.charAt(0) == '"') continue;

        boolean isNumber = true;
        int i = 0;
        if (label.charAt(i) == '-') i++;
        for (; i < label.length(); i++) {
          char ch = label.charAt(i);
          if (!Character.isDigit(ch) && ch != '.') isNumber = false;
        }

        if (isNumber) continue;

        System.err.println("*** Warning: constructor " + label + " has no rule definitions");
      }
    // Stage two - rewrite the rules to use only only numeric variables to normalise the configurations
    for (int normaliseRelationIndex : trRules.keySet()) { // Step through the relations
      for (Integer thetaRoot : trRules.get(normaliseRelationIndex).keySet()) { // Collect the map of rules for this relation
        Set<Integer> newRuleSet = new HashSet<>();
        for (Integer ruleIndex : trRules.get(normaliseRelationIndex).get(thetaRoot)) {// Step through the list of rules
          // System.out.println("Normalising rule: " +tt.toString(ruleIndex, null));
          int rewrittenRule = normaliseRuleRec(ruleIndex, variableNamesByRule.get(ruleIndex));
          // System.out.println("Rewritten to: " +tt.toString(rewrittenRule, null));
          newRuleSet.add(rewrittenRule);
          // Add in map entries for the rewritten term, which will be the same as for the original rule!
          variableNamesByRule.put(rewrittenRule, variableNamesByRule.get(ruleIndex));
          reverseVariableNamesByRule.put(rewrittenRule, reverseVariableNamesByRule.get(ruleIndex));
        }
        trRules.get(normaliseRelationIndex).put(thetaRoot, newRuleSet);
      }
    }
  }
  /* End of preprocess eSOS rules ***************************************************************************************/

  /* Variable and function mapping ****************************************************************************/
  private int unlabeledRuleNumber = 1;

  private int normaliseRuleRec(Integer ruleIndex, Map<Integer, Integer> variableNameMap) {
    // System.out.println("normaliseRule at " + iTerms.toString(ruleIndex));
    int arity = iTerms.getTermArity(ruleIndex);
    int ruleStringIndex = iTerms.getTermSymbolIndex(ruleIndex);

    // Special case processing for unlabelled rules - generate a label ofthe form Rx
    if (arity == 0 && iTerms.hasSymbol(ruleIndex, "trLabel")) {
      // System.out.println("Generating new label R" + unlabeledRuleNumber);
      int[] newChildren = new int[1];
      newChildren[0] = iTerms.findTerm("R" + unlabeledRuleNumber++);
      return iTerms.findTerm(ruleStringIndex, newChildren);
    }

    int[] newChildren = new int[arity];

    if (variableNameMap.get(ruleStringIndex) != null) {
      // System.out.println(" rewriting " + iTerms.getString(newSymbolNameStringIndex) + " to " +
      // iTerms.getString(variableNameMap.get(newSymbolNameStringIndex)));
      ruleStringIndex = variableNameMap.get(ruleStringIndex);
    }

    for (int i = 0; i < arity; i++)
      newChildren[i] = normaliseRuleRec(iTerms.getSubterm(ruleIndex, i), variableNameMap);

    return iTerms.findTerm(ruleStringIndex, newChildren);
  }

  private int nextFreeVariableNumber = 1;

  private void collectVariablesAndConstructorsRec(int parentRewriteTermIndex, Map<Integer, Integer> variableNumbers, Map<Integer, Integer> constructorCount,
      Set<Integer> functionsInUse, Set<Integer> numericVariablesInUse, Integer termIndex) {
    // System.out.println("collectVariablesAndConstructorsRec() at " +tt.toString(termIndex, null));

    int termStringIndex = iTerms.getTermSymbolIndex(termIndex);
    if (iTerms.hasSymbol(termIndex, "trLabel")) return; // Do not go down into labels
    String termSymbolString = iTerms.getTermSymbolString(termIndex);

    if (termSymbolString.length() > 1 && termSymbolString.charAt(0) == '_' && termSymbolString.charAt(1) != '_') { // Variable
      if (iTerms.getTermArity(termIndex) > 0)
        System.out.println("*** Error: non-leaf variable " + termSymbolString + " in " + tt.toString(parentRewriteTermIndex));
      boolean isNumeric = true;
      for (int i = 1; i < termSymbolString.length(); i++)
        if (termSymbolString.charAt(i) < '0' || termSymbolString.charAt(i) > '9') isNumeric = false;
      if (isNumeric) {
        // System.out.println("Updating numericVariablesInUse with " + termSymbolString);
        numericVariablesInUse.add(termStringIndex);
      } else if (variableNumbers.get(termStringIndex) == null) {
        // System.out.println("Updating variableNumbers with " + termSymbolString + " mapped to " + nextFreeVariableNumber);
        variableNumbers.put(termStringIndex, nextFreeVariableNumber++);
      }
    } else if (termSymbolString.length() > 1 && termSymbolString.charAt(0) == '_' && termSymbolString.charAt(1) == '_') { // Function
      // System.out.println("Updating functionsInUse with " + termSymbolString);
      functionsInUse.add(termStringIndex);
    } else { // Normal constructor
      if (constructorCount.get(termStringIndex) == null) constructorCount.put(termStringIndex, 0);
      // System.out.println("Updating constructor counts for " + iTerms.getString(termStringIndex));
      constructorCount.put(termStringIndex, constructorCount.get(termStringIndex) + 1);
    }

    for (int i = 0; i < iTerms.getTermArity(termIndex); i++)
      collectVariablesAndConstructorsRec(parentRewriteTermIndex, variableNumbers, constructorCount, functionsInUse, numericVariablesInUse,
          iTerms.getSubterm(termIndex, i));
  }

  private void reportInvalidFunctionCallsRec(int parentRewriteTermIndex, int termIndex) {
    String termSymbolString = iTerms.getTermSymbolString(termIndex);
    int termStringIndex = iTerms.getTermSymbolIndex(termIndex);
    if (termSymbolString.length() > 0 && termSymbolString.charAt(0) != '_') {
      if (termRewriteConstructorUsages.get(termStringIndex) == null)
        termRewriteConstructorUsages.put(termStringIndex, 1);
      else
        termRewriteConstructorUsages.put(termStringIndex, termRewriteConstructorUsages.get(termStringIndex) + 1);
    }

    for (int i = 0; i < iTerms.getTermArity(termIndex); i++)
      reportInvalidFunctionCallsRec(parentRewriteTermIndex, iTerms.getSubterm(termIndex, i));
  }
  /* End of variable and function mapping ****************************************************************************/

  /* Module builder support ******************************************************************************************/
  public Map<Integer, Integer> getParaterminals() {
    return paraterminals;
  }

  public void buildCharacterRangeTerminal(int term) {
    String lo = iTerms.getTermSymbolString(iTerms.getSubterm(term, 0));
    String hi = iTerms.getTermSymbolString(iTerms.getSubterm(term, 1));

    char loC = lo.charAt(1);
    char hiC = hi.charAt(1);

    for (char x = loC; x <= hiC; x++)
      terminals.add(iTerms.findTerm("cfgCharacterTerminal(`" + x + ")"));
  }

  public void buildCFGRule(int term) {
    // System.out.println("Building CFG rule " + iTerms.toString(term));
    int lhsTerm = iTerms.getSubterm(term, 0), rhsTerm = iTerms.getSubterm(term, 1);
    if (getCfgRules().get(lhsTerm) == null) getCfgRules().put(lhsTerm, new HashSet<>());
    getCfgRules().get(lhsTerm).add(rhsTerm);
    if (defaultStartNonterminal == 0) defaultStartNonterminal = lhsTerm;
  }

  public void buildTRRule(int term) {
    int relation = iTerms.getSubterm(term, 1, 1, 1);
    int constructorIndex = iTerms.getTermSymbolIndex((iTerms.getSubterm(term, 1, 1, 0, 0, 0)));
    // System.out.println("Building TR rule " + iTerms.toString(term) + "\nwith relation " + iTerms.toString(relation) + "\nand constructor "
    // + iTerms.getString(constructorIndex));
    if (trRules.get(relation) == null) trRules.put(relation, new HashMap<>());
    Map<Integer, Set<Integer>> map = trRules.get(relation);
    if (map.get(constructorIndex) == null) map.put(constructorIndex, new HashSet<>());
    map.get(constructorIndex).add(term);
    if (defaultStartRelation == 0) defaultStartRelation = relation;
  }

  public void buildChooseRule(int term) {
    // System.out.println("Building choose rule " + iTerms.toString(term));
    chooseRules.add(term);
  }
  /* End of module builder support ******************************************************************************************/

  public Map<Integer, Set<Integer>> getCfgRules() {
    return cfgRules;
  }
}