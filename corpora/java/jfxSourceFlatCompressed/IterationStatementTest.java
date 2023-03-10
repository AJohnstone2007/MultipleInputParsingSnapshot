package com.sun.scenario.effect.compiler.parser;
import com.sun.scenario.effect.compiler.JSLParser;
import com.sun.scenario.effect.compiler.model.Type;
import com.sun.scenario.effect.compiler.tree.BinaryExpr;
import com.sun.scenario.effect.compiler.tree.DoWhileStmt;
import com.sun.scenario.effect.compiler.tree.ExprStmt;
import com.sun.scenario.effect.compiler.tree.ForStmt;
import com.sun.scenario.effect.compiler.tree.JSLVisitor;
import com.sun.scenario.effect.compiler.tree.Stmt;
import com.sun.scenario.effect.compiler.tree.WhileStmt;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.junit.Test;
import static org.junit.Assert.*;
public class IterationStatementTest extends ParserBase {
@Test
public void whileStmt() throws Exception {
Stmt tree = parseTreeFor("while (i >= 3) j += 4;");
assertTrue(tree instanceof WhileStmt);
WhileStmt s = (WhileStmt)tree;
assertTrue(s.getCondition() instanceof BinaryExpr);
assertTrue(s.getStmt() instanceof ExprStmt);
}
@Test
public void doWhileStmt() throws Exception {
Stmt tree = parseTreeFor("do j += 4; while (i >= 3);");
assertTrue(tree instanceof DoWhileStmt);
DoWhileStmt s = (DoWhileStmt)tree;
assertTrue(s.getStmt() instanceof ExprStmt);
assertTrue(s.getExpr() instanceof BinaryExpr);
}
@Test
public void forStmt() throws Exception {
Stmt tree = parseTreeFor("for (i = 0; i < 5; i += 2) j += 4;");
assertTrue(tree instanceof ForStmt);
ForStmt s = (ForStmt)tree;
assertTrue(s.getInit() instanceof ExprStmt);
assertTrue(s.getCondition() instanceof BinaryExpr);
assertTrue(s.getExpr() instanceof BinaryExpr);
assertTrue(s.getStmt() instanceof ExprStmt);
}
@Test
public void forStmtNoCondition() throws Exception {
Stmt tree = parseTreeFor("for (i = 0; ; i += 2) j += 4;");
assertTrue(tree instanceof ForStmt);
ForStmt s = (ForStmt)tree;
assertTrue(s.getInit() instanceof ExprStmt);
assertNull(s.getCondition());
assertTrue(s.getExpr() instanceof BinaryExpr);
assertTrue(s.getStmt() instanceof ExprStmt);
}
@Test
public void forStmtNoIncrement() throws Exception {
Stmt tree = parseTreeFor("for (i = 0; i < 5; ) j += 4;");
assertTrue(tree instanceof ForStmt);
ForStmt s = (ForStmt)tree;
assertTrue(s.getInit() instanceof ExprStmt);
assertTrue(s.getCondition() instanceof BinaryExpr);
assertNull(s.getExpr());
assertTrue(s.getStmt() instanceof ExprStmt);
}
@Test
public void forStmtNoConditionOrIncrement() throws Exception {
Stmt tree = parseTreeFor("for (i = 0; ; ) j += 4;");
assertTrue(tree instanceof ForStmt);
ForStmt s = (ForStmt)tree;
assertTrue(s.getInit() instanceof ExprStmt);
assertNull(s.getCondition());
assertNull(s.getExpr());
assertTrue(s.getStmt() instanceof ExprStmt);
}
@Test(expected = ParseCancellationException.class)
public void notAnIterationStmt() throws Exception {
parseTreeFor("return;");
}
private Stmt parseTreeFor(String text) throws Exception {
JSLParser parser = parserOver(text);
JSLVisitor visitor = new JSLVisitor();
visitor.getSymbolTable().declareVariable("i", Type.INT, null);
visitor.getSymbolTable().declareVariable("j", Type.INT, null);
return visitor.visitIteration_statement(parser.iteration_statement());
}
}
