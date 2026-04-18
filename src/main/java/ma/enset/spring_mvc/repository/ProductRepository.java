package ma.enset.spring_mvc.repository;

import ma.enset.spring_mvc.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Recherche par nom (insensible à la casse)
    List<Product> findByNameContainingIgnoreCase(String keyword);
}