
Key Features and Components


1. Authentication
Login System:
Farmers and Consumers can sign in using email/password and Google authentication.
The app includes a sign-up page where users can choose their role (Farmer or Consumer) using buttons (instead of a spinner).

3. Navigation
Home Page:
Displays two main options: Farmer and Consumer.
Depending on the selection, the user is directed to either a farmer's dashboard or a consumer's front page.

4. Farmer Dashboard:
Inventory: Farmers can manage their products, including adding, updating, and deleting items.
Orders: A section where farmers can track orders placed by consumers.
Weather: Provides weather updates, which are important for farmers to plan their activities.
Profile: Farmers can view and edit their profile information.
Map: Integration with maps for location tracking.
Signout: Allows users to sign out.

5. Inventory Management
RecyclerView:
Displays product information such as product image, name, quantity, and price in a card layout.
Includes a floating action button (FAB) to add a new product image via an addImage activity.
Product Descriptions: Ensures that every product entry includes a description field to provide additional details.

7. Language Selection
The first page includes an option to select a language, which will translate all the app pages, making it accessible to a broader audience.

9. Firebase Integration
Firebase Authentication:
Handles user authentication and account management (email/password, Google, and Facebook login).
Firebase Database:
Stores farmer and consumer profiles, inventory data, orders, and other app-related data.

11. UI Design
You are using a color palette of shades of green, gray, teal, and amber for the UI design, creating a nature-friendly, agricultural look and feel.
ConstraintLayout is used for UI components, with some exceptions (like RadioGroup elements, where you prefer not to use constraints).
View Binding is used for accessing UI elements, making the code more concise and less prone to errors.

13. Edge-to-Edge UI:
For a modern design, you’re using the enableEdgeToEdge function in the consumerFront activity, providing a seamless display across devices with notches and rounded corners.

15. Navigation Component
The app uses Android Navigation Component, and you've encountered some issues applying the Safe Args plugin, which you resolved.
Future Enhancements
Enhanced Weather Features:

Additional functionality for weather-based notifications, agricultural advice, or planning features.
Marketplaces:

Expansion of the app to allow farmers to directly list their products for sale, and consumers to browse and purchase.
Advanced Analytics:

Add analytics features to help farmers track sales trends, order history, and manage stock efficiently.
User Feedback:

Implementing a rating or feedback system where consumers can rate the quality of the products and services from farmers.
Social Integration:

Additional social features such as farmer forums, chat systems, or event calendars could enhance the user experience.
Challenges and Issues
Firebase Integration:

Managing profiles and syncing data between the consumer and farmer sections of the app while ensuring smooth performance.
Navigation Component Issues:

Resolving the Safe Args plugin issue for smoother navigation transitions and data passing.
UI/UX:

Ensuring the app works seamlessly on a wide variety of screen sizes and devices.
Tech Stack
Frontend: Android (Kotlin, XML, ConstraintLayout, RecyclerView)
Backend: Firebase (Authentication, Firestore)
Tools: Android Studio, View Binding, Safe Args plugin, Floating Action Button, Edge-to-Edge UI
