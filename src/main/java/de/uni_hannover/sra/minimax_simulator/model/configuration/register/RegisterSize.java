package de.uni_hannover.sra.minimax_simulator.model.configuration.register;

/**
 * This enumeration is used to differentiate between different sizes of the registers ({@link RegisterExtension}).
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public enum RegisterSize {

    /**
     * 32 Bit register
     */
    BITS_32 {
        @Override
        public String getHexFormat() {
            return "0x%08X";
        }

        @Override
        public int getBitMask() {
            return 0xFFFFFFFF;
        }

        public String toString() {
            return "32 Bits";
        }
    },

    /**
     * 24 Bit register
     */
    BITS_24
    {
        @Override
        public String getHexFormat() {
            return "0x%06X";
        }

        @Override
        public int getBitMask() {
            return 0x00FFFFFF;
        }

        public String toString() {
            return "24 Bits";
        }
    };

    /**
     * Gets the hexadecimal format string of the {@code RegisterSize}.
     *
     * @return
     *          the hex format string
     */
    public abstract String getHexFormat();

    /**
     * Gets the bit mask of the {@code RegisterSize}.
     *
     * @return
     *          the bit mask
     */
    public abstract int getBitMask();
}