package at.wavywalk.simpler.assetsmanagement

import java.io.File

class AssetsPathProvider(val publicFolderConfig: PublicFolderConfig) {

    init {
        buildJsPaths(publicFolderConfig)
        buildCssPaths(publicFolderConfig)
    }

    lateinit var js: JsAssets
    lateinit var css: CssAssets

    class JsAssets(
            val map: MutableMap<String, String>
    ): MutableMap<String, String> by map

    class CssAssets(
            val map: MutableMap<String,String>
    ): MutableMap<String,String> by map

    private fun buildJsPaths(config: PublicFolderConfig){
        val file: File = config.let {
            File("${it.pathToPublicDir}${it.pathToJsAssetsInPublicDir}")
        }

        val map = mutableMapOf<String, String>()

        if (!file.exists()) {
            println("WARNING: no directory exists at ${config.pathToPublicDir}${config.pathToJsAssetsInPublicDir}")
            js  = JsAssets(map)
            return
        }

        file.listFiles()?.forEach {
            iterateAndMapJsAssets(
                    file = it,
                    map = map,
                    depth = null,
                    config = config
            )
        }

        js = JsAssets(map)
        println("js ass-------")
        map.forEach { k, v ->
            println("$k : $v")
        }
    }

    private fun buildCssPaths(config: PublicFolderConfig) {
        val file: File = config.let {
            File("${it.pathToPublicDir}${it.pathToCssAssetsInPublicDir}")
        }

        val map = mutableMapOf<String, String>()

        if (!file.exists()) {
            println("WARNING: no directory exists at ${config.pathToPublicDir}${config.pathToCssAssetsInPublicDir}")
            css = CssAssets(map)
            return
//            throw Exception("no directory exists at ${config.pathToPublicDir}${config.pathToCssAssetsInPublicDir}")
        }

        file.listFiles()?.forEach {
            iterateAndMapCssAssets(
                    file = it,
                    map = map,
                    depth = null,
                    config = config
            )
        }

        css = CssAssets(map)

    }

    private fun iterateAndMapJsAssets(
            file: File,
            map: MutableMap<String, String>,
            depth: String?,
            config: PublicFolderConfig
    ) {
        if (file.isDirectory) {
            file.listFiles()?.let {
                it.forEach {
                    iterateAndMapJsAssets(
                            it, map, file.name, config
                    )
                }
            }
        } else {
            val splittedFileName = file.name.split(".")

            val fullName: String = depth?.let {
                "${config.publicFolderRoute}${config.pathToJsAssetsInPublicDir}/${depth}/${file.name}"
            } ?: "${config.publicFolderRoute}${config.pathToJsAssetsInPublicDir}/${file.name}"

            val simpleName: String = depth?.let {
                "${it}/${splittedFileName.first()}"
            } ?: splittedFileName.first()

            if (splittedFileName.last() == "gz" || splittedFileName.last() == "map") {
                return
            }

            map[simpleName] = fullName
        }
    }

    private fun iterateAndMapCssAssets(
            file: File,
            map: MutableMap<String, String>,
            depth: String?,
            config: PublicFolderConfig
    ) {
        if (file.isDirectory) {
            file.listFiles()?.let {
                it.forEach {
                    iterateAndMapJsAssets(
                            it, map, file.name, config
                    )
                }
            }
        } else {
            val splittedFileName = file.name.split(".")

            val fullName: String = depth?.let {
                "${config.publicFolderRoute}${config.pathToCssAssetsInPublicDir}/${depth}/${file.name}"
            } ?: "${config.publicFolderRoute}${config.pathToCssAssetsInPublicDir}/${file.name}"

            val simpleName: String = depth?.let {
                "${it}/${splittedFileName.first()}"
            } ?: splittedFileName.first()

            if (splittedFileName.last() == "js" || splittedFileName.last() == "gz" || splittedFileName.last() == "map") {
                return
            }

            map[simpleName] = fullName
        }
    }

}