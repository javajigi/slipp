package net.slipp.support.wiki.pegdown;

import java.util.Map;

import org.parboiled.BaseParser;
import org.parboiled.Rule;
import org.parboiled.support.StringBuilderVar;
import org.pegdown.Parser;
import org.pegdown.plugins.BlockPluginParser;

public class ComponentParser extends Parser implements BlockPluginParser {
	private final String TAG = "%%%";

	public ComponentParser() {
		super(ALL, 1000l, DefaultParseRunnerProvider);
	}

	@Override
	public Rule[] blockPluginRules() {
		return new Rule[] { component() };
	}

	public Rule component() {

		// stack ends up like this:
		//
		// body
		// params map
		// component name

		return NodeSequence(
	             open(),
	             body(),
	             close(),
	             push(new ComponentNode(
	                        (String)pop(2),
	                        (Map<String, String>)pop(1),
	                        (String)pop())));
	}

	/*
	 * parses out the component name and its parameters
	 * 
	 * example: %%% myComponent(foo=bar)
	 */
	public Rule open() {
		StringBuilderVar componentName = new StringBuilderVar();

		return Sequence(TAG, whitespace(),
				OneOrMore(TestNot('('), 
						BaseParser.ANY, 
						componentName.append(matchedChar())),
						push(componentName.getString()), 
						whitespace(), 
						'(', whitespace(), 
						params(), 
						whitespace(), 
						')',
				whitespace(), Newline());
	}

	/*
	 * parses out parameters from in between the parentheses they look like:
	 * foo=bar,baz=boo and optionally have whitespace around any tokens foo =
	 * bar , baz=boo
	 */
	public Rule params() {
		ParamVar params = new ParamVar();
		StringBuilderVar paramName = new StringBuilderVar();
		StringBuilderVar paramValue = new StringBuilderVar();

		return Sequence(
				ZeroOrMore(
						whitespace(),
						OneOrMore(TestNot('='), TestNot(' '), BaseParser.ANY, paramName.append(matchedChar())),
						whitespace(),
						'=',
						whitespace(),
						OneOrMore(TestNot(')'), TestNot(','), TestNot(' '), BaseParser.ANY,
								paramValue.append(matchedChar())), whitespace(), Optional(','), whitespace(),
						params.put(paramName.getString(), paramValue.getString()), paramName.clear(),
						paramValue.clear()), push(params.get()));
	}

	/*
	 * extracts the body of the component into a raw string
	 */
	public Rule body() {
		StringBuilderVar rawBody = new StringBuilderVar();

		return Sequence(OneOrMore(TestNot(TAG), BaseParser.ANY, rawBody.append(matchedChar())), push(rawBody
				.getString().trim()));
	}

	/*
	 * end of the component, ie "%%%"
	 */
	public String close() {
		return TAG;
	}

	public Rule whitespace() {
		return ZeroOrMore(AnyOf(" \t\f"));
	}
}
