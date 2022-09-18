package com.vrashkov.core.navigation

import android.net.Uri
import androidx.navigation.*
import androidx.navigation.NavType.Companion.BoolType
import androidx.navigation.NavType.Companion.IntType
import androidx.navigation.NavType.Companion.StringType


fun Route.args(map:Map<String, String>) : String {
    var location: String = this.link.substringBefore('?')

    this.arguments.forEach{
        if (map.containsKey(it.name)) {
            if (!location.contains("?")) {
                location += "?" + it.name + "=" + map[it.name];
            } else {
                location += "&" + it.name + "=" + map[it.name];
            }
        }
    }

    return location
}

sealed class Route(val link: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object AlbumsList: Route(link = "albumsList")

    object AlbumsSingle: Route(link = "albumsSingle?id={id}",
        arguments = listOf(
            navArgument("id") {
                nullable = false
                type = IntType
            }
        )
    )
}