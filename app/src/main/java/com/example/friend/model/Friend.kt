package com.example.friend.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
//создаем сущность. Указываем: название таблицы, название столбцов (имена аргументов)
@Entity(tableName = "friendsTable")
class Friend(val name: String, val age: String, val weight: String) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id = 0

    constructor(id: Int, name: String, age: String, weight: String ) : this(name, age, weight) {
        this.id = id
    }
}