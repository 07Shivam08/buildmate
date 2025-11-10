# Hilt Dependency Injection - Visual Architecture

## Module Dependency Graph

```
                    PRESENTATION
                         ↓
                    DomainModule
                         ↓
           ┌──────────────┴──────────────┐
           ↓                             ↓
      DataModule                  NetworkModule
      ┌────────────┐              ┌──────────────┐
      │ Database   │              │   Retrofit   │
      │ DAOs       │              │   API Keys   │
      │ Firebase   │              │   HTTP       │
      └────────────┘              └──────────────┘
```

## RepoImpl Dependency Injection Flow

```
   DomainModule.bindRepo()
           ↓
      RepoImpl
    ┌────┼────┬────────┬────────┐
    ↓    ↓    ↓        ↓        ↓
   FS   IdeaD UserD   Auth  GeminiAPI
    ↑    ↑    ↑        ↑        ↑
    └────┴────┴────────┴────────┘
           ↓
    DataModule  +  NetworkModule
```

## Complete Dependency Tree

```
Repo (Interface)
  │
  ├─→ Bound to: RepoImpl (via @Binds in DomainModule)
        │
        ├─→ Requires: FirebaseFirestore
        │              └─→ Provided by: DataModule.provideFirebaseFireStore()
        │
        ├─→ Requires: IdeaDao
        │              └─→ Provided by: DataModule.provideIdeaDao()
        │                  └─→ Requires: AppDatabase
        │                      └─→ Provided by: DataModule.provideAppDatabase()
        │
        ├─→ Requires: UserSkillDao
        │              └─→ Provided by: DataModule.provideUserSkillDao()
        │                  └─→ Requires: AppDatabase
        │
        ├─→ Requires: FirebaseAuth
        │              └─→ Provided by: DataModule.provideFirebaseAuth()
        │
        └─→ Requires: GeminiApiService
                       └─→ Provided by: NetworkModule.provideGeminiApi()
                           └─→ Requires: Retrofit
                               └─→ Provided by: NetworkModule.provideRetrofit()
                                   ├─→ Requires: Gson
                                   │              └─→ From NetworkModule.provideGson()
                                   │
                                   └─→ Requires: OkHttpClient
                                                  └─→ From NetworkModule.provideOkHttp()
```

## Singleton Scope Lifecycle

```
┌─────────────────────────────────────────────────┐
│        Application Initialization               │
└─────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────┐
│    Hilt Creates SingletonComponent              │
└─────────────────────────────────────────────────┘
                        ↓
    ┌───────────────────┼───────────────────┐
    ↓                   ↓                   ↓
DataModule         NetworkModule        DomainModule
    │                   │                   │
    ├─ AppDatabase      ├─ Gson            │
    │  (cached)         │  (cached)         │
    │                   │                   │
    ├─ Daos             ├─ OkHttpClient     │
    │  (cached)         │  (cached)         │
    │                   │                   │
    ├─ Firebase         ├─ Retrofit         │
    │  (cached)         │  (cached)         │
    │                   │                   │
    └─ Auth             ├─ GeminiApiService│─ Repo bound to
       (cached)         │  (cached)         │  RepoImpl (cached)
                        │                   │
                        └─ ApiKey          └
                           (cached)
                        ↓
┌─────────────────────────────────────────────────┐
│  All Singletons Created & Cached               │
│  Reused throughout entire app lifecycle        │
└─────────────────────────────────────────────────┘
                        ↓
┌─────────────────────────────────────────────────┐
│        Application Termination                  │
└─────────────────────────────────────────────────┘
```

## Injection Points in Your App

```
┌─────────────────────────────────────────┐
│         MainActivity                    │
│  @AndroidEntryPoint                     │
│  - Hilt injects dependencies            │
└────────────────┬────────────────────────┘
                 │ creates
        ┌────────▼────────┐
        │   MyViewModel   │
        │  @HiltViewModel │
        │  @Inject        │
        │  Repo           │
        └────────┬────────┘
                 │ needs
        ┌────────▼────────┐
        │ RepoImpl         │
        │ @Inject         │
        │ constructor(    │
        │  FS, DAO, ...)  │
        └────────┬────────┘
                 │ resolved by
        ┌────────▼─────────┐
        │  DataModule +    │
        │  NetworkModule   │
        └──────────────────┘
```

## Module Communication Pattern

```
                        ┌──────────────────┐
                        │  BaseApp         │
                        │ @HiltAndroidApp  │
                        └────────┬─────────┘
                                 │
                                 ├─→ Activates Hilt
                                 │
                                 └─→ Loads all @Module classes
                                      │
        ┌─────────────────────────────┼─────────────────────────────┐
        ↓                             ↓                             ↓
   DataModule              NetworkModule                      DomainModule
   (Data Layer)            (Data Layer)                        (Domain Layer)
        │                       │                                  │
        ├─ Room DB              ├─ Retrofit                        ├─ @Binds
        ├─ Firebase Auth        ├─ Gson                            │
        ├─ Firebase Firestore   ├─ OkHttp                          └─ Repo → RepoImpl
        └─ DAOs                 └─ API Services
        
        ├─ All @Singleton       ├─ All @Singleton              ├─ Singleton
        ├─ @Provides            ├─ @Provides              ├─ @Binds
        └─ @InstallIn           └─ @InstallIn             └─ @InstallIn
           (SingletonComponent)    (SingletonComponent)      (SingletonComponent)
```

## Error Prevention

```
BEFORE FIX (❌ Problems):
┌──────────────────────────────┐
│ Version 2.57.1 + 2.57.2      │ ← Conflict
│ jakarta.inject               │ ← Wrong import
│ @Singleton missing           │ ← Multiple instances
│ ApplicationId mismatch        │ ← Build issues
│ Manual repo creation          │ ← Incomplete injection
└──────────────────────────────┘

AFTER FIX (✅ Fixed):
┌──────────────────────────────┐
│ All versions 2.57.2           │ ← Consistent
│ javax.inject                  │ ← Correct
│ @Singleton everywhere         │ ← Single instances
│ ApplicationId = Namespace     │ ← Perfect alignment
│ Constructor injection         │ ← Complete & clean
└──────────────────────────────┘
```

## Dependency Resolution Sequence

```
Step 1: Application Starts
  │
Step 2: BaseApp @HiltAndroidApp creates Hilt component
  │
Step 3: Hilt scans for @Module classes
  │
  ├─→ Finds DataModule
  │   └─→ Creates providers for DB, Firebase, DAOs
  │
  ├─→ Finds NetworkModule
  │   └─→ Creates providers for Retrofit, APIs, Keys
  │
  └─→ Finds DomainModule
      └─→ Sets up @Binds for Repo → RepoImpl
      
Step 4: Caches all @Singleton instances in SingletonComponent

Step 5: When code requests Repo interface
  │
  ├─→ Looks up in DomainModule
  │
  ├─→ Finds: bindRepo(repoImpl: RepoImpl)
  │
  ├─→ Needs RepoImpl, sees @Inject constructor
  │
  ├─→ Collects 5 constructor parameters
  │
  ├─→ Looks up in DataModule & NetworkModule
  │
  ├─→ All dependencies are @Singleton and cached
  │
  └─→ Returns cached RepoImpl (bound to Repo interface)

Step 6: Repo is injected into consuming code ✅
```

## Scope & Lifetime

```
┌─ ApplicationScope (Process Lifetime)
│  │
│  ├─ SingletonComponent (@Singleton)
│  │  ├─ 🔒 AppDatabase (never recreated)
│  │  ├─ 🔒 UserSkillDao (never recreated)
│  │  ├─ 🔒 IdeaDao (never recreated)
│  │  ├─ 🔒 FirebaseFirestore (never recreated)
│  │  ├─ 🔒 FirebaseAuth (never recreated)
│  │  ├─ 🔒 Gson (never recreated)
│  │  ├─ 🔒 OkHttpClient (never recreated)
│  │  ├─ 🔒 Retrofit (never recreated)
│  │  ├─ 🔒 GeminiApiService (never recreated)
│  │  ├─ 🔒 ApiKey (never recreated)
│  │  └─ 🔒 Repo/RepoImpl (never recreated)
│  │
│  └─ ActivityScope (@ActivityScoped)
│     └─ ViewModelScope
│        └─ Single instance per ViewModel
└─ End

Legend: 🔒 = Cached for entire app lifetime
```

## Complete Wiring

```
user code          |  DomainModule      |  DataModule      | NetworkModule
  ↓                |      ↓             |      ↓           |     ↓
needs Repo         |  @Binds            |                  |
  ↓                |  bindRepo(         |                  |
  ↓                |    repoImpl:        |                  |
  ├────────────────→ RepoImpl            |                  |
  │                |      )             |                  |
  │                |         ↑          |                  |
  │                |         │          |                  |
  │                |    needs 5         |                  |
  │                |  dependencies      |                  |
  │                |         ↑          |                  |
  │                |         │          |                  |
  │                |    ┌────┼────┬─────┼───────┬──────┐   |
  │                |    │    │    │     │       │      │   |
  │                |    ↓    ↓    ↓     ↓       ↓      ↓   |
  │                |   FS   IDA  UDA   Auth  G-Api   ...  |
  │                |    ↑    ↑    ↑     ↑       ↑         |
  │                |    └────┼────┴─────┴───────┘         |
  │                |         │         └─────────────────→ |
  │                |    @Provides        @Provides       @Provides
  │                |    @Singleton       @Singleton      @Singleton
  │                |                                      |
  └────────────────→ (All resolved & cached) ←─────────────
                   Repo instance created & returned ✅
```

---

This visual representation shows:
- ✅ How modules are organized
- ✅ How dependencies flow
- ✅ What gets cached (singletons)
- ✅ How injection happens
- ✅ Complete resolution chain
