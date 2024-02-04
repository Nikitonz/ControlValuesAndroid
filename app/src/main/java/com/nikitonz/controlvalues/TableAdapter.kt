package com.nikitonz.controlvalues

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TableAdapter(private val context: Context) : RecyclerView.Adapter<TableAdapter.ViewHolder>() {

    private var data: MutableList<MutableList<String>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowData = data[position]
        holder.bind(rowData, position == 0, position)
    }

    override fun getItemCount(): Int = data.size

    fun addRow(rowData: List<String>) {
        val maxColumns = maxOf(data.maxOfOrNull { it.size } ?: 0, rowData.size)

        for (row in data) {
            while (row.size < maxColumns) {
                row.add("")
            }
        }

        data.add(rowData.toMutableList())
        notifyItemInserted(data.size - 1)
    }

    fun addColumn(columnData: List<String>) {
        val columnSize = columnData.size
        val rowCount = data.size

        for (i in 0 until rowCount) {
            if (i < columnSize) {
                data[i].add(columnData[i])
            } else {
                data[i].add("")
            }
        }

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        private var rowIndex: Int = -1
        private var columnIndex: Int = -1

        init {
            itemView.setOnClickListener {
                handleCellClick(adapterPosition)
            }
        }

        fun bind(rowData: List<String>, isHeader: Boolean, position: Int) {
            linearLayout.removeAllViews()

            val columnCount = rowData.size
            val minCellWidth = context.resources.getDimensionPixelSize(R.dimen.cell_width)

            for (i in 0 until columnCount) {
                val text = rowData[i]

                val textView = TextView(context)
                textView.text = text

                val editText = EditText(context)
                editText.setText(text)
                editText.visibility = View.GONE
                editText.maxLines = 1
                editText.ellipsize = TextUtils.TruncateAt.END

                textView.gravity = Gravity.CENTER
                textView.maxLines = 1
                textView.ellipsize = TextUtils.TruncateAt.END

                val layoutParams = LinearLayout.LayoutParams(
                    0,
                    context.resources.getDimensionPixelSize(R.dimen.cell_height),
                    1f
                )

                layoutParams.width = maxOf(layoutParams.width, minCellWidth)

                textView.layoutParams = layoutParams
                editText.layoutParams = layoutParams

                if (isHeader) {
                    textView.setBackgroundResource(R.drawable.table_header_cell_border)
                } else {
                    textView.setBackgroundResource(R.drawable.table_cell_border)
                }

                linearLayout.addView(textView)
                linearLayout.addView(editText)

                if (!isHeader) {
                    editText.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            enableEditMode(position, i)
                        } else {
                            disableEditMode()
                        }
                    }
                }
            }

            rowIndex = position
            columnIndex = -1
        }

        private fun handleCellClick(position: Int) {
            val row = position / data[0].size
            val column = position % data[0].size

            if (row != rowIndex || column != columnIndex) {
                disableEditMode()
                enableEditMode(row, column)
            }
        }

        private fun enableEditMode(row: Int, column: Int) {
            val textView = linearLayout.getChildAt(column * 2) as? TextView
            val editText = linearLayout.getChildAt(column * 2 + 1) as? EditText

            if (textView != null && editText != null) {
                editText.visibility = View.VISIBLE
                textView.visibility = View.GONE
                editText.requestFocus()
                rowIndex = row
                columnIndex = column
            }
        }

        private fun disableEditMode() {
            if (rowIndex != -1 && columnIndex != -1) {
                val textView = linearLayout.getChildAt(columnIndex * 2) as? TextView
                val editText = linearLayout.getChildAt(columnIndex * 2 + 1) as? EditText

                if (textView != null && editText != null) {
                    editText.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                    textView.text = editText.text.toString()

                    data[rowIndex][columnIndex] = editText.text.toString()

                    Toast.makeText(
                        context,
                        "Row: $rowIndex, Column: $columnIndex",
                        Toast.LENGTH_SHORT
                    ).show()

                    editText.text.clear()
                }

                rowIndex = -1
                columnIndex = -1
            }
        }
    }
}