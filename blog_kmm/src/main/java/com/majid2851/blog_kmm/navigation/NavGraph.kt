package com.majid2851.blog_kmm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController,
)
{
    NavHost(
        navController = navController,
        startDestination =Screen.Home.route ,
    ){
        composable(route=Screen.Home.route){

        }
        composable(route=Screen.Category.route){
            
        }
        composable(route=Screen.Details.route){

        }

    }

}