# HomeScreen Implementation Documentation

## Overview
A modern, beautiful HomeScreen has been created with multiple navigation options, featuring:
- **Light theme** with modern gradient colors
- **Custom Poppins font** for eye-appealing typography
- **Smooth animations** (slide-in and fade-in effects)
- **User welcome message** with dynamic username fetched from ViewModel
- **Three interactive option cards** for navigation

---

## Features Implemented

### 1. **User Welcome Section**
```kotlin
Text: "Welcome back, $userName! 👋"
Subtitle: "Let's make something awesome today"
```
- Dynamically fetches username from `MyViewModel.fetchUserName()`
- Displays personalized greeting
- Smooth slide-in animation on load

### 2. **Navigation Cards** (Interactive Options)

#### Card 1: **Enter Your Skills**
- **Icon**: Trophy (EmojiEvents)
- **Color**: Blue (#2196F3)
- **Background**: Light Blue (#E3F2FD)
- **Navigation**: `Routes.SkillScreenRoutes`
- **Description**: "Add and manage your professional skills"

#### Card 2: **Show All Ideas**
- **Icon**: Lightbulb
- **Color**: Orange (#FFA726)
- **Background**: Light Orange (#FFF3E0)
- **Navigation**: `Routes.GetAllIdeaScreenRoutes(userId = userName)`
- **Description**: "View your generated AI-powered ideas"
- **Note**: Passes current user's ID for filtering ideas

#### Card 3: **Your Profile**
- **Icon**: Account Circle
- **Color**: Purple (#9C27B0)
- **Background**: Light Purple (#F3E5F5)
- **Navigation**: `Routes.ProfileScreenRoutes`
- **Description**: "View and edit your profile information"

---

## Design System

### Colors
- **Background**: Light Gray (#F8F9FA)
- **Text (Primary)**: Dark Gray (#1E1E1E)
- **Text (Secondary)**: Medium Gray (#7A7A7A)
- **Card Background**: White with colored overlays

### Typography
- **Font Family**: Poppins (custom, from res/font)
  - Regular (Normal weight)
  - Medium (500 weight)
  - SemiBold (600 weight)
  - Bold (700 weight)
  - Light (300 weight)

- **Font Sizes**:
  - Welcome Header: 28sp (Bold)
  - Subtitle: 14sp (Normal)
  - Card Title: 18sp (SemiBold)
  - Card Subtitle: 12sp (Normal)

### Animations
- **Type**: Slide-In (Up) + Fade-In
- **Applied to**: Welcome section and cards container
- **Effect**: Smooth, modern appearance on screen load

---

## Code Structure

### Custom Font Definitions
```kotlin
val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.W500),
    Font(R.font.poppins_semibold, FontWeight.W600),
    Font(R.font.poppins_bold, FontWeight.Bold),
    Font(R.font.poppins_light, FontWeight.Light),
)

val SoraFontFamily = FontFamily(
    Font(R.font.sora_semibold, FontWeight.SemiBold),
)
```

### Main Composables
1. **HomeScreen**: Main container with authentication check
2. **HomeOptionCard**: Reusable card component for navigation options

### ViewModel Integration
- Uses `MyViewModel` via `@HiltViewModel`
- Calls `fetchUserName()` on initial load via `LaunchedEffect`
- Collects username state with `collectAsState()`

---

## Navigation Flow

### HomeScreen Navigation Routes:
```
HomeScreen
├── Routes.SkillScreenRoutes
├── Routes.GetAllIdeaScreenRoutes(userId)
└── Routes.ProfileScreenRoutes
```

### Data Flow:
1. User opens HomeScreen
2. `LaunchedEffect` triggers `fetchUserName()`
3. ViewModel fetches username from `getUserNameUseCase`
4. Username displayed in greeting
5. User clicks card → navigates to respective screen
6. For Ideas: passes `userId` (username) for filtering

---

## Responsiveness

- **Full Screen Layout**: Fills entire screen
- **Scrollable**: Vertical scroll enabled for content overflow
- **Padding**: 20dp from edges for spacing
- **Adaptive Cards**: 
  - Full width cards
  - Spacing between cards: 16dp
  - Rounded corners: 16dp
  - Elevation: 4dp shadow

---

## ViewModel Methods Used

| Method | Purpose |
|--------|---------|
| `fetchUserName()` | Fetches current user's name from Firebase |
| `userName` StateFlow | Observes username state changes |

---

## File Modified
- **File**: `app/src/main/java/com/skillMatcher/buildMate/presentation/screens/HomeScreen.kt`
- **Status**: ✅ Complete and error-free
- **Lines**: 248 lines

---

## Testing Checklist

- [ ] HomeScreen displays correctly
- [ ] Username fetches and displays in greeting
- [ ] "Enter Your Skills" card navigates to SkillScreen
- [ ] "Show All Ideas" card navigates to GetAllIdeaScreen with userId
- [ ] "Your Profile" card navigates to ProfileScreen
- [ ] Animations play smoothly on load
- [ ] Cards are clickable and responsive
- [ ] Scroll works if content overflows
- [ ] Theme matches light mode with modern colors

---

## Future Enhancements

1. Add loading state while fetching username
2. Add error handling with retry option
3. Add ripple effect on card click
4. Add pull-to-refresh functionality
5. Add quick stats (total skills, ideas count)
6. Add logout/settings option in top bar
