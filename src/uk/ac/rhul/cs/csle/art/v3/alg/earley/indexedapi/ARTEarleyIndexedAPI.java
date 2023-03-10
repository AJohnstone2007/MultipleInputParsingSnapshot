package uk.ac.rhul.cs.csle.art.v3.alg.earley.indexedapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import uk.ac.rhul.cs.csle.art.util.slotarray.ARTSlotArray;
import uk.ac.rhul.cs.csle.art.v3.alg.ARTParserBase;
import uk.ac.rhul.cs.csle.art.v3.alg.gll.support.ARTGLLAttributeBlock;
import uk.ac.rhul.cs.csle.art.v3.lex.ARTLexerV3;
import uk.ac.rhul.cs.csle.art.v3.manager.grammar.ARTGrammar;
import uk.ac.rhul.cs.csle.art.v3.manager.grammar.element.ARTGrammarElementTerminal;

public class ARTEarleyIndexedAPI extends ARTParserBase {
  /*
   * This code is derived from ARTEarlySPPPBasic.
   *
   * Changes:
   *
   * 1. Use slot array instead of grammar
   *
   * 2. Guard the creation of terminals with an i = n check to avoid spurious $ terminal (this mod is in Basic too but missing from paper)
   *
   */
  private int inputLength;
  private ARTEarleySPPFNode epsilonSPPFNode;
  private int qSetRemovals, rSetRemovals;

  // Set declarations: initialisation in parse()
  private ArrayList<Set<ARTEarleyItem>> eSets;
  private Set<ARTEarleyItem> qSet, qPrimeSet, rSet;
  private Map<ARTEarleySPPFNode, ARTEarleySPPFNode> vMap; // Use a map to overcome Java's lack of a get() method for
                                                          // class Set<>
  private Map<Integer, ARTEarleySPPFNode> hMap;
  private Set<ARTEarleySPPFNode> sppf; // Remember all SPPF nodes for rendering - not needed for core algorithm
  private Set<Integer> acceptingSlotsSet;

  public ARTEarleyIndexedAPI(ARTGrammar grammar) {
    super(grammar);
    artGrammar.getARTManager().printMemory(this.getClass().getSimpleName() + " start of constructor");
    artSlotArray = new ARTSlotArray(grammar);
  }

  private boolean inSigmaN(int p) {
    return artSlotArray.isNonterminalOrEpsilon(artSlotArray.slotRightSymbols[p]);
  }

  // These are 'support functions' which bury the OO API accesses inside function
  // calls so as to ease conversion to ANSI-C
  private void setAdd(Set<ARTEarleyItem> set, ARTEarleyItem element) {
    if (artTrace) artTraceText.println("Adding to Earley set " + element.toString(artSlotArray));
    set.add(element);
  }

  private ARTEarleyItem setRemove(Set<ARTEarleyItem> set) {
    ARTEarleyItem element;
    Iterator<ARTEarleyItem> iterator = set.iterator();
    element = iterator.next();
    set.remove(element);
    return element;
  }

  private String setToString(Set<ARTEarleyItem> set, ARTSlotArray artSlotArray) {
    String ret = "[";

    boolean first = true;

    for (ARTEarleyItem s : set) {
      if (first)
        first = false;
      else
        ret += ", ";
      ret += s.toString(artSlotArray);
    }
    return ret + "]";
  }

  private void printSets() {
    artTraceText.println("Earley sets");
    for (int i = 0; i < eSets.size(); i++)
      for (ARTEarleyItem s : eSets.get(i))
        artTraceText.println(i + ": " + s.toString(artSlotArray));
    artTraceText
        .println("Q = " + setToString(qSet, artSlotArray) + "\nQ' = " + setToString(qPrimeSet, artSlotArray) + "\nR = " + setToString(rSet, artSlotArray));

    artTraceText.println("Total removals from R =  " + rSetRemovals + ", total removals from Q = " + qSetRemovals);
  }

  @Override
  public void artParse(String stringInput, ARTGLLAttributeBlock startAttributes) {

    if (artNotBNF()) {
      if (artTrace) artTraceText.println(this.getClass() + " called on EBNF grammar aborting");
      return;
    }

    epsilonSPPFNode = new ARTEarleySPPFNode(artSlotArray.epsilon, 0, 0);

    int p;
    acceptingSlotsSet = new HashSet<>();
    for (int productionNumber = 0; (p = artSlotArray.slotIndex[artSlotArray.startSymbol][productionNumber]) != 0; productionNumber++) {
      // !! update p to be the accepting slot!
      p++; // go to child
      while (artSlotArray.slotRightSymbols[p] != 0)
        p++;
      acceptingSlotsSet.add(p);
    }

    ArrayList<ARTGrammarElementTerminal> input = new ARTLexerV3(artGrammar).lexicaliseToArrayListOfTerminals(stringInput, 1);

    if (input == null) {
      System.out.println("EarleyIndexedAPI: reject lexical");
      if (artTrace) artTraceText.println("Reject lexical");
      // log(inputFilename);
      return;
    }

    inputLength = input.size() - 2; // input[0] is not used and input[n+1] is $

    if (artTrace) {
      artTraceText.println("EarleyIndexedAPI running on " + inputLength + " tokens");
    }

    // E0 , . . . , En , R, Q??? , V = ???
    eSets = new ArrayList<>();
    for (int i = 0; i < inputLength + 1; i++)
      eSets.add(new HashSet<>());
    rSet = new HashSet<>(); // redundant?
    qPrimeSet = new HashSet<>();
    vMap = new HashMap<>();
    hMap = new HashMap<>();
    sppf = new HashSet<>();

    // artGrammar.getARTManager().forceGC(this.getClass().getSimpleName() + " start of parse");
    artRestartClock();

    // for all (S ::= ??) ??? P { if ?? ??? ??N add (S ::= ????, 0, null) to E0
    // if ?? = a1 ????? add (S ::= ????, 0, null) to Q??? } !! Q' is now Q[0] for this
    // initialisation phase
    for (int productionNumber = 0; (p = artSlotArray.slotIndex[artSlotArray.startSymbol][productionNumber]) != 0; productionNumber++) {
      if (inSigmaN(p)) setAdd(eSets.get(0), new ARTEarleyItem(p + 1, 0, null));
      if (artSlotArray.slotRightSymbols[p + 1] == input.get(1).getElementNumber()) setAdd(qPrimeSet, new ARTEarleyItem(p + 1, 0, null));
    }
    // for 0 ??? i ??? n {
    for (int i = 0; i <= inputLength; i++) {

      // H = ???, R = Ei , Q = Q??? !! Q = Q' now not needed as we have Qi instead
      hMap.clear();
      rSet = new HashSet<>(eSets.get(i));
      qSet = new HashSet<>(qPrimeSet);

      // Q??? = ???
      qPrimeSet = new HashSet<>();

      // while R ! = ??? {
      while (!rSet.isEmpty()) {
        if (artTrace) artTraceText.println("At top of main loop, rSet is " + setToString(rSet, artSlotArray));
        // remove an element, ?? say, from R
        ARTEarleyItem lambda = setRemove(rSet);
        if (artTrace) artTraceText.println("Processing " + lambda.toString(artSlotArray));

        int c = artSlotArray.slotRightSymbols[lambda.slot];
        int h = lambda.i;
        ARTEarleySPPFNode w = lambda.sppfNode;

        // if ?? = (B ::= ?? ?? C??, h, w) {
        if (artSlotArray.isNonterminal(c)) {
          // for all (C ::= ??) ??? P {
          int deltaSlot;
          for (int productionNumber = 0; (deltaSlot = artSlotArray.slotIndex[c][productionNumber]) != 0; productionNumber++) {
            // if ?? ??? ??N and (C ::= ????, i, null) ! ??? Ei {
            deltaSlot++; // Move to first child
            ARTEarleyItem eSetCandidate1 = new ARTEarleyItem(deltaSlot, i, null);
            if (inSigmaN(deltaSlot) && !eSets.get(i).contains(eSetCandidate1)) {
              // add (C ::= ????, i, null) to Ei and R }
              setAdd(eSets.get(i), eSetCandidate1);
              setAdd(rSet, eSetCandidate1);
            }
            // if ?? = ai+1 ??' { add (C ::= ????, i, null) to Q } }
            if (artSlotArray.slotRightSymbols[deltaSlot] == input.get(i + 1).getElementNumber()) {
              setAdd(qSet, eSetCandidate1);
            }
          }
          // if ((C, v) ??? H) {
          ARTEarleySPPFNode v;
          if ((v = hMap.get(c)) != null) {
            // let y = MAKE_NODE(B ::= ??C ?? ??, h, i, w, v, V)
            int betaPreSlot = lambda.slot + 1;
            ARTEarleySPPFNode y = makeNode(betaPreSlot, h, i, w, v, vMap);
            ARTEarleyItem eSetCandidate2 = new ARTEarleyItem(betaPreSlot, h, y);
            // if ?? ??? ??N and (B ::= ??C ?? ??, h, y) ! ??? Ei {
            if (inSigmaN(betaPreSlot) && !eSets.get(i).contains(eSetCandidate2)) {
              // add (B ::= ??C ?? ??, h, y) to Ei and R }
              setAdd(eSets.get(i), eSetCandidate2);
              setAdd(rSet, eSetCandidate2);
            }
            // if ?? = ai+1 ?? ??? { add (B ::= ??C ?? ??, h, y) to Q }
            int betaFirst = artSlotArray.slotRightSymbols[betaPreSlot];
            if (artSlotArray.isTerminal(betaFirst) && betaFirst == input.get(i + 1).getElementNumber()) {
              setAdd(qSet, eSetCandidate2);
            }
          }
        }

        // if ?? = (D ::= ????, h, w) {
        int D = artSlotArray.slotLHSSymbols[lambda.slot];
        if (artSlotArray.slotRightSymbols[lambda.slot] == 0 || artSlotArray.slotRightSymbols[lambda.slot] == artSlotArray.epsilon) {
          if (artTrace) artTraceText.println("Processing end slot item " + lambda.toString(artSlotArray));
          // if w = null {
          if (w == null) {
            // if there is no node v in V labelled (D, i, i) create one
            ARTEarleySPPFNode v = new ARTEarleySPPFNode(D, i, i);
            sppf.add(v);
            // set w = v
            w = v;
            // if w does not have family (epsilon) add one }
            w.addFamily(epsilonSPPFNode);
          }
          // if h = i { add (D, w) to H }
          if (h == i) {
            hMap.put(D, w);
          }
          // for all (A ::= ?? ?? D??, k, z) in Eh {
          for (ARTEarleyItem e : new LinkedList<>(eSets.get(h))) {
            // major inefficiency here: rework as a set of hashmaps from D to items
            if (artSlotArray.slotRightSymbols[e.slot] == D) {
              // let y = MAKE_NODE(A ::= ?? D ?? ??, k, i, z, w, V)
              int k = e.i;
              ARTEarleySPPFNode z = e.sppfNode;
              int deltaPreSlot = e.slot + 1;
              ARTEarleySPPFNode y = makeNode(deltaPreSlot, k, i, z, w, vMap);
              // if ?? ??? ??N and (A ::= ?? D ?? ??, k, y) ! ??? Ei {
              ARTEarleyItem candidateItem = new ARTEarleyItem(deltaPreSlot, k, y);
              if (inSigmaN(deltaPreSlot) && !eSets.get(i).contains(candidateItem)) {
                // add (A ::= ?? D ?? ??, k, y) to Ei and R }
                setAdd(eSets.get(i), candidateItem);
                setAdd(rSet, candidateItem);
              }
              // if ?? = ai+1 ?? ??? { add (A ::= ?? D ?? ??, k, y) to Q } }
              if (artSlotArray.slotRightSymbols[deltaPreSlot] != 0 && artSlotArray.slotRightSymbols[deltaPreSlot] == input.get(i + 1).getElementNumber()) {
                setAdd(qSet, candidateItem);
              }
            }
          }
        }
      }
      // V=???
      vMap.clear();
      // create an SPPF node v labelled (ai+1 , i, i + 1)
      if (i != inputLength) {
        ARTEarleySPPFNode v = new ARTEarleySPPFNode(input.get(i + 1).getElementNumber(), i, i + 1);
        sppf.add(v);
        // while Q ! = ??? {
        while (!qSet.isEmpty()) {
          // remove an element, ?? = (B ::= ?? ?? ai+1 ??, h, w) say, from Q
          ARTEarleyItem lambda = setRemove(qSet);

          int postAlphaSlot = lambda.slot;
          int h = lambda.i;
          ARTEarleySPPFNode w = lambda.sppfNode;
          // let y = MAKE_NODE(B ::= ?? ai+1 ?? ??, h, i + 1, w, v, V)
          int preBetaSlot = postAlphaSlot + 1;
          ARTEarleySPPFNode y = makeNode(preBetaSlot, h, i + 1, w, v, vMap);
          // if ?? ??? ??N { add (B ::= ?? ai+1 ?? ??, h, y) to Ei+1 }
          ARTEarleyItem candidateItem = new ARTEarleyItem(preBetaSlot, h, y);
          if (inSigmaN(preBetaSlot)) {
            setAdd(eSets.get(i + 1), candidateItem);
          }

          // if ?? = ai+2 ????? { add (B ::= ?? ai+1 ?? ??, h, y) to Q??? }
          if (artSlotArray.slotRightSymbols[preBetaSlot] != 0 && artSlotArray.slotRightSymbols[preBetaSlot] == input.get(i + 2).getElementNumber()) {
            setAdd(qPrimeSet, candidateItem);
          }
        }
      }
    }
    artParseCompleteTime = artReadClock();
    // artGrammar.getARTManager().forceGC(this.getClass().getSimpleName() + " end of parse");

    // if (S ::= ?? ??, 0, w) ??? En return wb

    // Scan eSets.get(inputLength) to look for accepting slots and some w, then
    // return w
    if (artTrace) artTraceText.println("input length is " + inputLength + "\naccepting slots are " + acceptingSlotsSet + "\nfinal Earley set is "
        + setToString(eSets.get(inputLength), artSlotArray));

    for (ARTEarleyItem e : eSets.get(inputLength))
      if (e.i == 0 && acceptingSlotsSet.contains(e.slot)) {
        artIsInLanguage = true;
        System.out.println("EarleyIndexedAPI " + (artIsInLanguage ? "accept" : "reject") + " in " + artParseCompleteTime * 1E-6 + "ms");
        if (artTrace) {
          artTraceText.println("EarleyIndexedAPI: accept");
          printSets();
          artTraceText.close();
        }
        return /* w */ /* e.sppfNode */;
      }
    // else return failure
    {
      System.out.println("EarleyIndexedAPI: reject");
      artIsInLanguage = false;
      System.out.println("EarleyIndexedAPI " + (artIsInLanguage ? "accept" : "reject") + " in " + artParseCompleteTime * 1E-6 + "ms");
      if (artTrace) {
        artTraceText.println("EarleyIndexedAPI: reject");
        printSets();
        artTraceText.close();
      }
      return;
    }
  }

  // MAKE_NODE(B ::= ??x ?? ??, j, i, w, v, V) {
  ARTEarleySPPFNode makeNode(int betaSlot, int j, int i, ARTEarleySPPFNode w, ARTEarleySPPFNode v, Map<ARTEarleySPPFNode, ARTEarleySPPFNode> vSet2) {
    if (artTrace) artTraceText.println("MAKE_NODE(" + artSlotArray.symbolJavaStrings[betaSlot] + ", " + i + ", " + j + ")");

    // if ?? = epsilon { let s = B } else { let s = (B ::= ??x ?? ??) }
    int s;
    if (artSlotArray.slotRightSymbols[betaSlot] == 0)
      s = artSlotArray.slotLHSSymbols[betaSlot];
    else
      s = betaSlot;

    // if ?? = epsilon and ?? != epsilon { let y = v }
    ARTEarleySPPFNode y;
    int postAlphaSlot = betaSlot - 2;
    if (artSlotArray.slotRightSymbols[postAlphaSlot] == 0 && artSlotArray.slotRightSymbols[betaSlot] != 0) {
      y = v;
    }
    // else {
    else {
      ARTEarleySPPFNode candidateItem, candidateItemImage;
      candidateItem = new ARTEarleySPPFNode(s, j, i);
      // if there is no node y in V labelled (s, j, i) create one and add it to V
      if ((candidateItemImage = vMap.get(candidateItem)) == null) {
        y = candidateItem;
        vMap.put(y, y);
        sppf.add(candidateItem);
      } else
        y = candidateItemImage;

      // if w = null and y does not have a family of children (v) add one
      if (w == null)
        y.addFamily(v);
      // if w != null and y does not have a family of children (w, v) add one }
      else
        y.addFamily(w, v);
    }

    // return y
    return y;
  }

  @Override
  public void artParse(String inputString) {
    System.out.println("Internal error: no grammar supplied to " + this.getClass());
  }

  @Override
  public void artWriteRDT(String filename) {
    // TODO Auto-generated method stub

  }

  @Override
  public void artPrintRDT() {
    // TODO Auto-generated method stub

  }
}