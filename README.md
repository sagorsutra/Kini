# Kini - E-commerce App 🛒

**Kini** is a modern e-commerce application designed to deliver a seamless and engaging shopping experience. With features like dynamic product categorization, special offers, and an event slider, the app is intuitive, user-friendly, and built to enhance user engagement.

---

## Features 📋

### 1. Dynamic Product Categories  
- Products are organized into categories fetched directly from **Firebase Realtime Database**.  
- Categories are displayed in a scrollable **TabLayout** for smooth navigation between different product types.  

### 2. Special Offer Section  
- The **Home** category highlights special products tagged under the `offerCategory` field in Firebase.  
- Users can explore exclusive deals in this section, boosting engagement and sales.  

### 3. Firebase Integration  
- Real-time updates: All data for products, categories, and events are retrieved from **Firebase Realtime Database**.  
- Any updates in the database are instantly reflected in the app.  

### 4. Modern UI/UX  
- User-friendly interface designed with **Material Design Components**.  
- Includes advanced features like a **search bar**, **microphone integration**, and a **scan option** for enhanced usability.  


---

## Project Structure 🗂
(```)**
Kini
│
├── adapter/                     # Adapters for RecyclerView and ViewPager2
│   ├── CategoryAdapter.kt       # Adapter for product categories
│   ├── ProductAdapter.kt        # Adapter for products
│   ├── SliderAdapter.kt         # Adapter for the event slider
│   ├── CartAdapter.kt           # Adapter for displaying cart items
│   ├── OrderAdapter.kt          # Adapter for viewing order history
│
├── fragment/Shopping/           # Fragments for shopping features
│   ├── HomeFragment.kt          # Main fragment for the home screen
│   ├── CategoryFragment.kt      # Fragment for specific category products
│   ├── CartFragment.kt          # Fragment for viewing cart items
│   └── OrdersFragment.kt        # Fragment for viewing order history
│
├── fragment/Admin/              # Admin-specific features
│   ├── AdminOrdersFragment.kt   # Admin interface to manage orders
│
├── model/                       # Data models
│   ├── Category.kt              # Model for categories
│   ├── Product.kt               # Model for products
│   ├── Event.kt                 # Model for event images
│   ├── CartItem.kt              # Model for cart items
│   └── Order.kt                 # Model for orders
│
├── utils/                       # Utility functions and constants
│   └── FirebaseUtils.kt         # Firebase-related helper functions
│
├── res/layout/                  # XML layouts for activities and fragments
│   ├── fragment_home.xml        # Layout for the home fragment
│   ├── category_item.xml        # Layout for category items
│   ├── slider_item.xml          # Layout for the event slider
│   ├── cart_item.xml            # Layout for cart items
│   ├── order_item.xml           # Layout for displaying orders
│
└── MainActivity.kt              # The main entry point of the app
(```)**

## Technologies Used 🛠
### Kotlin: Programming language.
### Firebase Realtime Database: Backend for real-time data fetching.
### ViewPager2: For event image slider.
### RecyclerView: For displaying categories and products.
### Glide: For image loading and caching.
### Material Components: For UI design.

## How It Works 🛠
### 1. Fetching Categories
Categories are retrieved from the Categories node in Firebase and displayed as tabs in the TabLayout.
### 2. Fetching Products
Products are fetched from the Products node in Firebase.
Special products with the offerCategory field set to Special Product are displayed in the Home category.
