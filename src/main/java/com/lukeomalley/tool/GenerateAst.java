package com.lukeomalley.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// Build the AST
// mvn clean insatll
// java -cp target/jlox-1.0-SNAPSHOT.jar com.lukeomalley.tool.GenerateAst /Users/luke/Documents/jlox/src/main/java/com/lukeomalley/lox/

public class GenerateAst {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }

    String outputDir = args[0];
    defineAst(outputDir, "Expr", Arrays.asList("Binary : Expr left, Token operator, Expr right",
        "Grouping : Expr expression", "Literal : Object value", "Urnary : Token operator, Expr right"));

  }

  public static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
    String path = outputDir + "/" + baseName + ".java";

    PrintWriter writer = new PrintWriter(path, "UTF-8");

    writer.println("package com.lukeomalley.lox;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println();
    writer.println("abstract class " + baseName + " {");
    writer.println();

    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();

      defineType(writer, baseName, className, fields);

    }

    writer.println("}");

    writer.close();
  }

  private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
    writer.println("  static class " + className + " extends " + baseName + " {");

    String[] fields = fieldList.split(", ");
    // Fields
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }

    writer.println();

    // Constructor
    writer.println("    " + className + "(" + fieldList + ") {");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");
    writer.println("  }");
    writer.println();
  }
}
