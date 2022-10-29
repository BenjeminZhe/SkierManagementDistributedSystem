import Model.LiftRide;
import com.google.gson.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConsumerRunnable implements Runnable {
    Gson gson = new Gson();
    private final Connection cn;
    private final String queue;
    private final Map<Integer, List<Integer>> hashmap;

    public ConsumerRunnable(Connection cn, String queue, Map<Integer, List<Integer>> hashmap) {
        this.cn = cn;
        this.queue = queue;
        this.hashmap = hashmap;
    }

    @Override
    public void run() {
        try {
            Channel channel = cn.createChannel();
            channel.queueDeclare(queue, false, false, false, null);
            System.out.println(" [x] Thread " + Thread.currentThread().getId() + " waiting for messages.");

            channel.basicQos(1);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                LiftRide lift = gson.fromJson(message, LiftRide.class);

                System.out.println(" [x] Received '" + message + "'");
                try {
                    storeInMap(lift);
                } finally {
                    System.out.println(" [x] Done");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };
            channel.basicConsume(queue, false, deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void storeInMap(LiftRide liftRide) {
        int liftId = Integer.parseInt(liftRide.getLiftID());
        int skierId = liftRide.getSkierId();
        if (!hashmap.containsKey(skierId)) {
            hashmap.put(skierId, Collections.synchronizedList(new ArrayList<>()));
        }
        hashmap.get(skierId).add(liftId);
    }
}
