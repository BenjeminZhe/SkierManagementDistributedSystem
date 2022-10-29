/**
 * <h3>server</h3>
 *
 * @description <p>${description}</p>
 * @author Zhe Xi
 * @date 2022-10-04 00:40
 **/

import Model.LiftRide;
import Model.Message;
import Model.Resorts;
import Model.ResortsList;
import Model.SeasonsList;
import Model.SkierVertical;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "ResortServlet", value = "/ResortServlet")
public class ResortServlet extends HttpServlet {

    Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String urlPath = req.getPathInfo();

        PrintWriter out = res.getWriter();
        Message message = new Message("Get Successful!");
        if (urlPath == null || urlPath.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_OK);
            ResortsList rl = new ResortsList();
            rl.addResorts(new Resorts("Ben", 1));
            out.print(gson.toJson(rl));
            out.flush();
            return;
        }

        String[] urls = urlPath.split("/");
        if (!urlValidOrNot(urls)) {
            message.changeMessage("Invalid Request!");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            res.setStatus(HttpServletResponse.SC_OK);
            if (urls.length == 3) {
                SeasonsList sl = new SeasonsList();
                sl.addSeasons("2022");
                out.write(gson.toJson(sl));
                out.flush();
            } else {
                out.write(gson.toJson("{}"));
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
            message.changeMessage("Input Successful!");
            res.setStatus(HttpServletResponse.SC_CREATED);
        }
        out.write(gson.toJson(message));
        out.flush();
    }

    private boolean urlValidOrNot(String[] urls) {
//        if (urls.length == 3) {
//            if (!urls[2].equals("seasons")) {
//                return false;
//            }
//
//            try {
//                Integer.parseInt(urls[1]);
//                return true;
//            } catch (Exception e) {
//                return false;
//            }
//        } else if (urls.length == 7) {
//            if (!urls[2].equals("seasons") || urls[4].equals("day") || urls[6].equals("skiers")) {
//                return false;
//            }
//
//            try {
//                for (int i = 1; i < 7; i += 2) {
//                    Integer.parseInt(urls[i]);
//                }
//                return true;
//            } catch (Exception e) {
//                return false;
//            }
//        }
//
//        return false;
        return true;
    }
}
