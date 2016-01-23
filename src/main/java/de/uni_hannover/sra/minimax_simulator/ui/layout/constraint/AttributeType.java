package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The type of an {@link Attribute}.
 *
 * @author Martin L&uuml;ck
 */
public enum AttributeType {

    /** left bound */
    LEFT {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.HORIZONTAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // left can be derived if two of width, right and center are known
            if (owner.hasSet(WIDTH)) {
                int width = owner.get(WIDTH);
                if (owner.hasSet(RIGHT)) {
                    return owner.get(RIGHT) - width;
                }
                if (owner.hasSet(HORIZONTAL_CENTER)) {
                    return owner.get(HORIZONTAL_CENTER) - width / 2;
                }
            }
            else {
                if (owner.hasSet(HORIZONTAL_CENTER) && owner.hasSet(RIGHT)) {
                    // center - (right - center)
                    return 2 * owner.get(HORIZONTAL_CENTER) - owner.get(RIGHT);
                }

                // so, use internal component width
                int width = owner.getPreferredWidth();

                // right or center must be known
                if (owner.hasSet(HORIZONTAL_CENTER)) {
                    return owner.get(HORIZONTAL_CENTER) - width / 2;
                }

                // right or center must be known
                if (owner.hasSet(RIGHT)) {
                    return owner.get(RIGHT) - width;
                }
            }
            throw new IllegalStateException(owner.toString() + ": Underconstrained attribute: " + this);
        }
    },

    /** right bound */
    RIGHT {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.HORIZONTAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // right can be derived if two of width, left and center are known
            if (owner.hasSet(WIDTH)) {
                int width = owner.get(WIDTH);
                if (owner.hasSet(LEFT)) {
                    return owner.get(LEFT) + width;
                }
                if (owner.hasSet(HORIZONTAL_CENTER)) {
                    return owner.get(HORIZONTAL_CENTER) + width / 2;
                }
            }
            else {
                if (owner.hasSet(HORIZONTAL_CENTER) && owner.hasSet(LEFT)) {
                    // center + (center - left)
                    return 2 * owner.get(HORIZONTAL_CENTER) - owner.get(LEFT);
                }

                // so, use internal component width
                int width = owner.getPreferredWidth();

                // left or center must be known
                if (owner.hasSet(HORIZONTAL_CENTER)) {
                    return owner.get(HORIZONTAL_CENTER) + width / 2;
                }

                // left or center must be known
                if (owner.hasSet(LEFT)) {
                    return owner.get(LEFT) + width;
                }
            }
            throw new IllegalStateException(owner.toString() + ": Underconstrained attribute: " + this);
        }
    },

    /** center of the horizontal axis */
    HORIZONTAL_CENTER {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.HORIZONTAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // center can be derived if two of width, left and right are known
            if (owner.hasSet(WIDTH)) {
                int width = owner.get(WIDTH);
                if (owner.hasSet(LEFT)) {
                    return owner.get(LEFT) + width / 2;
                }
                if (owner.hasSet(RIGHT)) {
                    return owner.get(RIGHT) - width / 2;
                }
            }
            else {
                if (owner.hasSet(LEFT) && owner.hasSet(RIGHT)) {
                    return (owner.get(LEFT) + owner.get(RIGHT)) / 2;
                }

                // so, use internal component width
                int width = owner.getPreferredWidth();

                // left or right must be known
                if (owner.hasSet(LEFT)) {
                    return owner.get(LEFT) + width / 2;
                }

                // left or right must be known
                if (owner.hasSet(RIGHT)) {
                    return owner.get(RIGHT) - width / 2;
                }
            }
            throw new IllegalStateException(owner.toString() + ": Underconstrained attribute: " + this);
        }
    },

    /** upper bound */
    TOP {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.VERTICAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // top can be derived if two of height, bottom or center are known
            if (owner.hasSet(HEIGHT)) {
                int height = owner.get(HEIGHT);
                if (owner.hasSet(BOTTOM)) {
                    return owner.get(BOTTOM) - height;
                }
                if (owner.hasSet(VERTICAL_CENTER)) {
                    return owner.get(VERTICAL_CENTER) - height / 2;
                }
            }
            else {
                if (owner.hasSet(VERTICAL_CENTER) && owner.hasSet(BOTTOM)) {
                    // center - (bottom - center)
                    return 2 * owner.get(VERTICAL_CENTER) - owner.get(BOTTOM);
                }

                // so, use internal component height
                int height = owner.getPreferredHeight();

                // bottom or center must be known
                if (owner.hasSet(VERTICAL_CENTER)) {
                    return owner.get(VERTICAL_CENTER) - height / 2;
                }

                // bottom or center must be known
                if (owner.hasSet(BOTTOM)) {
                    return owner.get(BOTTOM) - height;
                }
            }
            throw new IllegalStateException(owner.toString() + ": Underconstrained attribute: " + this);
        }
    },

    /** lower bound */
    BOTTOM {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.VERTICAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // bottom can be derived if two of height, top or center are known
            if (owner.hasSet(HEIGHT)) {
                int height = owner.get(HEIGHT);
                if (owner.hasSet(TOP)) {
                    return owner.get(TOP) + height;
                }
                if (owner.hasSet(VERTICAL_CENTER)) {
                    return owner.get(VERTICAL_CENTER) + height / 2;
                }
            }
            else {
                if (owner.hasSet(VERTICAL_CENTER) && owner.hasSet(TOP)) {
                    // center + (center - top)
                    return 2 * owner.get(VERTICAL_CENTER) - owner.get(TOP);
                }

                // so, use internal component height
                int height = owner.getPreferredHeight();

                // top or center must be known
                if (owner.hasSet(VERTICAL_CENTER)) {
                    return owner.get(VERTICAL_CENTER) + height / 2;
                }

                // top or center must be known
                if (owner.hasSet(TOP)) {
                    return owner.get(TOP) + height;
                }
            }
            throw new IllegalStateException(owner.toString() + ": Underconstrained attribute: " + this);
        }
    },

    /** center of the vertical axis */
    VERTICAL_CENTER {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.VERTICAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // center can be derived if two of height, top and bottom are known
            if (owner.hasSet(HEIGHT)) {
                int height = owner.get(HEIGHT);
                if (owner.hasSet(TOP)) {
                    return owner.get(TOP) + height / 2;
                }
                if (owner.hasSet(BOTTOM)) {
                    return owner.get(BOTTOM) - height / 2;
                }
            }
            else {
                if (owner.hasSet(TOP) && owner.hasSet(BOTTOM)) {
                    return (owner.get(TOP) + owner.get(BOTTOM)) / 2;
                }

                // so, use internal component height
                int height = owner.getPreferredHeight();

                // top or bottom must be known
                if (owner.hasSet(TOP)) {
                    return owner.get(TOP) + height / 2;
                }

                // top or bottom must be known
                if (owner.hasSet(BOTTOM)) {
                    return owner.get(BOTTOM) - height / 2;
                }
            }
            throw new IllegalStateException(owner.toString() + ": Underconstrained attribute: " + this);
        }
    },

    /** width */
    WIDTH {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.HORIZONTAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // width can be derived if two of center, left and right are known
            if (owner.hasSet(HORIZONTAL_CENTER)) {
                int center = owner.get(HORIZONTAL_CENTER);
                if (owner.hasSet(LEFT)) {
                    return 2 * (center - owner.get(LEFT));
                }
                if (owner.hasSet(RIGHT)) {
                    return 2 * (owner.get(RIGHT) - center);
                }
            }
            else {
                // left and right must be known
                if (owner.hasSet(LEFT) && owner.hasSet(RIGHT)) {
                    return owner.get(RIGHT) - owner.get(LEFT);
                }
            }
            return owner.getPreferredWidth();
        }
    },

    /** height */
    HEIGHT {
        @Override
        public AttributeAxis getAxis() {
            return AttributeAxis.VERTICAL;
        }

        @Override
        public int deriveValue(AttributeOwner owner) {
            // height can be derived if two of center, top and bottom are known
            if (owner.hasSet(VERTICAL_CENTER)) {
                int center = owner.get(VERTICAL_CENTER);
                if (owner.hasSet(TOP)) {
                    return 2 * (center - owner.get(TOP));
                }
                if (owner.hasSet(BOTTOM)) {
                    return 2 * (owner.get(BOTTOM) - center);
                }
            }
            else {
                // top and bottom must be known
                if (owner.hasSet(TOP) && owner.hasSet(BOTTOM)) {
                    return owner.get(BOTTOM) - owner.get(TOP);
                }
            }
            return owner.getPreferredHeight();
        }
    };

    private static final EnumMap<AttributeAxis, Set<AttributeType>> AXIS_ATTRS;

    /**
     * Gets the {@link AttributeAxis} of the {@code AttributeType}.
     *
     * @return
     *          the {@code AttributeAxis}
     */
    public abstract AttributeAxis getAxis();

    /**
     * Derives the value of the {@code AttributeType} using the specified {@link AttributeOwner}.
     *
     * @param owner
     *          the owner of the attribute
     * @return
     *          the derived value
     * @throws IllegalStateException
     *          thrown if the attribute is underconstrained
     */
    public abstract int deriveValue(AttributeOwner owner);

    static {
        AXIS_ATTRS = new EnumMap<>(AttributeAxis.class);
        for (AttributeAxis axis : AttributeAxis.values()) {
            AXIS_ATTRS.put(axis, EnumSet.noneOf(AttributeType.class));
        }

        for (AttributeType attr : values()) {
            AXIS_ATTRS.get(attr.getAxis()).add(attr);
        }

        for (Entry<AttributeAxis, Set<AttributeType>> entry : AXIS_ATTRS.entrySet()) {
            entry.setValue(Collections.unmodifiableSet(entry.getValue()));
        }
    }

    /**
     * Gets the {@code AttributeType}s suitable for the specified {@link AttributeAxis}.
     *
     * @param axis
     *          the {@code AttributeAxis}
     * @return
     *          a set of the suitable {@code AttributeType}s
     */
    public static Set<AttributeType> getAxisTypes(AttributeAxis axis) {
        return AXIS_ATTRS.get(axis);
    }
}