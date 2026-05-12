# FreshGo Grocery App - Kotlin Assignment

This is a mini grocery delivery application built as part of the Kotlin Android assignment. The app is inspired by Blinkit/Swiggy Instamart and includes features like product browsing, cart management, and order tracking.

## Core Features
- **Login**: Mobile login with 1234 OTP (fake verification).
- **Home**: Product listing with search and category filtering. Added some extra filters for Vegan and Organic items.
- **Cart**: Add/remove items with a real-time bill summary.
- **Checkout**: Simple address input and payment selection (COD/Card).
- **Tracking**: A simulated tracking screen that shows the rider moving.
- **Notifications**: An in-app notification center that saves your history.

## Tech Used
- **Architecture**: MVVM (View-ViewModel-Repository).
- **Database**: Room (used for saving Order History and Notifications).
- **UI**: XML only, no Compose. Used RecyclerView for all lists.
- **Animations**: Added some Lottie animations for the splash screen and order tracking.
- **Background**: WorkManager handles some background offer notifications.

## How to setup
1. Open the project in Android Studio.
2. Build and run on an emulator (API 33 or above recommended for notifications).
3. Use any 10-digit number to login. OTP is 1234.

## Future Plans (TODO)
- Integrate real Google Maps for tracking.
- Add a proper backend (Firebase or Node.js).
- Implement a "frequent buy" algorithm based on real user data.
- Add voice search for easier shopping.

## Challenges Faced
- Managing the cart state across different screens without a database was tricky, so I used a Singleton manager.
- Making the simulated rider move smoothly on the tracking screen took some trial and error with animations.
- Setting up the Room database for notifications while keeping the UI reactive required some Flow/LiveData handling.
