package my.lokalan.posq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import my.lokalan.posq.navigation.Screen
import my.lokalan.posq.presentation.home.member.MemberDetailScreen
import my.lokalan.posq.presentation.user.ListUserScreen
import my.lokalan.posq.presentation.user.adduser.AddUserScreen
import my.lokalan.posq.presentation.user.editprofile.EditProfileScreen

@Composable
fun MemberNavHost(navController: NavHostController, rootNavController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.BottomNavItem.MEMBER,
    ) {
        composable(Screen.BottomNavItem.MEMBER) {
            ListUserScreen(rootNavController = rootNavController, navHostController = navController)
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
        composable<Screen.MemberDetailRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.MemberDetailRoute>()
            MemberDetailScreen(
                navHostController = navController,
                userId = args.userId,
            )
        }
    }
}
