package net.slipp.support.wiki.pegdown

import java.util.{List, Map}

import org.pegdown.ast.{AbstractNode, Node, Visitor}

class ComponentNode(name: String, params: Map[String, String], body: String) extends AbstractNode {
  def accept(visitor: Visitor) {
    visitor.visit(this.asInstanceOf[Node])
  }

  def getChildren: List[Node] = {
    return null
  }

  def getName: String = {
    return name
  }

  def getParams: Map[String, String] = {
    return params
  }

  def getBody: String = {
    return body
  }
}
