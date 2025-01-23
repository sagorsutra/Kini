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
Kini
│
├── adapter/                     # Adapters for RecyclerView and ViewPager2
│   ├── CategoryAdapter.kt       # Adapter for product categories
│   ├── ProductAdapter.kt        # Adapter for products
│   └── SliderAdapter.kt         # Adapter for the event slider
│
├── fragment/Shopping/           # Fragments for shopping features
│   ├── HomeFragment.kt          # Main fragment for the home screen
│   └── CategoryFragment.kt      # Fragment for specific category products
│
├── model/                       # Data models
│   ├── Category.kt              # Model for categories
│   ├── Product.kt               # Model for products
│   └── Event.kt                 # Model for event images
│
├── utils/                       # Utility functions and constants
│   └── FirebaseUtils.kt         # Firebase-related helper functions
│
├── res/layout/                  # XML layouts for activities and fragments
│   ├── fragment_home.xml        # Layout for the home fragment
│   ├── category_item.xml        # Layout for category items
│   └── slider_item.xml          # Layout for the event slider
│
└── MainActivity.kt              # The main entry point of the app
