package com.nikitonz.controlvalues

import android.content.Context
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class DataManager(private val context: Context) {

    private val fileName = "dump.csv"
    private val delimiter = ";"

    // Сохранение данных в файл
    fun saveData(data: Array<Array<String>>) {
        try {
            val file = File(context.filesDir, fileName)
            val writer = BufferedWriter(FileWriter(file))

            for (row in data) {
                writer.write(row.joinToString(delimiter))
                writer.newLine()
            }

            writer.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun loadData(): Array<Array<String>> {
        val data = mutableListOf<Array<String>>()

        try {
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                val reader = BufferedReader(FileReader(file))
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    val values = line?.split(delimiter)?.toTypedArray()
                    if (values != null) {
                        data.add(values)
                    }
                }

                reader.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return data.toTypedArray()
    }
}