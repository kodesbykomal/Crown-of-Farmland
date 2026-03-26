package farmland;

import java.io.File;

/**
 * Holds validated startup configuration.
 */
public class StartupConfig {

    private final int seed;
    private final File unitsFile;
    private final File deck1File;
    private final File deck2File;
    private final String mode;
    private final String symbols;

    public StartupConfig(int seed,
                         File unitsFile,
                         File deck1File,
                         File deck2File,
                         String mode,
                         String symbols) {

        this.seed = seed;
        this.unitsFile = unitsFile;
        this.deck1File = deck1File;
        this.deck2File = deck2File;
        this.mode = mode;
        this.symbols = symbols;
    }

    public int getSeed() { return seed; }

    public File getUnitsFile() { return unitsFile; }

    public File getDeck1File() { return deck1File; }

    public File getDeck2File() { return deck2File; }

    public String getMode() { return mode; }

    public String getSymbols() { return symbols; }
}