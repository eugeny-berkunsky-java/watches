package manage;

import model.Order;
import model.OrderDAO;

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
        return String.format("%d - %s - %s - %.02f", order.getId(), order.getDate(),
                order.getCustomer().getName(), order.getTotalPrice());
    }
}
