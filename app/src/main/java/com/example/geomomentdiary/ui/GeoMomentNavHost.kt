package com.example.geomomentdiary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.geomomentdiary.ui.map.MapScreen

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object AddNote : Screen("add_note")

    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(id: Int) = "edit_note/$id"
    }

    object DetailNote : Screen("detail_note/{noteId}") {
        fun createRoute(id: Int) = "detail_note/$id"
    }

    object NotesList : Screen("notes_list")

    object Map : Screen("map")
}

@Composable
fun GeoMomentNavHost(
    viewModel: GeoMomentViewModel
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // HOME
        composable(Screen.Home.route) {
            HomeScreen(
                onBuatCatatan = {
                    navController.navigate(Screen.AddNote.route)
                },
                onCatatanSaya = {
                    navController.navigate(Screen.NotesList.route)
                },
                onPetaMomen = {
                    navController.navigate(Screen.Map.route)
                }
            )
        }

        // ADD
        composable(Screen.AddNote.route) {
            AddEditNoteScreen(
                viewModel = viewModel,
                noteId = null,
                onDone = {
                    navController.popBackStack()
                }
            )
        }

        // EDIT
        composable(
            route = Screen.EditNote.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments?.getInt("noteId")

            AddEditNoteScreen(
                viewModel = viewModel,
                noteId = noteId,
                onDone = {
                    navController.popBackStack()
                }
            )
        }

        // LIST
        composable(Screen.NotesList.route) {
            NotesListScreen(
                viewModel = viewModel,
                onNoteClick = { note ->
                    navController.navigate(
                        Screen.DetailNote.createRoute(note.id)
                    )
                }
            )
        }

        // DETAIL
        composable(
            route = Screen.DetailNote.route,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments?.getInt("noteId")

            val moments by viewModel.moments.collectAsState(
                initial = emptyList()
            )

            val moment = moments.firstOrNull {
                it.id == noteId
            }

            if (moment != null) {

                DetailNoteScreen(
                    moment = moment,

                    onBack = {
                        navController.popBackStack()
                    },

                    onEdit = {
                        navController.navigate(
                            Screen.EditNote.createRoute(moment.id)
                        )
                    },

                    onDelete = {
                        viewModel.deleteMoment(moment)
                        navController.popBackStack()
                    }
                )

            }

        }

        // MAP
        composable(Screen.Map.route) {
            MapScreen(
                viewModel = viewModel,
                onLocationClick = { id ->
                    navController.navigate(
                        Screen.DetailNote.createRoute(id)
                    )
                }
            )
        }
    }
}