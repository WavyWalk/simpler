package at.wavywalk.simpler.assetsmanagement

import at.wavywalk.simplerconfigurator.anotations.ConfigurationProperty
import kotlin.reflect.KProperty

//TODO: refactor to use special data class
class PublicFolderConfig {

    @ConfigurationProperty
    var pathToPublicDir: String? = "/resources/public"
        set(value) {
            field = ApplicationRootDirProvider.rootDir.toString() + value
        }

    @ConfigurationProperty
    var pathToJsAssetsInPublicDir: String? = "/"

    @ConfigurationProperty
    var pathToCssAssetsInPublicDir: String? = "/"

    @ConfigurationProperty
    var pathToUploads: String? = null

    @ConfigurationProperty
    var publicFolderRoute: String? = "/public"
}

