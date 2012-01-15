public class SerialEngine {
    public int _dataGrid[][];
    public int _RowResult[];
    public int _ColumnResult[];

    int _rows;
    int _cols;

    public SerialEngine(int rows, int cols) {
        _rows = rows;
        _cols = cols;

        _dataGrid = new int[_rows][_cols];
        _RowResult = new int[_rows];
        _ColumnResult = new int[_cols];
    }

    public void Setup() {
        int i, j;
        StopWatch setupSW = new StopWatch();
        setupSW.Start();

        for (i = 0; i < _rows; i++)
            for (j = 0; j < _cols; j++)
                _dataGrid[i][j] = (i * j) % 1024;

        setupSW.Stop();
        System.out.println("setup time: " + setupSW.Elapsed());
    }

    public void CrossRowsComputation() {
        int i, j;
        StopWatch setupSW = new StopWatch();
        setupSW.Start();

        for (i = 0; i < _rows; i++)
            for (j = 0; j < _cols; j++)
                _RowResult[i] = (_RowResult[i] + _dataGrid[i][j] * 5) % 93;

        setupSW.Stop();
        System.out.println("cross row computation time: " + setupSW.Elapsed());
    }

    public void CrossColumnsComputation() {
        int i, j;
        StopWatch setupSW = new StopWatch();
        setupSW.Start();

        for (j = 0; j < _cols; j++) {
            for (i = 0; i < _rows; i++) {
                _ColumnResult[j] = (_ColumnResult[j] + (_dataGrid[i][j] * 7)) % 97;
            }
            // System.out.println("column computation: " + _ColumnResult[j]);
        }
        setupSW.Stop();
        System.out.println("cross col computation time: " + setupSW.Elapsed());
    }

    public boolean RowResultMatches(int[] compareWith) {
        boolean match = true;

        for (int i = 0; i < _rows && match; i++) {
            if (_RowResult[i] != compareWith[i]) {
                match = false;
            }
        }
        return match;
    }

    public boolean ColumnResultMatches(int[] compareWith) {
        boolean match = true;

        for (int i = 0; i < _cols && match; i++) {
            // System.out.println(_ColumnResult[i] + " " + compareWith[i]);
            if (_ColumnResult[i] != compareWith[i]) {
                match = false;
            }
        }
        return match;
    }
}
