package ru.jobni.jobni.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore


//fun getRealPath(context: Context, uri: Uri): String? {
//    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//    Log.i("URI", uri.toString())
//    val result = uri.toString()
//    // DocumentProvider
//    //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//    if (isKitKat && result.contains("media.documents")) {
//        val ary = result.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        val length = ary.size
//        val imgary = ary[length - 1]
//        val dat = imgary.split("%3A".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//        val docId = dat[1]
//        val type = dat[0]
//        var contentUri: Uri? = null
//        if ("image" == type) {
//            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        } else if ("video" == type) {
//        } else if ("audio" == type) {
//        }
//        val selection = "_id=?"
//        val selectionArgs = arrayOf<String>(dat[1])
//        return getDataColumn(context, contentUri, selection, selectionArgs)
//    } else if ("content".equals(uri.getScheme(), ignoreCase = true)) {
//        return getDataColumn(context, uri, null, null)
//    } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
//        return uri.getPath()
//    }// File
//    return null
//}
//
//fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
//    var cursor: Cursor? = null
//    val column = "_data"
//    val projection = arrayOf(column)
//    try {
//        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
//        if (cursor != null && cursor.moveToFirst()) {
//            val column_index = cursor.getColumnIndexOrThrow(column)
//            return cursor.getString(column_index)
//        }
//    } finally {
//        if (cursor != null)
//            cursor.close()
//    }
//    return null
//}

fun getRealPath(context: Context, uri: Uri): String? {
    var cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    var image_id = cursor?.getString(0)
    image_id = image_id?.substring(image_id.lastIndexOf(":") + 1)
    cursor?.close()
    cursor = context.contentResolver.query(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null,
        MediaStore.Images.Media._ID + " = ? ",
        arrayOf(image_id),
        null
    )
    cursor?.moveToFirst()
    val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
    cursor?.close()
    return path
}