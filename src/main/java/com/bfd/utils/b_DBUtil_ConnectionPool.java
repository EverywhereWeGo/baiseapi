package com.bfd.utils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.bfd.utils.a_PropertiesLoadUtil.loadProperties;


/*
初始化创建连接队列
每次取从队列最后一个取，如果失效则放在队列最前面，如果有效则使用返回后还是放在队列末尾
检查程序每分钟取队列第一个检查有效性，没问题则放在最后，有问题则删除旧的并创建新的放在队列最后。
 */
public class b_DBUtil_ConnectionPool {
    private static Logger logger = LoggerFactory.getLogger(b_DBUtil_ConnectionPool.class);
    private static LinkedList<Connection> connectionQueue;

    private static String url;
    private static String username;
    private static String password;
    private static int poolsnum;

    static {
        try {
            Properties prop = loadProperties("config.properties");
            url = prop.getProperty("url");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            poolsnum = (null == prop.getProperty("poolsnum")) ? 4 : Integer.valueOf(prop.getProperty("poolsnum"));
            Class.forName(prop.getProperty("driverclass"));
            createConnectionPool();
            timerConnectionVaildCheck();
            logger.info("数据库连接池创建成功，连接数:" + poolsnum + "个");
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private static void createConnectionPool() {
        try {
            if (connectionQueue == null) {
                connectionQueue = new LinkedList<Connection>();
                for (int i = 0; i < poolsnum; i++) {
                    Connection conn = DriverManager.getConnection(url, username, password);
                    connectionQueue.addLast(conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public synchronized static Connection getConnection() {
        if (connectionQueue.size() == 0) {
            logger.warn("连接池耗尽,请稍后再试");
        }
        Connection conn;
        try {
            //每次从最后一个取，有问题放最前面
            for (int i = 0; i < connectionQueue.size(); i++) {
                conn = connectionQueue.removeLast();
                if (conn.isValid(1000)) {
                    return conn;
                } else {
                    connectionQueue.addFirst(conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized static void returnConnection(Connection conn) {
        if (null != conn) {
            connectionQueue.addLast(conn);
        }

    }

    private synchronized static Connection getConnectionforCheck() {
        Connection conn = connectionQueue.removeFirst();
        return conn;
    }


    private static Connection checkandcreate(Connection conn) {
        try {
            if (!conn.isValid(100)) {
                conn = DriverManager.getConnection(url, username, password);
            } else {
                logger.trace("有效直接返回");
            }
        } catch (Exception e) {
            //如果不返回,connection会少于创建时连接池的数量
            logger.error("创建失败,先返回之前无效的connection", e);
        }
        return conn;
    }


    //守护线程每分钟检测连接队列中的连接有效性,以上一个检查结束作为起点等待60秒
    private static void timerConnectionVaildCheck() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("daemon_dbcheck").daemon(true).build());
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection connectionforcheck = checkandcreate(getConnectionforCheck());
                    returnConnection(connectionforcheck);
                } catch (Exception e) {
                    logger.error("dbcheck失败", e);
                }
            }
        }, 60, 60, TimeUnit.SECONDS);
    }


    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection conn = getConnection();
            }
        }, "1").start();
    }

}
