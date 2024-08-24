package com.unicorns.invisible.no65.saveload

import android.annotation.SuppressLint
import android.content.Context
import com.unicorns.invisible.no65.MainActivity
import com.unicorns.invisible.no65.R
import com.unicorns.invisible.no65.init.InitData.Companion.INIT_MAP_INDEX
import com.unicorns.invisible.no65.model.GameState65
import com.unicorns.invisible.no65.model.knowledge.Knowledge
import com.unicorns.invisible.no65.model.lands.RegisteredCells
import com.unicorns.invisible.no65.model.lands.map.LandsMap
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


class SaveManager {
    companion object {
        private val jsonManager = Json {
            serializersModule = RegisteredCells.serializersModule
            allowStructuredMapKeys = true
            ignoreUnknownKeys = true
        }

        // STATE OPERATIONS

        fun saveState(state: GameState65, activity: MainActivity) {
            val json = jsonManager.encodeToString(state)

            activity
                .openFileOutput(SAVE_FILE_NAME, Context.MODE_PRIVATE)
                .bufferedWriter().use { it.write(json) }
        }

        fun loadState(activity: MainActivity): GameState65 {
            val json = activity
                .openFileInput(SAVE_FILE_NAME)
                .bufferedReader()
                .readText()

            return jsonManager.decodeFromString(json)
        }

        fun loadInitState(activity: MainActivity): GameState65 {
            val graphJson = activity
                .resources
                .openRawResource(R.raw.m_init_graph)
                .bufferedReader()
                .readText()

            return GameState65(jsonManager.decodeFromString(graphJson), INIT_MAP_INDEX)
        }

        fun saveExists(activity: MainActivity): Boolean {
            val file = File(activity.filesDir, SAVE_FILE_NAME)
            return file.exists()
        }

        // MAP OPERATIONS

        fun saveMap(activity: MainActivity, map: LandsMap) {
            val savedMapName = "${map.name}$MAP_EXTENSION"
            val mapsDirectory = activity.filesDir.resolve(MAPS_SAVE_DIRECTORY).apply {
                if (!exists()) mkdir()
            }
            val file = File(mapsDirectory, savedMapName).apply {
                if (!exists()) createNewFile()
            }

            val json = jsonManager.encodeToString(map)
            // HERE YOU CAN COPY JSON!!!
            file.writeText(json)
        }

        fun loadMap(file: File): LandsMap {
            if (!file.exists()) {
                return LandsMap("FAILED_TO_LOAD_MAP")
            }
            val json = file.readText()
            return jsonManager.decodeFromString(json)
        }

        @SuppressLint("DiscouragedApi")
        fun loadMapFromRaw(activity: MainActivity, mapName: String): LandsMap {
            val id = activity.resources.getIdentifier(mapName, "raw", activity.packageName)
            val json = activity.resources.openRawResource(id).bufferedReader().readText()
            return jsonManager.decodeFromString(json)
        }

        fun loadMapFiles(activity: MainActivity): Array<File> {
            val mapsDirectory = activity.filesDir.resolve(MAPS_SAVE_DIRECTORY).apply {
                if (!exists()) mkdir()
            }
            return mapsDirectory.listFiles() ?: emptyArray()
        }

        fun loadKnowledgeFromState(activity: MainActivity): Knowledge {
            if (!saveExists(activity)) {
                return Knowledge()
            }
            return loadState(activity).knowledge
        }

        private const val SAVE_FILE_NAME = "game_state.s65"
        private const val BATTLES_FILE_NAME = "battles.s65"

        private const val MAP_EXTENSION = ".M65"
        private const val MAPS_SAVE_DIRECTORY = "maps"
    }
}