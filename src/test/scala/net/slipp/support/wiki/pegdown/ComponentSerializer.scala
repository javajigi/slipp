package net.slipp.support.wiki.pegdown

import org.pegdown.Printer
import org.pegdown.ast.Node
import org.pegdown.ast.Visitor
import org.pegdown.plugins.ToHtmlSerializerPlugin

class ComponentSerializer extends ToHtmlSerializerPlugin {
  def visit(node: Node, visitor: Visitor, printer: Printer): Boolean = {
    if (node.isInstanceOf[ComponentNode]) {
      val cNode: ComponentNode = node.asInstanceOf[ComponentNode]
      printer.print("\nThis gets dumped into the final HTML.\n")
      printer.print(cNode.getName)
      printer.println
      return true
    }
    return false
  }
}
