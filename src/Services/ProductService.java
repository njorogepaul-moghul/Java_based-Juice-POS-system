package models;

public class ProductService {
    private List<Product> products = new ArrayList<>();
    public void add

    product(Product product){
        products.add(product)
    }

    public List<Product> getAllProducts() {
        return products;
    }
}
