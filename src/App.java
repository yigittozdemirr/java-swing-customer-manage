import business.UserController;
import core.Database;
import core.Helper;
import entity.User;
import view.DashboardUI;
import view.LoginUI;

import java.sql.Connection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App {
    public static void main(String[] args) {
        Helper.setTheme();
        LoginUI loginUI = new LoginUI();
        /*
        UserController userController = new UserController();
        User user = userController.findByLogin("yigitozdemir@patika.dev", "123123");
        DashboardUI dashboardUI = new DashboardUI(user);
         */
    }
}