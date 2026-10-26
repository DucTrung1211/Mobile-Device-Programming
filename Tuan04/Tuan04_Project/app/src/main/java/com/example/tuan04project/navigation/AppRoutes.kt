package com.example.tuan04project.navigation

object AppRoutes {
    const val HOME = "home"

    const val IN_CLASS_ROOT = "inclass/root"
    const val IN_CLASS_LIST = "inclass/list"
    const val IN_CLASS_DETAIL = "inclass/detail/{id}"

    const val TODO_SPLASH = "todo/splash"
    const val TODO_ONBOARDING = "todo/onboarding"

    const val LIBRARY = "library"

    const val DATA_FLOW_GRAPH = "dataflow"
    const val FORGOT_PASSWORD = "dataflow/forgot_password"
    const val VERIFY_CODE = "dataflow/verify_code"
    const val RESET_PASSWORD = "dataflow/reset_password"
    const val CONFIRM = "dataflow/confirm"

    fun inClassDetail(id: Int): String = "inclass/detail/$id"
}
