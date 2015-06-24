package de.uni_hannover.sra.minimax_simulator.gui.util;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.common.dialogs.FxDialog;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

public class MemoryExportWorker implements Runnable
{
	private final static Logger	_log	= Logger.getLogger(MemoryExportWorker.class.getName());

	private final MachineMemory	_memory;

	private final File			_file;
	private final TextResource	_res;

	private final int			_fromAddress;
	private final int			_toAddress;

	public MemoryExportWorker(MachineMemory memory, int fromAddress, int toAddress,
							  File file, TextResource res)
	{
		_memory = memory;

		_file = file;
		_res = res;

		_fromAddress = fromAddress;
		_toAddress = toAddress;

		checkArgument(fromAddress >= memory.getMinAddress());
		checkArgument(fromAddress <= toAddress);
		checkArgument(toAddress <= memory.getMaxAddress());
	}

	@Override
	public void run()
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(_file);
			doExport(fos);
		}
		catch (IOException ioe)
		{
			UI.invokeInFAT(new Runnable() {
				@Override
				public void run() {
					FxDialog fnw = new FxDialog(AlertType.ERROR, _res.get("memory.export.error"), _res.format("memory.export.write-error", _file.getPath()));
					// FIXME: delete if issue with long texts in linux is resolved
					fnw.setResizable(true);

					fnw.showAndWait();
				}
			});
		}
		finally
		{
			IOUtils.closeQuietly(fos);
		}
	}

	private void doExport(OutputStream os) throws IOException
	{
		MemoryState state = _memory.getMemoryState();

		BufferedOutputStream bos = IOUtils.toBufferedStream(os);

		byte[] intBytes = new byte[4];

		try
		{
			for (int i = _fromAddress, n = _toAddress; i <= n; i++)
			{
				int value = state.getInt(i);

				// convert integer to little-endian byte-array
				intBytes[0] = (byte) (value & 0xFF);
				value >>>= 8;
				intBytes[1] = (byte) (value & 0xFF);
				value >>>= 8;
				intBytes[2] = (byte) (value & 0xFF);
				value >>>= 8;
				intBytes[3] = (byte) (value & 0xFF);

				bos.write(intBytes);
			}
		}
		finally
		{
			IOUtils.closeQuietly(bos);
		}

		if (_log.isLoggable(Level.FINE))
			_log.fine(((_toAddress - _fromAddress) << 2) + " bytes / " + (_toAddress - _fromAddress) + " words exported to " + _file.getPath());
	}
}
