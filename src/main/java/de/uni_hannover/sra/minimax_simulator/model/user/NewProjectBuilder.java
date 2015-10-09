package de.uni_hannover.sra.minimax_simulator.model.user;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.BaseControlPort;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfigurationBuilder;
import de.uni_hannover.sra.minimax_simulator.model.signal.DefaultSignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;
import de.uni_hannover.sra.minimax_simulator.resources.ResourceBundleLoader;

public class NewProjectBuilder implements ProjectBuilder {
	@Override
	public Project buildProject() {
		ResourceBundleLoader res = Main.getResourceLoader();

		MachineConfigurationBuilder mb = new MachineConfigurationBuilder();
		MachineConfiguration conf = mb.loadDefaultValues(
			res.getTextResource("register")).build();

		SignalTable table = new DefaultSignalTable();
		createDefaultProgram(table);
		Project project = new Project(conf, new ProjectConfiguration(), table);
		return project;
	}

	private void createDefaultProgram(SignalTable table) {
		SignalRow[] rows = new SignalRow[4];
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new SignalRow();
			table.addSignalRow(rows[i]);
		}

		// ACCU <- ACCU + 1
		rows[0].setSignalValue("ACCU.W", 1);
		rows[0].setSignalValue(BaseControlPort.ALU_SELECT_A.name(), 1);
		rows[0].setSignalValue(BaseControlPort.ALU_SELECT_B.name(), 3);
		rows[0].setSignalValue(BaseControlPort.ALU_CTRL.name(), 0);
		rows[0].setLabel("start");

		for (int i = 1; i < 4; i++) {
			// ACCU/PC <- ACCU + ACCU
			rows[i].setSignalValue("ACCU.W", 1);
			rows[i].setSignalValue("PC.W", 1);
			rows[i].setSignalValue(BaseControlPort.ALU_SELECT_A.name(), 2);
			rows[i].setSignalValue(BaseControlPort.ALU_SELECT_B.name(), 3);
			rows[i].setSignalValue(BaseControlPort.ALU_CTRL.name(), 0);
		}

		// PC == 0?
		SignalRow row = new SignalRow();
		row.setSignalValue(BaseControlPort.ALU_SELECT_A.name(), 0);
		row.setSignalValue(BaseControlPort.ALU_SELECT_B.name(), 2);
		row.setSignalValue(BaseControlPort.ALU_CTRL.name(), 0);
		row.setJump(new ConditionalJump(5, 7));
		table.addSignalRow(row);

		// MDR/ACCU <- ACCU + ACCU
		row = new SignalRow();
		row.setSignalValue("MDR.W", 1);
		row.setSignalValue("ACCU.W", 1);
		row.setSignalValue(BaseControlPort.ALU_SELECT_A.name(), 2);
		row.setSignalValue(BaseControlPort.ALU_SELECT_B.name(), 3);
		row.setSignalValue(BaseControlPort.ALU_CTRL.name(), 0);
		table.addSignalRow(row);

		// PC <- PC - 1
		row = new SignalRow();
		row.setSignalValue("PC.W", 1);
		row.setSignalValue(BaseControlPort.ALU_SELECT_A.name(), 1);
		row.setSignalValue(BaseControlPort.ALU_SELECT_B.name(), 2);
		row.setSignalValue(BaseControlPort.ALU_CTRL.name(), 1);
		row.setJump(new UnconditionalJump(4));
		table.addSignalRow(row);

		// M[MAR] <- MDR
		row = new SignalRow();
		row.setSignalValue(BaseControlPort.MEM_CS.name(), 1);
		row.setSignalValue(BaseControlPort.MEM_RW.name(), 0);
		row.setLabel("store");
		row.setBreakpoint(true);
		table.addSignalRow(row);
	}
}