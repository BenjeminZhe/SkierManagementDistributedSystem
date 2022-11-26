import Model.ChannelFactory;
import Model.LiftRide;
import Model.Message;
import Model.SkierVertical;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

    Gson gson = new Gson();
    private ObjectPool<Channel> pool;
    private final String EXCHANGE = "liftDealer";

    //private final int NUM_OF_CHANNELS = 50;
    public void init() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //connectionFactory.setHost("localhost");
        connectionFactory.setHost("172.31.10.32");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            pool = new GenericObjectPool<>(new ChannelFactory(connection));
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        String urlPath = req.getPathInfo();

        PrintWriter out = res.getWriter();
        Message message = new Message("Get Successful!");
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message.changeMessage("Website Not Valid!");
            out.print(gson.toJson(message));
            out.flush();
            return;
        }

        String[] urls = urlPath.split("/");
        if (!urlValidOrNot(urls)) {
            message.changeMessage("Invalid Request!");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else  {
            res.setStatus(HttpServletResponse.SC_OK);
            if (urls.length == 3) {
                out.write(gson.toJson(new SkierVertical("2022", 18)));
                out.flush();
            } else {
                out.write(gson.toJson(new SkierVertical("2022", 22)));
                out.flush();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String urlPath = req.getPathInfo();

        PrintWriter out = res.getWriter();
        Message message = new Message("POST Successful!");
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
            message.changeMessage("POST Not Valid!");
            out.print(gson.toJson(message));
            out.flush();
            return;
        }

        String[] urls = urlPath.split("/");
        if (!urlValidOrNot(urls)) {
            message.changeMessage("Invalid Request!");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            String body = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            LiftRide liftRide = gson.fromJson(body, LiftRide.class);
            if (liftRide.getLiftID() == null || liftRide.getTime() == null) {
                message.changeMessage("Invalid Request!");
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            System.out.print(liftRide.getLiftID());
            message.changeMessage("Input Successful!");
            res.setStatus(HttpServletResponse.SC_CREATED);
            liftRide.setResortId(Integer.valueOf(urls[1]));
            liftRide.setSeasonId(Integer.valueOf(urls[3]));
            liftRide.setDayId(Integer.valueOf(urls[5]));
            liftRide.setSkierId(Integer.valueOf(urls[7]));
            Channel cur = null;
            try {
                cur = pool.borrowObject();
                cur.exchangeDeclare(EXCHANGE, "fanout");
                String send = gson.toJson(liftRide);
                cur.basicPublish(EXCHANGE, "", null, send.getBytes());
                System.out.println(" [x] Sent '" + send + "'");
                pool.returnObject(cur);
                out.write(gson.toJson(message));
            } catch (Exception e) {
                e.printStackTrace();
                message.changeMessage("Unsuccessful!");
                out.write(gson.toJson(message));
            }
            out.flush();
        }

    }

    private boolean urlValidOrNot(String[] urls) {
        if (urls.length == 8) {
            if (!urls[2].equals("seasons") || !urls[4].equals("days") || !urls[6].equals("skiers")) {
                return false;
            }

            try {
                for (int i = 1; i < 8; i += 2) {
                    Integer.parseInt(urls[i]);
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (urls.length == 3) {
            if (!urls[2].equals("vertical")) {
                return false;
            }

            try {
                Integer.parseInt(urls[1]);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }
}
