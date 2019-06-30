package com.home.colornotedemo.main.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @Entity: 這個物件需要持久化
 * @Id: 這個物件的主鍵
 */
@Entity
data class NoteData(
    @Id var id: Long = 0,
    var lastModifiedTime: String? = null,
    var lastModifiedMonthDay: String? = null,
    var lastModifiedTimestamp: Long? = null,
    var content: String? = null
)
