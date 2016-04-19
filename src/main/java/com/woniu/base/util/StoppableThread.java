package com.woniu.base.util;

public abstract class StoppableThread implements Runnable {

	protected volatile boolean stopped;
	protected volatile Thread thread;

	public void start() {
		if (thread != null) {
			throw new IllegalStateException("already started");
		}

		stopped = false;
		thread = new Thread(this, getThreadName());
		thread.start();
	}

	protected String getThreadName() {
		return getClass().getSimpleName();
	}

	public boolean isStopped() {
		return stopped;
	}

	public void stop() {
		if (thread == null) {
			return;
		}

		stopped = true;
		while (thread.isAlive()) {
			thread.interrupt();
			try {
				thread.join(50);
			} catch (InterruptedException e) {
			}
		}
		thread = null;
	}

    public boolean isAlive() {
        Thread t = thread;
        if (t == null) {
            return false;
        }

        return t.isAlive();
    }

    public void join(long millis) {
        try {
            thread.join(millis);
        } catch (InterruptedException e) {
        }
    }
}
