# Contact Manager

A modern and intuitive Android application designed to help users manage their contacts efficiently. With a sleek Material Design interface, the app provides seamless CRUD operations, advanced swipe actions, and a critical SOS feature for safety.

## Features

- **Efficient Contact Management**: Add, edit, and delete contacts with ease.
- **Advanced Swipe Actions**: 
  - Swipe **Right** to quickly initiate a call.
  - Swipe **Left** to delete a contact instantly.
- **Dynamic Search**: Find any contact in real-time as you type.
- **SOS Emergency Messaging**: Send your live location via WhatsApp in case of emergencies.
- **Favorites**: Mark important contacts as favorites for quick access.
- **Clean UI/UX**: Built with Material 3 design principles for a premium feel.

## Visual Tour

### Main Dashboard
![Main Activity](./screenshots/main_activity.png)
*The central hub for all your contacts with a clean, card-based layout.*

### Contact Details & Actions
![Description Card](./screenshots/description_card.png)
*View detailed contact information and quickly initiate calls or emails.*

### Swipe to Call (Right)
![Swipe Right](./screenshots/swipe_right.png)
*Convenient gesture to call contacts directly from the list.*

### Swipe to Delete (Left)
![Swipe Left](./screenshots/swipe_left.png)
*Quick and easy removal of unwanted contacts.*

### SOS Emergency Feature
![SOS Message](./screenshots/sos_message.png)
*Send your current location coordinates via WhatsApp with a single tap.*

### Adding a New Contact
![Add Contact](./screenshots/add_contact.png)
*Simple and organized form to save new connections.*

### Real-time Search
![Search Contact](./screenshots/search_contact.png)
*Instantly filter your contact list to find exactly who you're looking for.*

## Tech Stack

- **Language**: Java
- **UI Framework**: Android XML with Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library (Local SQLite)
- **Data Binding**: Used to bind UI components directly to data sources.
- **Architecture Components**:
  - `ViewModel`: Handles UI-related data in a lifecycle-conscious way.
  - `LiveData`: Notifies views of data changes automatically.
- **External Services**:
  - `Google Play Services Location`: For accurate SOS location tracking.
  - `WhatsApp Integration`: For sending emergency messages.

---
*Created with ❤️ for a better contact management experience.*
