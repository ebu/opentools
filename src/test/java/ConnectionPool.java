import ch.ebu.opentools.pool.ObjectPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//The three remaining methods are abstract
//and therefore must be implemented by the subclass
public class ConnectionPool extends ObjectPool<Connection> {

    private String dsn, usr, pwd;

    public ConnectionPool(int poolSize, long expirationTime, String driver, String dsn, String usr, String pwd) {
        super(poolSize, expirationTime);
        try {
            Class.forName(driver).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dsn = dsn;
        this.usr = usr;
        this.pwd = pwd;
    }

    @Override
    protected Connection create() {
        try {
            return (DriverManager.getConnection(dsn, usr, pwd));
        } catch (SQLException e) {
            e.printStackTrace();
            return (null);
        }
    }

    @Override
    public void expire(Connection o) {
        try {
            o.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validate(Connection o) {
        try {
            return (!o.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            return (false);
        }
    }

}