package com.test.jcdecaux.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.jcdecaux.R
import com.test.jcdecaux.model.Post
import com.test.jcdecaux.presenter.DetailsActivity
import kotlinx.android.synthetic.main.post_item.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.textColor
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class MainAdapter(var items: List<Post>) : RecyclerView.Adapter<MainAdapter.PostHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false))
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class PostHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Post) = with(itemView) {
            mStatus.text = item.status
            if (item.status == "OPEN")
                mStatus.textColor = Color.GREEN
            else
                mStatus.textColor = Color.RED

            mTitle.text = item.name
            contractName.text = item.contract_name

            itemView.onClick {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra("contract_name", item.contract_name)
                intent.putExtra("address", item.address)
                intent.putExtra("bike_stands", item.bike_stands)
                intent.putExtra("available_bike_stands", item.available_bike_stands)
                intent.putExtra("bonus", item.bonus)
                intent.putExtra("banking", item.banking)
                intent.putExtra("last_update", item.last_update)
                // start your next activity
                context.startActivity(intent)
            }

        }
    }

    private fun getDistanceBetweenPoints(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}