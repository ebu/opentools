import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAll {
    static int poolSize = 3;
    static long expirationTimeInSeconds = 10;
    static ConnectionPool connectionPool;

    @BeforeAll
    static void up() {
        startPool();
    }

    @AfterAll
    static void down() {
        shutDownPool();
    }

    @Test
    void testA_Basic() throws InterruptedException, SQLException {
        int connectionTest = 6;
        // try to take over the pool size
        Connection[] connectionArray = new Connection[connectionTest];
        for (int i = 0; i < connectionTest; i++) {
            connectionArray[i] = connectionPool.create();
            System.out.println("Connection ID[" + i + "] = " + connectionArray[i].toString()
                    + " is closed() " + connectionArray[i].isClosed());
            connectionPool.checkIn(connectionArray[i]);
        }
        assertEquals(connectionPool.getPooledObjectNumber(), poolSize);
        assertEquals(connectionPool.getFreedObjectNumber(), 0);

        // check pool is totally available
        sleep(expirationTimeInSeconds * 1000);
        connectionPool.refresh();
        assertEquals(connectionPool.getPooledObjectNumber(), 0);
        assertEquals(connectionPool.getFreedObjectNumber(), poolSize);

        // connection 4,5 still alived
        int count = 0;
        for (int i = connectionTest - 1; i > poolSize; i--) {
            System.out.println("Connection ID[" + i + "] = " + connectionArray[i].toString()
                    + " is closed() " + connectionArray[i].isClosed());
            connectionPool.checkOut(connectionArray[i]);
            if (!connectionArray[i].isClosed()) {
                count++;
            }
        }
        assertEquals(connectionTest - poolSize - 1, count);
    }

    @Test
    public void testB_InOut() throws InterruptedException, SQLException {
        shutDownPool();
        connectionPool = null;
        sleep(3000);

        // restart with fresh pool
        poolSize = 3;
        startPool();
        // try to take over the pool size
        Connection[] connectionArray = new Connection[poolSize];
        for (int i = 0; i < poolSize; i++) {
            connectionArray[i] = connectionPool.create();
            System.out.println("Connection ID[" + i + "] = " + connectionArray[i].toString()
                    + " is closed() " + connectionArray[i].isClosed());
            connectionPool.checkIn(connectionArray[i]);
        }
        assertEquals(connectionPool.getPooledObjectNumber(), poolSize);
        assertEquals(connectionPool.getFreedObjectNumber(), 0);

        // push all unlocked to locked
        connectionPool.freeAll();
        assertEquals(connectionPool.getPooledObjectNumber(), 0);
        assertEquals(connectionPool.getFreedObjectNumber(), poolSize);

        // so shutdown
        connectionPool.shutdownAll();
        assertEquals(connectionPool.getPooledObjectNumber(), 0);
        assertEquals(connectionPool.getFreedObjectNumber(), 0);
    }

    public static void startPool() {
        connectionPool = ConnectionPool.getInstance();
        connectionPool.buildPool(poolSize, expirationTimeInSeconds * 1000,
                "com.mysql.jdbc.Driver", "jdbc:mysql://db4free.net:3306/metairie",
                "rootmetairie", "eurovision");
    }

    public static void shutDownPool() {
        connectionPool.shutdownAll();
    }
}