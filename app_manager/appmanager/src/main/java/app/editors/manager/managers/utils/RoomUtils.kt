package app.editors.manager.managers.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.editors.manager.R


data class RoomInfo(@DrawableRes val icon: Int, @StringRes val title: Int, @StringRes val description: Int)
object RoomUtils {

    fun getRoomInfo(type: Int): RoomInfo {
        val icon = when (type) {
            2 -> R.drawable.ic_collaboration_room
            6 -> R.drawable.ic_public_room
            5 -> R.drawable.ic_custom_room
            else -> {
                -1
            }
        }
        val title = when (type) {
            2 -> R.string.rooms_add_collaboration
            6 -> R.string.rooms_add_public_room
            5 -> R.string.rooms_add_custom
            else -> {
                -1
            }
        }
        val des = when (type) {
            2 -> R.string.rooms_add_collaboration_des
            6 -> R.string.rooms_add_public_room_des
            5 -> R.string.rooms_add_custom_des
            else -> {
                -1
            }
        }
        return RoomInfo(icon, title, des)
    }

}