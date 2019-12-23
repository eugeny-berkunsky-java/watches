package manage;

import model.Order;
import model.OrderDAO;

import java.time.format.DateTimeFormatter;

public class OrderManager {
    private OrderDAO dao;

    public OrderManager() {
        dao = new OrderDAO();
        dao.generateOrders();
    }

    public void showOrders() {
        System.out.println("-------------------------Orders------------------------");
        dao.getAll().stream().map(this::orderView).forEach(System.out::println);
        System.out.println("-------------------------------------------------------");
    }

    private String orderView(Order order) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return String.format("%d - %s - %s - %.02f", order.getId(), order.getDate().format(formatter),
                order.getCustomer().getName(), order.getTotalPrice());
    }
}
