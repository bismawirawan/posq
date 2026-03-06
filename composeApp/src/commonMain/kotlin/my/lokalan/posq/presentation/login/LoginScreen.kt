package my.lokalan.posq.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.posq.shared.SageDark
import my.posq.shared.PosqTypography
import my.posq.shared.TextSecondaryDark
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.ui.component.InputText
import my.lokalan.posq.ui.component.LoadingButton
import my.lokalan.posq.ui.component.PasswordInput
import my.lokalan.posq.ui.component.TalangragaScaffold
import my.lokalan.posq.ui.component.ToastManager
import my.lokalan.posq.ui.component.ToastType
import my.lokalan.posq.ui.theme.PosqTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import posq.composeapp.generated.resources.Res
import posq.composeapp.generated.resources.bg_screen
import posq.composeapp.generated.resources.input_here
import posq.composeapp.generated.resources.input_password_here
import posq.composeapp.generated.resources.label_username_or_email
import posq.composeapp.generated.resources.login
import posq.composeapp.generated.resources.password
import posq.composeapp.generated.resources.talangraga_logo

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    viewModel: LoginViewModel = koinViewModel(),
) {

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val loginSucceed by viewModel.loginSucceed.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    LaunchedEffect(loginSucceed) {
        if (loginSucceed == true) {
            navHostController.navigate(Screen.MainRoute.ROUTE) {
                popUpTo(Screen.LoginRoute) { inclusive = true }
            }
        }
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastManager.show(message = errorMessage.orEmpty(), type = ToastType.Error)
            viewModel.clearError() // Only if you have this in your VM
        }
    }

    LoginContent(
        isLoading = isLoading,
        identifier = viewModel.identifier.value,
        password = viewModel.password.value,
        onIdentifierChange = viewModel::onIdentifierChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = viewModel::login
    )
}

@Composable
fun LoginContent(
    isLoading: Boolean = false,
    identifier: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    onIdentifierChange: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    TalangragaScaffold { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(SageDark, Color.White),
                        start = Offset(0f, 0f), // Top-left
                        end = Offset(
                            0f,
                            Float.POSITIVE_INFINITY
                        ) // Bottom-right
                    )
                )
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.bg_screen),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.45f),
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.talangraga_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    "Masuk",
                    style = PosqTypography.titleMedium.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Masuk via email, nomor hp, atau username",
                    style = PosqTypography.titleMedium.copy(
                        fontSize = 16.sp,
                        color = TextSecondaryDark,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                InputText(
                    title = stringResource(Res.string.label_username_or_email),
                    value = identifier,
                    onValueChange = onIdentifierChange,
                    placeholder = stringResource(Res.string.input_here),
                    leadingIcon = Icons.Filled.AccountCircle,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                PasswordInput(
                    title = stringResource(Res.string.password),
                    password = password,
                    onPasswordChange = onPasswordChange,
                    placeholder = stringResource(Res.string.input_password_here),
                    leadingIcon = Icons.Filled.Security,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                LoadingButton(
                    modifier = Modifier.fillMaxWidth(),
                    isLoading = isLoading,
                    text = stringResource(Res.string.login),
                    enabled = identifier.isNotBlank() && password.isNotBlank(),
                    onClick = onLoginClick
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    PosqTheme(darkTheme = false, useDynamicColor = false) {
        LoginContent(
            isLoading = false,
            identifier = "testuser",
            password = "password",
            onPasswordChange = {},
            onIdentifierChange = {},
            onLoginClick = {}
        )
    }
}
