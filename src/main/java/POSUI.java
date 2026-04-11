import models.Order;
import models.OrderItem;
import models.Product;
import Services.DashboardService;
import Services.OrderService;
import Services.ProductService;
import javafx.beans.property.ReadOnlyStringWrapper;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class POSUI extends Application {

    private OrderService orderService = new OrderService();
    private DashboardService dashboardService = new DashboardService();

    @Override
    public void start(Stage primaryStage) {
        try {
            BorderPane root = new BorderPane();

            // sidebar
            VBox sidebar = new VBox(10);
            Button dashboardBtn = new Button("Dashboard");
            Button ordersBtn = new Button("Orders");
            Button productsBtn = new Button("Products");
            Button newOrderBtn = new Button("New Order");
            sidebar.getChildren().addAll(dashboardBtn, newOrderBtn, ordersBtn, productsBtn);
            sidebar.setPrefWidth(150);

            // main content area
            StackPane contentArea = new StackPane();
            root.setLeft(sidebar);
            root.setCenter(contentArea);
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            sidebar.setId("sidebar");
            contentArea.setId("contentArea");

            primaryStage.setTitle("Juice POS System");
            primaryStage.setScene(scene);
            primaryStage.show();

            // --- Dashboard Screen ---
            VBox dashboardScreen = new VBox(16);
            dashboardScreen.setVisible(true);

            Label salesLabel = new Label("Today's Sales\nKsh " + dashboardService.getTodaySales());
            Label ordersLabel = new Label("Today's Orders\n" + dashboardService.getTodayOrderCount());
            Label topLabel = new Label("Top Product\n" + dashboardService.getTopSellingProduct());

            for (Label card : new Label[] { salesLabel, ordersLabel, topLabel }) {
                card.setStyle(
                        "-fx-background-color: #f0f4ff;" +
                                "-fx-padding: 16;" +
                                "-fx-border-radius: 8;" +
                                "-fx-background-radius: 8;" +
                                "-fx-font-size: 14px;" +
                                "-fx-min-width: 180;");
            }

            HBox statsRow = new HBox(12, salesLabel, ordersLabel, topLabel);

            Label lowStockTitle = new Label("Low Stock Alerts");
            lowStockTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            ObservableList<Product> lowStockList = FXCollections.observableArrayList(
                    dashboardService.getLowStockProducts(5));
            TableView<Product> lowStockTable = new TableView<>(lowStockList);
            lowStockTable.setMaxHeight(160);

            TableColumn<Product, String> lsNameCol = new TableColumn<>("Product");
            lsNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
            TableColumn<Product, String> lsStockCol = new TableColumn<>("Stock Left");
            lsStockCol
                    .setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getStock())));
            TableColumn<Product, String> lsCatCol = new TableColumn<>("Category");
            lsCatCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCategory()));

            @SuppressWarnings("unchecked")
            TableColumn<Product, String>[] lsCols = new TableColumn[] { lsNameCol, lsStockCol, lsCatCol };
            lowStockTable.getColumns().addAll(lsCols);

            Label chartTitle = new Label("Revenue - Last 7 Days");
            chartTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            javafx.scene.chart.CategoryAxis xAxis = new javafx.scene.chart.CategoryAxis();
            javafx.scene.chart.NumberAxis yAxis = new javafx.scene.chart.NumberAxis();
            javafx.scene.chart.BarChart<String, Number> revenueChart = new javafx.scene.chart.BarChart<>(xAxis, yAxis);
            revenueChart.setTitle("Daily Revenue (Ksh)");
            revenueChart.setLegendVisible(false);
            revenueChart.setMaxHeight(220);

            javafx.scene.chart.XYChart.Series<String, Number> series = new javafx.scene.chart.XYChart.Series<>();
            for (double[] point : dashboardService.getWeeklyRevenue()) {
                String day = new java.text.SimpleDateFormat("EEE").format(new java.util.Date((long) point[0]));
                series.getData().add(new javafx.scene.chart.XYChart.Data<>(day, point[1]));
            }
            revenueChart.getData().add(series);

            dashboardScreen.getChildren().addAll(statsRow, lowStockTitle, lowStockTable, chartTitle, revenueChart);

            salesLabel.getStyleClass().add("stat-card");
            ordersLabel.getStyleClass().add("stat-card");
            topLabel.getStyleClass().add("stat-card");
            lowStockTitle.getStyleClass().add("section-title");
            dashboardScreen.setStyle("-fx-padding: 20;");
            chartTitle.getStyleClass().add("section-title");
            dashboardScreen.setStyle("-fx-padding: 20;");
            // wrap statsRow in a ScrollPane or increase card min-height
            salesLabel.setMinHeight(80);
            ordersLabel.setMinHeight(80);
            topLabel.setMinHeight(80);
            ScrollPane dashboardScroll = new ScrollPane(dashboardScreen);
            dashboardScroll.setFitToWidth(true);
            contentArea.getChildren().add(dashboardScroll);

            // --- Orders Screen ---
            VBox ordersScreen = new VBox();
            ObservableList<Order> orderList = FXCollections.observableArrayList(orderService.getAllOrders());

            TableView<Order> table = new TableView<>(orderList);

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

            // --- Products Screen ---
            VBox productsScreen = new VBox(10);
            ProductService productService = new ProductService();
            ObservableList<Product> productList = FXCollections.observableArrayList(productService.getAllProducts());

            TableView<Product> productTable = new TableView<>(productList);

            TableColumn<Product, String> pIdCol = new TableColumn<>("ID");
            pIdCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getId())));
            TableColumn<Product, String> pNameCol = new TableColumn<>("Name");
            pNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));
            TableColumn<Product, String> pPriceCol = new TableColumn<>("Price");
            pPriceCol
                    .setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getPrice())));
            TableColumn<Product, String> pStockCol = new TableColumn<>("Stock");
            pStockCol
                    .setCellValueFactory(data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getStock())));
            TableColumn<Product, String> pCatCol = new TableColumn<>("Category");
            pCatCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getCategory()));

            @SuppressWarnings("unchecked")
            TableColumn<Product, String>[] pCols = new TableColumn[] { pIdCol, pNameCol, pPriceCol, pStockCol,
                    pCatCol };
            productTable.getColumns().addAll(pCols);

            TextField idField = new TextField();
            idField.setPromptText("ID");
            TextField nameField = new TextField();
            nameField.setPromptText("Name");
            TextField priceField = new TextField();
            priceField.setPromptText("Price");
            TextField stockField = new TextField();
            stockField.setPromptText("Stock");
            TextField catField = new TextField();
            catField.setPromptText("Category");
            HBox formRow = new HBox(8, idField, nameField, priceField, stockField, catField);

            Button addBtn = new Button("Add");
            Button editBtn = new Button("Edit");
            Button deleteBtn = new Button("Delete");
            HBox btnRow = new HBox(8, addBtn, editBtn, deleteBtn);

            addBtn.setOnAction(e -> {
                try {
                    Product p = new Product(
                            Integer.parseInt(idField.getText().trim()),
                            nameField.getText().trim(),
                            Double.parseDouble(priceField.getText().trim()),
                            Integer.parseInt(stockField.getText().trim()),
                            catField.getText().trim());
                    productService.addProduct(p);
                    productList.setAll(productService.getAllProducts());
                    idField.clear();
                    nameField.clear();
                    priceField.clear();
                    stockField.clear();
                    catField.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please enter valid numbers for ID, Price and Stock.",
                            Alert.AlertType.ERROR);
                }
            });

            editBtn.setOnAction(e -> {
                Product selected = productTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    idField.setText(String.valueOf(selected.getId()));
                    nameField.setText(selected.getName());
                    priceField.setText(String.valueOf(selected.getPrice()));
                    stockField.setText(String.valueOf(selected.getStock()));
                    catField.setText(selected.getCategory());
                    productService.removeProduct(selected.getId());
                    productList.setAll(productService.getAllProducts());
                }
            });

            deleteBtn.setOnAction(e -> {
                Product selected = productTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    productService.removeProduct(selected.getId());
                    productList.setAll(productService.getAllProducts());
                }
            });

            productsScreen.getChildren().addAll(productTable, formRow, btnRow);
            deleteBtn.getStyleClass().add("button-danger");
            ordersScreen.setStyle("-fx-padding: 20;");
            productsScreen.setStyle("-fx-padding: 20;");

            // --- New Order Screen ---
            VBox newOrderScreen = new VBox(10);
            newOrderScreen.setVisible(false);

            ComboBox<Product> productPicker = new ComboBox<>();
            productPicker.setPromptText("Select product");
            productPicker.setItems(FXCollections.observableArrayList(productService.getAllProducts()));

            TextField qtyField = new TextField();
            qtyField.setPromptText("Quantity");
            qtyField.setPrefWidth(80);
            Button addToOrderBtn = new Button("Add to Order");
            HBox pickerRow = new HBox(8, productPicker, qtyField, addToOrderBtn);

            ObservableList<OrderItem> cartList = FXCollections.observableArrayList();
            TableView<OrderItem> cartTable = new TableView<>(cartList);

            TableColumn<OrderItem, String> cartNameCol = new TableColumn<>("Product");
            cartNameCol.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getProduct().getName()));
            TableColumn<OrderItem, String> cartQtyCol = new TableColumn<>("Qty");
            cartQtyCol.setCellValueFactory(
                    data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getQuantity())));
            TableColumn<OrderItem, String> cartSubCol = new TableColumn<>("Subtotal");
            cartSubCol.setCellValueFactory(
                    data -> new ReadOnlyStringWrapper(String.valueOf(data.getValue().getSubtotal())));

            @SuppressWarnings("unchecked")
            TableColumn<OrderItem, String>[] cartCols = new TableColumn[] { cartNameCol, cartQtyCol, cartSubCol };
            cartTable.getColumns().addAll(cartCols);

            Label totalLabel = new Label("Total: 0.0");
            Button checkoutBtn = new Button("Checkout");
            HBox bottomRow = new HBox(8, totalLabel, checkoutBtn);

            newOrderScreen.getChildren().addAll(pickerRow, cartTable, bottomRow);

            Order[] activeOrder = { orderService.createOrder((int) (Math.random() * 1000)) };

            addToOrderBtn.setOnAction(e -> {
                Product selected = productPicker.getValue();
                if (selected == null)
                    return;
                try {
                    int qty = Integer.parseInt(qtyField.getText().trim());
                    orderService.addItem(activeOrder[0], selected, qty);
                    cartList.setAll(activeOrder[0].getItems());
                    totalLabel.setText("Total: " + activeOrder[0].getTotalAmount());
                    qtyField.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Quantity", "Please enter a valid number for quantity.", Alert.AlertType.ERROR);
                }
            });

            checkoutBtn.setOnAction(e -> {
                if (activeOrder[0].getItems().isEmpty()) {
                    showAlert("Empty Order", "Please add at least one item before checking out.",
                            Alert.AlertType.WARNING);
                    return;
                }
                orderService.checkout(activeOrder[0]);

                // --- Receipt dialog ---
                StringBuilder receipt = new StringBuilder("===== RECEIPT =====\n\n");
                for (OrderItem item : activeOrder[0].getItems()) {
                    receipt.append(item.getProduct().getName())
                            .append(" x").append(item.getQuantity())
                            .append(" = Ksh ").append(item.getSubtotal())
                            .append("\n");
                }
                receipt.append("\n-------------------")
                        .append("\nTOTAL: Ksh ").append(activeOrder[0].getTotalAmount())
                        .append("\n===================");

                showAlert("Checkout Complete", receipt.toString(), Alert.AlertType.INFORMATION);

                cartList.clear();
                activeOrder[0] = orderService.createOrder((int) (Math.random() * 1000));
                totalLabel.setText("Total: 0.0");
            });
            newOrderScreen.setStyle("-fx-padding: 20;");

            // --- Add all screens to content area ---
            contentArea.getChildren().addAll(dashboardScreen, ordersScreen, productsScreen, newOrderScreen);

            // --- Default visibility ---
            ordersScreen.setVisible(false);
            productsScreen.setVisible(false);
            newOrderScreen.setVisible(false);

            // --- Button switching logic ---
            dashboardBtn.setOnAction(e -> {
                dashboardScreen.setVisible(true);
                ordersScreen.setVisible(false);
                productsScreen.setVisible(false);
                newOrderScreen.setVisible(false);
            });
            ordersBtn.setOnAction(e -> {
                dashboardScreen.setVisible(false);
                ordersScreen.setVisible(true);
                productsScreen.setVisible(false);
                newOrderScreen.setVisible(false);
            });
            productsBtn.setOnAction(e -> {
                dashboardScreen.setVisible(false);
                ordersScreen.setVisible(false);
                productsScreen.setVisible(true);
                newOrderScreen.setVisible(false);
            });
            newOrderBtn.setOnAction(e -> {
                dashboardScreen.setVisible(false);
                ordersScreen.setVisible(false);
                productsScreen.setVisible(false);
                newOrderScreen.setVisible(true);
            });
            totalLabel.getStyleClass().add("total-label");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
