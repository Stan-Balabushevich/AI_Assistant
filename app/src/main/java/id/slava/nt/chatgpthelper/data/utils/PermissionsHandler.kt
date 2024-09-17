package id.slava.nt.chatgpthelper.data.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsHandler(private val context: Context) {

    data class Permission(val name: String, val granted: Boolean)

    private var permissions: List<Permission> = emptyList()
    private var onPermissionGranted: ((String) -> Unit)? = null
    private var onPermissionDenied: ((String) -> Unit)? = null

    fun setPermissionCallbacks(
        onGranted: ((String) -> Unit)? = null,
        onDenied: ((String) -> Unit)? = null
    ) {
        onPermissionGranted = onGranted
        onPermissionDenied = onDenied
    }

    fun requestPermission(permissionName: String) {
        val permission = permissions.find { it.name == permissionName }

        if (permission != null && permission.granted) {
            onPermissionGranted?.invoke(permissionName)
        } else {
            val isPermissionGranted = ContextCompat.checkSelfPermission(
                context,
                permissionName
            ) == PackageManager.PERMISSION_GRANTED

            if (isPermissionGranted) {
                onPermissionGranted?.invoke(permissionName)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        permissionName
                    )
                ) {
                    // Explain to the user why the permission is needed
                    // You can show a dialog, toast, or any other UI element to provide an explanation
                    onPermissionDenied?.invoke(permissionName)
                } else {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(permissionName),
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
