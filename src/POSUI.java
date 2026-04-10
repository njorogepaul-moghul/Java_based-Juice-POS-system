import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class POSUI extends Application {

    private ObservableList<Order> orders = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        // ----- Sidebar -----
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(150);
        Button btnOrders = new Button("Orders");
        Button btnItems = new Button("Items");
        sidebar.getChildren().addAll(btnOrders, btnItems);

        // ----- Orders Table -----
        TableView<Order> ordersTable = new TableView<>();

        TableColumn<Order, String> colOrderId = new TableColumn<>("Order ID");
        colOrderId.setCellValueFactory(cell -> new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getOrderId())));

        TableColumn<Order, String> colItems = new TableColumn<>("Items");
        colItems.setCellValueFactory(cell -> {
            List<OrderItem> itemsList = cell.getValue().getItems();
            String itemsStr = itemsList.stream()
                    .map(OrderItem::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
            return new ReadOnlyStringWrapper(itemsStr);
        });

        TableColumn<Order, String> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(String.valueOf(cell.getValue().getTotalAmount())));

        TableColumn<Order, String> colTimestamp = new TableColumn<>("Timestamp");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        colTimestamp.setCellValueFactory(
                cell -> new ReadOnlyStringWrapper(cell.getValue().getTimestamp().format(formatter)));

        ordersTable.getColumns().addAll(colOrderId, colItems, colTotal, colTimestamp);
        ordersTable.setItems(orders); // bind data dynamically

        // ----- Items Table (placeholder) -----
        TableView itemsTable = new TableView();

        // ----- Main content container -----
        StackPane mainContent = new StackPane();
        mainContent.getChildren().add(ordersTable);

        // ----- Button actions -----
        btnOrders.setOnAction(e -> {
            mainContent.getChildren().clear();
            mainContent.getChildren().add(ordersTable);
        });
        btnItems.setOnAction(e -> {
            mainContent.getChildren().clear();
            mainContent.getChildren().add(itemsTable);
        });

        // ----- Layout -----
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainContent);

        // ----- Scene -----
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Juice POS");
        primaryStage.setScene(scene);
        primaryStage.show();

        // ----- Load sample orders dynamically -----
        loadSampleOrders();
    }

    // ----- Sample data -----
    private void loadSampleOrders() {
        OrderItem item1 = new OrderItem("Apple Juice", 3.5);
        OrderItem item2 = new OrderItem("Orange Juice", 4.0);
        orders.add(new Order(1, List.of(item1, item2), 7.5));
        orders.add(new Order(2, List.of(item2), 4.0));
    }

    public static void main(String[] args) {
        launch();
    }
}