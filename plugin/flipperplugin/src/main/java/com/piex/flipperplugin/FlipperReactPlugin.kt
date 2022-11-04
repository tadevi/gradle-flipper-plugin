package com.piex.flipperplugin

import com.android.build.gradle.BaseExtension
import com.piex.plugin.BuildConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.DependencyResolutionListener
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ResolvableDependencies

class FlipperReactPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.findProperty("android")
        if (android is BaseExtension) {
            project.gradle.addListener(object : DependencyResolutionListener {
                override fun beforeResolve(p0: ResolvableDependencies) {
                    val compileDeps = project.configurations.getByName("compile").dependencies
                    injectDependencies(compileDeps, project)
                    project.gradle.removeListener(this)
                }

                override fun afterResolve(p0: ResolvableDependencies) {
                }
            })
            android.registerTransform(GradleTransformationImpl(project))
        }
    }

    private fun injectDependencies(compileDeps: DependencySet, project: Project) {
        val projectDeps = project.dependencies
        // inject flipper util
        compileDeps.add(projectDeps.create("io.github.tadevi:flipper-util-native:${BuildConfig.FLIPPER_UTIL_VERSION}"))
    }
}