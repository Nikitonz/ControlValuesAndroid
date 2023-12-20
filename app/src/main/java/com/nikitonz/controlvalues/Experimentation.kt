package com.nikitonz.controlvalues

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TableLayout
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity

class Experimentation : AppCompatActivity() {

private lateinit var tableLayout: TableLayout

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_view)

        tableLayout = findViewById(R.id.tableLayout)

        // Create initial table with 2 rows and 2 columns
        addRowToTable("", "")
        addRowToTable("", "")
        var sumButton = findViewById<ImageButton>(R.id.saveButton)
        sumButton.setOnClickListener {
        addColumnToTable()
        }
        }

private fun addRowToTable(column1Text: String, column2Text: String) {
        val row = TableRow(this)
        val params = TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.WRAP_CONTENT
        )
        row.layoutParams = params
        row.setBackgroundResource(R.drawable.table_cell_border)

        val editText1 = createEditText(column1Text)
        val editText2 = createEditText(column2Text)

        row.addView(editText1)
        row.addView(editText2)

        tableLayout.addView(row)

        // Listen for changes in the last column of each row
        editText2.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // If the last column is being edited in any row, add a new column to all rows
        if (isLastColumn(editText2)) {
        addColumnToTable()
        }
        }

        override fun afterTextChanged(s: Editable?) {
        // Check if all cells in the row are empty, then remove the row
        if (isRowEmpty(row)) {
        tableLayout.removeView(row)
        }
        }
        })
        }

private fun addColumnToTable() {
        // Add a new empty cell to each existing row
        for (i in 0 until tableLayout.childCount) {
        val row = tableLayout.getChildAt(i) as TableRow
        val emptyEditText = createEditText("")

        // Set fixed width for the new column
        val params = TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.WRAP_CONTENT
        )
        emptyEditText.layoutParams = params

        row.addView(emptyEditText)

        // Listen for changes in the last column
        emptyEditText.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // If the last column is being edited, add a new column
        if (isLastColumn(emptyEditText)) {
        addColumnToTable()
        }
        }

        override fun afterTextChanged(s: Editable?) {}
        })
        }
        }

private fun createEditText(text: String): EditText {
        val editText = EditText(this)
        editText.setText(text)
        editText.setTextColor(resources.getColor(R.color.black)) // Set text color
        editText.gravity = Gravity.CENTER
        editText.setBackgroundResource(R.drawable.table_cell_border) // Set cell border

        // Set fixed width and height for each cell
        val params = TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.WRAP_CONTENT
        )
        editText.layoutParams = params

        editText.ellipsize = android.text.TextUtils.TruncateAt.END // Ellipsize long text
        editText.isSingleLine = true // Single line
        val padding = resources.getDimensionPixelSize(R.dimen.cell_padding)
        editText.setPadding(padding, padding, padding, padding)
        return editText
        }

private fun isLastColumn(editText: EditText): Boolean {
        val columnIndex = (editText.parent as TableRow).indexOfChild(editText)
        return columnIndex == (editText.parent as TableRow).childCount - 1
        }

private fun isRowEmpty(row: TableRow): Boolean {
        for (i in 0 until row.childCount) {
        val editText = row.getChildAt(i) as EditText
        if (editText.text.toString().isNotEmpty()) {
        return false
        }
        }
        return true
        }
        }