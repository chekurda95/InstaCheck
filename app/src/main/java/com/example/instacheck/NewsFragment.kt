package com.example.instacheck

import android.content.Context
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ImageDecodeOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder


class NewsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_news_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.news_list).run {
            adapter = NewsListAdapter().apply { notifyDataSetChanged() }
            layoutManager = LinearLayoutManager(context)
        }
    }

    private class NewsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private companion object {
            private const val NEWS_ITEM_TYPE = 0
            private const val MAX_RECYCLED_VIEWS = 100
        }

        private var itemsList: List<String> = listOf(
            "https://www.ixbt.com/img/n1/news/2020/11/3/12-poleznyx-funkciJ-Google-Foto-o-kotoryx-stoit-znat-kazhdomu-polzovatelyu_1537989945_0_large.jpg",
            "https://i.pinimg.com/736x/b3/a6/32/b3a632a5547d22c553075514add449db.jpg",
            "https://i.pinimg.com/736x/e6/1d/40/e61d40897cc347e98fd56849ecfe4208.jpg",
            "https://pechaterfoto.ru/wp-content/uploads/2019/05/%D1%84%D0%BE%D1%82%D0%BE-%D0%BE%D1%82-pechaterfoto.jpg"
        )

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            recyclerView.recycledViewPool.setMaxRecycledViews(NEWS_ITEM_TYPE, MAX_RECYCLED_VIEWS)
        }

        fun setData(list: List<String>) {
            itemsList = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                NEWS_ITEM_TYPE -> NewsViewHolder(parent)
                else -> throw Exception("Unsupported viewType for NewsListAdapter = $viewType")
            }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is NewsViewHolder) {
                holder.bind(itemsList[position])
            }
        }

        override fun getItemViewType(position: Int): Int =
            NEWS_ITEM_TYPE

        override fun getItemCount(): Int = itemsList.size

        private class NewsViewHolder private constructor(
            private val view: NewsCardView
        ) : RecyclerView.ViewHolder(view) {

            constructor(parent: ViewGroup) : this(view = NewsCardView(parent.context))

            fun bind(photoUrl: String) {
                view.setData(photoUrl)
            }
        }

        private class NewsCardView(context: Context) : FrameLayout(context) {

            private val photoView = SimpleDraweeView(context).apply {
                layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }

            init {
                layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                addView(photoView)
            }

            private val controllerListener = object : BaseControllerListener<ImageInfo>() {
                override fun onIntermediateImageSet(id: String?, imageInfo: ImageInfo?) {
                    super.onIntermediateImageSet(id, imageInfo)
                    val lp = photoView.layoutParams
                    lp.height = (photoView.measuredWidth * (imageInfo?.let { it.height.toFloat() / it.width } ?: 1f)).toInt()
                    photoView.layoutParams = lp
                }

                override fun onFinalImageSet(
                    id: String?,
                    imageInfo: ImageInfo?,
                    animatable: Animatable?
                ) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    val lp = photoView.layoutParams
                    lp.height = (photoView.measuredWidth * (imageInfo?.let { it.height.toFloat() / it.width } ?: 1f)).toInt()
                    photoView.layoutParams = lp
                }

                override fun onFailure(id: String?, throwable: Throwable?) {
                    super.onFailure(id, throwable)
                    Log.e("TAGTAG", "NewsCardView loading image failure", throwable)
                }
            }

            fun setData(photoUrl: String) {
                val imageDecodeOptions = ImageDecodeOptions.newBuilder()
                    .setDecodePreviewFrame(true)
                    .build()
                val imageRequest = ImageRequestBuilder.fromRequest(ImageRequest.fromUri(photoUrl))
                    .setImageDecodeOptions(imageDecodeOptions)
                    .setProgressiveRenderingEnabled(true)
                    .build()
                val controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(imageRequest)
                    .setOldController(photoView.controller)
                    .setAutoPlayAnimations(true)
                    .setTapToRetryEnabled(true)
                    .setControllerListener(controllerListener)
                    .build()
                photoView.controller = controller
            }
        }
    }

}