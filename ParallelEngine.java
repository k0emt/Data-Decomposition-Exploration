public class ParallelEngine extends Thread {
    static public int _dataGrid[][];
    static public int _RowResult[];
    static public int _ColumnResult[];
    static public int _numberOfPartitions;

    private enum ComputeMode {Row, Column}

    ;

    static int _rows;
    static int _cols;

    ComputeMode _computeMode;
    int _start;
    int _end;

    public ParallelEngine(int rows, int cols, int numberOfPartitions) {
        _rows = rows;
        _cols = cols;
        _numberOfPartitions = numberOfPartitions;

        _dataGrid = new int[_rows][_cols];
        _RowResult = new int[_rows];
        _ColumnResult = new int[_cols];
    }

    private ParallelEngine(ComputeMode rowOrColumn, int start, int end) {
        _computeMode = rowOrColumn;
        _start = start;
        _end = end;
    }

    // TODO convert to parallel setup?
    public void Setup() {
        int i, j;
        StopWatch setupSW = new StopWatch();
        setupSW.Start();

        // need to put the setup code here
        for (i = 0; i < _rows; i++)
            for (j = 0; j < _cols; j++)
                _dataGrid[i][j] = (i * j) % 1024;

        setupSW.Stop();
        System.out.println("setup time: " + setupSW.Elapsed());
    }

    // TODO add unhandled mode
    public void run() {
        switch (_computeMode) {
            case Row:
                ComputeRowsDomainLimited(_start, _end);
                break;

            case Column:
                ComputeColumnsDomainLimited(_start, _end);
                break;

            default:

                // currently should be exceptional
                break;
        }
    }

    public void CrossRowsComputation() {
        StopWatch setupSW = new StopWatch();
        setupSW.Start();

        // set up and make calls to runnable
        int partitionSize = _rows / _numberOfPartitions;
        int partitionRemainder = _rows % _numberOfPartitions;
        int start;
        int end = 0;
        Thread[] runners;

        runners = new Thread[_numberOfPartitions + (partitionRemainder > 0 ? 1 : 0)];

        for (int o = 0; o < _numberOfPartitions; o++) {
            start = o * partitionSize;
            end = start + partitionSize;
            runners[o] = new Thread(new ParallelEngine(ComputeMode.Row, start, end));
        }
        if (partitionRemainder > 0) {
            runners[runners.length - 1] = new Thread(new ParallelEngine(ComputeMode.Row, end + 1, end + partitionRemainder));
        }

        // Start all the threads (including remainder)
        for (int o = 0; o < runners.length; o++) {
            // System.out.println(o + " of " + runners.length);
            runners[o].start();
        }

        // wait for them to finish
        for (int o = 0; o < runners.length; o++) {
            try {
                runners[o].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        setupSW.Stop();
        System.out.println("cross row computation time: " + setupSW.Elapsed());
    }

    private void ComputeRowsDomainLimited(int start, int end) {
        int i, j;

        for (i = start; i < end; i++)
            for (j = 0; j < _cols; j++)
                _RowResult[i] = (_RowResult[i] + _dataGrid[i][j] * 5) % 93;
    }

    public void CrossColumnsComputation() {
        // set up and make calls to runnable
        StopWatch setupSW = new StopWatch();
        setupSW.Start();

        // test initial serial configuration
        // ComputeColumnsDomainLimited(0, _cols);

        // set up and make calls to runnable
        int partitionSize = _cols / _numberOfPartitions;
        int partitionRemainder = _cols % _numberOfPartitions;
        int start;
        int end = 0;
        Thread[] runners;

        runners = new Thread[_numberOfPartitions + (partitionRemainder > 0 ? 1 : 0)];

        for (int o = 0; o < _numberOfPartitions; o++) {
            start = o * partitionSize;
            end = start + partitionSize;
            runners[o] = new Thread(new ParallelEngine(ComputeMode.Column, start, end));
        }
        if (partitionRemainder > 0) {
            runners[runners.length - 1] = new Thread(new ParallelEngine(ComputeMode.Column, end, end + partitionRemainder));
        }

        // Start all the threads (including remainder)
        for (int o = 0; o < runners.length; o++) {
            // System.out.println(o + " of " + runners.length);
            runners[o].start();
        }

        // wait for them to finish
        for (int o = 0; o < runners.length; o++) {
            try {
                runners[o].join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        setupSW.Stop();
        System.out.println("cross col computation time: " + setupSW.Elapsed());
    }

    private void ComputeColumnsDomainLimited(int start, int end) {
        int i, j;

        for (j = start; j < end; j++) {
            for (i = 0; i < _rows; i++) {
                _ColumnResult[j] = (_ColumnResult[j] + (_dataGrid[i][j] * 7)) % 97;
            }
        }
    }

    /*
   computeRowsDomainLimited(int startRow, int endRow)
   {
       Partsize = size / # threads
       Remainder =

       For o = # threads
       {
           Start = o * size
           End = start + partSize
           computeRowsDomain(start,end)
       }
       computeRowsDomain(end, end+remainder)

       Set up threads with the above
       Then run them
       Then join/wait for all to finish
   }
    */
}
