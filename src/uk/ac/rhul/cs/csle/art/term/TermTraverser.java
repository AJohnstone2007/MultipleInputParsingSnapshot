package uk.ac.rhul.cs.csle.art.term;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class TermTraverser {
  protected final ITerms iTerms;
  protected final Map<Integer, Consumer<Integer>> opsPreorder = new HashMap<>();
  protected final Map<Integer, Consumer<Integer>> opsInorder = new HashMap<>();
  protected final Map<Integer, Consumer<Integer>> opsPostorder = new HashMap<>();
  protected final Set<Integer> breakSet = new HashSet<>();

  public TermTraverser(ITerms iTerms) {
    this.iTerms = iTerms;
  }

  public void addBreak(Integer... term) {
    for (Integer t : term)
      breakSet.add(t);
  }

  public void addBreak(String... termRootSymbol) {
    for (String s : termRootSymbol)
      addBreak(iTerms.findString(s));
  }

  public void addOp(String symbol, Consumer<Integer> preorder, Consumer<Integer> inorder, Consumer<Integer> postorder) {
    addOp(iTerms.findString(symbol), preorder, inorder, postorder);
  }

  public void addBreakOp(String symbol, Consumer<Integer> preorder, Consumer<Integer> inorder, Consumer<Integer> postorder) {
    addBreak(symbol);
    addOp(iTerms.findString(symbol), preorder, inorder, postorder);
  }

  //@formatter:off
  public void addOp(Integer symbolIndex, Consumer<Integer> preorder, Consumer<Integer> inorder, Consumer<Integer> postorder) {
    opsPreorder.put(symbolIndex, preorder == null ? (Integer t) -> {} : preorder);
    opsInorder.put(symbolIndex, inorder == null ? (Integer t) -> {} : inorder);
    opsPostorder.put(symbolIndex, postorder == null ? (Integer t) -> {} : postorder);
  }

  public void addNoOp(String... symbolString) { // force no operation for all actions on a particular key
    for (String s : symbolString)
      addOp(s,
          (Integer t) -> {},
          (Integer t) -> {},
          (Integer t) -> {});
  }
 //@formatter:on

  public void perform(Map<Integer, Consumer<Integer>> map, int termIndex) { // Perform an action: use default (keyed on -1) if there is no action in the table
    Consumer<Integer> action = map.get(iTerms.getTermSymbolIndex(termIndex));
    if (action == null) action = map.get(-1); // get default action
    if (action != null) action.accept(termIndex);
  }

  public void traverse(int termIndex) {
    perform(opsPreorder, termIndex);
    int[] children = iTerms.getTermChildren(termIndex);
    int length = children.length;
    int lengthLessOne = length - 1;
    if (!breakSet.contains(iTerms.getTermSymbolIndex(termIndex))) for (int i = 0; i < length; i++) {
      traverse(children[i]);
      if (i < lengthLessOne) perform(opsInorder, termIndex);
    }
    perform(opsPostorder, termIndex);
  }
}
