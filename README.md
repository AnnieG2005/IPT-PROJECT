# LOFO - Lost and Found App

## 📱 Project Description

**LOFO** is an innovative Android application designed to streamline the process of reuniting individuals with their lost belongings, addressing a common problem faced in college communities. Lost and found items are typically managed through a manual and time-consuming email system, resulting in a cluttered and inefficient process.

Our project aims to simplify this ordeal by creating a user-friendly, intuitive app that centralizes the lost and found experience. Users can easily report lost items, providing essential details and descriptions, while those who have found items can upload information about the discovered belongings.

LOFO promotes a more organized and accessible way of managing lost items, reducing the clutter in email inboxes, and fostering community by reuniting individuals with their cherished belongings. This project intends to enhance the college experience by providing a reliable and user-friendly platform for lost and found items.

---

## 🛠️ Tech Stack

### Programming Languages
- **Java** (Java 17) - Primary programming language for Android development

### Development Tools
- **Android Studio** - Integrated Development Environment (IDE)
- **Gradle** (8.6.0) - Build automation tool
- **Android Gradle Plugin** (8.6.0) - Android build system
- **Git** - Version control system

### Backend & Database
- **Firebase Authentication** - User authentication and authorization
- **Firebase Firestore** - NoSQL cloud database for storing items and user data
- **Firebase Storage** - Cloud storage for images and files
- **Firebase Realtime Database** - Real-time data synchronization
- **Firebase Analytics** - App usage analytics
- **Cloudinary** - Cloud-based image and video management service

### UI/UX Libraries & Frameworks
- **Material Design Components** (1.12.0) - Google's Material Design UI components
- **AndroidX AppCompat** (1.7.0) - Backward compatibility library
- **AndroidX Core KTX** (1.15.0) - Kotlin extensions for Android framework
- **ConstraintLayout** (2.2.0) - Flexible layout manager
- **CardView** (1.0.0) - Material Design card container
- **Navigation Component** (2.8.4) - Navigation between destinations
- **View Binding** - Type-safe view references

### Image Processing & Display
- **Glide** (4.16.0) - Image loading and caching library
- **ImageSlideshow** (0.1.0) - Image slideshow component
- **CircularImageView** (4.3.1) - Circular image view for profile pictures

### Architecture & Design Patterns
- **MVVM (Model-View-ViewModel)** - Architectural pattern
- **Lifecycle Components** (2.8.7) - Lifecycle-aware components
- **LiveData** - Observable data holder
- **ViewModel** - UI-related data holder

### Networking & HTTP
- **Android Async HTTP** (1.4.9) - Asynchronous HTTP client

### Authentication
- **AndroidX Credentials** (1.5.0) - Credential management API
- **Google Identity Services** (1.1.1) - Google Sign-In integration
- **Play Services Auth** (1.5.0) - Google Play Services authentication

### Firebase UI
- **Firebase UI Firestore** (8.0.2) - UI bindings for Firestore

### Platform Specifications
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35
- **Java Version**: 17

---

## ✨ Features

### 1. User Registration and Authentication
- Secure user registration and authentication system
- Firebase Authentication integration
- User profile management with editable information

### 2. Lost Item Reporting
- Easy-to-use form for reporting lost items
- Fields include:
  - Item name
  - Category selection
  - Date and time of loss
  - Location where item was lost
  - Detailed description
  - Image upload capability

### 3. Found Item Submission
- Simple form for submitting found items
- Fields include:
  - Item name
  - Category selection
  - Date and time of discovery
  - Location where item was found
  - Detailed description
  - Image upload capability

### 4. Item Listings and Search
- Browse all lost and found items
- Search functionality by category, date, and location
- View detailed information for each item
- Dashboard showing recent items

### 5. My Items Section
- View all items you've posted (lost or found)
- Track your submissions in one place

### 6. Communication Features
- Direct contact information display
- Call and SMS functionality to connect with item owners/finders
- User contact details (name, phone, email) available for each item

### 7. Additional Features
- User profile management
- Help section
- About Us information
- Privacy Policy

---

## 📖 User Manual

### Getting Started

#### Installation
1. Download the LOFO app APK file
2. Enable "Install from Unknown Sources" in your Android device settings
3. Install the APK file
4. Launch the app from your app drawer

#### First-Time Setup

**Step 1: Create an Account**
1. Open the LOFO app
2. On the login screen, tap "Register" or "Sign Up"
3. Fill in the registration form with:
   - Your full name
   - Email address
   - Phone number
   - Password (create a strong password)
4. Tap "Register" to create your account
5. You will be automatically logged in after successful registration

**Step 2: Complete Your Profile**
1. After logging in, navigate to "My Profile" from the navigation drawer
2. Review your profile information
3. Tap "Edit Profile" to update:
   - Profile picture
   - Name
   - Email
   - Phone number
4. Save your changes

---

### Using the App

#### Navigation
The app uses a navigation drawer (hamburger menu) accessible from the top-left corner. The main sections include:
- **Dashboard**: Home screen showing recent lost and found items
- **Lost Items**: Browse and report lost items
- **Found Items**: Browse and report found items
- **My Items**: View your posted items
- **My Profile**: Manage your account
- **Help**: Get assistance
- **About Us**: Learn about the app
- **Privacy Policy**: Read privacy information

---

### Reporting a Lost Item

**Step 1: Access Lost Items Section**
1. Open the navigation drawer
2. Tap on "Lost Items"

**Step 2: Add a Lost Item**
1. Tap the "+" or "Add Lost Item" button
2. Fill in the lost item form:
   - **Item Name**: Enter the name of the lost item (e.g., "Blue Backpack", "iPhone 12")
   - **Category**: Select from available categories (Electronics, Clothing, Books, Accessories, etc.)
   - **Date Lost**: Select the date when you lost the item
   - **Time Lost**: Enter the approximate time
   - **Location**: Specify where you lost the item (e.g., "Library - 2nd Floor", "Cafeteria")
   - **Description**: Provide detailed description including:
     - Color, size, brand
     - Any distinctive features
     - Contents (if applicable)
   - **Image**: Tap to upload a photo of the item (if you have one)
3. Review all information
4. Tap "Submit" or "Post" to publish your lost item

**Step 3: View Your Posted Item**
- Your item will appear in the "Lost Items" list
- It will also be visible in "My Items" section
- Other users can view and contact you if they find your item

---

### Reporting a Found Item

**Step 1: Access Found Items Section**
1. Open the navigation drawer
2. Tap on "Found Items"

**Step 2: Add a Found Item**
1. Tap the "+" or "Add Found Item" button
2. Fill in the found item form:
   - **Item Name**: Enter a descriptive name for the found item
   - **Category**: Select the appropriate category
   - **Date Found**: Select when you found the item
   - **Time Found**: Enter the time of discovery
   - **Location**: Specify where you found the item
   - **Description**: Provide details about the item's condition and appearance
   - **Image**: Upload a clear photo of the found item
3. Review all information
4. Tap "Submit" or "Post" to publish your found item

**Step 3: Wait for Contact**
- Your found item will appear in the "Found Items" list
- Users who lost similar items can contact you via call or SMS

---

### Searching and Browsing Items

**Browse All Items:**
1. Navigate to "Lost Items" or "Found Items" from the navigation drawer
2. Scroll through the list of items
3. Each item card shows:
   - Item name
   - Category
   - Date and location
   - Thumbnail image (if available)

**View Item Details:**
1. Tap on any item card to view full details
2. The detail screen shows:
   - Full item description
   - Complete date and time information
   - Location details
   - Full-size image (if available)
   - Owner/Finder contact information
   - Call and SMS buttons for direct contact

**Search Functionality:**
- Use the search bar to filter items by:
  - Item name
  - Category
  - Location
  - Date range

---

### Contacting Other Users

**When You Find a Match:**
1. View the item details by tapping on the item card
2. Review the contact information displayed
3. Choose your preferred contact method:
   - **Call**: Tap the phone icon to call the user directly
   - **SMS**: Tap the message icon to send a text message
4. Introduce yourself and provide details about the item
5. Arrange a meeting place and time to exchange the item

**Best Practices:**
- Be polite and respectful when contacting other users
- Verify item details before meeting
- Meet in a safe, public location
- Confirm the item matches the description before exchanging

---

### Managing Your Items

**View Your Posted Items:**
1. Open the navigation drawer
2. Tap "My Items"
3. View all items you've posted (both lost and found)
4. Items are organized by type and date

**Update or Remove Items:**
- If you find your lost item or return a found item, consider updating the status
- Contact support if you need to remove an item from the list

---

### Profile Management

**Edit Your Profile:**
1. Navigate to "My Profile" from the navigation drawer
2. Tap "Edit Profile"
3. Update any of the following:
   - Profile picture (tap to change)
   - Name
   - Email address
   - Phone number
4. Tap "Save" to apply changes

**Why Keep Profile Updated:**
- Accurate contact information helps others reach you quickly
- Updated profile increases trust in the community

---

### Dashboard

The Dashboard is your home screen and provides:
- **Recent Lost Items**: Latest items reported as lost
- **Recent Found Items**: Latest items reported as found
- Quick access to main features
- Overview of community activity

---

## 🔒 Privacy and Security

- All user data is securely stored using Firebase Authentication
- Contact information is only visible to users viewing item details
- Images are securely hosted on Cloudinary
- Users can review the Privacy Policy in the app settings

---

## 🆘 Troubleshooting

### Common Issues

**Can't log in:**
- Verify your email and password are correct
- Check your internet connection
- Try resetting your password if forgotten

**Images not uploading:**
- Check your internet connection
- Ensure you've granted storage permissions
- Try using a smaller image file

**Can't contact other users:**
- Verify your device has call/SMS capabilities
- Check that the phone number is displayed correctly
- Ensure you have a stable internet connection

**App crashes:**
- Close the app completely and reopen it
- Clear app cache from device settings
- Reinstall the app if issues persist

---

## 📞 Support

For additional help:
1. Navigate to "Help" section in the app
2. Review the "About Us" section for more information
3. Contact the development team through the provided channels

---

## 📄 License

This project is developed for educational purposes and community use.

---

## 👥 Credits

Developed with ❤️ for the college community to help reunite people with their lost belongings.

---

## 🔗 Additional Resources

For screenshots and detailed visual guide, refer to the [User Manual PDF](User%20Manual-%20Lost%20and%20Found%20App.pdf) included in this repository.

---

**Version**: 1.0  
**Last Updated**: 2024
