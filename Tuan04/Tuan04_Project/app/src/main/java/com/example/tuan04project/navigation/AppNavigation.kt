package com.example.tuan04project.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tuan04project.features.dataflow.ui.ConfirmScreen
import com.example.tuan04project.features.dataflow.ui.ForgotPasswordScreen
import com.example.tuan04project.features.dataflow.ui.ResetPasswordScreen
import com.example.tuan04project.features.dataflow.ui.VerifyCodeScreen
import com.example.tuan04project.features.dataflow.viewmodel.DataFlowViewModel
import com.example.tuan04project.features.home.HomeScreen
import com.example.tuan04project.features.inclass.DetailScreen
import com.example.tuan04project.features.inclass.ListScreen
import com.example.tuan04project.features.inclass.RootScreen
import com.example.tuan04project.features.library.ui.LibraryScreen
import com.example.tuan04project.features.todo.data.TodoRepository
import com.example.tuan04project.features.todo.ui.OnboardingRoute
import com.example.tuan04project.features.todo.ui.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    fun openExercise(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun backToHome() {
        navController.popBackStack(AppRoutes.HOME, inclusive = false)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        NavHost(navController = navController, startDestination = AppRoutes.HOME) {
            composable(AppRoutes.HOME) {
                HomeScreen(
                    onOpenTodo = { openExercise(AppRoutes.TODO_SPLASH) },
                    onOpenLibrary = { openExercise(AppRoutes.LIBRARY) },
                    onOpenDataFlow = { openExercise(AppRoutes.DATA_FLOW_GRAPH) },
                    onOpenInClass = { openExercise(AppRoutes.IN_CLASS_ROOT) }
                )
            }

            composable(AppRoutes.TODO_SPLASH) {
                SplashScreen(
                    appInfo = TodoRepository.appInfo,
                    onFinished = {
                        navController.navigate(AppRoutes.TODO_ONBOARDING) {
                            popUpTo(AppRoutes.TODO_SPLASH) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppRoutes.TODO_ONBOARDING) {
                OnboardingRoute(onFinish = { backToHome() })
            }

            composable(AppRoutes.LIBRARY) {
                LibraryScreen()
            }

            navigation(startDestination = AppRoutes.FORGOT_PASSWORD, route = AppRoutes.DATA_FLOW_GRAPH) {
                composable(AppRoutes.FORGOT_PASSWORD) { entry ->
                    val dataFlowViewModel = rememberDataFlowViewModel(navController, entry)
                    ForgotPasswordScreen(
                        email = dataFlowViewModel.email,
                        emailError = dataFlowViewModel.emailError,
                        submittedResult = dataFlowViewModel.submittedResult,
                        onEmailChange = dataFlowViewModel::updateEmail,
                        onNextClick = {
                            if (dataFlowViewModel.submitEmail()) {
                                navController.navigate(AppRoutes.VERIFY_CODE)
                            }
                        }
                    )
                }
                composable(AppRoutes.VERIFY_CODE) { entry ->
                    val dataFlowViewModel = rememberDataFlowViewModel(navController, entry)
                    VerifyCodeScreen(
                        email = dataFlowViewModel.email,
                        otp = dataFlowViewModel.otp,
                        otpError = dataFlowViewModel.otpError,
                        onOtpChange = dataFlowViewModel::updateOtp,
                        onBack = { navController.popBackStack() },
                        onNextClick = {
                            if (dataFlowViewModel.submitOtp()) {
                                navController.navigate(AppRoutes.RESET_PASSWORD)
                            }
                        }
                    )
                }
                composable(AppRoutes.RESET_PASSWORD) { entry ->
                    val dataFlowViewModel = rememberDataFlowViewModel(navController, entry)
                    ResetPasswordScreen(
                        newPassword = dataFlowViewModel.newPassword,
                        confirmPassword = dataFlowViewModel.confirmPassword,
                        passwordError = dataFlowViewModel.passwordError,
                        onNewPasswordChange = dataFlowViewModel::updateNewPassword,
                        onConfirmPasswordChange = dataFlowViewModel::updateConfirmPassword,
                        onBack = { navController.popBackStack() },
                        onNextClick = {
                            if (dataFlowViewModel.submitNewPassword()) {
                                navController.navigate(AppRoutes.CONFIRM)
                            }
                        }
                    )
                }
                composable(AppRoutes.CONFIRM) { entry ->
                    val dataFlowViewModel = rememberDataFlowViewModel(navController, entry)
                    ConfirmScreen(
                        email = dataFlowViewModel.email,
                        otp = dataFlowViewModel.otp,
                        password = dataFlowViewModel.newPassword,
                        confirmError = dataFlowViewModel.confirmError,
                        onBack = { navController.popBackStack() },
                        onSubmitClick = {
                            if (dataFlowViewModel.submit()) {
                                navController.popBackStack(AppRoutes.FORGOT_PASSWORD, inclusive = false)
                            }
                        }
                    )
                }
            }

            composable(AppRoutes.IN_CLASS_ROOT) {
                RootScreen(onPush = { navController.navigate(AppRoutes.IN_CLASS_LIST) })
            }
            composable(AppRoutes.IN_CLASS_LIST) {
                ListScreen(
                    onBack = { navController.popBackStack() },
                    onItemClick = { id -> navController.navigate(AppRoutes.inClassDetail(id)) }
                )
            }
            composable(
                route = AppRoutes.IN_CLASS_DETAIL,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val soThuTu = entry.arguments?.getInt("id") ?: 1
                DetailScreen(
                    soThuTu = soThuTu,
                    onBack = { navController.popBackStack() },
                    onBackToRoot = { navController.popBackStack(AppRoutes.IN_CLASS_ROOT, inclusive = false) }
                )
            }
        }
    }
}

@Composable
private fun rememberDataFlowViewModel(
    navController: NavHostController,
    currentEntry: NavBackStackEntry
): DataFlowViewModel {
    val dataFlowGraphEntry = remember(currentEntry) {
        navController.getBackStackEntry(AppRoutes.DATA_FLOW_GRAPH)
    }
    return viewModel(dataFlowGraphEntry)
}
