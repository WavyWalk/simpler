package at.wavywalk.simpler.servletUtils

import at.wavywalk.simpler.router.SimplerServletRequestContext
import java.io.File
import java.io.FileInputStream
import java.net.URI

class PrimitiveFileServer(
        val rootPathOfServedFile: String
) {

    fun serveFile(context: SimplerServletRequestContext) {
        var filePath = "${rootPathOfServedFile}${URI(context.request.requestURI).normalize()}"
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1)
        }
        val file = File(filePath)
        if (!file.exists()) {
            context.response.sendError(404)
            return
        }

        context.response.setHeader("Content-Disposition", "attachment;filename=${file.name}")

        val outputStream = context.response.getOutputStream()
        val inputStream = FileInputStream(file)
        val buffer = ByteArray(4096)

        while(true) {
            val length = inputStream.read(buffer)
            if(length > 0) {
                outputStream.write(buffer, 0, length)
            } else {
                break
            }
        }

        inputStream.close()
        outputStream.flush()
    }


}