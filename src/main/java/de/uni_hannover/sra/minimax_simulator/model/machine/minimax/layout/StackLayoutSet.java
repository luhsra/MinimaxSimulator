package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Container for {@link StackLayout}s.
 *
 * @author Martin L&uuml;ck
 */
public class StackLayoutSet extends DefaultLayoutSet {

    /**
     * Constructs a new {@code StackLayoutSet} with the specified anchor, targets and spacing.
     *
     * @param anchor
     *          the anchor
     * @param targets
     *          following components
     * @param spacing
     *          spacing
     */
    public StackLayoutSet(String anchor, List<String> targets, int spacing) {
        this(anchor, targets, Collections.nCopies(targets.size(), spacing), null);
    }

    /**
     * Constructs a new {@code StackLayoutSet} with the specified anchor, targets, spacing and group name.
     *
     * @param anchor
     *          the anchor
     * @param targets
     *          following components
     * @param spacing
     *          spacing
     * @param groupName
     *          group name
     */
    public StackLayoutSet(String anchor, List<String> targets, int spacing, String groupName) {
        this(anchor, targets, Collections.nCopies(targets.size(), spacing), groupName);
    }

    /**
     * Constructs a new {@code StackLayoutSet} with the specified anchor, targets and spacings.
     *
     * @param anchor
     *          the anchor
     * @param targets
     *          following components
     * @param spacings
     *          spacings
     */
    public StackLayoutSet(String anchor, List<String> targets, List<Integer> spacings) {
        this(anchor, targets, spacings, null);
    }

    /**
     * Constructs a new {@code StackLayoutSet} with the specified anchor, targets, spacings and group name.
     *
     * @param anchor
     *          the anchor
     * @param targets
     *          following components
     * @param spacings
     *          spacings
     * @param groupName
     *          group name
     */
    public StackLayoutSet(String anchor, List<String> targets, List<Integer> spacings, String groupName) {
        if (anchor == null) {
            throw new NullPointerException("anchor is null");
        }

        String[] targetArr = targets.toArray(new String[targets.size()]);
        Integer[] spacingArr = spacings.toArray(new Integer[spacings.size()]);

        if (targetArr.length != spacingArr.length) {
            throw new IllegalArgumentException();
        }

        if (targetArr.length > 0) {
            addLayout(targetArr[0], new StackLayout(anchor, spacingArr[0]));
            for (int i = 1; i < targetArr.length; i++) {
                addLayout(targetArr[i], new StackLayout(targetArr[i - 1], spacingArr[i]));
            }
        }

        // Add group layout
        if (groupName != null) {
            if (targetArr.length > 0) {
                addLayout(groupName, new GroupLayout(new HashSet<>(targets)));
            }
            else {
                addLayout(groupName, new StackLayout(anchor, 0));
            }
        }
    }
}