package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

/**
 * Unfinished class.
 *
 * 
 *
 */
public class XmlConstraintLoader
{
	private enum ConstraintType
	{
		ALIGN,
		ALIGNV,
		ALIGNH,
		LEFT,
		RIGHT,
		ABOVE,
		BELOW,
	}

	private class Constraint
	{
		public int offset;
		public ConstraintType type;
	}

	private class ConstraintData
	{
		public String target;
		public String source;
		public List<Constraint> constraints;

		public ConstraintData()
		{
			constraints = new ArrayList<Constraint>(1);
		}

		public ConstraintData(ConstraintData data)
		{
			target = data.target;
			source = data.source;
			constraints = new ArrayList<Constraint>(data.constraints);
		}
	}

	private final ConstraintFactory	_f;

	public XmlConstraintLoader(ConstraintFactory cf)
	{
		_f = cf;
	}

	public void loadConstraints(Reader reader)
	{
		Document doc;
		try
		{
			doc = new SAXBuilder().build(reader);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Cannot load xml document", e);
		}

		Element root = doc.getRootElement();
		if ("machine".equals(root.getName()))
		{
			for (Element element : root.getChildren())
			{
				parseElement(element, new ConstraintData());
			}
		}
		else
			throw new RuntimeException("No <layout> root entry found in document");
	}

	private void parseElement(Element element, ConstraintData constraint)
	{
		String e = element.getName();

		// Target: allowed if no target is set
		if ("target".equals(e))
		{
			if (constraint.target != null)
				throw new IllegalStateException("Duplicate constraint target");

			String id = element.getAttributeValue("id");
			if (id == null)
				throw new IllegalArgumentException("Missing id attribute for target");

			ConstraintData subConstraint = new ConstraintData(constraint);
			subConstraint.target = id;

			apply(subConstraint);

			for (Element child : element.getChildren())
				parseElement(child, subConstraint);
		}
		// Source: allowed if no source is set
		else if ("source".equals(e))
		{
			if (constraint.source != null)
				throw new IllegalStateException("Duplicate constraint source");

			String id = element.getAttributeValue("id");
			if (id == null)
				throw new IllegalArgumentException("Missing id attribute for source");

			ConstraintData subConstraint = new ConstraintData(constraint);
			subConstraint.source = id;

			apply(subConstraint);

			for (Element child : element.getChildren())
				parseElement(child, subConstraint);
		}
		// Layout: allowed if neither target nor source is set
		else if ("layout".equals(e))
		{
			if (constraint.target != null)
				throw new IllegalStateException("Duplicate constraint target");

			if (constraint.source != null)
				throw new IllegalStateException("Duplicate constraint source");

			String target = element.getAttributeValue("target", constraint.target);
			String source = element.getAttributeValue("source", constraint.source);

			ConstraintData subConstraint = new ConstraintData(constraint);
			subConstraint.source = source;
			subConstraint.target = target;

			apply(subConstraint);

			for (Element child : element.getChildren())
				parseElement(child, subConstraint);
		}

		else if ("align".equals(e))
		{
			String target = element.getAttributeValue("target", constraint.target);
			String source = element.getAttributeValue("source", constraint.source);

			ConstraintData subConstraint = new ConstraintData(constraint);
			Constraint c = new Constraint();
			c.type = ConstraintType.ALIGN;
			subConstraint.constraints.add(c);
			subConstraint.source = source;
			subConstraint.target = target;

			apply(subConstraint);

			for (Element child : element.getChildren())
				parseElement(child, subConstraint);
		}
		else if ("alignH".equals(e))
		{
			String target = element.getAttributeValue("target", constraint.target);
			String source = element.getAttributeValue("source", constraint.source);

			ConstraintData subConstraint = new ConstraintData(constraint);
			Constraint c = new Constraint();
			c.type = ConstraintType.ALIGNH;
			subConstraint.constraints.add(c);
			subConstraint.source = source;
			subConstraint.target = target;

			apply(subConstraint);

			for (Element child : element.getChildren())
				parseElement(child, subConstraint);
		}
		else if ("alignV".equals(e))
		{
			String target = element.getAttributeValue("target", constraint.target);
			String source = element.getAttributeValue("source", constraint.source);

			ConstraintData subConstraint = new ConstraintData(constraint);
			Constraint c = new Constraint();
			c.type = ConstraintType.ALIGNV;
			subConstraint.constraints.add(c);
			subConstraint.source = source;
			subConstraint.target = target;

			apply(subConstraint);

			for (Element child : element.getChildren())
				parseElement(child, subConstraint);
		}
		else if ("absolute".equals(e))
		{
			String target = element.getAttributeValue("target", constraint.target);

			ConstraintData subConstraint = new ConstraintData(constraint);
			Constraint c = new Constraint();
			c.type = ConstraintType.LEFT;
			subConstraint.constraints.add(c);
			subConstraint.target = target;

			String leftStr = element.getAttributeValue("left");
			String topStr = element.getAttributeValue("top");

			if (leftStr != null)
			{
				int left = Integer.parseInt(leftStr);
				_f.absolute(target, AttributeType.LEFT, left);
			}
			if (topStr != null)
			{
				int top = Integer.parseInt(topStr);
				_f.absolute(target, AttributeType.TOP, top);
			}

		}
	}

	private void apply(ConstraintData constraint)
	{
		if (constraint.source == null)
			return;
		if (constraint.target == null)
			return;

		for (Constraint c : constraint.constraints)
		{
			switch (c.type)
			{
				case ABOVE:
					_f.above(constraint.target, constraint.source, c.offset);
					break;
				case ALIGN:
					_f.align(constraint.target, constraint.source);
					break;
				case ALIGNH:
					_f.alignHorizontally(constraint.target, constraint.source);
					break;
				case ALIGNV:
					_f.alignVertically(constraint.target, constraint.source);
					break;
				case BELOW:
					_f.below(constraint.target, constraint.source, c.offset);
					break;
				case LEFT:
					_f.left(constraint.target, constraint.source, c.offset);
					break;
				case RIGHT:
					_f.rightTo(constraint.target, constraint.source, c.offset);
					break;
				default:
					break;
			}
		}
	}
}