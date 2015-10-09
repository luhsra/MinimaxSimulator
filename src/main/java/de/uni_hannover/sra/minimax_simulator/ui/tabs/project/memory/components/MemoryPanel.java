package de.uni_hannover.sra.minimax_simulator.ui.tabs.project.memory.components;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.user.Project;
import de.uni_hannover.sra.minimax_simulator.ui.common.Disposable;

@Deprecated
public class MemoryPanel extends JPanel implements Disposable
{
	private MachineMemory			_memory;
	private MemoryView				_memoryView;

	private final MemoryImportPanel	_importPanel;
	private final MemoryExportPanel _exportPanel;
	private final MemoryClearPanel	_clearPanel;

	private MigLayout createLayout()
	{
		String cols = "[fill]unrel[fill]";
		String rows = "[min:p:p, top]rel[min!]rel[min!]";

		return new MigLayout("left, top", cols, rows);
	}

	public MemoryPanel(Project project)
	{
		_memory = project.getMachine().getMemory();
		_memoryView = new MemoryView(_memory);

		setLayout(createLayout());

		_importPanel = new MemoryImportPanel(_memory);
		_exportPanel = new MemoryExportPanel(_memory);
		_clearPanel = new MemoryClearPanel(_memory);

		_memoryView.addMemorySelectionListener(_importPanel);
		_memoryView.addMemorySelectionListener(_exportPanel);

		add(_memoryView, "cell 0 0 1 3");
		add(_importPanel, "cell 1 0");
		add(_exportPanel, "cell 1 1");
		add(_clearPanel, "cell 1 2");
	}

	@Override
	public void dispose()
	{
		// also clears listeners
		_memoryView.dispose();
	}
}