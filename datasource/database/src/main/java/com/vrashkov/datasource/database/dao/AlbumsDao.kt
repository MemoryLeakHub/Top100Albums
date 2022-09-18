package com.vrashkov.datasource.database.dao

import com.vrashkov.datasource.database.entity.AlbumsEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumsDao
@Inject
constructor(
    private val realm: Realm
){
    private val className = AlbumsEntity::class

    fun getAlbumsByPagingNotify(): Flow<ResultsChange<AlbumsEntity>> {
        return try {
            realm.query(clazz = className).asFlow()
        } catch(ex: Exception) {
            throw ex
        }
    }

    fun getAlbumsByPaging(page: Int, limit: Int): RealmResults<AlbumsEntity> {
        return try {
            val pageCalc = (page-1) * limit
            realm.query(clazz = className, "index >= $pageCalc").limit(limit).find()
        } catch(ex: Exception) {
            throw ex
        }
    }
    fun getById(id: String) : AlbumsEntity? {
        try {

            val album: AlbumsEntity? = realm.query(clazz = className, query = "id == $0", id).first().find()

            return album
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun insertAll(list: List<AlbumsEntity>) {
        realm.write {
            list.forEach { data ->
                copyToRealm(data)
            }
        }
    }

    suspend fun deleteAll() {
        realm.write {
            val albums: RealmResults<AlbumsEntity> = this.query(className).find()
            delete(albums)
        }
    }
}