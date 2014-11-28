package de.uni_hannover.sra.minimax_simulator.layout.constraint;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;

public enum AttributeType
{
	LEFT
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.HORIZONTAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Left can be derived if two of width, right and center are known.
			if (owner.hasSet(WIDTH))
			{
				int width = owner.get(WIDTH);
				if (owner.hasSet(RIGHT))
				{
					return owner.get(RIGHT) - width;
				}
				if (owner.hasSet(HORIZONTAL_CENTER))
				{
					return owner.get(HORIZONTAL_CENTER) - width / 2;
				}	
			}
			else
			{
				if (owner.hasSet(HORIZONTAL_CENTER) && owner.hasSet(RIGHT))
				{
					// center - (right - center)
					return 2 * owner.get(HORIZONTAL_CENTER) - owner.get(RIGHT);
				}

				// So, use internal component width.
				int width = owner.getPreferredWidth();

				// right or center must be known
				if (owner.hasSet(HORIZONTAL_CENTER))
				{
					return owner.get(HORIZONTAL_CENTER) - width / 2;
				}

				// right or center must be known
				if (owner.hasSet(RIGHT))
				{
					return owner.get(RIGHT) - width;
				}
			}
			throw new IllegalStateException(owner.toString()
				+ ": Underconstrained attribute: " + this);
		}
	},
	RIGHT
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.HORIZONTAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Right can be derived if two of width, left and center are known.
			if (owner.hasSet(WIDTH))
			{
				int width = owner.get(WIDTH);
				if (owner.hasSet(LEFT))
				{
					return owner.get(LEFT) + width;
				}
				if (owner.hasSet(HORIZONTAL_CENTER))
				{
					return owner.get(HORIZONTAL_CENTER) + width / 2;
				}	
			}
			else
			{
				if (owner.hasSet(HORIZONTAL_CENTER) && owner.hasSet(LEFT))
				{
					// center + (center - left)
					return 2 * owner.get(HORIZONTAL_CENTER) - owner.get(LEFT);
				}

				// So, use internal component width.
				int width = owner.getPreferredWidth();

				// left or center must be known
				if (owner.hasSet(HORIZONTAL_CENTER))
				{
					return owner.get(HORIZONTAL_CENTER) + width / 2;
				}

				// left or center must be known
				if (owner.hasSet(LEFT))
				{
					return owner.get(LEFT) + width;
				}
			}
			throw new IllegalStateException(owner.toString()
				+ ": Underconstrained attribute: " + this);
		}
	},
	HORIZONTAL_CENTER
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.HORIZONTAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Center can be derived if two of width, left and right are known.
			if (owner.hasSet(WIDTH))
			{
				int width = owner.get(WIDTH);
				if (owner.hasSet(LEFT))
				{
					return owner.get(LEFT) + width / 2;
				}
				if (owner.hasSet(RIGHT))
				{
					return owner.get(RIGHT) - width / 2;
				}	
			}
			else
			{
				if (owner.hasSet(LEFT) && owner.hasSet(RIGHT))
				{
					return (owner.get(LEFT) + owner.get(RIGHT)) / 2;
				}

				// So, use internal component width.
				int width = owner.getPreferredWidth();

				// left or right must be known
				if (owner.hasSet(LEFT))
				{
					return owner.get(LEFT) + width / 2;
				}

				// left or right must be known
				if (owner.hasSet(RIGHT))
				{
					return owner.get(RIGHT) - width / 2;
				}
			}
			throw new IllegalStateException(owner.toString()
				+ ": Underconstrained attribute: " + this);
		}
	},
	TOP
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.VERTICAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Top can be derived if two of height, bottom or center are known.
			if (owner.hasSet(HEIGHT))
			{
				int height = owner.get(HEIGHT);
				if (owner.hasSet(BOTTOM))
				{
					return owner.get(BOTTOM) - height;
				}
				if (owner.hasSet(VERTICAL_CENTER))
				{
					return owner.get(VERTICAL_CENTER) - height / 2;
				}	
			}
			else
			{
				if (owner.hasSet(VERTICAL_CENTER) && owner.hasSet(BOTTOM))
				{
					// center - (bottom - center)
					return 2 * owner.get(VERTICAL_CENTER) - owner.get(BOTTOM);
				}

				// So, use internal component height.
				int height = owner.getPreferredHeight();

				// bottom or center must be known
				if (owner.hasSet(VERTICAL_CENTER))
				{
					return owner.get(VERTICAL_CENTER) - height / 2;
				}

				// bottom or center must be known
				if (owner.hasSet(BOTTOM))
				{
					return owner.get(BOTTOM) - height;
				}
			}
			throw new IllegalStateException(owner.toString()
				+ ": Underconstrained attribute: " + this);
		}
	},
	BOTTOM
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.VERTICAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Bottom can be derived if two of height, top or center are known.
			if (owner.hasSet(HEIGHT))
			{
				int height = owner.get(HEIGHT);
				if (owner.hasSet(TOP))
				{
					return owner.get(TOP) + height;
				}
				if (owner.hasSet(VERTICAL_CENTER))
				{
					return owner.get(VERTICAL_CENTER) + height / 2;
				}	
			}
			else
			{
				if (owner.hasSet(VERTICAL_CENTER) && owner.hasSet(TOP))
				{
					// center + (center - top)
					return 2 * owner.get(VERTICAL_CENTER) - owner.get(TOP);
				}

				// So, use internal component height.
				int height = owner.getPreferredHeight();

				// top or center must be known
				if (owner.hasSet(VERTICAL_CENTER))
				{
					return owner.get(VERTICAL_CENTER) + height / 2;
				}

				// top or center must be known
				if (owner.hasSet(TOP))
				{
					return owner.get(TOP) + height;
				}
			}
			throw new IllegalStateException(owner.toString()
				+ ": Underconstrained attribute: " + this);
		}
	},
	VERTICAL_CENTER
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.VERTICAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Center can be derived if two of height, top and bottom are known.
			if (owner.hasSet(HEIGHT))
			{
				int height = owner.get(HEIGHT);
				if (owner.hasSet(TOP))
				{
					return owner.get(TOP) + height / 2;
				}
				if (owner.hasSet(BOTTOM))
				{
					return owner.get(BOTTOM) - height / 2;
				}
			}
			else
			{
				if (owner.hasSet(TOP) && owner.hasSet(BOTTOM))
				{
					return (owner.get(TOP) + owner.get(BOTTOM)) / 2;
				}

				// So, use internal component height.
				int height = owner.getPreferredHeight();

				// top or bottom must be known
				if (owner.hasSet(TOP))
				{
					return owner.get(TOP) + height / 2;
				}

				// top or bottom must be known
				if (owner.hasSet(BOTTOM))
				{
					return owner.get(BOTTOM) - height / 2;
				}
			}
			throw new IllegalStateException(owner.toString()
				+ ": Underconstrained attribute: " + this);
		}
	},
	WIDTH
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.HORIZONTAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Width can be derived if two of center, left and right are known.
			if (owner.hasSet(HORIZONTAL_CENTER))
			{
				int center = owner.get(HORIZONTAL_CENTER);
				if (owner.hasSet(LEFT))
				{
					return 2 * (center - owner.get(LEFT));
				}
				if (owner.hasSet(RIGHT))
				{
					return 2 * (owner.get(RIGHT) - center);
				}	
			}
			else
			{
				// ok, left and right must be known
				if (owner.hasSet(LEFT) && owner.hasSet(RIGHT))
				{
					return owner.get(RIGHT) - owner.get(LEFT);
				}
			}
			return owner.getPreferredWidth();
		}
	},
	HEIGHT
	{
		@Override
		public AttributeAxis getAxis()
		{
			return AttributeAxis.VERTICAL;
		}

		@Override
		public int deriveValue(AttributeOwner owner)
		{
			// Height can be derived if two of center, top and bottom are known.
			if (owner.hasSet(VERTICAL_CENTER))
			{
				int center = owner.get(VERTICAL_CENTER);
				if (owner.hasSet(TOP))
				{
					return 2 * (center - owner.get(TOP));
				}
				if (owner.hasSet(BOTTOM))
				{
					return 2 * (owner.get(BOTTOM) - center);
				}	
			}
			else
			{
				// ok, top and bottom must be known
				if (owner.hasSet(TOP) && owner.hasSet(BOTTOM))
				{
					return owner.get(BOTTOM) - owner.get(TOP);
				}
			}
			return owner.getPreferredHeight();
		}
	};

	public abstract AttributeAxis getAxis();

	public abstract int deriveValue(AttributeOwner owner);

	private final static EnumMap<AttributeAxis, Set<AttributeType>> _axisAttrs;

	static
	{
		_axisAttrs = new EnumMap<AttributeAxis, Set<AttributeType>>(AttributeAxis.class);
		for (AttributeAxis axis : AttributeAxis.values())
			_axisAttrs.put(axis, EnumSet.noneOf(AttributeType.class));

		for (AttributeType attr : values())
			_axisAttrs.get(attr.getAxis()).add(attr);

		for (Entry<AttributeAxis, Set<AttributeType>> entry : _axisAttrs.entrySet())
			entry.setValue(Collections.unmodifiableSet(entry.getValue()));
	}

	public static Set<AttributeType> getAxisTypes(AttributeAxis axis)
	{
		return _axisAttrs.get(axis);
	}
}