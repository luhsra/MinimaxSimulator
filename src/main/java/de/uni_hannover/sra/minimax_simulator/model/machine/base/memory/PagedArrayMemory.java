package de.uni_hannover.sra.minimax_simulator.model.machine.base.memory;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation of the {@link AbstractMemory} using a paged array (two-dimensional array) to store values.
 *
 * @author Martin L&uuml;ck
 */
public class PagedArrayMemory extends AbstractMemory {

    /**
     * The {@link MemoryState} of a {@link PagedArrayMemory}.
     */
    private class PagedMemoryState implements MemoryState {

        /** The paged array holding the values stored in memory. */
        private final int[][] pages;

        /**
         * Constructs a new {@code PagedMemoryState} with {@link PagedArrayMemory#pageCount} pages.
         */
        PagedMemoryState() {
            pages = new int[pageCount][];
        }

        @Override
        public int getInt(int address) {
            int value = page(address)[address & pageAddressMask];
            fireReadAccess(address, value);
            return value;
        }

        @Override
        public void setInt(int address, int value) {
            page(address)[address & pageAddressMask] = value;
            fireWriteAccess(address, value);
        }

        @Override
        public void zero() {
            Arrays.fill(pages, null);
            fireMemoryChanged();
        }

        /**
         * Gets the entire page the specified address belongs to.
         *
         * @param addr
         *          the address
         * @return
         *          the page the address belongs to
         */
        int[] page(int addr) {
            int[] p = pages[addr >>> pageAddressWidth];
            if (p == null) {
                pages[addr >>> pageAddressWidth] = p = new int[pageSize];
            }
            return p;
        }

        /**
         * Creates a copy of the {@code PagedMemoryState}.
         *
         * @return
         *          the copy
         */
        public MemoryState copy() {
            PagedMemoryState c = new PagedMemoryState();
            for (int i = 0; i < pages.length; i++) {
                if (pages[i] != null) {
                    c.pages[i] = Arrays.copyOf(pages[i], pageSize);
                }
            }
            return c;
        }
    }

    private final int pageCount;
    private final int pageSize;

    private final int pageAddressWidth;
    private final int pageAddressMask;

    /**
     * Constructs a new {@code PagedArrayMemory} of the specified length with the
     * specified page size.
     *
     * @param addressWidth
     *          the length of the new {@code PagedArrayMemory}
     * @param pageAddressWidth
     *          the length of a page
     */
    public PagedArrayMemory(int addressWidth, int pageAddressWidth) {
        super(addressWidth);
        checkArgument(pageAddressWidth <= addressWidth, "too big pages");

        pageSize = 1 << pageAddressWidth;
        pageCount = 1 << (addressWidth - pageAddressWidth);

        this.pageAddressWidth = pageAddressWidth;
        pageAddressMask = (1 << pageAddressWidth) - 1;

        setupMemoryState();
    }

    @Override
    protected MemoryState createMemoryState() {
        return new PagedMemoryState();
    }

    @Override
    protected MemoryState cloneState(MemoryState state) {
        return ((PagedMemoryState) state).copy();
    }
}