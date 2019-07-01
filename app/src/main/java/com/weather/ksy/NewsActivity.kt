package com.weather.ksy

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.weather.ksy.model.TotalNews
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.android.synthetic.main.news_items.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NewsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var newsList: ArrayList<TotalNews.Articles>? = null
    private var adapter:newsRecyclerViewAdapter? = null
    private var selectMode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        newsList = ArrayList()
        adapter = newsRecyclerViewAdapter(newsList!!)

//      spinner
        val spinnerAdapter:ArrayAdapter<String> = ArrayAdapter(this,R.layout.spinner_item, resources.getStringArray(R.array.news_type))
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        news_type_spinner.adapter = spinnerAdapter

//      recycler view
        news_recycler_view.setHasFixedSize(true)
        news_recycler_view.layoutManager = LinearLayoutManager(this)

//       swipe refresh view
        swipe_layout.setOnRefreshListener(this)

        news_type_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectMode = 0
                getNews(parent?.getItemAtPosition(position).toString())
                progress_bar.visibility = View.VISIBLE
            }
        }

        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                selectMode = 1
                progress_bar.visibility = View.VISIBLE
                SearchNews(p0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    override fun onRefresh() {
        Toast.makeText(this, "새로운 뉴스를 가져오는중..", Toast.LENGTH_SHORT).show()
        when(selectMode){
            0 -> {
                getNews(news_type_spinner.selectedItem.toString())
            }
            1 -> {
                SearchNews(search_view.query.toString())
            }
        }
        swipe_layout.isRefreshing = false
    }

    private fun SearchNews(search:String?){
        (application as ServiceApplication)
            .requestNews()
            ?.searchNews(
                query = search!!,
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
                        totalNews?.let {
                            newsList = it.articleList
                            adapter = newsRecyclerViewAdapter(newsList!!)
                            news_recycler_view.adapter = adapter
                            progress_bar.visibility = View.GONE
                        }
                    } else {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@NewsActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private fun getNews(category:String) {
        (application as ServiceApplication)
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
                        totalNews?.let {
                            newsList = it.articleList
                            adapter = newsRecyclerViewAdapter(newsList!!)
                            news_recycler_view.adapter = adapter
                            progress_bar.visibility = View.GONE
                        }
                    } else {
                        progress_bar.visibility = View.GONE
                        Toast.makeText(this@NewsActivity, response.errorBody().toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    private inner class newsRecyclerViewAdapter(var newsItemList:ArrayList<TotalNews.Articles>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var viewholder : View? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_items, parent, false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(var view: View?) : RecyclerView.ViewHolder(view!!)

        override fun getItemCount(): Int {
            return newsItemList.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            viewholder = (holder as CustomViewHolder).itemView
            Glide.with(viewholder?.context!!).load(newsItemList[position].urlToImage).into(viewholder?.news_item_image_title!!)
            viewholder?.news_item_text_title?.text = newsItemList[position].title
            viewholder?.news_item_text_content?.text = newsItemList[position].description

            val currentTime = newsItemList[position].publishedAt
            val date_text = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분",Locale.getDefault()).format(currentTime)
            viewholder?.news_item_text_time?.text = date_text

            viewholder?.setOnClickListener {
                val newsintent = Intent(this@NewsActivity, NewsContentActivity::class.java)
                newsintent.putExtra("contentUrl", newsItemList[position].url)
                newsintent.putExtra("contentTitle", newsItemList[position].title)
                startActivity(newsintent)
            }
        }
    }
}

