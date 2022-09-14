package com.example.childtaskmanager.core.data.db

import androidx.room.*

abstract class BaseDao<Entity> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(list: List<Entity>): List<Long>

    @Update
    protected abstract suspend fun update(list: List<Entity>)

    @Transaction
    open suspend fun insertOrUpdate(vararg entities: Entity) {
        val entitiesAsList = listOf(*entities)
        val entitiesToUpdate = mutableListOf<Entity>()
        val ids = insert(entitiesAsList)

        for (i in ids.indices) {
            if (ids[i] == -1L) {
                entitiesToUpdate += entitiesAsList[i]
            }
        }

        if (entitiesToUpdate.isNotEmpty()) {
            update(entitiesToUpdate)
        }
    }

    @Delete
    abstract suspend fun delete(vararg entities: Entity)

}