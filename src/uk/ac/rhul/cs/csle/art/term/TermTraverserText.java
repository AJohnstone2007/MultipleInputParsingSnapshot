package uk.ac.rhul.cs.csle.art.term;

import java.util.HashMap;
import java.util.Map;

/* This extension to TermTraverser adds text specific functions that allow a traverser to build a String rendering of a term
*/

public class TermTraverserText extends TermTraverser {
  // TODO: Shouldn't this be stringIndex to stringIndex for efficiency?
  private final Map<String, String> globalAliases = new HashMap<>();
  private Map<Integer, Integer> localAliases = null;
  private final StringBuilder sb = new StringBuilder();
  private int depthLimit = -1;

  public TermTraverserText(ITerms iTerms) {
    super(iTerms);
  }

  //@formatter:off
  public void addOp(String symbol, String preorder, String inorder, String postorder) {
    addOp(symbol,
        (preorder == null ? null : (Integer t) -> append(preorder)),
        (inorder == null ? null : (Integer t) -> append(inorder)),
        (postorder == null ? null : (Integer t) -> append(postorder)));
  }
  //@formatter:on

  public void addBreakOp(String symbol, String preorder, String inorder, String postorder) {
    addBreak(symbol);
    addOp(symbol, preorder, inorder, postorder);
  }

  /* Aliases allow strings to be mapped to other strings - this is intensively used for LaTeX outputs */
  public void addAlias(String key, String value) {
    globalAliases.put(key, value);
  }

  private String mapToAlias(String str) {
    String ret = globalAliases.get(str);

    return ret == null ? str : ret;
  }

  public String childSymbolString(int root, int childNumber) {
    return iTerms.getTermSymbolString(iTerms.getTermChildren(root)[childNumber]);
  }

  public String childStrippedSymbolString(int root, int childNumber) {
    String str = childSymbolString(root, childNumber);
    return str.substring(1, str.length() - 1);
  }

  public void clear() {
    sb.setLength(0);
  }

  public void append(String str) {
    sb.append(mapToAlias(str));
  }

  public String getString() {
    return sb.toString();
  }

  public void traverse(int termIndex, int depth) {
    System.out.println("TermTraveserText.traverse at " + termIndex);

    perform(opsPreorder, termIndex);
    if (depthLimit >= 0 && depth >= depthLimit)
      append("..");
    else {
      int[] children = iTerms.getTermChildren(termIndex);
      int length = children.length;
      int lengthLessOne = length - 1;
      if (!breakSet.contains(iTerms.getTermSymbolIndex(termIndex))) for (int i = 0; i < length; i++) {
        traverse(children[i]);
        if (i < lengthLessOne) perform(opsInorder, termIndex);
      }
    }
    perform(opsPostorder, termIndex);
  }

  public String toString(Integer term) {
    clear();
    traverse(term);
    return sb.toString();
  }

  public String toString(Integer term, Integer depthLimit) {
    this.depthLimit = depthLimit;
    String str = toString(term);
    this.depthLimit = -1;
    return str;
  }

  public String toString(Integer term, Map<Integer, Integer> localAliases) {
    this.localAliases = localAliases;
    String str = toString(term);
    this.localAliases = null;
    return str;
  }
}
