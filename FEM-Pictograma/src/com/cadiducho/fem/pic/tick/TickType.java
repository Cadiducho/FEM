package com.cadiducho.fem.pic.tick;

public enum TickType {
    MIN_64, MIN_32, MIN_16, MIN_08, MIN_04, MIN_02, MIN_01, SLOWEST, SLOWER, SLOW, SEC, FAST, FASTER, FASTEST, TICK;

    private long _time;
    private long _last;
    private long _timeSpent;
    private long _timeCount;

    public boolean Elapsed() {
        if (internalElapsed(this._last, this._time)) {
            this._last = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public void StartTime() {
        this._timeCount = System.currentTimeMillis();
    }

    public void StopTime() {
        this._timeSpent += System.currentTimeMillis() - this._timeCount;
    }

    public void PrintAndResetTime() {
        System.out.println(name() + " in a second: " + this._timeSpent);
        this._timeSpent = 0L;
    }

    public static boolean internalElapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }
}
