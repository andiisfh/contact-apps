package com.andiinsanudin.jenius.testapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.andiinsanudin.jenius.testapp.R
import com.andiinsanudin.jenius.testapp.model.ContactModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

/**
 * Created by Andi Insanudin on 2019-06-14.
 */
class ContactAdapter(
    private val context: Context,
    private val list: List<ContactModel>,
    private val callback: (ContactModel) -> Unit,
    private val callbackAdd: (ContactModel) -> Unit,
    private val callbackCopy: (String) -> Unit,
    private val callbackDelete: (ContactModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeItem = 1
    private val typeAdd = 2

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return if(position == itemCount -1){
            typeAdd
        }else{
            typeItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            typeAdd -> addCreateHolder(parent)
            typeItem -> itemCreateHolder(parent)
            else -> itemCreateHolder(parent)
        }
    }

    private fun itemCreateHolder(parent: ViewGroup): RecyclerView.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(inflater, parent)
    }

    private fun addCreateHolder(parent: ViewGroup): RecyclerView.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return AddViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            typeItem -> item(holder, position)
            typeAdd -> add(holder, position)
        }
    }

    private fun item(viewHolder: RecyclerView.ViewHolder, position: Int){
        val holder = viewHolder as ContactViewHolder
        val contact: ContactModel = list[position]
        holder.itemView.setOnClickListener {
            callback(contact)
        }
        holder.mIVCopy?.setOnClickListener {
            callbackCopy(contact.id)
        }
        holder.mIVDelete?.setOnClickListener {
            callbackDelete(contact)
        }
        holder.bind(context, contact)
    }

    private fun add(viewHolder: RecyclerView.ViewHolder, position: Int){
        val holder = viewHolder as AddViewHolder
        val contact: ContactModel = list[position]
        holder.itemView.setOnClickListener {
            callbackAdd(contact)
        }
    }

    class ContactViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(com.andiinsanudin.jenius.testapp.R.layout.item_contact, parent, false)) {
        private var mTVName: TextView? = null
        private var mTVId: TextView? = null
        private var mIVAvatar: ImageView? = null
        var mIVCopy: ImageView? = null
        var mIVDelete: ImageView? = null

        init {
            mTVName = itemView.findViewById(R.id.tv_name)
            mTVId = itemView.findViewById(R.id.tv_id)
            mIVAvatar = itemView.findViewById(R.id.iv_avatar)
            mIVCopy = itemView.findViewById(R.id.iv_copy)
            mIVDelete = itemView.findViewById(R.id.iv_delete)
        }

        fun bind(context: Context, contact: ContactModel) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val defaultBackground = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)

            Glide.with(context).asBitmap().load(contact.photo).listener(object : RequestListener<Bitmap> {
                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).error(defaultBackground).placeholder(circularProgressDrawable).into(mIVAvatar!!)

            mTVName?.text = contact.firstName.plus(" ").plus(contact.lastName)
            mTVId?.text = contact.id
        }
    }

    class AddViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(inflater.inflate(com.andiinsanudin.jenius.testapp.R.layout.item_add, parent, false))
}