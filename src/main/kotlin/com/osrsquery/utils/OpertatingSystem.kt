package com.osrsquery.utils

enum class OperatingSystem {
    Windows, MacOS, Linux, Other;

    companion object {

        var type = ""

        var OS = run {
            type = System.getProperty("os.name").lowercase()
            operatingSystemType()
        }

        fun isWindows() = OS == Windows
        fun isMac() = OS == MacOS
        fun isLinux() = OS == Linux
        fun isOther() = OS == Other

        private fun operatingSystemType() = when {
            type.contains("mac") || type.contains("darwin") -> MacOS
            type.contains("win") -> Windows
            type.contains("nux") -> Linux
            else -> Other
        }
    }

}