package de.uni_hannover.sra.minimax_simulator.ui.gui.util;

import com.google.common.io.ByteStreams;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.FXDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The {@code MemoryImportWorker} is a {@link Runnable} that loads a memory image from file.
 * An error dialog will be shown if the import fails.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MemoryImportWorker implements Runnable {

	private final static Logger LOG = Logger.getLogger(MemoryImportWorker.class.getName());

	private final MachineMemory memory;
	private final int addressStart;

	private final int byteCount;
	private final int effectiveByteCount;

	private final File file;
	private final TextResource res;

	/**
	 * Constructs a new {@code MemoryImportWorker} instance.
	 *
	 * @param memory
	 *          the machine's memory
	 * @param addressStart
	 *          the start address
	 * @param byteCount
	 *          the number of bytes to import
	 * @param file
	 *          the file to read from
	 * @param res
	 *          the {@link TextResource} for getting localized texts
	 */
	public MemoryImportWorker(MachineMemory memory, int addressStart, int byteCount, File file, TextResource res) {
		this.memory = memory;
		this.addressStart = addressStart;

		this.byteCount = byteCount;

		long addressStartInBytes = ((long) addressStart) << 2;
		long endAddressInBytes = ((long) (memory.getMaxAddress() + 1)) << 2;
		effectiveByteCount = (int) (Math.min(endAddressInBytes, addressStartInBytes + byteCount) - addressStartInBytes);

		checkArgument(effectiveByteCount >= 0, "Negative number of bytes to import: " + effectiveByteCount);
		checkArgument(effectiveByteCount <= Integer.MAX_VALUE, "Too many bytes to import: " + effectiveByteCount);

		this.file = file;
		this.res = res;
	}

	/**
	 * Tries to load the memory image from file. An error dialog will be shown if the import fails.
	 */
	@Override
	public void run() {
		FileInputStream fis = null;
		boolean memoryNotifiesListeners = memory.getNotifiesListeners();

		try {
			fis = new FileInputStream(file);
			memory.setNotifiesListeners(false);
			doImport(fis);
		} catch (IOException ioe) {
			UI.invokeInFAT(new Runnable() {
				@Override
				public void run() {
					FXDialog fne = new FXDialog(AlertType.ERROR, res.get("memory.import.error"), res.format("memory.import.file-not-existing", file.getPath()));
					// FIXME: delete if issue with long texts in linux is resolved
					fne.setResizable(true);

					fne.showAndWait();
				}
			});
		} finally {
			memory.setNotifiesListeners(memoryNotifiesListeners);
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * Loads a memory image from file. The input is treated as little-endian.
	 *
	 * @param is
	 * 			the {@link InputStream} to use for reading
	 * @throws IOException
	 * 			thrown if the file is not readable or does not exist
	 */
	private void doImport(InputStream is) throws IOException {
		byte[] bytes = new byte[effectiveByteCount];
		ByteStreams.readFully(is, bytes, 0, effectiveByteCount);

		MemoryState state = memory.getMemoryState();

		// Convert byte array to ints
		// divide length by 4 (rounding up)
		int intCount = ((effectiveByteCount - 1) >> 2) + 1;

		for (int i = 0, n = intCount, a = addressStart; i < n; i++, a++) {
			// multiply by 4
			int byteNum = i << 2;

//			// treat 4 bytes as big-endian integer
//			int value = bytes[byteNum] << 24;
//			if (byteNum + 1 < effectiveByteCount)
//				value |= bytes[byteNum + 1] << 16;
//			if (byteNum + 2 < effectiveByteCount)
//				value |= bytes[byteNum + 2] << 8;
//			if (byteNum + 3 < effectiveByteCount)
//				value |= bytes[byteNum + 3];

			// treat 4 bytes as little-endian integer
			int value = bytes[byteNum];
			if (byteNum + 1 < effectiveByteCount)
				value |= bytes[byteNum + 1] << 8;
			if (byteNum + 2 < effectiveByteCount)
				value |= bytes[byteNum + 2] << 16;
			if (byteNum + 3 < effectiveByteCount)
				value |= bytes[byteNum + 3] << 24;

			state.setInt(a, value);
		}

		int truncated = byteCount - effectiveByteCount;

		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine(bytes.length + " bytes / " + intCount + " words imported, " + truncated + " bytes truncated from " + file.getPath());
		}

		if (truncated > 0) {
			int maxAddress = memory.getMaxAddress();
			int width = memory.getAddressWidth();

			UI.invokeInFAT(new Runnable() {
				@Override
				public void run() {
					FXDialog trunc = new FXDialog(AlertType.WARNING, res.get("memory.import.warning"), res.format("memory.import.bytes-truncated", Util.toHex(maxAddress, width, true), truncated));
					// FIXME: delete if issue with long texts in linux is resolved
					trunc.setResizable(true);

					trunc.showAndWait();
				}
			});
		}
	}
}
