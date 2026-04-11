
package Services;

import models.Product;
import repository.ProductRepository;

import java.util.List;

public class ProductService {
    private ProductRepository repo = new ProductRepository();

    public void addProduct(Product product) {
        repo.addProduct(product);
    }

    public List<Product> getAllProducts() {
        return repo.getAllProducts();
    }

    public void removeProduct(int id) {
        repo.deleteProduct(id);
    }

    public void updateProduct(Product p) {
        repo.updateProduct(p);
    }

    public Product getProductById(int id) {
        return getAllProducts().stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
}