package com.demo.gidermanagement.ui.transactions.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.demo.gidermanagement.R
import com.demo.gidermanagement.ui.theme.Color.Blue600

@Composable
fun NumberPanel(
    calculateAction: (String) -> Unit,
    amountText: String,
    saveTransaction: () -> Unit
    ){
    val calculatorList = listOf(
        "7","8","9","4","5","6","1","2","3",",","0","DEL"
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(bottom = 28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Blue600),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    )
                    .size(36.dp),
                painter = painterResource(id = R.drawable.wallet),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = amountText,
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                modifier = Modifier
                    .padding(
                        start = 12.dp,
                        top = 16.dp,
                        end = 12.dp,
                        bottom = 16.dp
                    )
                    .size(36.dp)
                    .clickable { saveTransaction.invoke() },
                painter = painterResource(id = R.drawable.save),
                contentDescription = ""
            )
        }

        LazyVerticalGrid(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.Bottom,
            columns = GridCells.Fixed(NumberPanelDefaults.GridCellCount),

            // content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 12.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(calculatorList.size) { index ->
                    ElevatedButton(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(
                            NumberPanelDefaults.ForNumberPanelCornerShape
                        ),
                        colors = ButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Black
                        ),
                        onClick = { calculateAction.invoke(calculatorList[index]) }
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = calculatorList[index],
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        )
    }
}


object NumberPanelDefaults {
    const val ForNumberPanelCornerShape = 20
    const val GridCellCount = 3
}
@Preview
@Composable
fun PreviewCalculateButton() {
    NumberPanel(
        calculateAction = {},
        "5",
        {}
    )
}
