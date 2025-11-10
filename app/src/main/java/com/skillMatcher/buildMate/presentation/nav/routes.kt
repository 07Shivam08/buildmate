package com.skillMatcher.buildMate.presentation.nav

import kotlinx.serialization.Serializable


sealed class SubNavigation{

    @Serializable
    object LoginSignUpScreenRoutes : SubNavigation()


    @Serializable
    object HomeScreenRoutes : SubNavigation()

}


sealed class Routes {

    @Serializable
    object SplashScreenRoutes : Routes()

    @Serializable
    object LoginScreenRoutes : Routes()

    @Serializable
    object SignUpScreenRoutes : Routes()

    @Serializable
    object HomeScreenRoutes : Routes()

    @Serializable
    object ProfileScreenRoutes : Routes()

    @Serializable
    data class IdeaScreenRoutes(val ideaId : Int) : Routes()

    @Serializable
    data class GetAllIdeaScreenRoutes(val userId : String) : Routes()

    @Serializable
    object SkillScreenRoutes : Routes()

    @Serializable
    data class IdeaByIdScreenRoutes(val ideaId : String) : Routes()

}