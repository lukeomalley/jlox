package com.lukeomalley.lox;

import java.util.List;

abstract class Expr {

  static class Binary extends Expr {
    final Expr left;
    final Token operator;
    final Expr right;

    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }
  }

  static class Grouping extends Expr {
    final Expr expression;

    Grouping(Expr expression) {
      this.expression = expression;
    }
  }

  static class Literal extends Expr {
    final Object value;

    Literal(Object value) {
      this.value = value;
    }
  }

  static class Urnary extends Expr {
    final Token operator;
    final Expr right;

    Urnary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }
  }

}
