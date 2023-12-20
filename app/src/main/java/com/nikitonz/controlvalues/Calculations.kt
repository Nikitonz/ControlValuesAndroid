package com.nikitonz.controlvalues

import android.widget.EditText
import android.widget.TableLayout
import android.widget.TableRow
import java.lang.Math.max
import java.lang.Math.min

class Calculations {
    companion object {
        fun prepareList(tableLayout: TableLayout, coordinates: List<Pair<Int, Int>>): MutableList<Double> {
            val values = mutableListOf<Double>()

            if (coordinates.size == 2) {
                val startPair = coordinates[0]
                val endPair = coordinates[1]

                val startRow = min(startPair.first, endPair.first)
                val endRow = max(startPair.first, endPair.first)
                val startColumn = min(startPair.second, endPair.second)
                val endColumn = max(startPair.second, endPair.second)

                for (i in startRow..endRow) {
                    val row = tableLayout.getChildAt(i) as? TableRow

                    for (j in startColumn..endColumn) {
                        val editText = row?.getChildAt(j) as? EditText

                        val text = editText?.text?.toString()
                        val value = text?.toDoubleOrNull()

                        if (value != null && value != 0.0) {
                            values.add(value)
                        }
                    }
                }
            }

            return values
        }

        fun average(tableLayout: TableLayout, coordinates: List<Pair<Int, Int>>): Double {
            val values = prepareList(tableLayout, coordinates)
            return if (values.isNotEmpty()) {
                values.average()
            } else {
                0.0
            }
        }

        fun sum(tableLayout: TableLayout, coordinates: List<Pair<Int, Int>>): Double{
            val values = prepareList(tableLayout, coordinates)
            return if (values.isNotEmpty()) {
                values.sum()
            } else
                0.0
        }

        fun max(tableLayout: TableLayout, coordinates: List<Pair<Int, Int>>):Any{
            val values = prepareList(tableLayout, coordinates)
            return if (values.isNotEmpty()) {
                values.max()
            } else
                "not found"
        }

        fun min(tableLayout: TableLayout, coordinates: List<Pair<Int, Int>>):Any{
            val values = prepareList(tableLayout, coordinates)
            return if (values.isNotEmpty()) {
                values.min()
            } else
                "not found"
        }
    }




}