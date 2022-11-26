import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SkierConsumer {
    private static final int NUM_THREADS = 100;
    private static final Map<Integer, List<Integer>> HASH_MAP = new ConcurrentHashMap<>();
    private static String QUEUE = "skier";
    private static String EXCHANGE = "liftDealer";
    private static JedisPool pool = null;

    public static void main(String[] args) throws IOException {
        pool = new JedisPool("172.31.9.8", 6379);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(3000);
        //config.setTestOnBorrow(true);
        pool.setConfig(config);

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //connectionFactory.setHost("localhost");
        connectionFactory.setHost("172.31.10.32");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        try {
            Connection connection = connectionFactory.newConnection();
            ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);
            for (int i = 0; i < NUM_THREADS; i++) {
                threadPool.execute(new ConsumerRunnable(connection, QUEUE, EXCHANGE, HASH_MAP, pool));
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
