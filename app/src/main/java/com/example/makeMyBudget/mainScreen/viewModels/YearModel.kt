package com.example.makeMyBudget.mainScreen.viewModels

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.example.makeMyBudget.mainScreen.transactionLibrary.MonthAdapter
import com.example.makemybudget.R

@EpoxyModelClass
abstract class YearModel : EpoxyModelWithHolder<YearModel.holder>() {
    override fun getDefaultLayout(): Int {
        return R.layout.epoxy_years
    }

    @EpoxyAttribute
    lateinit var year: String

    @EpoxyAttribute
    lateinit var adapter: MonthAdapter

    override fun bind(holder: holder) {
        super.bind(holder)
        holder.view.text = year
        holder.list.adapter = adapter
        holder.list.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(holder.view.context)
        holder.button.setOnClickListener {
            if (holder.list.visibility == View.GONE) {
                holder.list.visibility = View.VISIBLE
                holder.button.setImageResource(R.drawable.ic_baseline_collapse_up)
            } else {
                holder.list.visibility = View.GONE
                holder.button.setImageResource(R.drawable.ic_baseline_expand_down)
            }
        }
    }

    inner class holder : EpoxyHolder() {
        lateinit var view: TextView
        lateinit var button: ImageView
        lateinit var list: RecyclerView

        override fun bindView(itemView: View) {
            view = itemView.findViewById(R.id.year)
            button = itemView.findViewById(R.id.drop_down_button)
            list = itemView.findViewById(R.id.view)
        }
    }

}