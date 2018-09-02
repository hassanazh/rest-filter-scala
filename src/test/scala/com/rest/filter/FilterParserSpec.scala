package com.rest.filter

import com.rest.filter.ast._


final class FilterParserSpec extends FlatSpec with Matchers {
  
  "parse" should "accept a string equality expression with 2-level select" in {
    val ast = FilterParser.parse("""(EQ foo.bar.baz "test")""")
    ast should equal (Right(Condition(EQ, Sym(Some("foo"), "bar", Some("baz")), Str("test"))))
  }

  "parse" should "accept a string equality expression with 1-level select" in {
    val ast = FilterParser.parse("""(EQ foo.bar "test")""")
    ast should equal (Right(Condition(EQ, Sym(Some("foo"), "bar", None), Str("test"))))
  }

  "parse" should "accept a string equality expression with simple identifier" in {
    val ast = FilterParser.parse("""(EQ foo "test")""")
    ast should equal (Right(Condition(EQ, Sym(None, "foo", None), Str("test"))))
  }

  "parse" should "accept an integer equality expression with 2-level select" in {
    val ast = FilterParser.parse("""(EQ foo.bar.baz 42)""")
    ast should equal (Right(Condition(EQ, Sym(Some("foo"), "bar", Some("baz")), Number(42))))
  }

  "parse" should "accept an integer equality expression with 1-level select" in {
    val ast = FilterParser.parse("""(EQ foo.bar 42)""")
    ast should equal (Right(Condition(EQ, Sym(Some("foo"), "bar", None), Number(42))))
  }

  "parse" should "accept an integer equality expression with simple identifier" in {
    val ast = FilterParser.parse("""(EQ foo 42)""")
    ast should equal (Right(Condition(EQ, Sym(None, "foo", None), Number(42))))
  }

  "parse" should "accept an AND condition" in {
    val ast = FilterParser.parse("""(AND (EQ foo 42) (EQ bar "test"))""")
    ast should equal (Right(And(Seq(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(EQ, Sym(None, "bar", None), Str("test"))))))
  }

  "parse" should "accept less than as operator" in {
    val ast = FilterParser.parse("""(LT foo.bar 10)""")
    ast should equal (Right(Condition(LT, Sym(Some("foo"), "bar", None), Number(10))))
  }

  "parse" should "accept greater than as operator" in {
    val ast = FilterParser.parse("""(GT foo.bar 10)""")
    ast should equal (Right(Condition(GT, Sym(Some("foo"), "bar", None), Number(10))))
  }

  "parse" should "accept EQ, LT and GT in parseFilters" in {
    val ast = FilterParser.parseFilters("""(AND (EQ foo 42) (LT bar 15) (GT baz 10))""")
    ast should equal( Set(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(LT, Sym(None, "bar", None), Number(15)), Condition(GT, Sym(None, "baz", None), Number(10))) )
  }

  "parse" should "accept an OR condition" in {
    val ast = FilterParser.parse("""(OR (EQ foo 42) (EQ bar "test"))""")
    ast should equal (Right(Or(Seq(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(EQ, Sym(None, "bar", None), Str("test"))))))
  }

  "parse" should "handle nested AND/OR" in {
    val ast = FilterParser.parse("""(AND (OR (EQ foo 42) (EQ bar "test")) (AND (EQ foo 42) (EQ baz 10)))""")
    val orNest = Condition(OR, Sym(None, "", None), Or(Seq(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(EQ, Sym(None, "bar", None), Str("test")))))
    val andNest = Condition(AND, Sym(None, "", None), And(Seq(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(EQ, Sym(None, "baz", None), Number(10)))))
    ast should equal(Right(And(Seq(orNest, andNest))))
  }

  "parse" should "handle recursive nested AND/OR" in {
    val ast = FilterParser.parse("""(AND (OR (AND (EQ foo 42) (EQ baz 5)) (EQ bar "test")) (AND (EQ foo 42) (EQ baz 10)))""")
    val andNestDeep = Condition(AND, Sym(None, "", None), And(Seq(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(EQ, Sym(None, "baz", None), Number(5)))))
    val orNest = Condition(OR, Sym(None, "", None), Or(Seq(andNestDeep, Condition(EQ, Sym(None, "bar", None), Str("test")))))
    val andNest = Condition(AND, Sym(None, "", None), And(Seq(Condition(EQ, Sym(None, "foo", None), Number(42)), Condition(EQ, Sym(None, "baz", None), Number(10)))))
    ast should equal(Right(And(Seq(orNest, andNest))))
  }

  "parse" should "accept an WILDCARD condition match any character sequence" in {
    val ast = FilterParser.parse("""(WILDCARD foo "*BAR*")""")
    ast should equal (Right(Condition(WILDCARD, Sym(None, "foo", None), Str("*BAR*"))))
  }

  "parse" should "accept an WILDCARD condition match single character" in {
    val ast = FilterParser.parse("""(WILDCARD foo "B?R")""")
    ast should equal (Right(Condition(WILDCARD, Sym(None, "foo", None), Str("B?R"))))
  }

  "parse" should "accept an WILDCARD condition" in {
    val ast = FilterParser.parse("""(AND (WILDCARD char "B?R") (WILDCARD star "f*o"))""")
    ast should equal (
      Right(And(Seq(Condition(WILDCARD, Sym(None, "char", None), Str("B?R")),Condition(WILDCARD, Sym(None, "star", None), Str("f*o"))))))
  }
}
