import models.Order;
//mport models.OrderItem;
import models.Product;
import Services.OrderService;
//mport Services.ProductService;
import javafx.beans.property.ReadOnlyStringWrapper;

//mport java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//mport java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class POSUI extends Application {
    private OrderService orderService = new OrderService();

    // rivate ProductService productService = new ProductService();
    @Override
    public void start(Stage PrimaryStage) {
        BorderPane root = new BorderPane();

        // sidebar
        VBox Sidebar = new VBox(10);
        Button dashboardBtn = new Button("Dashboard");
        Button ordersBtn = new Button("Orders");
        Button productsBtn = new Button("Products");

        Sidebar.getChildren().addAll(dashboardBtn, ordersBtn, productsBtn);
        Sidebar.setPrefWidth(150);

        // main content area
        StackPane ContentArea = new StackPane();
        root.setLeft(Sidebar);
        root.setCenter(ContentArea);
        Scene scene = new Scene(root, 800, 600);
        PrimaryStage.setTitle("Juice POS System");
        PrimaryStage.setScene(scene);
        PrimaryStage.show();

        // screens
        VBox dashboardScreen = new VBox(new Label("Dashboard"));
        VBox ordersScreen = new VBox();
        VBox productsScreen = new VBox();
        ContentArea.getChildren().addAll(dashboardScreen, ordersScreen, productsScreen);

        // to show only dashboard only by default
        dashboardScreen.setVisible(true);
        ordersScreen.setVisible(false);
        productsScreen.setVisible(false);

        // button swtching logic

        dashboardBtn.setOnAction(e -> {
            dashboardScreen.setVisible(true);
            ordersScreen.setVisible(false);
            productsScreen.setVisible(false);
        });
        ordersBtn.setOnAction(e -> {
            dashboardScreen.setVisible(false);
            ordersScreen.setVisible(true);
            productsScreen.setVisible(false);

        });
        productsBtn.setOnAction(e -> {
            dashboardScreen.setVisible(false);
            ordersScreen.setVisible(false);
            productsScreen.setVisible(true);

        });

        // orders data we manage locally in UI for now
        ObservableList<Order> orderList = FXCollections.observableArrayList();

        // add sample orders for testing
        Order sample = orderService.createOrder(1);
        orderService.addItem(sample, new Product(1, "Mango Juice", 150.0, 10), 2);
        orderList.add(sample);

        TableView<Order> table = new TableView<>(orderList);

        // Columns
        TableColumn<Order, String> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getOrderId())));

        TableColumn<Order, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(
                data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getTotalAmount())));

        TableColumn<Order, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(
                data.getValue().getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm"))));

        @SuppressWarnings("unchecked")
        TableColumn<Order, String>[] cols = new TableColumn[] { idCol, totalCol, timeCol };
        table.getColumns().addAll(cols);

        ordersScreen.getChildren().add(table);

    }

}