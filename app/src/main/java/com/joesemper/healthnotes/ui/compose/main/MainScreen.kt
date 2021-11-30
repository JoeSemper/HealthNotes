package com.joesemper.healthnotes.ui.compose.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.joesemper.healthnotes.R
import com.joesemper.healthnotes.data.model.HealthData
import com.joesemper.healthnotes.ui.theme.*
import com.joesemper.healthnotes.utils.getDateByMillisecondsTextMonth
import com.joesemper.healthnotes.utils.getDatesList
import com.joesemper.healthnotes.utils.getTimeByMilliseconds
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@Composable
fun MainScreen(viewModel: MainViewModel = getViewModel()) {

    val state by viewModel.currentContent

    val dialogState = rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        floatingActionButton = {
            MainFab(onClick = { dialogState.value = true })
        },
        floatingActionButtonPosition = FabPosition.End,
        backgroundColor = backgroundColor
    ) {
        NewDataDialog(dialogState = dialogState, viewModel = viewModel)
        Crossfade(targetState = state) { data ->
            UserHealthData(data = data)
        }
    }
}

@Composable
fun MainFab(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
            contentDescription = ""
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun UserHealthData(
    data: List<HealthData>,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        when {
            data.isNotEmpty() -> {
                getDatesList(data).forEach { date ->
                    item {
                        ItemDate(text = date)
                    }
                    items(items = data
                        .filter { healthData ->
                            getDateByMillisecondsTextMonth(healthData.time) == date
                        }
                        .sortedByDescending { it.time }
                    ) { healthData ->
                        ItemHealthData(data = healthData)
                    }
                }
            }
            data.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                }
            }
        }
    }
}

@Composable
fun ItemDate(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
            shape = RoundedCornerShape(24.dp),
            color = Teal200
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                text = text,
                color = Color.White
            )
        }
    }
}

@Composable
fun ItemHealthData(modifier: Modifier = Modifier, data: HealthData) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        elevation = 4.dp
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (time, topPressure, bottomPressure, divider, pulse, pulseIcon) = createRefs()
            Text(
                modifier = Modifier.constrainAs(divider) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    absoluteRight.linkTo(parent.absoluteRight)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = stringResource(R.string.divider),
                color = supportTextColor,
                fontSize = 32.sp
            )
            Text(
                modifier = Modifier.constrainAs(time) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    absoluteLeft.linkTo(parent.absoluteLeft, 16.dp)
                },
                text = getTimeByMilliseconds(data.time),
                color = supportTextColor,
                fontSize = 14.sp
            )

            Text(
                modifier = Modifier.constrainAs(topPressure) {
                    absoluteRight.linkTo(divider.absoluteLeft, 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = data.pressureTop.toString(),
                color = primaryTextColor,
                fontSize = 32.sp
            )

            Text(
                modifier = Modifier.constrainAs(bottomPressure) {
                    absoluteLeft.linkTo(divider.absoluteRight, 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = data.pressureBottom.toString(),
                color = primaryTextColor,
                fontSize = 32.sp
            )

            Icon(
                modifier = Modifier.constrainAs(pulseIcon) {
                    absoluteRight.linkTo(parent.absoluteRight, 64.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                tint = secondaryTextColor,
                painter = painterResource(id = R.drawable.ic_baseline_favorite_24),
                contentDescription = ""
            )

            Text(
                modifier = Modifier.constrainAs(pulse) {
                    absoluteLeft.linkTo(pulseIcon.absoluteRight, 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = data.pulse.toString(),
                color = secondaryTextColor,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun NewDataDialog(dialogState: MutableState<Boolean>, viewModel: MainViewModel) {
    if (dialogState.value) {
        Dialog(onDismissRequest = { dialogState.value = false }) {
            Card(
                modifier = Modifier
                    .width(400.dp)
                    .height(300.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = "New Note"
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.topPressure.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            viewModel.topPressure.value = it
                        },
                        label = {
                            Text(text = "Top pressure")
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.bottomPressure.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            viewModel.bottomPressure.value = it
                        },
                        label = {
                            Text(text = "Bottom pressure")
                        }
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        value = viewModel.pulse.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            viewModel.pulse.value = it
                        },
                        label = {
                            Text(text = "Pulse")
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = { dialogState.value = false }
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = {
                                viewModel.addNewData()
                                dialogState.value = false
                            }
                        ) {
                            Text(text = "Crate")
                        }
                    }
                }
            }
        }
    }
}