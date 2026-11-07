package me.gblrod.composechatbot.data.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.composechatbotDataStore by preferencesDataStore(name = "composechatbot_preferences")