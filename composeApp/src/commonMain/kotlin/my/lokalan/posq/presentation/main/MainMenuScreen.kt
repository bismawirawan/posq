package my.lokalan.posq.presentation.main

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.home.HomeUiState
import my.lokalan.posq.presentation.home.HomeViewModel
import my.lokalan.posq.presentation.home.SectionState
import my.lokalan.posq.presentation.user.model.UserUIData
import my.lokalan.posq.ui.component.BasicImage
import my.lokalan.posq.ui.component.CardMenu
import my.lokalan.posq.ui.component.PosqScaffold
import my.lokalan.posq.ui.theme.PosqTheme
import my.lokalan.posq.ui.utils.ImageSourceUtils
import my.posq.shared.PosqTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import posq.composeapp.generated.resources.Res
import posq.composeapp.generated.resources.ic_noimage

data class MenuItem(
    val id: String,
    val title: String,
    val imageSource: ImageSourceUtils,
    val onClick: () -> Unit = {}
)

@Composable
fun MainMenuScreen(
    navHostController: NavHostController,
    viewModel: HomeViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Pass a menu click handler that uses the NavHostController to navigate.
    MainMenuContent(
        uiState = uiState,
        onClickProfile = {
            navHostController.navigate(Screen.MainMenuRoute)
        },
        onMenuClick = { menuId ->
            // Navigate to different screens based on menu item clicked
            when (menuId) {
                "transaksi" -> {
                    // Navigate to Add Transaction screen
                    navHostController.navigate(Screen.AddTransactionRoute(isCollective = false))
                }

                "tabungan" -> {
                    // Navigate to savings/transaction screen
                    navHostController.navigate(Screen.AddTransactionRoute(isCollective = true))
                }

                "anggota" -> {
                    // Navigate to member list screen
                    navHostController.navigate(Screen.ListUserRoute)
                }

                "laporan" -> {
                    // Navigate to home/report screen
                    navHostController.navigate(Screen.HomeRoute(justLogin = false))
                }

                "pengaturan" -> {
                    // Navigate to profile/settings screen
                    // Get user ID from current user profile
                    val userId = (uiState.profile as? SectionState.Success)?.data?.id ?: 0
                    navHostController.navigate(
                        Screen.EditProfileRoute(userId = userId, isLoginUser = true)
                    )
                }

                "bantuan" -> {
                    // You can navigate to help screen or show a dialog
                    // For now, navigate to MainMenu as placeholder
                    navHostController.navigate(Screen.MainMenuRoute)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuContent(
    uiState: HomeUiState,
    onClickProfile: () -> Unit = {},
    onMenuClick: (String) -> Unit = {}
) {

    val refreshState = rememberPullToRefreshState()

    // State untuk menu items yang bisa ditambahkan secara dinamis
    var menuItems by remember {
        mutableStateOf(getDefaultMenuItems(onMenuClick))
    }

    // Contoh: Jika ingin load menu berdasarkan user role
    // var menuItems by remember {
    //     val userRole = (uiState.profile as? SectionState.Success)?.data?.role ?: ""
    //     mutableStateOf(getDynamicMenuItems(userRole))
    // }

    // Contoh: Menambahkan menu item secara dinamis
    // menuItems = addMenuItem(
    //     currentItems = menuItems,
    //     newItem = MenuItem(
    //         id = "menu_baru",
    //         title = "Menu Baru",
    //         imageSource = ImageSourceUtils.Remote("https://example.com/image.png"),
    //         onClick = { /* handle click */ }
    //     )
    // )

    // Contoh: Menghapus menu item berdasarkan ID
    // menuItems = removeMenuItem(menuItems, "bantuan")

    PosqScaffold(
        contentWindowInsets = WindowInsets.statusBars
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = {
//                onFetchProfile()
//                if (selectedPeriod != null) {
//                    onPeriodChange(selectedPeriod)
//                } else {
//                    onFetchAllTransaction()
//                }
            },
            state = refreshState,
            modifier = Modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header dengan text dan profile image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp,
                            bottom = 10.dp,
                            start = 16.dp,
                            end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Dashboard",
                        style = PosqTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    BasicImage(
                        model = (uiState.profile as? SectionState.Success)?.data?.imageProfileUrl.orEmpty(),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable {
                                onClickProfile()
                            },
                        placeholder = painterResource(Res.drawable.ic_noimage),
                        error = painterResource(Res.drawable.ic_noimage)
                    )
                }

                // Grid Menu dengan 2 kolom
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(menuItems) { menuItem ->
                        CardMenu(
                            title = menuItem.title,
                            source = menuItem.imageSource,
                            onClick = menuItem.onClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Text(
                    text = "PosQⓒ2026",
                    style = PosqTypography.titleSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.Gray
                )
            }
        }

    }
}

// Fungsi untuk mendapatkan default menu items (statis)
private fun getDefaultMenuItems(onMenuClick: (String) -> Unit = {}): List<MenuItem> {
    return listOf(
        MenuItem(
            id = "transaksi",
            title = "Transaksi",
            imageSource = ImageSourceUtils.Icon(Icons.Default.ShoppingCart),
            onClick = { onMenuClick("transaksi") }
        ),
        MenuItem(
            id = "tabungan",
            title = "Tabungan",
            imageSource = ImageSourceUtils.Icon(Icons.Default.Savings),
            onClick = { onMenuClick("tabungan") }
        ),
        MenuItem(
            id = "anggota",
            title = "Anggota",
            imageSource = ImageSourceUtils.Icon(Icons.Default.Groups),
            onClick = { onMenuClick("anggota") }
        ),
        MenuItem(
            id = "laporan",
            title = "Laporan",
            imageSource = ImageSourceUtils.Icon(Icons.Default.BarChart),
            onClick = { onMenuClick("laporan") }
        ),
        MenuItem(
            id = "pengaturan",
            title = "Pengaturan",
            imageSource = ImageSourceUtils.Icon(Icons.Default.Settings),
            onClick = { onMenuClick("pengaturan") }
        ),
        MenuItem(
            id = "bantuan",
            title = "Bantuan",
            imageSource = ImageSourceUtils.Icon(Icons.Filled.HelpOutline),
            onClick = { onMenuClick("bantuan") }
        )
    )
}

/**
 * Contoh fungsi helper untuk menambahkan menu item secara dinamis
 *
 * Cara menggunakan:
 * 1. Panggil fungsi ini dari action button atau trigger lainnya
 * 2. Pass current list dan item baru yang ingin ditambahkan
 * 3. Return updated list
 *
 * Contoh:
 * ```
 * Button(onClick = {
 *     menuItems = addMenuItem(
 *         currentItems = menuItems,
 *         newItem = MenuItem(
 *             id = "menu_baru",
 *             title = "Menu Baru",
 *             imageSource = ImageSourceUtils.Icon(Icons.Default.Add),
 *             onClick = { /* handle click */ }
 *         )
 *     )
 * }) { Text("Tambah Menu") }
 * ```
 */
fun addMenuItem(currentItems: List<MenuItem>, newItem: MenuItem): List<MenuItem> {
    return currentItems + newItem
}

/**
 * Contoh fungsi helper untuk menghapus menu item berdasarkan ID
 *
 * Cara menggunakan:
 * ```
 * menuItems = removeMenuItem(menuItems, "id_menu_yang_ingin_dihapus")
 * ```
 */
fun removeMenuItem(currentItems: List<MenuItem>, id: String): List<MenuItem> {
    return currentItems.filter { it.id != id }
}

/**
 * Contoh fungsi untuk mendapatkan menu items dari API/Database (dinamis)
 *
 * Implementasi ini bisa disesuaikan dengan kebutuhan:
 * - Fetch dari API
 * - Load dari database lokal
 * - Filter berdasarkan role user
 * - dll
 */
fun getDynamicMenuItems(userRole: String): List<MenuItem> {
    // Contoh: Filter menu berdasarkan role user
    val allMenus = getDefaultMenuItems()

    return when (userRole.lowercase()) {
        "admin" -> allMenus // Admin dapat akses semua menu
        "user" -> allMenus.filter {
            it.id in listOf("transaksi", "tabungan", "bantuan")
        } // User hanya dapat akses menu tertentu
        else -> emptyList()
    }
}

@Preview
@Composable
fun MainMenuContentPreview() {
    PosqTheme(useDynamicColor = false) {
        MainMenuContent(
            uiState = HomeUiState(
                profile = SectionState.Success(
                    UserUIData(
                        id = 1,
                        username = "admin1",
                        fullname = "Admin 1",
                        email = "",
                        phone = "",
                        role = "Admin",
                        imageProfileUrl = "",
                        isActive = true
                    )
                ),
                transactions = SectionState.Success(
                    data = listOf()
                )
            ),
        )
    }
}