package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by kapodes on 07.03.2016.
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("redirect to mealList");
        List<UserMeal> mealList = UserMealsUtil.getTestUserMealList();
        List<UserMealWithExceed> processedMealList = UserMealsUtil.getFilteredMealsWithExceeded(mealList,
                LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        request.setAttribute("mealList",processedMealList);

        request.getRequestDispatcher("/mealList.jsp").forward(request, response);
//        response.sendRedirect("mealList.jsp");
    }

}
