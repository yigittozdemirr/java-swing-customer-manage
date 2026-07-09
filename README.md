# 🏢 Müşteri Yönetim Sistemi

> Java Swing tabanlı, MySQL destekli masaüstü CRM (Müşteri İlişkileri Yönetimi) uygulaması.

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Swing](https://img.shields.io/badge/GUI-Java%20Swing-007396?style=for-the-badge&logo=java&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

---

## 📋 İçindekiler

- [Hakkında](#-hakkında)
- [Özellikler](#-özellikler)
- [Mimari](#-mimari)
- [Kurulum](#-kurulum)
- [Kullanım](#-kullanım)
- [Veritabanı Şeması](#-veritabanı-şeması)
- [Proje Yapısı](#-proje-yapısı)
- [Teknolojiler](#-teknolojiler)
- [Ekran Görüntüleri](#-ekran-görüntüleri)
- [Lisans](#-lisans)

---

## 📖 Hakkında

**Müşteri Yönetim Sistemi**, küçük ve orta ölçekli işletmelerin müşteri, ürün ve sipariş süreçlerini tek bir masaüstü uygulamadan yönetmesini sağlayan bir CRM çözümüdür. Kullanıcı dostu Swing arayüzü ile hızlı ve etkili operasyonlar sunar.

---

## ✨ Özellikler

### 🔐 Kullanıcı Yönetimi
- E-posta ve şifre ile güvenli giriş sistemi
- E-posta doğrulaması
- Kullanıcıya özel karşılama ekranı

### 👥 Müşteri Yönetimi
- Müşteri ekleme, güncelleme ve silme (CRUD)
- **Bireysel** ve **Kurumsal** müşteri tipleri
- İsim ve tipe göre filtreleme / arama
- Sağ tık menüsü ile hızlı işlem

### 📦 Ürün Yönetimi
- Ürün ekleme, güncelleme ve silme (CRUD)
- Ürün kodu, fiyat ve stok takibi
- İsim, kod ve stok durumuna göre filtreleme
- Stok kontrolü ile sepete ekleme

### 🛒 Sepet & Sipariş Sistemi
- Ürünleri sepete ekleme (sağ tık menüsü)
- Sepet toplam tutar ve ürün sayısı gösterimi
- Müşteri seçerek sipariş oluşturma
- Sipariş notu ve tarih kaydı
- Sipariş geçmişi görüntüleme

### 🎨 Arayüz
- **Nimbus** Look and Feel teması
- Sekmeli (tabbed) dashboard tasarımı
- Türkçe dil desteği
- Sezgisel popup menüler

---

## 🏗 Mimari

Proje **katmanlı mimari (Layered Architecture)** prensibine göre tasarlanmıştır:

```
┌─────────────────────────────────┐
│          🖥️ View (UI)           │  ← Swing arayüz bileşenleri
├─────────────────────────────────┤
│       ⚙️ Business (Controller)  │  ← İş kuralları & veri doğrulama
├─────────────────────────────────┤
│       💾 DAO (Data Access)      │  ← Veritabanı CRUD işlemleri
├─────────────────────────────────┤
│       📦 Entity (Model)        │  ← Veri taşıma nesneleri
├─────────────────────────────────┤
│       🔧 Core (Utilities)      │  ← DB bağlantısı & yardımcılar
├─────────────────────────────────┤
│         🗄️ MySQL Database       │  ← Kalıcı veri depolama
└─────────────────────────────────┘
```

**Tasarım Desenleri:**
- **Singleton Pattern** → Veritabanı bağlantı yönetimi (`Database.java`)
- **DAO Pattern** → Veri erişim katmanı soyutlaması
- **MVC Pattern** → Model-View-Controller ayrımı

---

## 🚀 Kurulum

### Ön Gereksinimler

| Araç | Minimum Versiyon | Açıklama |
|------|-----------------|----------|
| Java JDK | 17+ | Derleme ve çalıştırma |
| XAMPP | 3.3+ | Apache + MySQL sunucusu |
| IntelliJ IDEA | 2023.1+ | IDE (önerilir) |

### 1. Projeyi Klonlayın

```bash
git clone https://github.com/yigittozdemirr/java-swing-customer-manage.git
cd java-swing-customer-manage
```

### 2. XAMPP'ı Başlatın & Veritabanını Kurun

1. **XAMPP Control Panel**'i açın
2. **Apache** ve **MySQL** servislerini **Start** butonuyla başlatın
3. Tarayıcıda [http://localhost/phpmyadmin](http://localhost/phpmyadmin) adresine gidin
4. **Import** sekmesine tıklayın → `db/customermanage.sql` dosyasını seçin → **Go** butonuna tıklayın

> **Alternatif:** XAMPP Shell üzerinden de çalıştırabilirsiniz:
> ```bash
> mysql -u root < db/customermanage.sql
> ```

### 3. Veritabanı Bağlantısını Yapılandırın

XAMPP varsayılan ayarlarıyla çalışıyorsanız herhangi bir değişiklik yapmanıza **gerek yoktur**.

Özel ayarlarınız varsa `src/core/Database.java` dosyasını güncelleyin:

```java
private final String DB_URL      = "jdbc:mysql://localhost:3306/customermanage";
private final String DB_USERNAME = "root";
private final String DB_PASSWORD = "";  // XAMPP varsayılanı boştur
```

### 4. MySQL Connector/J

Proje, **MySQL Connector/J 9.7.0** kullanmaktadır. JAR dosyası proje dizininde bulunmaktadır.

> IntelliJ IDEA kullanıyorsanız: `File → Project Structure → Libraries → +` ile `mysql-connector-j-9.7.0.jar` dosyasını ekleyin.

### 5. Projeyi Çalıştırın

IntelliJ IDEA'da `src/App.java` dosyasını açıp **Run** butonuna tıklayın.

---

## 💡 Kullanım

1. **Giriş Yapın** → E-posta ve şifre ile sisteme giriş yapın
   - Varsayılan kullanıcı: `admin@patika.dev` / `123123`
2. **Müşteriler** sekmesinden müşteri ekleyin, düzenleyin veya silin
3. **Ürünler** sekmesinden ürün kataloğunuzu yönetin
4. **Sipariş Oluştur** sekmesinde ürünleri sepete ekleyin, müşteri seçin ve siparişi onaylayın
5. **Siparişler** sekmesinden tüm sipariş geçmişini görüntüleyin

---

## 🗄 Veritabanı Şeması

```
┌──────────┐      ┌───────────┐      ┌──────────┐
│   user   │      │  customer │      │ product  │
├──────────┤      ├───────────┤      ├──────────┤
│ id (PK)  │      │ id (PK)   │◄──┐  │ id (PK)  │◄──┐
│ name     │      │ name      │   │  │ name     │   │
│ mail     │      │ type      │   │  │ code     │   │
│ password │      │ phone     │   │  │ price    │   │
└──────────┘      │ mail      │   │  │ stock    │   │
                  │ adress    │   │  └──────────┘   │
                  └───────────┘   │                  │
                                  │                  │
                  ┌───────────┐   │  ┌──────────┐   │
                  │   cart    │   │  │  basket  │   │
                  ├───────────┤   │  ├──────────┤   │
                  │ id (PK)   │   │  │ id (PK)  │   │
                  │ customer_id│───┘  │product_id│───┘
                  │ product_id│───┐  └──────────┘
                  │ price     │   │
                  │ note      │   │
                  │ date      │   │
                  └───────────┘   │
                                  └── product(id)
```

---

## 📁 Proje Yapısı

```
musteri-yonetim-sistemi/
│
├── src/
│   ├── App.java                    # Uygulama giriş noktası
│   │
│   ├── core/                       # Çekirdek yardımcı sınıflar
│   │   ├── Database.java           #   Singleton DB bağlantısı
│   │   ├── Helper.java             #   UI yardımcıları & doğrulama
│   │   └── Item.java               #   ComboBox anahtar-değer çifti
│   │
│   ├── entity/                     # Veri modelleri (POJO)
│   │   ├── User.java               #   Kullanıcı modeli
│   │   ├── Customer.java           #   Müşteri modeli
│   │   ├── Product.java            #   Ürün modeli
│   │   ├── Cart.java               #   Sipariş modeli
│   │   └── Basket.java             #   Sepet modeli
│   │
│   ├── dao/                        # Veri erişim katmanı
│   │   ├── UserDao.java            #   Kullanıcı CRUD
│   │   ├── CustomerDao.java        #   Müşteri CRUD + filtreleme
│   │   ├── ProductDao.java         #   Ürün CRUD + filtreleme
│   │   ├── CartDao.java            #   Sipariş CRUD
│   │   └── BasketDao.java          #   Sepet CRUD
│   │
│   ├── business/                   # İş mantığı katmanı
│   │   ├── UserController.java     #   Kullanıcı iş kuralları
│   │   ├── CustomerController.java #   Müşteri iş kuralları
│   │   ├── ProductController.java  #   Ürün iş kuralları
│   │   ├── CartController.java     #   Sipariş iş kuralları
│   │   └── BasketController.java   #   Sepet iş kuralları
│   │
│   └── view/                       # Arayüz katmanı
│       ├── LoginUI.java            #   Giriş ekranı
│       ├── DashboardUI.java        #   Ana kontrol paneli
│       ├── CustomerUI.java         #   Müşteri ekleme/düzenleme
│       ├── ProductUI.java          #   Ürün ekleme/düzenleme
│       ├── CartUI.java             #   Sipariş oluşturma
│       └── *.form                  #   IntelliJ GUI Designer formları
│
├── db/
│   └── customermanage.sql          # Veritabanı şeması & örnek veri
│
├── mysql-connector-j-9.7.0.jar     # MySQL JDBC sürücüsü
├── .gitignore                      # Git ignore kuralları
├── LICENSE                         # MIT Lisansı
└── README.md                       # Bu dosya
```

---

## 🛠 Teknolojiler

| Teknoloji | Kullanım Amacı |
|-----------|---------------|
| **Java 17+** | Ana programlama dili |
| **Java Swing** | Masaüstü GUI framework |
| **MySQL 8.0+** | İlişkisel veritabanı |
| **JDBC** | Veritabanı bağlantı API'si |
| **MySQL Connector/J 9.7.0** | MySQL JDBC sürücüsü |
| **IntelliJ IDEA GUI Designer** | Form tasarım aracı |
| **Nimbus L&F** | Modern görünüm teması |

---

## 📸 Ekran Görüntüleri

> _Ekran görüntüleri eklenecektir._

<!-- Ekran görüntülerinizi screenshots/ klasörüne koyup aşağıdaki gibi ekleyebilirsiniz:
![Giriş Ekranı](screenshots/login.png)
![Dashboard](screenshots/dashboard.png)
![Müşteri Yönetimi](screenshots/customers.png)
-->

---

## 🤝 Katkıda Bulunma

1. Bu repoyu **fork** edin
2. Feature branch oluşturun (`git checkout -b feature/yeni-ozellik`)
3. Değişikliklerinizi commit edin (`git commit -m 'feat: Yeni özellik eklendi'`)
4. Branch'i push edin (`git push origin feature/yeni-ozellik`)
5. **Pull Request** açın

---

## 📄 Lisans

Bu proje [MIT Lisansı](LICENSE) altında lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakınız.

---

<div align="center">

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!

**Patika.dev** Full Stack Java Geliştirici Eğitimi kapsamında geliştirilmiştir.

</div>
