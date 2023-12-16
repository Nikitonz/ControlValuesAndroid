package com.nikitonz.controlvalues

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.children

class TableViewActivity : AppCompatActivity() {



    private lateinit var tableLayout: TableLayout
    private var rowCount = 1
    private var columnCount = 1
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_view)

        tableLayout = findViewById(R.id.tableLayout)
        dataManager = DataManager(this)
        var buttonSave = findViewById<ImageButton>(R.id.sumButton)
        buttonSave.setOnClickListener {
            saveDataToManager()
            Toast.makeText(this,"saved data...",Toast.LENGTH_SHORT).show()

        }

        val loadedData = dataManager.loadData()
        if (loadedData.isNotEmpty()) {

            restoreTable(loadedData)
        } else {

            addRowToTable("", "")
        }
    }

    private fun restoreTable(data: Array<Array<String>>) {
        rowCount = data.size
        columnCount = if (data.isNotEmpty()) data[0].size else 1

        for (i in data.indices) {
            val columnTexts = data[i]
            addRowToTable(*columnTexts)
        }
    }

    private fun saveDataToManager() {
        val data = mutableListOf<Array<String>>()

        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as? TableRow
            if (row != null) {
                val rowData = mutableListOf<String>()

                for (j in 0 until row.childCount) {
                    val editText = row.getChildAt(j) as? EditText
                    rowData.add(editText?.text?.toString() ?: "")
                }

                data.add(rowData.toTypedArray())
            }
        }
        dataManager.saveData(data.toTypedArray())
    }



    private fun addRowToTable(vararg columnTexts: String) {
        val row = TableRow(this)
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = params



        var actualColumnCount = 0
        if (columnTexts.isNotEmpty()) {
            actualColumnCount = columnTexts.size
        }

        for (i in 0 until columnCount) {
            val editText = createEditText(if (i < actualColumnCount) columnTexts[i] else "")
            row.addView(editText)


            addTextChangedListenerForEditText(editText, row)
        }

        tableLayout.addView(row)
    }

    private fun createEditText(text: String): EditText {
        val editText = EditText(this)
        editText.setText(text)
        editText.setTextColor(ContextCompat.getColor(this,R.color.black))
        editText.gravity = Gravity.CENTER
        if (rowCount <= 1) {

            editText.setBackgroundResource(R.drawable.table_header_cell_border)
        } else {
            editText.setBackgroundResource(R.drawable.table_cell_border)
        }
        editText.isSingleLine = true

        val params = TableRow.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.cell_width),
            resources.getDimensionPixelSize(R.dimen.cell_height)
        )
        editText.layoutParams = params

        val padding = resources.getDimensionPixelSize(R.dimen.cell_padding)
        editText.setPadding(padding, padding, padding, padding)

        return editText
    }

    private fun isLastRow(row: TableRow): Boolean {
        return row == tableLayout.getChildAt(tableLayout.childCount - 1)
    }

    private fun isLastColumn(editText: EditText): Boolean {
        val columnIndex = (editText.parent as TableRow).indexOfChild(editText)
        return columnIndex == (editText.parent as TableRow).childCount - 1
    }

    private fun addColumnToTable() {
        var coloredCell: Boolean = false
        for (existingRow in tableLayout.children) {
            if (existingRow is TableRow) {
                val emptyEditText = createEditText("")
                val params = TableRow.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.cell_width),
                    resources.getDimensionPixelSize(R.dimen.cell_height)
                )
                emptyEditText.layoutParams = params
                if (!coloredCell) {
                    emptyEditText.setBackgroundResource(R.drawable.table_header_cell_border)
                    coloredCell = !coloredCell
                }
                existingRow.addView(emptyEditText)


                addTextChangedListenerForEditText(emptyEditText, existingRow)
            }
        }
    }


    private fun addTextChangedListenerForEditText(editText: EditText, row: TableRow) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isLastRow(row)) {
                    rowCount += 1
                    addRowToTable()
                }

                if (isLastColumn(editText) && !isLastRow(row)) {
                    columnCount += 1
                    addColumnToTable()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}