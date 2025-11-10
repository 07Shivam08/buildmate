# Quick Reference: Hilt Module Structure

## The Three Modules

### DataModule (Local Data & Firebase)
```
┌─ AppDatabase (Room)
├─ UserSkillDao 
├─ IdeaDao
├─ FirebaseFirestore
└─ FirebaseAuth
```

### NetworkModule (Remote APIs)
```
┌─ Gson (JSON serialization)
├─ OkHttpClient (HTTP with logging)
├─ Retrofit (REST client builder)
├─ GeminiApiService (Gemini API)
└─ @Named("GEMINI_API_KEY") String (API key)
```

### DomainModule (Repository Layer)
```
Repo interface ←binds← RepoImpl
   ↓
RepoImpl requires:
  - FirebaseFirestore (from DataModule)
  - IdeaDao (from DataModule)
  - UserSkillDao (from DataModule)
  - FirebaseAuth (from DataModule)
  - GeminiApiService (from NetworkModule)
```

## Dependency Resolution Chain

```
Need Repo?
  → DomainModule.bindRepo() returns RepoImpl
  
RepoImpl needs 5 dependencies:
  → 4 from DataModule
  → 1 from NetworkModule
  → All auto-injected via constructor
```

## Key Annotations

- `@Module` - Marks class as Hilt provider
- `@Provides` - Manual object provision
- `@Binds` - Bind interface to implementation (preferred)
- `@Singleton` - Single instance app-wide
- `@InstallIn(SingletonComponent::class)` - Makes available globally
- `@Inject` - Mark constructor for auto-injection
- `@AndroidEntryPoint` - Enable Hilt in Activities/Fragments
- `@HiltViewModel` - Enable Hilt in ViewModels
- `@Named("key")` - Disambiguate same types

## Files to Remember

```
app/
├── build.gradle.kts (Hilt version 2.57.2)
└── src/main/java/com/skillMatcher/buildMate/
    ├── BaseApp.kt (@HiltAndroidApp)
    ├── MainActivity.kt (@AndroidEntryPoint)
    ├── data/
    │   └── di/
    │       ├── DataModule.kt
    │       └── NetworkModule.kt
    └── domain/
        └── di/
            └── DomainModule.kt
```

## All Set! ✅

Your Hilt setup is properly configured with:
- Correct version (2.57.2)
- Proper namespace (com.skillMatcher.buildMate)
- Clean module separation
- Best practices applied
