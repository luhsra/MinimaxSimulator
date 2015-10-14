package de.uni_hannover.sra.minimax_simulator.ui.editor;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;

import org.junit.Test;

import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfigurationBuilder;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;
//import de.uni_hannover.sra.minimax_simulator.ui.tabs.machine.overview.model.AluTableModel;

public class AluTableModelTest
{
	@Test
	public void testTableModel()
	{
/*		ResourceBundleLoader resourceLoader = Main.getResourceLoader();
		MachineConfigurationBuilder mb = new MachineConfigurationBuilder();
		MachineConfiguration conf = mb.loadDefaultValues(
			resourceLoader.getTextResource("register")).build();

		AluTableModel model = new AluTableModel(conf, resourceLoader.getTextResource("editor"));
		conf.addMachineConfigListener(model);

		conf.addAluOperation(AluOperation.A_SUB_B);
		conf.addAluOperation(AluOperation.A_MUL_B);
		conf.addAluOperation(AluOperation.A_ROTL_B);

		Object addCode = model.getValueAt(0, 0);
		Object subCode = model.getValueAt(1, 0);
		Object mulCode = model.getValueAt(2, 0);
		Object rotlCode = model.getValueAt(3, 0);	*/

//		assertEquals("00", addCode);
//		assertEquals("01", subCode);
//		assertEquals("10", mulCode);
//		assertEquals("11", rotlCode);
	}
}