package de.uni_hannover.sra.minimax_simulator.model.machine.layout;

import org.junit.Test;

import de.uni_hannover.sra.minimax_simulator.layout.AbstractComponent;
import de.uni_hannover.sra.minimax_simulator.layout.Dimension;
import de.uni_hannover.sra.minimax_simulator.model.layout.container.GridContainer;
import static org.junit.Assert.*;

public class GridLayoutTest
{
	@Test
	public void test()
	{
		GridContainer c = new GridContainer(3, 2);

		// first row
		AbstractComponent c11 = new AbstractComponent();
		c11.setDimension(new Dimension(100, 10));
		AbstractComponent c21 = new AbstractComponent();
		c21.setDimension(new Dimension(50, 10));
		AbstractComponent c31 = new AbstractComponent();
		c31.setDimension(new Dimension(10, 120));

		// second row
		AbstractComponent c12 = new AbstractComponent();
		c12.setDimension(new Dimension(50, 20));
		AbstractComponent c22 = new AbstractComponent();
		c22.setDimension(new Dimension(60, 10));
		AbstractComponent c32 = new AbstractComponent();
		c32.setDimension(new Dimension(20, 50));

		c.addComponent(c11, null);
		c.addComponent(c21, null);
		c.addComponent(c31, null);
		c.addComponent(c12, null);
		c.addComponent(c22, null);
		c.addComponent(c32, null);

		c.updateSize();

		Dimension dim = c.getDimension();

		assertEquals(180, dim.w);
		assertEquals(170, dim.h);
	}
}
