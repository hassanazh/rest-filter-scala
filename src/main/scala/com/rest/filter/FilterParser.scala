package com.rest.filter

import org.parboiled2._
import org.parboiled2.ParserInput
import scala.util._

import com.rest.filter.ast._


/** Parses the 'filter' argument for Query API services. */
object FilterParser {
  def parse(text: String): Either[String, Ast] = {
    val parser = new FilterParser(text)
    val result = parser.StartRule.run()

    result match {
      case Success(x) => Right(x)

      case Failure(e: ParseError) =>
        val errorMsg = parser.formatError(e, new ErrorFormatter(showTraces = true))
        Left(errorMsg)

      case Failure(e) => Left(s"Failed to parse: ${e.getMessage}")
    }
  }

  /**
    * Since the filtering only supports AND at this point we can
    * safely compress it like this.
    */
  private[this] def flattenFilters(ast: Ast): Set[Condition] = {
    ast match {
      case And(items) => items.flatMap(flattenFilters).toSet
      case Or(items) => items.flatMap(flattenFilters).toSet
      case Condition(EQ, key, value) => Set(Condition(EQ, key, value))
      case Condition(LT, key, value) => Set(Condition(LT, key, value))
      case Condition(GT, key, value) => Set(Condition(GT, key, value))
      case Condition(WILDCARD, key, value) => Set(Condition(WILDCARD, key, value))
      case _ => Set.empty
    }
  }

  def parseFilters(filter: String): Set[Condition] = {
    FilterParser.parse(filter) match {
      case Right(res) => flattenFilters(res)
      case Left(err) => Set.empty
    }
  }
}

private final class FilterParser(val input: ParserInput) extends Parser {
  def StartRule = rule { (AndRule | OrRule | ConditionRule) ~ EOI }

  def AndRule: Rule1[And] =
    rule { "(AND" ~ oneOrMore(Space ~ (ConditionRule | NestedRule)) ~ ")" ~> And }

  def OrRule: Rule1[Or] =
    rule { "(OR" ~ oneOrMore(Space ~ (ConditionRule | NestedRule)) ~ ")" ~> Or }

  def ConditionRule: Rule1[Condition] =
    rule { "(" ~ capture(EQ | LT | GT | WILDCARD) ~ Space ~ (SymbolRule3 | SymbolRule2 | SymbolRule1) ~ Space ~ (StringRule | IntegerRule) ~ ")" ~> Condition }

  def NestedRule: Rule1[Condition] =
    rule { "(" ~ capture(AND | OR) ~ oneOrMore(Space ~ (ConditionRule | NestedRule)) ~ ")" ~> {(op: String, items: Seq[Condition]) =>
      Condition(op, Sym(None, "", None),
        op match {
          case AND => And(items)
          case OR => Or(items)
        }
      )
    }}

  def Space = rule { oneOrMore(' ') }

  def StringRule: Rule1[Str] =
    rule { '"' ~ ( capture(oneOrMore(CharPredicate.AlphaNum | '-' | ':' | '+' | '.' | ' ' | '_' | '|' | '&' | '*' | '?')) ~> Str ) ~ '"' }

  def IntegerRule: Rule1[Number] =
    rule { capture(oneOrMore(CharPredicate.Digit)) ~> ((str: String) => Number(str.toInt)) }

  def ArgumentRule: Rule1[String] =
    rule { capture(oneOrMore(CharPredicate.AlphaNum | '-')) }

  /**
    * Rule used to search for attribute in current resource
    * [<AttributeName>]
    */
  def SymbolRule1: Rule1[Sym] =
    rule {
      ArgumentRule ~> ((key: String) => Sym(None, key, None))
    }

  /**
    * Rule used to search for attribute in current resource
    * Either:
    *  - Nested attribute in current resource [<GroupName>.<AttributeName>]
    *  - Attribute in related resource [<RelationshipName>.<AttributeName>]
    */
  def SymbolRule2: Rule1[Sym] =
    rule {
      ArgumentRule ~ '.' ~ ArgumentRule ~>
        ((kind: String, key: String) => Sym(Option(kind), key, None))
    }

  /**
    * Rule used to search for nested attribute in related resource
    * [<RelationshipName>.<GroupName>.<AttributeName>]
    */
  def SymbolRule3: Rule1[Sym] =
    rule {
      ArgumentRule ~ '.' ~ ArgumentRule ~ '.' ~ ArgumentRule ~>
        ((kind: String, key: String, nested: String) => Sym(Option(kind), key, Option(nested)))
    }
}

