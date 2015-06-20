package de.uni_hannover.sra.minimax_simulator.gui.util;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.StringConverter;

/**
 * Created by philipp on 20.06.15.
 */
public class MemorySpinnerValueFactory extends SpinnerValueFactory.IntegerSpinnerValueFactory {

    public MemorySpinnerValueFactory(MachineMemory mMemory) {
        super(mMemory.getMinAddress(), mMemory.getMaxAddress());
        this.setWrapAround(true);
        final String _addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);
        this.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return (value == null) ? "" : String.format(_addressFormatString, value);
            }

            @Override
            public Integer fromString(String text) {
                if (text == null || text.isEmpty())
                    return null;

                try {
                    Long l = Long.valueOf(text, 16);
                    int value = l.intValue();
                    if (value < mMemory.getMinAddress())
                        value = mMemory.getMinAddress();
                    else if (value > mMemory.getMaxAddress())
                        value = mMemory.getMaxAddress();
                    return value;
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                return null;
            }
        });
    }

}
