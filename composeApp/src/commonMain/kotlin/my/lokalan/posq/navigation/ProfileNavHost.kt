package my.lokalan.posq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.user.adduser.AddUserScreen
import my.lokalan.posq.presentation.user.changepassword.ChangePasswordScreen
import my.lokalan.posq.presentation.user.editprofile.EditProfileScreen
import my.lokalan.posq.presentation.user.profile.ProfileScreen

@Composable
fun ProfileNavHost(rootNavController: NavHostController, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.PROFILE
    ) {
        composable(Screen.BottomNavItem.PROFILE) {
            ProfileScreen(
                rootNavHostController = rootNavController,
                navHostController = navController,
                isLoginUser = true
            )
        }
        composable<Screen.AddUserRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.AddUserRoute>()
            AddUserScreen(
                navController = navController,
                isEdit = args.isEdit,
                userId = args.userId,
                isLoginUser = args.isLoginUser
            )
        }
        composable<Screen.EditProfileRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.EditProfileRoute>()
            EditProfileScreen(
                navHostController = navController,
                userId = args.userId,
                isLoginUser = args.isLoginUser,
            )
        }
        composable<Screen.ChangePasswordRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ChangePasswordRoute>()
            ChangePasswordScreen(
                navHostController = navController,
                userId = args.userId
            )
        }
    }
}
