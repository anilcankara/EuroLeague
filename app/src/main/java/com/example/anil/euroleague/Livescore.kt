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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livescore.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.util.*

class Livescore : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_livescore)

        val livescores = ArrayList<Game>()

        val retrofit = Retrofit.Builder().baseUrl("https://allsportsapi.com/api/basketball/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EuroleagueAPI::class.java)

        val time = LocalDateTime.now().toString().substring(0,10)
        service.getLivescores(time, time).enqueue(object: Callback<EuroleagueLiveScoreResponse>{
            override fun onFailure(call: Call<EuroleagueLiveScoreResponse>, t: Throwable) {
                Log.e("error", "Something went wrong with Euroleague API.")
            }

            override fun onResponse(call: Call<EuroleagueLiveScoreResponse>, response: Response<EuroleagueLiveScoreResponse>) {
                val data = response.body()

                // API cevap vermezse nolacağını da burda kontrol edebilirsin
                if(data!!.games!=null){

                    for(element in data.games){
                        livescores.add(element)
                    }
                    val noMatches : TextView = findViewById(R.id.noMatches)
                    val angry : ImageView = findViewById(R.id.angry)
                    noMatches.setVisibility(View.GONE)
                    angry.setVisibility(View.GONE)
                }
                else{
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    val recycler: RecyclerView = findViewById(R.id.logoRecycler)
                    recycler.visibility = View.GONE
                    val noMatches : TextView = findViewById(R.id.noMatches)
                    noMatches.setText(R.string.noMatches)
                    noMatches.setBackgroundColor(Color.BLACK)
                    val angry : ImageView = findViewById(R.id.angry)
                    angry.setBackgroundResource(R.drawable.angry)
                }

                fun selector(element: Game): String = element.event_time
                livescores.sortBy({selector(it)})

                logoRecycler.addItemDecoration(DividerItemDecoration(this@Livescore, LinearLayoutManager.VERTICAL))

                logoRecycler.layoutManager = LinearLayoutManager(this@Livescore)
                logoRecycler.setHasFixedSize(true) //to improve performance
                logoRecycler.adapter = LivescoreAdapter(livescores, applicationContext)


            }

        })


    }
}

class LivescoreAdapter(private val data: List<Game>, context: Context) : RecyclerView.Adapter<LivescoreAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): LivescoreAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.livescore_item, parent, false )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: LivescoreAdapter.MyViewHolder, position: Int) {
        val game: Game = data.get(position)
        holder.time.setText(game.event_time)
        holder.homeTeam.setText(game.event_home_team)
        holder.awayTeam.setText(game.event_away_team)
        holder.score.setText(game.event_final_result)
        if(game.event_status.equals("Finished")){
            holder.score.setBackgroundColor(Color.RED)
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
        val time :TextView = view.findViewById(R.id.time)
        val homeTeam :TextView = view.findViewById(R.id.homeTeam)
        val homeTeamLogo: ImageView = view.findViewById(R.id.homeTeamLogo)
        val awayTeamLogo: ImageView = view.findViewById(R.id.awayTeamLogo)
        val awayTeam :TextView = view.findViewById(R.id.awayTeam)
        val score: TextView = view.findViewById(R.id.score)
    }

}

