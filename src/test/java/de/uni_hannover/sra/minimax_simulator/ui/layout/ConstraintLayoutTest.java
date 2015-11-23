package de.uni_hannover.sra.minimax_simulator.ui.layout;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uni_hannover.sra.minimax_simulator.ui.layout.AbstractComponent;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintContainer;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintFactory;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.FixedShape;

public class ConstraintLayoutTest
{
	@Test
	public void test()
	{
/*		ConstraintContainer c = new ConstraintContainer();

		AbstractComponent c1 = new AbstractComponent();
		c1.setName("c1");
		c1.setShape(new FixedShape(100, 100));

		AbstractComponent c2 = new AbstractComponent();
		c2.setName("c2");
		c2.setShape(new FixedShape(100, 100));

		c.addComponent(c1, "c1");
		c.addComponent(c2, "c2");

		ConstraintFactory cf = c.createConstraintFactory();

		cf.absolute("c1", AttributeType.TOP, 0);
		cf.absolute("c1", AttributeType.LEFT, 0);
		cf.absolute("c2", AttributeType.TOP, 0);
		cf.relative("c2", AttributeType.LEFT, "c1", AttributeType.RIGHT, 10);

		c.updateSize();
		c.doLayout();

		assertEquals(210, c.getDimension().w);
		assertEquals(100, c.getDimension().h);
		assertEquals(0, c1.getBounds().x);
		assertEquals(0, c1.getBounds().y);
		assertEquals(110, c2.getBounds().x);
		assertEquals(0, c2.getBounds().y);	*/
	}
}