package net.slipp.support.wiki.pegdown;

import java.util.List;
import java.util.Map;

import org.pegdown.ast.AbstractNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;

public class ComponentNode extends AbstractNode {
	private String name;
	private Map<String, String> params;
	private String body;

	public ComponentNode(String name, Map<String, String> params, String body) {
		this.name = name;
		this.params = params;
		this.body = body;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Node) this);
	}

	@Override
	public List<Node> getChildren() {
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public Map<String, String> getParams() {
		return params;
	}
	
	public String getBody() {
		return body;
	}
}
