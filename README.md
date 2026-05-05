# LOFO - Lost and Found App

## 📱 Project Description
**LOFO** is an innovative Android application designed to streamline the process of reuniting individuals with their lost belongings, addressing a common problem faced in college communities. Lost and found items are typically managed through a manual and time-consuming email system, resulting in a cluttered and inefficient process.

Our project aims to simplify this ordeal by creating a user-friendly, intuitive app that centralizes the lost and found experience. Users can easily report lost items, providing essential details and descriptions, while those who have found items can upload information about the discovered belongings.

---

## 🏗️ System Architecture
The project follows the **MVVM (Model-View-ViewModel)** architectural pattern to ensure separation of concerns, scalability, and maintainability.

- **View (UI Layer):** Fragments and Activities (e.g., `CreatePostFragment`, `DashBoardFragment`) handle the UI and user interactions.
- **ViewModel:** Acts as a bridge between the View and the Model. It manages UI-related data and communicates with the repository/API layer.
- **Model (Data Layer):** Data models (e.g., `Item.java`) and the `ApiService` interface define the data structures and network operations.
- **Communication:** Uses **Retrofit** for making RESTful API calls to the Node.js backend.
- **Image Management:** **Cloudinary** is integrated for cloud-based image storage and transformation.

---

## 🔗 API Documentation
The application communicates with a Node.js backend via a RESTful API.

### Authentication Endpoints
- `POST /api/login` - Authenticates a user and returns a token.
- `POST /api/register` - Registers a new user account.
- `PATCH /api/update-profile` - Updates the current user's profile details.

### Item Management Endpoints
- `GET /api/items` - Fetches all items, filterable by type (`lost` or `found`).
- `GET /api/items/{id}` - Retrieves detailed information for a specific item.
- `GET /api/user-items` - Fetches all items posted by the authenticated user.
- `POST /api/items` - Creates a new lost or found item report.
- `PATCH /api/items/{id}/status` - Updates the status of an item (e.g., "Found" or "Returned").
- `DELETE /api/items/{id}` - Removes an item entry from the system.

---

## 📊 Database Schema (Items Table)
The core of the system revolves around the **Items** entity, stored in the backend database.

| Field | Type | Description |
| :--- | :--- | :--- |
| `item_id` | Integer | Primary Key |
| `user_id` | Integer | Foreign Key to User |
| `type` | String | 'lost' or 'found' |
| `item_name` | String | Name/Title of the item |
| `category` | String | Category (Electronics, Books, etc.) |
| `date` | String | Date of loss/discovery |
| `time` | String | Time of loss/discovery |
| `location` | String | Specific location coordinates or name |
| `description`| String | Detailed text description |
| `image_url` | String | Cloudinary URL for the item image |
| `status` | String | Current status (Pending, Resolved, etc.) |
| `contact_phone`| String | Contact number for inquiries |

---

## ✨ Features
1. **User Authentication:** Secure login and registration.
2. **Dynamic Dashboard:** Real-time view of recent lost and found items.
3. **Item Reporting:** Comprehensive forms for posting lost or found items with image support.
4. **My Items Management:** Dedicated section to track and manage personal posts.
5. **Direct Communication:** One-tap Call and SMS integration to contact finders/owners.
6. **Profile Customization:** User-specific profiles with editable contact information.
7. **Search & Filter:** Easily find items by category or location.

---

## 🛠️ Tech Stack
- **Frontend:** Java (Android SDK)
- **Architecture:** MVVM
- **Networking:** Retrofit 2, OkHttp 3
- **Backend:** Node.js (REST API)
- **Image Storage:** Cloudinary
- **Database:**  Aiven
- **UI Components:** Material Design 3, Lottie Animations, Glide

---

## 📖 User Manual
### Getting Started
1. **Installation:** Install the provided APK.
2. **Registration:** Create an account using your email and phone number.
3. **Reporting:** Use the '+' button to report a lost or found item. Fill in the details and upload a photo.
4. **Browsing:** Check the Dashboard or specific categories to look for matches.
5. **Contacting:** Tap on an item and use the 'Call' or 'Message' buttons to reach out.

---

**Version:** 1.0  
**Last Updated:** 2024
