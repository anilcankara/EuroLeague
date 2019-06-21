package com.example.anil.euroleague

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import kotlinx.android.synthetic.main.activity_standings.*
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import com.squareup.picasso.Picasso
import java.lang.Exception



class Standings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standings)

        val standingTeams = ArrayList<EuroleagueTeam>()

        val retrofit = Retrofit.Builder().baseUrl("https://allsportsapi.com/api/basketball/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(EuroleagueAPI::class.java)

        service.getStandings().enqueue(object : Callback<EuroleagueStandingsResponse> {
            override fun onResponse(call: Call<EuroleagueStandingsResponse>, response: Response<EuroleagueStandingsResponse>){
                val data = response.body()
                val standings = data!!.allTables.totalTable

                for (element in standings){
                    standingTeams.add(element)
                }

                recyclerView.addItemDecoration(DividerItemDecoration(this@Standings, LinearLayoutManager.VERTICAL))

                recyclerView.layoutManager = LinearLayoutManager(this@Standings)
                recyclerView.setHasFixedSize(true) //to improve performance
                recyclerView.adapter = RecyclerAdapter(standingTeams, applicationContext, retrofit)

            }

            override fun onFailure(call: Call<EuroleagueStandingsResponse>, t:Throwable){
                Log.e("error", "Something went wrong with the API.")
            }

        })


    }

}
class RecyclerAdapter(private val data: List<EuroleagueTeam>, context: Context, private val retrofit: Retrofit) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(){

    val mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.standings_item, parent, false )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val service = retrofit.create(EuroleagueAPI::class.java)
        val team : EuroleagueTeam = data.get(position)
        holder.position.setText(team.position)

        service.getTeamLogo(team.team_key).enqueue(object: Callback<LogoResponse>{
            override fun onFailure(call: Call<LogoResponse>, t: Throwable) {
                Log.e("error", "NOOOOOOOOOOOOOOOO")
            }


            override fun onResponse(call: Call<LogoResponse>, response: Response<LogoResponse>) {

                val data = response.body() //we can add try-catch here
                val logoUrl = data!!.result.get(0).team_logo
                team.logo = logoUrl

                Picasso
                    .get().load(team.logo).placeholder(R.drawable.euroleaguelogo).into(holder.logo, object: com.squareup.picasso.Callback{
                        override fun onSuccess() {
                            Log.d("onSuccess", "Successfully loaded: " + team.logo)
                        }

                        override fun onError(e: Exception?) {
                            Log.d("onFailure", "Cannot load: " + team.logo.toString())
                            e?.printStackTrace()
                        }
                    })
            }
        })

        holder.teamName.setText(team.teamName)
        holder.wins.setText(team.wins)
        holder.losses.text = team.losses
        holder.points_plus.setText(team.points_plus)
        holder.points_minus.setText(team.points_minus)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val position : TextView = view.findViewById(R.id.position)
        val teamName: TextView = view.findViewById(R.id.teamName)
        val wins: TextView = view.findViewById(R.id.wins)
        val losses: TextView = view.findViewById(R.id.losses)
        val points_plus: TextView = view.findViewById(R.id.points_plus)
        val points_minus: TextView = view.findViewById(R.id.points_minus)
        val logo: ImageView = view.findViewById(R.id.logo)

    }
    override fun getItemCount(): Int {
        return data.size
    }

}

