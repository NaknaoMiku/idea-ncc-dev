package com.summer.lijiahao.nccdevtool.devconfig.util.libraries

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFileManager
import com.summer.lijiahao.nccdevtool.base.util.ProjectManager
import com.summer.lijiahao.nccdevtool.devconfig.service.NCCloudEnvSettingService
import java.io.*
import java.util.jar.JarFile

class LibrariesUtil {

    companion object {
        fun setLibrariesAndTip(event: AnActionEvent, homePath: String) {
            val opt = Messages.showYesNoDialog(
                "是否更新类路径？", "询问", Messages.getQuestionIcon()
            )
            if (opt == Messages.OK) {
                setLibraries(event, homePath)
            }
        }

        fun setLibraries(event: AnActionEvent, homePath: String) {

            if (homePath.isEmpty()) {
                throw Exception("请先设置home路径")
            }
            val homeFile = File(homePath)
            if (!homeFile.exists()) {
                throw Exception("home不存在，请检查")
            }

            //nc类路径
            val ncLibraries: List<String> = ClassPathConstant.getNCLibrary()
            //当前工程
            val project: Project = event.project!!

            //首先创建库
            val model = LibraryTablesRegistrar.getInstance().getLibraryTable(project).modifiableModel
            val libraryMap: MutableMap<String, Library> = mutableMapOf()
            for (libraryName in ncLibraries) {
                //根据库名获取库
                var library = model.getLibraryByName(libraryName) as LibraryEx?
                // 库不存在创建新的
                if (library == null) {
                    library = model.createLibrary(libraryName) as LibraryEx
                }
                libraryMap[libraryName] = library
            }

            /*扫描 nc home */
            //设置ant
            val antPath = homePath + File.separator + "ant"
            val antUrl: Set<String> = scanJarAndClasses(antPath, true, false)

            //设置framework
            val frameworkPath = homePath + File.separator + "framework"
            val frameworkList = scanJarAndClasses(frameworkPath, false, false)

            //设置middleware
            val middlewarePath = homePath + File.separator + "middleware"
            val middlewareList = scanJarAndClasses(middlewarePath, false, false)

            //扫描lang目录
            val langPath = homePath + File.separator + "langlib"
            val langList = scanJarAndClasses(langPath, false, false)

            //扫描hotwebs
            val externalPath = homePath + File.separator + "external"
            hotwebEspecial(event, homePath, externalPath, "nccloud", "ncchr") //移动pub_platform到external

            //扫描lib 和 external
            val libPath = homePath + File.separator + "lib"
            val libList = scanJarAndClasses(libPath, false, false)
            val externalList = scanJarAndClasses(externalPath, true, true)
            val productList: MutableSet<String> = mutableSetOf()
            productList.addAll(libList)
            productList.addAll(externalList)

            //扫描ejb目录
            val ejbPath = homePath + "ejb"
            val ejbList = scanJarAndClasses(ejbPath, false, false)

            //扫描resource
            val resourcePath = homePath + File.separator + "resources"
            val resourcesList: MutableSet<String> = mutableSetOf()
            resourcesList.add(resourcePath)

            //扫描modules
            val modulesPath = homePath + File.separator + "modules"
            val moduleMap: Map<String, Set<String>> = scanModules(modulesPath)
            if (moduleMap.isNotEmpty()) {
                for (key in moduleMap.keys) {
                    setLibrary(moduleMap[key]!!, project, libraryMap[key]!!.modifiableModel)
                }
            }

            //设置类路径
            setLibrary(antUrl, project, libraryMap[ClassPathConstant.PATH_NAME_ANT]!!.modifiableModel)
            setLibrary(
                frameworkList,
                project,
                libraryMap[ClassPathConstant.PATH_NAME_FRAMEWORK]!!.modifiableModel
            )
            setLibrary(
                middlewareList,
                project,
                libraryMap[ClassPathConstant.PATH_NAME_MIDDLEWARE]!!.modifiableModel
            )
            setLibrary(
                langList,
                project,
                libraryMap[ClassPathConstant.PATH_NAME_LANG]!!.modifiableModel
            )
            setLibrary(
                productList,
                project,
                libraryMap[ClassPathConstant.PATH_NAME_PRODUCT]!!.modifiableModel
            )
            setLibrary(
                ejbList,
                project,
                libraryMap[ClassPathConstant.PATH_NAME_EJB]!!.modifiableModel
            )

            setLibrary(
                resourcesList,
                project,
                libraryMap[ClassPathConstant.PATH_NAME_RESOURCES]!!.modifiableModel
            )

            WriteCommandAction.runWriteCommandAction(project) { model.commit() }
            setAllModuleLibrary(project)
        }

        private fun setAllModuleLibrary(project: Project) {
            val modules: Array<Module> = ProjectManager().getAllModules(project)
            val libraries: Array<Library> = getProjectLibraries(project)
            for (module in modules) {
                val contentRoots = ModuleRootManager.getInstance(module).contentRoots
                if (contentRoots.isEmpty()) {
                    continue
                }
                for (library in libraries) {
                    if (ModuleRootManager.getInstance(module).modifiableModel.findLibraryOrderEntry(library) == null) {
                        ModuleRootModificationUtil.addDependency(module, library)
                    }
                }
            }
        }

        fun getProjectLibraries(project: Project): Array<Library> {
            val list: List<String> = ClassPathConstant.getNCLibrary()
            val libraries: MutableList<Library> = mutableListOf()
            val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
            for (libName in list) {
                val library = libraryTable.getLibraryByName(libName)
                    ?: throw Exception("ncc libraries缺失！请先到集成配置设置nc类路径\n")
                libraries.add(library)
            }
            return libraries.toTypedArray()
        }


        /**
         * 扫描指定目录下的lib 和classes
         *
         * @param basePath
         * @param libFlag
         * @return
         */
        private fun scanJarAndClasses(basePath: String, libFlag: Boolean, classFlag: Boolean): Set<String> {
            var basePath = basePath
            val pathList: MutableSet<String> = mutableSetOf()
            basePath += File.separator
            if (classFlag) {
                //扫描classes
                val classesPath = basePath + "classes"
                val classesFile = File(classesPath)
                if (classesFile.exists()) {
                    pathList.add(classesPath)
                }
            }
            //扫描lib
            var jarPath = basePath
            if (libFlag) {
                jarPath += "lib"
            }
            val jarFile = File(jarPath)
            if (jarFile.exists()) {
                pathList.add(jarPath)
            }

            //这里暂时不清楚有多少个extra。目前仅仅知道在basepp/MATA-INF下有，因此暂时默认所有的private代码可能会有extra
            var extraPath = basePath
            if (basePath.contains("META-INF")) {
                extraPath += "extra"
                val extraFile = File(basePath)
                if (extraFile.exists()) {
                    pathList.add(extraPath)
                }
            }
            return pathList
        }

        /**
         * hotweb下非ui类jar包转移到external下
         *
         * @param homePath
         * @param externalPath
         * @param webServers
         */
        private fun hotwebEspecial(
            event: AnActionEvent,
            homePath: String,
            externalPath: String,
            vararg webServers: String
        ) {
            val hotwebsPath = homePath + File.separator + "hotwebs"
            for (server in webServers) {
                val hotwebFile =
                    File(hotwebsPath + File.separator + server + File.separator + "WEB-INF" + File.separator + "lib")
                val externalFile = File(externalPath + File.separator + "lib")
                if (!hotwebFile.exists() || !externalFile.exists()) {
                    return
                }
                val files = hotwebFile.listFiles() ?: return
                val isNCCloudFlag = server == "nccloud"
                val jarBuffer = StringBuffer()
                for (file in files) {
                    try {
                        //nccloud的jar需要解压提取鉴权文件
                        if (file.name.endsWith("jar") && !file.name.contains("_src")) {
                            jarBuffer.append(",").append(file.name)
                            if (isNCCloudFlag) {
                                unZip(homePath, file)
                            }
                        }
                        val newFile = File(externalFile.path + File.separator + file.name)
                        if (newFile.exists()) {
                            newFile.delete()
                        }
                        //jar包复制到external/lib下
                        FileUtil.copy(file, newFile)
                        //复制后删除
                        file.delete()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                var str = jarBuffer.toString()
                if (str.startsWith(",")) {
                    str = str.substring(1)
                    when (server) {
                        "nccloud" -> NCCloudEnvSettingService.getInstance(event).nccloudJar = str
                        "ncchr" -> NCCloudEnvSettingService.getInstance(event).ncchrJAR = str
                    }
                }
            }
        }

        /**
         * 扫描nc module
         *
         * @param modulesPath
         * @return
         */
        private fun scanModules(modulesPath: String): Map<String, Set<String>> {
            val jarMap: MutableMap<String, Set<String>> = mutableMapOf()
            val modulesFile = File(modulesPath)
            val modules = modulesFile.listFiles() ?: return jarMap
            val publicLibrarySet: MutableSet<String> = mutableSetOf()
            val privateLibrarySet: MutableSet<String> = mutableSetOf()
            val clientLibrarySet: MutableSet<String> = mutableSetOf()
            for (module in modules) {
                val modulePath = module.path
                val clientPath = modulePath + File.separator + "client"
                val privatePath = modulePath + File.separator + "META-INF"
                publicLibrarySet.addAll(scanJarAndClasses(modulePath, true, true))
                privateLibrarySet.addAll(scanJarAndClasses(privatePath, true, true))
                clientLibrarySet.addAll(scanJarAndClasses(clientPath, true, true))
            }
            jarMap[ClassPathConstant.PATH_NAME_PUBLIC] = publicLibrarySet
            jarMap[ClassPathConstant.PATH_NAME_PRIVATE] = privateLibrarySet
            jarMap[ClassPathConstant.PATH_NAME_CLIENT] = clientLibrarySet
            return jarMap
        }

        /**
         * 读取jar包
         *
         * @param jarFile
         */
        private fun unZip(homePath: String, jarFile: File) {
            val outPath =
                homePath + File.separator + "hotwebs" + File.separator + "nccloud" + File.separator + "WEB-INF" + File.separator + "extend"
            val jar = JarFile(jarFile.path)
            val entries = jar.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.isDirectory) {
                    continue
                }
                val name = entry.name
                if (isConfigResource(name)) {
                    println(name)
                    val inputStream = jar.getInputStream(entry)
                    val `in` = BufferedInputStream(inputStream)
                    val file = File(outPath + File.separator + name)
                    if (!file.exists()) {
                        file.parentFile.mkdirs()
                        file.createNewFile()
                    }
                    val out = BufferedOutputStream(FileOutputStream(file.path))
                    var len = -1
                    val b = ByteArray(1024)
                    while (`in`.read(b).also { len = it } != -1) {
                        out.write(b, 0, len)
                    }
                    `in`.close()
                    out.close()
                }
            }
            jar.close()
        }

        /**
         * 设置依赖库
         *
         */
        private fun setLibrary(urlSet: Set<String>, project: Project, libraryModel: Library.ModifiableModel) {

            //删除旧的库依赖
            val classUrl = libraryModel.getUrls(OrderRootType.CLASSES)
            val sourceUrl = libraryModel.getUrls(OrderRootType.SOURCES)
            val oldUrlSet: MutableSet<String> = mutableSetOf()
            oldUrlSet.addAll(listOf(*classUrl))
            oldUrlSet.addAll(listOf(*sourceUrl))
            for (url in oldUrlSet) {
                libraryModel.removeRoot(url, OrderRootType.CLASSES)
                libraryModel.removeRoot(url, OrderRootType.SOURCES)
            }
            //添加库依赖
            for (url in urlSet) {
                val file = File(url)
                if (file.exists()) {
                    if (!file.name.endsWith("classes") && !file.name.endsWith("resources")) { //非补丁目录,非resources目录
                        libraryModel.addJarDirectory(VirtualFileManager.constructUrl("file", url), false)
                        libraryModel.addJarDirectory(
                            VirtualFileManager.constructUrl("file", url),
                            false,
                            OrderRootType.SOURCES
                        )
                    } else {
                        libraryModel.addRoot(VirtualFileManager.constructUrl("file", url), OrderRootType.CLASSES)
                        libraryModel.addRoot(VirtualFileManager.constructUrl("file", url), OrderRootType.SOURCES)
                    }
                }
            }
            // 提交库变更
            WriteCommandAction.runWriteCommandAction(project) { libraryModel.commit() }
        }

        private fun isConfigResource(name: String): Boolean {
            var flag: Boolean
            if (!name.startsWith("yyconfig")) {
                return false
            }
            flag = name.contains(".xml")
            if (!flag) {
                flag = name.contains(".json")
            }
            if (flag) {
                flag = !isSystemConfig(name)
            }
            return flag
        }

        private fun isSystemConfig(name: String): Boolean {
            val systemNames = arrayOf(
                "yyconfig/configreader/configreader.xml", "log.xml",
                "yyconfig/baseapi/baseapi.xml"
            )
            for (systemName in systemNames) {
                if (name.indexOf(systemName) > -1) {
                    return true
                }
            }
            return false
        }

        fun setModuleLibrary(project: Project?, module: Module) {
            val libraries = getProjectLibraries(project!!)
            if (libraries.isEmpty()) {
                throw Exception("this project is not set ncc libraries!")
            }
            for (library in libraries) {
                if (ModuleRootManager.getInstance(module).modifiableModel.findLibraryOrderEntry(library) == null) {
                    ModuleRootModificationUtil.addDependency(module, library)
                }
            }
        }
    }
}