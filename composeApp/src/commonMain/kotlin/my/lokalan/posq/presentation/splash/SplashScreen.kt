package my.lokalan.posq.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import my.posq.data.local.session.SessionKey
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.ui.theme.PosqTheme
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import posq.composeapp.generated.resources.Res
import posq.composeapp.generated.resources.talangraga_logo

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: SplashViewModel = koinViewModel()
) {

    val isLogin = viewModel.session.getBoolean(SessionKey.IS_LOGGED_IN)

    LaunchedEffect(isLogin) {
        delay(1000)
        if (!isLogin) {
            navHostController.navigate(Screen.LoginRoute) {
                popUpTo(Screen.SplashRoute) { inclusive = true }
            }
        } else {
            // navigate to MainRoute’s HOME tab
            navHostController.navigate(Screen.MainRoute.ROUTE) {
                popUpTo(Screen.SplashRoute) { inclusive = true }
            }
        }
    }

    SplashContent()
}

@Composable
@Preview
fun SplashContentPreview() {
    SplashContent()
}

@Composable
fun SplashContent() {
    PosqTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.talangraga_logo),
                contentDescription = "Splash Screen Image",
            )
        }
    }
}
