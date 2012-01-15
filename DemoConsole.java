public class DemoConsole {

    /**
     * @param args
     */

    static final int GRID_ROWS = 1024 * 8;
    static final int GRID_COLUMNS = 1024 * 8;
    static final int GRID_PARTITIONS = 32;

    public static void main(String[] args) {
        int grid_partitions[] = new int[]{
                1, 2, 3, 4, 5, 6, 7, 8,
                16, 32,
                64, 128, 256, 512, 1024
                // ,4096
        };

        SerialEngine sEngine = new SerialEngine(GRID_ROWS, GRID_COLUMNS);
        int i = 0, repeated = 10;

        // TODO create an array to save the results
        // TODO drop the results out to CSV
        // TODO create XLS file to show min/max/avg times, plot speed increase and scalability

        System.out.println("Data Decomposition Demo");
        System.out.println("Grid Rows: " + GRID_ROWS);
        System.out.println("Grid Cols: " + GRID_COLUMNS);
        // System.out.println("Grid Partitions: " + GRID_PARTITIONS);

        // serial style methods
        System.out.println("\nSerial style tasks");
        sEngine.Setup();

        // row based computation
        for (i = 0; i < repeated; i++) {
            sEngine.CrossRowsComputation();
        }

        // column based computation
        System.out.println();
        for (i = 0; i < repeated; i++) {
            sEngine.CrossColumnsComputation();
        }

        // parallel style methods
        System.out.println("\nParallel style tasks");

        ParallelEngine pEngine;

        for (int pSize : grid_partitions) {
            System.out.println("\n----------------");
            System.out.println("Grid Partitions: " + pSize);
            pEngine = new ParallelEngine(GRID_ROWS, GRID_COLUMNS, pSize);
            pEngine.Setup();

            // row based computation
            for (i = 0; i < repeated; i++) {
                pEngine.CrossRowsComputation();
            }

            // column based computation
            System.out.println();
            for (i = 0; i < repeated; i++) {
                pEngine.CrossColumnsComputation();
            }
        }

        System.out.println("\n----------------");
        // serial and parallel operations to verify that serial and parallel results are the same
        // check matching rows
        System.out.println("\nRow results match: " +
                (sEngine.RowResultMatches(ParallelEngine._RowResult) ? "TRUE" : "FALSE")
        );

        // check matching columns
        System.out.println("Column results match: " +
                (sEngine.ColumnResultMatches(ParallelEngine._ColumnResult) ? "TRUE" : "FALSE")
        );

        System.out.println("\nrun complete.");
    }


}



