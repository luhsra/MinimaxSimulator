package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.util.HashMap;
import java.util.Map;

public class DefaultLayoutManager implements LayoutManager
{
	private final ConstraintContainer	_container;
	private final Map<String, Layout>	_layouts;

	public DefaultLayoutManager(ConstraintContainer container)
	{
		_container = container;

		_layouts = new HashMap<String, Layout>();
	}

	@Override
	public Layout putLayout(String name, Layout layout)
	{
		if (layout == null)
			throw new NullPointerException("layout must not be null");

		Layout l = _layouts.put(name, layout);
		refreshConstraints(name, layout);
		return l;
	}

	@Override
	public Layout removeLayout(String name)
	{
		Layout layout = _layouts.remove(name);
		if (layout != null)
		{
			_container.clearConstraints(name);
		}
		return layout;
	}

	@Override
	public void updateLayout(String name)
	{
		Layout layout = _layouts.get(name);
		if (layout == null)
			throw new IllegalArgumentException("Component has no layout: " + name);

		refreshConstraints(name, layout);
	}

	private void refreshConstraints(String name, Layout layout)
	{
		_container.clearConstraints(name);
		for (AttributeType attribute : layout.getConstrainedAttributes())
		{
			Constraint constraint = layout.getConstraint(attribute);
			_container.setConstraint(name, attribute, constraint);
		}
	}

	@Override
	public void addConstraint(String owner, AttributeType attribute, Constraint con)
	{
		_container.addConstraint(owner, attribute, con);
	}

	@Override
	public void setConstraint(String owner, AttributeType attribute, Constraint con)
	{
		_container.setConstraint(owner, attribute, con);
	}

	@Override
	public void clearConstraints(String owner)
	{
		_container.clearConstraints(owner);
	}

	@Override
	public void removeConstraint(String owner, AttributeType attribute)
	{
		_container.removeConstraint(owner, attribute);
	}

	@Override
	public ConstraintFactory createConstraintFactory()
	{
		return _container.createConstraintFactory();
	}
}