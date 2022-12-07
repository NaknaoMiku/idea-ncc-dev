package com.summer.lijiahao.nccdevtool.devconfig.util.libraries

object ClassPathConstant {
    const val PATH_NAME_ANT = "Ant_Library"
    const val PATH_NAME_PRODUCT = "Product_Common_Library"
    const val PATH_NAME_MIDDLEWARE = "Middleware_Library"
    const val PATH_NAME_FRAMEWORK = "Framework_Library"
    const val PATH_NAME_PUBLIC = "Module_Public_Library"
    const val PATH_NAME_CLIENT = "Module_Client_Library"
    const val PATH_NAME_PRIVATE = "Module_Private_Library"
    const val PATH_NAME_LANG = "Module_Lang_Library"
    const val PATH_NAME_NCCLOUD = "NCCloud_Library"
    const val PATH_NAME_EJB = "Generated_EJB"
    const val PATH_NAME_RESOURCES = "resources"

    fun getNCLibrary(): List<String> {
        val ncLibraries: MutableList<String> = ArrayList()
        ncLibraries.add(PATH_NAME_ANT)
        ncLibraries.add(PATH_NAME_PRODUCT)
        ncLibraries.add(PATH_NAME_MIDDLEWARE)
        ncLibraries.add(PATH_NAME_FRAMEWORK)
        ncLibraries.add(PATH_NAME_PUBLIC)
        ncLibraries.add(PATH_NAME_CLIENT)
        ncLibraries.add(PATH_NAME_PRIVATE)
        ncLibraries.add(PATH_NAME_LANG)
        ncLibraries.add(PATH_NAME_NCCLOUD)
        ncLibraries.add(PATH_NAME_EJB)
        ncLibraries.add(PATH_NAME_RESOURCES)
        return ncLibraries
    }
}