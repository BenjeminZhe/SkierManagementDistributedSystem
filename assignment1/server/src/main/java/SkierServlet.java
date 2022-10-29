/**
 * <h3>server</h3>
 *
 * @description <p>${description}</p>
 * @author Zhe Xi
 * @date 2022-10-04 00:40
 **/

import Model.LiftRide;
import Model.Message;
import Model.SkierVertical;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import com.google.gson.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@WebServlet(name = "SkierServlet", value = "/SkierServlet")
public class SkierServlet extends HttpServlet {

    Gson gson = new Gson();
    private BlockingQueue<Channel> pool;
    private final String QUEUE = "liftDealer";
    private final int NUM_OF_CHANNELS = 50;
    public void init() {
        pool = new LinkedBlockingQueue<>();
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = null;
        try {
            connection = connectionFactory.newConnection();
            for (int i = 0; i < NUM_OF_CHANNELS; i++) {
                pool.offer(connection.createChannel());
            }
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
            }
            message.changeMessage("Input Successful!");
            res.setStatus(HttpServletResponse.SC_CREATED);
            liftRide.setResortId(Integer.valueOf(urls[1]));
            liftRide.setSeasonId(Integer.valueOf(urls[3]));
            liftRide.setDayId(Integer.valueOf(urls[5]));
            liftRide.setSkierId(Integer.valueOf(urls[7]));
            try {
                Channel cur = pool.take();
                cur.queueDeclare(QUEUE, false, false, false, null);
                String send = gson.toJson(liftRide);
                cur.basicPublish("", QUEUE, null, send.getBytes());
                System.out.println(" [x] Sent '" + send + "'");
                pool.add(cur);
                out.write(gson.toJson(message));
            } catch (InterruptedException e) {
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
