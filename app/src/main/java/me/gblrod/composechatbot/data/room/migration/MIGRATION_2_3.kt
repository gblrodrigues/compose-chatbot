package me.gblrod.composechatbot.data.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
                ALTER TABLE chat_messages
                ADD COLUMN image_path TEXT
            """.trimIndent()
        )
    }
}