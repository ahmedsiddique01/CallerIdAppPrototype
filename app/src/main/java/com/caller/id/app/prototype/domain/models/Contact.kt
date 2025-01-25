package com.caller.id.app.prototype.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.io.Serializable

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "number")
    val number: String,
    var isBlocked: Boolean = false
):Serializable{
    override fun toString(): String {
        return "Contact(id='$id', name='$name', number='$number', isBlocked=$isBlocked)"
    }
}
