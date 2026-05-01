package my.lokalan.posq.application

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import my.posq.data.network.TokenManager
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.login.LoginScreen
import my.lokalan.posq.presentation.main.MainScreen
import my.lokalan.posq.presentation.splash.SplashScreen
import my.lokalan.posq.presentation.user.adduser.AddUserScreen
import my.lokalan.posq.presentation.transaction.addtransaction.AddTransactionScreen
import my.lokalan.posq.ui.theme.PosqTheme
import my.lokalan.posq.ui.theme.ThemeManager
import my.lokalan.posq.ui.theme.ThemeMode
import kotlinx.coroutines.flow.collectLatest
import kotlinx.serialization.json.Json
import my.lokalan.posq.presentation.transaction.detailtransaction.TransactionDetailScreen
import my.lokalan.posq.presentation.transaction.model.TransactionUiData
import org.koin.compose.koinInject

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App() {

    val themeManager: ThemeManager = koinInject()
    val tokenManager: TokenManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()

    val systemDark = isSystemInDarkTheme()
    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    // NavController (Top-level)
    val rootNavController = rememberNavController()

    LaunchedEffect(Unit) {
        tokenManager.logoutEvent.collectLatest {
            rootNavController.navigate(Screen.LoginRoute) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Crossfade(targetState = systemDark, animationSpec = tween(400)) {
        PosqTheme(
            darkTheme = isDarkTheme,
            useDynamicColor = false
        ) {

//            val navBackStack by rootNavController.currentBackStackEntryAsState()
//            val currentRoute = navBackStack?.destination?.route
//
//            val showBottomBar = currentRoute !in listOf(
//                Screen.SplashRoute::class.qualifiedName,
//                Screen.LoginRoute::class.qualifiedName
//            )

            NavHost(
                navController = rootNavController,
                startDestination = Screen.SplashRoute,
            ) {

                composable<Screen.SplashRoute> {
                    SplashScreen(rootNavController)
                }

                composable<Screen.LoginRoute> {
                    LoginScreen(rootNavController)
                }

                // MAIN CONTENT AREA (Persistent)
                composable(Screen.MainRoute.ROUTE) {
                    MainScreen(
                        rootNavHostController = rootNavController
                    )
                }

                composable<Screen.AddTransactionRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.AddTransactionRoute>()
                    AddTransactionScreen(
                        navController = rootNavController,
                        isCollective = args.isCollective
                    )
                }

                composable<Screen.AddUserRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.AddUserRoute>()
                    AddUserScreen(
                        navController = rootNavController,
                        isEdit = args.isEdit,
                        userId = args.userId,
                        isLoginUser = args.isLoginUser
                    )
                }

                composable<Screen.TransactionDetailRoute> { backStackEntry ->
                    val args = backStackEntry.toRoute<Screen.TransactionDetailRoute>()
                    val transaction = Json.decodeFromString<TransactionUiData>(args.transactionJson)
                    TransactionDetailScreen(
                        transaction = transaction,
                        onBackClick = { rootNavController.popBackStack() }
                    )
                }
            }

        }
    }
}