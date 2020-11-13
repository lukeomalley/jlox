package com.lukeomalley.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.lukeomalley.lox.Expr.Assign;
import com.lukeomalley.lox.Expr.Binary;
import com.lukeomalley.lox.Expr.Call;
import com.lukeomalley.lox.Expr.Grouping;
import com.lukeomalley.lox.Expr.Literal;
import com.lukeomalley.lox.Expr.Logical;
import com.lukeomalley.lox.Expr.Unary;
import com.lukeomalley.lox.Expr.Variable;
import com.lukeomalley.lox.Stmt.Block;
import com.lukeomalley.lox.Stmt.Expression;
import com.lukeomalley.lox.Stmt.Function;
import com.lukeomalley.lox.Stmt.If;
import com.lukeomalley.lox.Stmt.Print;
import com.lukeomalley.lox.Stmt.Return;
import com.lukeomalley.lox.Stmt.Var;
import com.lukeomalley.lox.Stmt.While;

class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void> {
  private final Interpreter interpreter;
  private final Stack<Map<String, Boolean>> scopes = new Stack<>();

  Resolver(Interpreter interpreter) {
    this.interpreter = interpreter;
  }

  void resolve(List<Stmt> statements) {
    for (Stmt statement : statements) {
      resolve(statement);
    }
  }

  private void resolve(Stmt stmt) {
    stmt.accept(this);
  }

  private void resolve(Expr expr) {
    expr.accept(this);
  }

  @Override
  public Void visitBlockStmt(Block stmt) {
    beginScope();
    resolve(stmt.statements);
    endScope();
    return null;
  }

  @Override
  public Void visitVarStmt(Var stmt) {
    declare(stmt.name);
    if (stmt.initializer != null) {
      resolve(stmt.initializer);
    }

    define(stmt.name);

    return null;
  }

  @Override
  public Void visitVariableExpr(Variable expr) {
    if (!scopes.isEmpty() && scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
      Lox.error(expr.name, "Can't read local varible in its own initializer.");
    }

    resolveLocal(expr, expr.name);
    return null;
  }

  @Override
  public Void visitAssignExpr(Assign expr) {
    resolve(expr.value);
    resolveLocal(expr, expr.name);
    return null;
  }

  @Override
  public Void visitFunctionStmt(Function stmt) {
    declare(stmt.name);
    define(stmt.name);

    resolveFunction(stmt);
    return null;
  }

  private void resolveFunction(Function function) {
    beginScope();

    for (Token param : function.params) {
      declare(param);
      define(param);
    }
    resolve(function.body);

    endScope();
  }

  @Override
  public Void visitExpressionStmt(Expression stmt) {
    resolve(stmt.expression);
    return null;
  }

  @Override
  public Void visitIfStmt(If stmt) {
    resolve(stmt.condition);
    resolve(stmt.thenBranch);

    if (stmt.elseBranch != null) {
      resolve(stmt.elseBranch);
    }

    return null;
  }

  @Override
  public Void visitPrintStmt(Print stmt) {
    resolve(stmt.expression);
    return null;
  }

  @Override
  public Void visitReturnStmt(Return stmt) {
    if (stmt.value != null) {
      resolve(stmt.value);
    }

    return null;
  }

  @Override
  public Void visitWhileStmt(While stmt) {
    resolve(stmt.condition);
    resolve(stmt.body);
    return null;
  }

  @Override
  public Void visitBinaryExpr(Binary expr) {
    resolve(expr.left);
    resolve(expr.right);
    return null;
  }

  @Override
  public Void visitCallExpr(Call expr) {
    resolve(expr.callee);

    for (Expr argument : expr.arguments) {
      resolve(argument);
    }

    return null;
  }

  @Override
  public Void visitGroupingExpr(Grouping expr) {
    resolve(expr.expression);
    return null;
  }

  @Override
  public Void visitLiteralExpr(Literal expr) {
    return null;
  }

  @Override
  public Void visitLogicalExpr(Logical expr) {
    resolve(expr.left);
    resolve(expr.right);
    return null;
  }

  @Override
  public Void visitUnaryExpr(Unary expr) {
    resolve(expr.right);
    return null;
  }

  private void resolveLocal(Expr expr, Token name) {
    for (int i = scopes.size() - 1; i >= 0; i--) {
      if (scopes.get(i).containsKey(name.lexeme)) {
        interpreter.resolve(expr, scopes.size() - 1 - i);
        return;
      }

    }
  }

  private void declare(Token name) {
    if (scopes.isEmpty()) {
      return;
    }

    Map<String, Boolean> scope = scopes.peek();

    scope.put(name.lexeme, false);
  }

  private void define(Token name) {
    if (scopes.isEmpty()) {
      return;
    }

    scopes.peek().put(name.lexeme, true);
  }

  private void beginScope() {
    scopes.push(new HashMap<String, Boolean>());
  }

  private void endScope() {
    scopes.pop();
  }

}
