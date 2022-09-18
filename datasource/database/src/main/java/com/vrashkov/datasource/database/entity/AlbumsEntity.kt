package com.vrashkov.datasource.database.entity

import io.realm.kotlin.types.RealmObject

class AlbumsEntity : RealmObject {
    var index: Int = 1
    var id: String = ""
    var albumUrl: String = ""
    var artistName: String = ""
    var albumName: String = ""
    var genres: String = ""
    var image: String = ""
    var releaseDate: String = ""
}
