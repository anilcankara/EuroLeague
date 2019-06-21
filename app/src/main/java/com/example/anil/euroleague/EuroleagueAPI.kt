package com.example.anil.euroleague
// Deneme satırı
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EuroleagueAPI {
    @GET("?met=Standings&leagueId=821&APIkey=5221630f8a9809edce25ec96318493ec5089a88afb1baf0569f519fb402d80bf")
    fun getStandings() : Call<EuroleagueStandingsResponse>

  /*  @GET("?met=Livescore&APIkey=2b5a52076cc4a033029332f16dbd9dc21ac45dfb91c63573b9ab5b1c7fe7472c")
    fun getLivescores() : Call<EuroleagueLiveScoreResponse> */

    @GET("?met=Fixtures&leagueId=821&APIkey=5221630f8a9809edce25ec96318493ec5089a88afb1baf0569f519fb402d80bf")
    fun getLivescores(@Query("from") day1:String, @Query("to") day2: String) : Call<EuroleagueLiveScoreResponse>

    @GET("?met=Teams&APIkey=5221630f8a9809edce25ec96318493ec5089a88afb1baf0569f519fb402d80bf")
    fun getTeamLogo(@Query("teamId") teamKey:String): Call<LogoResponse>

    @GET("?met=Fixtures&leagueId=821&APIkey=5221630f8a9809edce25ec96318493ec5089a88afb1baf0569f519fb402d80bf")
    fun getResults(@Query("from") day1:String, @Query("to") day2: String) : Call<ResultsResponse>
}

data class ResultsResponse(
    @SerializedName("result")
    val games : ArrayList<ResGame>
)

data class ResGame(
    val event_date: String,
    val event_time: String,
    val event_home_team: String, val home_team_key: String,
    val event_away_team: String, val away_team_key: String,
    val event_final_result: String,
    val event_home_team_logo: String, val event_away_team_logo: String,
    val event_status: String
)
/////////////STANDINGS////////////////////////
data class EuroleagueStandingsResponse(
    @SerializedName("result")
    val allTables : StandingsTables

)

data class StandingsTables(
    @SerializedName("total")
    val totalTable : ArrayList<EuroleagueTeam>,
    @SerializedName("home")
    val homeTable : ArrayList<EuroleagueTeam>,
    @SerializedName("away")
    val awayTable: ArrayList<EuroleagueTeam>

)

data class EuroleagueTeam(
    @SerializedName("standing_place")
    val position : String,
    @SerializedName("standing_team")
    val teamName: String,
    @SerializedName("standing_W")
    val wins : String,
    @SerializedName("standing_L")
    val losses: String,
    @SerializedName("standing_F")
    var points_plus: String,
    @SerializedName("standing_A")
    var points_minus: String,
    val team_key: String,
    var logo: String? = ""
)
////////////LOGOS////////////////////////

data class LogoResponse(
    val result: ArrayList<LogoTeam>
)
data class LogoTeam(
    val team_logo: String
)

////////////LIVESCORES/////////////////

data class EuroleagueLiveScoreResponse(
    @SerializedName("result")
    val games: ArrayList<Game>
)

data class Game(
    val event_date: String,
    val event_time: String,
    val event_home_team: String, val home_team_key: String,
    val event_away_team: String, val away_team_key: String,
    val event_final_result: String,
    val league_round: String,
    val event_status: String,
    //why am I getting an error if I don't put a question mark here?
    //"Parameter specified as non-null is null"
    val event_home_team_logo: String , val event_away_team_logo: String
    // @SerializedName("score")
    //val quarterScores: ArrayList<Quarter>

)