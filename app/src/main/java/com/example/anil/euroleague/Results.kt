package com.example.anil.euroleague

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.Time
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livescore.*
import kotlinx.android.synthetic.main.activity_results.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class Results : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val results = ArrayList<ResGame>()

        val retrofit = Retrofit.Builder().baseUrl("https://allsportsapi.com/api/basketball/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EuroleagueAPI::class.java)

        val time = LocalDateTime.now().toString().substring(0,10)
        val time2 = LocalDateTime.now().plusDays(14).toString().substring(0,10)
        service.getResults(time, time2).enqueue(object: Callback<ResultsResponse> {
            override fun onFailure(call: Call<ResultsResponse>, t: Throwable) {
                Log.e("error", "Something went wrong with Euroleague API.")
            }

            override fun onResponse(call: Call<ResultsResponse>, response: Response<ResultsResponse>) {
                val data = response.body()

                // API cevap vermezse nolacağını da burda kontrol edebilirsin
                if(data!!.games!=null){

                    for(element in data.games){

                        results.add(element)
                    }
                    val noMatches : TextView = findViewById(R.id.noResults)
                    noMatches.setVisibility(View.GONE)
                    val sad : ImageView = findViewById(R.id.sad)
                    sad.setVisibility(View.GONE)
                }
                else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    val recycler: RecyclerView = findViewById(R.id.resultsRecycler)
                    recycler.visibility = View.GONE
                    val noMatches : TextView = findViewById(R.id.noResults)
                    noMatches.setText(R.string.noUpcomingMatches)
                    noMatches.setBackgroundColor(Color.BLACK)
                    val sad : ImageView = findViewById(R.id.sad)
                    sad.setBackgroundResource(R.drawable.sad2)
                }

                fun selector(element: ResGame): String = element.event_date
                results.sortBy({selector(it)})

                resultsRecycler.addItemDecoration(DividerItemDecoration(this@Results, LinearLayoutManager.VERTICAL))
                resultsRecycler.layoutManager = LinearLayoutManager(this@Results)
                resultsRecycler.setHasFixedSize(true) //to improve performance
                resultsRecycler.adapter = ResultsAdapter(results, applicationContext)


            }

        })


    }
}

class ResultsAdapter(private val data: List<ResGame>, context: Context) : RecyclerView.Adapter<ResultsAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ResultsAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.results_item, parent, false )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsAdapter.MyViewHolder, position: Int) {
        val game: ResGame = data.get(position)
        holder.time.setText(game.event_time)

        val tempDate : String = game.event_date
        val textDate : String = tempDate.substring(8,10) + "." + tempDate.substring(5,7) + "." + tempDate.substring(0,4)
        holder.eventDate.setText(textDate)
        holder.homeTeam.setText(game.event_home_team)
        holder.awayTeam.setText(game.event_away_team)
        holder.score.setText(game.event_final_result)
        if(game.event_status.equals("Finished")){
            holder.score.setBackgroundColor(Color.GREEN)
        }
        else if(game.event_status.equals(" ")){
            // Do nothing
        }
        else{
            holder.score.setBackgroundColor(Color.GREEN)
        }
        Picasso.get().load(game.event_home_team_logo).placeholder(R.drawable.euroleaguelogo).into(holder.homeTeamLogo)
        Picasso.get().load(game.event_away_team_logo).placeholder(R.drawable.euroleaguelogo).into(holder.awayTeamLogo)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val time : TextView = view.findViewById(R.id.res_time)
        val eventDate: TextView = view.findViewById(R.id.event_date)
        val homeTeam : TextView = view.findViewById(R.id.res_home)
        val homeTeamLogo: ImageView = view.findViewById(R.id.res_home_logo)
        val awayTeamLogo: ImageView = view.findViewById(R.id.res_away_logo)
        val awayTeam : TextView = view.findViewById(R.id.res_away)
        val score: TextView = view.findViewById(R.id.res_score)
    }

}
