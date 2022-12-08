package com.summer.lijiahao.nccdevtool.module

import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.openapi.module.ModifiableModuleModel
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.module.StdModuleTypes
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.projectRoots.JavaSdk
import com.intellij.openapi.roots.CompilerModuleExtension
import com.intellij.openapi.roots.LanguageLevelProjectExtension
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtilCore
import java.io.File
import java.io.IOException

class NCCModuleBuilder : ModuleBuilder() {

    var mySourcePaths: List<Pair<String, String>>? = null
    var libraries: Array<Library>? = null
    private var myCompilerOutputPath: String? = null

    override fun getModuleType(): ModuleType<*> {
        return StdModuleTypes.JAVA
    }

    override fun commit(
        project: Project,
        model: ModifiableModuleModel?,
        modulesProvider: ModulesProvider?
    ): MutableList<Module>? {
        val extension = LanguageLevelProjectExtension.getInstance(ProjectManager.getInstance().defaultProject)
        val aDefault = extension.default
        val instance = LanguageLevelProjectExtension.getInstance(project)
        if (aDefault != null && !aDefault) {
            instance.languageLevel = extension.languageLevel
        } else {
            val sdk = ProjectRootManager.getInstance(project).projectSdk
            if (sdk != null) {
                val version = JavaSdk.getInstance().getVersion(sdk)
                if (version != null) {
                    instance.languageLevel = version.maxLanguageLevel
                    instance.default = true
                }
            }
        }
        return super.commit(project, model, modulesProvider)
    }


    override fun setupRootModel(rootModel: ModifiableRootModel) {
        //设置jdk
        val compilerModuleExtension = rootModel.getModuleExtension(
            CompilerModuleExtension::class.java
        )
        compilerModuleExtension.isExcludeOutput = true
        if (myJdk != null) {
            rootModel.sdk = myJdk
        } else {
            rootModel.inheritSdk()
        }

        //设置source目录
        val contentEntry = doAddContentEntry(rootModel)
        if (contentEntry != null) {
            val sourcePaths: List<Pair<String, String>>? = this.getSourcePaths()
            sourcePaths?.let {
                for (path in it) {
                    val moduleLibraryPath = path.getFirst()
                    val sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(
                        FileUtil.toSystemIndependentName(
                            moduleLibraryPath!!
                        )
                    )
                    if (sourceRoot != null) {
                        contentEntry.addSourceFolder(sourceRoot, false, path.getSecond()!!)
                    }
                }
            }
        }

        //设置输出目录
        if (this.myCompilerOutputPath != null) {
            val canonicalPath: String? = try {
                FileUtil.resolveShortWindowsName(this.myCompilerOutputPath!!)
            } catch (e: IOException) {
                this.myCompilerOutputPath
            }
            compilerModuleExtension.setCompilerOutputPath(VfsUtilCore.pathToUrl(canonicalPath!!))
        } else {
            compilerModuleExtension.inheritCompilerOutputPath(true)
        }

        //设置依赖
        if (libraries != null) {
            for (library in libraries!!) {
                rootModel.addLibraryEntry(library)
            }
        }
    }

    private fun getSourcePaths(): List<Pair<String, String>>? {
        return if (mySourcePaths == null) {
            val paths: MutableList<Pair<String, String>> = mutableListOf()
            val entryPath = this.contentEntryPath
            val path = entryPath + File.separator + "src"
            File(path).mkdirs()
            paths.add(Pair.create(path, ""))
            paths
        } else {
            mySourcePaths
        }
    }

}