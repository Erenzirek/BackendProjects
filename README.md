# FilmApp Backend Projesi

Bu proje, FilmApp uygulamasının backend kısmını içermektedir. Java Spring Boot kullanılarak geliştirilmiş olup, MongoDB veritabanı ile veri yönetimi yapılmaktadır. Projede kullanıcılar film favorilerine ekleme, yorum yapma gibi işlemleri gerçekleştirebilir.

---

## Proje Paket Mimarisi ve MVC Yapısı

Proje, modüler ve anlaşılır olması amacıyla farklı paketlere ayrılmıştır. Bu sayede kod organizasyonu ve bakım kolaylaşır. Kullanılan temel paketler şunlardır:

- **config:** Projenin genel ayarlarının ve yapılandırmalarının (örneğin güvenlik ayarları, CORS, vs.) bulunduğu paket.
- **controller:** API isteklerini karşılayan ve yönlendiren katman. HTTP istekleri burada karşılanır, servis ve repository katmanına yönlendirilir.
- **dto:** Veri transfer nesneleri. İstemci ile sunucu arasında veri alışverişinde kullanılan, genellikle isteklere veya yanıtlara özel oluşturulan sınıflar.
- **model:** Veritabanı yapısını yansıtan varlık sınıfları (entity). MongoDB doküman yapısına uygun modeller burada tanımlanır.
- **repository:** MongoDB gibi veri kaynakları ile iletişimi sağlayan katman. CRUD operasyonları burada gerçekleştirilir.
- **security:** Proje güvenliğini sağlayan paket. JWT tabanlı kimlik doğrulama ve yetkilendirme işlemleri burada yönetilir.

Bu mimari, **Model-View-Controller (MVC)** prensiplerine uygun olarak tasarlanmıştır ve kodun sürdürülebilirliğini, genişletilebilirliğini sağlar.

---

## Proje Özellikleri

- Kullanıcı Kayıt ve Giriş Sistemi (JWT ile güvenli)
- Film Listeleme ve Detay Görüntüleme
- Kullanıcıların favori film ekleme ve kaldırma işlemleri
- Film yorumları yazma ve listeleme
- MongoDB ile veri yönetimi

---

## Görsel: Proje Paket Mimarisi

Aşağıdaki şemada proje paketlerinin birbirleriyle ilişkisi ve işleyişi gösterilmektedir:

![Proje Paket Mimarisi](architecture-diagram.png)

---

## README Dosyasını Projeye Ekleme

README.md dosyasını, backend projesinin ana dizinine (root dizin) eklemelisiniz.  
Genellikle bu dizin, `pom.xml` dosyasının bulunduğu yer olur.

Dosya yolu örneği:

## User Modeli (`User.java`)

Projemize kullanıcı yönetimi için temel veri yapısını tanımlayan **User** modelini oluşturarak başladık.

Bu model, MongoDB’deki **users** koleksiyonuna karşılık gelir ve kullanıcıların sistemde tutulması gereken temel bilgileri içerir:

- **id:** MongoDB tarafından otomatik oluşturulan benzersiz kullanıcı kimliği.
- **email:** Kullanıcının e-posta adresi, giriş ve iletişim için kullanılır.
- **password:** Kullanıcının şifre bilgisi, güvenliğin temelidir.
- **username:** Kullanıcı adı, uygulama içinde kullanıcıyı tanımlamak için kullanılır.

`@Document` annotasyonu ile bu sınıfın MongoDB koleksiyonu olduğunu belirtiyoruz, `@Id` ile de benzersiz kimlik alanı işaretleniyor.

Bu model, kullanıcı kayıt, giriş ve kimlik doğrulama süreçlerinin temelini oluşturur ve uygulamanın güvenli ve düzenli çalışmasını sağlar.

```java
@Getter
@Setter
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String username;
}
```
### Getter ve Setter Anotasyonları

Modelde **@Getter** ve **@Setter** anotasyonlarını kullanmamızın sebebi, Java sınıfındaki alanlara (field'lara) otomatik olarak **getter** ve **setter** metodlarının oluşturulmasını sağlamaktır.

- **Getter** metodları, bir nesnenin alanlarının değerlerini dışarıya okumak için kullanılır.
- **Setter** metodları ise alanların değerlerinin değiştirilmesine izin verir.

Bu anotasyonlar **Lombok** kütüphanesine aittir ve kodun daha temiz, okunabilir ve kısa olmasını sağlar. Böylece elle getter/setter yazmak zorunda kalmayız ve hata yapma ihtimalimiz azalır.

Özetle, bu sayede:

- Model sınıfımızda alanların erişim ve değişimi için gereken metodlar otomatik üretilir.
- Kod kalabalığı ve tekrarı önlenir.
- Proje daha düzenli ve bakımı kolay olur.

## UserRepository (Veri Erişim Katmanı)
UserRepository, MongoDB'deki `users` koleksiyonuna erişim sağlayan veri erişim (repository) katmanıdır.

`MongoRepository<User, String>` kullanılarak, User modeli ve ID tipi (String) belirtilmiştir.

Bu sayede, temel CRUD (Create, Read, Update, Delete) işlemleri için standart metodlar otomatik olarak sağlanır.

Ek olarak, kullanıcıların email adresine göre sorgulama yapabilmek için `findByEmail` metodu tanımlanmıştır. Bu metod, kullanıcı giriş doğrulama gibi işlemlerde email bazlı kullanıcı bulmayı sağlar.

Repository katmanı, veritabanı işlemlerini soyutlayarak uygulamanın diğer katmanlarında kolay ve tutarlı veri erişimi sağlar.
```java
package com.filmapp.filmapp_backend.repository;

import com.filmapp.filmapp_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
```
## AuthController (Kullanıcı Kimlik Doğrulama ve Kayıt)

`AuthController`, kullanıcıların kayıt ve giriş işlemlerini yöneten API katmanıdır.

- **@RestController** ve **@RequestMapping("/api/auth")** anotasyonlarıyla, bu controller'a `/api/auth` altındaki istekler yönlendirilir.
- **@CrossOrigin(origins = "http://localhost:5173")** ile React frontend uygulamasından gelen isteklerin CORS politikası nedeniyle engellenmemesi sağlanır.

### Kayıt İşlemi (`/register`)

- `POST /api/auth/register` endpoint'i ile yeni kullanıcı kaydı yapılır.
- Gelen kullanıcı bilgisi içinde e-posta adresi öncelikle kullanıcı veri tabanında sorgulanır; eğer varsa hata döner.
- Şifre, `BCryptPasswordEncoder` kullanılarak güvenli şekilde hashlenir.
- Kullanıcı bilgisi veritabanına kaydedilir.
- Başarılı kayıt durumunda 200 OK ve başarı mesajı döner.

### Giriş İşlemi (`/login`)

- `POST /api/auth/login` endpoint'i ile kullanıcı giriş yapar.
- Girilen email ile kullanıcı veritabanında aranır.
- Eğer kullanıcı varsa, girilen şifre ile kayıtlı hashlenmiş şifre `BCryptPasswordEncoder` ile karşılaştırılır.
- Doğru ise kullanıcı bilgileri döner, aksi halde 401 Unauthorized hata mesajı gönderilir.

---

### AuthController Kodu

```java
package com.filmapp.filmapp_backend.controller;

import com.filmapp.filmapp_backend.model.User;
import com.filmapp.filmapp_backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // React için CORS ayarı
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Kullanıcı kayıt işlemi
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // Kullanıcı giriş işlemi
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> userOpt = userRepository.findByEmail(loginUser.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (new BCryptPasswordEncoder().matches(loginUser.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(user);
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
```
## 🧪 Postman ile API Testi

### 📮 Kullanıcı Kayıt ve Giriş İşlemleri

#### 🔹 1. Kullanıcı Kaydı (Register)

- **HTTP Method:** `POST`  
- **URL:** `http://localhost:9090/api/auth/register`  
- **Headers:**
  - `Content-Type: application/json`
- **Request Body (raw > JSON):**
  ```json
  {
    "email": "test@example.com",
    "password": "123456",
    "username": "testuser"
  }
  ```
## 🎬 Film Modeli (Film.java)

### 📌 Amaç
`Film` sınıfı, her bir film bilgisini temsil eden modeldir. MongoDB veritabanında `films` adlı koleksiyona karşılık gelir. Bu sınıf sayesinde film bilgileri düzenli bir yapıda saklanır ve Spring Boot ile kolayca işlenebilir.

---

### 🧠 Neden Model Sınıfı Olarak Tanımladık?

- MongoDB'de bir belgeyi (document) temsil eder.
- Her film için başlık, poster, tür, fragman linki gibi bilgileri tek bir yerde toplar.
- Spring Boot, bu sınıfı kullanarak veritabanıyla otomatik veri alışverişi yapar.
- CRUD (oluşturma, okuma, güncelleme, silme) işlemleri bu yapı sayesinde kolaylaşır.

---

### 📁 Kod İncelemesi

```java
package com.filmapp.filmapp_backend.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "films")
public class Film {

    @Id
    private ObjectId id;

    private String imdbId;
    private String title;
    private String releaseDate;
    private String trailerLink;
    private String poster;
    private List<String> backdrops;
    private List<String> genres;
    private double averageRating = 0.0;
}
```

## 🗃️ FilmRepository – Spring Data MongoDB

`AuthController`, kullanıcıların kayıt ve giriş işlemlerini yöneten API katmanıdır.

- `FilmRepository`, MongoDB veritabanı ile film verileri arasında bağlantı kurmamızı sağlayan bir **veri erişim katmanıdır (DAO)**. Spring Data MongoDB tarafından otomatik olarak implement edilir.
- @RestController
  Bu anotasyon, sınıfın REST API endpoint’lerini yöneten bir controller olduğunu belirtir. HTTP isteklerini karşılar ve JSON formatında yanıt döner.
- @RequestMapping("/api/films")
  Bu controller'a /api/films URL yolu üzerinden erişileceğini belirtir. Tüm metotların temel yolu budur.

- @CrossOrigin(origins = "http://localhost:5173")
React frontend uygulamasının çalıştığı http://localhost:5173 adresinden gelen isteklerin kabul edilmesini sağlar. Böylece CORS (Cross-Origin Resource Sharing) hatası önlenir.

- private final FilmRepository filmRepository;
Film verilerine erişmek için kullanılan repository nesnesi. Veri tabanı işlemleri bu katmanda yapılır.

- Constructor Injection
FilmRepository nesnesi, controller’a yapıcı metod (constructor) ile enjekte edilir. Bu, bağımlılıkların yönetimi ve test edilebilirlik için en iyi yöntemdir.


### 📁 Kod İncelmesi
```java
package com.filmapp.filmapp_backend.controller;

import com.filmapp.filmapp_backend.model.Film;
import com.filmapp.filmapp_backend.repository.FilmRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@CrossOrigin(origins = "http://localhost:5173")
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @GetMapping("/{imdbId}")
    public Film getFilmByImdbId(@PathVariable String imdbId) {
        return filmRepository.findByImdbId(imdbId);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        return filmRepository.save(film);
    }
}
```
### Kayıt İşlemi (`/register`)

- `POST /api/auth/register` endpoint'i ile yeni kullanıcı kaydı yapılır.
- Gelen kullanıcı bilgisi içinde e-posta adresi öncelikle kullanıcı veri tabanında sorgulanır; eğer varsa hata döner.
- Şifre, `BCryptPasswordEncoder` kullanılarak güvenli şekilde hashlenir.
- Kullanıcı bilgisi veritabanına kaydedilir.
- Başarılı kayıt durumunda 200 OK ve başarı mesajı döner.

## application.properties Açıklamaları

Bu dosya, Spring Boot uygulamanızın temel yapılandırma ayarlarını içerir.

---

### MongoDB Bağlantısı 
- **spring.data.mongodb.uri**: Uygulamanın MongoDB veritabanına bağlanması için kullanılan bağlantı URI'si.
- `mongodb://localhost:27017/films` ifadesi, yerel makinede çalışan MongoDB’nin 27017 portundaki `films` adlı veritabanına bağlanılacağını belirtir.

---

### Sunucu Portu
- **server.port**: Spring Boot uygulamasının dinleyeceği HTTP portunu belirler.
- Burada `9090` portu seçilmiştir, yani API istekleri bu port üzerinden alınır.

---
### CORS (Cross-Origin Resource Sharing) Ayarları
- **CORS**, farklı kaynak (origin) domain’lerden gelen web isteklerinin engellenmesini veya izin verilmesini kontrol eden bir güvenlik mekanizmasıdır.
- `allowed-origins`: Hangi adreslerin API’ye istek gönderebileceğini belirler. Burada React frontend uygulamanızın çalıştığı `http://localhost:5173` adresi izinli kılınmıştır.
- `allowed-methods`: İzin verilen HTTP metotları (GET, POST, PUT, DELETE, OPTIONS) listelenmiştir.
- Bu ayarlar, frontend ve backend ayrı portlarda çalışırken tarayıcıda oluşan CORS hatalarını önler ve frontend'in backend API’sine erişmesini sağlar.

---

### Özet

- MongoDB bağlantısı yapılandırılmış,
- Backend uygulama portu belirlenmiş,
- Frontend’den gelen API çağrıları için CORS izinleri tanımlanmıştır.

Bu sayede backend uygulamanız güvenli ve sorunsuz çalışır.
