package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Constraint;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class DefaultLayout implements Layout
{
	private final EnumMap<AttributeType, Constraint>	_constraints;
	private final Map<AttributeType, Constraint>		_constraintsView;

	public DefaultLayout()
	{
		_constraints = new EnumMap<AttributeType, Constraint>(AttributeType.class);
		_constraintsView = Collections.unmodifiableMap(_constraints);
	}

	public DefaultLayout(Map<AttributeType, ? extends Constraint> constraints)
	{
		_constraints = new EnumMap<AttributeType, Constraint>(constraints);
		_constraintsView = Collections.unmodifiableMap(_constraints);
	}

	public void setConstraint(AttributeType attribute, Constraint constraint)
	{
		_constraints.put(attribute, constraint);
	}

	@Override
	public Constraint getConstraint(AttributeType attribute)
	{
		return _constraintsView.get(attribute);
	}

	@Override
	public Set<AttributeType> getConstrainedAttributes()
	{
		return _constraintsView.keySet();
	}
}