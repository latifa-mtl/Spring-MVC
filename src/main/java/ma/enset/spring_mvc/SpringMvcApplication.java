package ma.enset.spring_mvc;



import ma.enset.spring_mvc.entities.Product;
import ma.enset.spring_mvc.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication(exclude =  SecurityAutoConfiguration.class)
@SpringBootApplication
public class SpringMvcApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringMvcApplication.class, args);

    }

    @Bean
    public CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder()
                    .name("Product 1")
                    .price(100.0)
                    .quantity(3)
                    .build());
            productRepository.save(Product.builder()
                    .name("Product 2")
                    .price(200.0)
                    .quantity(4)
                    .build());
            productRepository.save(Product.builder()
                    .name("Product 3")
                    .price(500.0)
                    .quantity(12)
                    .build());
            productRepository.findAll().forEach(p-> {
                System.out.println(p.toString());
            });
        };
    }

}
