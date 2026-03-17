@file:Suppress("AssignedValueIsNeverRead")

package my.lokalan.posq.presentation.user.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.posq.shared.AccentRed
import my.posq.shared.Red
import my.posq.shared.Sage
import my.posq.shared.PosqTypography
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.home.section.LogoutDialog
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.presentation.user.profile.ProfileViewModel
import my.lokalan.posq.presentation.utils.toUiData
import my.lokalan.posq.ui.component.BasicImage
import my.lokalan.posq.ui.component.IconBlock
import my.lokalan.posq.ui.component.ImageViewerManager
import my.lokalan.posq.ui.component.PosqScaffold
import my.lokalan.posq.ui.theme.PosqTheme
import my.lokalan.posq.ui.theme.ThemeManager
import my.lokalan.posq.ui.theme.ThemeMode
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import posq.composeapp.generated.resources.Res
import posq.composeapp.generated.resources.logout

@Composable
fun ProfileScreen(
    rootNavHostController: NavHostController,
    navHostController: NavHostController,
    isLoginUser: Boolean,
    viewModel: ProfileViewModel = koinViewModel(),
) {

    val profile by viewModel.session.userProfile.collectAsStateWithLifecycle()

    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()
    val systemDark = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    ProfileContent(
        isDarkMode = isDarkTheme,
        isLoginUser = isLoginUser,
        user = profile?.toUiData(),
        imageUrl = viewModel.imageUrl.value,
        themeManager = themeManager,
        onLogout = {
            viewModel.clearSession()
            rootNavHostController.navigate(Screen.LoginRoute) {
                popUpTo(Screen.MainRoute.ROUTE) {
                    inclusive = true
                }
            }
        },
        onClickEdit = {
            navHostController.navigate(
                Screen.AddUserRoute(
                    userId = profile?.id ?: 0,
                    isEdit = true,
                    isLoginUser = true
                )
            )
        },
        onChangePassword = {
            navHostController.navigate(Screen.ChangePasswordRoute(profile?.id ?: 0))
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    isDarkMode: Boolean,
    isLoginUser: Boolean,
    user: UserUIData?,
    imageUrl: String?,
    themeManager: ThemeManager?,
    onClickEdit: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
) {

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismissRequest = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    PosqScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val title = if (isLoginUser) "Profil Saya" else "Profil Pengguna"
                    Text(text = title, style = PosqTypography.titleLarge)
                },
                modifier = Modifier,
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (imageProfileRef, fullNameRef, usernameRef) = createRefs()

                    BasicImage(
                        model = imageUrl.orEmpty(),
                        modifier = Modifier
                            .clickable {
                                ImageViewerManager.show(imageUrl)
                            }
                            .size(124.dp)
                            .clip(CircleShape)
                            .constrainAs(imageProfileRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    )

                    Text(
                        text = user?.fullname.orEmpty(),
                        style = PosqTypography.titleLarge,
                        modifier = Modifier.constrainAs(fullNameRef) {
                            top.linkTo(imageProfileRef.bottom, 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )

                    val username = "@${user?.username.orEmpty()}"
                    Text(
                        text = username,
                        modifier = Modifier.constrainAs(usernameRef) {
                            top.linkTo(fullNameRef.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserMenuItem(icon = Icons.Filled.Phone, text = user?.phone.orEmpty())
                        UserMenuItem(icon = Icons.Filled.Email, text = user?.email.orEmpty())
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier,
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        val (settingLabelRef, settingRef, iconRef, modeRef, switchRef) = createRefs()

                        Text(
                            text = "Pengaturan",
                            style = PosqTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.constrainAs(settingLabelRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                        )

                        Column(modifier = Modifier.constrainAs(settingRef) {
                            top.linkTo(settingLabelRef.bottom, 8.dp)
                            start.linkTo(parent.start); end.linkTo(parent.end)
                        }, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            UserMenuItem(
                                icon = Icons.Default.Edit,
                                text = "Ubah Profil",
                                showArrow = true,
                                modifier = Modifier.clickable(onClick = onClickEdit)
                            )
                            UserMenuItem(
                                icon = Icons.Default.Password,
                                text = "Ganti Kata Sandi",
                                showArrow = true,
                                modifier = Modifier.clickable(onClick = onChangePassword)
                            )
                        }

                        IconBlock(
                            icon = Icons.Filled.LightMode,
                            startColor = Sage,
                            endColor = Sage,
                            size = 40.dp,
                            iconSize = 24.dp,
                            modifier = Modifier.constrainAs(iconRef) {
                                top.linkTo(settingRef.bottom, 8.dp)
                                start.linkTo(parent.start)
                            }
                        )
                        val mode = if (isDarkMode) "Gelap" else "Terang"
                        Text(
                            text = "Mode $mode",
                            style = PosqTypography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.constrainAs(modeRef) {
                                top.linkTo(iconRef.top)
                                bottom.linkTo(iconRef.bottom)
                                start.linkTo(iconRef.end, 16.dp)
                            }
                        )

                        themeManager?.let {
                            ThemeToggleScreen(
                                themeManager = it,
                                modifier = Modifier.fillMaxWidth().constrainAs(switchRef) {
                                    start.linkTo(iconRef.start)
                                    top.linkTo(iconRef.bottom, 16.dp)
                                })
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        // Show Logout Dialog
                        showLogoutDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    border = BorderStroke(width = 1.dp, color = Red)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(Res.string.logout),
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = Red
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserMenuItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    showArrow: Boolean = false
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconBlock(
                icon = icon,
                startColor = Sage,
                endColor = Sage,
                size = 40.dp,
                iconSize = 24.dp,
                modifier = Modifier
            )
            Text(
                text = text,
                style = PosqTypography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                modifier = Modifier
            )
        }
        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ThemeToggleScreen(modifier: Modifier, themeManager: ThemeManager) {
    val themeMode by themeManager.themeMode.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Select Theme:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.clickable { themeManager.setTheme(mode) }
                ) {
                    RadioButton(
                        selected = themeMode == mode,
                        onClick = { themeManager.setTheme(mode) })
                    Text(text = mode.name)
                }
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun PreviewProfileContent() {
    PosqTheme(useDynamicColor = false) {
        ProfileContent(
            user = UserUIData(
                1, "admin1", "Admin 1", "admin1@pos.com", "087822882668",
                role = "admin",
                imageProfileUrl = "",
                isActive = true
            ),
            isLoginUser = true,
            isDarkMode = false,
            themeManager = null,
            onLogout = {},
            imageUrl = "",
            onClickEdit = { },
            onChangePassword = {}
        )
    }
}
