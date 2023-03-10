package com.sun.scenario.effect.compiler.backend.hw;
import com.sun.scenario.effect.compiler.JSLParser;
import com.sun.scenario.effect.compiler.model.BinaryOpType;
import com.sun.scenario.effect.compiler.model.Function;
import com.sun.scenario.effect.compiler.model.Param;
import com.sun.scenario.effect.compiler.model.Precision;
import com.sun.scenario.effect.compiler.model.Qualifier;
import com.sun.scenario.effect.compiler.model.Type;
import com.sun.scenario.effect.compiler.model.Variable;
import com.sun.scenario.effect.compiler.tree.ArrayAccessExpr;
import com.sun.scenario.effect.compiler.tree.BinaryExpr;
import com.sun.scenario.effect.compiler.tree.BreakStmt;
import com.sun.scenario.effect.compiler.tree.CallExpr;
import com.sun.scenario.effect.compiler.tree.CompoundStmt;
import com.sun.scenario.effect.compiler.tree.ContinueStmt;
import com.sun.scenario.effect.compiler.tree.DeclStmt;
import com.sun.scenario.effect.compiler.tree.DiscardStmt;
import com.sun.scenario.effect.compiler.tree.DoWhileStmt;
import com.sun.scenario.effect.compiler.tree.Expr;
import com.sun.scenario.effect.compiler.tree.ExprStmt;
import com.sun.scenario.effect.compiler.tree.FieldSelectExpr;
import com.sun.scenario.effect.compiler.tree.ForStmt;
import com.sun.scenario.effect.compiler.tree.FuncDef;
import com.sun.scenario.effect.compiler.tree.JSLVisitor;
import com.sun.scenario.effect.compiler.tree.LiteralExpr;
import com.sun.scenario.effect.compiler.tree.ParenExpr;
import com.sun.scenario.effect.compiler.tree.ProgramUnit;
import com.sun.scenario.effect.compiler.tree.ReturnStmt;
import com.sun.scenario.effect.compiler.tree.SelectStmt;
import com.sun.scenario.effect.compiler.tree.Stmt;
import com.sun.scenario.effect.compiler.tree.TreeScanner;
import com.sun.scenario.effect.compiler.tree.UnaryExpr;
import com.sun.scenario.effect.compiler.tree.VarDecl;
import com.sun.scenario.effect.compiler.tree.VariableExpr;
import com.sun.scenario.effect.compiler.tree.VectorCtorExpr;
import com.sun.scenario.effect.compiler.tree.WhileStmt;
public abstract class SLBackend extends TreeScanner {
private JSLParser parser;
protected JSLVisitor visitor;
private StringBuilder sb = new StringBuilder();
private Variable unrollVar = null;
private int unrollIndex = -1;
protected boolean isPixcoordReferenced;
protected boolean isVertexColorReferenced;
protected int maxTexCoordIndex = -1;
protected SLBackend(JSLParser parser, JSLVisitor visitor) {
this.parser = parser;
this.visitor = visitor;
}
protected final void output(String s) {
sb.append(s);
}
public String getShader() {
return getHeader() + sb.toString();
}
protected final JSLParser getParser() {
return parser;
}
protected String getHeader() {
return "";
}
protected String getPrecision(Precision p) {
return null;
}
protected abstract String getType(Type t);
protected abstract String getQualifier(Qualifier q);
protected abstract String getVar(String v);
protected abstract String getFuncName(String f);
@Override
public void visitArrayAccessExpr(ArrayAccessExpr e) {
scan(e.getExpr());
output("[");
boolean needsScan = true;
if (unrollVar != null) {
if (e.getIndex() instanceof VariableExpr) {
VariableExpr vexpr = (VariableExpr)e.getIndex();
if (vexpr.getVariable() == unrollVar) {
output("" + unrollIndex);
needsScan = false;
}
}
}
if (needsScan) {
scan(e.getIndex());
}
output("]");
}
@Override
public void visitBinaryExpr(BinaryExpr e) {
scan(e.getLeft());
output(" " + e.getOp() + " ");
scan(e.getRight());
}
@Override
public void visitBreakStmt(BreakStmt s) {
output("break;");
}
@Override
public void visitCallExpr(CallExpr e) {
output(getFuncName(e.getFunction().getName()) + "(");
boolean first = true;
for (Expr param : e.getParams()) {
if (first) {
first = false;
} else {
output(", ");
}
scan(param);
}
output(")");
}
@Override
public void visitCompoundStmt(CompoundStmt s) {
output("{\n");
super.visitCompoundStmt(s);
output("}\n");
}
@Override
public void visitContinueStmt(ContinueStmt s) {
output("continue;");
}
@Override
public void visitDeclStmt(DeclStmt s) {
super.visitDeclStmt(s);
}
@Override
public void visitDiscardStmt(DiscardStmt s) {
output("discard;");
}
@Override
public void visitDoWhileStmt(DoWhileStmt s) {
output("do ");
scan(s.getStmt());
output(" while (");
scan(s.getExpr());
output(");");
}
@Override
public void visitExprStmt(ExprStmt s) {
scan(s.getExpr());
output(";\n");
}
@Override
public void visitFieldSelectExpr(FieldSelectExpr e) {
scan(e.getExpr());
output("." + e.getFields());
}
@Override
public void visitForStmt(ForStmt s) {
boolean unroll = s.getUnrollMax() > 0;
if (unroll) {
if (unrollVar != null) {
throw new RuntimeException("Unrolling of nested 'for' loops not yet supported");
}
String maxName = null;
Expr cond = s.getCondition();
if (cond instanceof BinaryExpr) {
BinaryExpr bcond = (BinaryExpr)cond;
if (bcond.getOp() != BinaryOpType.LT) {
throw new RuntimeException("Condition must be '<' in order to unroll 'for' loop (for now)");
}
Expr left = bcond.getLeft();
if (left instanceof VariableExpr) {
VariableExpr vexpr = (VariableExpr)left;
Variable var = vexpr.getVariable();
if (var.getType() != Type.INT) {
throw new RuntimeException("Condition LHS must be integer variable in order to unroll 'for' loop (for now)");
}
unrollVar = var;
} else {
throw new RuntimeException("Condition LHS must be integer variable in order to unroll 'for' loop (for now)");
}
Expr right = bcond.getRight();
if (right instanceof VariableExpr) {
VariableExpr vexpr = (VariableExpr)right;
Variable var = vexpr.getVariable();
maxName = var.getName();
} else if (s.getUnrollCheck() > 0) {
throw new RuntimeException("Condition RHS must be variable in order to unroll 'for' loop");
}
} else {
throw new RuntimeException("Invalid unrollable 'for' loop condition");
}
int max = s.getUnrollMax();
int check = max - s.getUnrollCheck();
for (int i = 0; i < s.getUnrollMax(); i++) {
if (i >= check) {
output("if (" + i + " < " + maxName + ")\n");
}
unrollIndex = i;
scan(s.getStmt());
}
unrollVar = null;
unrollIndex = -1;
} else {
output("for (");
scan(s.getInit());
scan(s.getCondition());
output(";");
scan(s.getExpr());
output(")");
scan(s.getStmt());
}
}
@Override
public void visitFuncDef(FuncDef d) {
Function func = d.getFunction();
output(getType(func.getReturnType()) + " " + func.getName() + "(");
boolean first = true;
for (Param param : func.getParams()) {
if (first) {
first = false;
} else {
output(", ");
}
output(getType(param.getType()) + " " + param.getName());
}
output(") ");
scan(d.getStmt());
}
@Override
public void visitLiteralExpr(LiteralExpr e) {
output(e.getValue().toString());
}
@Override
public void visitParenExpr(ParenExpr e) {
output("(");
scan(e.getExpr());
output(")");
}
@Override
public void visitProgramUnit(ProgramUnit p) {
super.visitProgramUnit(p);
}
@Override
public void visitReturnStmt(ReturnStmt s) {
output("return");
Expr ret = s.getExpr();
if (ret != null) {
output(" ");
scan(s.getExpr());
}
output(";\n");
}
@Override
public void visitSelectStmt(SelectStmt s) {
output("if (");
scan(s.getIfExpr());
output(")");
scan(s.getThenStmt());
Stmt e = s.getElseStmt();
if (e != null) {
output(" else ");
scan(e);
}
}
@Override
public void visitUnaryExpr(UnaryExpr e) {
output(e.getOp().toString());
scan(e.getExpr());
}
@Override
public void visitVarDecl(VarDecl d) {
Variable var = d.getVariable();
Qualifier qual = var.getQualifier();
if (qual != null) {
output(getQualifier(qual) + " ");
}
Precision precision = var.getPrecision();
if (precision != null) {
String precisionStr = getPrecision(precision);
if (precisionStr != null) {
output(precisionStr + " ");
}
}
output(getType(var.getType()) + " " + var.getName());
if (var.isArray()) {
output("[" + var.getArraySize() + "]");
}
Expr init = d.getInit();
if (init != null) {
output(" = ");
scan(init);
}
output(";\n");
}
@Override
public void visitVariableExpr(VariableExpr e) {
String varName = e.getVariable().getName();
if (varName.equals("pixcoord")) {
isPixcoordReferenced = true;
} else if (varName.equals("pos0")) {
maxTexCoordIndex = Math.max(maxTexCoordIndex, 0);
} else if (varName.equals("pos1")) {
maxTexCoordIndex = Math.max(maxTexCoordIndex, 1);
} else if (varName.equals("jsl_vertexColor")) {
isVertexColorReferenced = true;
}
output(getVar(varName));
}
@Override
public void visitVectorCtorExpr(VectorCtorExpr e) {
output(getType(e.getType()));
output("(");
boolean first = true;
for (Expr param : e.getParams()) {
if (first) {
first = false;
} else {
output(", ");
}
scan(param);
}
output(")");
}
@Override
public void visitWhileStmt(WhileStmt s) {
output("while (");
scan(s.getCondition());
output(")");
scan(s.getStmt());
}
}
