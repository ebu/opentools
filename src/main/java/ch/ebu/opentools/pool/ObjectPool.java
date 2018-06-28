package ch.ebu.opentools.pool;

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class ObjectPool<T> {
    private long expirationTime;
    private int poolSize;

    private Hashtable<T, Long> unlocked, locked;

    protected ObjectPool() {
        unlocked = new Hashtable<>();
        locked = new Hashtable<>();
    }

    protected abstract T create();

    public abstract boolean validate(T t);

    public abstract void expire(T t);

    public abstract void buildPool(int poolSize, long expirationTime);

    public int getPooledObjectNumber() {
        return locked.size();
    }

    public int getFreedObjectNumber() {
        return unlocked.size();
    }

    public boolean isExpired(T t, long now) {
        T o = (T) locked.get(t);
        if (o != null) {
            return now - locked.get(t) > this.expirationTime;
        } else {
            return true;
        }
    }

    /**
     * Remove expired or not valid objects
     */
    public synchronized void refresh() {
        long now = System.currentTimeMillis();
        T t;
        // locked
        if (locked.size() > 0) {
            Enumeration<T> e = locked.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                if (isExpired(t, now)) {
                    // object has expired
                    expire(t);
                    locked.remove(t);
                    unlocked.put(t, now);
                } else {
                    // object is not valid
                    if (!validate(t)) {
                        locked.remove(t);
                        unlocked.put(t, now);
                    }
                }
            }
        }
        // unlocked
        if (unlocked.size() > 0) {
            Enumeration<T> e = unlocked.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                if ((now - unlocked.get(t)) > expirationTime) {
                    // object has expired
                    expire(t);
                    unlocked.remove(t);
                }
            }
        }
    }

    /**
     * add object in pool
     *
     * @param t
     */
    public synchronized void checkOut(T t) {
        if (locked.size() > 0) {
            locked.remove(t);
            unlocked.put(t, System.currentTimeMillis());
            System.out.println(t + " is removed from locked pool and push to unlocked");
        } else {
            System.out.println("No object found, impossible to check it out !");
        }
    }

    /**
     * add object in pool
     *
     * @param t
     */
    public synchronized void checkIn(T t) {
        if (locked.size() < poolSize) {
            unlocked.remove(t);
            locked.put(t, System.currentTimeMillis());
            System.out.println(t + " is added to locked pool");
        } else {
            System.out.println("Maximum pool size reached (" + poolSize + "), impossible to check new object in !");
        }
    }

    /**
     * free all object
     * expiration
     */
    public synchronized void freeAll() {
        long now = System.currentTimeMillis();
        T t;
        if (locked.size() > 0) {
            Enumeration<T> e = locked.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                locked.remove(t);
                unlocked.put(t, now);
            }
        }
    }

    /**
     * remove all objects (unlocked and locked)
     */
    public synchronized void shutdownAll() {
        long now = System.currentTimeMillis();
        T t;
        // locked
        if (locked.size() > 0) {
            Enumeration<T> e = locked.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                expire(t);
                locked.remove(t);
            }
        }
        // unlocked
        if (unlocked.size() > 0) {
            Enumeration<T> e = unlocked.keys();
            while (e.hasMoreElements()) {
                t = e.nextElement();
                // object has expired
                expire(t);
                unlocked.remove(t);
            }
        }
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }
}
