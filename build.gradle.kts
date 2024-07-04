// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.navigationSafeArgs) apply false
    alias(libs.plugins.pluginDaggerHilt) apply false
    alias(libs.plugins.pluginDevKsp) apply false
}