package ma.enset.spring_mvc.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ma.enset.spring_mvc.entities.Product;
import ma.enset.spring_mvc.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping({"/", "/user/index"})
    public String index(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("productList", products);
        return "products";
    }

    @GetMapping("/admin/newProduct")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        return "new-product";
    }

    @PostMapping("/admin/saveProduct")
    public String saveProduct(@Valid Product product,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "new-product";
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("successMsg", "Produit enregistré avec succès !");
        return "redirect:/user/index";
    }

    @GetMapping("/admin/editProduct/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable : " + id));
        model.addAttribute("product", product);
        return "edit-product";
    }

    @PostMapping("/admin/updateProduct")
    public String updateProduct(@Valid Product product,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) return "edit-product";
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("successMsg", "Produit mis à jour avec succès !");
        return "redirect:/user/index";
    }

    @PostMapping("/admin/delete")
    public String delete(@RequestParam Long id,
                         RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMsg", "Produit supprimé.");
        return "redirect:/user/index";
    }

    @PostMapping("/admin/deleteSelected")
    public String deleteSelected(
            @RequestParam(value = "selectedIds", required = false) List<Long> ids,
            RedirectAttributes redirectAttributes) {
        if (ids != null && !ids.isEmpty()) {
            productRepository.deleteAllById(ids);
            redirectAttributes.addFlashAttribute("successMsg", ids.size() + " produit(s) supprimé(s).");
        } else {
            redirectAttributes.addFlashAttribute("warnMsg", "Aucun produit sélectionné.");
        }
        return "redirect:/user/index";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized() { return "notAuthorized"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "login";
    }
}