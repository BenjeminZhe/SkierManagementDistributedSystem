import Model.LiftRide;
import com.google.gson.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConsumerRunnable implements Runnable {
    Gson gson = new Gson();
    private final Connection cn;
    private final String queue;
    private final String exchange;
    private final Map<Integer, List<Integer>> hashmap;
    private final JedisPool pool;

    public ConsumerRunnable(Connection cn, String queue, String exchange, Map<Integer, List<Integer>> hashmap, JedisPool pool) {
        this.cn = cn;
        this.queue = queue;
        this.exchange = exchange;
        this.hashmap = hashmap;
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            Channel channel = cn.createChannel();
            channel.exchangeDeclare(exchange, "fanout");
            channel.queueDeclare(queue, false, false, false, null);
            channel.queueBind(queue, exchange, "");
            System.out.println(" [x] Thread " + Thread.currentThread().getId() + " waiting for messages.");

            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                LiftRide lift = gson.fromJson(message, LiftRide.class);

                System.out.println(" [x] Received '" + message + "'");
                JsonObject input = new JsonObject();
                String key;
                try {
                    String skierId = String.valueOf(lift.getSkierId());
                    String dayId = String.valueOf(lift.getDayId());
                    key = skierId + ":" + dayId;
                    input.add("liftID", JsonParser.parseString(lift.getLiftID()));
                    input.add("time", JsonParser.parseString(lift.getTime()));
                    input.add("resortId", JsonParser.parseString(String.valueOf(lift.getResortId())));
                    input.add("seasonId", JsonParser.parseString(String.valueOf(lift.getSeasonId())));
                } catch (Exception e) {
                    e.printStackTrace();
                    channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
                    return;
                }

                try (Jedis jedis = pool.getResource()) {
                    jedis.sadd(key, new GsonBuilder().setPrettyPrinting().create().toJson(input));
                }

                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            channel.basicConsume(queue, false, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
