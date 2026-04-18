# 🛍️ Spring MVC Product Manager

Une application web full-stack de gestion de produits développée avec **Spring Boot**, **Spring MVC**, **Thymeleaf** et **Spring Security**.

---

## 📋 Table des matières

- [Aperçu](#aperçu)
- [Technologies utilisées](#technologies-utilisées)
- [Structure du projet](#structure-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Sécurité & Rôles](#sécurité--rôles)
- [Entité Product](#entité-product)
- [Routes & Contrôleur](#routes--contrôleur)
- [Vues Thymeleaf](#vues-thymeleaf)
- [Démarrage](#démarrage)
- [Données initiales](#données-initiales)

---

## Aperçu

Cette application permet de gérer un catalogue de produits avec une interface web sécurisée. Elle inclut une authentification par formulaire, une gestion des rôles (USER / ADMIN), ainsi que des opérations CRUD sur les produits (affichage, ajout, suppression).

---

## Technologies utilisées

| Couche        | Technologie                          |
|---------------|--------------------------------------|
| Backend       | Java 17+, Spring Boot                |
| Web           | Spring MVC, Thymeleaf                |
| Sécurité      | Spring Security (InMemory)           |
| Persistance   | Spring Data JPA, Jakarta Persistence |
| Validation    | Jakarta Bean Validation              |
| UI            | Bootstrap 5.3.6 (via WebJars)        |
| Utilitaires   | Lombok                               |

---

## Structure du projet

```
spring_mvc/
│
├── src/main/java/ma/enset/spring_mvc/
│   ├── SpringMvcApplication.java       # Point d'entrée + données initiales
│   ├── entities/
│   │   └── Product.java                # Entité JPA avec validation
│   ├── repository/
│   │   └── ProductRepository.java      # Interface JpaRepository
│   ├── web/
│   │   └── ProductController.java      # Contrôleur MVC
│   └── sec/
│       └── SecurityConfig.java         # Configuration Spring Security
│
└── src/main/resources/templates/
    ├── layout1.html                    # Template de mise en page (navbar)
    ├── login.html                      # Page de connexion
    ├── products.html                   # Liste des produits
    ├── new-product.html                # Formulaire d'ajout
    └── notAuthorized.html              # Page accès refusé
```

---

## Fonctionnalités

- 🔐 **Authentification** par formulaire avec page de login personnalisée
- 📋 **Liste des produits** accessible à tous les utilisateurs connectés
- ➕ **Ajout de produit** réservé aux administrateurs (avec validation des champs)
- 🗑️ **Suppression de produit** réservée aux administrateurs
- 🚫 **Page "Non autorisé"** affichée en cas d'accès refusé
- 🔒 **Déconnexion** avec invalidation de session

---

## Sécurité & Rôles

La sécurité est gérée en mémoire (`InMemoryUserDetailsManager`) avec les comptes suivants :

| Utilisateur | Mot de passe | Rôles        |
|-------------|--------------|--------------|
| `user1`     | `1234`       | USER         |
| `user2`     | `1234`       | USER         |
| `admin`     | `1234`       | USER, ADMIN  |

### Règles d'accès

| URL                    | Accès requis          |
|------------------------|-----------------------|
| `/login`, `/error`     | Public                |
| `/webjars/**`          | Public                |
| `/user/index`, `/`     | Authentifié           |
| `/admin/newProduct`    | ADMIN                 |
| `/admin/saveProduct`   | ADMIN                 |
| `/admin/delete`        | ADMIN                 |

> Les mots de passe sont encodés avec **BCryptPasswordEncoder**.

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

| Champ      | Type     | Contrainte                  |
|------------|----------|-----------------------------|
| `id`       | `Long`   | Auto-généré (clé primaire)  |
| `name`     | `String` | Non vide, 3 à 50 caractères |
| `price`    | `double` | Valeur minimale : 0         |
| `quantity` | `int`    | Valeur minimale : 1         |

---

## Routes & Contrôleur

| Méthode  | URL                    | Rôle requis | Description                        |
|----------|------------------------|-------------|------------------------------------|
| `GET`    | `/` ou `/user/index`   | USER        | Afficher la liste des produits     |
| `GET`    | `/admin/newProduct`    | ADMIN       | Afficher le formulaire d'ajout     |
| `POST`   | `/admin/saveProduct`   | ADMIN       | Enregistrer un nouveau produit     |
| `POST`   | `/admin/delete`        | ADMIN       | Supprimer un produit par ID        |
| `GET`    | `/login`               | Public      | Afficher la page de connexion      |
| `GET`    | `/logout`              | Authentifié | Déconnecter l'utilisateur          |
| `GET`    | `/notAuthorized`       | Tous        | Afficher la page accès refusé      |

---

## Vues Thymeleaf

| Fichier              | Description                                              |
|----------------------|----------------------------------------------------------|
| `layout1.html`       | Template de base avec navbar Bootstrap et menu utilisateur |
| `login.html`         | Formulaire d'authentification                            |
| `products.html`      | Tableau des produits (bouton Supprimer visible pour ADMIN) |
| `new-product.html`   | Formulaire d'ajout avec validation et messages d'erreur  |
| `notAuthorized.html` | Page d'erreur 403 personnalisée                          |

---

## Démarrage

### Prérequis

- Java 17+
- Maven 3.x

### Lancer l'application

```bash
# Cloner le dépôt
git clone https://github.com/latifa-mtl/angular_framework.git
cd spring_mvc

# Compiler et démarrer
./mvnw spring-boot:run
```

L'application sera accessible à : **http://localhost:8080**

### Se connecter

Rendez-vous sur `http://localhost:8080/login` et utilisez l'un des comptes ci-dessus.

---

## Données initiales

Au démarrage, l'application insère automatiquement 3 produits en base via un `CommandLineRunner` :

| Nom        | Prix  | Quantité |
|------------|-------|----------|
| Product 1  | 100.0 | 3        |
| Product 2  | 200.0 | 4        |
| Product 3  | 500.0 | 12       |

