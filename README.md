# Spring MVC — Product Manager

Une application web de gestion de produits développée avec **Spring Boot**, **Spring MVC**, **Thymeleaf** et **Spring Security**.

---

## Table des matières

- [Aperçu](#aperçu)
- [Technologies](#technologies)
- [Structure du projet](#structure-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Sécurité & Rôles](#sécurité--rôles)
- [Entité Product](#entité-product)
- [Routes](#routes)
- [Vues](#vues)
- [Démarrage](#démarrage)
- [Données initiales](#données-initiales)

---

## Aperçu

Application CRUD sécurisée pour gérer un catalogue de produits. Elle inclut une authentification par formulaire, une gestion des rôles USER / ADMIN, l'ajout, l'édition, la suppression simple et la suppression en masse de produits, avec un design épuré et sobre.

---

## Technologies

| Couche       | Technologie                            |
|--------------|----------------------------------------|
| Backend      | Java 17, Spring Boot 3.4.5             |
| Web          | Spring MVC, Thymeleaf Layout Dialect   |
| Sécurité     | Spring Security 6 (InMemory)           |
| Persistance  | Spring Data JPA, H2 / MySQL            |
| Validation   | Jakarta Bean Validation                |
| UI           | Bootstrap 5.3.3 (WebJars)              |
| Utilitaires  | Lombok, WebJars Locator                |

---

## Structure du projet

```
spring_mvc/
│
├── src/main/java/ma/enset/spring_mvc/
│   ├── SpringMvcApplication.java        # Point d'entrée + données initiales
│   ├── entities/
│   │   └── Product.java                 # Entité JPA avec validation
│   ├── repository/
│   │   └── ProductRepository.java       # JpaRepository
│   ├── web/
│   │   └── ProductController.java       # Contrôleur MVC
│   └── sec/
│       └── SecurityConfig.java          # Configuration Spring Security
│
└── src/main/resources/templates/
    ├── layout1.html                     # Template de base (navbar)
    ├── login.html                       # Page de connexion
    ├── products.html                    # Liste des produits
    ├── new-product.html                 # Formulaire d'ajout
    ├── edit-product.html                # Formulaire d'édition
    └── notAuthorized.html               # Page accès refusé (403)
```

---

## Fonctionnalités

- **Authentification** par formulaire avec page de login personnalisée
- **Liste des produits** avec badges de stock (En stock / Limité / Faible)
- **Ajout** d'un produit avec validation des champs (ADMIN)
- **Édition et mise à jour** d'un produit existant (ADMIN)
- **Suppression directe** par ligne avec confirmation native (ADMIN)
- **Suppression en masse** via sélection de cases à cocher (ADMIN)
- **Messages flash** de succès ou d'avertissement après chaque action
- **Déconnexion** avec invalidation de session

---

## Sécurité & Rôles

Authentification gérée en mémoire avec `InMemoryUserDetailsManager` :

| Utilisateur | Mot de passe | Rôles       |
|-------------|--------------|-------------|
| `user1`     | `1234`       | USER        |
| `user2`     | `1234`       | USER        |
| `admin`     | `1234`       | USER, ADMIN |

Les mots de passe sont encodés avec `BCryptPasswordEncoder`.

### Règles d'accès

| URL                       | Accès          |
|---------------------------|----------------|
| `/login`, `/error`        | Public         |
| `/webjars/**`             | Public         |
| `/`, `/user/index`        | Authentifié    |
| `/admin/newProduct`       | ADMIN          |
| `/admin/saveProduct`      | ADMIN          |
| `/admin/editProduct/{id}` | ADMIN          |
| `/admin/updateProduct`    | ADMIN          |
| `/admin/delete`           | ADMIN          |
| `/admin/deleteSelected`   | ADMIN          |

---

## Entité Product

```java
@Entity
public class Product {
    @Id @GeneratedValue
    private Long id;

    @NotEmpty @Size(min = 3, max = 50)
    private String name;

    @Min(0)
    private double price;

    @Min(1)
    private int quantity;
}
```

| Champ      | Type     | Contrainte                   |
|------------|----------|------------------------------|
| `id`       | `Long`   | Auto-généré (clé primaire)   |
| `name`     | `String` | Non vide, 3 à 50 caractères  |
| `price`    | `double` | Valeur minimale : 0          |
| `quantity` | `int`    | Valeur minimale : 1          |

---

## Routes

| Méthode | URL                        | Rôle        | Description                      |
|---------|----------------------------|-------------|----------------------------------|
| GET     | `/` ou `/user/index`       | Authentifié | Liste de tous les produits       |
| GET     | `/admin/newProduct`        | ADMIN       | Formulaire d'ajout               |
| POST    | `/admin/saveProduct`       | ADMIN       | Enregistrer un nouveau produit   |
| GET     | `/admin/editProduct/{id}`  | ADMIN       | Formulaire d'édition             |
| POST    | `/admin/updateProduct`     | ADMIN       | Mettre à jour un produit         |
| POST    | `/admin/delete`            | ADMIN       | Supprimer un produit par ID      |
| POST    | `/admin/deleteSelected`    | ADMIN       | Supprimer plusieurs produits     |
| GET     | `/login`                   | Public      | Page de connexion                |
| GET     | `/logout`                  | Authentifié | Déconnexion                      |
| GET     | `/notAuthorized`           | Tous        | Page accès refusé                |

---

## Vues

| Fichier              | Description                                                       |
|----------------------|-------------------------------------------------------------------|
| `layout1.html`       | Template de base : navbar sombre, badge rôle, lien déconnexion   |
| `login.html`         | Formulaire d'authentification                                     |
| `products.html`      | Tableau produits : badges stock, boutons Éditer / Supprimer / Sélection en masse |
| `new-product.html`   | Formulaire d'ajout avec validation inline                         |
| `edit-product.html`  | Formulaire pré-rempli pour la mise à jour (id caché pour UPDATE)  |
| `notAuthorized.html` | Page 403 personnalisée                                            |

---

## Démarrage

### Prérequis

- Java 17+
- Maven 3.x

### Lancer l'application

```bash
git clone https://github.com/latifa-mtl/angular_framework.git
cd spring_mvc
./mvnw spring-boot:run
```

L'application démarre sur **http://localhost:8080**

Connectez-vous via `http://localhost:8080/login` avec l'un des comptes ci-dessus.

---

## Données initiales

Au démarrage, un `CommandLineRunner` insère automatiquement 3 produits :

| Nom       | Prix (€) | Quantité |
|-----------|----------|----------|
| Product 1 | 100.00   | 3        |
| Product 2 | 200.00   | 4        |
| Product 3 | 500.00   | 12       |

---

## Licence

Projet réalisé à des fins pédagogiques.
