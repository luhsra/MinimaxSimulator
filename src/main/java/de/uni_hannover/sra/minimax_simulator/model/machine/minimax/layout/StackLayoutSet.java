package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class StackLayoutSet extends DefaultLayoutSet
{
	public StackLayoutSet(String anchor, List<String> targets, int spacing)
	{
		this(anchor, targets, Collections.nCopies(targets.size(),
			Integer.valueOf(spacing)), null);
	}

	public StackLayoutSet(String anchor, List<String> targets, int spacing,
			String groupName)
	{
		this(anchor, targets, Collections.nCopies(targets.size(),
			Integer.valueOf(spacing)), groupName);
	}

	public StackLayoutSet(String anchor, List<String> targets, List<Integer> spacings)
	{
		this(anchor, targets, spacings, null);
	}

	public StackLayoutSet(String anchor, List<String> targets, List<Integer> spacings,
			String groupName)
	{
		if (anchor == null)
			throw new NullPointerException("anchor is null");

		String[] targetArr = targets.toArray(new String[targets.size()]);
		Integer[] spacingArr = spacings.toArray(new Integer[spacings.size()]);

		if (targetArr.length != spacingArr.length)
			throw new IllegalArgumentException();

		if (targetArr.length > 0)
		{
			addLayout(targetArr[0], new StackLayout(anchor, spacingArr[0]));
			for (int i = 1; i < targetArr.length; i++)
				addLayout(targetArr[i], new StackLayout(targetArr[i - 1], spacingArr[i]));
		}

		// Add group layout
		if (groupName != null)
		{
			if (targetArr.length > 0)
				addLayout(groupName, new GroupLayout(new HashSet<String>(targets)));
			else
				addLayout(groupName, new StackLayout(anchor, 0));
		}
	}
}