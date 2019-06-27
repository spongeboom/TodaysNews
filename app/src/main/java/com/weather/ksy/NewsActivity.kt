package com.weather.ksy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.news_items.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var newsList: ArrayList<TotalNews.Articles>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        newsList = ArrayList()
//        progress_bar.visibility = View.VISIBLE
        news_recycler_view.setHasFixedSize(true)
        news_recycler_view.layoutManager = LinearLayoutManager(this)
        news_recycler_view.adapter = newsRecyclerViewAdapter()
//        getNews()
        swipe_layout.setOnRefreshListener(this)

        news_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getNews(parent?.getItemAtPosition(position).toString())
                progress_bar.visibility = View.VISIBLE
            }
        }
    }

    override fun onRefresh() {
        Toast.makeText(this, "새로운 뉴스를 가져오는중..", Toast.LENGTH_SHORT).show()
        getNews(news_type_spinner.selectedItem.toString())
        swipe_layout.isRefreshing = false
    }

    private fun getNews(category:String) {
        Log.d("TAG",category)
        (application as WeatherApplication)
            .requestNews()
            ?.getNews(
                category = category,
                apiKey = getString(R.string.news_api_key)
            )
            ?.enqueue(object : Callback<TotalNews> {
                override fun onFailure(call: Call<TotalNews>, t: Throwable) {
                    progress_bar.visibility = View.GONE
                    Toast.makeText(this@NewsActivity, t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<TotalNews>, response: Response<TotalNews>) {
                    if (response.isSuccessful) {
                        newsList?.clear()
                        val totalNews = response.body()
                        totalNews?.articleList?.forEach {
                            Log.d("CONTENT", it.title)
                        }
                        Log.d("CONTENT",totalNews?.articleList?.size.toString())
                        totalNews?.let {
                            newsList = it.articleList
                            newsRecyclerViewAdapter().notifyDataSetChanged()
                            progress_bar.visibility = View.GONE
                        }
                    } else {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@NewsActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private inner class newsRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_items, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(var view: View?) : RecyclerView.ViewHolder(view!!)

        override fun getItemCount(): Int {
            return newsList!!.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewholder = (holder as CustomViewHolder).itemView
            Glide.with(viewholder.context).load(newsList!![position].urlToImage).into(viewholder.news_item_image_title)
            viewholder.news_item_text_title.text = newsList!![position].title
            viewholder.news_item_text_content.text = newsList!![position].description
            viewholder.setOnClickListener {
                var newsintent = Intent(this@NewsActivity, NewsContentActivity::class.java)
                newsintent.putExtra("contentUrl", newsList!![position].url)
                newsintent.putExtra("contentTitle", newsList!![position].title)
                startActivity(newsintent)
            }
        }
    }

}
