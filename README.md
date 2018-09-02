# Parser for filter expressions in JSON-API interfaces

Since JSON API does not define the format of filtering, this API opted to with a LISP-style syntax
(Operator Operand Operand) supporting a few operations that are serialized into standard query string format.


# Usage

## Adding to your project

Add the library to `libraryDependencies` in your `build.sbt`:


## Code examples

```scala
import com.rest.filter.FilterParser

FilterParser.parse("""(and (EQ name "John") (EQ last-name "Doe"))""") match {
  case Left(error) =>
    Console.err.println(s"Failed to parse: $error")

  case Right(ast) =>
    evaluate(ast)
}
```