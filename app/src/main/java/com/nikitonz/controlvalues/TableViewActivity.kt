package com.nikitonz.controlvalues

import android.bluetooth.BluetoothAdapter
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.lang.Exception
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.absoluteValue

class TableViewActivity : AppCompatActivity() {



    private lateinit var tableLayout: TableLayout
    private lateinit var resultBar:TextView
    private var rowCount = 1
    private var columnCount = 1
    private lateinit var dataManager: DataManager
    private var isSelectionModeEnabled: Boolean = false
    private val selectedCellCoordinates = mutableListOf<Pair<Int, Int>>()
    private var operationType = OperationType.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_view)

        tableLayout = findViewById(R.id.tableLayout)
        dataManager = DataManager(this)
        val buttonSave = findViewById<ImageButton>(R.id.saveButton)
        buttonSave.setOnClickListener {
            saveDataToManager()
            Toast.makeText(this,"saved data...",Toast.LENGTH_SHORT).show()

        }

        val loadedData = dataManager.loadData()
        if (loadedData.isNotEmpty()) {

            restoreTable(loadedData)

        } else {

            addRowToTable("")
        }
        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.getChildAt(0).setOnClickListener {}
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val hamburgerButton: ImageButton = findViewById(R.id.hamburger)


        hamburgerButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val averageButton = findViewById<LinearLayout>(R.id.averageButton)
        val sumButtonInDrawer = findViewById<LinearLayout>(R.id.sumButton)
        val maxB= findViewById<LinearLayout>(R.id.maxB)
        val minB= findViewById<LinearLayout>(R.id.minB)
        val findAnything= findViewById<LinearLayout>(R.id.searchButton)


        averageButton.setOnClickListener {
            isSelectionModeEnabled=true
            Toast.makeText(this, "Режим разметки включен", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawer(GravityCompat.START)
            operationType=OperationType.AVERAGE
        }

        sumButtonInDrawer.setOnClickListener {
            isSelectionModeEnabled=true
            Toast.makeText(this, "Режим разметки включен", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawer(GravityCompat.START)
            operationType=OperationType.SUM
        }
        val aboutTextView= findViewById<LinearLayout>(R.id.aboutProg)
        aboutTextView.setOnClickListener {
            try{
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                drawerLayout.closeDrawer(GravityCompat.START)
            } catch (e:Exception){
                Log.e("DEBUG_ERR", e.toString())
            }
        }

        maxB.setOnClickListener {
            isSelectionModeEnabled=true
            Toast.makeText(this, "Режим разметки включен", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawer(GravityCompat.START)
            operationType=OperationType.MAX
        }

        minB.setOnClickListener {
            isSelectionModeEnabled=true
            Toast.makeText(this, "Режим разметки включен", Toast.LENGTH_SHORT).show()
            drawerLayout.closeDrawer(GravityCompat.START)
            operationType=OperationType.MIN
        }
        val searchPaneLayout = findViewById<LinearLayout>(R.id.Laja)
        val searchEditText = findViewById<EditText>(R.id.searchPane)
        val closeSearch = findViewById<ImageButton>(R.id.closeSearch)
        closeSearch.setOnClickListener {
            searchPaneLayout.visibility = View.GONE
            colourToStandart()
        }
        findAnything.setOnClickListener {

                drawerLayout.closeDrawer(GravityCompat.START)
                operationType = OperationType.FIND
                searchPaneLayout.visibility = View.VISIBLE

                searchEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        charSequence: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(editable: Editable?) {

                        val searchText = editable.toString()

                        performSearch(searchText)
                    }
                })
                drawerLayout.closeDrawer(GravityCompat.START)
                operationType = OperationType.FIND
        }
        val createUserB= findViewById<LinearLayout>(R.id.createUser)
        createUserB.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        val shareWith= findViewById<LinearLayout>(R.id.shareWith)
        shareWith.setOnClickListener {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (enableBtIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(enableBtIntent, "share"))
            }

        }
        resultBar = findViewById(R.id.resultBar)

    }

    fun performSearch(searchText: String) {
        val tableRows = tableLayout.childCount

        for (i in 0 until tableRows) {
            val row = tableLayout.getChildAt(i) as TableRow
            val cells = row.childCount

            for (j in 0 until cells) {
                val cell = row.getChildAt(j) as TextView
                val cellText = cell.text.toString()


                if (cellText.contains(searchText, ignoreCase = true) and searchText.isNotEmpty()) {

                    cell.setBackgroundColor(Color.YELLOW)
                } else if (i!=0){

                    cell.setBackgroundResource(R.drawable.table_cell_border)
                } else{
                    cell.setBackgroundResource(R.drawable.table_header_cell_border)
                }

            }
        }
    }
    private fun restoreTable(data: Array<Array<String>>) {
        rowCount = data.size
        columnCount = if (data.isNotEmpty()) data[0].size else 1

        for (i in data.indices) {
            val columnTexts = data[i]
            addRowToTable(*columnTexts)
        }


        colourToStandart()
    }
    private fun colourToStandart(){

        val firstRow = tableLayout.getChildAt(0) as? TableRow
        if (firstRow != null) {
            for (j in 0 until firstRow.childCount) {
                val editText = firstRow.getChildAt(j) as? EditText
                editText?.setBackgroundResource(R.drawable.table_header_cell_border)
            }
        }
        if (tableLayout.childCount!=1) {
            for (i in 1 until tableLayout.childCount) {
                val row = tableLayout.getChildAt(i) as TableRow
                for (j in 0 until row.childCount+1) {
                    val editText = row.getChildAt(j) as? EditText
                    editText?.setBackgroundResource(R.drawable.table_cell_border)
                }

            }
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



    private fun addColumnToTable() {

        for (existingRow in tableLayout.children) {
            if (existingRow is TableRow) {
                val emptyEditText = createEditText("")
                val params = TableRow.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.cell_width),
                    resources.getDimensionPixelSize(R.dimen.cell_height)
                )
                emptyEditText.layoutParams = params

                existingRow.addView(emptyEditText)


                addTextChangedListenerForEditText(emptyEditText, existingRow)

            }
        }
    }
    private fun removeRow(row: TableRow) {
        tableLayout.removeView(row)
        rowCount -= 1
    }

    private fun removeColumn(columnIndex: Int) {
        for (i in 0 until tableLayout.childCount) {
            val row = tableLayout.getChildAt(i) as? TableRow
            row?.removeViewAt(columnIndex)
        }
        columnCount -= 1
    }
    private fun createEditText(text: String): EditText {
        val editText = EditText(this)
        editText.setText(text)
        editText.setTextColor(ContextCompat.getColor(this,R.color.black))
        editText.gravity = Gravity.CENTER

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





    private fun addTextChangedListenerForEditText(editText: EditText, row: TableRow) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isLastRow(row)) {
                    rowCount += 1
                    addRowToTable()
                    colourToStandart()
                }

                if (isLastColumn(editText) && !isLastRow(row)) {
                    columnCount += 1
                    addColumnToTable()
                    colourToStandart()
                }


            }

            override fun afterTextChanged(s: Editable?) {

                if (s.isNullOrBlank()) {

                    val hasValuesInRow = (0 until row.childCount)
                        .map { (row.getChildAt(it) as? EditText)?.text?.toString().isNullOrBlank() }
                        .any { !it }


                    val hasValuesInColumn = (0 until tableLayout.childCount)
                        .map {
                            (tableLayout.getChildAt(it) as? TableRow)?.getChildAt(
                                row.indexOfChild(
                                    editText
                                )
                            ) as? EditText
                        }
                        .map { it?.text?.toString().isNullOrBlank() }
                        .any { !it }


                    if (!hasValuesInRow) {
                        removeRow(row)
                    }

                    if (!hasValuesInColumn) {
                        removeColumn(row.indexOfChild(editText))
                    }
                }
            }
        })
//-----------------------------------------------
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (isSelectionModeEnabled && hasFocus) {


                if (selectedCellCoordinates.size < 2) {
                    val currentPair =
                        Pair(tableLayout.indexOfChild(row), row.indexOfChild(editText))
                    if (currentPair.first!=0) {


                        selectedCellCoordinates.add(currentPair)
                        editText.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.pale_orange
                            )
                        )
                    }
                    if (selectedCellCoordinates.size == 2) {


                        isSelectionModeEnabled = false
                        fadeOutSelectedCells()
                        val result = when (operationType) {
                            OperationType.AVERAGE -> Calculations.average(tableLayout, selectedCellCoordinates)
                            OperationType.SUM -> Calculations.sum(tableLayout, selectedCellCoordinates)
                            OperationType.FIND -> TODO()
                            OperationType.MAX -> Calculations.max(tableLayout, selectedCellCoordinates)
                            OperationType.MIN -> Calculations.min(tableLayout, selectedCellCoordinates)
                            OperationType.NONE -> TODO()
                        }

                        resultBar.text = String.format("%s is %s",
                            operationType.toString().lowercase(),
                            (result as? Number)?.let {
                                if ((it.toDouble() % 1).absoluteValue < 1e-10) {
                                    "%.0f".format(it)
                                } else {
                                    "%.3f".format(it)
                                }
                            } ?: result.toString()
                        )
                        Handler(Looper.getMainLooper()).removeCallbacksAndMessages(null)
                        Handler(Looper.getMainLooper()).postDelayed({
                            resultBar.text = ""
                        }, 5000)
                        selectedCellCoordinates.clear()

                    }
                }
            }
        }
    }
//-----------------------------------------------
    private fun fadeOutSelectedCells() {
        val delay = 2000L
        val startColor = ContextCompat.getColor(this, R.color.pale_orange)

        val startPair = selectedCellCoordinates[0]
        val endPair = selectedCellCoordinates[1]

        val startRow = min(startPair.first, endPair.first)
        val endRow = max(startPair.first, endPair.first)
        val startColumn = min(startPair.second, endPair.second)
        val endColumn = max(startPair.second, endPair.second)

        for (i in startRow..endRow) {
            val tableRow = tableLayout.getChildAt(i) as TableRow

            for (j in startColumn..endColumn) {
                val cell = tableRow.getChildAt(j) as EditText
                cell.setBackgroundColor(startColor)
                val transitionDrawable = TransitionDrawable(
                    arrayOf(ColorDrawable(startColor), ContextCompat.getDrawable(this, R.drawable.table_cell_border))
                )
                cell.background = transitionDrawable
                transitionDrawable.startTransition(delay.toInt())
                Handler(Looper.getMainLooper()).postDelayed({
                    cell.setBackgroundResource(R.drawable.table_cell_border)
                    selectedCellCoordinates.clear()
                }, delay)
            }
        }

    }




}