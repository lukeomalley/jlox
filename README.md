# 🪄 Jlox

> Tree-walking interpreter for the lox programming language. Supports conditionals, iteration, first class functions, closures, recursion, classes & inheritance.

## How it Works

1. Tokenizing - First the raw source code is transformed into a list of tokens.
2. Parsing - Through a recursive descent stratedgy, the parser scans the tokens and generates an abstract syntax tree (ast).
3. Resolving - The resolving stage walks te ast and binds variable definitions and function dcelarations to their respective enrivonments. This allows for the proper functionality of closures. Additionally, performs a few compile-time validations such as verifying that super and this are used in the right context.
4. Interpreting - In the final step, the intrepreter walks the ast and evaluates the code thus producing the output for the users program.

## Sample Code

Classes

```javascript
class Cake {

  init(flavor) {
    this.flavor = flavor;
  }

  taste() {
    var adjective = "delicious";
    print "The " + this.flavor + " cake is " + adjective + "!";
  }
}

var cake = Cake("German chocolate");
cake.taste();
```

Inheritance

```javascript
class Shape {
  rotate(degrees) {
    print "Rotating";
  }
}

class Square < Shape {

}

var s = Square();
s.rotate(180);
```

Recursion

```javascript
fun fib(n) {
  if (n <= 1) return n;
  return fib(n - 2) + fib(n - 1);
}

for (var i = 0; i < 20; i = i + 1) {
  print fib(i);
}
```

Closures

```javascript
fun makeCounter() {
  var i = 0;
  fun count() {
    i = i + 1;
    print i;
  }

  return count;
}

var counter = makeCounter();
counter(); // "1".
counter(); // "2".
```
