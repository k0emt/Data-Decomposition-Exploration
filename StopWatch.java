public class StopWatch {
    static private long _startTime;
    static private long _endTime = 0;

    void Start() {
        _startTime = System.currentTimeMillis();
        _endTime = 0;
    }

    void Stop() {
        _endTime = System.currentTimeMillis();
    }

    // Elapsed time in seconds
    float Elapsed() {
        if (_endTime > 0) {
            return (_endTime - _startTime) / 1000F;
        } else {
            // the StopWatch hasn't had the Stop button pushed
            return (System.currentTimeMillis() - _startTime) / 1000F;
        }
    }
}