package net.slipp.support.wiki.pegdown;

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class ComponentSerializer implements ToHtmlSerializerPlugin {

	@Override
	public boolean visit(Node node, Visitor visitor, Printer printer) {
		if (node instanceof ComponentNode) {
			ComponentNode cNode = (ComponentNode) node;

			printer.print("\nThis gets dumped into the final HTML.\n");
			printer.print(cNode.getName());
			printer.println();

			return true;
		}
		return false;
	}
}
