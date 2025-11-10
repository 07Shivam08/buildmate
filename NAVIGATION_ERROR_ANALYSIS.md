# 🐛 Navigation Error: "You must call setGraph() before calling getGraph()"

## Problem Identified

In your `App.kt` file, you're trying to navigate **BEFORE** the navigation graph is initialized.

### Current (Broken) Code:

```kotlin
@Composable
fun App(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    val isAuthenticated = firebaseAuth.currentUser != null

    // ❌ ERROR: Navigating BEFORE NavHost initializes the graph!
    val startScreen = if (isAuthenticated) {
        navController.navigate(Routes.HomeScreenRoutes) {
            popUpTo(Routes.LoginScreenRoutes) { inclusive = true }
        }
        // This returns Unit, not a Route object!
    } else {
        navController.navigate(Routes.LoginScreenRoutes) {
            popUpTo(Routes.SplashScreenRoutes) { inclusive = true }
        }
        // This returns Unit, not a Route object!
    }

    // NavHost tries to use 'startScreen' (which is Unit), then initialize graph
    NavHost(navController, startDestination = Routes.LoginScreenRoutes) {
        // ... routes
    }
}
```

### Why This Fails:

1. ❌ `navController.navigate()` returns `Unit`, not a Route
2. ❌ You're calling navigate BEFORE the graph is set
3. ❌ The `NavHost` composable hasn't been created yet
4. ❌ Hilt/Navigation graph isn't initialized

---

## ✅ Solution

Move the navigation logic INSIDE the `NavHost` lambda, or use `LaunchedEffect` to navigate AFTER the graph is set up.

### Fix Option 1: Use LaunchedEffect (RECOMMENDED)

```kotlin
@Composable
fun App(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    val isAuthenticated = firebaseAuth.currentUser != null

    // Determine start destination (without navigating)
    val startDestination = if (isAuthenticated) {
        Routes.HomeScreenRoutes as Routes  // Type it properly
    } else {
        Routes.LoginScreenRoutes as Routes
    }

    // Create NavHost with the correct start destination
    NavHost(navController, startDestination = startDestination) {
        navigation<SubNavigation.LoginSignUpScreenRoutes>(
            startDestination = Routes.LoginScreenRoutes
        ) {
            composable<Routes.LoginScreenRoutes> {
                LoginScreen(navController = navController)
            }
            composable<Routes.SignUpScreenRoutes> {
                SignupScreen(navController = navController)
            }
        }
        navigation<Routes.HomeScreenRoutes>(
            startDestination = Routes.HomeScreenRoutes
        ) {
            composable<Routes.HomeScreenRoutes> {
                HomeScreen(navController = navController)
            }
            // ... other routes
        }
    }

    // Navigate after the graph is initialized
    LaunchedEffect(Unit) {
        if (isAuthenticated) {
            navController.navigate(Routes.HomeScreenRoutes) {
                popUpTo(Routes.LoginScreenRoutes) { inclusive = true }
            }
        } else {
            navController.navigate(Routes.LoginScreenRoutes) {
                popUpTo(Routes.SplashScreenRoutes) { inclusive = true }
            }
        }
    }
}
```

### Fix Option 2: Use startDestination Parameter (SIMPLEST)

```kotlin
@Composable
fun App(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    val isAuthenticated = firebaseAuth.currentUser != null

    // Simply determine the start destination
    val startDestination: Routes = if (isAuthenticated) {
        Routes.HomeScreenRoutes
    } else {
        Routes.LoginScreenRoutes
    }

    // Pass it to NavHost - no manual navigation needed!
    NavHost(navController, startDestination = startDestination) {
        // All your routes...
    }
}
```

---

## Why Each Solution Works

### Option 1 (LaunchedEffect): Best for Complex Navigation
- ✅ Ensures graph is initialized BEFORE navigation
- ✅ Can perform multiple navigations in sequence
- ✅ Can handle side effects safely

### Option 2 (startDestination): Simplest
- ✅ NavHost handles everything
- ✅ No manual navigation calls needed
- ✅ Cleaner, more declarative
- ✅ **Recommended for your use case**

---

## 🔧 The Fix to Apply

I recommend **Option 2** since you just need to determine the initial screen, not navigate dynamically.

### Key Changes:

```diff
@Composable
fun App(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    val isAuthenticated = firebaseAuth.currentUser != null

-   val startScreen = if (isAuthenticated) {
-       navController.navigate(Routes.HomeScreenRoutes) {
-           popUpTo(Routes.LoginScreenRoutes) { inclusive = true }
-       }
-   } else {
-       navController.navigate(Routes.LoginScreenRoutes) {
-           popUpTo(Routes.SplashScreenRoutes) { inclusive = true }
-       }
-   }

+   val startDestination: Routes = if (isAuthenticated) {
+       Routes.HomeScreenRoutes
+   } else {
+       Routes.LoginScreenRoutes
+   }

-   NavHost(navController, startDestination = Routes.LoginScreenRoutes) {
+   NavHost(navController, startDestination = startDestination) {
        // ... routes
    }
}
```

---

## 📊 Comparison

| Method | Pros | Cons |
|--------|------|------|
| **Option 1: LaunchedEffect** | Safe, works for complex scenarios, handles side effects | More code, slight delay in navigation |
| **Option 2: startDestination** | ✅ Simple, clean, declarative, **RECOMMENDED** | Not suitable for very complex navigation logic |
| **Current (Broken)** | ❌ None | ❌ Crashes with setGraph() error |

---

## 📋 Summary

### Root Cause:
You were calling `navController.navigate()` **before** the `NavHost` composable initialized the navigation graph.

### Solution:
Simply determine which route to start with, then pass it as the `startDestination` parameter to `NavHost`.

### Result:
✅ No "setGraph()" errors
✅ Cleaner, more declarative code
✅ Proper Compose/Hilt integration
✅ Follows Navigation best practices

---

**This is a Navigation Compose issue, not a Hilt issue.**
The fix ensures you follow the proper lifecycle:
1. Create NavHost
2. Initialize graph
3. Navigate (if needed)
