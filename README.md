# ComposeChatbot

ComposeChatbot is an AI-powered Android chatbot built with Kotlin and Jetpack Compose, integrating Google's Gemini 3.1 Flash-Lite through Firebase AI Logic. 
It features context-aware conversations, local message persistence, Markdown rendering, code blocks, and customizable themes.

- [Preview](#preview)
- [Features](#features)
- [Architecture](#architecture)
- [Tests](#tests)
- [Biggest Challenge](#biggest-challenge)
- [Technologies Used](#technologies-used)
- [Contact](#contact)

## Preview
<table>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/67c29a41-1510-4fce-9d71-d4674fa357e9" width="250" alt="Welcome Screen"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/8bb75f1b-0a0c-42da-8637-1d2d36279181" width="250" alt="Conversation"/>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/05b1766f-b0b3-42df-a91d-bad292976de6" width="250" alt="Code Blocks"/>
    </td>
  </tr>
  <tr>
    <td align="center"><b>Welcome Screen</b></td>
    <td align="center"><b>Conversation</b></td>
    <td align="center"><b>Code Blocks</b></td>
  </tr>
</table>

## Features
* AI-powered conversations with Gemini 3.1 Flash-Lite
* Context-aware conversations
* Local message persistence with Room
* Message delivery states (Pending, Sent, and Failed)
* Recovery of pending messages after app restarts
* Markdown rendering and code blocks
* Typing indicator and automatic scrolling
* Clear conversation with confirmation
* Error handling with customized Snackbar feedback
* Theme selection (System, Light, Dark) with DataStore persistence

## Architecture

ComposeChatbot follows the MVVM pattern with a layered architecture separating presentation, domain, and data responsibilities.

```text
              ┌──────────────────────┐
              │    Presentation      │
              │   Jetpack Compose    │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │     ViewModels       │
              │   MVVM + StateFlow   │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │       Domain         │
              │ Models & Contracts   │
              └──────────┬───────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │        Data          │
              │     Repositories     │
              └──────────┬───────────┘
                         │
              ┌──────────┴───────────┐
              ▼                      ▼
       ┌──────────────┐       ┌──────────────┐
       │ Firebase AI  │       │     Room     │
       │ Logic/Gemini │       │   Database   │
       └──────────────┘       └──────────────┘
                                     
              SettingsRepository
                       │
                       ▼
                   DataStore
```

Hilt handles dependency injection, while Kotlin Coroutines and Flow manage asynchronous operations and reactive data streams.

## Tests
ComposeChatbot includes unit tests covering ViewModel behavior, including:

* Initial UI state
* State management and updates
* Chat interaction handling
* Conversation state management

## Biggest Challenge

### Firebase AI Logic Integration and App Check
Integrating Firebase AI Logic required configuring Firebase App Check to protect AI requests, including setting up the Debug Provider and registering its token for development.

Another challenge was handling Firebase-specific exceptions and translating them into application-level errors. This keeps SDK implementation details inside the data layer and 
allows the ViewModel to provide meaningful feedback to the user.

## Technologies Used

| Category | Technology | Purpose |
|---|---|---|
| Language | [![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/) | Main development language (my favorite 💜) |
| UI | [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose) | Declarative Android UI |
| Design | [![Material 3](https://img.shields.io/badge/Material%203-6750A4?style=flat&logo=materialdesign&logoColor=white)](https://m3.material.io/) | UI components and theming |
| AI | [![Firebase AI Logic](https://img.shields.io/badge/Firebase%20AI%20Logic-FFCA28?style=flat&logo=firebase&logoColor=black)](https://firebase.google.com/docs/ai-logic) [![Gemini](https://img.shields.io/badge/Gemini-8E75B2?style=flat&logo=googlegemini&logoColor=white)](https://ai.google.dev/) | AI-powered conversations |
| Security | [![Firebase App Check](https://img.shields.io/badge/App%20Check-FFCA28?style=flat&logo=firebase&logoColor=black)](https://firebase.google.com/docs/app-check) | Firebase request protection |
| DI | [![Hilt](https://img.shields.io/badge/Hilt-34A853?style=flat&logo=android&logoColor=white)](https://dagger.dev/hilt/) | Dependency injection |
| Persistence | [![Room](https://img.shields.io/badge/Room-4285F4?style=flat&logo=android&logoColor=white)](https://developer.android.com/training/data-storage/room) | Local conversation storage |
| Preferences | [![DataStore](https://img.shields.io/badge/DataStore-3DDC84?style=flat&logo=android&logoColor=white)](https://developer.android.com/topic/libraries/architecture/datastore) | Persistent theme preferences |
| Async | [![Coroutines](https://img.shields.io/badge/Coroutines%20%26%20Flow-7F52FF?style=flat&logo=kotlin&logoColor=white)](https://kotlinlang.org/docs/coroutines-overview.html) | Asynchronous programming and reactive streams |
| Markdown | [![Markdown Renderer](https://img.shields.io/badge/Markdown%20Renderer-6D4C9B?style=flat)](https://github.com/mikepenz/multiplatform-markdown-renderer) | Markdown rendering in Compose |

## Contact
<a href="https://www.linkedin.com/in/gblrodrigues">
  <img src="https://img.shields.io/badge/LinkedIn-100%2B-0077B5?style=flat&logo=linkedin&logoColor=white" alt="My LinkedIn Profile"/>
</a>
