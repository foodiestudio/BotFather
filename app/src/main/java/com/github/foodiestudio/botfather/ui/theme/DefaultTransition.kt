package com.github.foodiestudio.botfather.ui.theme

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle

@OptIn(ExperimentalAnimationApi::class)
object DefaultTransition : DestinationStyle.Animated {
    override fun AnimatedContentScope<NavBackStackEntry>.enterTransition(): EnterTransition {
        return scaleIn(
            initialScale = 0.95f,
            animationSpec = tween(300)
        )
    }

    override fun AnimatedContentScope<NavBackStackEntry>.popEnterTransition(): EnterTransition {
        return scaleIn(
            initialScale = 1.3f,
            animationSpec = tween(300)
        )
    }
}
