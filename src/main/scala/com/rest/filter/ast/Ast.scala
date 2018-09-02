package com.rest.filter.ast

/** The syntax tree representation of a filter expression. */
sealed abstract class Ast

final case class Str(text: String) extends Ast
/** A numeric value. */
final case class Number(value: Int) extends Ast
/** Reference to an identifier.
  *  - `Sym(None, "foo", None)` represents a simple identifier, i.e. identifier ‘foo’.
  *  - `Sym(Some("foo"), "bar", None)` represents a single-level selection, i.e. identifier ‘foo.bar’.
  *  - `Sym(Some("foo"), "bar", Some("baz"))` represents a two-level selection, i.e. identifier ‘foo.bar.baz’.
  */
final case class Sym(kind: Option[String], key: String, nested: Option[String]) extends Ast

/** Conjunction of multiple conditions. */
final case class And(items: Seq[Condition]) extends Ast
final case class Or(items: Seq[Condition]) extends Ast
/** A filter condition, e.g. equality of an identifier and a constant. */
final case class Condition(op: String, sym: Sym, value: Ast) extends Ast
